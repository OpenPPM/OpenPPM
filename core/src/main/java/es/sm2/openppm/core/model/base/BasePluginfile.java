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
 * File: BasePluginfile.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Plugin;
import es.sm2.openppm.core.model.impl.Pluginfile;


/**
 * Base Pojo object for domain model class Pluginfile
 * @see es.sm2.openppm.core.model.base.Pluginfile
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Pluginfile
 */
public class BasePluginfile  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "pluginfile";
	
	public static final String IDPLUGINFILE = "idPluginfile";
	public static final String PLUGIN = "plugin";
	public static final String CONTENTFILE = "contentfile";
	public static final String TYPEFILE = "typeFile";

     private Integer idPluginfile;
     private Plugin plugin;
     private Contentfile contentfile;
     private String typeFile;

    public BasePluginfile() {
    }
    
    public BasePluginfile(Integer idPluginfile) {
    	this.idPluginfile = idPluginfile;
    }
   
    public Integer getIdPluginfile() {
        return this.idPluginfile;
    }
    
    public void setIdPluginfile(Integer idPluginfile) {
        this.idPluginfile = idPluginfile;
    }
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
    public Contentfile getContentfile() {
        return this.contentfile;
    }
    
    public void setContentfile(Contentfile contentfile) {
        this.contentfile = contentfile;
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
		 else if (!(other instanceof Pluginfile) )  { result = false; }
		 else if (other != null) {
		 	Pluginfile castOther = (Pluginfile) other;
			if (castOther.getIdPluginfile().equals(this.getIdPluginfile())) { result = true; }
         }
		 return result;
   }


}


