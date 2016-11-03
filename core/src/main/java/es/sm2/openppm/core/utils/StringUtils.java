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
 * File: StringUtils.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.Random;

public class StringUtils {

	protected StringUtils() {}
	
	/**
	 * Encrypt String
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String md5(String text) throws Exception {
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(text.getBytes());
		
		int size = b.length;
		
		StringBuffer encryptStr = new StringBuffer(size);
		
		for (int i = 0; i < size; i++) {
			
			int u = b[i] & 255;
			if (u < 16) {
				encryptStr.append("0" + Integer.toHexString(u));
			}
			else {
				encryptStr.append(Integer.toHexString(u));
			}
		}
		
		return encryptStr.toString();
	}
	
	/**
	 * Generate a random string
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length){
		
		long milis	= new java.util.GregorianCalendar().getTimeInMillis();
		Random r	= new Random(milis);
		
		int i = 0;
		
		String randomStr = "";
		
		while (i < length) {
			char c = (char) r.nextInt(255);
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				randomStr += c;
				i++;
			}
		}
		
		return randomStr;
	}

    /**
     * Formatting text accents and special characters and put lower case
     *
     * @param text
     * @return
     */
    public static String formatting(String text) {

        String formatted = "";

        if (text != null) {

            // Replace Unicode accents and diacritics
            formatted = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            // Replace special characters
            formatted = formatted.replaceAll("([^\\w\\d.])", "_");

            // Lower case
            formatted = formatted.toLowerCase();
        }

        return formatted;
    }
}

