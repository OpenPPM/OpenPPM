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
 * File: AbstractGenericServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.ContactLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.NotificationLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.PluginLogic;
import es.sm2.openppm.core.logic.impl.SettingLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.plugin.OperationPlugin;
import es.sm2.openppm.core.plugin.PluginLoad;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet implementation class GestorServlet
 */
public abstract class AbstractGenericServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(AbstractGenericServlet.class);
	
	private boolean isForward					= false; 

    private Hashtable<String, FileItem> multipartFields;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set multipart
        if (ServletFileUpload.isMultipartContent(request)) {
            try {

                ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
                List fileItemList = servletFileUpload.parseRequest(request);
                Hashtable<String, FileItem> multipartFields = parseFields(fileItemList);

                this.setMultipartFields(multipartFields);
            }
            catch (Exception e) { ExceptionUtil.evalueException(request, getResourceBundle(request), LOGGER, e); }
        }

		String accion 		= ParamUtil.getString(request, "accion");
		String accionLogin	= ParamUtil.getString(request, "a");
		String scrollTop	= ParamUtil.getString(request, "scrollTop", null);
		setForward(false);
		
		if (scrollTop != null) { request.setAttribute("scrollTop", scrollTop); }
		
		// Add information to response
		SettingLogic logic = new SettingLogic();
		try {
			
			setLocale(request);
			addSettings(request);

            String infoApp = logic.findSetting(Settings.SETTING_INFORMATION);
			
			request.setAttribute("infoApp", infoApp);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		if (SecurityUtil.consUserRole(request) == -1 && !HomeServlet.CHOOSE_ROL.equals(accion)
				&& request.getRemoteUser() != null && !LoginServlet.LOGOFF.equals(accionLogin)
				&& !ErrorServlet.ERROR_403.equals(accion)) {
			
			setRolSession(request, response);
		}
		
		// Send info plugins
		sendInfoPlugins(request, ValidateUtil.isNullCh(accion, accionLogin));
		
		// Send notification center data
		if (getUser(request) != null) {

			// List notifications
			JSONArray notifications = new JSONArray();
			
			// Declare logic
			NotificationLogic notificationLogic = new NotificationLogic();
			
			try {
				
				// Logic
				notifications = notificationLogic.findByContact(getUser(request).getContact());
			} 
			catch (Exception e) {
				ExceptionUtil.evalueException(request, getResourceBundle(request), LOGGER, e);
			}
			
			// Send notifications to client
			request.setAttribute("notificationCenter", notifications.toString());
		}
		
	}

    /**
     * Send info for load plugins
     *
     * @param request
     * @param action
     */
	private void sendInfoPlugins(HttpServletRequest request, String action) {
		
		Employee user = getUser(request);
		
		if (user != null && !isAjaxCall(request)) {
			
			try {
				
				// Info for load plugins
				List<PluginLoad> pluginsLoad = new ArrayList<PluginLoad>();
				
				String servletName = this.getClass().getSimpleName();
				
				PluginLogic logic = new PluginLogic();
				
				// Find plugins for logged user
				HashMap<String, Boolean> plugins = logic.getPlugins(getUser(request).getContact());
			
				for (String plugin : plugins.keySet()) {
					
					// Plugin enabled
					if (plugins.get(plugin)) {

						OperationPlugin operationPlugin = null;
				    	
						try {
							
							// Instantiate the operation
							operationPlugin = (OperationPlugin) Class.forName(OperationPlugin.pluginClass(plugin)).newInstance();
                            setInformationPlugin(request, operationPlugin);

						} catch (Exception e) {
//							LOGGER.warn(e);
						}
						
						// Plugin has configuration
						if (operationPlugin != null) {
							
							// Add configuration of plugin
							pluginsLoad.addAll(operationPlugin.pluginLoad(servletName, action));
						}
					}
				}

                // Find in package annotation
                Set<Class<?>> configuredClasses = CacheStatic.getPluginLoads();

                if (ValidateUtil.isNotNull(configuredClasses)) {

                    // Proccess anotations
                    for (Class<?> configuredClass : configuredClasses) {


                        // Get annotation configuration
                        es.sm2.openppm.core.plugin.annotations.PluginLoad load = configuredClass.getAnnotation(es.sm2.openppm.core.plugin.annotations.PluginLoad.class);

                        if (Arrays.asList(load.servlet()).contains(servletName)) {

                            PluginAction annotation = CacheStatic.getPluginAction(configuredClass);

                            PluginLoad pluginLoad = new PluginLoad();
                            pluginLoad.setPluginName(annotation.plugin());
                            pluginLoad.setSelector(load.selector());
                            pluginLoad.setTypeModification(load.loadType().name());
                            pluginLoad.setTemplate(annotation.action());
                            pluginLoad.setForms(load.forms());

                            pluginsLoad.add(pluginLoad);
                        }
                    }
                }


				// Add configuration for load plugins
				if (ValidateUtil.isNotNull(pluginsLoad)) {
					
					for (PluginLoad load : pluginsLoad) {
						LOGGER.debug("Plugin for load: "+load.getPluginName()+" ["+load.getSelector()+"||"+load.getTemplate()+"]");
					}
					
					request.setAttribute("pluginsLoad", pluginsLoad);
				}
			} catch (Exception e) {
				LOGGER.warn("Error in plugin", e);
			}
		}
	}

	/**
	 * Set settings in request
	 * @param request
	 * @throws Exception 
	 */
	private void addSettings(HttpServletRequest request) throws Exception {
		
		Company company = getCompany(request);
		HashMap<String, String> settings;
		
		if (company != null) { 
			settings = SettingUtil.getSettings(company);
		}
		else { settings = new HashMap<String, String>(); }
		
		request.setAttribute("numberOfMonths", Constants.NUMBEROFMONTHS);
		request.setAttribute("settings", settings);
	}

    /**
     * Set locale
     *
     * @param req
     */
	protected void setLocale(HttpServletRequest req) {
		
		req.setAttribute("locale", getResourceBundle(req).getLocale());
		
		// FIXME generate format by user logged
		req.setAttribute("datePattern", DateUtil.getDatePattern(getResourceBundle(req)));
        req.setAttribute("dateTimePattern", Constants.TIME_PATTERN);
		req.setAttribute("datePickerPattern", DateUtil.getDatePickerPattern(getResourceBundle(req)));
	}
	
	/**
	 * Prepare for choose or select role
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void setRolSession(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		try {
			ContactLogic contactLogic 	= new ContactLogic();
	    	
			Contact contact = contactLogic.findByUser(request.getRemoteUser());
			
			if (contact != null) {
				
				if (request.getSession().getAttribute("plugins") == null) {
					
					PluginLogic pluginLogic = new PluginLogic();
					request.getSession().setAttribute("plugins", pluginLogic.getPlugins(contact));
				}
				
				EmployeeLogic employeeLogic = new EmployeeLogic();
				List<Employee> employees = employeeLogic.consEmployeesByUser(contact);
				
				if (employees.isEmpty()) { // Any user match
					request.setAttribute("error", getResourceBundle(request).getString("msg.error.without_permission"));
					request.setAttribute("notLogin", true);
					
					HttpSession session = request.getSession();
					if (session != null) { session.invalidate();  }
					
					setForward(true);
					forward("/login.jsp", request, response);
				}
				else if (employees.size() == 1) { // One user match
					Employee user = employeeLogic.consEmployee(employees.get(0).getIdEmployee());
					request.getSession().setAttribute("user", user);
					request.getSession().setAttribute("rolPrincipal", user.getResourceprofiles().getIdProfile());
				}
				else if (employees.size() > 1) { // More than one user found
					PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
					
					List<Performingorg> orgs = performingOrgLogic.consByContact(contact);
					
					Employee user = new Employee();
					user.setContact(contact);
					request.getSession().setAttribute("user", user);
					
					request.setAttribute("employees", employees);
					request.setAttribute("organizactions", orgs);
					
					// Configurations
					ConfigurationLogic configurationLogic	= new ConfigurationLogic();
					request.setAttribute("configurations", configurationLogic.findByTypes(user, Configurations.TYPE_CHOOSE_ROLE));
					
					setForward(true);
					forward("/select_rol.jsp", request, response);
				}
			}
			else { // Contact not exists
				request.setAttribute("error", getResourceBundle(request).getString("msg.error_login.message"));
				setForward(true);
				forward("/index.jsp", request, response);
			}
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(request, getResourceBundle(request), LOGGER, e);
			setForward(true);
			forward("/index.jsp", request, response);
		}
	}

	/**
	 * JSP Forward
	 * @param uri
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void forward (String uri, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			getServletConfig().getServletContext().getRequestDispatcher("/jsp"+uri).forward(req, resp);
		}
		catch (Exception e) {
			LOGGER.error(StringPool.ERROR, e);
		}
	}
	
	/**
	 * Servlet Forward
	 * @param uri
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void forwardServlet(String uri, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			getServletConfig().getServletContext().getRequestDispatcher("/"+uri).forward(req, resp);
		}
		catch (Exception e) {
			LOGGER.error(StringPool.ERROR, e);
		}
	}
	
	/**
	 * Send file
	 * 
	 * @param req
	 * @param resp
	 * @param file
	 * @param fileName
	 * @param contentType
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void sendFile(HttpServletRequest req,
			HttpServletResponse resp, byte[] file, String fileName, String contentType)
			throws ServletException, IOException {
		
		resp.setContentType(contentType);
		resp.setHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
		resp.setContentLength(file.length);
		
		ByteArrayInputStream bais	= new ByteArrayInputStream(file);
		OutputStream outs			= resp.getOutputStream();
		
		int start	= 0;
		int length	= 4 * 1024; // Buffer 4KB
		byte[] buff = new byte[length];
		
		while (bais.read(buff, start, length) != -1) {
			outs.write(buff, start, length);
		}
		
		bais.close();
	}

	/**
	 * Send file
	 * 
	 * @param req
	 * @param resp
	 * @param file
	 * @param fileName
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void sendFile(HttpServletRequest req, HttpServletResponse resp, byte[] file, String fileName) throws ServletException, IOException {
		
		sendFile(req, resp, file, fileName, Constants.FILE_CONTENT_TYPE_GENERIC);
	}
	
	
	@SuppressWarnings("rawtypes")
	protected Hashtable<String, FileItem> parseFields (List fileItems) throws IOException {

    	Hashtable<String, FileItem> hashFields = new Hashtable<String, FileItem>();

        for (Object fileItem : fileItems) {

            FileItem fileItemTemp = (FileItem) fileItem;

            if (fileItemTemp.isFormField()) {
                hashFields.put(fileItemTemp.getFieldName(), fileItemTemp);
            } else {
                hashFields.put("file", fileItemTemp);
            }
        }

        return hashFields;
    }
	
	/****************** JSON Methods ***********************/
	
	protected JSONObject infoCreated(ResourceBundle resourceBoundle, JSONObject info, String key) {
		return JsonUtil.infoCreated(resourceBoundle, info, key);
	}
	
	protected JSONObject infoUpdated(ResourceBundle resourceBoundle, JSONObject info, String key) {
		return JsonUtil.infoUpdated(resourceBoundle, info, key);
	}

	protected JSONObject infoDeleted(ResourceBundle resourceBoundle, String key) {
		return JsonUtil.infoDeleted(resourceBoundle, key);
	}
	
	protected JSONObject putInfo(String type, JSONObject objectJSON, JSONObject infoJSON) {
		return JsonUtil.putInfo(type, objectJSON, infoJSON);
	}
	
	protected JSONObject info(ResourceBundle resourceBoundle, String type, String key, JSONObject objectJSON, Object...args) {
		return JsonUtil.info(resourceBoundle, type, key, objectJSON, args);
	}
	
	protected JSONObject info(String type, String value, JSONObject objectJSON) {
		return JsonUtil.info(type, value, objectJSON);
	}

	/****************** Request Methods ***********************/
	
	protected void infoCreated(HttpServletRequest req, String key) {
		
		info(StringPool.SUCCESS, req,"msg.info.created",key);
	}
	
	protected void infoUpdated(HttpServletRequest req, String key) {
		
		info(StringPool.SUCCESS, req,"msg.info.updated",key);
	}
	
	protected void infoDeleted(HttpServletRequest req, String key) {
		
		info(StringPool.SUCCESS, req,"msg.info.deleted",key);
	}
	
	@SuppressWarnings("unchecked")
	protected void info(String type, HttpServletRequest req, String key, Object...args) {
		
		List<String> information = null;
				
		Object object = req.getAttribute(type);
		
		if (object != null && object instanceof String) {
			information = new ArrayList<String>();
			information.add((String) object);
		}
		else if (object != null && object instanceof List) {
			information = (List<String>) object;
		}
		
		if (information == null) {
			information = new ArrayList<String>();
		}
		ParamResourceBundle msgFormat = new ParamResourceBundle(key, args);
		msgFormat.setBlack(true);
		information.add(msgFormat.getMessage(getResourceBundle(req)));
		req.setAttribute(type, information);
	}

	/**
	 * Create infos
	 * 
	 * @param infos
	 * @param req
	 */
	protected void createInfos(List<Info> infos, HttpServletRequest req) {
		
		if (ValidateUtil.isNotNull(infos)) {
			
			for (Info info : infos) {
				
				if (ValidateUtil.isNotNull(info.getType()) && ValidateUtil.isNotNull(info.getKey())) {
					info(info.getType(), req, info.getKey(), info.getArgs());
				}
			}
		}
	}

    /**
     * Create infos JSON
     *
     * @param infos
     * @param infoJSON
     * @param resourceBundle
     */
    protected void createInfos(List<Info> infos, JSONObject infoJSON, ResourceBundle resourceBundle) {

        if (ValidateUtil.isNotNull(infos)) {

            for (Info info : infos) {

                if (ValidateUtil.isNotNull(info.getType()) && ValidateUtil.isNotNull(info.getKey())) {
                    info(resourceBundle, info.getType(), info.getKey(), infoJSON, info.getArgs());
                }
            }
        }
    }
	
	/**
	 * Logged user
	 * @param req
	 * @return
	 */
	protected Employee getUser(HttpServletRequest req) {
		return SecurityUtil.consUser(req);
	}
	
	/**
	 * Company of logged user
	 * @param req
	 * @return
	 */
	protected Company getCompany(HttpServletRequest req) {
		
		Employee user = getUser(req);

        return (user == null?null:user.getContact().getCompany());
	}
	
	protected boolean isAjaxCall(HttpServletRequest request) {
		return ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
	}
	
	public void setForward(boolean isForward) {
		this.isForward = isForward;
	}

	public boolean isForward() {
		return isForward;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> getSettings(HttpServletRequest req) {
		
		return (HashMap<String, String> )req.getAttribute("settings");
	}
	
	/**
	 * Format date
	 * @param req
	 * @param date
	 * @return
	 */
	public String formatDate(HttpServletRequest req, Date date) {
		
		String value = "";
		
		if (date != null) {
			
			value = getDateFormat(req).format(date);
		}
		
		return value;
	}
	
	public ResourceBundle getResourceBundle(HttpServletRequest req) {
		
		ResourceBundle resourceBoundle;
		
		try {
			String[] locale = getUser(req).getContact().getLocale().split("_");
			resourceBoundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", new Locale(locale[0], locale[1]));
		}
		catch (Exception e) {
			resourceBoundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", Constants.DEF_LOCALE);
		}
		
		return resourceBoundle;
	}

	public SimpleDateFormat getDateFormat(HttpServletRequest req) {
		
		// FIXME generate format by user logged
		return new SimpleDateFormat(DateUtil.getDatePattern(getResourceBundle(req)), Constants.DEF_LOCALE_NUMBER);
	}

	public NumberFormat getNumberFormat() {
		
		// FIXME generate format by user logged
		return NumberFormat.getNumberInstance(Constants.DEF_LOCALE_NUMBER);
	}

	public String getDatePattern(HttpServletRequest req) {
		
		// FIXME generate format by user logged
		return DateUtil.getDatePattern(getResourceBundle(req));
	}
	
	public Integer getIdProject(HttpServletRequest req) {
		return ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
	}

    /**
     * Add information to plugin
     *
     * @param req
     * @param operationPlugin
     */
    @SuppressWarnings("rawtypes")
    protected void setInformationPlugin(HttpServletRequest req, OperationPlugin operationPlugin) {

        // Request attributes
        //
        Hashtable<String, Object> attributes = new Hashtable<String, Object>();

        for (Enumeration e = req.getAttributeNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            attributes.put(attName, req.getAttribute(attName));
        }

        operationPlugin.setAttributes(attributes);

        // Request parameters
        //
        Hashtable<String, Object> parameters = new Hashtable<String, Object>();

        for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            parameters.put(attName, req.getParameter(attName));
        }

        operationPlugin.setParameters(parameters);

        // Request session attributes
        //
        Hashtable<String, Object> sessionAttributes = new Hashtable<String, Object>();

        for (Enumeration e = req.getSession().getAttributeNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            sessionAttributes.put(attName, req.getSession().getAttribute(attName));
        }

        operationPlugin.setSessionAttributes(sessionAttributes);

        // Resource boundle
        operationPlugin.setResourceBundle(getResourceBundle(req));
    }


    /**
     * Prepare data to send to action listener
     *
     * @param request - data of servlet call
     * @return - extra data
     */
    protected Map<String, Object> getExtraData(HttpServletRequest request) {

        HashMap<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(ProcessListener.EXTRA_DATA_RESOURCE_BUNDLE, getResourceBundle(request));
        extraData.put(ProcessListener.EXTRA_DATA_SETTINGS, getSettings(request));
        extraData.put(ProcessListener.EXTRA_DATA_USER, getUser(request));
        extraData.put(ProcessListener.EXTRA_DATA_IDPROJECT, getIdProject(request));

        return extraData;
    }


    /**
     * Add information to plugin
     *
     * @param req
     * @param genericAction
     */
    protected void setInformationPlugin(HttpServletRequest req, GenericAction genericAction) {

        // Set multipart fields
        genericAction.setMultipartFields(getMultipartFields());

        // Request attributes
        //
        Hashtable<String, Object> attributes = new Hashtable<String, Object>();

        for (Enumeration e = req.getAttributeNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            attributes.put(attName, req.getAttribute(attName));
        }

        genericAction.setAttributes(attributes);

        // Request parameters
        //
        Hashtable<String, Object> parameters = new Hashtable<String, Object>();

        for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            parameters.put(attName, req.getParameter(attName));
        }

        genericAction.setParameters(parameters);

        // Request session attributes
        //
        Hashtable<String, Object> sessionAttributes = new Hashtable<String, Object>();

        for (Enumeration e = req.getSession().getAttributeNames(); e.hasMoreElements();) {

            String attName = (String) e.nextElement();

            sessionAttributes.put(attName, req.getSession().getAttribute(attName));
        }

        genericAction.setSessionAttributes(sessionAttributes);

        // Resource boundle
        genericAction.setResourceBundle(getResourceBundle(req));
    }

    /**
     * Find Multipart filed by name
     *
     * @param name - name of field
     * @return field in String format
     * @throws UnsupportedEncodingException bad encoding format
     */
    protected String getMultipartField(String name, String encoding) throws UnsupportedEncodingException {

        if (getMultipartFields() != null) {

            FileItem fileItem = getMultipartFields().get(name);

            if (fileItem != null) {

                return encoding == null?fileItem.getString():fileItem.getString(encoding);
            }
        }

        return StringPool.BLANK;
    }

    /**
     * Find Multipart filed by name
     *
     * @param name - name of field
     * @return field in String format
     */
    protected String getMultipartField(String name) {

        try {
            return getMultipartField(name, null);
        } catch (UnsupportedEncodingException e) {
            return StringPool.BLANK;
        }
    }
    protected Hashtable<String, FileItem> getMultipartFields() {
        return multipartFields;
    }

    protected void setMultipartFields(Hashtable<String, FileItem> multipartFields) {
        this.multipartFields = multipartFields;
    }
}


