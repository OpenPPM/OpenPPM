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
 * File: TimesheetWrap.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.wrap;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class TimesheetWrap extends Timesheet {

	private static final long serialVersionUID = 1L;

	/**
	 * Status for day
	 */
	public static final String TYPE_READONLY			= "readonly";
	public static final String TYPE_EXCEPTIONDAY		= "exceptionDay";
	public static final String TYPE_EXCEPTIONDAYCOLOR	= "exceptionDayColor";
	public static final String TYPE_NOTWORKDAY			= "notWorkDay";
	public static final String TYPE_NONWORKINGDAY		= "nonWorkingDay";
	public static final String TYPE_NONWORKINGDAYCOLOR	= "nonWorkingDayColor";
	
	/**
	 * Permissions of time sheet
	 */
	private boolean allowReject;
	private boolean allowApprove;
	private boolean projectClosed;
	private Boolean suggestReject;
	private String  suggestRejectComment;
	
	/**
	 * Types of days
	 */
	private String typeDay1;
    private String typeDay2;
    private String typeDay3;
    private String typeDay4;
    private String typeDay5;
    private String typeDay6;
    private String typeDay7;
	
    /**
     * Copy time sheet
     * 
     * @param sheet
     */
	public TimesheetWrap(Timesheet sheet) {
		
		super();
		
		this.setIdTimeSheet(sheet.getIdTimeSheet());
		this.setOperation(sheet.getOperation());
		this.setProjectactivity(sheet.getProjectactivity());
		this.setEmployee(sheet.getEmployee());
		this.setInitDate(sheet.getInitDate());
		this.setEndDate(sheet.getEndDate());
		this.setStatus(sheet.getStatus());
		this.setHoursDay1(sheet.getHoursDay1());
		this.setHoursDay2(sheet.getHoursDay2());
		this.setHoursDay3(sheet.getHoursDay3());
		this.setHoursDay4(sheet.getHoursDay4());
		this.setHoursDay5(sheet.getHoursDay5());
		this.setHoursDay6(sheet.getHoursDay6());
		this.setHoursDay7(sheet.getHoursDay7());
		this.setTimesheetcomments(sheet.getTimesheetcomments());
		this.setSuggestReject(sheet.getSuggestReject());
		this.setSuggestRejectComment(sheet.getSuggestRejectComment());
	}

	/**
	 * Copy time sheet and additional information about project, activity project, operation
	 *
	 * @param sheet
	 * @param status
	 * @param idActivity
	 * @param activityName
	 * @param operationName
	 * @param idProject
	 * @param projectName
	 * @param chartLabel
	 * @param idProject
	 * @param chartLabel
	 */
	public TimesheetWrap(Timesheet sheet, 
			String status,
			Integer idActivity,
			String activityName,
			String operationName,
			Integer idProject,
			String projectName, 
			String chartLabel,
			String projectStatus,
			Integer idEmployeePM,
			String fullNamePM) {
			
		// Check if activity is null, other wise, is an operation
		if (ValidateUtil.isNotNull(activityName)) {
			
			// Creating new object project
			Project project = new Project();
			project.setIdProject(idProject);
			project.setProjectName(projectName);
			project.setChartLabel(chartLabel);
			project.setStatus(projectStatus);

			// Creating new Employee and Contact for PM
			Employee pm = new Employee();
			pm.setIdEmployee(idEmployeePM);
			pm.setContact(new Contact());
			pm.getContact().setFullName(fullNamePM);
			project.setEmployeeByProjectManager(pm);

			// Creating new object activity
			Projectactivity activity = new Projectactivity();
			activity.setIdActivity(idActivity);
			activity.setActivityName(activityName);
			
			// Set project to activity
			activity.setProject(project);
			this.setProjectactivity(activity);
			
		}
		else {
			
			// Creating new object operation
			Operation operation = new Operation();
			operation.setOperationName(operationName);
			this.setOperation(operation);
		}
		
		// Setting parameters
		this.setIdTimeSheet(sheet.getIdTimeSheet());
		this.setInitDate(sheet.getInitDate());
		this.setEndDate(sheet.getEndDate());
		this.setStatus(sheet.getStatus());
		this.setHoursDay1(sheet.getHoursDay1());
		this.setHoursDay2(sheet.getHoursDay2());
		this.setHoursDay3(sheet.getHoursDay3());
		this.setHoursDay4(sheet.getHoursDay4());
		this.setHoursDay5(sheet.getHoursDay5());
		this.setHoursDay6(sheet.getHoursDay6());
		this.setHoursDay7(sheet.getHoursDay7());
		this.setTimesheetcomments(sheet.getTimesheetcomments());
		this.setSuggestReject(sheet.getSuggestReject());
		this.setSuggestRejectComment(sheet.getSuggestRejectComment());
		
		if (status != null) {
			this.setProjectClosed(Constants.STATUS_CLOSED.equals(status) || !Constants.STATUS_ARCHIVED.equals(status));
			this.setAllowReject(!Constants.STATUS_CLOSED.equals(status) && !Constants.STATUS_ARCHIVED.equals(status));
			this.setAllowApprove(!Constants.STATUS_CLOSED.equals(status) && !Constants.STATUS_ARCHIVED.equals(status));
		}
	}
	/**
	 * @return the typeDay1
	 */
	public String getTypeDay1() {
		return typeDay1;
	}
	/**
	 * @param typeDay1 the typeDay1 to set
	 */
	public void setTypeDay1(String typeDay1) {
		this.typeDay1 = typeDay1;
	}
	/**
	 * @return the typeDay2
	 */
	public String getTypeDay2() {
		return typeDay2;
	}
	/**
	 * @param typeDay2 the typeDay2 to set
	 */
	public void setTypeDay2(String typeDay2) {
		this.typeDay2 = typeDay2;
	}
	/**
	 * @return the typeDay3
	 */
	public String getTypeDay3() {
		return typeDay3;
	}
	/**
	 * @param typeDay3 the typeDay3 to set
	 */
	public void setTypeDay3(String typeDay3) {
		this.typeDay3 = typeDay3;
	}
	/**
	 * @return the typeDay4
	 */
	public String getTypeDay4() {
		return typeDay4;
	}
	/**
	 * @param typeDay4 the typeDay4 to set
	 */
	public void setTypeDay4(String typeDay4) {
		this.typeDay4 = typeDay4;
	}
	/**
	 * @return the typeDay5
	 */
	public String getTypeDay5() {
		return typeDay5;
	}
	/**
	 * @param typeDay5 the typeDay5 to set
	 */
	public void setTypeDay5(String typeDay5) {
		this.typeDay5 = typeDay5;
	}
	/**
	 * @return the typeDay6
	 */
	public String getTypeDay6() {
		return typeDay6;
	}
	/**
	 * @param typeDay6 the typeDay6 to set
	 */
	public void setTypeDay6(String typeDay6) {
		this.typeDay6 = typeDay6;
	}
	/**
	 * @return the typeDay7
	 */
	public String getTypeDay7() {
		return typeDay7;
	}
	/**
	 * @param typeDay7 the typeDay7 to set
	 */
	public void setTypeDay7(String typeDay7) {
		this.typeDay7 = typeDay7;
	}
	/**
	 * @return the allowReject
	 */
	public boolean isAllowReject() {
		return allowReject;
	}
	/**
	 * @param allowReject the allowReject to set
	 */
	public void setAllowReject(boolean allowReject) {
		this.allowReject = allowReject;
	}
	/**
	 * @return the allowApprove
	 */
	public boolean isAllowApprove() {
		return allowApprove;
	}
	/**
	 * @param allowApprove the allowApprove to set
	 */
	public void setAllowApprove(boolean allowApprove) {
		this.allowApprove = allowApprove;
	}
	/**
	 * @return the projectClosed
	 */
	public boolean isProjectClosed() {
		return projectClosed;
	}
	/**
	 * @param projectClosed the projectClosed to set
	 */
	public void setProjectClosed(boolean projectClosed) {
		this.projectClosed = projectClosed;
	}
	/**
	 * @return the suggestReject
	 */
	public Boolean getSuggestReject() {
		return suggestReject;
	}
	/**
	 * @param suggestReject the suggestReject to set
	 */
	public void setSuggestReject(Boolean suggestReject) {
		this.suggestReject = suggestReject;
	}
	/**
	 * @return the suggestRejectComment
	 */
	public String getSuggestRejectComment() {
		return suggestRejectComment;
	}
    /**
     * @param suggestRejectComment
     */
	public void setSuggestRejectComment(String suggestRejectComment) {
		this.suggestRejectComment = suggestRejectComment;
	}
}
