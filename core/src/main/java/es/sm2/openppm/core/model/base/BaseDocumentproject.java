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
 * File: BaseDocumentproject.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Documentproject
 * @see BaseDocumentproject
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Documentproject
 */
public class BaseDocumentproject  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "documentproject";
	
	public static final String IDDOCUMENTPROJECT = "idDocumentProject";
	public static final String PROJECT = "project";
	public static final String LINK = "link";
	public static final String TYPE = "type";
	public static final String MIME = "mime";
	public static final String EXTENSION = "extension";
	public static final String NAME = "name";
	public static final String CONTENTCOMMENT = "contentComment";
	public static final String CREATIONCONTACT = "creationContact";
     public static final String CREATIONDATE = "creationDate";
     public static final String CLOSURECHECKPROJECTS = "closurecheckprojects";
     public static final String TIMELINES = "timelines";

     private Integer idDocumentProject;
     private Project project;
     private String link;
     private String type;
     private String mime;
     private String extension;
     private String name;
     private String contentComment;
	 private String creationContact;
     private Date creationDate;
     private Set<Closurecheckproject> closurecheckprojects = new HashSet<Closurecheckproject>(0);
     private Set<Timeline> timelines = new HashSet<Timeline>(0);

    public BaseDocumentproject() {
    }

     /**
      * Getter for property 'timelines'.
      *
      * @return Value for property 'timelines'.
      */
     public Set<Timeline> getTimelines() {
         return timelines;
     }

     /**
      * Setter for property 'timelines'.
      *
      * @param timelines Value to set for property 'timelines'.
      */
     public void setTimelines(Set<Timeline> timelines) {
         this.timelines = timelines;
     }

     public BaseDocumentproject(Integer idDocumentProject) {
    	this.idDocumentProject = idDocumentProject;
    }
   
    public Integer getIdDocumentProject() {
        return this.idDocumentProject;
    }
    
    public void setIdDocumentProject(Integer idDocumentProject) {
        this.idDocumentProject = idDocumentProject;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getLink() {
        return this.link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getMime() {
        return this.mime;
    }
    
    public void setMime(String mime) {
        this.mime = mime;
    }
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getContentComment() {
        return this.contentComment;
    }
    
    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }
    public Set<Closurecheckproject> getClosurecheckprojects() {
        return this.closurecheckprojects;
    }
    
    public void setClosurecheckprojects(Set<Closurecheckproject> closurecheckprojects) {
        this.closurecheckprojects = closurecheckprojects;
    }

     public String getCreationContact() {
         return creationContact;
     }

     public void setCreationContact(String creationContact) {
         this.creationContact = creationContact;
     }

     public Date getCreationDate() {
         return creationDate;
     }

     public void setCreationDate(Date creationDate) {
         this.creationDate = creationDate;
     }

     @Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Documentproject) )  { result = false; }
		 else if (other != null) {
		 	Documentproject castOther = (Documentproject) other;
			if (castOther.getIdDocumentProject().equals(this.getIdDocumentProject())) { result = true; }
         }
		 return result;
   }


}


