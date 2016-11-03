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
 * File: ConnectLDAP.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.javabean.UserLDAP;
import es.sm2.openppm.core.logic.impl.SecurityLogic;
import es.sm2.openppm.core.logic.setting.SecuritySetting;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;

public class ConnectLDAP {

	private static final Logger LOGGER = Logger.getLogger(ConnectLDAP.class);

	private static final String DISTINGUISHED_NAME 		= "distinguishedName";
	public static final String AUTHENTICATION_SIMPLE	= "simple";
	public static final String CONTEXT_FACTORY			= "com.sun.jndi.ldap.LdapCtxFactory";

	private Hashtable<String, Object> env;
	private HashMap<String, String> settings;
	
	public ConnectLDAP(HashMap<String, String> settings) {

		this.settings = settings;

		env = new Hashtable<String, Object>();
		env.put(Context.SECURITY_AUTHENTICATION, AUTHENTICATION_SIMPLE);
		env.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY);

		// Create Provider URL
		StringBuilder providerURL = new StringBuilder()
				.append(SettingUtil.getString(settings, SecuritySetting.PROTOCOL).toLowerCase())
				.append("://")
				.append(SettingUtil.getString(settings, Settings.SETTING_LDAP_SERVER, StringPool.BLANK))
				.append(":")
				.append(SettingUtil.getString(settings, Settings.SETTING_LDAP_PORT, StringPool.BLANK));

		env.put(Context.PROVIDER_URL, providerURL.toString());
	}

	/**
	 * Login user in LDAP
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean loginSuccess(String username, String password) {
		
		try {
			
			List<String> distinguishedName = findDistinguishedName(username);
			
			if (distinguishedName.isEmpty() || distinguishedName.size() > 1) {
				return false;
			}
		
			env.put(Context.SECURITY_PRINCIPAL, distinguishedName.get(0));
			env.put(Context.SECURITY_CREDENTIALS, password);
		
			new InitialDirContext(env);
			
			return true;
		} catch (Exception e) {}
		
		return false;
	}
	
	/**
	 * Find DistinguishedName for login
	 * @param username
	 * @return
	 * @throws Exception
	 */
	private List<String> findDistinguishedName(String username) throws Exception {
		
		DirContext ctx = createConnection();
		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		String search = "("+SettingUtil.getString(settings, Settings.SETTING_LDAP_IDENTIFICATOR, StringPool.BLANK)+"="+username+")";

		NamingEnumeration<SearchResult> answer = ctx.search(SettingUtil.getString(settings, Settings.SETTING_LDAP_BASE, StringPool.BLANK), search, constraints);
		
		List<String> distinguishedNameList = new ArrayList<String>();
		
		while (answer.hasMoreElements()) {
			
			SearchResult result = answer.nextElement();
			
			Attribute distinguishedAttr		= result.getAttributes().get(DISTINGUISHED_NAME);
			
			if (distinguishedAttr != null) { 
				distinguishedNameList.add(distinguishedAttr.get().toString());
			}
		}
		
		return distinguishedNameList;
	}

	/**
	 * Find users in LDAP
	 * @param name
	 * @param lastName
	 * @param username
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public List<UserLDAP> findUsers(String name, String lastName, String username, String email) throws Exception {
		
		DirContext ctx = createConnection();
		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		String search = "";
		
		if (!ValidateUtil.isNull(username)) {
			
			search = "("+SettingUtil.getString(settings, Settings.SETTING_LDAP_IDENTIFICATOR, StringPool.BLANK)+"=*"+username+"*"+") ";
		}
		
		if (!ValidateUtil.isNull(email)) {
			
			search += "("+SettingUtil.getString(settings, Settings.SETTING_LDAP_MAIL, StringPool.BLANK)+"=*"+email+"*"+") ";
		}
		
		if (!ValidateUtil.isNull(name)) {
			
			search += "("+SettingUtil.getString(settings, Settings.SETTING_LDAP_NAME, StringPool.BLANK)+"=*"+name+"*"+") ";
		}
		
		if (!ValidateUtil.isNull(lastName)) {
			
			search += "("+SettingUtil.getString(settings, Settings.SETTING_LDAP_LASTNAME, StringPool.BLANK)+"=*"+lastName+"*"+") ";
		}
        search = "(&"+search+")";

        LOGGER.debug("Filters for find in LDAP: "+search);

		NamingEnumeration<SearchResult> answer = ctx.search(SettingUtil.getString(settings, Settings.SETTING_LDAP_BASE, StringPool.BLANK), search, constraints);
		
		SecurityLogic securityLogic	= new SecurityLogic();
		
		List<UserLDAP> users = new ArrayList<UserLDAP>();
		
		while (answer.hasMoreElements()) {
			
			SearchResult result = answer.nextElement();
			
			Attribute nameAttr		= result.getAttributes().get(SettingUtil.getString(settings, Settings.SETTING_LDAP_NAME, StringPool.BLANK));
			Attribute lastNameAttr	= result.getAttributes().get(SettingUtil.getString(settings, Settings.SETTING_LDAP_LASTNAME, StringPool.BLANK));
			Attribute userAttr		= result.getAttributes().get(SettingUtil.getString(settings, Settings.SETTING_LDAP_IDENTIFICATOR, StringPool.BLANK));
			Attribute mailAttr		= result.getAttributes().get(SettingUtil.getString(settings, Settings.SETTING_LDAP_MAIL, StringPool.BLANK));
			
			UserLDAP user = new UserLDAP();
			
			if (nameAttr != null) { user.setName(nameAttr.get().toString()); }
			if (lastNameAttr != null) { user.setLastname(lastNameAttr.get().toString()); }
			if (userAttr != null) { user.setUsername(userAttr.get().toString()); }
			if (mailAttr != null) { user.setEmail(mailAttr.get().toString()); }
			
			if (!ValidateUtil.isNull(user.getUsername())) {
				
				Security security = new Security();
				security.setLogin(user.getUsername());
				
				if (!securityLogic.isUserName(security)) {
					
					users.add(user);
				}
			}
		}
		
		return users;
	}
	
	public Hashtable<String, Object> getEnv() {
		return env;
	}

	public void setEnv(Hashtable<String, Object> env) {
		this.env = env;
	}

	/**
	 * Create connection
	 * @return
	 * @throws NamingException
	 */
	public InitialDirContext createConnection() throws NamingException {

		env.put(Context.SECURITY_PRINCIPAL, SettingUtil.getString(settings, Settings.SETTING_LDAP_ACCOUNT, StringPool.BLANK));
		env.put(Context.SECURITY_CREDENTIALS, SettingUtil.getString(settings, Settings.SETTING_LDAP_PASSWORD, StringPool.BLANK));
		
		return new InitialDirContext(env);
	}

}
