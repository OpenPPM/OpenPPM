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
 * File: BaseWbstemplate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Wbstemplate
 * @see es.sm2.openppm.core.model.base.Wbstemplate
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Wbstemplate
 */
public class BaseWbstemplate  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "wbstemplate";
	
	public static final String IDWBSNODE = "idWbsnode";
	public static final String WBSTEMPLATE = "wbstemplate";
	public static final String COMPANY = "company";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ISCONTROLACCOUNT = "isControlAccount";
	public static final String ROOT = "root";
	public static final String WBSTEMPLATES = "wbstemplates";

     private Integer idWbsnode;
     private Wbstemplate wbstemplate;
     private Company company;
     private String code;
     private String name;
     private String description;
     private Boolean isControlAccount;
     private Integer root;
     private Set<Wbstemplate> wbstemplates = new HashSet<Wbstemplate>(0);

    public BaseWbstemplate() {
    }
    
    public BaseWbstemplate(Integer idWbsnode) {
    	this.idWbsnode = idWbsnode;
    }
   
    public Integer getIdWbsnode() {
        return this.idWbsnode;
    }
    
    public void setIdWbsnode(Integer idWbsnode) {
        this.idWbsnode = idWbsnode;
    }
    public Wbstemplate getWbstemplate() {
        return this.wbstemplate;
    }
    
    public void setWbstemplate(Wbstemplate wbstemplate) {
        this.wbstemplate = wbstemplate;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getIsControlAccount() {
        return this.isControlAccount;
    }
    
    public void setIsControlAccount(Boolean isControlAccount) {
        this.isControlAccount = isControlAccount;
    }
    public Integer getRoot() {
        return this.root;
    }
    
    public void setRoot(Integer root) {
        this.root = root;
    }
    public Set<Wbstemplate> getWbstemplates() {
        return this.wbstemplates;
    }
    
    public void setWbstemplates(Set<Wbstemplate> wbstemplates) {
        this.wbstemplates = wbstemplates;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Wbstemplate) )  { result = false; }
		 else if (other != null) {
		 	Wbstemplate castOther = (Wbstemplate) other;
			if (castOther.getIdWbsnode().equals(this.getIdWbsnode())) { result = true; }
         }
		 return result;
   }


}


