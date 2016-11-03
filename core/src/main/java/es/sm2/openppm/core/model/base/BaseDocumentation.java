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
 * File: BaseDocumentation.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Documentation
 * @see es.sm2.openppm.core.model.base.Documentation
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Documentation
 */
public class BaseDocumentation  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "documentation";
	
	public static final String IDDOCUMENTATION = "idDocumentation";
	public static final String CONTENTFILE = "contentfile";
	public static final String COMPANY = "company";
	public static final String NAMEFILE = "nameFile";
	public static final String NAMEPOPUP = "namePopup";

     private Integer idDocumentation;
     private Contentfile contentfile;
     private Company company;
     private String nameFile;
     private String namePopup;

    public BaseDocumentation() {
    }
    
    public BaseDocumentation(Integer idDocumentation) {
    	this.idDocumentation = idDocumentation;
    }
   
    public Integer getIdDocumentation() {
        return this.idDocumentation;
    }
    
    public void setIdDocumentation(Integer idDocumentation) {
        this.idDocumentation = idDocumentation;
    }
    public Contentfile getContentfile() {
        return this.contentfile;
    }
    
    public void setContentfile(Contentfile contentfile) {
        this.contentfile = contentfile;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getNameFile() {
        return this.nameFile;
    }
    
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
    public String getNamePopup() {
        return this.namePopup;
    }
    
    public void setNamePopup(String namePopup) {
        this.namePopup = namePopup;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Documentation) )  { result = false; }
		 else if (other != null) {
		 	Documentation castOther = (Documentation) other;
			if (castOther.getIdDocumentation().equals(this.getIdDocumentation())) { result = true; }
         }
		 return result;
   }


}


