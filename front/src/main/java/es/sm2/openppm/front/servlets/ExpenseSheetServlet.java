
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
 * File: ExpenseSheetServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.ExpenseaccountsLogic;
import es.sm2.openppm.core.logic.impl.ExpensesheetLogic;
import es.sm2.openppm.core.logic.impl.ExpensesheetcommentLogic;
import es.sm2.openppm.core.logic.impl.OperationLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Expenseaccounts;
import es.sm2.openppm.core.model.impl.Expenses;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Expensesheetcomment;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;

/**
 * Servlet implementation class TimeSheetServlet
 */
public class ExpenseSheetServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER		= Logger.getLogger(ExpenseSheetServlet.class);
	public final static String REFERENCE	= "expensesheet";
	
	/***************** Actions ****************/
	public final static String VIEW_EXPENSE_SHEET		= "view-expensesheet";
	public final static String CHANGE_MONTH				= "change-month-sheet";
	public final static String ADD_EXPENSE				= "add-expensesheet";
	public final static String SAVE_ALL_EXPENSESHEET	= "save-all-expensesheet";
	public final static String APPROVE_ALL_EXPENSESHEET	= "approve-all-expensesheet";
	public final static String REJECT_ALL_EXPENSESHEET	= "reject-all-expensesheet";
	public final static String DELETE_ALL_EXPENSESHEET	= "delete-all-expensesheet";
	public final static String APPROVE_SEL_EXPENSESHEET	= "approve-sel-expensesheet";
	public final static String REJECT_SEL_EXPENSESHEET	= "reject-sel-expensesheet";
	
	/************** Actions AJAX **************/
	public final static String JX_SAVE_EXPENSESHEET		= "ajax-save-expensesheet";
	public final static String JX_APPROVE_EXPENSESHEET		= "ajax-approve-expensesheet";
	public final static String JX_DELETE_EXPENSESHEET		= "ajax-delete-expensesheet";
	public final static String JX_VIEW_COMMENTS			= "ajax-view-comments-expensesheet";
    
	
	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);
    	
		String accion = ParamUtil.getString(req, "accion");
		
		LOGGER.debug("Accion: " + accion);
		
		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, VIEW_EXPENSE_SHEET))) {

			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion) || VIEW_EXPENSE_SHEET.equals(accion)) { viewExpenseSheet(req, resp, null); }
			else if (CHANGE_MONTH.equals(accion)){ changeMonth(req, resp); }
			else if (ADD_EXPENSE.equals(accion)) { addExpenseSheet(req, resp); }
			else if (SAVE_ALL_EXPENSESHEET.equals(accion)) { updateAllExpenseSheet(req, resp, false); }
			else if (APPROVE_ALL_EXPENSESHEET.equals(accion)) {
				if (SecurityUtil.isUserInRole(req, Constants.ROLE_RESOURCE)) { updateAllExpenseSheet(req, resp, true); }
				else { changeALLExpenseSheet(req, resp, true); }
				
			}
			else if (REJECT_ALL_EXPENSESHEET.equals(accion)) { changeALLExpenseSheet(req, resp, false); }
			else if (DELETE_ALL_EXPENSESHEET.equals(accion)) { deleteAllExpenseSheet(req, resp); }
			else if (APPROVE_SEL_EXPENSESHEET.equals(accion)) { changeSellExpenseSheet(req, resp, true); }
			else if (REJECT_SEL_EXPENSESHEET.equals(accion)) { changeSellExpenseSheet(req, resp, false); }
			
			/************** Actions AJAX **************/
			else if (JX_SAVE_EXPENSESHEET.equals(accion)) { updateExpenseSheetJX(req, resp, false); }
			else if (JX_APPROVE_EXPENSESHEET.equals(accion)) { updateExpenseSheetJX(req, resp, true); }
			else if (JX_DELETE_EXPENSESHEET.equals(accion)) { deleteExpenseSheetJX(req, resp); }
			else if (JX_VIEW_COMMENTS.equals(accion)) { viewCommentsJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Change selected Expense Sheets
     * @param req
     * @param resp
     * @param approve
     * @throws IOException 
     * @throws ServletException 
     */
    private void changeSellExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws ServletException, IOException {
		
    	Date sheetDate = ParamUtil.getDate(req, "sheetDate", getDateFormat(req), DateUtil.getCalendar().getTime());
		
    	String idProjectsStr	= ParamUtil.getString(req, "idProjects",null);
    	String idEmployeesStr	= ParamUtil.getString(req, "idEmployees",null);
    	String comments			= ParamUtil.getString(req, "comments");
    	
    	Integer[] idProjects	= StringUtil.splitStrToIntegers(idProjectsStr, null);
    	Integer[] idEmployees	= StringUtil.splitStrToIntegers(idEmployeesStr, null);
    	
    	try {
			
    		if (idEmployees != null && idEmployees.length > 0) {
    			
    			ExpensesheetLogic expensesheetLogic		= new ExpensesheetLogic();
    			ExpensesheetcommentLogic commentLogic	= new ExpensesheetcommentLogic();
    			
    			int approvalRol = SettingUtil.getApprovalRol(req);
    			
    			Employee filterUser = (SecurityUtil.isUserInRole(req, approvalRol)?getUser(req):null);
    			
    			// Status for rol
	    		String nextStatus	  = null;
	    		
	    		if (approve) { nextStatus = Constants.EXPENSE_STATUS_APP3; }
	    		else { nextStatus = Constants.EXPENSE_STATUS_APP0; }
    			
	    		String lastLevel = (approve?Constants.EXPENSE_STATUS_APP2: Constants.EXPENSE_STATUS_APP3);
	    		
    			int i = 0;
    			for (int idEmployee : idEmployees) {
    				
    				Project proj = (SecurityUtil.isUserInRole(req, Constants.ROLE_PM)?new Project(idProjects[i]):null);
    	    		
    	    		// Get Expense Sheet for change
    				List<Expensesheet> expenseSheets = expensesheetLogic.findByResource(new Employee(idEmployee), sheetDate, null,
    						proj, Constants.EXPENSE_STATUS_APP2, lastLevel, filterUser);
    				
    				expenseSheets.addAll(expensesheetLogic.findByResource(new Employee(idEmployee), sheetDate, null,
    						null, Constants.EXPENSE_STATUS_APP2, lastLevel, null));
    				
    				// Change time Sheets
    				for (Expensesheet expenseSheet : expenseSheets) {
    					
    					Expensesheetcomment expenseSheetComment = new Expensesheetcomment();
    					expenseSheetComment.setContentComment(comments);
    					expenseSheetComment.setActualStatus(nextStatus);
    					expenseSheetComment.setPreviousStatus(expenseSheet.getStatus());
    					expenseSheetComment.setExpensesheet(expenseSheet);
    					expenseSheetComment.setCommentDate(new Date());
    					commentLogic.save(expenseSheetComment);
    					
    					expenseSheet.setStatus(nextStatus);
	    	    			
    					expensesheetLogic.save(expenseSheet);
    				}
    				
    				i++;
    			}
    		}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewExpenseSheet(req, resp, sheetDate);
	}

	/**
     * Change All Expense Sheet
     * @param req
     * @param resp
     * @param approve
     * @throws IOException 
     * @throws ServletException 
     */
    private void changeALLExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws ServletException, IOException {
    	
    	Integer[] ids	= ParamUtil.getIntegerList(req, "idExpenseSheet");
		String comments	= ParamUtil.getString(req, "comments");
		
		try {
			
			if (ids != null) {
    			
    			ExpensesheetLogic expensesheetLogic		= new ExpensesheetLogic();
    			ExpensesheetcommentLogic commentLogic	= new ExpensesheetcommentLogic();
    			
	    		String nextStatus	  = null;
	    		
	    		if (approve) { nextStatus = Constants.EXPENSE_STATUS_APP3; }
	    		else { nextStatus = Constants.EXPENSE_STATUS_APP0; }
	    		
    			for (int idExpenseSheet : ids) {
    				
    				Expensesheet expense = expensesheetLogic.findById(idExpenseSheet);
    				
					Expensesheetcomment expenseSheetComment = new Expensesheetcomment();
					expenseSheetComment.setContentComment(comments);
					expenseSheetComment.setActualStatus(nextStatus);
					expenseSheetComment.setPreviousStatus(expense.getStatus());
					expenseSheetComment.setExpensesheet(expense);
					expenseSheetComment.setCommentDate(new Date());
					commentLogic.save(expenseSheetComment);
    				
					expense.setStatus(nextStatus);
					
    				expensesheetLogic.save(expense);
    			}
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewExpenseSheet(req, resp, null);
	}

	/**
     * Update Expense Sheet
     * @param req
     * @param resp
     * @param approve
     * @throws IOException 
     */
    private void updateExpenseSheetJX(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws IOException {
		
    	Integer idExpenseSheet		= ParamUtil.getInteger(req, "idExpenseSheet");
    	Date expenseDate			= ParamUtil.getDate(req, "expenseDate", getDateFormat(req), new Date());
    	int idExpenseAccount		= ParamUtil.getInteger(req, "idExpenseAccount", -1);
    	int idExpense				= ParamUtil.getInteger(req, "idExpense", -1);
    	boolean reimbursable		= ParamUtil.getBoolean(req, "reimbursable");
    	boolean paidEmployee		= ParamUtil.getBoolean(req, "paidEmployee");
    	String autorizationNumber	= ParamUtil.getString(req, "autorizationNumber");
    	String description			= ParamUtil.getString(req, "description");
    	double cost					= ParamUtil.getCurrency(req, "cost", 0);
    	String comments				= ParamUtil.getString(req, "comments");
    	
		PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		ExpensesheetLogic expensesheetLogic		= new ExpensesheetLogic();
    		
			Expensesheet expense = expensesheetLogic.findById(idExpenseSheet);
			
			expense.setExpenseDate(expenseDate);
			expense.setReimbursable(reimbursable);
			expense.setPaidEmployee(paidEmployee);
			expense.setAutorizationNumber(autorizationNumber);
			expense.setDescription(description);
			expense.setCost(cost);
			
			if (idExpenseAccount != -1) { expense.setExpenseaccounts(new Expenseaccounts(idExpenseAccount)); }
			else if (idExpense != -1) { expense.setExpenses(new Expenses(idExpense));  }
			
			JSONObject returnJSON = null;
			
			if (approve) {
				ExpensesheetcommentLogic commentLogic	= new ExpensesheetcommentLogic();
				
				Expensesheetcomment expenseSheetComment = new Expensesheetcomment();
				expenseSheetComment.setContentComment(comments);
				expenseSheetComment.setActualStatus(Constants.TIMESTATUS_APP2);
				expenseSheetComment.setPreviousStatus(expense.getStatus());
				expenseSheetComment.setExpensesheet(expense);
				expenseSheetComment.setCommentDate(new Date());
				commentLogic.save(expenseSheetComment);
				
				expense.setStatus(Constants.EXPENSE_STATUS_APP2);
				
				returnJSON = info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.status", returnJSON, "expense", "applevel.app2");
			}
			else { returnJSON = infoUpdated(getResourceBundle(req), returnJSON, "expense"); }
			
			expensesheetLogic.save(expense);
    		
    		out.print(returnJSON);
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Delete Expense Sheet
     * @param req
     * @param resp
     * @throws IOException 
     */
	private void deleteExpenseSheetJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		Integer idExpenseSheet	= ParamUtil.getInteger(req, "idExpenseSheet");
		
		PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		ExpensesheetLogic expensesheetLogic = new ExpensesheetLogic();
			
			Expensesheet expense = expensesheetLogic.findById(idExpenseSheet);
			expensesheetLogic.delete(expense);
    		
    		out.print(infoDeleted(getResourceBundle(req), "expense"));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * View Comments
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void viewCommentsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int idExpenseSheet = ParamUtil.getInteger(req, "idExpenseSheet",-1);
    	
		PrintWriter out = resp.getWriter();
    	
    	try {
    		
    		ExpensesheetcommentLogic commentLogic	= new ExpensesheetcommentLogic();
    		
    		List<Expensesheetcomment> comments = commentLogic.findByRelation(Expensesheetcomment.EXPENSESHEET, new Expensesheet(idExpenseSheet));
    		
    		out.print(JsonUtil.toJSON(comments, Constants.TIME_PATTERN, new Exclusion(Expensesheet.class)));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Delete all expense sheet
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void deleteAllExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	Integer[] ids	= ParamUtil.getIntegerList(req, "idExpenseSheet");
    	
		try {
			
			if (ids != null) {
    			
    			ExpensesheetLogic expensesheetLogic = new ExpensesheetLogic();
    			
    			for (int idExpenseSheet : ids) {
    				
    				Expensesheet expense = expensesheetLogic.findById(idExpenseSheet);
    				if (Constants.EXPENSE_STATUS_APP0.equals(expense.getStatus())) {
    					expensesheetLogic.delete(expense);
    				}
    			}
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewExpenseSheet(req, resp, null);
	}

    /**
     * Update all expense sheet
     * @param req
     * @param resp
     * @param approve
     * @throws IOException 
     * @throws ServletException 
     */
	private void updateAllExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp, boolean approve) throws ServletException, IOException {
		
		Integer[] ids	= ParamUtil.getIntegerList(req, "idExpenseSheet");
		String comments	= ParamUtil.getString(req, "comments");
		
		try {
			
			if (ids != null) {
    			
    			ExpensesheetLogic expensesheetLogic		= new ExpensesheetLogic();
    			ExpensesheetcommentLogic commentLogic	= new ExpensesheetcommentLogic();
    			
    			for (int idExpenseSheet : ids) {
    				
    				Expensesheet expense = expensesheetLogic.findById(idExpenseSheet);
    				
    				if (Constants.EXPENSE_STATUS_APP0.equals(expense.getStatus())) {
    					
	    				Date expenseDate			= ParamUtil.getDate(req, "expenseDate_"+idExpenseSheet, getDateFormat(req), new Date());
	    				int idExpenseAccount		= ParamUtil.getInteger(req, "expensesType_"+idExpenseSheet, -1);
	    				int idExpense				= ParamUtil.getInteger(req, "idExpense_"+idExpenseSheet, -1);
	    				boolean reimbursable		= ParamUtil.getBoolean(req, "reimbursable_"+idExpenseSheet);
	    				boolean paidEmployee		= ParamUtil.getBoolean(req, "paidEmployee_"+idExpenseSheet);
	    				String autorizationNumber	= ParamUtil.getString(req, "autorizationNumber_"+idExpenseSheet);
	    				String description			= ParamUtil.getString(req, "description_"+idExpenseSheet);
	    				double cost					= ParamUtil.getCurrency(req, "cost_"+idExpenseSheet, 0);
	    				
	    				expense.setExpenseDate(expenseDate);
	    				expense.setReimbursable(reimbursable);
	    				expense.setPaidEmployee(paidEmployee);
	    				expense.setAutorizationNumber(autorizationNumber);
	    				expense.setDescription(description);
	    				expense.setCost(cost);
	    				
	    				if (idExpenseAccount != -1) { expense.setExpenseaccounts(new Expenseaccounts(idExpenseAccount)); }
	    				else if (idExpense != -1) { expense.setExpenses(new Expenses(idExpense));  }
	    				
	    				if (approve) {
	    					expense.setStatus(Constants.EXPENSE_STATUS_APP2);
	    					
	    					Expensesheetcomment expenseSheetComment = new Expensesheetcomment();
	    					expenseSheetComment.setContentComment(comments);
	    					expenseSheetComment.setActualStatus(Constants.TIMESTATUS_APP2);
	    					expenseSheetComment.setPreviousStatus(expense.getStatus());
	    					expenseSheetComment.setExpensesheet(expense);
	    					expenseSheetComment.setCommentDate(new Date());
	    					commentLogic.save(expenseSheetComment);
	    				}
	    				
	    				expensesheetLogic.save(expense);
    				}
    			}
			}
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewExpenseSheet(req, resp, null);
	}

	/**
     * Add Expense Sheet
     * @param req
     * @param resp
     * @throws IOException 
     * @throws ServletException 
     */
    private void addExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
    	Date sheetDate 	= ParamUtil.getDate(req, "sheetDate", getDateFormat(req), DateUtil.getCalendar().getTime());
    	
    	int idOperation = ParamUtil.getInteger(req, "idOperation", -1);
    	int idProject	= ParamUtil.getInteger(req, "idProject", -1);
    	
		try {
			
			ExpensesheetLogic expensesheetLogic = new ExpensesheetLogic();
			
			Expensesheet expense = new Expensesheet();
			expense.setEmployee(getUser(req));
			expense.setExpenseDate(sheetDate);
			expense.setStatus(Constants.EXPENSE_STATUS_APP0);
			
			if (idOperation != -1) { expense.setOperation(new Operation(idOperation)); }
			else { expense.setProject(new Project(idProject));  }
			
			expensesheetLogic.save(expense);
			
			infoCreated(req, "expense");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		viewExpenseSheet(req, resp, sheetDate);
	}

	/**
	 * Goto to the previous month
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void changeMonth(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Date sheetDate = ParamUtil.getDate(req, "sheetDate", getDateFormat(req));
		viewExpenseSheet(req, resp, sheetDate);
	}

	/**
	 * View Sheet
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void viewExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp, Date sheetDate) throws ServletException, IOException {
		
		if (sheetDate == null) { 
			sheetDate = ParamUtil.getDate(req, "sheetDate", getDateFormat(req), DateUtil.getCalendar().getTime()); 
		}
		
		Calendar cal = DateUtil.getCalendar();
		cal.setTime(sheetDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_RESOURCE)) { viewExpenseSheetEmployee(req, resp, cal.getTime()); }
		else if (SecurityUtil.isUserInRole(req, SettingUtil.getInteger(getSettings(req), Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Constants.ROLE_PMO+StringPool.BLANK))) {
			approveExpenseSheet(req, resp, cal.getTime());
		}
	}

	/**
	 * Approve Expense Sheet
	 * @param req
	 * @param resp
	 * @param sheetDate
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void approveExpenseSheet(HttpServletRequest req,
			HttpServletResponse resp, Date sheetDate) throws ServletException, IOException {
		
		Integer idEmployee	= ParamUtil.getInteger(req, "idEmployee", null);
    	
    	List<Employee> approvals	= null;
		List<Expensesheet> expenses = null;
		Employee employee			= null;
		List<Date> listDates		= new ArrayList<Date>();
		
		try {
			
			EmployeeLogic employeeLogic = new EmployeeLogic();
			approvals = employeeLogic.findApprovalsExpense(
					getUser(req),
					DateUtil.getFirstMonthDay(sheetDate),
					DateUtil.getLastMonthDay(sheetDate));
			
			if (idEmployee != null) {
				
				ExpensesheetLogic expensesheetLogic = new ExpensesheetLogic();
    			
    			List<String> joins = new ArrayList<String>();
    			joins.add(Expensesheet.PROJECT);
    			joins.add(Expensesheet.OPERATION);
    			joins.add(Expensesheet.EXPENSEACCOUNTS);
    			joins.add(Expensesheet.EXPENSES);
    			joins.add(Expensesheet.EXPENSES+"."+Expenses.BUDGETACCOUNTS);
    			
    			expenses = expensesheetLogic.findByResource(
    					new Employee(idEmployee),
    					sheetDate, joins,
    					null, Constants.TIMESTATUS_APP2, Constants.TIMESTATUS_APP3, getUser(req));
    			
    			joins = new ArrayList<String>();
    			joins.add(Employee.CONTACT);
    			employee = employeeLogic.findById(idEmployee, joins);
			}
			
			Calendar tempCal 	= DateUtil.getCalendar();
			Calendar actualCal 	= DateUtil.getCalendar();
			
			tempCal.setTime(sheetDate);
			
			if(tempCal.after(actualCal)){
				actualCal.setTime(sheetDate);
				actualCal.add(Calendar.MONTH, 1);
			}
			
			tempCal.add(Calendar.MONTH, -12);
			actualCal.add(Calendar.MONTH, 1);
			
			while(actualCal.after(tempCal)){
				listDates.add(tempCal.getTime());
				tempCal.add(Calendar.MONTH, 1);
			}

		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("approvals", approvals);
		req.setAttribute("expenses", expenses);
		req.setAttribute("sheetDate", sheetDate);
		req.setAttribute("employee", employee);
		req.setAttribute("listDates", listDates);
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.expense_approvals"));
		forward("/index.jsp?nextForm=expensesheet/approve_expensesheet", req, resp);
	}

	/**
	 * View the JSP for employee
	 * @param req
	 * @param resp
	 * @param sheetDate
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewExpenseSheetEmployee(HttpServletRequest req, HttpServletResponse resp, Date sheetDate)
		throws ServletException, IOException {
		
		List<Project> projects					= null;
		List<Operation> operations				= null;
		List<Expenseaccounts> expenseAccounts	= null;
		List<Expensesheet> expenseSheets		= null;
		List<Date> listDates					= new ArrayList<Date>();
		
		try {
			
			OperationLogic operationLogic				= new OperationLogic();
			ExpenseaccountsLogic expenseaccountsLogic	= new ExpenseaccountsLogic();
			ExpensesheetLogic expensesheetLogic			= new ExpensesheetLogic();
			ProjectLogic projectLogic					= new ProjectLogic(getSettings(req), getResourceBundle(req));

			List<String> joins = new ArrayList<String>();
			joins.add(Expensesheet.EXPENSEACCOUNTS);
			joins.add(Expensesheet.PROJECT);
			joins.add(Expensesheet.PROJECT+"."+Project.PROJECTCOSTSES);
			joins.add(Expensesheet.PROJECT+"."+Project.PROJECTCOSTSES+"."+Projectcosts.EXPENSESES);
			joins.add(Expensesheet.PROJECT+"."+Project.PROJECTCOSTSES+"."+Projectcosts.EXPENSESES+"."+Expenses.BUDGETACCOUNTS);
			joins.add(Expensesheet.OPERATION);
			joins.add(Expensesheet.EXPENSES);
			joins.add(Expensesheet.EXPENSES+"."+Expenses.BUDGETACCOUNTS);
			
			expenseSheets	= expensesheetLogic.findByResource(getUser(req), sheetDate, joins);
			expenseAccounts	= expenseaccountsLogic.findByRelation(Expenseaccounts.COMPANY, getCompany(req));
            operations      = operationLogic.findByCompanyAndUser(getCompany(req), getUser(req));
			projects		= projectLogic.findByResourceInProject(getUser(req), sheetDate);

			Calendar tempCal 	= DateUtil.getCalendar();
			Calendar actualCal 	= DateUtil.getCalendar();
			
			tempCal.setTime(sheetDate);
			
			if(tempCal.after(actualCal)){
				actualCal.setTime(sheetDate);
				actualCal.add(Calendar.MONTH, 1);
			}
			
			tempCal.add(Calendar.MONTH, -12);
			actualCal.add(Calendar.MONTH, 1);
			
			while(actualCal.after(tempCal)){
				listDates.add(tempCal.getTime());
				tempCal.add(Calendar.MONTH, 1);
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		req.setAttribute("sheetDate", sheetDate);
		req.setAttribute("projects", projects);
		req.setAttribute("operations", operations);
		req.setAttribute("expenseAccounts", expenseAccounts);
		req.setAttribute("expenseSheets", expenseSheets);
		req.setAttribute("listDates", listDates);		
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));
		req.setAttribute("title", getResourceBundle(req).getString("menu.expenses_sheet"));
		forward("/index.jsp?nextForm=expensesheet/expensesheet", req, resp);
	}
}