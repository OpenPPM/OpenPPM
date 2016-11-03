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
 * File: HtmlUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.functions;

import es.sm2.openppm.utils.StringUtil;

/**
 * @author daniel.casas
 * @version 1.0 - 29-mar-2010 12:15:16
 */
public class HtmlUtil {


	/**
	 * Escape special characters
	 * @param text
	 * @return
	 */
	public static String escape(String text) {

		if (text == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(text.length());

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			switch (c) {
				case '<':
					sb.append("&lt;");

					break;

				case '>':
					sb.append("&gt;");

					break;

				case '&':
					sb.append("&amp;");

					break;

				case '"':
					sb.append("&#034;");

					break;

				case '\'':
				case 146:
					sb.append("&#039;");

					break;
					
				case 128:
					sb.append("&euro;");

					break;	
					
				default:
					sb.append(c);

					break;
			}
		}

		return sb.toString();
	}

	/**
	 * Change special character to unicode
	 *
	 * @param text
	 * @return
	 */
	public static String escapeUnicode(String text) {

		if (text == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(text.length());

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			switch (c) {
				case '<':
					sb.append("\\u003C");

					break;

				case '>':
					sb.append("\\u003E");

					break;

				case '&':
					sb.append("\\u0026");

					break;

				case '"':
					sb.append("\\u0022");

					break;
				case '\n':
					sb.append("\\u000A");

					break;
				case '\r':
					sb.append("\\u000D");

					break;
				case '\b':
					sb.append("\\u0008");

					break;
				case '\f':
					sb.append("\\u000C");

					break;

				case '\'':
				case 146:
					sb.append("\\u0027");

					break;

				case 128:
					sb.append("\\u20AC");

					break;

				default:
					sb.append(c);

					break;
			}
		}

		return sb.toString();
	}
	
	/**
	 * Replace <> for HTML representation
	 * 
	 * @param text
	 * @return
	 */
	public static String escapeTag(String text) {

		if (text == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(text.length());

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			switch (c) {
				case '<':
					sb.append("&lt;");

					break;

				case '>':
					sb.append("&gt;");

					break;

				default:
					sb.append(c);

					break;
			}
		}

		return sb.toString();
	}

	/**
	 * Un escape special characters
	 * @param text
	 * @return
	 */
	public static String unescape(String text) {

		if (text == null) {
			return null;
		}

		// Optimize this

		text = StringUtil.replace(text, "&lt;", "<");
		text = StringUtil.replace(text, "&gt;", ">");
		text = StringUtil.replace(text, "&amp;", "&");
		text = StringUtil.replace(text, "&#034;", "\"");
		text = StringUtil.replace(text, "&#039;", "'");

		return text;
	}
	
	/**
	 * Escape accents
	 * 
	 * @param text
	 * @return
	 */
	public static String escapeAccents (String text) {
		
		if (text==null) {
			return ""; 
		}
	    StringBuffer out=new StringBuffer(""); 
	    char[] chars=text.toCharArray(); 
	    
	    for (int i=0;i<chars.length;i++) { 
	        boolean found=true; 
	        
	        switch(chars[i]) { 
		        case 38:out.append("&amp;"); break; 
		        case 198:out.append("&AElig;"); break; 
		        case 193:out.append("&Aacute;"); break; 
		        case 194:out.append("&Acirc;"); break; 
		        case 192:out.append("&Agrave;"); break; 
		        case 197:out.append("&Aring;"); break; 
		        case 195:out.append("&Atilde;"); break; 
		        case 196:out.append("&Auml;"); break; 
		        case 199:out.append("&Ccedil;"); break; 
		        case 208:out.append("&ETH;"); break; 
		        case 201:out.append("&Eacute;"); break; 
		        case 202:out.append("&Ecirc;"); break; 
		        case 200:out.append("&Egrave;"); break; 
		        case 203:out.append("&Euml;"); break; 
		        case 205:out.append("&Iacute;"); break; 
		        case 206:out.append("&Icirc;"); break; 
		        case 204:out.append("&Igrave;"); break; 
		        case 207:out.append("&Iuml;"); break; 
		        case 209:out.append("&Ntilde;"); break; 
		        case 211:out.append("&Oacute;"); break; 
		        case 212:out.append("&Ocirc;"); break; 
		        case 210:out.append("&Ograve;"); break; 
		        case 216:out.append("&Oslash;"); break; 
		        case 213:out.append("&Otilde;"); break; 
		        case 214:out.append("&Ouml;"); break; 
		        case 222:out.append("&THORN;"); break; 
		        case 218:out.append("&Uacute;"); break; 
		        case 219:out.append("&Ucirc;"); break; 
		        case 217:out.append("&Ugrave;"); break; 
		        case 220:out.append("&Uuml;"); break; 
		        case 221:out.append("&Yacute;"); break; 
		        case 225:out.append("&aacute;"); break; 
		        case 226:out.append("&acirc;"); break; 
		        case 230:out.append("&aelig;"); break; 
		        case 224:out.append("&agrave;"); break; 
		        case 229:out.append("&aring;"); break; 
		        case 227:out.append("&atilde;"); break; 
		        case 228:out.append("&auml;"); break; 
		        case 231:out.append("&ccedil;"); break; 
		        case 233:out.append("&eacute;"); break; 
		        case 234:out.append("&ecirc;"); break; 
		        case 232:out.append("&egrave;"); break; 
		        case 240:out.append("&eth;"); break; 
		        case 235:out.append("&euml;"); break; 
		        case 237:out.append("&iacute;"); break; 
		        case 238:out.append("&icirc;"); break; 
		        case 236:out.append("&igrave;"); break; 
		        case 239:out.append("&iuml;"); break; 
		        case 241:out.append("&ntilde;"); break; 
		        case 243:out.append("&oacute;"); break; 
		        case 244:out.append("&ocirc;"); break; 
		        case 242:out.append("&ograve;"); break; 
		        case 248:out.append("&oslash;"); break; 
		        case 245:out.append("&otilde;"); break; 
		        case 246:out.append("&ouml;"); break; 
		        case 223:out.append("&szlig;"); break; 
		        case 254:out.append("&thorn;"); break; 
		        case 250:out.append("&uacute;"); break; 
		        case 251:out.append("&ucirc;"); break; 
		        case 249:out.append("&ugrave;"); break; 
		        case 252:out.append("&uuml;"); break; 
		        case 253:out.append("&yacute;"); break; 
		        case 255:out.append("&yuml;"); break; 
		        case 162:out.append("&cent;"); break; 
		        default: 
		          found=false; 
		          break; 
	        } 
	      
	        if (!found) {
	        	out.append(chars[i]);
	        } 
	    }   
	    return out.toString(); 
	}
}
