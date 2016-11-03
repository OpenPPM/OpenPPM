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
 * File: PlainLoginModule.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.auth;

import java.security.Principal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.SecurityDAO;
import es.sm2.openppm.core.dao.SettingDAO;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.front.utils.ConnectLDAP;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;



public class PlainLoginModule implements LoginModule {

	private static final  Logger LOGGER = Logger.getLogger(PlainLoginModule.class);
	
	 /** Callback handler to store between initialization and authentication. */
    private CallbackHandler handler;

    /** Subject to store. */
    private Subject subject;

    /** Login name. */
    Security security = null;

    /**
     * This implementation always return <code>false</code>.
     * 
     * @see javax.security.auth.spi.LoginModule#abort()
     */
    public boolean abort() throws LoginException {
        return false;
    }

    /**
     * This is where, should the entire authentication process succeeds,
     * principal would be set.
     * 
     * @see javax.security.auth.spi.LoginModule#commit()
     */
    public boolean commit() {
        	
        assignPrincipal(new PlainUserPrincipal(security.getLogin()));
        assignPrincipal(new PlainRolePrincipal(Settings.APLICATION_ROL));
        
        return true;
    }
    
    private void assignPrincipal(Principal p) {
        // Make sure we dont add duplicate principals
        if (!subject.getPrincipals().contains(p)) {
            subject.getPrincipals().add(p);
        }
    }

    /**
     * This implementation ignores both state and options.
     * 
     * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
     *      javax.security.auth.callback.CallbackHandler, java.util.Map,
     *      java.util.Map)
     */
    public void initialize(Subject aSubject, CallbackHandler aCallbackHandler, Map<String, ?> aSharedState,
            Map<String, ?> aOptions) {
        handler = aCallbackHandler;
        subject = aSubject;
    }

    /**
     * This method checks whether the name and the password are the same.
     * 
     * @see javax.security.auth.spi.LoginModule#login()
     */
    public boolean login() throws LoginException {
        
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("password", true);
        
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
        	Transaction tx = session.beginTransaction();
        	
        	SecurityDAO securityDAO		= new SecurityDAO(session);
        	SettingDAO settingDAO		= new SettingDAO(session);
        	
            handler.handle(callbacks);

            String name			= ((NameCallback) callbacks[0]).getName();
            String password		= String.valueOf(((PasswordCallback) callbacks[1]).getPassword());

            // Authentication
            security = new Security();
            security.setLogin(name);
            
            security = securityDAO.getByLogin(security);

            Calendar actualDate = Calendar.getInstance();
            Calendar dateLapsed = null;
            
            // Date lapsed for check bloqued user
            if (security != null && security.getDateLapsed() != null) {
            	dateLapsed = Calendar.getInstance();
            	dateLapsed.setTime(security.getDateLapsed());
            }
            
            boolean error = false;
            
            if (security != null && !ValidateUtil.isNull(password) && (dateLapsed == null || actualDate.after(dateLapsed))) {
            	
            	// Company of de user
            	Company company = security.getContact().getCompany();
            	
            	// Settings of company
            	List<Setting> settingsList = settingDAO.findByCompany(company, null);
            	HashMap<String, String> settings = new HashMap<String, String>();
    			for(Setting setting : settingsList) {
    				settings.put(setting.getName(), setting.getValue());
    			}
    			
    			boolean checkLDAP = false;

    			// Login by LDAP
    			if (Constants.SECURITY_LOGIN_LDAP.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
    					|| Constants.SECURITY_LOGIN_MIXED.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))) {
    				
    				ConnectLDAP connectLDAP = new ConnectLDAP(settings);
    				error = !connectLDAP.loginSuccess(name, password);
    				checkLDAP = true;
    				
    				LOGGER.info("Attempt to login LDAP for user: '"+name+ "' Error: "+error);
    			}
    			
    			// Login by BBDD
    			if ((Constants.SECURITY_LOGIN_BBDD.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
    					|| Constants.SECURITY_LOGIN_MIXED.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE)))
    					&& ((checkLDAP && error) || !checkLDAP)) {
    				
    				// Check BBDD
    				error = !SecurityUtil.md5(password).equals(security.getPassword());

    				LOGGER.info("Attempt to login BBDD for user: '"+name+ "' Error: "+error);
    			}
    			
    			if (security.getDateLapsed() != null) {
    	            
    				security.setAttempts(0);
    				security.setDateLapsed(null);
	            }
            }
            else {
            	LOGGER.info("Attempt to login. Bad user: '"+name+"'");
            	
            	error = true;
            }

            // Update date last attempt in any case
            security.setDateLastAttempt(actualDate.getTime());

            // Update date error
            if (!error) {
            	security.setAttempts(0);
				security.setDateLapsed(null);
			}
            
            securityDAO.makePersistent(security);
            
            tx.commit();
            
            // Send error login
            if (error) { throw new FailedLoginException("Authentication failed!"); }
            
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        	throw new LoginException(e.getMessage());
		}
    }

    /**
     * Clears subject from principal and credentials.
     * 
     * @see javax.security.auth.spi.LoginModule#logout()
     */
    public boolean logout() throws LoginException {
        try {
            PlainUserPrincipal user = new PlainUserPrincipal(security.getLogin());
            PlainRolePrincipal role = new PlainRolePrincipal("admin");

            subject.getPrincipals().remove(user);
            subject.getPrincipals().remove(role);

            return true;
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }
    }

}
