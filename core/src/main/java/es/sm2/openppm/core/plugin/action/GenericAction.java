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
 * File: GenericAction.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin.action;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.PluginconfigurationLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Pluginconfiguration;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.plugin.action.beans.PluginResponse;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.core.utils.ResourceBoundleUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.GetterUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 01/10/2014.
 */
public abstract class GenericAction {

    public static final String PACKAGE = "es.sm2.openppm";

    public final static Logger LOGGER = Logger.getLogger(GenericAction.class);

    private Hashtable<String, Object> attributes;
    private Hashtable<String, Object> parameters;
    private Hashtable<String, Object> sessionAttributes;
    private ResourceBundle resourceBundle;
    private Hashtable<String, FileItem> multipartFields;

    public abstract PluginResponse process();

    // ********************************************************************
    //   UTILS
    // ********************************************************************


    /**
     * Add error to response
     *
     * @param response
     * @param actionClass
     * @param throwable
     */
    public void addError(PluginResponse response, Class<? extends GenericAction> actionClass, Throwable throwable) {

        PluginAction pluginAction = actionClass.getAnnotation(PluginAction.class);

        LogManager.getLog(this.getClass()).error("Plugin "+pluginAction.plugin()+". Action: "+pluginAction.action()+"", throwable);
        response.setError(throwable.getMessage());
    }

    /**
     *
     * Create localiced language
     * @param key
     * @param params
     * @return
     */
    protected String getParametrizedKey(String key, Object...params) {

        return ResourceBoundleUtil.getInstance().value(getResourceBundle(), key, params);
    }

    public HashMap<String, String> getConfiguration(String pluginName) throws Exception {

        HashMap<String, String> configurationMap = new HashMap<String, String>();

        // Find configurations
        PluginconfigurationLogic logic = new PluginconfigurationLogic();
        List<Pluginconfiguration> configurations = logic.findConfigurations(getCompany(), pluginName);

        LOGGER.debug("Configurations for plugin: "+pluginName);

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
     * Find id project in parameters [id/idProject] and multipart fields
     *
     * @return idProject
     */
    public Integer getIdProject() {

        Integer idProject = null;

        if (parameters.get("id") != null) {

            idProject = new Integer((String)parameters.get("id"));
        }
        else if (parameters.get("idProject") != null) {

            idProject = new Integer((String)parameters.get("idProject"));
        }
        else if (getMultipartFields() != null && !getMultipartFields().isEmpty()) {

            String id = getMultipartField("id");

            try {
                // Replace all non digits
                id = id.replaceAll("\\D","");

                idProject = new Integer(id);
            }
            catch (NumberFormatException e) {
                LOGGER.error(e);
            }
        }

        return idProject;
    }

    /**
     * Get integer value from parameters
     *
     * @param paramName
     * @return
     */
    public Integer getInteger(String paramName) {

        return GetterUtil.getInteger(getString(paramName));
    }

    /**
     * Get integer value from parameters
     *
     * @param paramName
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String paramName, Integer defaultValue) {

        return GetterUtil.getInteger(getString(paramName), defaultValue);
    }

    /**
     * Get string value form parameters
     *
     * @param paramName
     * @return
     */
    public String getString(String paramName, String defaultValue) {

        Object data = parameters.get(paramName);
        return GetterUtil.getString(data == null?defaultValue:(String)data);
    }

    /**
     * Get string value form parameters
     *
     * @param paramName
     * @return
     */
    public String getString(String paramName) {

        return getString(paramName, null);
    }

    /**
     * Get boolean value form parameters
     *
     * @param paramName
     * @return
     */
    public Boolean getBoolean(String paramName, Boolean defaultValue) {

        return GetterUtil.getBoolean(getString(paramName), defaultValue);
    }

    /**
     * Get boolean value form parameters
     *
     * @param paramName
     * @return
     */
    public Boolean getBoolean(String paramName) {

        return GetterUtil.getBoolean(getString(paramName));
    }

    /**
     * Get currency value form parameters
     *
     * @param paramName
     * @return
     */
    public Double getCurrency(String paramName) {

        return GetterUtil.getCurrency(getString(paramName));
    }

    /**
     * Get currency value form parameters
     *
     * @param paramName
     * @return
     */
    public Double getCurrency(String paramName, Double defaultValue) {

        return GetterUtil.getCurrency(getString(paramName), defaultValue);
    }

    /**
     * Get data from session attributes
     *
     * @param attributeName
     * @return
     */
    public Object getSessionAttribute(String attributeName) {
        return sessionAttributes.get(attributeName);
    }

    /**
     * Get date value form parameters
     *
     * @param paramName
     * @return
     */
    public Date getDate(String paramName) {

        return getDate(paramName, null);
    }

    /**
     * Get date value form parameters
     *
     * @param paramName
     * @param defaultValue
     * @return
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
     * @throws java.io.IOException
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

    /**
     * Find project in BD
     *
     * @return project by ID
     */
    public Project getProject() {

        Project project = null;

        try {
            // Find project data
            ProjectLogic projectLogic = new ProjectLogic(getSettings(), getResourceBundle());
            project = projectLogic.findById(getIdProject());

        } catch (Exception e) {
            LOGGER.error("Project not found", e);
        }

        return project;
    }

    /**
     * Find Multipart filed by name
     *
     * @param name - name of field
     * @return field in String format
     */
    protected String getMultipartField(String name) {

        if (getMultipartFields() != null) {

            FileItem fileItem = getMultipartFields().get(name);

            if (fileItem != null) {
                return fileItem.getString();
            }
        }

        return StringPool.BLANK;
    }

    // ********************************************************************
    //   Getters and Setters
    // ********************************************************************

    public Hashtable<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Hashtable<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Hashtable<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Hashtable<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setSessionAttributes(Hashtable<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Hashtable<String, FileItem> getMultipartFields() {
        return multipartFields;
    }

    public void setMultipartFields(Hashtable<String, FileItem> multipartFields) {
        this.multipartFields = multipartFields;
    }
}
