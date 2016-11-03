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
 * Module: plugin-project-synchronize
 * File: SynchronizeButtonsLoad.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:07:48
 */

package es.sm2.openppm.plugin.synchronize.action.load;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.plugin.PluginTemplate;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.action.beans.PluginResponse;
import es.sm2.openppm.core.plugin.action.beans.StringResponse;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.core.plugin.annotations.PluginLoad;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 02/10/2014.
 */
@PluginLoad(servlet = "ProjectInitServlet", selector = "#footerButtons", forms = "#frm_project")
@PluginAction(action = "SynchronizeButtonsLoad", plugin = "SynchronizeProject")
public class SynchronizeButtonsLoad extends GenericAction {

    @Override
    public PluginResponse process() {

        StringResponse resposne = new StringResponse();

        // Has permissions
        if (isUserRole(Constants.ROLE_PMO) || isUserRole(Constants.ROLE_PM)) {

            // Parameters for template
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("resourceBundle", getResourceBundle());
            parameters.put("idProject", getIdProject());

            try {
                // Generate HTML from template
                resposne.setData(PluginTemplate.getTemplate("/es/sm2/openppm/plugin/synchronize/template/load/SynchronizeButtonsLoad.ftl", parameters));

            } catch (IOException e) {
                addError(resposne, this.getClass(), e);
            } catch (TemplateException e) {
                addError(resposne, this.getClass(), e);
            }
        }

        return resposne;
    }
}
