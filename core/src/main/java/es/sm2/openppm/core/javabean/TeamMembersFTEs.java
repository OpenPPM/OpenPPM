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
 * File: TeamMembersFTEs.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.util.List;

import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class TeamMembersFTEs {
	
	private Teammember member;
	private Project project;
	private double hours;
	private String projectNames;
	private String title;
	
	private int[] ftes;
	private double[] listHours;
	
	private Operation operation;
	
	public TeamMembersFTEs(Teammember member, Project project){
		this.member = member;
		this.project = project;
	}
	
	public TeamMembersFTEs(Teammember member){
		this.member = member;
	}
	
	public TeamMembersFTEs(){
		
	}
	
	/**
	 * @return the hours
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(double hours) {
		this.hours = hours;
	}
	
	/**
	 * @return the member
	 */
	public Teammember getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(Teammember member) {
		this.member = member;
	}

	/**
	 * @return the ftes
	 */
	public int[] getFtes() {
		return ftes;
	}

	/**
	 * @param ftes the ftes to set
	 */
	public void setFtes(int[]ftes) {
		this.ftes = ftes;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}
	
	/**
	 * @return project names
	 */
	public String getProjectNames() {
		return projectNames;
	}

	/**
	 * @param projectNames
	 */
	public void setProjectNames(List<Project> projects) {
		
		String projectNames = "";
		
		for (Project project : projects) {
			
			if (ValidateUtil.isNull(projectNames)) { projectNames += project.getProjectName(); }
			else { projectNames += "<br>"+project.getProjectName(); }
		}
		this.projectNames = projectNames;
	}

	public void setListHours(double[] listHours) {
		this.listHours = listHours;
	}

	public double[] getListHours() {
		return listHours;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
