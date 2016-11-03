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
 * File: LocalicedException.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.exceptions;

import java.util.ResourceBundle;

import es.sm2.openppm.utils.javabean.ParamResourceBundle;

public class LocalicedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[] param;

	public LocalicedException(String msg, String...param) {
		super(msg);
		this.param = param;
	}
	
	public String getMessage(ResourceBundle idioma) {
		String error = "";
		if (param != null) {
			
			if (param.length > 1) {
				
				Object[] values = new String[param.length];
				int i = 0;
				for (Object item : param) {
					try {
						values[i] = idioma.getString((String)item);
					}
					catch (Exception e){
						values[i] = item;
					}
					i++;
				}
				error = new ParamResourceBundle(this.getMessage(), values).toString(idioma);
			}
			else {
				error = new ParamResourceBundle(this.getMessage(), this.param).toString(idioma);
			}
		}
		else {
			error = idioma.getString(this.getMessage());
		}
		return error;
	}
	
	public LocalicedException(Throwable cause) {
        super(cause);
    }

	public LocalicedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
