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
 * Module: core
 * File: OperationPlugin.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.PluginFile;
import es.sm2.openppm.core.logic.impl.PluginconfigurationLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Pluginconfiguration;
import es.sm2.openppm.core.utils.ResourceBoundleUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class OperationPlugin {

	public final static Logger LOGGER = Logger.getLogger(OperationPlugin.class);
	
	protected static String pluginName;
	private Hashtable<String, Object> attributes;
	private Hashtable<String, Object> parameters;
	private Hashtable<String, Object> sessionAttributes;
	private ResourceBundle resourceBundle;
	
	public static String pluginClass(String name) {
		
		return "es.sm2.openppm.plugin."+name.toLowerCase()+"."+name+"Operation";
	}
	
	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}
	
	// ********************************************************************
	//   Abstract methods
	// ********************************************************************

	/**
	 * Return document
	 * 
	 * @return
	 */
	public abstract PluginFile pluginDocument() throws Exception;
	
	/**
	 * Return bean and request info
	 * 
	 * @return
	 */
	public abstract Object pluginBean() throws Exception;
	
	/**
	 * Return JSON format
	 * 
	 * @return
	 */
	public abstract JSONObject pluginJSON() throws Exception;

	/**
	 * Return String format (Example HTML response)
	 * 
	 * @return
	 */
	public abstract String pluginString() throws Exception;
	
	/**
	 * Return plugin load by action
	 * 
	 * @return
	 */
	public abstract List<PluginLoad> pluginLoad(String servlet, String action) throws Exception;
	
	/**
	 * Return compiled template in HTML source
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String generateTemplateHtml() throws Exception;
	
	// ********************************************************************
	//   UTILS
	// ********************************************************************


    /**
     * Create localiced language
     * @param key
     * @param params
     * @return
     */
    protected String getLoceKey(String key, Object...params) {

        return ResourceBoundleUtil.getInstance().value(getResourceBundle(), key, params);
    }

	public HashMap<String, String> getConfiguration() throws Exception {
		
		HashMap<String, String> configurationMap = new HashMap<String, String>();
		
		// Find configurations
		PluginconfigurationLogic logic = new PluginconfigurationLogic();
		List<Pluginconfiguration> configurations = logic.findConfigurations(getCompany(), getPluginName());
		
		LOGGER.debug("Configurations for plugin: "+getPluginName());
		
		for (Pluginconfiguration item : configurations) {
			
			LOGGER.debug("\t"+item.getConfiguration()+"="+item.getValue());
			configurationMap.put(item.getConfiguration(), item.getValue());
		}
		
		return configurationMap;
		
	}
	
	/**
	 * Get settings
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getSettings() {
		
		return (HashMap<String, String> )attributes.get("settings");
	}
	
	/**
	 * Get id project
	 * 
	 * @return
	 */
	public Integer getIdProject() {
		return new Integer((String)parameters.get("id"));
	}
	
	/**
	 * Get integer value
	 * 
	 * @param paramName
	 * @return
	 */
	public Integer getInteger(String paramName) {
		return new Integer((String)parameters.get(paramName));
	}
	
	/**
	 * Get date
	 * 
	 * @param paramName
	 * @return
	 * @throws ParseException
	 */
	public Date getDate(String paramName, Date defaultValue) {
		
		Date date;
		
		// Get parameter as string
		String dateStr = (String)parameters.get(paramName);
		
		// Format parameter
		if (ValidateUtil.isNotNull(dateStr)) {
			
			try {
				date = getDateFormat().parse(dateStr);
			}
			catch (ParseException e) {
				date = defaultValue;
			}
		}
		else {
			date = defaultValue;
		}
		
			
		return date;
	}
	/**
	 * Date format
	 * 
	 * @return
	 */
	public SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DateUtil.getDatePattern(getResourceBundle()), Constants.DEF_LOCALE_NUMBER);
	}
	
	/**
	 * Is user in role 
	 * 
	 * @param rol
	 * @return
	 */
	public boolean isUserRole(Integer rol) {
		
		boolean isUserRole = false;
		
		if (rol.equals(getSessionAttributes().get("rolPrincipal"))) {
			isUserRole = true;
		}
		
		return isUserRole;
	}
	
	/**
	 * Get company from session
	 * 
	 * @return
	 */
	public Company getCompany() {
		
		Employee user = (Employee)getSessionAttributes().get("user");
		
		return user.getContact().getCompany();
	}
	
	/**
	 * Get Logged user
	 * 
	 * @return
	 */
	public Employee getUser() {
		
		return (Employee)getSessionAttributes().get("user");
	}
	
	// ********************************************************************
	//   Generate files
	// ********************************************************************
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
		
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();
		
		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			
			LOGGER.error("File too large");
		}
		
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		
		// Read in the bytes
		int offset = 0;
		int numRead;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		
		is.close();
		
		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		return bytes;
	}
	
	/**
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipFiles (List<File> files) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		for (File f : files) {
		
			zos.putNextEntry(new ZipEntry(f.getName()));
			
			zos.write(getBytesFromFile(f.getAbsoluteFile()));
				
			zos.closeEntry();
		}

		zos.flush();
		baos.flush();
		zos.close();
		baos.close();
		
		return baos.toByteArray();
	}

	// ********************************************************************
	//   Getters and Setters
	// ********************************************************************
	
	/**
	 * @return the attributes
	 */
	public Hashtable<String, Object> getAttributes() {
		return attributes;
	}


	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Hashtable<String, Object> attributes) {
		this.attributes = attributes;
	}


	/**
	 * @return the parameters
	 */
	public Hashtable<String, Object> getParameters() {
		return parameters;
	}


	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Hashtable<String, Object> parameters) {
		this.parameters = parameters;
	}


	/**
	 * @return the sessionAttributes
	 */
	public Hashtable<String, Object> getSessionAttributes() {
		return sessionAttributes;
	}


	/**
	 * @param sessionAttributes the sessionAttributes to set
	 */
	public void setSessionAttributes(Hashtable<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}


	/**
	 * @return the resourceBundle
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}


	/**
	 * @param resourceBundle the resourceBundle to set
	 */
	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	
}
