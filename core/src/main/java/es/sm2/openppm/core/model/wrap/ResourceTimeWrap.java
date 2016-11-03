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
 * File: ResourceTimeWrap.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.wrap;

import java.util.Date;

public class ResourceTimeWrap {

	private Integer idEmployee;
	private String fullName;
	private Date iniDate;
	private Date finishDate;
	private Double hours;
	private String projectName;
	private String jobCategory;
	private String operation;

	public ResourceTimeWrap (
			 Integer idEmployee,
			 String fullName,
			 Date initDate,
			 Date finishDate,
			 Double hours
			) {
		
		this.idEmployee = idEmployee;
		this.fullName 	= fullName;
		this.iniDate	= initDate;
		this.finishDate = finishDate;
		this.hours		= hours;
	}
	
	/**
	 * Constructor for capacity running resource by project
	 * 
	 * @param idEmployee
	 * @param projectName
	 * @param fullName
	 * @param initDate
	 * @param finishDate
	 * @param hours
	 */
	public ResourceTimeWrap (
			 Integer idEmployee,
			 String projectName,
			 String fullName,
			 Date initDate,
			 Date finishDate,
			 Double hours
			) {
		
		this.idEmployee 	= idEmployee;
		this.projectName 	= projectName;
		this.fullName 		= fullName;
		this.iniDate		= initDate;
		this.finishDate 	= finishDate;
		this.hours			= hours;
	}
	
	/**
	 * Constructor for capacity running resource by job category
	 * 
	 * @param jobCategory
	 * @param projectName
	 * @param initDate
	 * @param finishDate
	 * @param hours
	 */
	public ResourceTimeWrap (
			 String jobCategory,
			 String projectName,
			 Date initDate,
			 Date finishDate,
			 Double hours
			) {
		
		this.jobCategory	= jobCategory;
		this.projectName 	= projectName;
		this.iniDate		= initDate;
		this.finishDate 	= finishDate;
		this.hours			= hours;
	}
	
	/**
	 * Constructor for operations
	 * 
	 * @param operation
	 * @param initDate
	 * @param finishDate
	 * @param hours
	 */
	public ResourceTimeWrap (
			 String operation,
			 Date initDate,
			 Date finishDate,
			 Double hours
			) {
		
		this.iniDate		= initDate;
		this.finishDate 	= finishDate;
		this.hours			= hours;
		this.operation		= operation;
	}

    /**
     * Constructor for operations
     *
     * @param operation
     * @param initDate
     * @param finishDate
     * @param idEmployee
     * @param hours
     */
    public ResourceTimeWrap (
            String operation,
            Date initDate,
            Date finishDate,
            Integer idEmployee,
            Double hours
    ) {

        this.iniDate		= initDate;
        this.finishDate 	= finishDate;
        this.hours			= hours;
        this.operation		= operation;
        this.idEmployee 	= idEmployee;
    }

	public ResourceTimeWrap (
			 Integer idEmployee
			) {
		
		this.idEmployee = idEmployee;
	}

	/**
	 * @return the idEmployee
	 */
	public Integer getIdEmployee() {
		return idEmployee;
	}

	/**
	 * @param idEmployee the idEmployee to set
	 */
	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the hours
	 */
	public Double getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(Double hours) {
		this.hours = hours;
	}

	/**
	 * @return the iniDate
	 */
	public Date getIniDate() {
		return iniDate;
	}

	/**
	 * @param iniDate the iniDate to set
	 */
	public void setIniDate(Date iniDate) {
		this.iniDate = iniDate;
	}

	/**
	 * @return the finishDate
	 */
	public Date getFinishDate() {
		return finishDate;
	}

	/**
	 * @param finishDate the finishDate to set
	 */
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the jobCategory
	 */
	public String getJobCategory() {
		return jobCategory;
	}

	/**
	 * @param jobCategory the jobCategory to set
	 */
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
}
