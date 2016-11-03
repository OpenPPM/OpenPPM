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
 * File: ApprovalWrap.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.wrap;
import es.sm2.openppm.core.model.impl.Employee;

public class ApprovalWrap {
	private Employee employee;
	private boolean suggestReject;
	private String employeeName;
	private Integer idEmployee;
	private Double app0Hours;
	private Double app1Hours;
	private Double app2Hours;
	private Double app3Hours;
	private Double activityHours;
	private Double operationHours;
	private Double totalHours;
	private String projectName;
	private Integer idProject;
	private String resourcePool;
	private String seller;
	
	public ApprovalWrap() {
	}
	
	/**
	 * New instance for approval APP3
	 *
	 * @param idEmployee
	 * @param employeeName
	 * @param resourcePool
	 * @param seller
	 * @param app0Hours
	 * @param app1Hours
	 * @param app2Hours
	 * @param app3Hours
	 * @param activityHours
	 * @param operationHours
	 * @param suggestReject
	 */
	public ApprovalWrap(
			Integer idEmployee, 
			String employeeName,
			String resourcePool,
			String seller,
			Double app0Hours,
			Double app1Hours, 
			Double app2Hours, 
			Double app3Hours,
			Double activityHours,
			Double operationHours,
			Long suggestReject) {

		this.resourcePool = resourcePool;
		this.seller = seller;
		this.idEmployee = idEmployee;
		this.employeeName = employeeName;
		this.app0Hours = app0Hours;
		this.app1Hours = app1Hours;
		this.app2Hours = app2Hours;
		this.app3Hours = app3Hours;
		this.activityHours = activityHours;
		this.operationHours = operationHours;
		this.totalHours = (activityHours + operationHours);
		this.suggestReject = (suggestReject > 0);
	}

	/**
	 * New instance for approval APP2
	 *
	 * @param idEmployee
	 * @param employeeName
	 * @param idProject
	 * @param projectName
	 * @param app0Hours
	 * @param app1Hours
	 * @param app2Hours
	 * @param app3Hours
	 * @param suggestReject
	 */
	public ApprovalWrap(
			Integer idEmployee,
			String employeeName,
			Integer idProject,
			String projectName,
			Double app0Hours,
			Double app1Hours,
			Double app2Hours,
			Double app3Hours,
			Long suggestReject) {

		this.idEmployee = idEmployee;
		this.employeeName = employeeName;
		this.projectName = projectName;
		this.app0Hours = app0Hours;
		this.app1Hours = app1Hours;
		this.app2Hours = app2Hours;
		this.app3Hours = app3Hours;
		this.totalHours = (app0Hours + app1Hours + app2Hours + app3Hours);
		this.suggestReject = (suggestReject > 0);
		this.idProject = idProject;
	}

	/**
	 * New instance for Filter employees an contacts having timesheets suggest to reject.
	 *
	 * @param idEmployee
	 * @param employeeName
	 */
	public ApprovalWrap(Integer idEmployee, String employeeName) {
		
		this.idEmployee = idEmployee;
		this.employeeName = employeeName;
	}
	
	/**
	 * 
	 * @return
	 */
	public Employee getEmployee() {
		return employee;
	}
	
	/**
	 * 
	 * @param employee
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSuggestReject() {
		return suggestReject;
	}
	
	/**
	 * 
	 * @param suggestReject
	 */
	public void setSuggestReject(boolean suggestReject) {
		this.suggestReject = suggestReject;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	
	/**
	 * 
	 * @param employeeName
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getIdEmployee() {
		return idEmployee;
	}
	
	/**
	 * 
	 * @param idEmployee
	 */
	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getApp0Hours() {
		return app0Hours;
	}
	
	/**
	 * 
	 * @param app0Hours
	 */
	public void setApp0Hours(Double app0Hours) {
		this.app0Hours = app0Hours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getApp1Hours() {
		return app1Hours;
	}
	
	/**
	 * 
	 * @param app1Hours
	 */
	public void setApp1Hours(Double app1Hours) {
		this.app1Hours = app1Hours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getApp2Hours() {
		return app2Hours;
	}
	
	/**
	 * 
	 * @param app2Hours
	 */
	public void setApp2Hours(Double app2Hours) {
		this.app2Hours = app2Hours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getApp3Hours() {
		return app3Hours;
	}
	
	/**
	 * 
	 * @param app3Hours
	 */
	public void setApp3Hours(Double app3Hours) {
		this.app3Hours = app3Hours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getActivityHours() {
		return activityHours;
	}
	
	/**
	 * 
	 * @param activityHours
	 */
	public void setActivityHours(Double activityHours) {
		this.activityHours = activityHours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getOperationHours() {
		return operationHours;
	}
	
	/**
	 * 
	 * @param operationHours
	 */
	public void setOperationHours(Double operationHours) {
		this.operationHours = operationHours;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getTotalHours() {
		return totalHours;
	}
	
	/**
	 * 
	 * @param totalHours
	 */
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * 
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getIdProject() {
		return idProject;
	}
	
	/**
	 * 
	 * @param idProject
	 */
	public void setIdProject(Integer idProject) {
		this.idProject = idProject;
	}

	/**
	 * @return the resourcePool
	 */
	public String getResourcePool() {

		return resourcePool;
	}

	/**
	 * @param resourcePool The resourcePool to set
	 */
	public void setResourcePool(String resourcePool) {

		this.resourcePool = resourcePool;
	}

	/**
	 * @return the seller
	 */
	public String getSeller() {

		return seller;
	}

	/**
	 * @param seller The seller to set
	 */
	public void setSeller(String seller) {

		this.seller = seller;
	}
}
