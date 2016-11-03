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
 * File: ProjectProcurementServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings.SettingType;
import es.sm2.openppm.core.javabean.ProcurementBudget;
import es.sm2.openppm.core.logic.impl.ActivitysellerLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProcurementpaymentsLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class ProjectServer
 */
public class ProjectProcurementServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER = Logger.getLogger(ProjectProcurementServlet.class);	
	
	public final static String REFERENCE = "projectprocurement";
	
	/***************** Actions ****************/
	public final static String SAVE_WORK						= "save-work";
	public final static String SAVE_SELLER						= "save-seller";
	public final static String SAVE_PAYMENT						= "save-payment";
	public final static String DELETE_SELLER					= "delete-seller";
	public final static String DELETE_PAYMENT					= "delete-payment";
	public final static String UPDATE_TYPE_ACTIVITIES_SELLER	= "update-type-activities-seller";
	public final static String SAVE_SOW							= "save-sow";
	/************** Actions AJAX **************/	
	public final static String JX_CONS_SELLER_ACTIVITIES		= "ajax-cons-seller-act";
	public final static String JX_SAVE_SOW						= "ajax-save-sow";	
	public final static String JX_SAME_TYPE_OF_PROVIDER			= "ajax-same-type-provider";	
    
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");		
		
		if (SecurityUtil.consUserRole(req) != -1) {
			
			LOGGER.debug("Accion: " + accion);
			
			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion)){ viewPMProcurementProject(req, resp); }
			else if (SAVE_WORK.equals(accion)) { saveWork(req, resp); }
			else if (SAVE_SELLER.equals(accion)) { saveSeller(req, resp); }
			else if (SAVE_PAYMENT.equals(accion)) { savePayment(req, resp); }
			else if (DELETE_SELLER.equals(accion)) { deleteSeller(req, resp); }
			else if (DELETE_PAYMENT.equals(accion)) { deletePayment(req, resp); }
			else if (UPDATE_TYPE_ACTIVITIES_SELLER.equals(accion)) { updateTypeActivitiesSeller(req, resp); }
			else if (SAVE_SOW.equals(accion)) { saveSow(req, resp); }
			
			/************** Actions AJAX **************/
			else if (JX_CONS_SELLER_ACTIVITIES.equals(accion)) { consSellerActivitiesJX(req, resp); }
			else if (JX_SAVE_SOW.equals(accion)) { saveSowJX(req, resp); }	
			else if (JX_SAME_TYPE_OF_PROVIDER.equals(accion)) { sameTypeOfProviderJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

	/**
	 * Save sow
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void saveSow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Request
		Integer idProject			= ParamUtil.getInteger(req, "id", -1);
		Integer idActivitySeller	= ParamUtil.getInteger(req, "idSow", -1);
		Integer idSeller 			= ParamUtil.getInteger(req, Activityseller.SELLER, -1);
		Integer idActivity 			= ParamUtil.getInteger(req, Activityseller.PROJECTACTIVITY, -1);
		String sow 					= ParamUtil.getString(req, Activityseller.STATEMENTOFWORK, StringPool.BLANK);
		String documents			= ParamUtil.getString(req, Activityseller.PROCUREMENTDOCUMENTS, StringPool.BLANK);
		Integer idProjectAssociate	= ParamUtil.getInteger(req, "projectAssociate", -1);
		Boolean indirect			= ParamUtil.getBoolean	(req, "indirect", false);
		Boolean updateAll			= ParamUtil.getBoolean	(req, "updateAll", false);
		
		try {
			Activityseller activitysellerOld = new Activityseller();
			
			
			// Exceptions
			if (idActivity == -1 || idProject == -1 || idSeller == -1) {
				throw new Exception( getResourceBundle(req).getString("msg.error.data"));
			}
			
			// Declare logics
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic(getResourceBundle(req));
			SellerLogic sellerLogic 				= new SellerLogic();
			
			// Set data activity seller
			//
			Activityseller activityseller = new Activityseller();
			
			// Update
			if (idActivitySeller != -1) {
				activityseller.setIdActivitySeller(idActivitySeller);
				
				// Logic
				activitysellerOld = activitysellerLogic.findById(idActivitySeller); 
				//TODO no se puede meter dentro el logic saveSow porque lanza
				// a different object with the same identifier value was already associated with the session
			}
			
			Projectactivity activity = new Projectactivity(idActivity);
			activity.setProject(new Project(idProject));
			activityseller.setProjectactivity(activity);
			
			if (idProjectAssociate != -1) {
				activityseller.setProject(new Project(idProjectAssociate));
			}
			
			// Logic find data seller
			Seller seller = sellerLogic.findById(idSeller);
			
			activityseller.setSeller(seller);
			activityseller.setStatementOfWork(sow);
			activityseller.setProcurementDocuments(documents);
			activityseller.setIndirect(indirect);
				
			// Logic
			List<Info> infos = activitysellerLogic.saveSow(activityseller, activitysellerOld, updateAll);
			
			// Send messages
			createInfos(infos, req);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);
	}

	/**
	 * Check same type of provider
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void sameTypeOfProviderJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Integer idActivitySeller	= ParamUtil.getInteger(req, "idSow", -1);
		Integer idProject			= ParamUtil.getInteger(req, "idProject",-1);
		Integer idSeller 			= ParamUtil.getInteger(req, Activityseller.SELLER, -1);
		Boolean indirect			= ParamUtil.getBoolean	(req, "indirect", false);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			JSONObject infoJSON = new JSONObject();
			Boolean created = null;
			
			// Declare logics
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
			
			if (idSeller != -1 && idProject != -1) {
				
				// Created
				if (idActivitySeller == -1) {
					created = true;
				}
				// Updated
				else {
					created = false;
				}
				
				// Logic
				Boolean sameTypeOfProvider = activitysellerLogic.sameTypeOfProvider(new Seller(idSeller), new Project(idProject), indirect, created);
				
				if (sameTypeOfProvider != null) {
					
					// Response
					//
					infoJSON.put("sameTypeOfProvider", sameTypeOfProvider);
				}
				else {
					throw new Exception( getResourceBundle(req).getString("msg.error.data"));
				}
			}
			else {
				throw new Exception( getResourceBundle(req).getString("msg.error.data"));
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Update type activities seller
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void updateTypeActivitiesSeller(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			
			// Request
			Integer idSeller 	= ParamUtil.getInteger(req, Activityseller.SELLER, -1);
			Boolean indirect 	= ParamUtil.getBoolean	(req, "indirect", false);
			Integer idProject	= ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
			
			// Declare logics
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic(getResourceBundle(req));
			
			// Logics
			activitysellerLogic.updateTypeActivitiesSeller(new Seller(idSeller), indirect, new Project(idProject));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);
	}

	/**
	 * Save Sow & selection data from procurement_project.jsp
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void saveSowJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Integer id		 			= ParamUtil.getInteger(req, "idSow", -1);
		Integer idSeller 			= ParamUtil.getInteger(req, Activityseller.SELLER, -1);
		Integer idActivity 			= ParamUtil.getInteger(req, Activityseller.PROJECTACTIVITY, -1);
		String sow 					= ParamUtil.getString(req, Activityseller.STATEMENTOFWORK, StringPool.BLANK);
		String documents			= ParamUtil.getString(req, Activityseller.PROCUREMENTDOCUMENTS, StringPool.BLANK);
		Integer idProjectAssociate	= ParamUtil.getInteger(req, "projectAssociate", -1);
		Boolean indirect			= ParamUtil.getBoolean	(req, "indirect", false);
		
		PrintWriter out = resp.getWriter();
		
		try {
			
			JSONObject infoJSON 						= new JSONObject();
			
			// Declare logics
			ActivitysellerLogic activitysellerLogic		= new ActivitysellerLogic();
			SellerLogic sellerLogic 					= new SellerLogic();
			Activityseller actSeller 					= new Activityseller();
			ProjectActivityLogic projectActivityLogic 	= new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			
			if (idSeller == -1 || idActivity == -1) {
				throw new Exception( getResourceBundle(req).getString("msg.error.data"));
			}
			
			// Logic
			Projectactivity activity = projectActivityLogic.consActivity(idActivity);
			
			if (id != -1) { // Update
				
				// Logic
				actSeller = activitysellerLogic.findById(id, false);
				
				if (!actSeller.getProjectactivity().getIdActivity().equals(idActivity) ) {
					
					// Set activity
					actSeller.setProjectactivity(activity);
					
					if (idProjectAssociate != -1) {
						if (actSeller.hasAssociatedProjects()) {
							infoJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.activity_seller"));
						}
					}
				}
				else {
					
					// Set activity
					actSeller.setProjectactivity(activity);
					
					if (idProjectAssociate != -1 && actSeller.getProject() == null) {
						
						if (actSeller.hasAssociatedProjects()) {
							infoJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.activity_seller"));
						}
					}
				}
			}
			else { 		// Create
				
				// Set activity
				actSeller.setProjectactivity(activity);
				
				// Set project associate
				if (idProjectAssociate != -1) {
					
					if (actSeller.hasAssociatedProjects()) {
						infoJSON.put(StringPool.ERROR, getResourceBundle(req).getString("msg.error.activity_seller"));
					}
				}
			}
			
			// A provider may not be in the same project direct and indirect
			if (activity != null) {
				
				List<Activityseller> activitysellers = activitysellerLogic.findBySellerAndProject(new Seller(idSeller), activity.getProject());
				
				if (ValidateUtil.isNotNull(activitysellers)) {
					
					if (!activitysellers.get(0).getIndirect().equals(indirect)) {
						infoJSON.put("errorTypeSeller", getResourceBundle(req).getString("msg.error.activity_seller.type"));
					}
				}
			}
			
			// If not errors
			if (infoJSON.isEmpty()) {
				
				if (idProjectAssociate != -1) {
					actSeller.setProject(new Project(idProjectAssociate));
				}
				else {
					actSeller.setProject(null);
				}
				
				// Set data activity seller
				actSeller.setSeller(sellerLogic.findById(idSeller, false));
				actSeller.setStatementOfWork(sow);
				actSeller.setProcurementDocuments(documents);
				actSeller.setIndirect(indirect);
				
				// Logic
				activitysellerLogic.save(actSeller);
			}
			else {
				infoJSON.put("idError", "sow-errors");
			}
			
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Recover Procurement-Project info and forward to procurement_project.jsp
	 *  
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewPMProcurementProject(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Information to recover
		Project project 						= null;
		List<Seller> sellers 					= null;
		List<Seller> procurementSellers 		= null;	
		List<Activityseller> activitySellers	= null;
		List<Procurementpayments> procPayments 	= null;
		List<ProcurementBudget> procBudgets 	= null;
		List<Documentproject> docs 				= new ArrayList<Documentproject>();
		List<Performingorg> perfOrgs 			= null;
		
		Integer idProject		= ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		boolean hasPermission	= false;
		
		try {
			hasPermission = (SecurityUtil.hasPermission(req, new Project(idProject), Constants.TAB_PROCURAMENT) &&
					SecurityUtil.hasPermissionProject(req, Constants.STATUS_PLANNING, Constants.STATUS_CONTROL, Constants.STATUS_CLOSED, Constants.STATUS_ARCHIVED));
			
			if (hasPermission) {
				DocumentprojectLogic documentprojectLogic 			= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
				ActivitysellerLogic activitysellerLogic 			= new ActivitysellerLogic();
				ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
				ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
				SellerLogic sellerLogic 							= new SellerLogic();
				
				List<String> joins = new ArrayList<String>();
				joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE);
				joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+ Employee.CONTACT);
				
				project = projectLogic.consProject(new Project(idProject), joins);
				
				sellers = sellerLogic.findAllOrdered(Order.asc(Seller.NAME));
				procurementSellers = sellerLogic.findByProcurement(project);
				
				joins = new ArrayList<String>();
				joins.add(Activityseller.PROJECTACTIVITY);
				joins.add(Activityseller.SELLER);
				joins.add(Activityseller.PROJECT);
				activitySellers = activitysellerLogic.consActivitySellerByProject(joins, project);
				
				joins = new ArrayList<String>();			
				joins.add(Procurementpayments.SELLER);
				procPayments = procurementpaymentsLogic.consProcurementPaymentsByProject(project, joins);
				procBudgets = procurementpaymentsLogic.consProcurementBudgetsByProject(project, joins);
				
				joins = new ArrayList<String>();			
				joins.add(Activityseller.SELLER);
				procPayments = procurementpaymentsLogic.consProcurementPaymentsByProject(project, joins);
				procBudgets = procurementpaymentsLogic.consProcurementBudgetsByProject(project, joins);
				
				PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
				perfOrgs = performingOrgLogic.findOtherPOs(getCompany(req), project.getPerformingorg().getIdPerfOrg());

                // Restriction for dates
                //
                SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_PATTERN);

                Calendar calendar = DateUtil.getCalendar();

                calendar.add(Calendar.YEAR, -2);

                // Get documents
                //
                docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_PROCUREMENT);

                req.setAttribute("minDate", date.format(calendar.getTime()));
                req.setAttribute("docs", docs);
                req.setAttribute("documentStorage", SettingUtil.getString(getSettings(req), SettingType.DOCUMENT_STORAGE));
            }
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}
		
		if (hasPermission) {
			req.setAttribute("project", project);
			req.setAttribute("sellers", sellers);
			req.setAttribute("procurementSellers", procurementSellers);		
			req.setAttribute("activitySellers", activitySellers);
			req.setAttribute("procPayments", procPayments);
			req.setAttribute("procBudgets", procBudgets);
			req.setAttribute("perfOrgs", perfOrgs);
			
			req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ? 
					project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());
			
			req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));			
			req.setAttribute("tab", Constants.TAB_PROCURAMENT);
			forward("/index.jsp?nextForm=project/procurement/procurement_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}
	
	/**
	 * Save Work Schedule data from procurement_project.jsp
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveWork(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {

		Integer id		 	= ParamUtil.getInteger(req, "idWork", -1);
		Integer idSeller 	= ParamUtil.getInteger(req, Activityseller.SELLER, -1);		
		Date baseStart 		= ParamUtil.getDate(req, Activityseller.BASELINESTART, getDateFormat(req), null);
		Date start	 		= ParamUtil.getDate(req, Activityseller.STARTDATE, getDateFormat(req), null);
		Date baseFinish		= ParamUtil.getDate(req, Activityseller.BASELINEFINISH, getDateFormat(req), null);
		Date finish 		= ParamUtil.getDate(req, Activityseller.FINISHDATE, getDateFormat(req), null);
		String info			= ParamUtil.getString(req, Activityseller.WORKSCHEDULEINFO, StringPool.BLANK);
		
		try {
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
			
			SellerLogic sellerLogic = new SellerLogic();
			
			if (idSeller == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			
			Activityseller actSeller = new Activityseller();
			
			actSeller = activitysellerLogic.findById(id, false);
			
			actSeller.setSeller(sellerLogic.findById(idSeller, false));
			actSeller.setBaselineStart(baseStart);
			actSeller.setStartDate(start);
			actSeller.setBaselineFinish(baseFinish);
			actSeller.setFinishDate(finish);
			actSeller.setWorkScheduleInfo(info);			
			
			activitysellerLogic.save(actSeller);			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);
	}
	
	/**
	 * Save Seller Performance data from procurement_project.jsp
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveSeller(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {

		Integer id		 	= ParamUtil.getInteger(req, "idActSeller", -1);
		Integer idSeller 	= ParamUtil.getInteger(req, Activityseller.SELLER, -1);
		String info			= ParamUtil.getString(req, Activityseller.SELLERPERFORMANCEINFO, StringPool.BLANK);

		try {
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
			SellerLogic sellerLogic = new SellerLogic();
			
			if (idSeller == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			
			Activityseller actSeller = new Activityseller();
			
			actSeller = activitysellerLogic.findById(id, false);
			
			actSeller.setSeller(sellerLogic.findById(idSeller, false));			
			actSeller.setSellerPerformanceInfo(info);			
			
			activitysellerLogic.save(actSeller);			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);
	}
	
	/**
	 * Save Procurement Payment data from procurement_project.jsp
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void savePayment(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {

		Integer id		 	= ParamUtil.getInteger(req, "idPayment", -1);
		Integer idProject 	= ParamUtil.getInteger(req, "id", -1);
		Integer idSeller 	= ParamUtil.getInteger(req, Procurementpayments.SELLER, -1);		
		String order		= ParamUtil.getString(req, Procurementpayments.PURCHASEORDER, StringPool.BLANK);
		Date plannedDate	= ParamUtil.getDate(req, Procurementpayments.PLANNEDDATE, getDateFormat(req), null);
		Date actualDate		= ParamUtil.getDate(req, Procurementpayments.ACTUALDATE, getDateFormat(req), null);
		Double plannedPay 	= ParamUtil.getCurrency(req, Procurementpayments.PLANNEDPAYMENT, 0);
		Double actualPay 	= ParamUtil.getCurrency(req, Procurementpayments.ACTUALPAYMENT, 0);
		String info			= ParamUtil.getString(req, Procurementpayments.PAYMENTSCHEDULEINFO, StringPool.BLANK);
        String concept      = ParamUtil.getString(req, Procurementpayments.CONCEPT, StringPool.BLANK);
        Boolean deductible  = ParamUtil.getBoolean(req, Procurementpayments.DEDUCTIBLE);
		
		try {
			ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
			SellerLogic sellerLogic 							= new SellerLogic();
			ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));
			
			if (idSeller == -1 || idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			
			Procurementpayments payment = new Procurementpayments();
			
			if (id != -1) {
				payment = procurementpaymentsLogic.findById(id, false);
			}
			
			payment.setSeller(sellerLogic.findById(idSeller, false));
			payment.setProject(projectLogic.consProject(idProject));
			payment.setPurchaseOrder(order);
			payment.setPlannedDate(plannedDate);
			payment.setActualDate(actualDate);
			payment.setPlannedPayment(plannedPay);
			payment.setActualPayment(actualPay);
			payment.setPaymentScheduleInfo(info);
            payment.setConcept(concept);
            payment.setDeductible(deductible);
			
			procurementpaymentsLogic.save(payment);			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteSeller(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			Integer idSeller = ParamUtil.get(req, "idSeller", -1);
			
			ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
			
			if (idSeller != -1) {
				activitysellerLogic.delete(activitysellerLogic.findById(idSeller));
			}
			
			infoDeleted(req, "procurement.sow_selection.sow");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewPMProcurementProject(req, resp);			
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deletePayment(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			Integer idPayment = ParamUtil.get(req, "idPayment", -1);
			
			ProcurementpaymentsLogic procurementpaymentsLogic = new ProcurementpaymentsLogic();
			procurementpaymentsLogic.delete(procurementpaymentsLogic.findById(idPayment));
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	
		viewPMProcurementProject(req, resp);			
	}
	
	/**
	 * List not assigned activities to seller
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void consSellerActivitiesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();
		
		try {
			ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(getSettings(req), getResourceBundle(req));
			
			int idProject	= ParamUtil.getInteger(req, Project.IDPROJECT, -1);
			int idSeller	= ParamUtil.getInteger(req, Seller.IDSELLER,-1);
			
			List<Object[]> activities = projectActivityLogic.consNoAssignedActivities(new Seller(idSeller), new Project(idProject), Order.asc(Projectactivity.ACTIVITYNAME));
			
			out.print(JsonUtil.toJSON(activities));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}
}
