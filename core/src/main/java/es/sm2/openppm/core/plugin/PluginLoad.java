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
 * File: PluginLoad.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin;


import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class PluginLoad {

    public String getForms() {
        return forms;
    }

    public void setForms(String[] forms) {

        StringBuilder stringBuilder = new StringBuilder();

        if (ValidateUtil.isNotNull(forms)) {

            for (String form : forms) {

                if (ValidateUtil.isNotNull(stringBuilder.toString())) {

                    stringBuilder.append(StringPool.COMMA);
                }
                stringBuilder.append(form);
            }
        }

        this.forms = stringBuilder.toString();
    }

    public enum PluginAccionHTML {
		HTML,
		PREPEND,
		APPEND;
	}
	
	private String selector;
	private String pluginName;
	private String template;
	private String typeModification;
    private String forms;

    public PluginLoad() {
    }

    public PluginLoad(LoadOption loadOption) {

        this.pluginName = loadOption.getPluginName();
        this.selector = loadOption.getSelector();
        this.template = loadOption.getTemplate();
        this.typeModification = loadOption.getPluginActionHTML().name();
    }

    @Deprecated
	public PluginLoad(String pluginName, String selector, String template) {
		this(pluginName, selector, template, PluginAccionHTML.PREPEND);
	}

    @Deprecated
	public PluginLoad(String pluginName, String selector, String template, PluginAccionHTML accionHTML) {
		super();
		this.pluginName = pluginName;
		this.selector = selector;
		this.template = template;
		this.typeModification = accionHTML.name();
	}
	
	/**
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}
	/**
	 * @param selector the selector to set
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * @param pluginName the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	/**
	 * @return the typeModification
	 */
	public String getTypeModification() {
		return typeModification;
	}

	/**
	 * @param typeModification the typeModification to set
	 */
	public void setTypeModification(String typeModification) {
		this.typeModification = typeModification;
	}

	
}

