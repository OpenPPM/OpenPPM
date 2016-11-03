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
 * Module: plugin-project-change
 * File: ChangeReport.java
 * Create User: javier.hernandez
 * Create Date: 20/04/2015 18:35:33
 */

package es.sm2.openppm.plugin.change.model;

import es.sm2.openppm.core.reports.ReportDataSource;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by javier.hernandez on 20/04/2015.
 */
public class ChangeReport {

    private HashMap<String, String> translationMap;

    private String projectName;
    private String projectManager;
    private String program;
    private String po;
    private String functionalManager;
    private String originator;
    private String budget;
    private String priority;
    private String customer;
    private String requestDate;
    private String changeType;
    private String changeDescription;
    private String recommendedSolution;
    private String impactDescription;
    private String status;
    private String validatedBy;
    private String resolutionDate;
    private String comments;

    private ReportDataSource<ProjectActivityReport> activities;

    private InputStream logo;

    /**
     * Setter for property 'logo'.
     *
     * @param logo Value to set for property 'logo'.
     */
    public void setLogo(InputStream logo) {

        this.logo = logo;
    }

    /**
     * Getter for property 'projectName'.
     *
     * @return Value for property 'projectName'.
     */
    public String getProjectName() {

        return projectName;
    }

    /**
     * Setter for property 'projectName'.
     *
     * @param projectName Value to set for property 'projectName'.
     */
    public void setProjectName(String projectName) {

        this.projectName = projectName;
    }

    /**
     * Getter for property 'projectManager'.
     *
     * @return Value for property 'projectManager'.
     */
    public String getProjectManager() {

        return projectManager;
    }

    /**
     * Setter for property 'projectManager'.
     *
     * @param projectManager Value to set for property 'projectManager'.
     */
    public void setProjectManager(String projectManager) {

        this.projectManager = projectManager;
    }

    /**
     * Getter for property 'program'.
     *
     * @return Value for property 'program'.
     */
    public String getProgram() {

        return program;
    }

    /**
     * Setter for property 'program'.
     *
     * @param program Value to set for property 'program'.
     */
    public void setProgram(String program) {

        this.program = program;
    }

    /**
     * Getter for property 'po'.
     *
     * @return Value for property 'po'.
     */
    public String getPo() {

        return po;
    }

    /**
     * Setter for property 'po'.
     *
     * @param po Value to set for property 'po'.
     */
    public void setPo(String po) {

        this.po = po;
    }

    /**
     * Getter for property 'functionalManager'.
     *
     * @return Value for property 'functionalManager'.
     */
    public String getFunctionalManager() {

        return functionalManager;
    }

    /**
     * Setter for property 'functionalManager'.
     *
     * @param functionalManager Value to set for property 'functionalManager'.
     */
    public void setFunctionalManager(String functionalManager) {

        this.functionalManager = functionalManager;
    }

    /**
     * Getter for property 'originator'.
     *
     * @return Value for property 'originator'.
     */
    public String getOriginator() {

        return originator;
    }

    /**
     * Setter for property 'originator'.
     *
     * @param originator Value to set for property 'originator'.
     */
    public void setOriginator(String originator) {

        this.originator = originator;
    }

    /**
     * Getter for property 'budget'.
     *
     * @return Value for property 'budget'.
     */
    public String getBudget() {

        return budget;
    }

    /**
     * Setter for property 'budget'.
     *
     * @param budget Value to set for property 'budget'.
     */
    public void setBudget(String budget) {

        this.budget = budget;
    }

    /**
     * Getter for property 'priority'.
     *
     * @return Value for property 'priority'.
     */
    public String getPriority() {

        return priority;
    }

    /**
     * Setter for property 'priority'.
     *
     * @param priority Value to set for property 'priority'.
     */
    public void setPriority(String priority) {

        this.priority = priority;
    }

    /**
     * Getter for property 'customer'.
     *
     * @return Value for property 'customer'.
     */
    public String getCustomer() {

        return customer;
    }

    /**
     * Setter for property 'customer'.
     *
     * @param customer Value to set for property 'customer'.
     */
    public void setCustomer(String customer) {

        this.customer = customer;
    }

    /**
     * Getter for property 'requestDate'.
     *
     * @return Value for property 'requestDate'.
     */
    public String getRequestDate() {

        return requestDate;
    }

    /**
     * Setter for property 'requestDate'.
     *
     * @param requestDate Value to set for property 'requestDate'.
     */
    public void setRequestDate(String requestDate) {

        this.requestDate = requestDate;
    }

    /**
     * Getter for property 'changeType'.
     *
     * @return Value for property 'changeType'.
     */
    public String getChangeType() {

        return changeType;
    }

    /**
     * Setter for property 'changeType'.
     *
     * @param changeType Value to set for property 'changeType'.
     */
    public void setChangeType(String changeType) {

        this.changeType = changeType;
    }

    /**
     * Getter for property 'changeDescription'.
     *
     * @return Value for property 'changeDescription'.
     */
    public String getChangeDescription() {

        return changeDescription;
    }

    /**
     * Setter for property 'changeDescription'.
     *
     * @param changeDescription Value to set for property 'changeDescription'.
     */
    public void setChangeDescription(String changeDescription) {

        this.changeDescription = changeDescription;
    }

    /**
     * Getter for property 'recommendedSolution'.
     *
     * @return Value for property 'recommendedSolution'.
     */
    public String getRecommendedSolution() {

        return recommendedSolution;
    }

    /**
     * Setter for property 'recommendedSolution'.
     *
     * @param recommendedSolution Value to set for property 'recommendedSolution'.
     */
    public void setRecommendedSolution(String recommendedSolution) {

        this.recommendedSolution = recommendedSolution;
    }

    /**
     * Getter for property 'impactDescription'.
     *
     * @return Value for property 'impactDescription'.
     */
    public String getImpactDescription() {

        return impactDescription;
    }

    /**
     * Setter for property 'impactDescription'.
     *
     * @param impactDescription Value to set for property 'impactDescription'.
     */
    public void setImpactDescription(String impactDescription) {

        this.impactDescription = impactDescription;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public String getStatus() {

        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Getter for property 'validatedBy'.
     *
     * @return Value for property 'validatedBy'.
     */
    public String getValidatedBy() {

        return validatedBy;
    }

    /**
     * Setter for property 'validatedBy'.
     *
     * @param validatedBy Value to set for property 'validatedBy'.
     */
    public void setValidatedBy(String validatedBy) {

        this.validatedBy = validatedBy;
    }

    /**
     * Getter for property 'resolutionDate'.
     *
     * @return Value for property 'resolutionDate'.
     */
    public String getResolutionDate() {

        return resolutionDate;
    }

    /**
     * Setter for property 'resolutionDate'.
     *
     * @param resolutionDate Value to set for property 'resolutionDate'.
     */
    public void setResolutionDate(String resolutionDate) {

        this.resolutionDate = resolutionDate;
    }

    /**
     * Getter for property 'comments'.
     *
     * @return Value for property 'comments'.
     */
    public String getComments() {

        return comments;
    }

    /**
     * Setter for property 'comments'.
     *
     * @param comments Value to set for property 'comments'.
     */
    public void setComments(String comments) {

        this.comments = comments;
    }

    /**
     * Getter for property 'activities'.
     *
     * @return Value for property 'activities'.
     */
    public ReportDataSource<ProjectActivityReport> getActivities() {

        return activities;
    }

    /**
     * Setter for property 'activities'.
     *
     * @param activities Value to set for property 'activities'.
     */
    public void setActivities(ReportDataSource<ProjectActivityReport> activities) {

        this.activities = activities;
    }

    public InputStream getLogo() {
        return logo;
    }

    public HashMap<String, String> getTranslationMap() {
        return translationMap;
    }

    public void setTranslationMap(HashMap<String, String> translationMap) {
        this.translationMap = translationMap;
    }
}
