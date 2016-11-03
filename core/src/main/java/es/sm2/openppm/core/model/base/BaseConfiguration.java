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
 * File: BaseConfiguration.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Contact;


/**
 * Base Pojo object for domain model class Configuration
 * @see es.sm2.openppm.core.model.base.Configuration
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Configuration
 */
public class BaseConfiguration  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "configuration";
	
	public static final String IDCONFIGURATION = "idConfiguration";
	public static final String CONTACT = "contact";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String TYPE = "type";

     private Integer idConfiguration;
     private Contact contact;
     private String name;
     private String value;
     private String type;

    public BaseConfiguration() {
    }
    
    public BaseConfiguration(Integer idConfiguration) {
    	this.idConfiguration = idConfiguration;
    }
   
    public Integer getIdConfiguration() {
        return this.idConfiguration;
    }
    
    public void setIdConfiguration(Integer idConfiguration) {
        this.idConfiguration = idConfiguration;
    }
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
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
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Configuration) )  { result = false; }
		 else if (other != null) {
		 	Configuration castOther = (Configuration) other;
			if (castOther.getIdConfiguration().equals(this.getIdConfiguration())) { result = true; }
         }
		 return result;
   }


}


