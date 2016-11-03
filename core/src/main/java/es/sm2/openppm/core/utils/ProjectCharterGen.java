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
 * File: ProjectCharterGen.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


public class ProjectCharterGen {

	/**
	 * 
	 * @param templateFile
	 * @param outputFilename
	 * @param paramsHeader
	 * @param paramsDoc
	 * @throws Exception
	 */
	public File generateFromTemplate (InputStream templateFile,
									  String outputFilename, 
									  HashMap<String,String> headerParams, 
									  HashMap<String,String> docParams )
	throws Exception {
		
		WordprocessingMLPackage wordMLPackage = DocxUtil.loadPackageFromFile(templateFile);

		// HEADER
		//
		if (headerParams.size() > 0) {
			DocxUtil.replaceHeaderFields(wordMLPackage, headerParams);
		}

		// DOCUMENT
		//
		DocxUtil.replaceDocumentFields(wordMLPackage, docParams);

		// SAVE THE NEW DOCUMENT
        //
		File outputFile = new File(outputFilename);
		
		wordMLPackage.save (outputFile);
		
		return outputFile;
	}
}
