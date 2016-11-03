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
 * File: BeanResponse.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin.action.beans;

import es.sm2.openppm.utils.javabean.ParamResourceBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 01/10/2014.
 */
public class BeanResponse extends PluginResponse {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.BEAN;
    }

    private String urlServlet;
    private String urlJSP;
    private String information;
    private String informationType;
    private HashMap<String,Object> data;

    public BeanResponse() {

        data = new HashMap<String, Object>();
    }

    /**
     * Add attribute to request
     *
     * @param name
     * @param data
     */
    public void addData(String name, Object data) {

        this.data.put(name, data);
    }

    public void addInfo(ResourceBundle bundle, String type, String key, Object...args) {

        List<String> information = null;

        Object object = data.get(type);

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
        information.add(msgFormat.getMessage(bundle));

        data.put(type, information);
    }

    /**
     * @return the urlServlet
     */
    public String getUrlServlet() {
        return urlServlet;
    }
    /**
     * @param urlServlet the urlServlet to set
     */
    public void setUrlServlet(String urlServlet) {
        this.urlServlet = urlServlet;
    }
    /**
     * @return the urlJSP
     */
    public String getUrlJSP() {
        return urlJSP;
    }
    /**
     * @param urlJSP the urlJSP to set
     */
    public void setUrlJSP(String urlJSP) {
        this.urlJSP = urlJSP;
    }
    /**
     * @return the information
     */
    public String getInformation() {
        return information;
    }
    /**
     * @param information the information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }
    /**
     * @return the informationType
     */
    public String getInformationType() {
        return informationType;
    }
    /**
     * @param informationType the informationType to set
     */
    public void setInformationType(String informationType) {
        this.informationType = informationType;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
