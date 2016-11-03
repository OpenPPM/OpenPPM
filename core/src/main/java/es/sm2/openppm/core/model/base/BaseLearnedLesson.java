/*
 *   Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program has been created in the hope that it will be useful.
 *   It is distributed WITHOUT ANY WARRANTY of any Kind,
 *   without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see http://www.gnu.org/licenses/.
 *
 *   For more information, please contact SM2 Software & Services Management.
 *   Mail: info@talaia-openppm.com
 *   Web: http://www.talaia-openppm.com
 *
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.ActionLesson;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.KnowledgeArea;
import es.sm2.openppm.core.model.impl.Profile;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.core.model.impl.RecommendationLesson;
import es.sm2.openppm.core.model.impl.Seller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Base Pojo object for domain model class LearnedLesson
 * @see es.sm2.openppm.core.model.base.BaseLearnedLesson
 * For implement your own methods use class LearnedLesson
 * Created by francisco.bisquerra on 31/07/2015.
 */
public class BaseLearnedLesson {


    public static final String IDLEARNEDLESSON = "idLearnedLesson";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String KNOWLEDGEAREA = "knowledgeArea";
    public static final String PROFILE = "profile";
    public static final String COMPANY = "company";
    public static final String LEARNEDLESSONLINKS = "learnedLessonLinks";
    public static final String LEARNEDLESSONPROJECTS = "learnedLessonProjects";
    public static final String ACTIONSLESSON = "actionsLesson";
    public static final String RECSLESSON = "recsLesson";
    public static final String PROCESSGROUP = "processGroup";
    public static final String DATERAISED = "dateRaised";
    public static final String CONTACT = "contact";
    public static final String IMPACTTIME = "impactTime";
    public static final String IMPACTCOST = "impactCost";
    public static final String IMPACTSATISFACTION = "impactSatisfaction";
    public static final String PROGRAM = "program";
    public static final String CUSTOMER = "customer";
    public static final String SELLER = "seller";
    public static final String GEOGRAPHY = "geography";
    public static final String CONTRACTTYPE = "contracttype";
    public static final String FUNDINGSOURCE = "fundingSource";

    private Integer idLearnedLesson;
    private String name;
    private String description;
    private Date dateRaised;
    private Contact contact;
    private Double impactTime;
    private Double impactCost;
    private String impactSatisfaction;
    private KnowledgeArea knowledgeArea;
    private Profile profile;
    private Program program;
    private Customer customer;
    private Seller seller;
    private Geography geography;
    private Contracttype contracttype;
    private Fundingsource fundingSource;
    private Company company;
    private String processGroup;
    private String type;
    private Set<LearnedLessonProject> learnedLessonProjects = new HashSet<LearnedLessonProject>(0);
    private Set<LearnedLessonLink> learnedLessonLinks = new HashSet<LearnedLessonLink>(0);
    private Set<ActionLesson> actionsLesson = new HashSet<ActionLesson>(0);
    private Set<RecommendationLesson> recsLesson = new HashSet<RecommendationLesson>(0);

    public BaseLearnedLesson(){
    }

    /**
     * Getter for property 'learnedLessonProjects'.
     *
     * @return Value for property 'learnedLessonProjects'.
     */
    public Set<LearnedLessonProject> getLearnedLessonProjects() {
        return learnedLessonProjects;
    }

    /**
     * Setter for property 'learnedLessonProjects'.
     *
     * @param learnedLessonProjects Value to set for property 'learnedLessonProjects'.
     */
    public void setLearnedLessonProjects(Set<LearnedLessonProject> learnedLessonProjects) {
        this.learnedLessonProjects = learnedLessonProjects;
    }

    /**
     * Getter for property 'learnedLessonLinks'.
     *
     * @return Value for property 'learnedLessonLinks'.
     */
    public Set<LearnedLessonLink> getLearnedLessonLinks() {
        return learnedLessonLinks;
    }

    /**
     * Setter for property 'learnedLessonLinks'.
     *
     * @param learnedLessonLinks Value to set for property 'learnedLessonLinks'.
     */
    public void setLearnedLessonLinks(Set<LearnedLessonLink> learnedLessonLinks) {
        this.learnedLessonLinks = learnedLessonLinks;
    }

    /**
     * Getter for property 'contact'.
     *
     * @return Value for property 'contact'.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Setter for property 'contact'.
     *
     * @param contact Value to set for property 'contact'.
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Getter for property 'dateRaised'.
     *
     * @return Value for property 'dateRaised'.
     */
    public Date getDateRaised() {
        return dateRaised;
    }

    /**
     * Setter for property 'dateRaised'.
     *
     * @param dateRaised Value to set for property 'dateRaised'.
     */
    public void setDateRaised(Date dateRaised) {
        this.dateRaised = dateRaised;
    }

    /**
     * Getter for property 'impactTime'.
     *
     * @return Value for property 'impactTime'.
     */
    public Double getImpactTime() {
        return impactTime;
    }

    /**
     * Setter for property 'impactTime'.
     *
     * @param impactTime Value to set for property 'impactTime'.
     */
    public void setImpactTime(Double impactTime) {
        this.impactTime = impactTime;
    }

    /**
     * Getter for property 'impactCost'.
     *
     * @return Value for property 'impactCost'.
     */
    public Double getImpactCost() {
        return impactCost;
    }

    /**
     * Setter for property 'impactCost'.
     *
     * @param impactCost Value to set for property 'impactCost'.
     */
    public void setImpactCost(Double impactCost) {
        this.impactCost = impactCost;
    }

    /**
     * Getter for property 'impactSatisfaction'.
     *
     * @return Value for property 'impactSatisfaction'.
     */
    public String getImpactSatisfaction() {
        return impactSatisfaction;
    }

    /**
     * Setter for property 'impactSatisfaction'.
     *
     * @param impactSatisfaction Value to set for property 'impactSatisfaction'.
     */
    public void setImpactSatisfaction(String impactSatisfaction) {
        this.impactSatisfaction = impactSatisfaction;
    }

    public BaseLearnedLesson(Integer idLearnedLesson){
        this.idLearnedLesson = idLearnedLesson;
    }

    /**
     * Getter for property 'program'.
     *
     * @return Value for property 'program'.
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Setter for property 'program'.
     *
     * @param program Value to set for property 'program'.
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Getter for property 'customer'.
     *
     * @return Value for property 'customer'.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Setter for property 'customer'.
     *
     * @param customer Value to set for property 'customer'.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Getter for property 'seller'.
     *
     * @return Value for property 'seller'.
     */
    public Seller getSeller() {
        return seller;
    }

    /**
     * Setter for property 'seller'.
     *
     * @param seller Value to set for property 'seller'.
     */
    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    /**
     * Getter for property 'geography'.
     *
     * @return Value for property 'geography'.
     */
    public Geography getGeography() {
        return geography;
    }

    /**
     * Setter for property 'geography'.
     *
     * @param geography Value to set for property 'geography'.
     */
    public void setGeography(Geography geography) {
        this.geography = geography;
    }

    /**
     * Getter for property 'contracttype'.
     *
     * @return Value for property 'contracttype'.
     */
    public Contracttype getContracttype() {
        return contracttype;
    }

    /**
     * Setter for property 'contracttype'.
     *
     * @param contracttype Value to set for property 'contracttype'.
     */
    public void setContracttype(Contracttype contracttype) {
        this.contracttype = contracttype;
    }

    /**
     * Getter for property 'fundingSource'.
     *
     * @return Value for property 'fundingSource'.
     */
    public Fundingsource getFundingSource() {
        return fundingSource;
    }

    /**
     * Setter for property 'fundingSource'.
     *
     * @param fundingSource Value to set for property 'fundingSource'.
     */
    public void setFundingSource(Fundingsource fundingSource) {
        this.fundingSource = fundingSource;
    }

    /**
     * Getter for property 'processGroup'.
     *
     * @return Value for property 'processGroup'.
     */
    public String getProcessGroup() {
        return processGroup;
    }

    /**
     * Setter for property 'processGroup'.
     *
     * @param processGroup Value to set for property 'processGroup'.
     */
    public void setProcessGroup(String processGroup) {
        this.processGroup = processGroup;
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for property 'type'.
     *
     * @param type Value to set for property 'type'.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for property 'idLearnedLesson'.
     *
     * @return Value for property 'idLearnedLesson'.
     */
    public Integer getIdLearnedLesson() {
        return idLearnedLesson;
    }

    /**
     * Setter for property 'idLearnedLesson'.
     *
     * @param idLearnedLesson Value to set for property 'idLearnedLesson'.
     */
    public void setIdLearnedLesson(Integer idLearnedLesson) {
        this.idLearnedLesson = idLearnedLesson;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property 'description'.
     *
     * @return Value for property 'description'.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property 'knowledgeArea'.
     *
     * @return Value for property 'knowledgeArea'.
     */
    public KnowledgeArea getKnowledgeArea() {
        return knowledgeArea;
    }

    /**
     * Setter for property 'knowledgeArea'.
     *
     * @param knowledgeArea Value to set for property 'knowledgeArea'.
     */
    public void setKnowledgeArea(KnowledgeArea knowledgeArea) {
        this.knowledgeArea = knowledgeArea;
    }

    /**
     * Getter for property 'profile'.
     *
     * @return Value for property 'profile'.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Setter for property 'profile'.
     *
     * @param profile Value to set for property 'profile'.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Getter for property 'company'.
     *
     * @return Value for property 'company'.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Setter for property 'company'.
     *
     * @param company Value to set for property 'company'.
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Getter for property 'actionsLesson'.
     *
     * @return Value for property 'actionsLesson'.
     */
    public Set<ActionLesson> getActionsLesson() {
        return actionsLesson;
    }

    /**
     * Setter for property 'actionsLesson'.
     *
     * @param actionsLesson Value to set for property 'actionsLesson'.
     */
    public void setActionsLesson(Set<ActionLesson> actionsLesson) {
        this.actionsLesson = actionsLesson;
    }

    /**
     * Getter for property 'recsLesson'.
     *
     * @return Value for property 'recsLesson'.
     */
    public Set<RecommendationLesson> getRecsLesson() {
        return recsLesson;
    }

    /**
     * Setter for property 'recsLesson'.
     *
     * @param recsLesson Value to set for property 'recsLesson'.
     */
    public void setRecsLesson(Set<RecommendationLesson> recsLesson) {
        this.recsLesson = recsLesson;
    }


}
