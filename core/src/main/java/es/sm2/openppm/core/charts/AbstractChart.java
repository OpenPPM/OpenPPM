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
 * File: AbstractChart.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public abstract class AbstractChart {
	
	private Document document;
	private ResourceBundle idioma;
	
	public AbstractChart(ResourceBundle idioma) throws ParserConfigurationException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		this.document	= db.newDocument();
		this.idioma 	= idioma;
	}
	
	/**
	 * Generate XML File
	 * @return
	 * @throws TransformerException
	 */
	public String generateXML() throws TransformerException {
		
		createXML();
		
		Source source				= new DOMSource(this.document);
		StringWriter stringWriter	= new StringWriter();
		Result result				= new StreamResult(stringWriter);
		
        TransformerFactory factory	= TransformerFactory.newInstance();
        Transformer transformer		= factory.newTransformer();
        
        transformer.transform(source, result);
        
        return stringWriter.getBuffer().toString();
	}
	/**
	 * Create XML file
	 * @return
	 */
	public abstract void createXML();
	
	
	public Document getDocument() {
		return this.document;
	}

	public ResourceBundle getIdioma() {
		return idioma;
	}


}
