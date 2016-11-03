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
 * File: LearnedLessonDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 12:18:59
 */

package es.sm2.openppm.api.converter.project;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.*;
import es.sm2.openppm.core.model.impl.ActionLesson;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.KnowledgeArea;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Profile;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.RecommendationLesson;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class LearnedLessonDTOConverter extends AbstractConverter<LearnedLessonDTO, LearnedLesson> {


    private Employee user;
    private Company company;

    public LearnedLessonDTOConverter() {
        super();
    }

    public LearnedLessonDTOConverter(Employee user, Company company) {
        this.user = user;
        this.company = company;
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
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public LearnedLessonDTO toDTO(LearnedLesson model) {

        LearnedLessonDTO lessonDTO = getInstanceDTO(model, LearnedLessonDTO.class);

        // Learned lesson data
        //
        lessonDTO.setCode(model.getIdLearnedLesson());

        // No send data
        lessonDTO.setImpactCost(null);
        lessonDTO.setImpactTime(null);
        lessonDTO.setImpactSatisfaction(null);

        // Generateds
        if (ValidateUtil.isNotNull(model.getLearnedLessonProjects())) {

            // There will be only one
            for (LearnedLessonProject projectGenLesson : model.getLearnedLessonProjects()) {

                Project project = projectGenLesson.getProject();

                if (project != null) {

                    // Project data
                    //
                    ProjectDTO projectDTO = new ProjectDTO();
                    projectDTO.setCode(project.getIdProject());
                    projectDTO.setName(project.getProjectName());
                    projectDTO.setStatus(project.getStatus());

                    if (project.getEmployeeByProjectManager() != null && project.getEmployeeByProjectManager().getContact() != null) {

                        // Project Manager data
                        projectDTO.setProjectManager(new EntityDTO(project.getEmployeeByProjectManager().getIdEmployee(),
                                project.getEmployeeByProjectManager().getContact().getFullName()));
                        lessonDTO.setProject(projectDTO);
                    }
                }
            }
        }

        // Useds
        if (ValidateUtil.isNotNull(model.getLearnedLessonLinks())) {
            lessonDTO.setRanking(model.getLearnedLessonLinks().size());
        } else {
            lessonDTO.setRanking(0);
        }

        // Owner Data
        //
        if(model.getContact() != null) {

            EntityDTO owner = new EntityDTO();

            owner.setName(model.getContact().getFullName());

            lessonDTO.setOwner(owner);
        }

        // Knowledge Area data
        //
        if (model.getKnowledgeArea() != null) {

            EntityDTO knowledgeArea = new EntityDTO();

            knowledgeArea.setName(model.getKnowledgeArea().getName());

            lessonDTO.setKnowledgeArea(knowledgeArea);
        }

        // Program data
        if(model.getProgram() != null){

            ProgramDTO program = new ProgramDTO();

            program.setCode(model.getProgram().getIdProgram());

            lessonDTO.setProgram(program);
        }

        // Action lesson data
        //
        List<ActionLessonDTO> actionsDTO = new ArrayList<ActionLessonDTO>();

        if (ValidateUtil.isNotNull(model.getActionsLesson())) {

            for (ActionLesson actionLesson : model.getActionsLesson()) {
                actionsDTO.add(new ActionLessonDTO());
            }
        }

        lessonDTO.setActions(actionsDTO);


        // Recommendation lesson data
        //
        List<RecommendationLessonDTO> recsDTO = new ArrayList<RecommendationLessonDTO>();

        if (ValidateUtil.isNotNull(model.getRecsLesson())) {

            for (RecommendationLesson recommendationLesson : model.getRecsLesson()) {
                recsDTO.add(new RecommendationLessonDTO());
            }
        }

        lessonDTO.setRecommendations(recsDTO);

        return lessonDTO;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    @Override
    public LearnedLesson toModel(LearnedLessonDTO dto) {

        LearnedLesson model = getInstanceModel(dto, LearnedLesson.class);

        // Set id
        model.setIdLearnedLesson(dto.getCode());

        // Set relations
        //
        model.setCompany(getCompany());
        model.setContact(new Contact(getUser().getContact().getIdContact()));

        if (dto.getProfile() != null) {
            model.setProfile(new Profile(dto.getProfile().getCode()));
        }

        if (dto.getCustomer() != null) {
            model.setCustomer(new Customer(dto.getCustomer().getCode()));
        }

        if (dto.getSeller() != null) {
            model.setSeller(new Seller(dto.getSeller().getCode()));
        }

        if (dto.getContractType() != null) {
            model.setContracttype(new Contracttype(dto.getContractType().getCode()));
        }

        if (dto.getGeography() != null) {
            model.setGeography(new Geography(dto.getGeography().getCode()));
        }

        if (dto.getFundingSource() != null) {
            model.setFundingSource(new Fundingsource(dto.getFundingSource().getCode()));
        }

        if (dto.getKnowledgeArea() != null) {
            model.setKnowledgeArea(new KnowledgeArea(dto.getKnowledgeArea().getCode()));
        }

        if (dto.getProgram() != null) {
            model.setProgram(new Program(dto.getProgram().getCode()));
        }

        if (dto.getProject() != null) {

            Set<LearnedLessonProject> learnedLessonProjects = new HashSet<LearnedLessonProject>(0);

            LearnedLessonProjectDTO lessonProjectDTO = new LearnedLessonProjectDTO();

            lessonProjectDTO.setProject(dto.getProject());

            // Converter
            //
            LearnedLessonProjectDTOConverter lessonProjectDTOConverter = new LearnedLessonProjectDTOConverter();

            learnedLessonProjects.add(lessonProjectDTOConverter.toModel(lessonProjectDTO));

            model.setLearnedLessonProjects(learnedLessonProjects);
        }

        return model;
    }


}
