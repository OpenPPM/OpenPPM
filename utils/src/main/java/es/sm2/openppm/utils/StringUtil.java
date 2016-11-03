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
 * File: StringUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;


public final class StringUtil {

	private StringUtil() {
		super();
	}
	
	public static String cut(String s, int length) {
		
		if (s != null && length >= 0 && s.length() > length) {
			return s.substring(0,length);
		}
		return s;
	}
	
	public static String shorten(String s, int length) {
		return shorten(s, length, "...");
	}

	public static String shorten(String s, String suffix) {
		return shorten(s, 20, suffix);
	}

	public static String shorten(String s, int length, String suffix) {
		
		StringBuilder sb = null;

		if (s != null && suffix != null && s.length() > length) {
			for (int j = length; j >= 0; j--) {
				if (Character.isWhitespace(s.charAt(j))) {
					length = j;

					break;
				}
			}

			sb = new StringBuilder();
			
			sb.append(s.substring(0, length));
			sb.append(suffix);

		}

		return (sb == null ? null : sb.toString());
	}

	public static String upperCase(String s) {
		return (s == null ? null : s.toUpperCase());
	}

	public static String upperCaseFirstLetter(String s) {
		char[] chars = s.toCharArray();

		if ((chars[0] >= 97) && (chars[0] <= 122)) {
			chars[0] = (char)(chars[0] - 32);
		}

		return new String(chars);
	}

	
	public static String replace(String s, String oldSub, String newSub) {
		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}

		int y = s.indexOf(oldSub);

		if (y >= 0) {

			// The number 5 is arbitrary and is used as extra padding to reduce
			// buffer expansion

			StringBuilder sb = new StringBuilder(
				s.length() + 5 * newSub.length());

			int length = oldSub.length();
			int x = 0;

			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);

				x = y + length;
				y = s.indexOf(oldSub, x);
			}

			sb.append(s.substring(x));

			return sb.toString();
		}
		else {
			return s;
		}
	}
	
	
	/**
	 *   
	 * @param num
	 * @param length
	 * @return
	 */
	public static String fillWithZeros (int num, int length) {
        
        return fill (num, length, '0');
    }
	
	
	/**
	 *   
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillWithZeros (String str, int length) {
        
        return fill (str, length, '0');
    }

	
	/**
	 *   
	 * @param num
	 * @param length
	 * @param c
	 * @return
	 */
	public static String fill(int num, int length, char c) {
        String sNumero;
        for (sNumero = StringPool.BLANK + num; sNumero.length() < length; sNumero = c + sNumero);
        return sNumero;
    }
	
	
	/**
	 *   
	 * @param str
	 * @param length
	 * @param c
	 * @return
	 */
	public static String fill(String str, int length, char c) {
        String sNumero;
        for (sNumero = StringPool.BLANK + str; sNumero.length() < length; sNumero = c + sNumero);
        return sNumero;
    }
	
	
	/**
	 * 
	 * @param f
	 * @return
	 */
	public static String formatEuro(Double f) {
		if(f == null) { return StringPool.BLANK; }
		return String.format("%.2f", f);
	}

	/**
	 * Parse null to String Blank
	 * @param object
	 * @return
	 */
	public static Object nullEmpty(Object object) {
		return (object == null?StringPool.BLANK:object);
	}
	
	/**
	 *  Parse a string comma separated values in an Integer array
	 * @param str
	 * @return
	 */
	public static Integer[] splitStrToIntegers(String str) {
		return splitStrToIntegers(str, null);
	}
	
	/**
	 * Parse a string comma separated values in an Integer array max length
	 * @param str
     * @param max
	 * @return
	 */
	public static Integer[] splitStrToIntegers(String str, Integer max) {

		Integer[] integerArray = null;
		if (str != null && !str.equals("null") && !str.equalsIgnoreCase("")){ //TODO !str.equals("null") is for IE
			String[] strArray = str.split(",");
			integerArray = new Integer[strArray.length];
			for (int i=0; i<strArray.length; i++) {
				if (max != null && i >= max ) { break; }
				integerArray[i] = Integer.parseInt(strArray[i]);
			}
		}
		return integerArray;
	}

    /**
     * Length string
     *
     * @param str
     * @return
     */
	public static int lengthIntegers(String str) {
		
		int length = 0;
		
	    if (str != null && !str.equals("null") && !str.equalsIgnoreCase("")){ //TODO !str.equals("null") is for IE
	         String[] strArray = str.split(",");
	         length =  strArray.length;
	    }
	    return length;
	}
	
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
			} else {
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
		String randomStr = "";
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while ( i < length){
			char c = (char)r.nextInt(255);
			if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') || (c >='a' && c <='z')){
				randomStr += c;
				i ++;
			}
		}
		return randomStr;
	}
	
	public static String clearSlashes (String name) {
		String s;
		s = name.replace(":", "-");
		s = s.replace("\\", "-");
		return s.replace("/", "-");		
	}

    /**
     * Convert list to string only witch comma
     *
     * @param list
     * @return
     */
    public static String toStringListInt(List<Integer> list) {

        String result = StringPool.BLANK;

        if (list != null && !list.isEmpty()) {

            result = list.toString();

            result = result.replace(StringPool.OPEN_BRACKET, StringPool.BLANK);
            result = result.replace(StringPool.CLOSE_BRACKET, StringPool.BLANK);

            result = result.replaceAll("\\s+", StringPool.BLANK);
        }

        return result;
    }

    /**
     * Convert list to string only witch comma
     *
     * @param list
     * @return
     */
    public static String toStringListStr(List<String> list) {

        String result = StringPool.BLANK;

        if (list != null && !list.isEmpty()) {

            result = list.toString();

            result = result.replace(StringPool.OPEN_BRACKET, StringPool.BLANK);
            result = result.replace(StringPool.CLOSE_BRACKET, StringPool.BLANK);

            result = result.replaceAll("\\s+", StringPool.BLANK);
        }

        return result;
    }
}
