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
 * File: BasePluginconfiguration.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Plugin;
import es.sm2.openppm.core.model.impl.Pluginconfiguration;


/**
 * Base Pojo object for domain model class Pluginconfiguration
 * @see es.sm2.openppm.core.model.base.Pluginconfiguration
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Pluginconfiguration
 */
public class BasePluginconfiguration  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "pluginconfiguration";
	
	public static final String IDPLUGINCONFIGURATION = "idPluginConfiguration";
	public static final String PLUGIN = "plugin";
	public static final String CONFIGURATION = "configuration";
	public static final String VALUE = "value";

     private Integer idPluginConfiguration;
     private Plugin plugin;
     private String configuration;
     private String value;

    public BasePluginconfiguration() {
    }
    
    public BasePluginconfiguration(Integer idPluginConfiguration) {
    	this.idPluginConfiguration = idPluginConfiguration;
    }
   
    public Integer getIdPluginConfiguration() {
        return this.idPluginConfiguration;
    }
    
    public void setIdPluginConfiguration(Integer idPluginConfiguration) {
        this.idPluginConfiguration = idPluginConfiguration;
    }
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
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
		 else if (!(other instanceof Pluginconfiguration) )  { result = false; }
		 else if (other != null) {
		 	Pluginconfiguration castOther = (Pluginconfiguration) other;
			if (castOther.getIdPluginConfiguration().equals(this.getIdPluginConfiguration())) { result = true; }
         }
		 return result;
   }


}


