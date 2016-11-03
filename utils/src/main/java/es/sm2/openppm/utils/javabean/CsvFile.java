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
 * File: CsvFile.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.javabean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class CsvFile {

	public final static Logger LOGGER = Logger.getLogger(CsvFile.class);
	
	private StringBuffer fileStream = new StringBuffer();
	private char separator;
	private boolean newline;
	
	/**
	 * Create file CSV
	 * 
	 * @param separator <Not null not supported> is the character to separate the elements
	 */
	public CsvFile(char separator) {
		this.separator = separator;
		this.newline = true;
	}
	
	/**
	 * Return file in array bytes
	 * @return
	 */
	public byte[] getFileBytes() {
		
		byte[] data = null;
		try {
			data = fileStream.toString().getBytes("windows-1252");
		}
		catch (UnsupportedEncodingException e) {
			LOGGER.error("Unsupported Encoding: 'windows-1252' for generate CSV file", e);
		}
		
		return data;
	}
	
	/**
	 * Add Value
	 * @param value
	 * @throws java.io.IOException
	 */
	public void addValue(String value) throws IOException {
		if (newline) {
			newline = false;
		}
        else {
        	fileStream.append(separator);
        }
		if (ValidateUtil.isNotNull(value)) {
			fileStream.append(value.replace(separator, separator == ';'?',':';'));
		}
		else {
			fileStream.append(StringPool.BLANK);
		}
	}
	
	/**
	 * New line
	 * @throws java.io.IOException
	 */
	public void newLine() throws IOException {
		String line = "\n";
        this.fileStream.append(line);
        newline = true;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public char getSeparator() {
		return separator;
	}
}
