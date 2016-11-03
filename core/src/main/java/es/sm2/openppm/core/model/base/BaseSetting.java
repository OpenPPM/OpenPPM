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
 * File: BaseSetting.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Setting
 * @see es.sm2.openppm.core.model.base.Setting
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Setting
 */
public class BaseSetting  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "setting";
	
	public static final String IDSETTING = "idSetting";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String VALUE = "value";

     private Integer idSetting;
     private Company company;
     private String name;
     private String value;

    public BaseSetting() {
    }
    
    public BaseSetting(Integer idSetting) {
    	this.idSetting = idSetting;
    }
   
    public Integer getIdSetting() {
        return this.idSetting;
    }
    
    public void setIdSetting(Integer idSetting) {
        this.idSetting = idSetting;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Setting) )  { result = false; }
		 else if (other != null) {
		 	Setting castOther = (Setting) other;
			if (castOther.getIdSetting().equals(this.getIdSetting())) { result = true; }
         }
		 return result;
   }


}


