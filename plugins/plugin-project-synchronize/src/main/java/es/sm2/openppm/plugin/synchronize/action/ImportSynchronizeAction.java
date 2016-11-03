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
 * File: ImportAction.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:03:16
 */

package es.sm2.openppm.plugin.synchronize.action;

import com.thoughtworks.xstream.XStream;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ContractTypeLogic;
import es.sm2.openppm.core.logic.impl.CustomerLogic;
import es.sm2.openppm.core.logic.impl.CustomertypeLogic;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.logic.impl.StagegateLogic;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.plugin.PluginTemplate;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.action.beans.BeanResponse;
import es.sm2.openppm.core.plugin.action.beans.HtmlResponse;
import es.sm2.openppm.core.plugin.action.beans.PluginResponse;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.plugin.synchronize.beans.ProjectSync;
import es.sm2.openppm.plugin.synchronize.logic.SynchronizeLogic;
import es.sm2.openppm.plugin.synchronize.util.XStreamUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;


/**
 * SM2 Baleares
 * Created by javier.hernandez on 02/10/2014.
 */
@PluginAction(action = "ImportSynchronizeAction", plugin = "SynchronizeProject")
public class ImportSynchronizeAction extends GenericAction {

    @Override
    public PluginResponse process() {

        PluginResponse response = new HtmlResponse();

        if (isUserRole(Constants.ROLE_PMO) || isUserRole(Constants.ROLE_PM)) {
            int idProject = Integer.parseInt(getMultipartField("id"));

            boolean error = false;

            try {

                FileItem dataFile = getMultipartFields().get("file");

                XStream xstream = XStreamUtil.createXStrean();
                ProjectSync projectSync = (ProjectSync) xstream.fromXML(dataFile.getInputStream());

                SynchronizeLogic logic = new SynchronizeLogic();

                Project project = logic.synchronize(idProject, projectSync);
                Projectcharter projectCharter = logic.synchronizeCharter(idProject, projectSync);

                response.addSessionAttribute("importData", projectSync);

                // Data for edit
                ContractTypeLogic contractTypeLogic = new ContractTypeLogic();
                CategoryLogic categoryLogic = new CategoryLogic();
                GeographyLogic geoLogic = new GeographyLogic();
                CustomerLogic customerLogic = new CustomerLogic();
                CustomertypeLogic customertypeLogic = new CustomertypeLogic();
                ResourceProfilesLogic resourceProfilesLogic = new ResourceProfilesLogic();
                PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
                ProgramLogic programLogic = new ProgramLogic();
                StagegateLogic stagegateLogic = new StagegateLogic();


                // Parameters for template
                HashMap<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("resourceBundle", getResourceBundle());
                parameters.put("customertypes", customertypeLogic.findByCompany(getUser()));
                parameters.put("categories", categoryLogic.findByRelation(Category.COMPANY, getCompany()));
                parameters.put("customers", customerLogic.searchByCompany(getCompany()));
                parameters.put("geographies", geoLogic.findByRelation(Geography.COMPANY, getCompany()));
                parameters.put("contractTypes", contractTypeLogic.findByRelation(Contracttype.COMPANY, getCompany()));
                parameters.put("programs", programLogic.searchByPerfOrg(project.getPerformingorg()));
                parameters.put("perforgs", performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany()));
                parameters.put("profiles", resourceProfilesLogic.findAll());
                parameters.put("projectSync", projectSync);
                parameters.put("project", project);
                parameters.put("projectCharter", projectCharter);
                parameters.put("stageGates", stagegateLogic.findByRelation(Currency.COMPANY, getCompany()));
                parameters.put("data", createLabels());
                parameters.put("defaultcostVariance", Boolean.parseBoolean(SettingUtil.getString(getSettings(), Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW, Settings.DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW)));
                parameters.put("defaultProbabilityVariance", ValidateUtil.isNull(SettingUtil.getList(getSettings(), Settings.DEFAULT_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY)[0]));
                parameters.put("defaultProbability",SettingUtil.getList(getSettings(), Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY));
                parameters.put("hasPermision", hasPermission(project));
                parameters.put("datePattern", DateUtil.getDatePattern(getResourceBundle()));
                parameters.put("disableValidate",checkValidate(project));
                parameters.put("optionalCurrecny", Boolean.valueOf(Settings.OPTIONAL_CURRENCY));
                parameters.put("currencyPattern", Constants.CURRENCY_FTL_PATTERN);


                // Generate HTML from template
                ((HtmlResponse) response).setData(PluginTemplate.getTemplate("/es/sm2/openppm/plugin/synchronize/template/load/SynchronizeLoad.ftl", parameters));

            } catch (Exception e) {
                error = true;
                addError(response, this.getClass(), e);
            }

            if (error) {
                response = new BeanResponse();
                ((BeanResponse) response).setUrlServlet("projectinit");
                ((BeanResponse) response).addData("idProject", getIdProject());
                ((BeanResponse) response).setInformationType(StringPool.ERROR);
                ((BeanResponse) response).setInformation("msg.error.bad_format_file");
            }
        }

        return response;
    }

    /**
     * Check if user has permission
     *
     * @param project
     */
    private boolean hasPermission(Project project) {

        // Permission controls
        return ((Constants.STATUS_INITIATING.equals(project.getStatus()) || Constants.STATUS_PLANNING.equals(project.getStatus()) || Constants.STATUS_CONTROL.equals(project.getStatus()))
                && (getUser().getResourceprofiles().getIdProfile() == Constants.ROLE_PM || getUser().getResourceprofiles().getIdProfile() == Constants.ROLE_PMO));
    }

    /**
     *  Create the label hashmap
     *
     * @return data
     */
    private HashMap<String, String> createLabels() {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("fmtShortNameRequired",getParametrizedKey("maintenance.error_msg_a","project.chart_label"));
        data.put("fmtProjectNameRequired",getParametrizedKey("maintenance.error_msg_a","project_name"));
        data.put("fmtInvestmentManagerRequired",getParametrizedKey("maintenance.error_msg_a","investment_manager"));
        data.put("fmtProjectManagerRequired",getParametrizedKey("maintenance.error_msg_a","project_manager"));
        data.put("fmtProgramRequired",getParametrizedKey("maintenance.error_msg_a","program"));
        data.put("fmtPlannedStartDateRequired",getParametrizedKey("maintenance.error_msg_a","baseline_start"));
        data.put("fmtPlannedFinishDateRequired",getParametrizedKey("maintenance.error_msg_a","baseline_finish"));
        data.put("fmtPlannedStartDateFormat",getParametrizedKey("msg.error.invalid_format","baseline_start"));
        data.put("fmtPlannedFinishDateFormat",getParametrizedKey("msg.error.invalid_format","baseline_finish"));
        data.put("fmtPlannedStartDateAfterFinish",getParametrizedKey("msg.error.date_before_date","baseline_start","baseline_finish"));
        data.put("fmtPlannedFinishDateBeforeStart",getParametrizedKey("msg.error.date_before_date","baseline_finish","baseline_start"));
        data.put("fmtPriorityOutOfRange",getParametrizedKey("project.priority","msg.error.out_of_range"));
        data.put("fmtProbabilityOutOfRange",getParametrizedKey("proposal.win_probability","msg.error.out_of_range"));
        data.put("fmtSacMinValue",getParametrizedKey("msg.error.min_value","sac"));
        data.put("fmtEffortMinValue",getParametrizedKey("msg.error.min_value","effort"));
        data.put("fmtTcvMax",getParametrizedKey("msg.error.maximum_allowed","tcv"));
        data.put("fmtBacMax",getParametrizedKey("msg.error.maximum_allowed","bac"));
        data.put("fmtNiMax", getParametrizedKey("msg.error.maximum_allowed", "project.net_value"));

        return data;
    }

    /**
     * Check project status
     *
     * @param project
     * @return String
     */
    private String checkValidate(Project project){
        if(Constants.STATUS_INITIATING.equals(project.getStatus())){
            return "readonly";
        }
        return "";
    }
}
