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
 * File: ReflectUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

public final class ReflectUtil {

	/**
	 * Perform resolution of a class name.
	 * <p/>
	 * Here we first check the context classloader, if one, before delegating to
	 * {@link Class#forName(String, boolean, ClassLoader)} using the caller's classloader
	 *
	 * @param name The class name
	 * @param caller The class from which this call originated (in order to access that class's loader).
	 * @return The class reference.
	 * @throws ClassNotFoundException From {@link Class#forName(String, boolean, ClassLoader)}.
	 */
	@SuppressWarnings("rawtypes")
	public static Class classForName(String name, Class caller) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if ( contextClassLoader != null ) {
				return contextClassLoader.loadClass( name );
			}
		}
		catch ( Throwable ignore ) {
		}
		return Class.forName( name, true, caller.getClassLoader() );
	}

	
	/**
	 * Perform resolution of a class name.
	 * <p/>
	 * Same as {@link #classForName(String, Class)} except that here we delegate to
	 * {@link Class#forName(String)} if the context classloader lookup is unsuccessful.
	 *
	 * @param name The class name
	 * @return The class reference.
	 * @throws ClassNotFoundException From {@link Class#forName(String)}.
	 */
	@SuppressWarnings("rawtypes")
	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if ( contextClassLoader != null ) {
				return contextClassLoader.loadClass(name);
			}
		}
		catch ( Throwable ignore ) {
		}
		return Class.forName( name );
	}
}
