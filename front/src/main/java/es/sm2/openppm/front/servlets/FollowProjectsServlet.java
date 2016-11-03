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
 * File: FollowProjectsServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.core.logic.impl.ChangeControlLogic;
import es.sm2.openppm.core.logic.impl.ChecklistLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.IssuelogLogic;
import es.sm2.openppm.core.logic.impl.MilestoneLogic;
import es.sm2.openppm.core.logic.impl.MilestonecategoryLogic;
import es.sm2.openppm.core.logic.impl.MilestonetypeLogic;
import es.sm2.openppm.core.logic.impl.ProcurementpaymentsLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectKpiLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectlabelLogic;
import es.sm2.openppm.core.logic.impl.RiskRegisterLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestones.MilestonePending;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.wrap.ProjectWrap;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class ProjectServer
 */
public class FollowProjectsServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	public final static Logger LOGGER = Logger.getLogger(FollowProjectsServlet.class);
	
	public final static String REFERENCE = "followprojects";
	
	/***************** Actions ****************/
	public static final String REPORT_PROJECT 				= "report-project";
	public static final String REPORT_PROJECTS 				= "report-projects";
	public static final String REPORT_PROJECTS_EXECUTIVE 	= "report-projects-executive";
	
	/************** Actions AJAX **************/

	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
    	String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {
			
			/***************** Actions ****************/
			if (REPORT_PROJECT.equals(accion)) { reportProject(req, resp); }
			else if (REPORT_PROJECTS.equals(accion)) { reportProjects(req, resp); }
			else if (REPORT_PROJECTS_EXECUTIVE.equals(accion) && Settings.REPORT_EXECUTIVE) { reportProjectsExecutive(req, resp); }
			
			/************** Actions AJAX **************/
			
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

	/**
	 * Follow-up executive report of projects
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
    private void reportProjectsExecutive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	List<ProjectWrap> projects = null;
		
		String strIds		= ParamUtil.getString(req, "ids");
		String propiedad 	= ParamUtil.getString(req, "propiedad");
		String orden 		= ParamUtil.getString(req, "orden");
		
		try {
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			ProjectSearch projectSearch = new ProjectSearch(getUser(req), getSettings(req));
			projectSearch.setProjects(ParamUtil.getIntegersSplit(req, "ids"));
			
			projects = projectLogic.findProjectsForExecutiveReport(projectSearch);
			
			TimesheetLogic timesheetLogic 						= new TimesheetLogic();
			ProjectActivityLogic projectActivityLogic 			= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
			
			int i = 0;
			for (ProjectWrap projectWrap : projects) {
				
				// AC activities 
				//
				List<Projectactivity> activities = projectActivityLogic.consActivities(new Project(projectWrap.getIdProject()), null);
				
				// Find setting
				boolean exludeWithProvider = SettingUtil.getBoolean(getSettings(req),
						Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER,
						Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT);
				
				// Calculate AC
				double	acApp3 				= 0.0;
				for (Projectactivity activity : activities) {
					
					acApp3 += timesheetLogic.calcACforActivityAndStatus(activity.getIdActivity(), Constants.TIMESTATUS_APP3, null, exludeWithProvider);
				}
				
				// Actual payments
				//
				List<String> joins = new ArrayList<String>();
				joins.add(Procurementpayments.SELLER);
				
				List<Procurementpayments> procPayments = procurementpaymentsLogic.consProcurementPaymentsByProject(new Project(projectWrap.getIdProject()), joins);
				
				double totalActualPayment 	= 0.0;
				for (Procurementpayments procPayment : procPayments) {
					totalActualPayment += procPayment.getActualPayment();
				}
				
				
				projects.get(i).setLastAc(acApp3 + totalActualPayment);
				
				i++;
			}
			
			Collections.sort(projects, new EntityComparator<ProjectWrap>(new Order(propiedad, orden)));
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		if (projects != null) {	
			req.setAttribute("projects", projects);
			req.setAttribute("ids", strIds);
			req.setAttribute("propiedad", propiedad);
			req.setAttribute("orden", orden);
			
			/* Filters */
			req.setAttribute("customerType", ParamUtil.getString(req, "customerType",null));
			req.setAttribute("customer", ParamUtil.getString(req, "customer"));
			req.setAttribute("program", ParamUtil.getString(req, "program"));
			req.setAttribute("PM", ParamUtil.getString(req, "PM"));
			
			req.setAttribute("category", ParamUtil.getString(req, "category"));
			req.setAttribute("geography", ParamUtil.getString(req, "geography"));
			req.setAttribute("seller", ParamUtil.getString(req, "seller"));
			req.setAttribute("sponsor", ParamUtil.getString(req, "sponsor"));
			req.setAttribute("fundingSource", ParamUtil.getString(req, "fundingSource"));
			req.setAttribute("label", ParamUtil.getString(req, "label"));
			
			forward("/index.jsp?nextForm=project/follow/colective_executive_follow", req, resp);
		}	
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
		
	}

	/**
     * Follow-up report of projects
     * 
     * @param req
     * @param resp
     * @throws ServletException
	 * @throws IOException
     */
	private void reportProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<ProjectWrap> projects 			= null;
		List<Milestones> milestones 		= null;
		Milestonetype milestonetype 		= null;
		Milestonecategory milestonecategory = null;
		List<Projectkpi> projectkpis 		= null;

		String strIds		= ParamUtil.getString(req, "ids");
		String property 	= ParamUtil.getString(req, "propiedad");
		String orden 		= ParamUtil.getString(req, "orden");
		// Request Milestones
		Date since	 				= ParamUtil.getDate(req, "milestoneDateSince", getDateFormat(req), null);
		Date until	 				= ParamUtil.getDate(req, "milestoneDateUntil", getDateFormat(req), null);
		Integer idMilestoneType 	= ParamUtil.getInteger(req, Milestones.MILESTONETYPE, -1);
		Integer idMilestoneCategory = ParamUtil.getInteger(req, Milestones.MILESTONECATEGORY, -1);
		String milestonePending 	= ParamUtil.getString(req, "milestonePending");

		try {

			List<Integer> ids = ParamUtil.getIntegersSplit(req, "ids");

			// Declare logic
			ProjectLogic projectLogic 						= new ProjectLogic(getSettings(req), getResourceBundle(req));
			MilestoneLogic milestoneLogic 					= new MilestoneLogic();
			MilestonetypeLogic milestonetypeLogic 			= new MilestonetypeLogic();
			MilestonecategoryLogic milestonecategoryLogic 	= new MilestonecategoryLogic();
			ProjectKpiLogic projectKpiLogic 				= new ProjectKpiLogic();

			if (ValidateUtil.isNotNull(ids)) {

				ProjectSearch projectSearch = new ProjectSearch(getUser(req), getSettings(req));
				projectSearch.setProjects(ids);

				// Project wrapper
				projects = projectLogic.findProjects(projectSearch);

				// Sort projects
				//
				String propertyOrder = "employeeByProjectManager".equals(property)?"projectManager":property;
				Collections.sort(projects, new EntityComparator<ProjectWrap>(new Order(propertyOrder, orden)));

				// Get milestone type
				if (idMilestoneType != -1) {
					milestonetype = milestonetypeLogic.findById(idMilestoneType);
				}

				// Get milestone category
				if (idMilestoneCategory != -1) {
					milestonecategory = milestonecategoryLogic.findById(idMilestoneCategory);
				}

				// Get milestones
				//
				List<String> joins = new ArrayList<String>();
				joins.add(Milestones.MILESTONETYPE);
				joins.add(Milestones.PROJECT);

				List<Project> projectsTemp = new ArrayList<Project>();

				for (Integer idProject : ids) {

					Project project = new Project(idProject);

					projectsTemp.add(project);
				}

				milestones	= milestoneLogic.filter(projectsTemp, milestonetype, milestonecategory, since, until, milestonePending, Milestones.PLANNED, Constants.ASCENDENT, joins);

				// Get KPIs
				//
				projectkpis = projectKpiLogic.getAggregates(ids.toArray(new Integer[ids.size()]));

				// Sort
				Collections.sort(projectkpis, new EntityComparator<Projectkpi>(new Order("metrick", Order.ASC)));
				Collections.sort(projectkpis, new EntityComparator<Projectkpi>(new Order("projectName", Order.ASC)));
			}
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		if (ValidateUtil.isNotNull(projects)) {
			req.setAttribute("projects", projects);
			req.setAttribute("ids", strIds);
			req.setAttribute("propiedad", property);
			req.setAttribute("orden", orden);
			
			/* Filters */
			req.setAttribute("customerType", ParamUtil.getString(req, "customerType",null));
			req.setAttribute("customer", ParamUtil.getString(req, "customer"));
			req.setAttribute("program", ParamUtil.getString(req, "program"));
			req.setAttribute("PM", ParamUtil.getString(req, "PM"));
			
			req.setAttribute("category", ParamUtil.getString(req, "category"));
			req.setAttribute("geography", ParamUtil.getString(req, "geography"));
			req.setAttribute("seller", ParamUtil.getString(req, "seller"));
			req.setAttribute("sponsor", ParamUtil.getString(req, "sponsor"));
			req.setAttribute("fundingSource", ParamUtil.getString(req, "fundingSource"));
			req.setAttribute("label", ParamUtil.getString(req, "label"));
			req.setAttribute("stageGate", ParamUtil.getString(req, "stageGate"));
			
			if (ValidateUtil.isNotNull(milestones)) {
				req.setAttribute("milestones", milestones);
				req.setAttribute("milestoneDateSince", ParamUtil.getDate(req, "milestoneDateSince", getDateFormat(req), null));
				req.setAttribute("milestoneDateUntil", ParamUtil.getDate(req, "milestoneDateUntil", getDateFormat(req), null));
				
				if (ValidateUtil.isNotNull(ParamUtil.getString(req, "milestonePending"))) {
					req.setAttribute("milestonePending", getResourceBundle(req).getString(MilestonePending.valueOf(ParamUtil.getString(req, "milestonePending")).getText()));
				}
				
				if (milestonetype != null && ValidateUtil.isNotNull(milestonetype.getName())) {
					req.setAttribute(Milestones.MILESTONETYPE, milestonetype.getName());
				}
				
				if (milestonecategory != null && ValidateUtil.isNotNull(milestonecategory.getName())) {
					req.setAttribute(Milestones.MILESTONECATEGORY, milestonecategory.getName());
				}
			}

			if (ValidateUtil.isNotNull(projectkpis)) {
				req.setAttribute("kpis", projectkpis);
			}
			
			forward("/index.jsp?nextForm=project/follow/colective_follow", req, resp);
		}	
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

	/**
	 * follow-up report of a project
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
     */
	private void reportProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer idProject ;
		Project project						= null;
		List<Milestones> milestones			= null;
		List<Projectactivity> activities	= null;
		List<Checklist> checklists			= null;
		List<Riskregister> risks			= null;
		List<Projectkpi> kpis				= null;
		List<Changecontrol> changes			= null;
		List<Issuelog> issues				= null;
		String documentStorage				= null;
		List<Documentproject> docsRepository	= new ArrayList<Documentproject>();
		List<Documentproject> docInv			= new ArrayList<Documentproject>();
		List<Documentproject> docInit			= new ArrayList<Documentproject>();
		List<Documentproject> docRisk			= new ArrayList<Documentproject>();
		List<Documentproject> docPlan			= new ArrayList<Documentproject>();
		List<Documentproject> docControl		= new ArrayList<Documentproject>();
		List<Documentproject> docProcurement	= new ArrayList<Documentproject>();
		List<Documentproject> docClosure		= new ArrayList<Documentproject>();
		
		idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		
		try {
			
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));
			DocumentprojectLogic documentprojectLogic 	= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
			ProjectActivityLogic activityLogic			= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			RiskRegisterLogic riskRegLogic 				= new RiskRegisterLogic();
			ProjectKpiLogic projectKpiLogic				= new ProjectKpiLogic();
			ChangeControlLogic changeControlLogic		= new ChangeControlLogic();
			IssuelogLogic issuelogLogic					= new IssuelogLogic();
			ProjectlabelLogic projectlabelLogic			= new ProjectlabelLogic();
			ChecklistLogic checklistLogic				= new ChecklistLogic();
			MilestoneLogic milestoneLogic				= new MilestoneLogic();
			
			// Project
			List<String> joins = new ArrayList<String>();
			joins.add(Project.PROGRAM);
			joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
			joins.add(Project.STAKEHOLDERS);
			joins.add(Project.STAKEHOLDERS+"."+Employee.CONTACT);
			joins.add(Project.INCOMESES);
			
			project 		= projectLogic.consProject(new Project(idProject), joins);
			
			// Milestones
			joins = new ArrayList<String>();
			joins.add(Milestones.MILESTONETYPE);
			joins.add(Milestones.PROJECTACTIVITY);
			
			milestones			= milestoneLogic.findByRelation(Projectkpi.PROJECT, project, Milestones.ESTIMATEDDATE, Constants.ASCENDENT, joins);
						
			// KPIs
			joins = new ArrayList<String>();
			joins.add(Projectkpi.METRICKPI);
			
			kpis			= projectKpiLogic.findByRelation(Projectkpi.PROJECT, project, joins);

            // Sort by name
            Collections.sort(kpis, new EntityComparator<Projectkpi>(new Order(Projectkpi.METRICK_NAME, Order.ASC)));

			// Activities
			joins = new ArrayList<String>();
			joins.add(Projectactivity.WBSNODE);
			
			activities		= activityLogic.findByRelation(Projectactivity.PROJECT, project, Projectactivity.ACTIVITYNAME, Constants.ASCENDENT, joins);
			
			// Deliverables
			checklists		= checklistLogic.findByProject(project);
			
			// Risks
			joins = new ArrayList<String>();
			joins.add(Riskregister.RISKCATEGORIES);
			
			risks 			= riskRegLogic.findByRelation(Riskregister.PROJECT, project, Riskregister.DATERAISED, Constants.DESCENDENT, joins);
			
			// Changes
			changes			= changeControlLogic.consChangesControl(project);
			
			// Issues
			issues 			= issuelogLogic.findByRelation(Issuelog.PROJECT, project, Issuelog.DATELOGGED, Constants.DESCENDENT);
			
			// Find labels of project
			joins = new ArrayList<String>();
			joins.add(Projectlabel.LABEL);
			req.setAttribute("projectLabels", projectlabelLogic.findByRelation(Projectlabel.PROJECT, new Project(idProject), joins));
			
			// Documents
			documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);
			
			// Get documents to repository
			//
			docsRepository = documentprojectLogic.getDocuments(getSettings(req), project, null);
			
			if (ValidateUtil.isNotNull(docsRepository)) {
				
				// Order
				for (Documentproject doc : docsRepository) {
					
					if (Constants.DOCUMENT_INVESTMENT.equals(doc.getType())) {
						docInv.add(doc);
					}
					else if (Constants.DOCUMENT_INITIATING.equals(doc.getType())) {
						docInit.add(doc);
					}
					else if (Constants.DOCUMENT_PLANNING.equals(doc.getType())) {
						docPlan.add(doc);
					}
					else if (Constants.DOCUMENT_RISK.equals(doc.getType())) {
						docRisk.add(doc);
					}
					else if (Constants.DOCUMENT_CONTROL.equals(doc.getType())) {
						docControl.add(doc);
					}
					else if (Constants.DOCUMENT_PROCUREMENT.equals(doc.getType())) {
						docProcurement.add(doc);
					}
					else if (Constants.DOCUMENT_CLOSURE.equals(doc.getType())) {
						docClosure.add(doc);
					}
				}
				
				docsRepository = new ArrayList<Documentproject>();
				
				docsRepository.addAll(docInv);
				docsRepository.addAll(docInit);
				docsRepository.addAll(docPlan);
				docsRepository.addAll(docRisk);
				docsRepository.addAll(docControl);
				docsRepository.addAll(docProcurement);
				docsRepository.addAll(docClosure);
			}
			
			req.setAttribute("docsRepository", docsRepository);	
			
	    }
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("milestones", milestones);
		req.setAttribute("project", project);
		req.setAttribute("activities", activities);
		req.setAttribute("checklists", checklists);
		req.setAttribute("kpis", kpis);
		req.setAttribute("risks", risks);
		req.setAttribute("changes", changes);
		req.setAttribute("issues", issues);
		req.setAttribute("documentStorage", documentStorage);
		
		forward("/index.jsp?nextForm=project/follow/individual_follow", req, resp);
	}
}