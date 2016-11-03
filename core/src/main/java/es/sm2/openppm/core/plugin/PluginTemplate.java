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
 * File: PluginTemplate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import es.sm2.openppm.utils.LogManager;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PluginTemplate {
	
	/**
	 * Load template for plugin
	 * 
	 * @param templateName
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String getTemplate(String templateName, HashMap<String, Object> data) throws IOException, TemplateException {
		
        Logger log = LogManager.getLog(PluginTemplate.class);

        // Print data to logger
        if (log.isDebugEnabled()) {
            log.debug("Create Template: "+templateName);

            if (data != null && !data.isEmpty()) {

                for (String key : data.keySet()) {
                    log.debug("Parameter: "+key+" Value: " +data.get(key));
                }
            }
        }
		
		// Load template
		InputStream stream = PluginTemplate.class.getResourceAsStream(templateName);
		String teamplate = IOUtils.toString(stream);

		// Create loader
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("template", teamplate);
		
		// Create configuration
		Configuration cfg = new Configuration();
		cfg.setTemplateLoader(stringLoader);
		
		Template template = cfg.getTemplate("template");
		
		StringWriter writer = new StringWriter();
		template.process(data, writer);
		
		return writer.toString();
	}

}
