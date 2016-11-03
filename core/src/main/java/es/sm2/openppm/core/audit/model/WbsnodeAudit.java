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
 * File: WbsnodeSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Wbsnode;

import java.util.ArrayList;
import java.util.List;

public class WbsnodeAudit {

	private Integer id;
	private Integer parent;
	private String code;
	private String name;
	private String description;
	private Boolean isControlAccount;
	private Double budget;
	private ActivityAudit activity;
	private List<ChecklistAudit> checklists = new ArrayList<ChecklistAudit>();

	public WbsnodeAudit(Wbsnode wbsnode) {

		setId(wbsnode.getIdWbsnode());
		setCode(wbsnode.getCode());
		setName(wbsnode.getName());
		setDescription(wbsnode.getDescription());
		setIsControlAccount(wbsnode.getIsControlAccount());
		setBudget(wbsnode.getBudget());

		// SET PARENT NODE
		if (wbsnode.getWbsnode() != null) {
			setParent(wbsnode.getWbsnode().getIdWbsnode());
		}

		// SET PROJECT ACTIVITY
		if (wbsnode.getProjectactivities() != null
				&& !wbsnode.getProjectactivities().isEmpty()) {

			for (Projectactivity activity : wbsnode.getProjectactivities()) {
				setActivity(new ActivityAudit(activity));
			}
		}

		// SET CHECKLIST
		if (wbsnode.getChecklists() != null
				&& !wbsnode.getChecklists().isEmpty()) {

			for (Checklist checklist : wbsnode.getChecklists()) {
				getChecklists().add(new ChecklistAudit(checklist));
			}

		}
	}

	/**
	 * @return the parent
	 */
	public Integer getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Integer parent) {
		this.parent = parent;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isControlAccount
	 */
	public Boolean getIsControlAccount() {
		return isControlAccount;
	}

	/**
	 * @param isControlAccount
	 *            the isControlAccount to set
	 */
	public void setIsControlAccount(Boolean isControlAccount) {
		this.isControlAccount = isControlAccount;
	}

	/**
	 * @return the budget
	 */
	public Double getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(Double budget) {
		this.budget = budget;
	}

	/**
	 * @return the activity
	 */
	public ActivityAudit getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(ActivityAudit activity) {
		this.activity = activity;
	}

	/**
	 * @return the checklists
	 */
	public List<ChecklistAudit> getChecklists() {
		return checklists;
	}

	/**
	 * @param checklists
	 *            the checklists to set
	 */
	public void setChecklists(List<ChecklistAudit> checklists) {
		this.checklists = checklists;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
}
