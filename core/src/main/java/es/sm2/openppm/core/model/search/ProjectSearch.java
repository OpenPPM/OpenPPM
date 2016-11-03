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
 * File: ProjectFilter.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.base.BaseSearch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProjectSearch extends BaseSearch {

	public static final String GREATHER_EQUAL	= "greaterEqual";
	public static final String LESS_EQUAL		= "lessEqual";
	public static final String BETWEEN			= "between";
	
	private List<Integer> performingorgs;
	private List<Integer> employeeBySponsors;
	private List<Integer> employeeByProjectManagers;
	private List<Integer> employeeByFunctionalManagers;
	private List<Integer> contractTypes;
	private List<Integer> customers;
	private List<Integer> customertypes;
	private List<Integer> programs;
	private List<Integer> categories;
	private List<Integer> sellers;
	private List<Integer> geography;
	private List<Integer> fundingsources;
	private List<Integer> projects;
	private List<Integer> labels;
    private List<Integer> technologies;
	private List<Integer> stageGates;
	private List<String> status;
	private List<String> investmentStatus;
	private List<Integer> classificationsLevel;
	private String projectName;
	private String rag;
	private Company company;
	private Employee employeeByInvestmentManager;
	private Employee stakeholder;
	private Employee programManager;
	private Date since;
	private Date until;
	private Boolean internalProject;
	private Boolean isGeoSelling;
	private Integer budgetYear;
	private String priority;
	private Integer firstPriority;
	private Integer lastPriority;
	private Boolean showInactivated;
	private Boolean includeDisabled;
	private Boolean includeRejected;
	private Boolean isIndirectSeller;
	private Boolean includingAdjustament;
	private String riskRating;
	private Integer firstRiskRating;
	private Integer lastRiskRating;
    private boolean classificationsLevelUnclassified;
    private boolean fundingSourceUnclassified;
    private Employee user;
    private HashMap<String, String> settings;

    public ProjectSearch(Employee user, HashMap<String, String> settings) {

        this.user = user;
        this.settings = settings;
        this.company = user.getContact().getCompany();
    }

    /**
     * Getter for property 'user'.
     *
     * @return Value for property 'user'.
     */
    public Employee getUser() {

        return user;
    }

    /**
     * Setter for property 'user'.
     *
     * @param user Value to set for property 'user'.
     */
    public void setUser(Employee user) {

        this.user = user;
    }

    /**
     * Getter for property 'settings'.
     *
     * @return Value for property 'settings'.
     */
    public HashMap<String, String> getSettings() {

        return settings;
    }

    /**
     * Setter for property 'settings'.
     *
     * @param settings Value to set for property 'settings'.
     */
    public void setSettings(HashMap<String, String> settings) {

        this.settings = settings;
    }

    /**
	 * @return the performingorgs
	 */
	public List<Integer> getPerformingorgs() {
		return performingorgs;
	}
	/**
	 * @param performingorgs the performingorgs to set
	 */
	public void setPerformingorgs(List<Integer> performingorgs) {
		this.performingorgs = performingorgs;
	}
	/**
	 * @return the employeeBySponsors
	 */
	public List<Integer> getEmployeeBySponsors() {
		return employeeBySponsors;
	}
	/**
	 * @param employeeBySponsors the employeeBySponsors to set
	 */
	public void setEmployeeBySponsors(List<Integer> employeeBySponsors) {
		this.employeeBySponsors = employeeBySponsors;
	}
	/**
	 * @return the employeeByProjectManagers
	 */
	public List<Integer> getEmployeeByProjectManagers() {
		return employeeByProjectManagers;
	}
	/**
	 * @param employeeByProjectManagers the employeeByProjectManagers to set
	 */
	public void setEmployeeByProjectManagers(List<Integer> employeeByProjectManagers) {
		this.employeeByProjectManagers = employeeByProjectManagers;
	}
	/**
	 * @return the customers
	 */
	public List<Integer> getCustomers() {
		return customers;
	}
	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Integer> customers) {
		this.customers = customers;
	}
	/**
	 * @return the customertypes
	 */
	public List<Integer> getCustomertypes() {
		return customertypes;
	}
	/**
	 * @param customertypes the customertypes to set
	 */
	public void setCustomertypes(List<Integer> customertypes) {
		this.customertypes = customertypes;
	}
	/**
	 * @return the programs
	 */
	public List<Integer> getPrograms() {
		return programs;
	}
	/**
	 * @param programs the programs to set
	 */
	public void setPrograms(List<Integer> programs) {
		this.programs = programs;
	}
	/**
	 * @return the categories
	 */
	public List<Integer> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}
	/**
	 * @return the sellers
	 */
	public List<Integer> getSellers() {
		return sellers;
	}
	/**
	 * @param sellers the sellers to set
	 */
	public void setSellers(List<Integer> sellers) {
		this.sellers = sellers;
	}
	/**
	 * @return the geography
	 */
	public List<Integer> getGeography() {
		return geography;
	}
	/**
	 * @param geography the geography to set
	 */
	public void setGeography(List<Integer> geography) {
		this.geography = geography;
	}
	/**
	 * @return the fundingsources
	 */
	public List<Integer> getFundingsources() {
		return fundingsources;
	}
	/**
	 * @param fundingsources the fundingsources to set
	 */
	public void setFundingsources(List<Integer> fundingsources) {
		this.fundingsources = fundingsources;
	}
	/**
	 * @return the projects
	 */
	public List<Integer> getProjects() {
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Integer> projects) {
		this.projects = projects;
	}
	/**
	 * @return the status
	 */
	public List<String> getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(List<String> status) {
		this.status = status;
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
	 * @return the rag
	 */
	public String getRag() {
		return rag;
	}
	/**
	 * @param rag the rag to set
	 */
	public void setRag(String rag) {
		this.rag = rag;
	}
	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}
	/**
	 * @return the employeeByInvestmentManager
	 */
	public Employee getEmployeeByInvestmentManager() {
		return employeeByInvestmentManager;
	}
	/**
	 * @param employeeByInvestmentManager the employeeByInvestmentManager to set
	 */
	public void setEmployeeByInvestmentManager(Employee employeeByInvestmentManager) {
		this.employeeByInvestmentManager = employeeByInvestmentManager;
	}
	/**
	 * @return the stakeholder
	 */
	public Employee getStakeholder() {
		return stakeholder;
	}
	/**
	 * @param stakeholder the stakeholder to set
	 */
	public void setStakeholder(Employee stakeholder) {
		this.stakeholder = stakeholder;
	}
	/**
	 * @return the programManager
	 */
	public Employee getProgramManager() {
		return programManager;
	}
	/**
	 * @param programManager the programManager to set
	 */
	public void setProgramManager(Employee programManager) {
		this.programManager = programManager;
	}
	/**
	 * @return the since
	 */
	public Date getSince() {
		return since;
	}
	/**
	 * @param since the since to set
	 */
	public void setSince(Date since) {
		this.since = since;
	}
	/**
	 * @return the until
	 */
	public Date getUntil() {
		return until;
	}
	/**
	 * @param until the until to set
	 */
	public void setUntil(Date until) {
		this.until = until;
	}
	/**
	 * @return the internalProject
	 */
	public Boolean getInternalProject() {
		return internalProject;
	}
	/**
	 * @param internalProject the internalProject to set
	 */
	public void setInternalProject(Boolean internalProject) {
		this.internalProject = internalProject;
	}
	/**
	 * @return the budgetYear
	 */
	public Integer getBudgetYear() {
		return budgetYear;
	}
	/**
	 * @param budgetYear the budgetYear to set
	 */
	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return the firstPriority
	 */
	public Integer getFirstPriority() {
		return firstPriority;
	}
	/**
	 * @param firstPriority the firstPriority to set
	 */
	public void setFirstPriority(Integer firstPriority) {
		this.firstPriority = firstPriority;
	}
	/**
	 * @return the lastPriority
	 */
	public Integer getLastPriority() {
		return lastPriority;
	}
	/**
	 * @param lastPriority the lastPriority to set
	 */
	public void setLastPriority(Integer lastPriority) {
		this.lastPriority = lastPriority;
	}
	/**
	 * @return the investmentStatus
	 */
	public List<String> getInvestmentStatus() {
		return investmentStatus;
	}
	/**
	 * @param investmentStatus the investmentStatus to set
	 */
	public void setInvestmentStatus(List<String> investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	/**
	 * @return the labels
	 */
	public List<Integer> getLabels() {
		return labels;
	}
	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<Integer> labels) {
		this.labels = labels;
	}
	/**
	 * @return the isGeoSelling
	 */
	public Boolean getIsGeoSelling() {
		return isGeoSelling;
	}
	/**
	 * @param isGeoSelling the isGeoSelling to set
	 */
	public void setIsGeoSelling(Boolean isGeoSelling) {
		this.isGeoSelling = isGeoSelling;
	}
	/**
	 * @return the showInactivated
	 */
	public Boolean getShowInactivated() {
		return showInactivated;
	}
	/**
	 * @param showInactivated the showInactivated to set
	 */
	public void setShowInactivated(Boolean showInactivated) {
		this.showInactivated = showInactivated;
	}
	/**
	 * @return the stageGates
	 */
	public List<Integer> getStageGates() {
		return stageGates;
	}
	/**
	 * @param stageGates the stageGates to set
	 */
	public void setStageGates(List<Integer> stageGates) {
		this.stageGates = stageGates;
	}
	/**
	 * @return the includeDisabled
	 */
	public Boolean getIncludeDisabled() {
		return includeDisabled;
	}
	/**
	 * @param includeDisabled the includeDisabled to set
	 */
	public void setIncludeDisabled(Boolean includeDisabled) {
		this.includeDisabled = includeDisabled;
	}
	/**
	 * @return the employeeByFunctionalManagers
	 */
	public List<Integer> getEmployeeByFunctionalManagers() {
		return employeeByFunctionalManagers;
	}
	/**
	 * @param employeeByFunctionalManagers the employeeByFunctionalManagers to set
	 */
	public void setEmployeeByFunctionalManagers(List<Integer> employeeByFunctionalManagers) {
		this.employeeByFunctionalManagers = employeeByFunctionalManagers;
	}
	/**
	 * @return the contractTypes
	 */
	public List<Integer> getContractTypes() {
		return contractTypes;
	}
	/**
	 * @param contractTypes the contractTypes to set
	 */
	public void setContractTypes(List<Integer> contractTypes) {
		this.contractTypes = contractTypes;
	}
	
	/**
	 * @return the isIndirectSeller
	 */
	public Boolean getIsIndirectSeller() {
		return isIndirectSeller;
	}
	/**
	 * @param isIndirectSeller the isIndirectSeller to set
	 */
	public void setIsIndirectSeller(Boolean isIndirectSeller) {
		this.isIndirectSeller = isIndirectSeller;
	}
	/**
	 * @return the includeRejected
	 */
	public Boolean getIncludeRejected() {
		return includeRejected;
	}
	/**
	 * @param includeRejected the includeRejected to set
	 */
	public void setIncludeRejected(Boolean includeRejected) {
		this.includeRejected = includeRejected;
	}
	/**
	 * @return the classificationsLevel
	 */
	public List<Integer> getClassificationsLevel() {
		return classificationsLevel;
	}
	/**
	 * @param classificationsLevel the classificationsLevel to set
	 */
	public void setClassificationsLevel(List<Integer> classificationsLevel) {
		this.classificationsLevel = classificationsLevel;
	}
	/**
	 * @return the includingAdjustament
	 */
	public Boolean getIncludingAdjustament() {
		return includingAdjustament;
	}
	/**
	 * @param includingAdjustament the includingAdjustament to set
	 */
	public void setIncludingAdjustament(Boolean includingAdjustament) {
		this.includingAdjustament = includingAdjustament;
	}
	/**
	 * @return the riskRating
	 */
	public String getRiskRating() {
		return riskRating;
	}
	/**
	 * @param riskRating the riskRating to set
	 */
	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}
	/**
	 * @return the firstRiskRating
	 */
	public Integer getFirstRiskRating() {
		return firstRiskRating;
	}
	/**
	 * @param firstRiskRating the firstRiskRating to set
	 */
	public void setFirstRiskRating(Integer firstRiskRating) {
		this.firstRiskRating = firstRiskRating;
	}
	/**
	 * @return the lastRiskRating
	 */
	public Integer getLastRiskRating() {
		return lastRiskRating;
	}
	/**
	 * @param lastRiskRating the lastRiskRating to set
	 */
	public void setLastRiskRating(Integer lastRiskRating) {
		this.lastRiskRating = lastRiskRating;
	}

    public void setClassificationsLevelUnclassified(boolean classificationsLevelUnclassified) {
        this.classificationsLevelUnclassified = classificationsLevelUnclassified;
    }

    public boolean isClassificationsLevelUnclassified() {
        return classificationsLevelUnclassified;
    }

    public boolean isFundingSourceUnclassified() {
        return fundingSourceUnclassified;
    }

    public void setFundingSourceUnclassified(boolean fundingSourceUnclassified) {
        this.fundingSourceUnclassified = fundingSourceUnclassified;
    }

    public List<Integer> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Integer> technologies) {
        this.technologies = technologies;
    }
}
