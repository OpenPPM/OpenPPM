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
 * File: DocxUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Document;
import org.docx4j.wml.Hdr;

/**
 * Utility class for docx documents 
 * 
 * @author daniel.casas
 *
 */
public class DocxUtil {

	
	/**
	 * 
	 * @param wordMLPackage
	 * @param params
	 * @return
	 * @throws JAXBException
	 */
	public static WordprocessingMLPackage replaceHeaderFields (
			WordprocessingMLPackage wordMLPackage, HashMap<String,String> params) 
	throws JAXBException {
		
		try {
			Hdr header = wordMLPackage.getDocumentModel().getSections().get(0).getHeaderFooterPolicy().getDefaultHeader().getJaxbElement();
			
			String hdrStr = XmlUtils.marshaltoString (header, true, true);
	
			header = (Hdr)XmlUtils.unmarshallFromTemplate(hdrStr, params);
			
			wordMLPackage.getDocumentModel().getSections().get(0).getHeaderFooterPolicy().getDefaultHeader().setJaxbElement(header);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
			
		return wordMLPackage;
	}
	

	/**
	 * 
	 * @param wordMLPackage
	 * @param params
	 * @return
	 * @throws JAXBException
	 */
	public static WordprocessingMLPackage replaceDocumentFields (
			WordprocessingMLPackage wordMLPackage, HashMap<String,String> params) 
	throws JAXBException {
	
		try {
			Document document = wordMLPackage.getMainDocumentPart().getJaxbElement();
			
			String docStr = XmlUtils.marshaltoString (document, true, true);
			document = (Document)XmlUtils.unmarshallFromTemplate (docStr, params);
			
			wordMLPackage.getMainDocumentPart().setJaxbElement(document);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
			
		return wordMLPackage;
	}
	

	/**
	 * 
	 * @param templateFile
	 * @return
	 * @throws Exception
	 */
	public static WordprocessingMLPackage loadPackageFromFile (InputStream templateFile) 
	throws Exception {
		
		// Load the Package
		return WordprocessingMLPackage.load(templateFile);
	}
	
	
}
