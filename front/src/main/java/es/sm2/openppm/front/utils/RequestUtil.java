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
 * Module: front
 * File: RequestUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

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
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RequestUtil {

	/**
	 * Configurations in request
	 * 
	 * @param req
	 * @param names
	 * @return
	 */
	public static HashMap<String, String> getConfigurationValues(HttpServletRequest req, String...names) {
	
		HashMap<String, String> configurations = new HashMap<String, String>();
		
		if (ValidateUtil.isNotNull(names)) {
			
			for (String name : names) {
				
				String value = ParamUtil.getString(req, name, "undefined");
				
				if (ValidateUtil.isNull(value)) {
					configurations.put(name, StringPool.BLANK);
				}
				else if (!"undefined".equals(value)) {
					configurations.put(name, value);
				}
			}
		}
		
		return configurations;
	}
	
	/**
	 * Configuration from request
	 * @param req
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getConfiguration(HttpServletRequest req, String name) {
		
		String value = StringPool.BLANK;
		
		HashMap<String, String> configurations = (HashMap<String, String>) req.getAttribute("configurations");
		
		if (configurations != null && configurations.containsKey(name)) {
			
			value = configurations.get(name);
		}
		
		return value;
	}

    public static boolean getBooleanConfiguration(HttpServletRequest req, String name) {

        String value = getConfiguration(req, name);
        return (Boolean.TRUE.toString().equalsIgnoreCase(value));
    }

    /**
     * Set Project instance from HTTPRequest parameters
     *
     * @param req
     * @param format
     * @return
     */
	public static Project getInitiatingProjectFromRequest(HttpServletRequest req, SimpleDateFormat format) {

		Integer idProject = ParamUtil.getInteger(req, "id", -1);
		
		Date plannedFinishDate		= ParamUtil.getDate		(req, "plannedFinishDate", format, null);
		String projectName			= ParamUtil.getString	(req, "projectname", null);
		int idProgram				= ParamUtil.getInteger	(req, "program", -1);
		String accountingCode		= ParamUtil.getString	(req, Project.ACCOUNTINGCODE, null);
		int idPerfOrg				= ParamUtil.getInteger	(req, "perforg", -1);
		int idProjectManager		= ParamUtil.getInteger	(req, "projectmanager", -1);
		String chartLabel			= ParamUtil.getString(req, Project.CHARTLABEL);
		int idCustomer				= ParamUtil.getInteger	(req, "customer", -1);
		int idInvestmentManager		= ParamUtil.getInteger	(req, Project.EMPLOYEEBYINVESTMENTMANAGER, -1);
		int idFunctionalManager 	= ParamUtil.getInteger	(req, "functionalmanager", -1);
		int idSponsor				= ParamUtil.getInteger	(req, "sponsor", -1);
		int idContractType			= ParamUtil.getInteger	(req, "contracttype", -1);
		double tcv					= ParamUtil.getCurrency	(req, "tcv", -1);
		double ni					= ParamUtil.getCurrency	(req, "ni", -1);
		double bac					= ParamUtil.getCurrency	(req, "bac", -1);
		int probability				= ParamUtil.getInteger	(req, Project.PROBABILITY, 0);
		int duration				= ParamUtil.getInteger	(req, "sac", -1);
		int effort					= ParamUtil.getInteger	(req, "effort", -1);
		int idGeography				= ParamUtil.getInteger	(req, Project.GEOGRAPHY, -1);
		int idStageGate				= ParamUtil.getInteger	(req, Project.STAGEGATE, -1);
		int priority				= ParamUtil.getInteger	(req, "strategic_value", 0);
		Integer budgetYear			= ParamUtil.getInteger	(req, Project.BUDGETYEAR, null);
		boolean traveling			= ParamUtil.getBoolean	(req, Project.ISGEOSELLING,false);
		boolean internalProject 	= ParamUtil.getBoolean	(req, "internalProject",false);
		int idCategory				= ParamUtil.getInteger	(req, Project.CATEGORY, -1);
		int idClassificationLevel	= ParamUtil.getInteger	(req, Project.CLASSIFICATIONLEVEL, -1);
		String linkDoc				= ParamUtil.getString(req, Project.LINKDOC);
		Double currencyOptional1	= ParamUtil.getCurrency	(req, Project.CURRENCYOPTIONAL1, null);
		Double currencyOptional2	= ParamUtil.getCurrency	(req, Project.CURRENCYOPTIONAL2, null);
		Double currencyOptional3	= ParamUtil.getCurrency	(req, Project.CURRENCYOPTIONAL3, null);
		Double currencyOptional4	= ParamUtil.getCurrency	(req, Project.CURRENCYOPTIONAL4, null);
		
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
		ProjectLogic projectLogic = new ProjectLogic((HashMap<String, String> )req.getAttribute("settings"), null);

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
        project.setTcv(tcv != -1 ? tcv : null);
        project.setNetIncome(ni != -1 ? ni : null);
        project.setBac(bac != -1 ? bac : null);
        project.setDuration(duration != -1 ? duration : null);
        project.setEffort(effort != -1 ? effort : null);

		return project;
	}
	
	
	/**
	 * Set ProjectCharter instance from HTTPRequest parameters
	 * @param req
	 * @return
	 * @throws LogicException 
	 */
	public static Projectcharter getProjectCharterFromRequest(HttpServletRequest req) throws Exception {
		
		ProjectCharterLogic projectCharterLogic = new ProjectCharterLogic();
		Projectcharter projCharter 				= new Projectcharter();
		
		Integer idProjectCharter				= ParamUtil.getInteger(req, "idprojectcharter", -1);
		String businessNeed						= ParamUtil.getString(req, "businessneed", StringPool.BLANK);
		String projectObjectives				= ParamUtil.getString(req, "projectobjectives", StringPool.BLANK);
		String sucessCriteria					= ParamUtil.getString(req, "successcriteria", StringPool.BLANK);
		String mainConstraints					= ParamUtil.getString(req, "mainconstraints", StringPool.BLANK);
		String mainAssumptions					= ParamUtil.getString(req, "mainassumptions", StringPool.BLANK);
		String mainRisks						= ParamUtil.getString(req, "mainrisks", StringPool.BLANK);
		String exclusions						= ParamUtil.getString(req, Projectcharter.EXCLUSIONS, StringPool.BLANK);
		String mainDeliverables					= ParamUtil.getString(req, Projectcharter.MAINDELIVERABLES, StringPool.BLANK);

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
}
