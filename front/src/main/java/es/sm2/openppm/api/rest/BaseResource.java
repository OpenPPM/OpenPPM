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
 * File: BaseResource.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 08:24:14
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.rest.error.AuthenticateException;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 05/08/2015.
 */
public class BaseResource {

    public static final String SIGNING_KEY = "SecretKeyOpenPPM";
    public static final String EMPLOYEE_ID_KEY = "employeeIdKey";
    public static final String TOKEN_PARAM = "X-Auth-Token";
//    public static final String FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String CODE_PROJECT = "codeProject";

    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @Context
    private UriInfo uriInfo;

    private HashMap<String, String> settings;

    protected Date formatDate(String dateApi) {

        Date date = null;

        if (dateApi != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE);

            try {
                date = simpleDateFormat.parse(dateApi);
            } catch (ParseException e) {
                throw new RestServiceException(Response.Status.BAD_REQUEST, "Wrong date format ["+dateApi+"] parse to: "+FORMAT_DATE);
            }
        }
        return date;
    }

    /**
     * Prepare data to send to action listener
     *
     * @return - extra data
     */
    protected Map<String, Object> getExtraData() {

        HashMap<String, Object> extraData = new HashMap<String, Object>();
        extraData.put(ProcessListener.EXTRA_DATA_RESOURCE_BUNDLE, getResourceBundle());
        extraData.put(ProcessListener.EXTRA_DATA_SETTINGS, getSettings());
        extraData.put(ProcessListener.EXTRA_DATA_USER, getUser());

        return extraData;
    }

    /**
     * Get Settings by company
     *
     * @return
     */
    protected HashMap<String, String> getSettings() {

        if (settings == null) {
            try {
                settings = SettingUtil.getSettings(getCompany());
            } catch (Exception e) {
                throw new RestServiceException(e);
            }
        }

        return settings;
    }

    /**
     * Generate response from Throwable error
     *
     * @param log
     * @param e
     * @return
     */
    @Deprecated
    protected Response getErrorResponse(Logger log, String method, Throwable e) {

        return generateErrorResponse(log, method, e);
    }

    /**
     * Generate response from Throwable error
     *
     * @param log
     * @param method
     * @param e
     * @return
     */
    public static Response generateErrorResponse(Logger log, String method, Throwable e) {

        // Send error to client
        if (e instanceof RestServiceException) { throw (RestServiceException)e; }

        // Server error
        log.error(method + StringPool.SPACE + e.getMessage(), e);
        throw new RestServiceException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());

        // TODO javier.hernandez - 23/09/2015 - eliminar response
    }

    /**
     * Generate Throwable error for rest API
     *
     * @param log
     * @param e
     * @return
     */
    protected void sendError(Logger log, String method, Throwable e) {

        // Send error to client
        if (e instanceof RestServiceException) { throw (RestServiceException)e; }

        // Server error
        log.error(method + StringPool.SPACE + e.getMessage(), e);
        throw new RestServiceException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * Check if user is not logged or is logged but has no role
     *
     * @param roles
     * @return
     */
    protected void isUserLogged(Resourceprofiles.Profile...roles) {

        if (isLogged() && ValidateUtil.isNotNull(roles) && !SecurityUtil.isUserInRole(request, roles)) {
            throw new RestServiceException(Response.Status.UNAUTHORIZED, "Not authorized, you not have permission for these method");
        }
        else if (!isLogged())  {
            throw new RestServiceException(Response.Status.UNAUTHORIZED, "Not authorized, you need to log in");
        }

        // Check project permissions
        checkProjectPermission();
    }

    /**
     * Check if user has one of these roles
     * @param roles
     * @return
     */
    protected boolean isUserInRole(Resourceprofiles.Profile...roles) {
        return SecurityUtil.isUserInRole(request, roles);
    }

    /**
     * Utilizar el metodo isUserLogged
     *
     * @param roles
     * @return
     */
    @Deprecated
    protected Response checkPermission(Integer...roles) {

        // TODO javier.hernandez - 14/09/2015 - eliminar metodo

        if (isLogged() && ValidateUtil.isNotNull(roles) && !SecurityUtil.isUserInRole(request, roles)) {
            throw new RestServiceException(Response.Status.UNAUTHORIZED, "Not authorized, you not have permission for these method");
        }
        else if (!isLogged())  {
            throw new RestServiceException(Response.Status.UNAUTHORIZED, "Not authorized, you need to log in");
        }

        // Check project permissions
        checkProjectPermission();

        return null;
    }

    /**
     * Check if user logged has permission for view project
     *
     */
    private void checkProjectPermission() {

        if (uriInfo != null && uriInfo.getPathParameters().containsKey(CODE_PROJECT)) {

            List<String> codeProjects = uriInfo.getPathParameters().get(CODE_PROJECT);

            if (codeProjects.size() > 1) {
                throw new RestServiceException(Response.Status.BAD_REQUEST, "Multiple [codeProject] are invalid");
            }

            Integer codeProject;

            try {
                codeProject = Integer.valueOf(codeProjects.get(0));
            }
            catch (NumberFormatException e) {
                LogManager.getLog(getClass()).warn("[codeProject] invalid format", e);
                throw new RestServiceException(Response.Status.BAD_REQUEST, "[codeProject] invalid format: "+codeProjects.get(0));
            }

            boolean hasPermission;
            try {
                ProjectLogic logic = new ProjectLogic(null);
                // TODO javier.hernandez - 22/09/2015 - param tab deprecated
                hasPermission = logic.hasPermission(new Project(codeProject), getUser(), Constants.TAB_CONTROL);

            }
            catch (Exception e) {
                LogManager.getLog(getClass()).error("Check security for project", e);
                throw new RestServiceException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            if (!hasPermission){
                throw new RestServiceException(Response.Status.UNAUTHORIZED, "Not authorized, you not have permission for view data of project");
            }
        }
    }

    /**
     * Logged user
     * @return
     */
    protected Employee getUser() {
        return request != null && request.getSession() != null ? SecurityUtil.consUser(request): null;
    }

    /**
     * Company of logged user
     * @return
     */
    protected Company getCompany() {

        Employee user = getUser();

        return (user == null ? null : user.getContact().getCompany());
    }

    /**
     * Resource bundle
     *
     * @return
     */
    public ResourceBundle getResourceBundle() {

        ResourceBundle resourceBoundle;

        try {
            String[] locale = getUser().getContact().getLocale().split("_");
            resourceBoundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", new Locale(locale[0], locale[1]));
        }
        catch (Exception e) {
            resourceBoundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", Constants.DEF_LOCALE);
        }

        return resourceBoundle;
    }

    /**
     * Check if user is logged by application
     *
     * @return
     */
    protected boolean isLogged() {

        return getUser() != null;
    }

    /**
     * Check JWT token and get employee with roles.
     *
     * @param token				JWT token
     * @return	Auth info with provider and principal
     * @throws AuthenticateException
     */
    protected Employee checkToken(String token) throws AuthenticateException {

        Jws<Claims> claims = getClaims(token);

        // TODO javier.hernandez - 05/08/2015 - get user by claims.getBody().getSubject()

        return new Employee();
    }

    private Jws<Claims> getClaims(String token) throws AuthenticateException {
        try {
            return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            throw new AuthenticateException(e.getMessage(), e);
        }
    }

    /**
     * Getter for property 'request'.
     *
     * @return Value for property 'request'.
     */
    public HttpServletRequest getRequest() {

        return request;
    }

    /**
     * Setter for property 'request'.
     *
     * @param request Value to set for property 'request'.
     */
    public void setRequest(HttpServletRequest request) {

        this.request = request;
    }

    /**
     * Getter for property 'uriInfo'.
     *
     * @return Value for property 'uriInfo'.
     */
    public UriInfo getUriInfo() {

        return uriInfo;
    }

    /**
     * Setter for property 'uriInfo'.
     *
     * @param uriInfo Value to set for property 'uriInfo'.
     */
    public void setUriInfo(UriInfo uriInfo) {

        this.uriInfo = uriInfo;
    }

    /**
     * Getter for property 'response'.
     *
     * @return Value for property 'response'.
     */
    public HttpServletResponse getResponse() {

        return response;
    }

    /**
     * Setter for property 'response'.
     *
     * @param response Value to set for property 'response'.
     */
    public void setResponse(HttpServletResponse response) {

        this.response = response;
    }
}
