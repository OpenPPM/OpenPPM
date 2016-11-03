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
 * File: BasePlugin.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Plugin;
import es.sm2.openppm.core.model.impl.Pluginconfiguration;
import es.sm2.openppm.core.model.impl.Pluginfile;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Plugin
 * @see es.sm2.openppm.core.model.base.Plugin
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Plugin
 */
public class BasePlugin  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "plugin";
	
	public static final String IDPLUGIN = "idPlugin";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String ENABLED = "enabled";
	public static final String PLUGINCONFIGURATIONS = "pluginconfigurations";
	public static final String PLUGINFILES = "pluginfiles";

     private Integer idPlugin;
     private Company company;
     private String name;
     private boolean enabled;
     private Set<Pluginconfiguration> pluginconfigurations = new HashSet<Pluginconfiguration>(0);
     private Set<Pluginfile> pluginfiles = new HashSet<Pluginfile>(0);

    public BasePlugin() {
    }
    
    public BasePlugin(Integer idPlugin) {
    	this.idPlugin = idPlugin;
    }
   
    public Integer getIdPlugin() {
        return this.idPlugin;
    }
    
    public void setIdPlugin(Integer idPlugin) {
        this.idPlugin = idPlugin;
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
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public Set<Pluginconfiguration> getPluginconfigurations() {
        return this.pluginconfigurations;
    }
    
    public void setPluginconfigurations(Set<Pluginconfiguration> pluginconfigurations) {
        this.pluginconfigurations = pluginconfigurations;
    }
    public Set<Pluginfile> getPluginfiles() {
        return this.pluginfiles;
    }
    
    public void setPluginfiles(Set<Pluginfile> pluginfiles) {
        this.pluginfiles = pluginfiles;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Plugin) )  { result = false; }
		 else if (other != null) {
		 	Plugin castOther = (Plugin) other;
			if (castOther.getIdPlugin().equals(this.getIdPlugin())) { result = true; }
         }
		 return result;
   }


}


