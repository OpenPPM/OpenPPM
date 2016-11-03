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
 * File: BaseContentfile.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Companyfile;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.Pluginfile;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Contentfile
 * @see es.sm2.openppm.core.model.base.Contentfile
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Contentfile
 */
public class BaseContentfile  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "contentfile";
	
	public static final String IDCONTENTFILE = "idContentFile";
	public static final String CONTENT = "content";
	public static final String EXTENSION = "extension";
	public static final String MIME = "mime";
	public static final String DOCUMENTATIONS = "documentations";
	public static final String PLUGINFILES = "pluginfiles";
	public static final String COMPANYFILES = "companyfiles";

     private Integer idContentFile;
     private byte[] content;
     private String extension;
     private String mime;
     private Set<Documentation> documentations = new HashSet<Documentation>(0);
     private Set<Pluginfile> pluginfiles = new HashSet<Pluginfile>(0);
     private Set<Companyfile> companyfiles = new HashSet<Companyfile>(0);

    public BaseContentfile() {
    }
    
    public BaseContentfile(Integer idContentFile) {
    	this.idContentFile = idContentFile;
    }
   
    public Integer getIdContentFile() {
        return this.idContentFile;
    }
    
    public void setIdContentFile(Integer idContentFile) {
        this.idContentFile = idContentFile;
    }
    public byte[] getContent() {
        return this.content;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getMime() {
        return this.mime;
    }
    
    public void setMime(String mime) {
        this.mime = mime;
    }
    public Set<Documentation> getDocumentations() {
        return this.documentations;
    }
    
    public void setDocumentations(Set<Documentation> documentations) {
        this.documentations = documentations;
    }
    public Set<Pluginfile> getPluginfiles() {
        return this.pluginfiles;
    }
    
    public void setPluginfiles(Set<Pluginfile> pluginfiles) {
        this.pluginfiles = pluginfiles;
    }
    public Set<Companyfile> getCompanyfiles() {
        return this.companyfiles;
    }
    
    public void setCompanyfiles(Set<Companyfile> companyfiles) {
        this.companyfiles = companyfiles;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Contentfile) )  { result = false; }
		 else if (other != null) {
		 	Contentfile castOther = (Contentfile) other;
			if (castOther.getIdContentFile().equals(this.getIdContentFile())) { result = true; }
         }
		 return result;
   }


}


