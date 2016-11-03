
/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: front
 * File: PluginServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.action.beans.*;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.utils.json.JsonUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.javabean.PluginFile;
import es.sm2.openppm.core.plugin.PluginResponse;
import es.sm2.openppm.core.plugin.OperationPlugin;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

public class PluginServlet extends AbstractGenericServlet {
	
	private static final long serialVersionUID = 1L;
	
	public final static Logger LOGGER = Logger.getLogger(PluginServlet.class);
	
	public final static String REFERENCE 		= "plugin";
	
	public final static String PLUGIN_DOCUMENT	= "plugin-document";
	public final static String PLUGIN_BEAN		= "plugin-bean";
	public final static String PLUGIN_HTML		= "plugin-html";
	
	/************** Actions AJAX **************/
	public final static String JX_PLUGIN_JSON	= "ajax-plugin-json";
	public final static String JX_PLUGIN_STRING	= "ajax-plugin-string";
    
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	OperationPlugin operationPlugin = null;
    	
    	super.service(req, resp);
    	
		String operationPluginString	= ParamUtil.getString(req, "operationPlugin", getMultipartField("operationPlugin"));
		String pluginAction				= ParamUtil.getString(req, "pluginAction", getMultipartField("pluginAction"));
        String template				    = ParamUtil.getString(req, "template", getMultipartField("template"));
		
		LOGGER.debug("OperationPlugin: " + operationPluginString);


        // Find in package annotation
        Set<Class<?>> configuredClasses = CacheStatic.getPluginActions();

        boolean executed = false;

        if (ValidateUtil.isNotNull(configuredClasses)) {

            // Proccess anotations
            Iterator<Class<?>> iterator = configuredClasses.iterator();

            while (!executed && iterator.hasNext()) {
                Class<?> configuredClass = iterator.next();

                // Get annotation configuration
                PluginAction annotation = configuredClass.getAnnotation(PluginAction.class);

                if (annotation.plugin().equals(operationPluginString) && (annotation.action().equals(pluginAction) || annotation.action().equals(template))) {

                    executed = true;

                    try {
                        GenericAction action = (GenericAction) configuredClass.newInstance();

                        // Add information to plugin
                        setInformationPlugin(req, action);

                        // Call plugin
                        es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse = action.process();

                        // Add session attributes
                        for (String attributeName : pluginResponse.getSessionAttributes().keySet()) {
                            req.getSession().setAttribute(attributeName, pluginResponse.getSessionAttributes().get(attributeName));
                        }

                        // Process specific response
                        if (es.sm2.openppm.core.plugin.action.beans.PluginResponse.ResponseType.BEAN.equals(pluginResponse.getResponseType())) {

                            sendBean(req, resp, pluginResponse);
                        } else if (es.sm2.openppm.core.plugin.action.beans.PluginResponse.ResponseType.FILE.equals(pluginResponse.getResponseType())) {

                            sendFile(req, resp, pluginResponse);
                        } else if (es.sm2.openppm.core.plugin.action.beans.PluginResponse.ResponseType.HTML.equals(pluginResponse.getResponseType())) {

                            sendHtml(req, resp, pluginResponse);
                        } else if (es.sm2.openppm.core.plugin.action.beans.PluginResponse.ResponseType.JSON.equals(pluginResponse.getResponseType())) {

                            sendJSON(req, resp, pluginResponse);
                        } else if (es.sm2.openppm.core.plugin.action.beans.PluginResponse.ResponseType.STRING.equals(pluginResponse.getResponseType())) {

                            sendString(req, resp, pluginResponse);
                        }

                    } catch (InstantiationException e) {
                        LOGGER.error("Plugin creste action", e);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Plugin access action", e);
                    }
                }
            }
        }

        if (!executed) {
            try {

                // Instantiate the operation
                operationPlugin = (OperationPlugin) Class.forName(OperationPlugin.pluginClass(operationPluginString)).newInstance();

                // Add information to plugin
                setInformationPlugin(req, operationPlugin);

            } catch (Exception e) {
                LOGGER.debug(e, e);
            }

            if (SecurityUtil.consUserRole(req) != -1 && operationPlugin != null) {

                /***************** Actions ****************/
                if (PLUGIN_DOCUMENT.equals(pluginAction)) {
                    pluginDocument(req, resp, operationPlugin);
                } else if (PLUGIN_BEAN.equals(pluginAction)) {
                    pluginBean(req, resp, operationPlugin);
                } else if (PLUGIN_HTML.equals(pluginAction)) {
                    pluginHtml(req, resp, operationPlugin);
                }

                /************** Actions AJAX **************/
                else if (JX_PLUGIN_JSON.equals(pluginAction)) {
                    pluginJSON(req, resp, operationPlugin);
                } else if (JX_PLUGIN_STRING.equals(pluginAction)) {
                    pluginStringJX(req, resp, operationPlugin);
                }
            } else if (!isForward()) {
                forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
            }
        }
	}

    private void sendFile(HttpServletRequest req, HttpServletResponse resp, es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse) {

        try {

            // Plugin file
            FileResponse file = (FileResponse)pluginResponse;

            if (file == null || ValidateUtil.isNotNull(file.getError()) || file.getFileBytes() == null) {

                // Message error
                String error = file != null && ValidateUtil.isNotNull(file.getError())
                        ? file.getError()
                        : getResourceBundle(req).getString("msg.error.generating_file");

                // Send error
                forwardServlet(ErrorServlet.REFERENCE+"?accion="
                        + ErrorServlet.ERROR_UNEXPECTED
                        + "&error="
                        + error, req, resp);
            }
            else {

                // Send file
                sendFile(req, resp, file.getFileBytes(), file.getName(), file.getContentType());
            }
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
    }

    private void sendBean(HttpServletRequest req, HttpServletResponse resp, es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse) throws ServletException, IOException {

        BeanResponse beanResponse = (BeanResponse)pluginResponse;

        if (beanResponse != null) {

            // Add beans to response
            if (beanResponse.getData() != null && !beanResponse.getData().isEmpty()) {

                for (String key : beanResponse.getData().keySet()) {

                    req.setAttribute(key, beanResponse.getData().get(key));
                }
            }

            if (ValidateUtil.isNotNull(beanResponse.getInformation())) {
                info(beanResponse.getInformationType(), req, beanResponse.getInformation());
            }

            if (ValidateUtil.isNotNull(beanResponse.getUrlJSP())) {

                forward(beanResponse.getUrlJSP(), req, resp);
            }
            else {
                forwardServlet(beanResponse.getUrlServlet(), req, resp);
            }
        }
        else {
            forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
        }
    }

    private void sendHtml(HttpServletRequest req, HttpServletResponse resp, es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse) throws IOException {

        // Declare writer for send data by AJAX
        PrintWriter out = resp.getWriter();

        try {

            // Send data to client
            out.print(((HtmlResponse)pluginResponse).getData());
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }
    private void sendString(HttpServletRequest req, HttpServletResponse resp, es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse) throws IOException {

        // Declare writer for send data by AJAX
        PrintWriter out = resp.getWriter();

        try {

            if (ValidateUtil.isNotNull(pluginResponse.getError())) {

                // Send Error
                out.print(pluginResponse.getError());
            }
            else {
                // Send data to client
                out.print(((StringResponse)pluginResponse).getData());

            }
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    private void sendJSON(HttpServletRequest req, HttpServletResponse resp, es.sm2.openppm.core.plugin.action.beans.PluginResponse pluginResponse) throws IOException {

        // Declare writer for send data by AJAX
        PrintWriter out = resp.getWriter();

        try {

            if (ValidateUtil.isNotNull(pluginResponse.getError())) {

                // Send Error
                out.print(JsonUtil.toJSON(StringPool.ERROR, pluginResponse.getError()));
            }
            else {
                // Send data to client
                out.print(((JSONResponse)pluginResponse).getData());
            }

        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Load HTML for plugin
     * 
     * @param req
     * @param resp
     * @param operationPlugin
     * @throws IOException 
     */
    private void pluginHtml(HttpServletRequest req, HttpServletResponse resp, OperationPlugin operationPlugin) throws IOException {
		
    	// Declare writer for send data by AJAX
    	PrintWriter out = resp.getWriter();		
		
		try {
			
			String html = StringPool.BLANK;
			
			try {
				
				// Call plugin
				html = operationPlugin.generateTemplateHtml();
			}
			catch (Exception e) { LOGGER.error(e.getMessage(), e); }
			
			// Send data to client
    		out.print(html);		
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }		
		finally { out.close(); }
	}
    
    /**
     * Generate data String 
     * 
     * @param req
     * @param resp
     * @param operationPlugin
     * @throws IOException 
     */
    private void pluginStringJX(HttpServletRequest req, HttpServletResponse resp, OperationPlugin operationPlugin) throws IOException {
		
    	// Declare writer for send data by AJAX
    	PrintWriter out = resp.getWriter();		
		
		try {
			
			String dataString = StringPool.BLANK;
			
			try {

				// Call plugin
				dataString = operationPlugin.pluginString();
			}
			catch (Exception e) { LOGGER.error(e.getMessage(), e.getCause()); e.printStackTrace(); }
			
			// Send data to client
    		out.print(dataString);		
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }		
		finally { out.close(); }
	}

	/**
	 * Send bean to client
	 * 
	 * @param req
	 * @param resp
	 * @param operationPlugin 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void pluginBean(HttpServletRequest req, HttpServletResponse resp, OperationPlugin operationPlugin) throws ServletException, IOException {
		
		PluginResponse pluginResponse = null;
		
		try {
			
			pluginResponse = (PluginResponse) operationPlugin.pluginBean();
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		if (pluginResponse != null) {
			
			req.setAttribute("pluginResponse", pluginResponse.getResponse());
			
			if (ValidateUtil.isNotNull(pluginResponse.getInformation())) {
				info(pluginResponse.getInformationType(), req, pluginResponse.getInformation());
			}
			
			if (ValidateUtil.isNotNull(pluginResponse.getUrlJSP())) {
				
				forward(pluginResponse.getUrlJSP(), req, resp);
			}
			else {
				forwardServlet(pluginResponse.getUrlServlet(), req, resp);
			}
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
	/**
	 * Send file to client
	 * 
	 * @param req
	 * @param resp
	 * @param operationPlugin 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void pluginDocument(HttpServletRequest req, HttpServletResponse resp, OperationPlugin operationPlugin) throws ServletException, IOException {
		
		try {
			
			// Plugin file
			PluginFile file = operationPlugin.pluginDocument();
			
			if (file == null || ValidateUtil.isNotNull(file.getError()) || file.getFileBytes() == null) {
				
				// Message error
				String error = file != null && ValidateUtil.isNotNull(file.getError())
						? file.getError()
						: getResourceBundle(req).getString("msg.error.generating_file");
				
				// Send error
				forwardServlet(ErrorServlet.REFERENCE+"?accion="
						+ ErrorServlet.ERROR_UNEXPECTED
						+ "&error="
						+ error, req, resp);
			}
			else {
				
				// Send file
				sendFile(req, resp, file.getFileBytes(), file.getName(), file.getContentType());
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }	
	}

	/**
	 * Send information in JSON format by AJAX call
	 * 
	 * @param req
	 * @param resp
	 * @param operationPlugin 
	 * @throws ServletException
	 * @throws IOException
	 */
	private void pluginJSON(HttpServletRequest req, HttpServletResponse resp, OperationPlugin operationPlugin) throws ServletException, IOException {
			
		// Declare writer for send data by AJAX
		PrintWriter out = resp.getWriter();		
		
		try {
			
			// Obtain data from plugin
			JSONObject response = operationPlugin.pluginJSON();
			
			if (response == null) {
				// Text of response
				String warning = "Plugin Operation is not supported: "+
						"[Plugin: "+operationPlugin.getPluginName()+" || "+operationPlugin.getParameters().get("accion")+"]";
				
				// Create default response
				LOGGER.warn(warning);
				response = info(StringPool.INFORMATION, warning, new JSONObject());
			}
			
			// Send data to client
    		out.print(response);		
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }		
		finally { out.close(); }
	}
		
   
}
