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
 * File: JsonUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Miercoles, 27 de Julio, 2011
*
* -------------------------------------------------------------------------
*/
package es.sm2.openppm.utils.json;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;

public class JsonUtil {

	public static final String DATE_PATTERN = "dd/MM/yyyy";
	
	/**
	 * Get GSON (default date pattern: dd/MM/yyyy)
	 * @param datePattern
	 * @param classExclusions
	 * @return
	 */
	public static Gson getGson(Exclusion...classExclusions) {
		
		return getGson(DATE_PATTERN, classExclusions);
	}
	
	/**
	 * Get GSON (default date pattern: dd/MM/yyyy)
	 * @param datePattern
	 * @param classExclusions
	 * @return
	 */
	public static Gson getGson(String datePattern, Exclusion...classExclusions) {
		
		GsonBuilder gsonBuilder = new GsonBuilder()
			.setDateFormat(datePattern)
			.setExclusionStrategies(classExclusions)
			.setExclusionStrategies(new Exclusion(Set.class))
			.serializeNulls();
		
		return gsonBuilder.create();
	}
	
	/**
	 * Remove Lazy relations
	 * @param object
	 * @param type
	 * @param classExclusions
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static Object removeLazy(Object object,Class<?> type, Exclusion...classExclusions) throws UnsupportedEncodingException {
		
		Gson gson = getGson(classExclusions);
		JsonElement element = gson.toJsonTree(object);
		
		Object item = gson.fromJson(element, type);
		
		return item;
	}
	
	/**
	 * Pasa un objeto a JSON
	 * @param object
	 * @param classExclusions
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String toJSON(Object object, String datePattern ,Exclusion...classExclusions) throws UnsupportedEncodingException {

		Gson gson = getGson(datePattern, classExclusions);
		return gson.toJson(object);
	}
	
	/**
	 * Pasa un objeto a JSON  (default date pattern: dd/MM/yyyy)
	 * @param object
	 * @param classExclusions
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String toJSON(Object object ,Exclusion...classExclusions) throws UnsupportedEncodingException {

		Gson gson = getGson(DATE_PATTERN, classExclusions);
		return gson.toJson(object);
	}
	
	/**
	 * Objeto JSON para llamadas void
	 * @return
	 */
	public static String infoTrue() {
		JSONObject info = new JSONObject();
		info.put(StringPool.INFO,StringPool.TRUE);
		return info.toString();
	}

	/**
	 * Create JSON Object
	 * @param key
	 * @param value
	 * @return
	 */
	public static JSONObject toJSON(String key, Object value) {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(key,value);
		
		return jsonObject;
	}
	
	/**
	 * Info created
	 * 
	 * @param resourceBoundle
	 * @param info
	 * @param key
	 * @return
	 */
	public static JSONObject infoCreated(ResourceBundle resourceBoundle, JSONObject info, String key) {
		
		return info(resourceBoundle, StringPool.SUCCESS, "msg.info.created", info, key);
	}
	
	/**
	 * Info updated
	 * 
	 * @param resourceBoundle
	 * @param info
	 * @param key
	 * @return
	 */
	public static JSONObject infoUpdated(ResourceBundle resourceBoundle, JSONObject info, String key) {
		
		return info(resourceBoundle, StringPool.SUCCESS, "msg.info.updated", info, key);
	}

	/**
	 * Info deleted
	 * 
	 * @param resourceBoundle
	 * @param key
	 * @return
	 */
	public static JSONObject infoDeleted(ResourceBundle resourceBoundle, String key) {
		
		return info(resourceBoundle, StringPool.SUCCESS, "msg.info.deleted", null, key);
	}
	
	/**
	 * Put info
	 * 
	 * @param type
	 * @param objectJSON
	 * @param infoJSON
	 * @return
	 */
	public static JSONObject putInfo(String type, JSONObject objectJSON, JSONObject infoJSON) {
		
		if (objectJSON != null && infoJSON != null) {
		
			JSONArray information = infoJSON.getJSONArray(type);
			
			if (information != null) {
				objectJSON.put(type, information);
			}
		}
		return objectJSON;
	}
	
	/**
	 * Info
	 * 
	 * @param resourceBoundle
	 * @param type
	 * @param key
	 * @param objectJSON
	 * @param args
	 * @return
	 */
	public static JSONObject info(ResourceBundle resourceBoundle, String type, String key, JSONObject objectJSON, Object...args) {
		
		if (objectJSON == null) { objectJSON = new JSONObject(); }
		
		JSONArray information = null;
		try {
			information = objectJSON.getJSONArray(type);
			objectJSON.remove(type);
		}
		catch (Exception e) {
			information = new JSONArray();
		}

		ParamResourceBundle msgFormat = new ParamResourceBundle(key, args);
		msgFormat.setBlack(true);
		
		information.add(msgFormat.getMessage(resourceBoundle));
		objectJSON.put(type, information);
		
		return objectJSON;
	}
	
	/**
	 * Info
	 * 
	 * @param type
	 * @param value
	 * @param objectJSON
	 * @return
	 */
	public static JSONObject info(String type, String value, JSONObject objectJSON) {
		
		if (objectJSON == null) { objectJSON = new JSONObject(); }
		
		JSONArray information = null;
		try {
			information = objectJSON.getJSONArray(type);
			objectJSON.remove(type);
		}
		catch (Exception e) {
			information = new JSONArray();
		}

		information.add(value);
		objectJSON.put(type, information);
		
		return objectJSON;
	}
	
}
