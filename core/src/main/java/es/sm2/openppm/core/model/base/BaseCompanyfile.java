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
 * File: BaseCompanyfile.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Companyfile;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Companyfile
 * @see es.sm2.openppm.core.model.base.Companyfile
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Companyfile
 */
public class BaseCompanyfile  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "companyfile";
	
	public static final String IDCOMPANYFILE = "idCompanyfile";
	public static final String CONTENTFILE = "contentfile";
	public static final String COMPANY = "company";
	public static final String TYPEFILE = "typeFile";

     private Integer idCompanyfile;
     private Contentfile contentfile;
     private Company company;
     private String typeFile;

    public BaseCompanyfile() {
    }
    
    public BaseCompanyfile(Integer idCompanyfile) {
    	this.idCompanyfile = idCompanyfile;
    }
   
    public Integer getIdCompanyfile() {
        return this.idCompanyfile;
    }
    
    public void setIdCompanyfile(Integer idCompanyfile) {
        this.idCompanyfile = idCompanyfile;
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
    public String getTypeFile() {
        return this.typeFile;
    }
    
    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Companyfile) )  { result = false; }
		 else if (other != null) {
		 	Companyfile castOther = (Companyfile) other;
			if (castOther.getIdCompanyfile().equals(this.getIdCompanyfile())) { result = true; }
         }
		 return result;
   }


}


