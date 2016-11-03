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
 * Module: utils
 * File: LogManager.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.NullEnumeration;

import es.sm2.openppm.utils.functions.ValidateUtil;

/**
 * 
 * @author javier.hernandez
 * 
 */
public class LogManager {

	public static final String SYSTEM_PROPERTY_LOG_CONSOLE = "SYSTEM_PROPERTY_LOG_CONSOLE";
	
	private static final String FORMAT_APPENDER = "%d{yyyy-MM-dd HH:mm:ss.SSSS} | %5p %c{1}:%L - %m%n";
	
	public LogManager() {
	}

	private static boolean activeConsoleLog() {
		
		String activeProperty = System.getProperty(SYSTEM_PROPERTY_LOG_CONSOLE);
		
		return ValidateUtil.isNotNull(activeProperty) && Boolean.valueOf(activeProperty);
	}
	private static ConsoleAppender createConsoleAppender() {
		
		return new ConsoleAppender(new PatternLayout(FORMAT_APPENDER));
	}
	
	/**
	 * Create Logger by name class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getLog(String clazz) {
		
		return getLog(clazz, activeConsoleLog());
	}

	/**
	 * Create Logger by class 
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getLog(Class<?> clazz) {
		
		return getLog(clazz, activeConsoleLog());
	}
	
	/**
	 * Create Logger by name class, print in console.
	 * Only for developer or JUnit
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getConsoleLog(String clazz) {
		
		return getLog(clazz, true);
	}

	/**
	 * Create Logger by class, print in console.
	 * Only for developer or JUnit
	 * 
	 * @param clazz
	 * @return
	 */
	public static Logger getConsoleLog(Class<?> clazz) {
		
		return getLog(clazz, true);
	}
	
	private static Logger getLog(String clazz, boolean console) {
		
		Logger logger = Logger.getLogger(clazz);
		
		if (console) {
			logger.addAppender(createConsoleAppender());
		}
		
		return logger;
	}

	private static Logger getLog(Class<?> clazz, boolean console) {
		
		Logger logger = Logger.getLogger(clazz);
		
		// Add console appender if not configuration log4j and is forced
		if (console && logger.getAllAppenders() instanceof NullEnumeration) {
			logger.addAppender(createConsoleAppender());
		}
		
		return logger;
	}
}
