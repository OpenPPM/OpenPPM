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
 * File: BaseCompany.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Bscdimension;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Businessdriver;
import es.sm2.openppm.core.model.impl.Businessdriverset;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Changetype;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Closurecheck;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Companyfile;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.Expenseaccounts;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Plugin;
import es.sm2.openppm.core.model.impl.Problemcheck;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Riskcategory;
import es.sm2.openppm.core.model.impl.Riskregistertemplate;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.core.model.impl.Wbstemplate;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Company
 * @see es.sm2.openppm.core.model.base.Company
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Company
 */
public class BaseCompany  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "company";
	
	public static final String IDCOMPANY = "idCompany";
	public static final String NAME = "name";
	public static final String DISABLE = "disable";
	public static final String STAKEHOLDERCLASSIFICATIONS = "stakeholderclassifications";
	public static final String CLASSIFICATIONLEVELS = "classificationlevels";
	public static final String PLUGINS = "plugins";
	public static final String CUSTOMERTYPES = "customertypes";
	public static final String PERFORMINGORGS = "performingorgs";
	public static final String COMPANYFILES = "companyfiles";
	public static final String RISKREGISTERTEMPLATES = "riskregistertemplates";
	public static final String SKILLS = "skills";
	public static final String CONTRACTTYPES = "contracttypes";
	public static final String LABELS = "labels";
	public static final String CALENDARBASES = "calendarbases";
	public static final String CURRENCIES = "currencies";
	public static final String CONTACTS = "contacts";
	public static final String MILESTONETYPES = "milestonetypes";
	public static final String MILESTONECATEGORIES = "milestonecategories";
	public static final String EXPENSEACCOUNTSES = "expenseaccountses";
	public static final String PROBLEMCHECKS = "problemchecks";
	public static final String BUDGETACCOUNTSES = "budgetaccountses";
	public static final String METRICKPIS = "metrickpis";
	public static final String SELLERS = "sellers";
	public static final String CATEGORIES = "categories";
	public static final String CLOSURECHECKS = "closurechecks";
	public static final String STAGEGATES = "stagegates";
	public static final String BUSINESSDRIVERSETS = "businessdriversets";
	public static final String RESOURCEPOOLS = "resourcepools";
	public static final String BSCDIMENSIONS = "bscdimensions";
	public static final String OPERATIONACCOUNTS = "operationaccounts";
	public static final String GEOGRAPHIES = "geographies";
	public static final String RISKCATEGORIES = "riskcategories";
	public static final String DOCUMENTATIONS = "documentations";
	public static final String CUSTOMERS = "customers";
	public static final String WBSTEMPLATES = "wbstemplates";
	public static final String FUNDINGSOURCES = "fundingsources";
	public static final String SETTINGS = "settings";
	public static final String JOBCATEGORIES = "jobcategories";
	public static final String CHANGETYPES = "changetypes";
	public static final String BUSINESSDRIVERS = "businessdrivers";

     private Integer idCompany;
     private String name;
     private Boolean disable;
     private Set<Stakeholderclassification> stakeholderclassifications = new HashSet<Stakeholderclassification>(0);
     private Set<Classificationlevel> classificationlevels = new HashSet<Classificationlevel>(0);
     private Set<Plugin> plugins = new HashSet<Plugin>(0);
     private Set<Customertype> customertypes = new HashSet<Customertype>(0);
     private Set<Performingorg> performingorgs = new HashSet<Performingorg>(0);
     private Set<Companyfile> companyfiles = new HashSet<Companyfile>(0);
     private Set<Riskregistertemplate> riskregistertemplates = new HashSet<Riskregistertemplate>(0);
     private Set<Skill> skills = new HashSet<Skill>(0);
     private Set<Contracttype> contracttypes = new HashSet<Contracttype>(0);
     private Set<Label> labels = new HashSet<Label>(0);
     private Set<Calendarbase> calendarbases = new HashSet<Calendarbase>(0);
     private Set<Currency> currencies = new HashSet<Currency>(0);
     private Set<Contact> contacts = new HashSet<Contact>(0);
     private Set<Milestonetype> milestonetypes = new HashSet<Milestonetype>(0);
     private Set<Milestonecategory> milestonecategories = new HashSet<Milestonecategory>(0);
     private Set<Expenseaccounts> expenseaccountses = new HashSet<Expenseaccounts>(0);
     private Set<Problemcheck> problemchecks = new HashSet<Problemcheck>(0);
     private Set<Budgetaccounts> budgetaccountses = new HashSet<Budgetaccounts>(0);
     private Set<Metrickpi> metrickpis = new HashSet<Metrickpi>(0);
     private Set<Seller> sellers = new HashSet<Seller>(0);
     private Set<Category> categories = new HashSet<Category>(0);
     private Set<Closurecheck> closurechecks = new HashSet<Closurecheck>(0);
     private Set<Stagegate> stagegates = new HashSet<Stagegate>(0);
     private Set<Businessdriverset> businessdriversets = new HashSet<Businessdriverset>(0);
     private Set<Resourcepool> resourcepools = new HashSet<Resourcepool>(0);
     private Set<Bscdimension> bscdimensions = new HashSet<Bscdimension>(0);
     private Set<Operationaccount> operationaccounts = new HashSet<Operationaccount>(0);
     private Set<Geography> geographies = new HashSet<Geography>(0);
     private Set<Riskcategory> riskcategories = new HashSet<Riskcategory>(0);
     private Set<Documentation> documentations = new HashSet<Documentation>(0);
     private Set<Customer> customers = new HashSet<Customer>(0);
     private Set<Wbstemplate> wbstemplates = new HashSet<Wbstemplate>(0);
     private Set<Fundingsource> fundingsources = new HashSet<Fundingsource>(0);
     private Set<Setting> settings = new HashSet<Setting>(0);
     private Set<Jobcategory> jobcategories = new HashSet<Jobcategory>(0);
     private Set<Changetype> changetypes = new HashSet<Changetype>(0);
     private Set<Businessdriver> businessdrivers = new HashSet<Businessdriver>(0);

    public BaseCompany() {
    }
    
    public BaseCompany(Integer idCompany) {
    	this.idCompany = idCompany;
    }
   
    public Integer getIdCompany() {
        return this.idCompany;
    }
    
    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Boolean getDisable() {
        return this.disable;
    }
    
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    public Set<Stakeholderclassification> getStakeholderclassifications() {
        return this.stakeholderclassifications;
    }
    
    public void setStakeholderclassifications(Set<Stakeholderclassification> stakeholderclassifications) {
        this.stakeholderclassifications = stakeholderclassifications;
    }
    public Set<Classificationlevel> getClassificationlevels() {
        return this.classificationlevels;
    }
    
    public void setClassificationlevels(Set<Classificationlevel> classificationlevels) {
        this.classificationlevels = classificationlevels;
    }
    public Set<Plugin> getPlugins() {
        return this.plugins;
    }
    
    public void setPlugins(Set<Plugin> plugins) {
        this.plugins = plugins;
    }
    public Set<Customertype> getCustomertypes() {
        return this.customertypes;
    }
    
    public void setCustomertypes(Set<Customertype> customertypes) {
        this.customertypes = customertypes;
    }
    public Set<Performingorg> getPerformingorgs() {
        return this.performingorgs;
    }
    
    public void setPerformingorgs(Set<Performingorg> performingorgs) {
        this.performingorgs = performingorgs;
    }
    public Set<Companyfile> getCompanyfiles() {
        return this.companyfiles;
    }
    
    public void setCompanyfiles(Set<Companyfile> companyfiles) {
        this.companyfiles = companyfiles;
    }
    public Set<Riskregistertemplate> getRiskregistertemplates() {
        return this.riskregistertemplates;
    }
    
    public void setRiskregistertemplates(Set<Riskregistertemplate> riskregistertemplates) {
        this.riskregistertemplates = riskregistertemplates;
    }
    public Set<Skill> getSkills() {
        return this.skills;
    }
    
    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
    public Set<Contracttype> getContracttypes() {
        return this.contracttypes;
    }
    
    public void setContracttypes(Set<Contracttype> contracttypes) {
        this.contracttypes = contracttypes;
    }
    public Set<Label> getLabels() {
        return this.labels;
    }
    
    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }
    public Set<Calendarbase> getCalendarbases() {
        return this.calendarbases;
    }
    
    public void setCalendarbases(Set<Calendarbase> calendarbases) {
        this.calendarbases = calendarbases;
    }
    public Set<Currency> getCurrencies() {
        return this.currencies;
    }
    
    public void setCurrencies(Set<Currency> currencies) {
        this.currencies = currencies;
    }
    public Set<Contact> getContacts() {
        return this.contacts;
    }
    
    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
    public Set<Milestonetype> getMilestonetypes() {
        return this.milestonetypes;
    }
    
    public void setMilestonetypes(Set<Milestonetype> milestonetypes) {
        this.milestonetypes = milestonetypes;
    }
    public Set<Milestonecategory> getMilestonecategories() {
        return this.milestonecategories;
    }
    
    public void setMilestonecategories(Set<Milestonecategory> milestonecategories) {
        this.milestonecategories = milestonecategories;
    }
    public Set<Expenseaccounts> getExpenseaccountses() {
        return this.expenseaccountses;
    }
    
    public void setExpenseaccountses(Set<Expenseaccounts> expenseaccountses) {
        this.expenseaccountses = expenseaccountses;
    }
    public Set<Problemcheck> getProblemchecks() {
        return this.problemchecks;
    }
    
    public void setProblemchecks(Set<Problemcheck> problemchecks) {
        this.problemchecks = problemchecks;
    }
    public Set<Budgetaccounts> getBudgetaccountses() {
        return this.budgetaccountses;
    }
    
    public void setBudgetaccountses(Set<Budgetaccounts> budgetaccountses) {
        this.budgetaccountses = budgetaccountses;
    }
    public Set<Metrickpi> getMetrickpis() {
        return this.metrickpis;
    }
    
    public void setMetrickpis(Set<Metrickpi> metrickpis) {
        this.metrickpis = metrickpis;
    }
    public Set<Seller> getSellers() {
        return this.sellers;
    }
    
    public void setSellers(Set<Seller> sellers) {
        this.sellers = sellers;
    }
    public Set<Category> getCategories() {
        return this.categories;
    }
    
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    public Set<Closurecheck> getClosurechecks() {
        return this.closurechecks;
    }
    
    public void setClosurechecks(Set<Closurecheck> closurechecks) {
        this.closurechecks = closurechecks;
    }
    public Set<Stagegate> getStagegates() {
        return this.stagegates;
    }
    
    public void setStagegates(Set<Stagegate> stagegates) {
        this.stagegates = stagegates;
    }
    public Set<Businessdriverset> getBusinessdriversets() {
        return this.businessdriversets;
    }
    
    public void setBusinessdriversets(Set<Businessdriverset> businessdriversets) {
        this.businessdriversets = businessdriversets;
    }
    public Set<Resourcepool> getResourcepools() {
        return this.resourcepools;
    }
    
    public void setResourcepools(Set<Resourcepool> resourcepools) {
        this.resourcepools = resourcepools;
    }
    public Set<Bscdimension> getBscdimensions() {
        return this.bscdimensions;
    }
    
    public void setBscdimensions(Set<Bscdimension> bscdimensions) {
        this.bscdimensions = bscdimensions;
    }
    public Set<Operationaccount> getOperationaccounts() {
        return this.operationaccounts;
    }
    
    public void setOperationaccounts(Set<Operationaccount> operationaccounts) {
        this.operationaccounts = operationaccounts;
    }
    public Set<Geography> getGeographies() {
        return this.geographies;
    }
    
    public void setGeographies(Set<Geography> geographies) {
        this.geographies = geographies;
    }
    public Set<Riskcategory> getRiskcategories() {
        return this.riskcategories;
    }
    
    public void setRiskcategories(Set<Riskcategory> riskcategories) {
        this.riskcategories = riskcategories;
    }
    public Set<Documentation> getDocumentations() {
        return this.documentations;
    }
    
    public void setDocumentations(Set<Documentation> documentations) {
        this.documentations = documentations;
    }
    public Set<Customer> getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
    public Set<Wbstemplate> getWbstemplates() {
        return this.wbstemplates;
    }
    
    public void setWbstemplates(Set<Wbstemplate> wbstemplates) {
        this.wbstemplates = wbstemplates;
    }
    public Set<Fundingsource> getFundingsources() {
        return this.fundingsources;
    }
    
    public void setFundingsources(Set<Fundingsource> fundingsources) {
        this.fundingsources = fundingsources;
    }
    public Set<Setting> getSettings() {
        return this.settings;
    }
    
    public void setSettings(Set<Setting> settings) {
        this.settings = settings;
    }
    public Set<Jobcategory> getJobcategories() {
        return this.jobcategories;
    }
    
    public void setJobcategories(Set<Jobcategory> jobcategories) {
        this.jobcategories = jobcategories;
    }
    public Set<Changetype> getChangetypes() {
        return this.changetypes;
    }
    
    public void setChangetypes(Set<Changetype> changetypes) {
        this.changetypes = changetypes;
    }
    public Set<Businessdriver> getBusinessdrivers() {
        return this.businessdrivers;
    }
    
    public void setBusinessdrivers(Set<Businessdriver> businessdrivers) {
        this.businessdrivers = businessdrivers;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Company) )  { result = false; }
		 else if (other != null) {
		 	Company castOther = (Company) other;
			if (castOther.getIdCompany().equals(this.getIdCompany())) { result = true; }
         }
		 return result;
   }


}


