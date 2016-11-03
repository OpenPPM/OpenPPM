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
 * File: StringPool.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

public final class StringPool {
	
	private StringPool() {
		super();
	}

	public static final String AMPERSAND = "&";

	public static final String AMPERSAND_ENCODED = "&amp;";

	public static final String APOSTROPHE = "'";

	public static final String AT = "@";

	public static final String BACK_SLASH = "\\";

	public static final String BETWEEN = "BETWEEN";

	public static final String BLANK = "";
	
	public static final String BLANK_DASH = " - ";

	public static final String CDATA_OPEN = "<![CDATA[";

	public static final String CDATA_CLOSE = "]]>";

	public static final String CLOSE_BRACKET = "]";

	public static final String CLOSE_CURLY_BRACE = "}";

	public static final String CLOSE_H7 = "</h7>";
	
	public static final String CLOSE_LI = "</li>";
	
	public static final String CLOSE_P = "</p>";
	
	public static final String CLOSE_SPAN = "</span>";
	
	public static final String CLOSE_UL = "</ul>";
	
	public static final String CLOSE_PARENTHESIS = ")";

	public static final String COLON = ":";
	
	public static final String COLON_SPACE = ": ";

	public static final String COMMA = ",";

	public static final String COMMA_AND_SPACE = ", ";

	public static final String DASH = "-";

	public static final String DOUBLE_APOSTROPHE = "''";

	public static final String DOUBLE_CLOSE_BRACKET = "]]";

	public static final String DOUBLE_CLOSE_CURLY_BRACE = "}}";

	public static final String DOUBLE_OPEN_BRACKET = "[[";

	public static final String DOUBLE_OPEN_CURLY_BRACE = "{{";

	public static final String DOUBLE_SLASH = "//";

	public static final String EQUAL = "=";

	public static final String GREATER_THAN = ">";

	public static final String GREATER_THAN_OR_EQUAL = ">=";

	public static final String FALSE = "false";

	public static final String FORWARD_SLASH = "/";

	public static final String FOUR_SPACES = "    ";

	public static final String IS_NOT_NULL = "IS NOT NULL";

	public static final String IS_NULL = "IS NULL";

	public static final String LESS_THAN = "<";

	public static final String LESS_THAN_OR_EQUAL = "<=";

	public static final String LIKE = "LIKE";

	public static final String MINUS = "-";

	public static final String NBSP = "&nbsp;";

	public static final String NEW_LINE = "\n";

	public static final String NOT_EQUAL = "!=";

	public static final String NOT_LIKE = "NOT LIKE";

	public static final String NULL = "null";

	public static final String OPEN_BRACKET = "[";

	public static final String OPEN_CURLY_BRACE = "{";

	public static final String OPEN_H7 = "<h7>";
	
	public static final String OPEN_LI = "<li>";
	
	public static final String OPEN_P = "<p>";
	
	public static final String OPEN_SPAN = "<span>";
	
	public static final String OPEN_UL = "<ul>";
	
	public static final String OPEN_PARENTHESIS = "(";

	public static final String PERCENT = "%";

	public static final String PERIOD = ".";

	public static final String PIPE = "|";

	public static final String PLUS = "+";

	public static final String POINT = ".";

	public static final String POUND = "#";

	public static final String QUESTION = "?";

	public static final String QUOTE = "\"";

	public static final String RETURN = "\r";

	public static final String RETURN_NEW_LINE = "\r\n";

	public static final String SEMICOLON = ";";

	public static final String SLASH = FORWARD_SLASH;

	public static final String SPACE = " ";

	public static final String STAR = "*";

	public static final String TAB = "\t";

	public static final String TILDE = "~";

	public static final String TRUE = "true";

	public static final String UNDERLINE = "_";

	public static final String UTC = "UTC";

	public static final String UTF8 = "UTF-8";
	
	public static final String END_DIV = "</div>";
	
	public static final String COLOR_WHITE = "ffffff";
	
	public static final String INFO = "info";
	
	public static final String ERROR = "error";
	
	public static final String INFORMATION = "information";
	
	public static final String SUCCESS = "success";
	
	public static final String DECIMAL_FORMAT = "0.00";
	
	
	public enum InfoType {
		SUCCESS("success"),
		INFORMATION("information"),
		ERROR("error");
		
		private String name;
		
		private InfoType(String name) {
			this.name = name;
		}

		/**
		 * @return the type
		 */
		public String getName() {
			return name;
		}
	}
}
