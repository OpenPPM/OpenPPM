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
 * File: SaveAction.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:04:33
 */

package es.sm2.openppm.plugin.synchronize.action;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ProjectCharterLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.action.beans.BeanResponse;
import es.sm2.openppm.core.plugin.action.beans.PluginResponse;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.core.plugin.exceptions.PluginException;
import es.sm2.openppm.plugin.synchronize.beans.ProjectSync;
import es.sm2.openppm.plugin.synchronize.logic.SynchronizeLogic;
import es.sm2.openppm.plugin.synchronize.util.XStreamUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 02/10/2014.
 */
@PluginAction(action = "SaveSynchronizeAction", plugin = "SynchronizeProject")
public class SaveSynchronizeAction extends GenericAction {

    @Override
    public PluginResponse process() {

        BeanResponse response = new BeanResponse();

        if (isUserRole(Constants.ROLE_PMO) || isUserRole(Constants.ROLE_PM)) {
            try {

                // File for import
                ProjectSync projectSync = (ProjectSync) getSessionAttribute("importData");

                if (projectSync == null) {

                    response.addInfo(getResourceBundle(), StringPool.ERROR, "msg.error.import_project");

                } else {
                    // Create project form request action
                    Project project = getInitiatingProject(projectSync);
                    Projectcharter charter = getProjectCharter();

                    // Remove data from session
                    response.addSessionAttribute("importData", null);

                    // Logics
                    ProjectLogic projectLogic = new ProjectLogic(getSettings(), getResourceBundle());
                    SynchronizeLogic synchronizeLogic = new SynchronizeLogic();

                    // Synchronize project
                    List<String> newInfo = synchronizeLogic.importData(project, charter, projectSync, getUser(), getDate("plannedInitDate"), getSettings());

                    // Add information to response

                    for (String inf : newInfo) {
                        response.addInfo(getResourceBundle(), StringPool.INFORMATION, inf);
                    }

                    if (projectLogic.chartLabelInUse(project, getUser())) {
                        response.addInfo(getResourceBundle(), StringPool.INFORMATION, "msg.info.in_use", "project.chart_label", project.getChartLabel());
                    }

                    response.addInfo(getResourceBundle(), StringPool.SUCCESS, "msg.info.updated", "project");
                }

            } catch (IOException e) {
                addError(response, this.getClass(), e);
            } catch (PluginException e) {
                addError(response, this.getClass(), e);
            } catch (Exception e) {
                addError(response, this.getClass(), e);
            }
        }

        response.setUrlServlet("projectinit");

        return response;
    }

    /**
     * Set project charter instance from parameters
     * @return
     * @throws Exception
     */
    private Projectcharter getProjectCharter() throws Exception {

        ProjectCharterLogic projectCharterLogic = new ProjectCharterLogic();
        Projectcharter projCharter 				= new Projectcharter();

        Integer idProjectCharter				= getInteger("idprojectcharter", -1);
        String businessNeed						= getString("businessneed", StringPool.BLANK);
        String projectObjectives				= getString("projectobjectives", StringPool.BLANK);
        String sucessCriteria					= getString("successcriteria", StringPool.BLANK);
        String mainConstraints					= getString("mainconstraints", StringPool.BLANK);
        String mainAssumptions					= getString("mainassumptions", StringPool.BLANK);
        String mainRisks						= getString("mainrisks", StringPool.BLANK);
        String exclusions						= getString(Projectcharter.EXCLUSIONS, StringPool.BLANK);
        String mainDeliverables					= getString(Projectcharter.MAINDELIVERABLES, StringPool.BLANK);

        if (idProjectCharter != -1) {
            projCharter = projectCharterLogic.consProjectCharter(idProjectCharter);
        }

        projCharter.setBusinessNeed(businessNeed);
        projCharter.setProjectObjectives(projectObjectives);
        projCharter.setSucessCriteria(sucessCriteria);
        projCharter.setMainConstraints(mainConstraints);
        projCharter.setMainAssumptions(mainAssumptions);
        projCharter.setMainRisks(mainRisks);
        projCharter.setExclusions(exclusions);
        projCharter.setMainDeliverables(mainDeliverables);

        return projCharter;
    }

    /**
     * Set Project instance from parameters
     * @return
     */
    private Project getInitiatingProject(ProjectSync projectSync) throws IOException {
        
        Integer idProject = getIdProject();

        Date plannedFinishDate		= getDate("plannedFinishDate", null);
        String projectName			= getString("projectname");
        int idProgram				= getInteger("program", -1);
        String accountingCode		= getString(Project.ACCOUNTINGCODE, null);
        int idPerfOrg				= getInteger("perforg", -1);
        int idProjectManager		= getInteger("projectmanager", -1);
        String chartLabel			= getString(Project.CHARTLABEL);
        int idCustomer				= getInteger("customer", -1);
        int idInvestmentManager		= getInteger(Project.EMPLOYEEBYINVESTMENTMANAGER, -1);
        int idFunctionalManager 	= getInteger("functionalmanager", -1);
        int idSponsor				= getInteger("sponsor", -1);
        int idContractType			= getInteger("contracttype", -1);
        Double tcv					= getCurrency("tcv", null);
        Double ni					= getCurrency("ni", null);
        Double bac					= getCurrency("bac", null);
        int probability				= getInteger(Project.PROBABILITY, 0);
        int duration				= getInteger("sac", -1);
        int effort					= getInteger("effort", -1);
        int idGeography				= getInteger(Project.GEOGRAPHY, -1);
        int idStageGate				= getInteger(Project.STAGEGATE, -1);
        int priority				= getInteger("strategic_value", 0);
        Integer budgetYear			= getInteger(Project.BUDGETYEAR);
        boolean traveling			= getBoolean(Project.ISGEOSELLING, false);
        boolean internalProject 	= getBoolean("internalProject", false);
        int idCategory				= getInteger(Project.CATEGORY, -1);
        int idClassificationLevel	= getInteger(Project.CLASSIFICATIONLEVEL, -1);
        String linkDoc				= getString(Project.LINKDOC);
        Double currencyOptional1	= getCurrency(Project.CURRENCYOPTIONAL1, null);
        Double currencyOptional2	= getCurrency(Project.CURRENCYOPTIONAL2, null);
        Double currencyOptional3	= getCurrency(Project.CURRENCYOPTIONAL3, null);
        Double currencyOptional4	= getCurrency(Project.CURRENCYOPTIONAL4, null);

        Customer customer = null;
        if (idCustomer != -1) {
            customer = new Customer();
            customer.setIdCustomer(idCustomer);
        }

        Employee projectManager = null;
        if (idProjectManager != -1) {
            projectManager = new Employee();
            projectManager.setIdEmployee(idProjectManager);
        }

        Employee functionalManager = null;
        if (idFunctionalManager != -1) {
            functionalManager = new Employee();
            functionalManager.setIdEmployee(idFunctionalManager);
        }

        Category category = null;
        if (idCategory != -1) { category = new Category(idCategory); }

        Classificationlevel classificationlevel = null;
        if (idClassificationLevel != -1) { classificationlevel = new Classificationlevel(idClassificationLevel); }

        Geography geography = null;
        if (idGeography != -1) { geography = new Geography(idGeography); }

        Stagegate stagegate = null;
        if (idStageGate != -1) { stagegate = new Stagegate(idStageGate); }

        Employee investmentManager = null;
        if (idInvestmentManager != -1) { investmentManager = new Employee(idInvestmentManager); }

        Employee sponsor = null;
        if (idSponsor != -1) {
            sponsor = new Employee();
            sponsor.setIdEmployee(idSponsor);
        }

        Contracttype contractType = null;
        if (idContractType != -1) {
            contractType = new Contracttype();
            contractType.setIdContractType(idContractType);
        }

        // Declare logic
        ProjectLogic projectLogic = new ProjectLogic(getSettings(), getResourceBundle());

        Project project = new Project();

        if (idProject != -1) {
            try {
                // Logic get project
                project = projectLogic.consProject(idProject);
            }
            catch (Exception ex) {
                project.setIdProject(idProject);
            }
        }

        project = XStreamUtil.synchronize(project, projectSync);

        project.setChartLabel(ValidateUtil.isNull(chartLabel)?accountingCode:chartLabel);
        project.setInternalProject(internalProject);
        project.setIsGeoSelling(traveling);
        project.setLinkDoc(linkDoc);
        project.setCategory(category);
        project.setClassificationlevel(classificationlevel);
        project.setGeography(geography);
        project.setStagegate(stagegate);
        project.setAccountingCode(accountingCode);
        project.setCurrencyOptional1(currencyOptional1);
        project.setCurrencyOptional2(currencyOptional2);
        project.setCurrencyOptional3(currencyOptional3);
        project.setCurrencyOptional4(currencyOptional4);

        project.setPlannedFinishDate(plannedFinishDate);

        if (projectName != null) { // Don't update this field
            project.setProjectName(projectName);
        }
        project.setBudgetYear(budgetYear);
        project.setProbability(probability);
        project.setPriority(priority);

        if (idProgram != -1) { project.setProgram(new Program(idProgram)); }
        if (idPerfOrg != -1) { project.setPerformingorg(new Performingorg(idPerfOrg)); }

        project.setCustomer(customer);
        project.setEmployeeByProjectManager(projectManager);
        project.setEmployeeByFunctionalManager(functionalManager);
        project.setEmployeeByInvestmentManager(investmentManager);
        project.setEmployeeBySponsor(sponsor);
        project.setContracttype(contractType);
        project.setTcv(tcv);
        project.setNetIncome(ni);
        project.setBac(bac);
        project.setDuration(duration != -1 ? duration : null);
        project.setEffort(effort != -1 ? effort : null);

        return project;
    }
}
