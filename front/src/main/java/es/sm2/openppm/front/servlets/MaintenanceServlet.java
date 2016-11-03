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
 * File: MaintenanceServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.charts.ChartWBS;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.exceptions.ChangePasswordException;
import es.sm2.openppm.core.exceptions.EmployeeNotFoundException;
import es.sm2.openppm.core.exceptions.LoginNameInUseException;
import es.sm2.openppm.core.javabean.UserLDAP;
import es.sm2.openppm.core.logic.impl.BscdimensionLogic;
import es.sm2.openppm.core.logic.impl.BudgetaccountsLogic;
import es.sm2.openppm.core.logic.impl.BusinessdriverLogic;
import es.sm2.openppm.core.logic.impl.BusinessdriversetLogic;
import es.sm2.openppm.core.logic.impl.CalendarBaseLogic;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ChangeTypesLogic;
import es.sm2.openppm.core.logic.impl.ClassificationlevelLogic;
import es.sm2.openppm.core.logic.impl.ClosurecheckLogic;
import es.sm2.openppm.core.logic.impl.CompanyLogic;
import es.sm2.openppm.core.logic.impl.ContactLogic;
import es.sm2.openppm.core.logic.impl.ContentFileLogic;
import es.sm2.openppm.core.logic.impl.ContractTypeLogic;
import es.sm2.openppm.core.logic.impl.CustomerLogic;
import es.sm2.openppm.core.logic.impl.CustomertypeLogic;
import es.sm2.openppm.core.logic.impl.DocumentationLogic;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.ExpenseaccountsLogic;
import es.sm2.openppm.core.logic.impl.FundingsourceLogic;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.logic.impl.JobcategoryLogic;
import es.sm2.openppm.core.logic.impl.JobcatemployeeLogic;
import es.sm2.openppm.core.logic.impl.LabelLogic;
import es.sm2.openppm.core.logic.impl.ManagepoolLogic;
import es.sm2.openppm.core.logic.impl.MetrickpiLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.OperationLogic;
import es.sm2.openppm.core.logic.impl.OperationaccountLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProblemcheckLogic;
import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.logic.impl.RiskcategoryLogic;
import es.sm2.openppm.core.logic.impl.RiskregistertemplateLogic;
import es.sm2.openppm.core.logic.impl.SecurityLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.impl.SkillLogic;
import es.sm2.openppm.core.logic.impl.SkillsemployeeLogic;
import es.sm2.openppm.core.logic.impl.StagegateLogic;
import es.sm2.openppm.core.logic.impl.TechnologyLogic;
import es.sm2.openppm.core.logic.impl.WBSTemplateLogic;
import es.sm2.openppm.core.logic.security.actions.MaintenanceAction;
import es.sm2.openppm.core.model.impl.Bscdimension;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Changetype;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Expenseaccounts;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Technology;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.core.utils.EmailUtil;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.front.utils.BeanUtil;
import es.sm2.openppm.front.utils.ConnectLDAP;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class MantenimientosServlet
 */
public class MaintenanceServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
	   
	private static final Logger LOGGER = Logger.getLogger(MaintenanceServlet.class);
	
	public final static String REFERENCE = "maintenance";
	
	/***************** Actions ****************/
	public final static String CONS_MAINTENANCE			= "cons-maintenance";
	public final static String DEL_COMPANY				= "delete-company";
	public final static String DEL_PERFORG				= "delete-perfoming-org";
	public final static String DEL_SKILL				= "delete-skill";
	public final static String DEL_CONTACT				= "delete-contact";
	public final static String SAVE_EMPLOYEE	 		= "save-employee";
	public final static String DEL_EMPLOYEE				= "delete-employee";
	public final static String DEL_BUDGETACCOUNTS		= "delete-budgetaccounts";
	public final static String DEL_CONTRACTTYPE			= "delete-contracttype";
	public final static String DEL_CHANGETYPE			= "delete-changetype";
	public final static String DEL_EXPENSEACCOUNTS		= "delete-expenseaccounts";
	public final static String DEL_CATEGORY				= "delete-category";
	public final static String DEL_GEOGRAPHY			= "delete-geography";
	public final static String DEL_OPERATIONACCOUNT 	= "delete-operationaccount";
	public final static String DEL_OPERATION			= "delete-operation";
	public final static String DEL_CUSTOMER				= "delete-customer";
	public final static String DEL_SELLER				= "delete-seller";
	public final static String DEL_METRIC				= "delete-metric";
	public final static String DEL_CUSTOMER_TYPE		= "delete-customer-type";
	public final static String DEL_FUNDINGSOURCE 		= "delete-fundingsource";
	public final static String SAVE_DOCUMENTATION 		= "save-documentation";
	public final static String DEL_DOCUMENTATION 		= "delete-documentation";
	public final static String DEL_WBS_TEMPLATE 		= "delete-wbs-template";
	public final static String DEL_LABEL 				= "delete-label";
	public final static String DEL_STAGE_GATE 			= "delete-stage-gate";
	public final static String DEL_RESOURCE_POOL 		= "delete-resource-pool";
	public final static String DEL_RISK_CATEGORY		= "delete-risk-category";
	public final static String DEL_MILESTONE_CATEGORY	= "delete-milestone-category";
	public final static String DEL_CLASSIFICATION_LEVEL = "delete-classification-level";
	public final static String DEL_BSC_DIMENSION        = "delete-bsc-dimension";

	/************** Actions AJAX **************/
	public final static String JX_DISABLE_CONTACT					= "ajax-disable-contact";
	public final static String JX_ENABLE_CONTACT					= "ajax-enable-contact";
	public final static String JX_DISABLE_EMPLOYEE					= "ajax-disable-employee";
	public final static String JX_ENABLE_EMPLOYEE					= "ajax-enable-employee";
	public final static String JX_SAVE_COMPANY						= "ajax-save-company";
	public final static String JX_SAVE_PERFORG						= "ajax-save-performing-org";
	public final static String JX_SAVE_SKILL						= "ajax-save-skill";
	public final static String JX_SAVE_CONTACT						= "ajax-save-contact";
	public final static String JX_LDAP_SEARCH						= "ajax-ldap-search";
	public final static String JX_SAVE_BUDGETACCOUNTS				= "ajax-save-budgetaccounts";
	public final static String JX_SAVE_CONTRACTTYPE					= "ajax-save-contracttype";
	public final static String JX_SAVE_CHANGETYPE					= "ajax-save-changetype";
	public final static String JX_SAVE_EXPENSEACCOUNTS				= "ajax-save-expenseaccounts";
	public final static String JX_SAVE_CATEGORY						= "ajax-save-category";
	public final static String JX_SAVE_GEOGRAPHY					= "ajax-save-geography";
	public final static String JX_SAVE_OPERATIONACCOUNT				= "ajax-save-operationaccount";
	public final static String JX_SAVE_OPERATION					= "ajax-save-operation";
	public final static String JX_SAVE_KNOWLEDGE_AREAS				= "ajax-save-knowledge-areas";
	public final static String JX_RESET_PASSWORD					= "ajax-reset-password";
	public final static String JX_GET_USERNAME						= "ajax-get-username";
	public final static String JX_SAVE_CUSTOMER						= "ajax-save-customer";
	public final static String JX_SAVE_SELLER						= "ajax-save-seller";
	public final static String JX_SAVE_METRIC						= "ajax-save-metric";
	public final static String JX_SAVE_CUSTOMER_TYPE				= "ajax-save-customer-type";
	public final static String JX_CONS_SKILLS						= "ajax-cons-skills";
	public final static String JX_SAVE_FUNDINGSOURCE				= "ajax-save-fundingsource";
	public final static String JX_VIEW_CHILDS_WBS_TEMPLATE			= "ajax-view-childs-wbs-template";
	public final static String JX_WBS_CHART 						= "ajax-generate-chart-wbs";
	public final static String JX_DEL_WBS_TEMPLATE_NODE 			= "ajax-delete-wbs-template";
	public final static String JX_VIEW_NODE_WBS_TEMPLATE			= "ajax-view-child-wbs-template";
	public final static String JX_SAVE_WBS_TEMPLATE_NODE			= "ajax-save-wbs-template-node";
	public final static String JX_SAVE_BUSINESS_DRIVER				= "ajax-save-business-driver";
	public final static String JX_DELETE_BUSINESS_DRIVER			= "ajax-delete-business-driver";
	public final static String JX_SAVE_BUSINESS_DRIVER_SET			= "ajax-save-business-driver-set";
	public final static String JX_DELETE_BUSINESS_DRIVER_SET		= "ajax-delete-business-driver-set";
	public final static String JX_VIEW_BUSINESS_DRIVERS				= "ajax-view-business-drivers";
	public final static String JX_SAVE_JOB_CATEGORY					= "ajax-save-job-category";
	public final static String JX_DELETE_JOB_CATEGORY				= "ajax-delete-job-category";
	public final static String JX_CONS_SKILLS_AND_JOB_CATEGORIES 	= "ajax-consult-skills-and-job_categories";
	public final static String JX_SAVE_RISK_CATEGORY				= "ajax-save-risk-category";
	public final static String JX_SAVE_MILESTONE_CATEGORY			= "ajax-save-milestone-category";
	public final static String JX_DISABLE_MILESTONE_CATEGORY		= "ajax-disable-milestone-category";
	public final static String JX_ENABLE_MILESTONE_CATEGORY			= "ajax-enable-milestone-category";
    public final static String JX_SAVE_BSC_DIMENSION                = "ajax-save-bsc-dimension";
	// LABELS
	public final static String JX_SAVE_LABEL 						= "ajax-save-label";
	public final static String JX_DISABLE_LABEL						= "ajax-disable-label";
	public final static String JX_ENABLE_LABEL						= "ajax-enable-label";
	// STAGE GATE
	public final static String JX_SAVE_STAGE_GATE 					= "ajax-save-stage-gate";
	// RESOURCE POOL
	public final static String JX_SAVE_RESOURCE_POOL				= "ajax-save-resource-pool";
	// MANAGE POOL
	public final static String JX_CONS_MANAGE_POOL					= "ajax-cons-manage-pool";
	// MILESTONE TYPE
	public final static String JX_SAVE_MILESTONE_TYPE				= "ajax-save-milestone-type";
	public final static String JX_DELETE_MILESTONE_TYPE				= "ajax-delete-milestone-type";
	// CLOSURE CHECK
	public final static String JX_SAVE_CLOSURE_CHECK				= "ajax-save-closure-check";
	public final static String JX_DELETE_CLOSURE_CHECK				= "ajax-delete-closure-check";
	// PROBLEM CHECK
	public final static String JX_SAVE_PROBLEM_CHECK				= "ajax-save-problem-check";
	public final static String JX_DELETE_PROBLEM_CHECK				= "ajax-delete-problem-check";
	// CLASSIFICATION LEVEL
	public final static String JX_SAVE_CLASSIFICATION_LEVEL			= "ajax-save-classification-level";
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	@SuppressWarnings("rawtypes")
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		super.service(req, resp);
		
		String accion = ParamUtil.getString(req, "accion", "");

		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {

			if(ServletFileUpload.isMultipartContent(req) && StringPool.BLANK.equals(accion)) {
                accion = getMultipartField("accion");
			}
			
			LOGGER.debug("Accion: " + accion);
			
			/***************** Actions ****************/
			if (StringPool.BLANK.equals(accion)) { viewMaintenance(req, resp); }
			else if (CONS_MAINTENANCE.equals(accion)) { consMaintenance(req, resp, null); }
			else if (DEL_COMPANY.equals(accion)) { deleteCompany(req, resp); }
			else if (SAVE_EMPLOYEE.equals(accion)) { saveEmployee(req, resp); }
			else if (DEL_EMPLOYEE.equals(accion)) { deleteEmployee(req, resp); }
			else if (DEL_PERFORG.equals(accion)) { deletePerfOrg(req, resp); }
			else if (DEL_SKILL.equals(accion)) { deleteSkill(req, resp); }
			else if (DEL_CONTACT.equals(accion)) { deleteContact(req, resp); }
			else if (DEL_BUDGETACCOUNTS.equals(accion)) { deleteBudgetAccount(req, resp); }
			else if (DEL_CONTRACTTYPE.equals(accion)) { deleteContractType(req, resp); }
			else if (DEL_CHANGETYPE.equals(accion)) { deleteChangeType(req, resp); }
			else if (DEL_EXPENSEACCOUNTS.equals(accion)) { deleteExpenseAccount(req, resp); }
			else if (DEL_CATEGORY.equals(accion)) { deleteCategory(req, resp); }
			else if (DEL_GEOGRAPHY.equals(accion)) { deleteGeography(req, resp); }
			else if (DEL_OPERATIONACCOUNT.equals(accion)) { deleteOperationAccount(req, resp); }
			else if (DEL_OPERATION.equals(accion)) { deleteOperation(req, resp); }
			else if (DEL_CUSTOMER.equals(accion)) { deleteCustomer(req, resp); }
			else if (DEL_SELLER.equals(accion)) { deleteSeller(req, resp); }
			else if (DEL_METRIC.equals(accion)) { deleteMetric(req, resp); }
			else if (DEL_CUSTOMER_TYPE.equals(accion)) { deleteCustomerType(req, resp); }
			else if (DEL_FUNDINGSOURCE.equals(accion)) { deleteFundingSource(req, resp); }
			else if (SAVE_DOCUMENTATION.equals(accion)) { saveDocumentation(req, resp); }
			else if (DEL_DOCUMENTATION.equals(accion)) { deleteDocumentation(req, resp); }
			else if (DEL_WBS_TEMPLATE.equals(accion)) { deleteWbsTemplate(req, resp); }
			else if (DEL_LABEL.equals(accion)) { deleteLabel(req, resp); }
			else if (DEL_STAGE_GATE.equals(accion)) { deleteStageGate(req, resp); }
			else if (DEL_RESOURCE_POOL.equals(accion)) { deleteResourcePool(req, resp); }
			else if (DEL_RISK_CATEGORY.equals(accion)) { deleteRiskCategory(req, resp); }
			else if (DEL_MILESTONE_CATEGORY.equals(accion)) { deleteMilestoneCategory(req, resp); }
			else if (DEL_CLASSIFICATION_LEVEL.equals(accion)) { deleteClassificationLevel(req, resp); }
            else if (DEL_BSC_DIMENSION.equals(accion)) { deleteBscDimension(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_ENABLE_CONTACT.equals(accion) || JX_DISABLE_CONTACT.equals(accion)) { updateContactStateJX(req, resp, accion); }
			else if (JX_ENABLE_EMPLOYEE.equals(accion) || JX_DISABLE_EMPLOYEE.equals(accion)) { updateEmployeeStateJX(req, resp, accion); }
			else if (JX_SAVE_COMPANY.equals(accion)) { saveCompanyJX(req, resp); }
			else if (JX_SAVE_PERFORG.equals(accion)) { savePerfOrgJX(req, resp); }
			else if (JX_SAVE_SKILL.equals(accion)) { saveSkillJX(req, resp); }
			else if (JX_SAVE_CONTACT.equals(accion)) { saveContactJX(req, resp); }
			else if (JX_LDAP_SEARCH.equals(accion)) { searchLDAPJX(req, resp); }
			else if (JX_RESET_PASSWORD.equals(accion)) { resetPasswordJX(req, resp); }
			else if (JX_GET_USERNAME.equals(accion)) { getUserNameJX(req, resp); }
			else if (JX_SAVE_BUDGETACCOUNTS.equals(accion)) { saveBudbetAccountJX(req, resp); }
			else if (JX_SAVE_CONTRACTTYPE.equals(accion)) { saveContractTypeJX(req, resp); }
			else if (JX_SAVE_CHANGETYPE.equals(accion)) { saveChangeTypeJX(req, resp); }
			else if (JX_SAVE_EXPENSEACCOUNTS.equals(accion)) { saveExpenseAccountJX(req, resp); }
			else if (JX_SAVE_CATEGORY.equals(accion)) { saveCategoryJX(req, resp); }
			else if (JX_SAVE_GEOGRAPHY.equals(accion)) { saveGeographyJX(req, resp); }
			else if (JX_SAVE_OPERATIONACCOUNT.equals(accion)) { saveOperationAccountJX(req, resp); }
			else if (JX_SAVE_OPERATION.equals(accion)) { saveOperationJX(req, resp); }
			else if (JX_SAVE_CUSTOMER.equals(accion)) { saveCustomerJX(req, resp); }
			else if (JX_SAVE_SELLER.equals(accion)) { saveSellerJX(req, resp); }
			else if (JX_SAVE_METRIC.equals(accion)) { saveMetricJX(req, resp); }
			else if (JX_SAVE_CUSTOMER_TYPE.equals(accion)) { saveCustomerTypeJX(req, resp); }
			else if (JX_CONS_SKILLS.equals(accion)) { consSkillsJX(req, resp); }
			else if (JX_SAVE_FUNDINGSOURCE.equals(accion)) { saveFundingSourceJX(req, resp); }
			else if (JX_VIEW_CHILDS_WBS_TEMPLATE.equals(accion)) { viewChildsWBSTemplate(req, resp); }
			else if (JX_WBS_CHART.equals(accion)) { wbsChartJX(req, resp); }
			else if (JX_DEL_WBS_TEMPLATE_NODE.equals(accion)) { deleteWbsTemplateNodeJX(req, resp); }
			else if (JX_VIEW_NODE_WBS_TEMPLATE.equals(accion)) { viewNodeWBSTemplateJX(req, resp); }
			else if (JX_SAVE_WBS_TEMPLATE_NODE.equals(accion)) { saveWBSTemplateNodeJX(req, resp); }
			else if (JX_SAVE_BUSINESS_DRIVER.equals(accion)) { saveBusinessDriverJX(req, resp); }
			else if (JX_DELETE_BUSINESS_DRIVER.equals(accion)) { deleteBusinessDriverJX(req, resp); }
			else if (JX_SAVE_BUSINESS_DRIVER_SET.equals(accion)) { saveBusinessDriverSetJX(req, resp); }
			else if (JX_DELETE_BUSINESS_DRIVER_SET.equals(accion)) { deleteBusinessDriverSetJX(req, resp); }
			else if (JX_VIEW_BUSINESS_DRIVERS.equals(accion)) { viewBusinessDriversJX(req, resp); }
			else if (JX_SAVE_JOB_CATEGORY.equals(accion)) { saveJobCategoryJX(req, resp); }
			else if (JX_DELETE_JOB_CATEGORY.equals(accion)) { deleteJobCategoryJX(req, resp); }
			else if (JX_CONS_SKILLS_AND_JOB_CATEGORIES.equals(accion)) { consultSkillsAndJobCategoriesJX(req, resp); }
			else if (JX_SAVE_LABEL.equals(accion)) { saveLabelJX(req, resp); }
			else if (JX_ENABLE_LABEL.equals(accion) || JX_DISABLE_LABEL.equals(accion)) { updateLabelStateJX(req, resp, accion); }
			else if (JX_SAVE_STAGE_GATE.equals(accion)) { saveStageGateJX(req, resp); }
			else if (JX_SAVE_RESOURCE_POOL.equals(accion)) { saveResourcePoolJX(req, resp); }
			else if (JX_CONS_MANAGE_POOL.equals(accion)) { consManagePoolJX(req, resp); }
			else if (JX_SAVE_MILESTONE_TYPE.equals(accion)) { saveMilestoneTypeJX(req, resp); }
			else if (JX_DELETE_MILESTONE_TYPE.equals(accion)) { deleteMilestoneTypeJX(req, resp); }
			else if (JX_SAVE_CLOSURE_CHECK.equals(accion)) { saveClosureCheckJX(req, resp); }
			else if (JX_DELETE_CLOSURE_CHECK.equals(accion)) { deleteClosureCheckJX(req, resp); }
			else if (JX_SAVE_RISK_CATEGORY.equals(accion)) { saveRiskCategoryJX(req, resp); }
			else if (JX_SAVE_PROBLEM_CHECK.equals(accion)) { saveProblemCheckJX(req, resp); }
			else if (JX_DELETE_PROBLEM_CHECK.equals(accion)) { deleteProblemCheckJX(req, resp); }
			else if (JX_SAVE_MILESTONE_CATEGORY.equals(accion)) { saveMilestoneCategoryJX(req, resp); }
			else if (JX_SAVE_CLASSIFICATION_LEVEL.equals(accion)) { saveClassificationLevelJX(req, resp); }
			else if (JX_ENABLE_MILESTONE_CATEGORY.equals(accion) || JX_DISABLE_MILESTONE_CATEGORY.equals(accion)) { updateMilestoneCategoryStateJX(req, resp, accion); }
            else if (JX_SAVE_BSC_DIMENSION.equals(accion)) { saveBscDimensionJX(req, resp); }
            // New security
            else if (SecurityUtil.hasPermission(req, MaintenanceAction.JX_SAVE_TECHNOLOGY, accion)) { saveTechnologyJX(req, resp); }
            else if (SecurityUtil.hasPermission(req, MaintenanceAction.JX_DELETE_TECHNOLOGY, accion)) { deleteTechnologyJX(req, resp); }
        }
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Delete technology
     *
     * @param req
     * @param resp
     */
    private void deleteTechnologyJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out	= resp.getWriter();

        try {

            // Request
            Integer idTechnology = ParamUtil.getInteger(req, Technology.IDTECHNOLOGY, null);

            JSONObject infoJSON;

            if (idTechnology != null) {

                // Declare logic
                TechnologyLogic technologyLogic = new TechnologyLogic();

                // Logic find object
                Technology technology = technologyLogic.findById(idTechnology);

                if (technology != null) {

                    // Logic delete
                    technologyLogic.remove(technology);

                    // Response
                    infoJSON = infoDeleted(getResourceBundle(req), "TECHNOLOGY");
                }
                else {
                    throw new LogicException("Not find technology");
                }
            }
            else {
                throw new LogicException("Not find id technology");
            }

            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }
    }

    /**
     * Save technology
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveTechnologyJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out	= resp.getWriter();

        try {

            // Request
            Technology technology = BeanUtil.loadBean(req, Technology.class, getDateFormat(req));

            JSONObject infoJSON	= null;

            // Create response
            //
            if (technology.getIdTechnology() == null) {
                infoJSON = infoCreated(getResourceBundle(req), infoJSON, "TECHNOLOGY");
            }
            else {
                infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "TECHNOLOGY");
            }

            // Declare logic
            TechnologyLogic technologyLogic = new TechnologyLogic();

            // Set company
            technology.setCompany(getCompany(req));

            // Logic save
            technology = technologyLogic.save(technology);

            // Response
            infoJSON.put(Technology.IDTECHNOLOGY, technology.getIdTechnology());

            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }
    }

    /**
	 * Save classification level
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveClassificationLevelJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idClassificationlevel	= ParamUtil.getInteger(req, Classificationlevel.IDCLASSIFICATIONLEVEL, -1);
		String name					= ParamUtil.getString(req, Classificationlevel.NAME);
		String description			= ParamUtil.getString(req, Classificationlevel.DESCRIPTION);
		
		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON						= null;
			Classificationlevel classificationlevel = null;
			
			// Declare logic
			ClassificationlevelLogic classificationlevelLogic = new ClassificationlevelLogic();
			
			// Create entity
			if (idClassificationlevel == -1) {
				
				classificationlevel = new Classificationlevel();
				
				classificationlevel.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "classificationLevel");
			}
			// Find for update entity
			else {
				classificationlevel = classificationlevelLogic.findById(idClassificationlevel, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "classificationLevel");
			}
			
			classificationlevel.setName(name);
			classificationlevel.setDescription(description);
			
			classificationlevel = classificationlevelLogic.save(classificationlevel);
		
			infoJSON.put(Classificationlevel.IDCLASSIFICATIONLEVEL, classificationlevel.getIdClassificationlevel());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Delete classification level
	 * 
	 * @param req
	 * @param resp
	 */
	private void deleteClassificationLevel(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Request
		Integer idClassificationlevel = ParamUtil.getInteger(req, Classificationlevel.IDCLASSIFICATIONLEVEL, -1);
		
		try {
			
			if (idClassificationlevel != -1){
				
				// Declare logic
				ClassificationlevelLogic classificationlevelLogic = new ClassificationlevelLogic();
				
				Classificationlevel classificationlevel = classificationlevelLogic.findById(idClassificationlevel);
				
				classificationlevelLogic.deleteClassificationLevel(classificationlevel);
				
				infoDeleted(req, "classificationLevel");
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete closure check
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteClosureCheckJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idClosureCheck	= ParamUtil.getInteger(req, Closurecheck.IDCLOSURECHECK, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= new JSONObject();
			
			if (idClosureCheck != -1){
				
				// Declare logic
				ClosurecheckLogic closurecheckLogic = new ClosurecheckLogic();
				
				Closurecheck closurecheck = new Closurecheck();
				
				closurecheck = closurecheckLogic.findById(idClosureCheck);
				
				closurecheckLogic.deleteClosureCheck(closurecheck);
				
				infoJSON = infoDeleted(getResourceBundle(req), "closure_check");
			}
			
			// Response
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

	/**
	 * Save closure check
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveClosureCheckJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idClosureCheck	= ParamUtil.getInteger(req, Closurecheck.IDCLOSURECHECK, -1);
		String name			= ParamUtil.getString(req, Closurecheck.NAME);
		String description	= ParamUtil.getString(req, Closurecheck.DESCRIPTION);
		String show			= ParamUtil.getString(req, Closurecheck.SHOWCHECK, null);
		
		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON			= null;
			Closurecheck closurecheck 	= null;
			
			// Declare logic
			ClosurecheckLogic closurecheckLogic = new ClosurecheckLogic();
			
			// Create entity
			if (idClosureCheck == -1) {
				
				closurecheck = new Closurecheck();
				
				closurecheck.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "closure_check");
			}
			// Find for update entity
			else {
				closurecheck = closurecheckLogic.findById(idClosureCheck, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "closure_check");
			}
			
			closurecheck.setName(name);
			closurecheck.setDescription(description);
			closurecheck.setShowCheck(show != null ? true:false);
			
			closurecheck = closurecheckLogic.save(closurecheck);
		
			infoJSON.put(Closurecheck.IDCLOSURECHECK, closurecheck.getIdClosureCheck());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Delete milestone type
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteMilestoneTypeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		Integer idMilestoneType = ParamUtil.getInteger(req, Milestonetype.IDMILESTONETYPE, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= new JSONObject();
			
			if (idMilestoneType != -1){
				MilestonetypeLogic milestonetypeLogic = new MilestonetypeLogic();
				Milestonetype milestonetype = new Milestonetype();
				
				milestonetype = milestonetypeLogic.findById(idMilestoneType);
				
				milestonetypeLogic.deleteMilestoneType(milestonetype);
				
				infoJSON = infoDeleted(getResourceBundle(req), "milestone_type");
			}
			
			// Response
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

	/**
	 * Save milestone type
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveMilestoneTypeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idMilestoneType	= ParamUtil.getInteger(req, Milestonetype.IDMILESTONETYPE, -1);
		String name			= ParamUtil.getString(req, Milestonetype.NAME);
		String description	= ParamUtil.getString(req, Milestonetype.DESCRIPTION);

		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON			= null;
			Milestonetype milestoneType	= null;
			
			// Declare logic
			MilestonetypeLogic milestonetypeLogic = new MilestonetypeLogic();
			
			// Create entity
			if (idMilestoneType == -1) {
				
				milestoneType = new Milestonetype();
				
				milestoneType.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "milestone_type");
			}
			// Find for update entity
			else {
				milestoneType = milestonetypeLogic.findById(idMilestoneType, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "milestone_type");
			}
			
			milestoneType.setName(name);
			milestoneType.setDescription(description);
			
			milestoneType = milestonetypeLogic.save(milestoneType);
		
			infoJSON.put(Milestonetype.IDMILESTONETYPE, milestoneType.getIdMilestoneType());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Cons resource managers to resourcepool
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void consManagePoolJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON	= new JSONObject();
			
			// Request 
			//
			int idResourcepool = ParamUtil.getInteger(req, Resourcepool.IDRESOURCEPOOL, -1);
			
			// Logic 
			//
			ManagepoolLogic managepoolLogic = new ManagepoolLogic();
			
			List<Managepool> listResourceManager = managepoolLogic.findByRelation(Managepool.RESOURCEPOOL, new Resourcepool(idResourcepool));
			
			// Parse to JSON 
			//
			JSONArray listResourceManagerJSON = new JSONArray();
			
			for (Managepool managepool : listResourceManager) {
				
				JSONObject managePoolJSON = new JSONObject();
				
				managePoolJSON.put(Employee.IDEMPLOYEE, managepool.getEmployee().getIdEmployee());
				
				listResourceManagerJSON.add(managePoolJSON);
			}
			
			// Response 
			//
			infoJSON.put("listResourceManager", listResourceManagerJSON);
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Delete resourcepool
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteResourcePool(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		try {
			// Request 
			//
			Resourcepool resourcepool = BeanUtil.loadBean(req, Resourcepool.class, getDateFormat(req));
			resourcepool.setCompany(getCompany(req));
			
			// Logic 
			//
			ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();
			
			resourcepoolLogic.deleteResourcePool(resourcepool);
			
			// Response 
			//
			infoDeleted(req, "maintenance.employee.resource_pool");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Save resourcepool
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveResourcePoolJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	
		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			// Request 
			//
			String[] idsResourceManagers 	= ParamUtil.getStringValues(req, "idManager", null);
			Resourcepool resourcepool 		= BeanUtil.loadBean(req, Resourcepool.class, getDateFormat(req));
			
			resourcepool.setCompany(getCompany(req));
			
			// Logic 
			//
			ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();
			
			resourcepool = resourcepoolLogic.save(resourcepool, idsResourceManagers);
			
			// Response 
			//
			// Create
			if (resourcepool.getIdResourcepool() == null) {
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.employee.resource_pool");
			}
			// Update
			else {
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.employee.resource_pool");
			}
			
			infoJSON.put(Resourcepool.IDRESOURCEPOOL, resourcepool.getIdResourcepool());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Delete Stage Gate
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteStageGate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int idStageGate = ParamUtil.getInteger(req, Stagegate.IDSTAGEGATE, -1);
		
		try {
			
			StagegateLogic stagegateLogic = new StagegateLogic();
			
			stagegateLogic.deleteStageGate(new Stagegate(idStageGate));
			
			infoDeleted(req, "stage_gate");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Save Stage Gate
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveStageGateJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int idStageGate		= ParamUtil.getInteger(req, Stagegate.IDSTAGEGATE, -1);
		String name			= ParamUtil.getString(req, Stagegate.NAME);
		String description	= ParamUtil.getString(req, Stagegate.DESCRIPTION);

		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON			= null;
			Stagegate stageGate					= null;
			StagegateLogic stagegateLogic = new StagegateLogic();
			
			// Create stage gate entity
			if (idStageGate == -1) {
				stageGate = new Stagegate();
				
				stageGate.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "stage_gate");
			}
			else {
				// Find stage gate for update entity
				stageGate = stagegateLogic.findById(idStageGate, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "stage_gate");
			}
			
			stageGate.setName(name);
			stageGate.setDescription(description);
			
			stageGate = stagegateLogic.save(stageGate);
		
			infoJSON.put(Stagegate.IDSTAGEGATE, stageGate.getIdStageGate());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Enable and disable label
	 * 
	 * @param req
	 * @param resp
	 * @param accion
	 * @throws IOException 
	 */
	private void updateLabelStateJX(HttpServletRequest req, HttpServletResponse resp, String accion) throws IOException {
		
		int idLabel = ParamUtil.getInteger(req, Label.IDLABEL, -1);
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		LabelLogic logic = new LabelLogic();
    		Label label = logic.findById(idLabel);
    		
    		label.setDisable(JX_DISABLE_LABEL.equals(accion));
			logic.save(label);
			
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.change_he", new JSONObject(), "label", JX_DISABLE_LABEL.equals(accion) ? "disabled" : "enabled"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Search LDAP
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void searchLDAPJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String nameFind = ParamUtil.getString(req, "nameFind", StringPool.BLANK);
		String lastNameFind = ParamUtil.getString(req, "lastNameFind", StringPool.BLANK);
		String userNameFind = ParamUtil.getString(req, "userNameFind", StringPool.BLANK);
		String emailFind	= ParamUtil.getString(req, "emailFind", StringPool.BLANK);

		PrintWriter out	= resp.getWriter();
		
		try {
			
			if (ValidateUtil.isNull(nameFind) && ValidateUtil.isNull(lastNameFind) && ValidateUtil.isNull(userNameFind) && ValidateUtil.isNull(emailFind)) {
				
				throw new LogicException("msg.error.ldap.on_filter_is_required");
			}
			ConnectLDAP connectLDAP = new ConnectLDAP(getSettings(req));
			List<UserLDAP> users = connectLDAP.findUsers(nameFind, lastNameFind, userNameFind, emailFind);
			
			out.print(JsonUtil.toJSON(users));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Consult skills and job categories
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void consultSkillsAndJobCategoriesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int idEmployee	= ParamUtil.getInteger(req, Employee.IDEMPLOYEE, -1);		

		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject updateJSON 					= new JSONObject();
			SkillsemployeeLogic skillsemployeeLogic = new SkillsemployeeLogic();
			JobcatemployeeLogic jobcatemployeeLogic = new JobcatemployeeLogic();
			
			JSONArray skillsJSON 					= new JSONArray();
			JSONArray jobcatemployeesJSON 			= new JSONArray();
			
			List<Skillsemployee> skillsDB = skillsemployeeLogic.findByEmployee(new Employee(idEmployee));			

			for (Skillsemployee s : skillsDB) {
				skillsJSON.add(s.getSkill().getIdSkill());
			}
			
			List<Jobcatemployee> jobcatemployees = jobcatemployeeLogic.findByRelation(Jobcatemployee.EMPLOYEE, new Employee(idEmployee));
			
			for (Jobcatemployee jobCatEmployee : jobcatemployees) {
				jobcatemployeesJSON.add(jobCatEmployee.getJobcategory().getIdJobCategory());
			}
		
			updateJSON.put("skills", skillsJSON);
			updateJSON.put("jobCategories", jobcatemployeesJSON);
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save Job Category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveJobCategoryJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idJobCategory 	= ParamUtil.getInteger(req, Jobcategory.IDJOBCATEGORY, -1);
		String name 			= ParamUtil.getString(req, Jobcategory.NAME, StringPool.BLANK);
		String description 		= ParamUtil.getString(req, Jobcategory.DESCRIPTION, StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JobcategoryLogic jobcategoryLogic 	= new JobcategoryLogic();
			Jobcategory jobcategory				= null;
			JSONObject infoJSON					= null;
			
			if (idJobCategory == -1){
				jobcategory = new Jobcategory();
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "job_category"); 
			}
			else{
				jobcategory = jobcategoryLogic.findById(idJobCategory, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "job_category");
			}
			
			jobcategory.setName(name);
			jobcategory.setDescription(description);
			jobcategory.setCompany(getCompany(req));
			
			jobcategoryLogic.save(jobcategory);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.JobCategoryToJSON(jobcategory),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }		
	}


	/**
	 * Delete Job Category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteJobCategoryJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idJobCategory 	= ParamUtil.getInteger(req, Jobcategory.IDJOBCATEGORY, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			if (idJobCategory != -1){
				JobcategoryLogic jobcategoryLogic 	= new JobcategoryLogic();
				Jobcategory	jobcategory 			= new Jobcategory();
				
				jobcategory = jobcategoryLogic.findById(idJobCategory);
				
				jobcategoryLogic.deleteJobCategory(jobcategory);
				
				infoJSON = infoDeleted(getResourceBundle(req), "job_category");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e, "idError"); }
		finally { out.close(); }	
	}

	/**
	 * View Busines Drivers associated with Business Driver Set
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void viewBusinessDriversJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Integer idBusinessDriverSet = ParamUtil.getInteger(req, Businessdriverset.IDBUSINESSDRIVERSET, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject updateJSON = new JSONObject();
			
			if (idBusinessDriverSet != -1){
				BusinessdriverLogic businessdriverLogic = new BusinessdriverLogic();
				
				List<Businessdriver> businessdrivers = businessdriverLogic.findByRelation(Businessdriver.BUSINESSDRIVERSET, new Businessdriverset(idBusinessDriverSet));
				
				JSONArray businessdriversJSON = new JSONArray();
				
				for (Businessdriver businessdriver : businessdrivers) {
					JSONObject businessdriverJSON = new JSONObject();
					
					businessdriverJSON.put(Businessdriver.IDBUSINESSDRIVER, businessdriver.getIdBusinessDriver());
					businessdriverJSON.put(Businessdriver.CODE, businessdriver.getCode());
					businessdriverJSON.put(Businessdriver.NAME, businessdriver.getName());
					businessdriverJSON.put(Businessdriver.RELATIVEPRIORIZATION, businessdriver.getRelativePriorization());
					
					businessdriversJSON.add(businessdriverJSON);
				}
				
				updateJSON.put("businessDrivers", businessdriversJSON);
			}
			
			out.print(updateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

	/**
	 * Delete Business Driver Set
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteBusinessDriverSetJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idBusinessDriverSet = ParamUtil.getInteger(req, Businessdriverset.IDBUSINESSDRIVERSET, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			if (idBusinessDriverSet != -1){
				BusinessdriversetLogic businessdriversetLogic 	= new BusinessdriversetLogic();
				Businessdriverset businessdriverset 			= new Businessdriverset();
				
				businessdriverset = businessdriversetLogic.findById(idBusinessDriverSet);
				
				businessdriversetLogic.deleteBusinessDriverSet(businessdriverset);
				
				infoJSON = infoDeleted(getResourceBundle(req), "business_driver_set");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

	/**
	 * Save Business Driver Set
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveBusinessDriverSetJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idBusinessDriverSet = ParamUtil.getInteger(req, Businessdriverset.IDBUSINESSDRIVERSET, -1);
		String name 				= ParamUtil.getString(req, Businessdriverset.NAME, StringPool.BLANK);
		
		PrintWriter out = resp.getWriter();
		
		try {
			BusinessdriversetLogic businessdriversetLogic 	= new BusinessdriversetLogic();
			Businessdriverset businessdriverset				= null;
			JSONObject infoJSON								= null;
			
			if (idBusinessDriverSet == -1){
				businessdriverset = new Businessdriverset();
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "business_driver"); 
			}
			else{
				businessdriverset = businessdriversetLogic.findById(idBusinessDriverSet, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "business_driver");
			}
			
			businessdriverset.setName(name);
			businessdriverset.setCompany(getCompany(req));
			
			businessdriversetLogic.save(businessdriverset);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.businessDriverSetToJSON(businessdriverset),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}


	/**
	 * Delete business driver
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteBusinessDriverJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idBusinessDriver = ParamUtil.getInteger(req, Businessdriver.IDBUSINESSDRIVER, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= null;
			
			if (idBusinessDriver != -1){
				BusinessdriverLogic businessdriverLogic = new BusinessdriverLogic();
				Businessdriver businessdriver 			= new Businessdriver();
				
				businessdriver = businessdriverLogic.findById(idBusinessDriver);
				
				businessdriverLogic.delete(businessdriver);
				
				infoJSON = infoDeleted(getResourceBundle(req), "business_driver");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

/**
 * Save business driver
 * @param req
 * @param resp
 * @throws IOException
 */
	private void saveBusinessDriverJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idBusinessDriver 	= ParamUtil.getInteger(req, Businessdriver.IDBUSINESSDRIVER, -1);
		String code 				= ParamUtil.getString(req, Businessdriver.CODE, StringPool.BLANK);
		String name 				= ParamUtil.getString(req, Businessdriver.NAME, StringPool.BLANK);
		Integer idBusinessDriverSet = ParamUtil.getInteger(req, Businessdriverset.IDBUSINESSDRIVERSET, -1);
		Double priority 			= ParamUtil.getDouble(req, Businessdriver.RELATIVEPRIORIZATION, null);
		
		PrintWriter out = resp.getWriter();
		
		try {
			BusinessdriverLogic businessdriverLogic = new BusinessdriverLogic();
			Businessdriver businessdriver 			= null;
			JSONObject infoJSON						= null;
			
			if (idBusinessDriver == -1){
				businessdriver = new Businessdriver();
				businessdriver.setRelativePriorization(0.0);
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "business_driver"); 
			}
			else {
				businessdriver = businessdriverLogic.findById(idBusinessDriver, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "business_driver");
			}
			
			if (!code.equals(StringPool.BLANK)) {
				businessdriver.setCode(code);
			}
			
			if (!name.equals(StringPool.BLANK)) {
				businessdriver.setName(name);
			}
			
			businessdriver.setCompany(getCompany(req));
			
			if (idBusinessDriverSet != -1) {
				businessdriver.setBusinessdriverset(new Businessdriverset(idBusinessDriverSet));
			}
			
			if (priority != null) {
				businessdriver.setRelativePriorization(priority);
			}
			
			businessdriver = businessdriverLogic.saveBusinessDriver(businessdriver);
			
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.businessDriverToJSON(businessdriver),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}


	/**
	 * Save WBS template node
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveWBSTemplateNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idRoot 		= ParamUtil.getInteger(req, "id", null);
		Integer idWbsnode 	= ParamUtil.getInteger(req, "wbs_id", null);
		Integer idParent 	= ParamUtil.getInteger(req, "wbs_parent", null);
		String code 		= ParamUtil.getString(req, "wbs_code", StringPool.BLANK);
		String name 		= ParamUtil.getString(req, "wbs_name", StringPool.BLANK);
		String description 	= ParamUtil.getString(req, "wbs_desc", StringPool.BLANK);
		Boolean isCA		= ParamUtil.getBoolean(req, "wbs_ca", false);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= new JSONObject();
			
			WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
			
			Wbstemplate wbstemplate = null;
			
			if (idWbsnode !=  null) {	//UPDATE
				List<String> joins = new ArrayList<String>();
				joins.add(Wbstemplate.WBSTEMPLATE);
				joins.add(Wbstemplate.WBSTEMPLATES);
				
				wbstemplate = wbsTemplateLogic.findById(idWbsnode, joins);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "wbs_node"); 
			}
			else {					//CREATE
				wbstemplate = new Wbstemplate();
				wbstemplate.setCompany(getCompany(req));
				wbstemplate.setRoot(idRoot);
				
				infoJSON =  infoCreated(getResourceBundle(req), infoJSON, "wbs_node"); 
			}
			
			if (wbstemplate != null) {
				wbstemplate.setCode(code);
				wbstemplate.setName(name);
				wbstemplate.setDescription(description);
				wbstemplate.setIsControlAccount(isCA);
				if (idParent != null) {
					wbstemplate.setWbstemplate(wbsTemplateLogic.findById(idParent));
				}
				
				wbsTemplateLogic.saveWBSTemplateNode(wbstemplate);
				
				infoJSON.put("idWbsnode", wbstemplate.getIdWbsnode());
				infoJSON.put("code", wbstemplate.getCode());
				infoJSON.put("name", wbstemplate.getName());
				infoJSON.put("idParent", wbstemplate.getWbstemplate() != null ? wbstemplate.getWbstemplate().getIdWbsnode() : null);
				infoJSON.put("description", wbstemplate.getDescription());
				infoJSON.put("isControlAccount", wbstemplate.getIsControlAccount());
			}
			
			out.print(infoJSON);	
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * View child WBS template
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void viewNodeWBSTemplateJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idRoot 		= ParamUtil.getInteger(req, "idRoot", -1);
		Integer idWbsnode 	= ParamUtil.getInteger(req, "idWbsnode", -1);
		
		PrintWriter out = resp.getWriter();
		try {
			JSONObject infoJSON		= new JSONObject();
			JSONArray parentsJSON 	= new JSONArray();
			
			WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
			
			if ( idWbsnode != -1 ) {
				Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWbsnode);
				
				infoJSON.put("idWbsnode", wbstemplate.getIdWbsnode());
				infoJSON.put("code", wbstemplate.getCode());
				infoJSON.put("name", wbstemplate.getName());
				infoJSON.put("description", wbstemplate.getDescription());
				infoJSON.put("isControlAccount", wbstemplate.getIsControlAccount());
				infoJSON.put("idParent", wbstemplate.getWbstemplate() != null ? wbstemplate.getWbstemplate().getIdWbsnode() : null);
			}
			
			//Parents list
			if ( idRoot != -1 ) {
				Wbstemplate wbstemplateRoot = wbsTemplateLogic.findById(idRoot);
				
				List<Wbstemplate> tree 	= wbsTemplateLogic.findWbsTemplate(wbstemplateRoot);
				
				for(Wbstemplate node : tree){
					if(!node.getIdWbsnode().equals(idWbsnode)){
						JSONObject nodeJSON = new JSONObject();
						
						nodeJSON.put("idWbsnode", node.getIdWbsnode());
						nodeJSON.put("name", node.getName());
						nodeJSON.put("isControlAccount", node.getIsControlAccount());
						
						parentsJSON.add(nodeJSON);
					}
				}
			}

			infoJSON.put("parents", parentsJSON);
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Delete node WBSTemplate
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void deleteWbsTemplateNodeJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idWbsnode = ParamUtil.getInteger(req, "id", -1);
		
		PrintWriter out = resp.getWriter();
		try {
			JSONObject infoJSON	= null;
			
			if(idWbsnode != -1){
				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
				
				Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWbsnode);
				
				wbsTemplateLogic.delete(wbstemplate);
				
				infoJSON = infoDeleted(getResourceBundle(req),"wbs_node");	
				
				out.print(infoJSON);
			}
			else {
				out.print(infoJSON);
			}
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Generate chart WBS template
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void wbsChartJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer idWbsnode = ParamUtil.getInteger(req, "id", -1);
		
		PrintWriter out = resp.getWriter();
		try {
			if(idWbsnode != -1){
				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
				
				Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWbsnode);
				
				ChartWBS chartWBS = new ChartWBS(wbstemplate);
				
				out.print(chartWBS.generateTemplate());
			}
			else {
				out.print(new JSONObject());
			}
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	private void viewChildsWBSTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int idWbsTemplate = ParamUtil.getInteger(req, "id", -1);

		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject upddateJSON = new JSONObject();
			
			if(idWbsTemplate != -1){
				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
				
				Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWbsTemplate);
				
				List<String> joins = new ArrayList<String>();
				joins.add(Wbstemplate.WBSTEMPLATE);
				
				List<Wbstemplate> childs = wbsTemplateLogic.findAllChildsWbsTemplate(wbstemplate, joins);
				
				JSONArray childsJSON = new JSONArray();
				
				for(Wbstemplate child : childs){
					
					JSONObject childJSON = new JSONObject();
					
					childJSON.put("idWbsnode", child.getIdWbsnode());
					childJSON.put("code", child.getCode());
					childJSON.put("name", child.getName());
					childJSON.put("isControlAccount", child.getIsControlAccount());
					childJSON.put("idParent", child.getWbstemplate().getIdWbsnode());
					
					childsJSON.add(childJSON);
				}
			
				upddateJSON.put("childs", childsJSON);
			}
			
			out.print(upddateJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
		
	}


	/**
	 * Delete WBS template
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteWbsTemplate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idWbsnode = ParamUtil.getInteger(req, "id", -1);		
		
		try {	
			if(idWbsnode != -1){
				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
				
				Wbstemplate wbstemplate = wbsTemplateLogic.findById(idWbsnode);
				
				wbsTemplateLogic.delete(wbstemplate);
				
				infoDeleted(req, "maintenance.wbs_templates");	
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
		
	
	/**
	 * Enable or disable employee
	 * @param req
	 * @param resp
	 * @param accion 
	 * @throws IOException 
	 */
	private void updateEmployeeStateJX(HttpServletRequest req,
			HttpServletResponse resp, String accion) throws IOException {
		
		int idEmployee = ParamUtil.getInteger(req, "idEmployee", -1);
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		EmployeeLogic employeeLogic = new EmployeeLogic();
    		Employee employee = employeeLogic.findById(idEmployee);
    		
    		employee.setDisable(JX_DISABLE_EMPLOYEE.equals(accion));
    		employeeLogic.save(employee);
			
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.change_he", new JSONObject(), "EMPLOYEE_PROFILE", JX_DISABLE_EMPLOYEE.equals(accion)?"SINGULAR_DISABLED":"enabled"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
	
	/**
	 * Enable or disable contact
	 * @param req
	 * @param resp
	 * @param accion 
	 * @throws IOException 
	 */
	private void updateContactStateJX(HttpServletRequest req,
			HttpServletResponse resp, String accion) throws IOException {
		
		int idContact = ParamUtil.getInteger(req, "idContact", -1);
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		ContactLogic contactLogic = new ContactLogic();
    		Contact contact = contactLogic.findById(idContact);
    		
    		contact.setDisable(JX_DISABLE_CONTACT.equals(accion));
			contactLogic.save(contact);
			
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.change_he", new JSONObject(), "contact", JX_DISABLE_CONTACT.equals(accion)?"disabled":"enabled"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Save or update Funding Source
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveFundingSourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		int idFundingSource	= ParamUtil.getInteger(req, "id", -1);
		String name 		= ParamUtil.getString(req, "name");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON						= null;
			Fundingsource fundingsource				= null;
			FundingsourceLogic fundingsourceLogic 	= new FundingsourceLogic();
			
			if (idFundingSource == -1){
			
				fundingsource = new Fundingsource();
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.fundingsource"); 
			}
			else {
				fundingsource = fundingsourceLogic.findById(idFundingSource, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.fundingsource");
			}
			
			fundingsource.setName(name);
			fundingsource.setCompany(getCompany(req));
			
			fundingsource = fundingsourceLogic.save(fundingsource);			
			
			infoJSON.put( Fundingsource.IDFUNDINGSOURCE, fundingsource.getIdFundingSource());
			infoJSON.put( Fundingsource.NAME, fundingsource.getName());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Delete Funding Source
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteFundingSource(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idFundingSource = ParamUtil.getInteger(req, "id", -1);
		
		try {		
			FundingsourceLogic fundingsourceLogic = new FundingsourceLogic();
			
			fundingsourceLogic.deleteFundingsource(new Fundingsource(idFundingSource));
			infoDeleted(req, "maintenance.fundingsource");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete Customer Type
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteCustomerType(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idCustomerType = ParamUtil.getInteger(req, Customertype.IDCUSTOMERTYPE, -1);
		
		try {
			CustomertypeLogic customertypeLogic = new CustomertypeLogic();
			customertypeLogic.deleteCustomertype(new Customertype(idCustomerType));
			infoDeleted(req, "customer_type");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Delete Label
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteLabel(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		int idLabel = ParamUtil.getInteger(req, Label.IDLABEL, -1);
		
		try {
			
			LabelLogic labelLogic = new LabelLogic();
			labelLogic.remove(new Label(idLabel));
			
			infoDeleted(req, "label");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Save or update label
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveLabelJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		int idLabel			= ParamUtil.getInteger(req, Label.IDLABEL, -1);
		String name			= ParamUtil.getString(req, Label.NAME);
		String description	= ParamUtil.getString(req, Label.DESCRIPTION);

		PrintWriter out			= resp.getWriter();
		
		try {
			JSONObject infoJSON			= null;
			Label label					= null;
			LabelLogic labelLogic = new LabelLogic();
			
			// Create label entity
			if (idLabel == -1) {
				label = new Label();
				
				label.setCompany(getCompany(req));
				label.setDisable(false);
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "label");
			}
			else {
				// Find label for update entity
				label = labelLogic.findById(idLabel, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "label");
			}
			
			label.setName(name);
			label.setDescription(description);
			
			label = labelLogic.save(label);
		
			infoJSON.put(Label.IDLABEL, label.getIdLabel());
			infoJSON.put(Label.DISABLE, label.getDisable() != null && label.getDisable());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save or update customer type
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveCustomerTypeJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		int idCustomerType	= ParamUtil.getInteger(req, Customertype.IDCUSTOMERTYPE, -1);
		String name			= ParamUtil.getString(req, Customertype.NAME);
		String description	= ParamUtil.getString(req, Customertype.DESCRIPTION);

		PrintWriter out			= resp.getWriter();
		
		try {
			JSONObject infoJSON					= null;
			Customertype type					= null;
			CustomertypeLogic customertypeLogic = new CustomertypeLogic();
			
			if (idCustomerType == -1) {
				type = new Customertype();
				
				type.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "customer_type");
			}
			else {
				type = customertypeLogic.findById(idCustomerType, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "customer_type");
			}
			
			type.setName(name);
			type.setDescription(description);
			
			type = customertypeLogic.save(type);
		
			infoJSON.put(Customertype.IDCUSTOMERTYPE, type.getIdCustomerType());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
	 * Delete Metric
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteMetric(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idMetricKpi = ParamUtil.getInteger(req, Metrickpi.IDMETRICKPI, -1);
		
		try {
			MetrickpiLogic metrickpiLogic = new MetrickpiLogic();
			
			metrickpiLogic.deleteMetrickpi(new Metrickpi(idMetricKpi));
			infoDeleted(req, "metric");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}


	/**
	 * Save or create Metric Kpi
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void saveMetricJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int idMetricKpi		= ParamUtil.getInteger(req, Metrickpi.IDMETRICKPI, -1);
		String name			= ParamUtil.getString(req, Metrickpi.NAME);
		String definition	= ParamUtil.getString(req, Metrickpi.DEFINITION);
        String type			= ParamUtil.getString(req, Metrickpi.TYPE, null);
		int idBscDimension	= ParamUtil.getInteger(req, Bscdimension.IDBSCDIMENSION, -1);

		PrintWriter out = resp.getWriter();
		
		try {
			Metrickpi metric				= null;
			JSONObject infoJSON				= null;
			MetrickpiLogic metrickpiLogic 	= new MetrickpiLogic();
			
			if (idMetricKpi == -1) {
				metric = new Metrickpi();
				metric.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "metric");
			}
			else {
				metric = metrickpiLogic.findById(idMetricKpi, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "metric");
			}
			
			metric.setName(name);
            metric.setType(type != StringPool.BLANK ? type : null);
			metric.setDefinition(definition);
            metric.setBscdimension(idBscDimension != -1 ? new Bscdimension(idBscDimension) : null);

			metric = metrickpiLogic.save(metric);
			
			infoJSON.put(Metrickpi.IDMETRICKPI, metric.getIdMetricKpi());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void viewMaintenance(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.maintenance"));
		forward("/index.jsp?nextForm=maintenance/maintenance", req, resp);
	}

	/**
	 * Save Customer Exception
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveCustomerJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idCustomer		= ParamUtil.getInteger(req, "id", -1);
		String name			= ParamUtil.getString(req, "name");
		int idCustomerType	= ParamUtil.getInteger(req, "type", -1);
		String description	= ParamUtil.getString(req, "description");
		
		PrintWriter out		= resp.getWriter();
		
		try {
			JSONObject infoJSON = null;
			Customer customer 	= null;
			CustomerLogic customerLogic = new CustomerLogic();
	    	
	    	if (idCustomer == -1) {   
	    		customer = new Customer();
	    		
	    		infoJSON = infoCreated(getResourceBundle(req), infoJSON, "customer"); 
	    	}
	    	else {
	    		customer = customerLogic.findById(idCustomer, false);
	    		
	    		infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "customer");
	    	}
	    	
	    	if (idCustomerType == -1) { customer.setCustomertype(null); }
	    	else { customer.setCustomertype(new Customertype(idCustomerType)); }
	    	
    		customer.setName(name);
    		customer.setDescription(description);
    		customer.setCompany(getCompany(req));
    		customerLogic.save(customer);
			
			infoJSON.put("idCustomer", customer.getIdCustomer());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save Seller Exception
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveSellerJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idSeller			= ParamUtil.getInteger(req, "id", -1);
		String name				= ParamUtil.getString(req, "name");
		Boolean selected		= ParamUtil.getBoolean(req, "selected", false);
		Boolean qualified		= ParamUtil.getBoolean(req, "qualified", false);
		Date qualifiedDate		= ParamUtil.getDate(req, "qualifiedDate", getDateFormat(req), null);
		String information		= ParamUtil.getString(req, "information");
		Boolean sole			= ParamUtil.getBoolean(req, "sole", false);
		Boolean single			= ParamUtil.getBoolean(req, "single", false);
		
		PrintWriter out			= resp.getWriter();
		
		try {
			SellerLogic sellerLogic = new SellerLogic();
			
			JSONObject infoJSON	= null;
			Seller seller 		= null;
	    	
	    	if (idSeller == -1) {    		
	    		seller = new Seller();
	    		
	    		infoJSON = infoCreated(getResourceBundle(req), infoJSON, "seller"); 
	    	}
	    	else{
	    		seller = sellerLogic.findById(idSeller, false);
	    		
	    		infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "seller");
	    	}
	    	
    		seller.setName(name);
    		seller.setInformation(information);
    		seller.setSelected(selected);
    		seller.setQualified(qualified);
    		seller.setQualificationDate(qualifiedDate);
    		seller.setSingleSource(single);
    		seller.setSoleSource(sole);
    		
    		seller.setCompany(getCompany(req));
			
			seller = sellerLogic.save(seller);
			
			infoJSON.put("idSeller", seller.getIdSeller());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save Operation
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveOperationJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idOperation 			= ParamUtil.getInteger(req, "id", -1);
		String operationName 		= ParamUtil.getString(req, "name", "");
		String operationCode		= ParamUtil.getString(req, "code", "");
		Integer idOpAccount			= ParamUtil.getInteger(req, "idOpAccount", -1);
		boolean availableForManager = ParamUtil.getBoolean(req, "availableForManager", false);
		boolean availableForApprove = ParamUtil.getBoolean(req, Operation.AVAILABLE_FOR_APPROVE, false);
        boolean excludeExternals    = ParamUtil.getBoolean(req, Operation.EXCLUDEEXTERNALS, false);
		
		PrintWriter out = resp.getWriter();	
		
		try {
			JSONObject infoJSON				= null;
			Operation operation 			;
			OperationLogic operationLogic 	= new OperationLogic();
			
			if (idOperation == -1){
				operation = new Operation();
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "operation"); 
			}
			else{
				operation = operationLogic.consOperation(idOperation);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "operation");
			}

			Operationaccount operationaccount = new Operationaccount();
			if (idOpAccount != -1) {
				operationaccount.setIdOpAccount(idOpAccount);
				operation.setOperationaccount(operationaccount);
			}
			
			operation.setOperationCode(operationCode);
			operation.setOperationName(operationName);
			operation.setAvailableForManager(availableForManager);
			operation.setAvailableForApprove(availableForApprove);
            operation.setExcludeExternals(excludeExternals);
			
			operationLogic.save(operation);			
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.operationToJSON(operation), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save OperationAccount
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveOperationAccountJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idOperationAccount		= ParamUtil.getInteger(req, "id", -1);
		String description			= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();	
		
		try {
			JSONObject infoJSON							= null;
			Operationaccount operationaccount 			= null;
			OperationaccountLogic operationaccountLogic = new OperationaccountLogic();
			
			if (idOperationAccount == -1) {
				operationaccount = new Operationaccount();
				
				operationaccount.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.operation.op_account"); 
			}
			else {
				operationaccount = operationaccountLogic.findById(idOperationAccount);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.operation.op_account");
			}

			operationaccount.setDescription(description);
			
			operationaccountLogic.save(operationaccount);			
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.operationAccountToJSON(operationaccount),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save Geography
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveGeographyJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idGeography 	= ParamUtil.getInteger(req, "id", -1);
		String name			= ParamUtil.getString(req, "name", "");
		String description	= ParamUtil.getString(req, "description", "");
		
		PrintWriter out 	= resp.getWriter();
		
		try {		
			JSONObject infoJSON	= null;
			Geography geography = null;
			GeographyLogic geoLogic = new GeographyLogic();
			
			if (idGeography == -1){
				geography = new Geography();
				geography.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "geography"); 
			}
			else{				
				geography = geoLogic.findById(idGeography);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "geography");
			}
			
			geography.setName(name);
			geography.setDescription(description);
			
			geoLogic.save(geography);			
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.geographyToJSON(geography), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save Category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveCategoryJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idCategory		= ParamUtil.getInteger(req, "id", -1);
		String categoryName = ParamUtil.getString(req, "name", "");
		String description	= ParamUtil.getString(req, "description", "");
        Boolean enable      = ParamUtil.getBoolean(req, Category.ENABLE);
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON	= null;
			Category category	= null;
			CategoryLogic categoryLogic = new CategoryLogic();
			
			if (idCategory == -1){
				category 		= new Category();
				category.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "category"); 
			}
			else {
				category = categoryLogic.findById(idCategory);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "category");
			}
			
			category.setName(categoryName);
			category.setDescription(description);
            category.setEnable(enable);
			
			categoryLogic.save(category);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.categoryToJSON(category), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


	/**
	 * Save Expenseaccounts
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveExpenseAccountJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idExpenseAccount	= ParamUtil.getInteger(req, "id", -1);
		String description		= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();
		
		try {	
			JSONObject infoJSON							= null;
			Expenseaccounts expenseaccount 				= null;
			ExpenseaccountsLogic expenseaccountsLogic	= new ExpenseaccountsLogic();
			
			if (idExpenseAccount == -1){
				expenseaccount 	= new Expenseaccounts();
				expenseaccount.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.expenseaccounts"); 
			}
			else{
				expenseaccount = expenseaccountsLogic.findById(idExpenseAccount);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.expenseaccounts");
			}
			
			expenseaccount.setDescription(description);
			
			expenseaccountsLogic.save(expenseaccount);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.expenseAccountToJSON(expenseaccount), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}	
	}

	/**
	 * Save contracttype
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveContractTypeJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idContractType	= ParamUtil.getInteger(req, "id", -1);
		String description	= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON			= null;
			Contracttype contractType 	= null;
			ContractTypeLogic contractTypeLogic = new ContractTypeLogic();
			
			if (idContractType == -1){
				contractType 	= new Contracttype();
				contractType.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "contract_type");
			}
			else{
				contractType = contractTypeLogic.findById(idContractType);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "contract_type");
			}
			
			contractType.setDescription(description);
			
			contractTypeLogic.save(contractType);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.contractTypeToJSON(contractType),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}	
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveChangeTypeJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idChangeType	= ParamUtil.getInteger(req, "id", -1);
		String description	= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON			= null;
			Changetype changeType 	= null;
			ChangeTypesLogic changeTypeLogic = new ChangeTypesLogic();
			
			if (idChangeType == -1){
				changeType 	= new Changetype();
				changeType.setCompany(getCompany(req));
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "change_type");
			}
			else{			
				changeType = changeTypeLogic.findById(idChangeType);
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "change_type");
			}
			
			changeType.setDescription(description);
			
			changeType = changeTypeLogic.save(changeType);
			
			infoJSON.put(Changetype.IDCHANGETYPE, changeType.getIdChangeType());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}	
	}

	/**
	 * Save budgetaccounts
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveBudbetAccountJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idBudgetAccounts	= ParamUtil.getInteger(req, "id", -1);
		String description		= ParamUtil.getString(req, "description", "");
		Integer typeCost		= ParamUtil.getInteger(req, "type_cost", -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON				= null;
			Budgetaccounts budgetaccounts 	= null;
			BudgetaccountsLogic budgetaccountsLogic = new BudgetaccountsLogic();
			
			if (idBudgetAccounts == -1){
				budgetaccounts 	= new Budgetaccounts();
				budgetaccounts.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.budgetaccounts"); 
			}
			else{
				budgetaccounts = budgetaccountsLogic.findById(idBudgetAccounts);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.budgetaccounts"); 
			}
			
			budgetaccounts.setDescription(description);
			budgetaccounts.setTypeCost(typeCost);
			
			budgetaccountsLogic.save(budgetaccounts);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.budgetAccountsToJSON(getResourceBundle(req), budgetaccounts),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * return the username of contact
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void getUserNameJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idContact = ParamUtil.getInteger(req, "idContact",-1);
		
		PrintWriter out = resp.getWriter();	
		
		try {
			SecurityLogic securityLogic = new SecurityLogic();
			
			Security security = securityLogic.getByContact(new Contact(idContact));
			
			JSONObject dataJSON = new JSONObject();
			
			if (security == null) {
				dataJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.username"));
			}
			else {
				dataJSON.put("login", security.getLogin());
			}
			
			out.print(dataJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Reset Password
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void resetPasswordJX(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idContact = ParamUtil.getInteger(req, "idContact", -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			SecurityLogic securityLogic = new SecurityLogic();
			ContactLogic contactLogic 	= new ContactLogic();
			
			JSONObject replyJSON 		= new JSONObject();
			
			if (idContact == -1) {
				throw new EmployeeNotFoundException("msg.error.employee");
			}
			else {
				Contact contact = contactLogic.consultContact(idContact);
				
				String to = contact.getEmail();
				String password = null;
				if (!Constants.SECURITY_LOGIN_LDAP.equals(SettingUtil.getString(getSettings(req), Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
						&& Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS))
						&& to != null && !"".equals(to)) {
					
					password = SecurityUtil.getRandomString(8);
				}
				Security security = securityLogic.changePassword(new Contact(idContact), password);
				
				if (security == null) {
					throw new ChangePasswordException("msg.error.change_password");
				}
				else {
					if (!ValidateUtil.isNull(to)
							&& Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS))
							&& SettingUtil.sendPassword(getSettings(req))) {
						try {							
							EmailUtil email = new EmailUtil(getSettings(req), null, to);
							
							ParamResourceBundle bundle = new ParamResourceBundle("msg.mail.subject",getResourceBundle(req).getString("maintenance.employee.email.login"));
							
							email.setSubject(bundle.getMessage(getResourceBundle(req)));
							
							StringBuilder bodytext = new StringBuilder();
							bodytext.append(
									new ParamResourceBundle("maintenance.employee.email.user_register", contact.getFullName()).toString(getResourceBundle(req)) +
									"\n\n" + getResourceBundle(req).getString("maintenance.employee.username")+": " +
									security.getLogin() + "\n" + getResourceBundle(req).getString("maintenance.employee.password")+": " + password);
							
							email.setBodyText(bodytext.toString());
							email.send();
						}
						catch (Exception e) {
							replyJSON.put(StringPool.INFORMATION, getResourceBundle(req).getString("msg.info.mail_account"));
							ExceptionUtil.sendToLogger(LOGGER, e, null);
						}
					}
					replyJSON.put(StringPool.SUCCESS, getResourceBundle(req).getString("msg.info.change_password"));
				}
			}
			out.print(replyJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * save contact
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveContactJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idContact					= ParamUtil.getInteger(req, "id", -1);
		String contactFullName 			= ParamUtil.getString(req, "full_name", "");
		String contactJobTitle			= ParamUtil.getString(req, "job_title", "");	
		String contactFileAs			= ParamUtil.getString(req, "file_as", "");	
		String contactBusinessPhone		= ParamUtil.getString(req, "business_phone", "");
		String contactMobilePhone		= ParamUtil.getString(req, "mobile_phone", "");
		String contactBusinessAddress	= ParamUtil.getString(req, "business_address", "");
		String contactEmail				= ParamUtil.getString(req, "email", "");	
		String contactNotes				= ParamUtil.getString(req, "notes", "");	
		String login 					= ParamUtil.getString(req, "login");
		
		PrintWriter out = resp.getWriter();
		try {
			SecurityLogic securityLogic	= new SecurityLogic();
			ContactLogic contactLogic	= new ContactLogic();
			
			JSONObject infoJSON	= null;
			Contact contact 	= null;
			
			if (idContact == -1){
				contact = new Contact();
				contact.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "contact"); 
			}
			else {
				contact = contactLogic.findById(idContact, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "contact");
			}
			
			contact.setFullName(contactFullName);
			contact.setJobTitle(contactJobTitle);
			contact.setFileAs(contactFileAs);
			contact.setBusinessPhone(contactBusinessPhone);
			contact.setMobilePhone(contactMobilePhone);
			contact.setBusinessAddress(contactBusinessAddress);
			contact.setEmail(contactEmail);
			contact.setNotes(contactNotes);
			
			if (contact.getDisable() == null) { contact.setDisable(false); }
			
			if (contact.getIdContact() == null) {			
				Security security = new Security();
				security.setLogin(login);
				security.setAttempts(0);
				security.setDateCreation(DateUtil.getCalendar().getTime());
				
				if (securityLogic.isUserName(security)) { throw new LoginNameInUseException(); }
				
				contactLogic.saveContact(contact, security);				
			}
			else {
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_PMO)) {
					if (securityLogic.isUserName(contact, login)) { throw new LoginNameInUseException(); }
					else {
						List<Security> securities = (List<Security>)securityLogic.findByRelation(Security.CONTACT, contact);
						
						if (!securities.isEmpty() && securities.size() == 1) {
							
							Security security = securities.get(0);
							if (!security.getLogin().equals(login)) {
								
								security.setLogin(login);
								securityLogic.save(security);
							}
						}
					}
				}
					
				contactLogic.save(contact);
				idContact = contact.getIdContact();
				contact = contactLogic.consultContact(idContact);	
			}
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.contactToJSON(contact),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save skill
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveSkillJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idSkill			= ParamUtil.getInteger(req, "id", -1);
		String name			= ParamUtil.getString(req, "name", "");	
		String description	= ParamUtil.getString(req, "description", "");	
		
		PrintWriter out = resp.getWriter();
		
		try {
			SkillLogic skillLogic 	= new SkillLogic();
			JSONObject infoJSON 	= null;
			Skill skill 			= null;
			
			if (idSkill == -1){
				skill = new Skill();
				skill.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "skill");
			}
			else {
				skill = skillLogic.findById(new Skill(idSkill));
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "skill");
			}
			
			skill.setName(name);
			skill.setDescription(description);
			
			skill = skillLogic.save(skill);
			
			infoJSON.put("id", skill.getIdSkill());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save performing organization
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void savePerfOrgJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idPerfOrg			= ParamUtil.getInteger(req, "id", -1);
		int idPerforgManager 	= ParamUtil.getInteger(req, "manager");
		boolean perforgOnsaas	= ParamUtil.getBoolean(req, "onsaas", false);	
		String perforgName		= ParamUtil.getString(req, "name", "");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON 					= null;
			Performingorg perforg 					= null;
			PerformingOrgLogic performingOrgLogic 	= new PerformingOrgLogic();
			
			if (idPerfOrg == -1){
				perforg = new Performingorg();
				perforg.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "perf_organization");
			}
			else {
				perforg = performingOrgLogic.consPerforg(idPerfOrg);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "perf_organization");
			}
			
			perforg.setEmployee(new Employee(idPerforgManager));
			perforg.setName(perforgName);
			perforg.setOnSaaS(perforgOnsaas);
			
			perforg = performingOrgLogic.save(perforg);
			
			if (idPerfOrg == -1) {
				
				Employee pmoForPO = new Employee();
				pmoForPO.setContact(getUser(req).getContact());
				pmoForPO.setPerformingorg(perforg);
				pmoForPO.setResourceprofiles( new Resourceprofiles(Constants.ROLE_PMO));
				pmoForPO.setProfileDate(new Date());
				
				EmployeeLogic employeeLogic = new EmployeeLogic();
				employeeLogic.save(pmoForPO);
			}
			
			infoJSON.put(Performingorg.IDPERFORG, perforg.getIdPerfOrg());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}

	/**
	 * Save company
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveCompanyJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idCompany	= ParamUtil.getInteger(req, "id", -1);
		String companyName	= ParamUtil.getString(req, "name", "");	
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			CompanyLogic companyLogic = new CompanyLogic();
			JSONObject infoJSON	= null;
			Company company 	= null;
			
			if (idCompany == -1){
				company = new Company();
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "company"); 
			}
			else{
				
				company = companyLogic.findById(idCompany, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "company");
			}
			
			company.setName(companyName);
			
			companyLogic.save(company);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.companyToJSON(company),infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}
	
	/**
	 * Delete operation
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteOperation(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idOperation = ParamUtil.getInteger(req, "id", -1);
		
		try {
			OperationLogic operationLogic = new OperationLogic();
			
			operationLogic.deleteOperation(idOperation);
			infoDeleted(req, "operation");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete operationaccount
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteOperationAccount(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idOperationAccount = ParamUtil.getInteger(req, "id", -1);
		
		try {
			OperationaccountLogic operationaccountLogic = new OperationaccountLogic();
			
			operationaccountLogic.deleteOperationAccount(idOperationAccount);
			infoDeleted(req, "maintenance.operation.op_account");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete geography
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteGeography(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idGeography = ParamUtil.getInteger(req, "id", -1);
		
		try {
			GeographyLogic geographyLogic = new GeographyLogic();
			
			geographyLogic.deleteGeography(idGeography);
			infoDeleted(req, "geography");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idCategory = ParamUtil.getInteger(req, "id", -1);
		
		try {
			CategoryLogic categoryLogic = new CategoryLogic();
			categoryLogic.deleteCategory(new Category(idCategory));
			infoDeleted(req, "category");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Delete customer
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idCustomer = ParamUtil.getInteger(req, "id", -1);
		
		try {
			CustomerLogic customerLogic = new CustomerLogic();
			customerLogic.deleteCustomer(new Customer(idCustomer));
			infoDeleted(req, "customer");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Delete seller
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteSeller(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idSeller = ParamUtil.getInteger(req, "id", -1);
		
		try {
			SellerLogic sellerLogic = new SellerLogic();
			
			sellerLogic.deleteSeller(new Seller(idSeller));
			infoDeleted(req, "seller");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}


	/**
	 * Delete Expenseaccounts
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteExpenseAccount(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idExpenseAccount = ParamUtil.getInteger(req, "id", -1);
		
		try {
			ExpenseaccountsLogic expenseaccountsLogic = new ExpenseaccountsLogic();
			
			expenseaccountsLogic.deleteExpenseAccount(idExpenseAccount);
			infoDeleted(req, "maintenance.expenseaccounts");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete Contracttype
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteContractType(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idContractType = ParamUtil.getInteger(req, "id", -1);
		
		try {
			ContractTypeLogic contractTypeLogic = new ContractTypeLogic();
			contractTypeLogic.deleteContractType(idContractType);
			infoDeleted(req, "contract_type");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Delete Changetype
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteChangeType(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idChangeType = ParamUtil.getInteger(req, "id", -1);
		
		try {		
			ChangeTypesLogic logic = new ChangeTypesLogic();
			logic.delete(new Changetype(idChangeType));
			infoDeleted(req, "change_type");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete budgetAccounts
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteBudgetAccount(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idBudgetAccounts = ParamUtil.getInteger(req, "id", -1);
		
		try {
			BudgetaccountsLogic budgetaccountsLogic = new BudgetaccountsLogic();
			budgetaccountsLogic.deleteBudgetAccounts(idBudgetAccounts);
			infoDeleted(req, "maintenance.budgetaccounts");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete contact
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idContact = ParamUtil.getInteger(req, "id", -1);
		
		try {
			ContactLogic contactLogic = new ContactLogic();
			contactLogic.deleteContact(idContact);
			infoDeleted(req, "contact");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete skill
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteSkill(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idSkill = ParamUtil.getInteger(req, "id", -1);
		
		try {
			SkillLogic skillLogic = new SkillLogic();
			
			skillLogic.deleteSkill(idSkill);
			infoDeleted(req, "skill");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete performing organization
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deletePerfOrg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idPerfOrg = ParamUtil.getInteger(req, "id", -1);
		
		try {
			PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
			
			performingOrgLogic.deletePerfOrg(idPerfOrg);
			infoDeleted(req, "perf_organization");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Save employee
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveEmployee(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idEmployee 				= ParamUtil.getInteger(req, "employee_id", -1);		
		double costRate 			= ParamUtil.getCurrency (req, "cost_rate", 0);
		Integer idContact 			= ParamUtil.getInteger(req, "idContact", null);
		Integer idResourcePool		= ParamUtil.getInteger(req, "idManager", null);
		Integer idProfile 			= ParamUtil.getInteger(req, "idProfile", null);
		Date profileDate 			= ParamUtil.getDate(req, "profileDate", getDateFormat(req), new Date());
		String[] idSkills			= ParamUtil.getStringValues(req, "skills", null);
		boolean isUpdate 			= ParamUtil.getBoolean(req, "isUpdate", false);
		Integer idCalendarBase		= ParamUtil.getInteger(req, "idCalendarBase", null);
		String[] idsJobCategories	= ParamUtil.getStringValues(req, "jobCategories", null);
		Integer idSeller 			= ParamUtil.getInteger(req, "idSeller", null);
		
		try {
			ContactLogic contactLogic 	= new ContactLogic();
	    	EmployeeLogic employeeLogic = new EmployeeLogic();
	    	SecurityLogic securityLogic = new SecurityLogic();

            if(!isUpdate) {

                if (contactLogic.hasProfile(getUser(req).getPerformingorg(), new Contact(idContact),
                                                new Resourceprofiles(idProfile))) {

                    throw new LogicException("msg.error.contact_has_profile");
                }
                else if (idProfile == Constants.ROLE_RESOURCE &&
                            contactLogic.hasResourcePool(new Contact(idContact), new Resourceprofiles(Constants.ROLE_RESOURCE))) {

                    throw new LogicException("MSG.ERROR.CONTACT_HAS_RESOURCEPOOL");
                }
            }

            Employee employee = null;

            if (idEmployee == -1) {
                employee = new Employee();
                infoCreated(req, "maintenance.employee");
            } else {
                employee = employeeLogic.consEmployee(idEmployee);
                infoUpdated(req, "maintenance.employee");
            }

            if (idProfile == Constants.ROLE_PORFM) {
                employee.setPerformingorg(null);
            } else if (employee.getPerformingorg() == null) {
                employee.setPerformingorg(getUser(req).getPerformingorg());
            }

            int employees = employeeLogic.rowCountEq(Employee.CONTACT, new Contact(idContact));
            if (employees == 0) {

                Contact contact = contactLogic.consultContact(idContact);
                Security security = securityLogic.getByContact(new Contact(idContact));

                //Password
                String password = security.getLogin();
                String to = contact.getEmail();

                if (Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS)) &&
                        !ValidateUtil.isNull(to) && !"".equals(to)) {
                    password = SecurityUtil.getRandomString(8);
                }
                security.setPassword(SecurityUtil.md5(password));

                contactLogic.saveContact(contact, security);

                if (!ValidateUtil.isNull(to) && Boolean.parseBoolean(SettingUtil.getString(getSettings(req), Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS))) {

                    try {
                        EmailUtil email = new EmailUtil(getSettings(req), null, to);

                        ParamResourceBundle bundle = new ParamResourceBundle("msg.mail.subject", getResourceBundle(req).getString("maintenance.employee.email.login"));

                        email.setSubject(bundle.getMessage(getResourceBundle(req)));

                        StringBuilder bodytext = new StringBuilder();
                        bodytext.append(
                                new ParamResourceBundle("maintenance.employee.email.user_register", contact.getFullName()).toString(getResourceBundle(req)) +
                                        "\n\n" + getResourceBundle(req).getString("url") + ": " +
                                        req.getRequestURL().substring(0, req.getRequestURL().lastIndexOf("/")) + "/\n" + getResourceBundle(req).getString("maintenance.employee.username") + ": " +
                                        security.getLogin());

                        if (SettingUtil.sendPassword(getSettings(req))) {
                            bodytext.append("\n" + getResourceBundle(req).getString("maintenance.employee.password") + ": " + password);
                        }

                        email.setBodyText(bodytext.toString());
                        email.send();
                    } catch (Exception e) {
                        ExceptionUtil.sendToLogger(LOGGER, e, null);
                    }
                }
            }

            if (idResourcePool != null) {
                employee.setResourcepool(new Resourcepool(idResourcePool));
            }
            if (idCalendarBase != null) {
                employee.setCalendarbase(new Calendarbase(idCalendarBase));
            }

            employee.setContact(new Contact(idContact));
            employee.setResourceprofiles(new Resourceprofiles(idProfile));
            employee.setCostRate(costRate);
            employee.setProfileDate(profileDate);

            if (employee.getDisable() == null) {
                employee.setDisable(false);
            }

            // Skills
            List<Skill> listSkills = null;
            List<Jobcategory> listJobcategories = null;

            if (idSkills != null) {
                listSkills = new ArrayList<Skill>();
                for (String i : idSkills) {
                    Skill sk = new Skill(Integer.parseInt(i));
                    listSkills.add(sk);
                }
            }

            // Job categories
            if (idsJobCategories != null) {
                listJobcategories = new ArrayList<Jobcategory>();
                for (String i : idsJobCategories) {
                    Jobcategory jobCat = new Jobcategory(Integer.parseInt(i));
                    listJobcategories.add(jobCat);
                }
            }

            // Seller
            if (idSeller != null) {
                employee.setSeller(new Seller(idSeller));
            } else {
                employee.setSeller(null);
            }

            employeeLogic.saveEmployee(employee, listSkills, listJobcategories);
            employee = employeeLogic.consEmployee(employee.getIdEmployee());

		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		consMaintenance(req, resp, null);
	}

	/**
	 * Delete employee
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteEmployee(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idEmployee = ParamUtil.getInteger(req, "id", -1);
		
		try {
	    	EmployeeLogic employeeLogic = new EmployeeLogic();
	    	
	    	employeeLogic.deleteEmployee(idEmployee);
			infoDeleted(req, "maintenance.employee");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	/**
	 * Delete company
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteCompany(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idCompany = ParamUtil.getInteger(req, "id", -1);
		
		try {
			CompanyLogic companyLogic = new CompanyLogic();
			companyLogic.delete(new Company(idCompany));
			infoDeleted(req, "company");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Save a document or manual
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void saveDocumentation(HttpServletRequest req, HttpServletResponse resp)
		throws IOException, ServletException {
		
		int docId = Integer.parseInt(getMultipartField("idDocumentation"));
		int idManten = Integer.parseInt(getMultipartField("idManten"));
		String namePopup = getMultipartField("namePopup");
		
		try {	
			FileItem dataFile = getMultipartFields().get("file");
						
			if (dataFile != null) {

				Documentation doc = new Documentation();
				Contentfile docFile = new Contentfile();

                // Declare logics
				DocumentationLogic docLogic = new DocumentationLogic();
				ContentFileLogic fileLogic  = new ContentFileLogic();
				
				if(docId != -1) {

                    // Logic find doc
					doc = docLogic.findById(docId);

					infoUpdated(req, "maintenance.documentation_manuals.documentation");
				}					
				else {
					infoCreated(req, "maintenance.documentation_manuals.documentation");
				}
				
				if(dataFile.getSize() > 0) {

                    // Delete old file if update doc
                    if (docId != -1) {
                        fileLogic.delete(fileLogic.findByDocumentation(doc));
                    }

                    // Set data content file
                    docFile.setExtension(FilenameUtils.getExtension(dataFile.getName()));
                    docFile.setMime(dataFile.getContentType());
                    docFile.setContent(dataFile.get());

                    // Logic save content file
                    docFile = fileLogic.save(docFile);

                    // Update doc
                    doc.setNameFile(dataFile.getName());
                    doc.setContentfile(docFile);
				}				
				
				doc.setNamePopup(namePopup);
				doc.setCompany(getCompany(req));

                // Logic save doc
				docLogic.save(doc);
			}		
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		consMaintenance(req, resp, idManten);
	}
	
	/**
	 * Delete a document or manual
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteDocumentation(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		int idDocumentation = ParamUtil.getInteger(req, "id", -1);		
		
		try {
            // Declare logic
			DocumentationLogic docLogic = new DocumentationLogic();

            // Logic delete
            docLogic.deleteDocument(idDocumentation);

			infoDeleted(req, "maintenance.documentation_manuals.documentation");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}

	@SuppressWarnings("rawtypes")
	private void consMaintenance(HttpServletRequest req,
			HttpServletResponse resp, Integer idMant) throws ServletException, IOException {
		
		int idManten = -1;
		if(idMant != null) {idManten = idMant;}else {idManten = ParamUtil.getInteger(req, "idManten", -1);}
		
		List list = null;
		
		try {
			CustomerLogic customerLogic 		= new CustomerLogic();
			CustomertypeLogic customertypeLogic = new CustomertypeLogic();
	    	EmployeeLogic employeeLogic 		= new EmployeeLogic();
			
			list  = maintenanceCheck(idManten, getUser(req), getCompany(req));	
			
			if (idManten == Constants.MANT_PERFORMING_ORG){ 
				List<Employee> listEmployees = employeeLogic.searchEmployees(
						"", "", Constants.ROLE_FM, -1, getUser(req));
				
				List<String> joins = new ArrayList<String>();
				joins.add(Performingorg.EMPLOYEE);
				joins.add(Performingorg.EMPLOYEE+"."+Employee.CONTACT);
				
				PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
				List<Performingorg> perfOrgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), joins);
				
				req.setAttribute("perfOrgs", perfOrgs);
				req.setAttribute("listEmployees", listEmployees);
			}
			else if (idManten == Constants.MANT_CONTACT){
				CompanyLogic companyLogic = new CompanyLogic();
				List<Company> listCompanies = companyLogic.findAll();	
				
				req.setAttribute("listCompanies", listCompanies);
			}
			
			else if (idManten == Constants.MANT_RESOURCE){
				
				ResourceProfilesLogic resourceProfilesLogic = new ResourceProfilesLogic();
				PerformingOrgLogic performingOrgLogic		= new PerformingOrgLogic();
				CalendarBaseLogic calendarBaseLogic 		= new CalendarBaseLogic();
				SkillLogic skillLogic						= new SkillLogic();
				JobcategoryLogic jobcategoryLogic			= new JobcategoryLogic();
				SellerLogic sellerLogic						= new SellerLogic();
				ResourcepoolLogic resourcepoolLogic			= new ResourcepoolLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Employee.CONTACT);
				
				List<Resourceprofiles> profiles		= resourceProfilesLogic.findAll();
				List<Performingorg> perforgs 		= performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req));
				List<Skill> listSkills				= skillLogic.findByRelation(Skill.COMPANY, getCompany(req), Skill.NAME, Constants.ASCENDENT);
				List<Calendarbase> calendars 		= calendarBaseLogic.findByRelation(Calendarbase.COMPANY, getCompany(req));
				List<Jobcategory> listJobCategories	= jobcategoryLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Jobcategory.NAME, Constants.ASCENDENT);
				List<Seller> listSellers			= sellerLogic.findByRelation(Jobcategory.COMPANY, getCompany(req), Seller.NAME, Constants.ASCENDENT);
				List<Resourcepool> listResourcePool = resourcepoolLogic.findByRelation(Resourcepool.COMPANY, getCompany(req));
				
				joins.add(Employee.PERFORMINGORG);
				joins.add(Employee.SELLER);
				joins.add(Employee.RESOURCEPROFILES);
				joins.add(Employee.RESOURCEPOOL);
				joins.add(Resourcepool.EMPLOYEES + "." + Employee.CONTACT);
				
				list = employeeLogic.findByCompany(getCompany(req), joins);
				
				req.setAttribute("profiles", profiles);
				req.setAttribute("perforgs", perforgs);
				req.setAttribute("listResourcePool", listResourcePool);
				req.setAttribute("listSkills", listSkills);
				req.setAttribute("calendars", calendars);
				req.setAttribute("listJobCategories", listJobCategories);
				req.setAttribute("listSellers", listSellers);
			}
			
			else if (idManten == Constants.MANT_OPERATION) {
				
				OperationLogic operationLogic 	= new OperationLogic();
				
				list = operationLogic.findByAllCompany(getCompany(req));
				
				OperationaccountLogic operationaccountLogic = new OperationaccountLogic();
				
				List<Operationaccount> listOpAccounts  = operationaccountLogic.findByRelation(Operationaccount.COMPANY, getCompany(req));
				
				req.setAttribute("listOpAccounts", listOpAccounts);
			}
			
			else if (idManten == Constants.MANT_SELLERS) {
				
				SellerLogic sellerLogic = new SellerLogic();
				
				List<Seller> listSellers = sellerLogic.findByRelation(Seller.COMPANY, getCompany(req));
				
				req.setAttribute("listSellers", listSellers);
			}
			
			else if (idManten == Constants.MANT_CUSTOMERS) {
				
				List<Customertype> types = customertypeLogic.findByCompany(getUser(req));
				List<Customer> listCustomers = customerLogic.findByCompany(getUser(req));
				
				req.setAttribute("listCustomers", listCustomers);
				req.setAttribute("types", types);
			}
			
			else if (idManten == Constants.MANT_METRIC_KPI) {
				BscdimensionLogic bscdimensionLogic = new BscdimensionLogic();
				MetrickpiLogic metrickpiLogic 		= new MetrickpiLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Metrickpi.BSCDIMENSION);
				
				List<Bscdimension> bscdimensions	= bscdimensionLogic.findByCompany(getUser(req));
				List<Metrickpi> metrics				= metrickpiLogic.findByCompany(getUser(req), joins);
				
				req.setAttribute("bscdimensions", bscdimensions);
				req.setAttribute("metrics", metrics);
			}
			else if (idManten == Constants.MANT_CUSTOMER_TYPE) {
				
				List<Customertype> customerTypes	= customertypeLogic.findByCompany(getUser(req));
				req.setAttribute("customerTypes", customerTypes);
			}
			
			else if (idManten == Constants.MANT_SKILL) {

				SkillLogic skillLogic = new SkillLogic();
				List<Skill> skills = skillLogic.findByRelation(Skill.COMPANY, getCompany(req));
				
				req.setAttribute("skills", skills);
			}
			
			else if (idManten == Constants.MANT_BUDGETACCOUNTS) {
				
				BudgetaccountsLogic budgetaccountsLogic = new BudgetaccountsLogic();
				
				list = budgetaccountsLogic.findByRelation(Budgetaccounts.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_CATEGORY) {
				
				CategoryLogic categoryLogic = new CategoryLogic();
				
				list = categoryLogic.findByRelation(Category.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_CONTRACTTYPE){
				ContractTypeLogic contractTypeLogic = new ContractTypeLogic();
				
				list = contractTypeLogic.findByRelation(Contracttype.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_EXPENSEACCOUNTS){
				ExpenseaccountsLogic expenseaccountsLogic 	= new ExpenseaccountsLogic();
				
				list = expenseaccountsLogic.findByRelation(Expenseaccounts.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_OPERATIONACCOUNT){
				OperationaccountLogic operationaccountLogic = new OperationaccountLogic();
				
				list = operationaccountLogic.findByRelation(Operationaccount.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_FUNDINGSOURCE) {
				FundingsourceLogic fundingsourceLogic = new FundingsourceLogic();
				
				List<Fundingsource> listFundingSource = fundingsourceLogic.findByCompany(getUser(req));
				
				req.setAttribute("listFundingSource", listFundingSource);
			}	
			else if(idManten == Constants.MANT_CHANGETYPE){
				ChangeTypesLogic changeTypeLogic = new ChangeTypesLogic();
				
				list = changeTypeLogic.findByRelation(Changetype.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_WBS){
				WBSTemplateLogic wbsTemplateLogic = new WBSTemplateLogic();
				
				list = wbsTemplateLogic.findByCompanyAndRelationNull(getCompany(req), Wbstemplate.WBSTEMPLATE);
			}
			else if(idManten == Constants.MANT_BUSINESS_DRIVER){		
				BusinessdriverLogic businessdriverLogic 		= new BusinessdriverLogic();
				BusinessdriversetLogic businessdriversetLogic 	= new BusinessdriversetLogic();
				List<String> joins 								= new ArrayList<String>();
				
				joins.add(Businessdriver.BUSINESSDRIVERSET);
				
				list 										= businessdriverLogic.findByRelation(Businessdriver.COMPANY, getCompany(req), joins);
				List<Businessdriverset> businessdriversets 	= businessdriversetLogic.findByRelation(Businessdriverset.COMPANY, getCompany(req));
				
				req.setAttribute("businessdriversets", businessdriversets);
			}
			else if(idManten == Constants.MANT_BUSINESS_DRIVER_SET){		
				BusinessdriversetLogic businessdriversetLogic = new BusinessdriversetLogic();
				
				list = businessdriversetLogic.findByRelation(Businessdriverset.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_RISK_TEMPLATES){		
				RiskregistertemplateLogic riskregistertemplateLogic = new RiskregistertemplateLogic();
				List<String> joins 									= new ArrayList<String>();
				
				joins.add(Riskregistertemplate.RISKCATEGORIES);
				
				list = riskregistertemplateLogic.findByRelation(Riskregistertemplate.COMPANY, getCompany(req), joins);
			}
			else if(idManten == Constants.MANT_JOB_CATEGORY){		
				JobcategoryLogic jobcategoryLogic 	= new JobcategoryLogic();
				
				list = jobcategoryLogic.findByRelation(Businessdriverset.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_LABEL){		
				LabelLogic logic = new LabelLogic();
				
				list = logic.findByRelation(Label.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_STAGE_GATES){	
				StagegateLogic stagegateLogic = new StagegateLogic();
				
				list = stagegateLogic.findByRelation(Stagegate.COMPANY, getCompany(req));
			}
			else if(idManten == Constants.MANT_RESOURCE_POOL){	
				ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();
				
				list = resourcepoolLogic.findByRelation(Stagegate.COMPANY, getCompany(req));
				
				List<Employee> listResourceManager = employeeLogic.consEmployeesByRole(Constants.ROLE_RM, getCompany(req));
				
				req.setAttribute("listResourceManager", listResourceManager);
			}
			else if (idManten == Constants.MANT_MILESTONES_TYPE) {	
				
				// Declare Logic
				MilestonetypeLogic milestonetypeLogic = new MilestonetypeLogic();
				
				// Logic
				list = milestonetypeLogic.findByRelation(Milestonetype.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_CLOSURE_CHECK) {	
				
				// Declare Logic
				ClosurecheckLogic closurecheckLogic = new ClosurecheckLogic();
				
				// Logic
				list = closurecheckLogic.findByRelation(Milestonetype.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_RISK_CATEGORY) {
				
				RiskcategoryLogic riskCategoryLogic = new RiskcategoryLogic();
				
				list = riskCategoryLogic.findByRelation(Category.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_PROBLEM_CHECK) {
				
				ProblemcheckLogic problemcheckLogic = new ProblemcheckLogic();
				
				list = problemcheckLogic.findByRelation(Category.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_MILESTONE_CATEGORY) {
				
				MilestonecategoryLogic milestonecategoryLogic = new MilestonecategoryLogic();
				
				list = milestonecategoryLogic.findByRelation(Category.COMPANY, getCompany(req));
			}
			else if (idManten == Constants.MANT_CLASSIFICATION_LEVEL) {
				
				ClassificationlevelLogic classificationlevelLogic = new ClassificationlevelLogic();
				
				list = classificationlevelLogic.findByRelation(Category.COMPANY, getCompany(req));
			}
            else if (idManten == Constants.MANT_BSC_DIMENSION) {

                BscdimensionLogic bscdimensionLogic = new BscdimensionLogic();

                list = bscdimensionLogic.findByRelation(Bscdimension.COMPANY, getCompany(req));
            }
            else if (idManten == Constants.MANT_TECHNOLOGIES_MAP) {

                TechnologyLogic technologyLogic = new TechnologyLogic();

                list = technologyLogic.findByRelation(Category.COMPANY, getCompany(req));
            }
            //TODO los mantenimientos que no estan aqui se hacen por la rest
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		req.setAttribute("list", list);
		req.setAttribute("idManten", idManten);
		req.setAttribute("title", getResourceBundle(req).getString("menu.maintenance"));
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		forward("/index.jsp?nextForm=maintenance/maintenance", req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void consSkillsJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		int idEmployee	= ParamUtil.getInteger(req, Employee.IDEMPLOYEE, -1);		

		PrintWriter out	= resp.getWriter();
		
		try {
			SkillsemployeeLogic skillsemployeeLogic = new SkillsemployeeLogic();
			
			List<Integer> skills = new ArrayList<Integer>();
			List<Skillsemployee> skillsDB = skillsemployeeLogic.findByEmployee(new Employee(idEmployee));			

			for (Skillsemployee s : skillsDB) {
				skills.add(s.getSkill().getIdSkill());
			}				
			out.print(skills);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * 
	 * @param idManten
	 * @param company 
	 * @return
	 * @throws LogicException
	 */
	@SuppressWarnings("rawtypes")
	private List maintenanceCheck(int idManten, Employee user, Company company) throws Exception {
		List list = null;
		List<String> joins = null;
		
		switch (idManten) {
		case Constants.MANT_COMPANY:
			CompanyLogic companyLogic = new CompanyLogic();
			list = companyLogic.findAll();
			break;
			
		case Constants.MANT_PROGRAM:
			ProgramLogic programLogic = new ProgramLogic();
			list = programLogic.findAllWithManager();
			break;
			
		case Constants.MANT_CONTACT:
			ContactLogic contactLogic = new ContactLogic();
			joins = new ArrayList<String>();
			joins.add(Contact.COMPANY);
			list = contactLogic.findByRelation(Contact.COMPANY, company, joins);
			break;
			
		case Constants.MANT_GEOGRAPHY:			
			GeographyLogic geoLogic = new GeographyLogic();
			list = geoLogic.findByRelation(Geography.COMPANY, company);
			break;
			
		case Constants.MANT_FUNDINGSOURCE:
			FundingsourceLogic funLogic = new FundingsourceLogic();
			list = funLogic.findByRelation(Fundingsource.COMPANY, company);
			break;	
			
		case Constants.MANT_DOCUMENTATION:
			DocumentationLogic docLogic = new DocumentationLogic();
			list = docLogic.findByRelation(Documentation.COMPANY, company);
			break;
		default: break;
		}
		return list;
	}
	
	/**
	 * Delete riskCategory
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteRiskCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idRiskCategory = ParamUtil.getInteger(req, "id", -1);
		
		try {
			RiskcategoryLogic riskCategoryLogic = new RiskcategoryLogic();
			riskCategoryLogic.deleteRiskCategory(new Riskcategory(idRiskCategory));
			infoDeleted(req, "riskCategory");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Save Risk Category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveRiskCategoryJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idRiskCategory		= ParamUtil.getInteger(req, "id", -1);
		String categoryName = ParamUtil.getString(req, "name", "");
		String description	= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON	= null;
			Riskcategory riskCategory	= null;
			RiskcategoryLogic riskCategoryLogic = new RiskcategoryLogic();
			
			if (idRiskCategory == -1){
				riskCategory 		= new Riskcategory();
				riskCategory.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "riskCategory"); 
			}
			else {
				riskCategory = riskCategoryLogic.findById(idRiskCategory);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "riskCategory");
			}
			
			riskCategory.setName(categoryName);
			riskCategory.setDescription(description);
			
			riskCategoryLogic.save(riskCategory);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.riskCategoryToJSON(riskCategory), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Delete problem check
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void deleteProblemCheckJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idProblemCheck	= ParamUtil.getInteger(req, Problemcheck.IDPROBLEMCHECK, -1);
		
		PrintWriter out = resp.getWriter();
		
		try {
			JSONObject infoJSON	= new JSONObject();
			
			if (idProblemCheck != -1){
				
				// Declare logic
				ProblemcheckLogic problemLogic = new ProblemcheckLogic();
				
				Problemcheck problemcheck = new Problemcheck();
				
				problemcheck = problemLogic.findById(idProblemCheck);
				
				problemLogic.deleteProblemCheck(problemcheck);
				
				infoJSON = infoDeleted(getResourceBundle(req), "problem_check");
			}
			
			// Response
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }	
	}
	
	/**
	 * Save problem check
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveProblemCheckJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// Request
		int idProblemCheck	= ParamUtil.getInteger(req, Problemcheck.IDPROBLEMCHECK, -1);
		String name			= ParamUtil.getString(req, Problemcheck.NAME);
		String description	= ParamUtil.getString(req, Problemcheck.DESCRIPTION);
		String show			= ParamUtil.getString(req, Problemcheck.SHOWCHECK, null);
		
		PrintWriter out	= resp.getWriter();
		
		try {
			JSONObject infoJSON			= null;
			Problemcheck problemcheck 	= null;
			
			// Declare logic
			ProblemcheckLogic problemcheckLogic = new ProblemcheckLogic();
			
			// Create entity
			if (idProblemCheck == -1) {
				
				problemcheck = new Problemcheck();
				
				problemcheck.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "problem_check");
			}
			// Find for update entity
			else {
				problemcheck = problemcheckLogic.findById(idProblemCheck, false);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "problem_check");
			}
			
			problemcheck.setName(name);
			problemcheck.setDescription(description);
			problemcheck.setShowCheck(show != null ? true:false);
			
			problemcheck = problemcheckLogic.save(problemcheck);
		
			infoJSON.put(Problemcheck.IDPROBLEMCHECK, problemcheck.getIdProblemCheck());
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Save Milestone Category
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveMilestoneCategoryJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idMilestoneCategory		= ParamUtil.getInteger(req, "id", -1);
		String categoryName = ParamUtil.getString(req, "name", "");
		String description	= ParamUtil.getString(req, "description", "");
		
		PrintWriter out = resp.getWriter();
		
		try {		
			JSONObject infoJSON	= null;
			Milestonecategory milestoneCategory	= null;
			MilestonecategoryLogic milestoneCategoryLogic = new MilestonecategoryLogic();
			
			if (idMilestoneCategory == -1){
				milestoneCategory 		= new Milestonecategory();
				milestoneCategory.setCompany(getCompany(req));
				
				infoJSON = infoCreated(getResourceBundle(req), infoJSON, "maintenance.milestone_category"); 
			}
			else {
				milestoneCategory = milestoneCategoryLogic.findById(idMilestoneCategory);
				
				infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "maintenance.milestone_category");
			}
			
			milestoneCategory.setName(categoryName);
			milestoneCategory.setDescription(description);
			
			if (milestoneCategory.getDisable() == null) { milestoneCategory.setDisable(false); }
			
			milestoneCategoryLogic.save(milestoneCategory);
			
			out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.milestoneCategoryToJSON(milestoneCategory), infoJSON));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}
	
	/**
	 * Delete milestoneCategory
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void deleteMilestoneCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int idMilestonecCategory = ParamUtil.getInteger(req, "id", -1);
		
		try {
			MilestonecategoryLogic milestoneCategoryLogic = new MilestonecategoryLogic();
			milestoneCategoryLogic.deleteMilestoneCategory(new Milestonecategory(idMilestonecCategory));
			infoDeleted(req, "maintenance.milestone_category");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		consMaintenance(req, resp, null);
	}
	
	/**
	 * Enable or disable milestonecategory
	 * @param req
	 * @param resp
	 * @param accion 
	 * @throws IOException 
	 */
	private void updateMilestoneCategoryStateJX(HttpServletRequest req,
			HttpServletResponse resp, String accion) throws IOException {
		
		int idMilestoneCategory = ParamUtil.getInteger(req, "idMilestoneCategory", -1);
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		MilestonecategoryLogic milestoneCategoryLogic = new MilestonecategoryLogic();
    		Milestonecategory milestoneCategory = milestoneCategoryLogic.findById(idMilestoneCategory);
    		
    		milestoneCategory.setDisable(JX_DISABLE_MILESTONE_CATEGORY.equals(accion));
    		milestoneCategoryLogic.save(milestoneCategory);
			
    		out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.change_he", new JSONObject(), "maintenance.milestone_category", JX_DISABLE_MILESTONE_CATEGORY.equals(accion)?"disabled":"enabled"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Save or create bsc dimension
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveBscDimensionJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name	= ParamUtil.getString(req, "name", "");
        int idBscDimension	= ParamUtil.getInteger(req, "id", -1);

        PrintWriter out = resp.getWriter();

        try {
            Bscdimension bscDim             = null;
            JSONObject infoJSON				= null;
            BscdimensionLogic bscDimLogic   = new BscdimensionLogic();

            if (idBscDimension == -1) {
                bscDim = new Bscdimension();

                bscDim.setCompany(getCompany(req));

                infoJSON = infoCreated(getResourceBundle(req), infoJSON, "metric.bsc_dimension");
            }
            else {
                bscDim = bscDimLogic.findById(idBscDimension);

                infoJSON = infoUpdated(getResourceBundle(req), infoJSON, "metric.bsc_dimension");
            }

            bscDim.setName(name);

            bscDimLogic.save(bscDim);

            out.print(putInfo(StringPool.SUCCESS, JSONModelUtil.bscDimensionToJSON(bscDim),infoJSON));
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }

    }

    /**
     * Delete bsc dimension
     *
     * @param req
     * @param resp
     */
    private void deleteBscDimension(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int idBscDimension = ParamUtil.getInteger(req, "id", -1);

        try {
            BscdimensionLogic bscDimLogic = new BscdimensionLogic();
            bscDimLogic.remove(idBscDimension);
            infoDeleted(req, "metric.bsc_dimension");
        }
        catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

        consMaintenance(req, resp, null);
    }


}


