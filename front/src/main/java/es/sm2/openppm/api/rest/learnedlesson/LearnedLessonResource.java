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
 * File: LessonsLearnedRest.java
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 12:26:04
 */

package es.sm2.openppm.api.rest.learnedlesson;

import es.sm2.openppm.api.converter.project.LearnedLessonDTOConverter;
import es.sm2.openppm.api.converter.project.LearnedLessonDetailDTOConverter;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.project.LearnedLessonDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ConfigurationLogic;
import es.sm2.openppm.core.logic.impl.LearnedLessonLinkLogic;
import es.sm2.openppm.core.logic.impl.LearnedLessonLogic;
import es.sm2.openppm.core.logic.impl.LearnedLessonProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.search.LearnedLessonSearch;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.core.utils.ResourceBoundleUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@Path("/learnedlesson")

@Produces({MediaType.APPLICATION_JSON})
public class LearnedLessonResource extends BaseResource {

    /**
     * Update
     *
     * @return
     */
    @PUT
    @Path("{CODE}")
    public Response update(@PathParam("CODE") Integer idLearnedLesson, LearnedLessonDTO lessonDTO) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        try {

            if (response == null) {

                // Converter dto to model
                //
                LearnedLessonDTOConverter learnedLessonDTOConverter = new LearnedLessonDTOConverter(getUser(), getCompany());

                LearnedLesson learnedLesson = learnedLessonDTOConverter.toModel(lessonDTO);

                // Set id
                learnedLesson.setIdLearnedLesson(idLearnedLesson);

                // Declare logic
                LearnedLessonLogic learnedLessonLogic = new LearnedLessonLogic();

                // Logic save
                learnedLessonLogic.save(learnedLesson);

                LogManager.getLog(getClass()).info("Learned Lesson saved");

                response = Response.ok().build();
            }
        }
        catch (Exception e) {
            response = getErrorResponse(LogManager.getLog(getClass()), "update()", e);
        }

        return response;
    }

    /**
     * Create new
     *
     * @return
     */
    @POST
    public Response save(LearnedLessonDTO lessonDTO) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        try {

            if (response == null) {

                // Converter dto to model
                //
                LearnedLessonDTOConverter learnedLessonDTOConverter = new LearnedLessonDTOConverter(getUser(), getCompany());

                LearnedLesson learnedLesson = learnedLessonDTOConverter.toModel(lessonDTO);

                // Declare logic
                LearnedLessonLogic learnedLessonLogic = new LearnedLessonLogic();

                // Logic save
                learnedLesson = learnedLessonLogic.save(learnedLesson);

                // Logic save specific is you have project
                //
                if (lessonDTO.getProject() != null && lessonDTO.getProject().getCode() != null) {

                    LearnedLessonProjectLogic learnedLessonProjectLogic = new LearnedLessonProjectLogic();

                    // Set data
                    LearnedLessonProject learnedLessonProject = new LearnedLessonProject();
                    learnedLessonProject.setProject(new Project(lessonDTO.getProject().getCode()));
                    learnedLessonProject.setLearnedLesson(learnedLesson);

                    learnedLessonProjectLogic.save(learnedLessonProject);
                }

                LogManager.getLog(getClass()).info("Learned Lesson saved");

                // Constructor response
                response = Response.ok().entity(learnedLessonDTOConverter.toDTO(learnedLesson)).build();
            }
        }
        catch (Exception e) {
            response = getErrorResponse(LogManager.getLog(getClass()), "save()", e);
        }

        return response;
    }

    /**
     * Find by LLAA and project filters
     *
     * @return
     */
    @GET
    public Response find(@QueryParam(value = "llaaQuery") String llaaQuery,
                         @QueryParam(value = "llaaIncludeGlobal")Boolean llaaIncludeGlobal,
                         @QueryParam(value = "llaaPrograms") List<Integer> llaaPrograms,
                         @QueryParam(value = "llaaCustomers") List<Integer> llaaCustomers,
                         @QueryParam(value = "llaaSellers") List<Integer> llaaSellers,
                         @QueryParam(value = "llaaGeographies") List<Integer> llaaGeographicAreas,
                         @QueryParam(value = "llaaCustomerTypes") List<Integer> llaaCustomerTypes,
                         @QueryParam(value = "llaaFundingSources") List<Integer> llaaFundingSources,
                         @QueryParam(value = "llaaProcessGroups") List<String> llaaProcessGroups,
                         @QueryParam(value = "llaaKnowledgeAreas") List<Integer> llaaKnowledgeArea,
                         @QueryParam(value = "llaaImportanceActions") List<String> llaaImportanceActions,
                         @QueryParam(value = "llaaImportanceRecommendations") List<String> llaaImportanceRecommendation,
                         @QueryParam(value = "llaaOwner") List<Integer> llaaOwner,
                         @QueryParam(value = "llaaJobProfile") List<Integer> llaaJobProfile,
                         @QueryParam(value = "llaaType") List<String> llaaType,
                         @QueryParam(value = "llaaImpactSatisfaction") List<String> llaaImpactSatisfaction,
                         @QueryParam(value = "llaaInternalProject") Boolean llaaInternalProject,
                         @QueryParam(value = "llaaIsGeoSelling") Boolean llaaIsGeoSelling,
                         @QueryParam(value = "llaaRag") String llaaRag,
                         @QueryParam(value = "llaaMaxImpactTime") BigDecimal llaaMaxImpactTime,
                         @QueryParam(value = "llaaMinImpactTime") BigDecimal llaaMinImpactTime,
                         @QueryParam(value = "llaaMinImpactCost") BigDecimal llaaMinImpactCost,
                         @QueryParam(value = "llaaMaxImpactCost") BigDecimal llaaMaxImpactCost,
                         @QueryParam(value = "llaaMaxRanking") BigDecimal llaaMaxRanking,
                         @QueryParam(value = "llaaMinRanking") BigDecimal llaaMinRanking,
                         @QueryParam(value = "llaaSince") String llaaSince,
                         @QueryParam(value = "llaaUntil") String llaaUntil,
                         // Filters for Project
                         @QueryParam(value = "projectName") String projectName,
                         @QueryParam(value = "customerTypes") List<Integer> customerTypes,
                         @QueryParam(value = "customers") List<Integer> customers,
                         @QueryParam(value = "programs") List<Integer> programs,
                         @QueryParam(value = "categories") List<Integer> categories,
                         @QueryParam(value = "projectManagers") List<Integer> projectManagers,
                         @QueryParam(value = "functionalManagers") List<Integer> functionalManagers,
                         @QueryParam(value = "sellers") List<Integer> sellers,
                         @QueryParam(value = "labels") List<Integer> labels,
                         @QueryParam(value = "stageGates") List<Integer> stageGates,
                         @QueryParam(value = "contractTypes") List<Integer> contractTypes,
                         @QueryParam(value = "geographies") List<Integer> geographies,
                         @QueryParam(value = "classificationLevels") List<Integer> classificationLevels,
                         @QueryParam(value = "technologies") List<Integer> technologies,
                         @QueryParam(value = "sponsors") List<Integer> sponsors,
                         @QueryParam(value = "fundingSources") List<Integer> fundingSources,
                         @QueryParam(value = "performingOrganizations") List<Integer> performingOrganizations,
                         @QueryParam(value = "status") List<String> status,
                         @QueryParam(value = "isGeoSelling") Boolean isGeoSelling,
                         @QueryParam(value = "rag") String rag,
                         @QueryParam(value = "minPriority") Integer minPriority,
                         @QueryParam(value = "maxPriority") Integer maxPriority,
                         @QueryParam(value = "isIndirectSeller") Boolean isIndirectSeller,
                         @QueryParam(value = "minRiskRating") Integer minRiskRating,
                         @QueryParam(value = "maxRiskRating") Integer maxRiskRating,
                         @QueryParam(value = "includingAdjustment") Boolean includingAdjustment,
                         @QueryParam(value = "budgetYear") Integer budgetYear,
                         @QueryParam(value = "internalProject") Boolean internalProject,
                         @QueryParam(value = "since") String since,
                         @QueryParam(value = "until") String until) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                List<LearnedLesson> filteredLessons = new ArrayList<LearnedLesson>();

                // Search LLAA Object
                LearnedLessonSearch searchParams = new LearnedLessonSearch(getUser(), SettingUtil.getSettings(getCompany()));

                // Filters for llaa
                searchParams.setLlaaQuery(llaaQuery);
                searchParams.setLlaaIncludeGlobal(llaaIncludeGlobal);
                searchParams.setLlaaPrograms(llaaPrograms);
                searchParams.setLlaaCustomers(llaaCustomers);
                searchParams.setLlaaSellers(llaaSellers);
                searchParams.setLlaaGeographicAreas(llaaGeographicAreas);
                searchParams.setLlaaCustomerTypes(llaaCustomerTypes);
                searchParams.setLlaaFundingSources(llaaFundingSources);
                searchParams.setLlaaProcessGroup(llaaProcessGroups);
                searchParams.setLlaaKnowledgeArea(llaaKnowledgeArea);
                searchParams.setLlaaImportanceActions(llaaImportanceActions);
                searchParams.setLlaaImportanceRecommendation(llaaImportanceRecommendation);
                searchParams.setLlaaOwner(llaaOwner);
                searchParams.setLlaaJobProfile(llaaJobProfile);
                searchParams.setLlaaType(llaaType);
                searchParams.setLlaaImpactSatisfaction(llaaImpactSatisfaction);
                searchParams.setLlaaInternalProject(llaaInternalProject);
                searchParams.setLlaaIsGeoSelling(llaaIsGeoSelling);
                searchParams.setLlaaRag(llaaRag);
                searchParams.setLlaaMaxImpactTime(llaaMaxImpactTime);
                searchParams.setLlaaMinImpactTime(llaaMinImpactTime);
                searchParams.setLlaaMaxImpactCost(llaaMaxImpactCost);
                searchParams.setLlaaMinImpactCost(llaaMinImpactCost);
                searchParams.setLlaaMaxRanking(llaaMaxRanking);
                searchParams.setLlaaMinRanking(llaaMinRanking);
                searchParams.setLlaaSince(formatDate(llaaSince));
                searchParams.setLlaaUntil(formatDate(llaaUntil));

                // Declare logic
                LogManager.getLog(getClass()).info("Find Learned Lessons");
                LearnedLessonLogic learnedLessonLogic = new LearnedLessonLogic();
                List<LearnedLesson> lessons = learnedLessonLogic.findGenerated(searchParams);

                // Search Project Object
                ProjectSearch searchParamsProject = new ProjectSearch(getUser(), SettingUtil.getSettings(getCompany()));

                if (ValidateUtil.isNotNull(projectName) || ValidateUtil.isNotNull(customerTypes) || ValidateUtil.isNotNull(customers) ||
                        ValidateUtil.isNotNull(programs) ||  ValidateUtil.isNotNull(categories) || ValidateUtil.isNotNull(projectManagers) ||
                        ValidateUtil.isNotNull(functionalManagers) || ValidateUtil.isNotNull(sellers) || ValidateUtil.isNotNull(labels) ||
                        ValidateUtil.isNotNull(stageGates) || ValidateUtil.isNotNull(contractTypes) || ValidateUtil.isNotNull(geographies) ||
                        ValidateUtil.isNotNull(classificationLevels) || ValidateUtil.isNotNull(technologies) || ValidateUtil.isNotNull(sponsors) ||
                        ValidateUtil.isNotNull(fundingSources) || ValidateUtil.isNotNull(performingOrganizations) || isGeoSelling != null || ValidateUtil.isNotNull(rag) ||
                        minPriority != null || maxPriority != null || isIndirectSeller != null || minRiskRating != null || maxRiskRating != null || includingAdjustment != null ||
                        budgetYear != null || internalProject != null || since != null || until != null || ValidateUtil.isNotNull(status)) {

                    // Filters by project
                    searchParamsProject.setProjectName(projectName);
                    searchParamsProject.setCustomertypes(customerTypes);
                    searchParamsProject.setCustomers(customers);
                    searchParamsProject.setPrograms(programs);
                    searchParamsProject.setCategories(categories);
                    searchParamsProject.setEmployeeByProjectManagers(projectManagers);
                    searchParamsProject.setEmployeeByFunctionalManagers(functionalManagers);
                    searchParamsProject.setSellers(sellers);
                    searchParamsProject.setLabels(labels);
                    searchParamsProject.setStageGates(stageGates);
                    searchParamsProject.setContractTypes(contractTypes);
                    searchParamsProject.setGeography(geographies);
                    searchParamsProject.setClassificationsLevel(classificationLevels);
                    searchParamsProject.setTechnologies(technologies);
                    searchParamsProject.setEmployeeBySponsors(sponsors);
                    searchParamsProject.setFundingsources(fundingSources);
                    searchParamsProject.setPerformingorgs(performingOrganizations);
                    searchParamsProject.setIsGeoSelling(isGeoSelling);
                    searchParamsProject.setRag(rag);
                    searchParamsProject.setFirstPriority(minPriority);
                    searchParamsProject.setLastPriority(maxPriority);
                    searchParamsProject.setIsIndirectSeller(isIndirectSeller);
                    searchParamsProject.setFirstRiskRating(minRiskRating);
                    searchParamsProject.setLastRiskRating(maxRiskRating);
                    searchParamsProject.setIncludingAdjustament(includingAdjustment);
                    searchParamsProject.setBudgetYear(budgetYear);
                    searchParamsProject.setInternalProject(internalProject);
                    searchParamsProject.setSince(formatDate(since));
                    searchParamsProject.setUntil(formatDate(until));
                    searchParamsProject.setStatus(status);

                    // Not include disable projects
                    searchParamsProject.setIncludeDisabled(false);

                    // Find projects
                    ProjectLogic projectLogic = new ProjectLogic(null);
                    List<Project> projects = projectLogic.find(searchParamsProject, false);

                    // Filter by project
                    if (ValidateUtil.isNotNull(projects)) {

                        for (LearnedLesson lesson : lessons) {

                            if (ValidateUtil.isNotNull(lesson.getLearnedLessonProjects())) {

                                boolean isAdded = false;
                                for (LearnedLessonProject learnedLessonProject : lesson.getLearnedLessonProjects()) { // Only one

                                    int i = 0;
                                    while (i < projects.size() && !isAdded) {

                                        Project project = projects.get(i);

                                        if (project.equals(learnedLessonProject.getProject())) {

                                            filteredLessons.add(lesson);

                                            isAdded = true;
                                        }

                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    filteredLessons = lessons;
                }

                // Save configurations
                ConfigurationLogic configurationLogic = new ConfigurationLogic();
                configurationLogic.saveFiltersLearnedLesson(searchParams, searchParamsProject);

                // Converter model object to dto object and send data
                LearnedLessonDTOConverter learnedLessonDTOConverter = new LearnedLessonDTOConverter();
                response = Response.ok().entity(learnedLessonDTOConverter.toDTOList(filteredLessons)).build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "find()", e);
            }
        }

        return response;
    }

    /**
     * Find learned lesson generic
     *
     * @param idLearnedLesson
     * @return
     */
    @GET
    @Path("{CODE}")
    public Response findByID(@PathParam("CODE") Integer idLearnedLesson) {

        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM,  Constants.ROLE_PROGM);

        try {

            // Find lessons learned
            LearnedLesson learnedLesson = getLearnedLesson(idLearnedLesson);

            // Check permissions
            //


            if (response == null) {

                LogManager.getLog(this.getClass()).info("Find Learned Lesson");

                if (learnedLesson != null) {

                    // Converter model object to dto object
                    //
                    LearnedLessonDetailDTOConverter lessonDetailDTOConverter = new LearnedLessonDetailDTOConverter();

                    LearnedLessonDTO learnedLessonDTO = lessonDetailDTOConverter.toDTODetail(learnedLesson);

                    // Constructor response
                    response = Response.ok().entity(learnedLessonDTO).build();
                }
                else {
                    throw new RestServiceException("No data");
                }
            }
        }
        catch (Exception e) {
            response = getErrorResponse(LogManager.getLog(getClass()), "findByID()", e);
        }

        return response;
    }

    /**
     * Find lessons learned by id
     *
     * @param idLearnedLesson
     * @return
     * @throws Exception
     */
    private LearnedLesson getLearnedLesson(Integer idLearnedLesson) throws Exception {

        // Declare logic
        LearnedLessonLogic learnedLessonLogic = new LearnedLessonLogic();

        // Joins
        List<String> joins = new ArrayList<String>();

        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS);
        joins.add(LearnedLesson.LEARNEDLESSONLINKS);
        joins.add(LearnedLesson.ACTIONSLESSON);
        joins.add(LearnedLesson.RECSLESSON);
        joins.add(LearnedLesson.PROFILE);
        joins.add(LearnedLesson.PROGRAM);
        joins.add(LearnedLesson.CUSTOMER);
        joins.add(LearnedLesson.CONTACT);
        joins.add(LearnedLesson.SELLER);
        joins.add(LearnedLesson.GEOGRAPHY);
        joins.add(LearnedLesson.CONTRACTTYPE);
        joins.add(LearnedLesson.FUNDINGSOURCE);
        joins.add(LearnedLesson.KNOWLEDGEAREA);

        // Project joins
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER + "." +
                Employee.CONTACT);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.PERFORMINGORG);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.PROGRAM);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.PROGRAM + "." + Program.EMPLOYEE);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYSPONSOR);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYSPONSOR + "." + Employee.CONTACT);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.CUSTOMER);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.GEOGRAPHY);
        joins.add(LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.CONTRACTTYPE);

        //Logic
        return learnedLessonLogic.findGenerated(idLearnedLesson, joins);
    }

    /**
     * Remove
     *
     * @param code
     * @return
     */
    @DELETE
    @Path("{CODE}")
    public Response remove(@PathParam("CODE") Integer code) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Declare logic
                LearnedLessonLogic learnedLessonLogic = new LearnedLessonLogic();

                // Logic
                learnedLessonLogic.delete(learnedLessonLogic.findById(code));

                LogManager.getLog(this.getClass()).info("Learned Lesson deleted");

                // Constructor response
                response = Response.ok().entity(new EntityDTO(code)).build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "remove()", e);
            }
        }

        return response;
    }

    /**
     * Save as used learned lesson
     *
     * @return
     */
    @PUT
    @Path("{CODE}/project/{CODEPROJECT}")
    public Response linked(@PathParam("CODE") Integer code, @PathParam("CODEPROJECT") Integer codeProject) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM);

        if (response == null) {

            try {
                // Declare logics
                LearnedLessonLinkLogic learnedLessonLinkLogic       = new LearnedLessonLinkLogic();
                LearnedLessonProjectLogic learnedLessonProjectLogic = new LearnedLessonProjectLogic();

                // Validations
                //

                // Validation repeat
                List<LearnedLessonLink> linkeds = learnedLessonLinkLogic.findByProject(new Project(codeProject));

                if (ValidateUtil.isNotNull(linkeds)) {

                    for (LearnedLessonLink linked : linkeds) {

                        if (linked.getLearnedLesson().getIdLearnedLesson().equals(code)) {

                            String error = ResourceBoundleUtil.getInstance().value(getResourceBundle(), "VIEW.LEARNED_LESSON.ERROR.ALREADY_LINKED",
                                    code, linked.getLearnedLesson().getName(),
                                    codeProject, linked.getProject().getProjectName());

                            throw new RestServiceException(error);
                        }
                    }
                }

                // Validation same
                LearnedLessonProject learnedLessonProject = learnedLessonProjectLogic.findByProjectAndLA(new Project(codeProject), new LearnedLesson(code));

                if (learnedLessonProject != null) {

                    String error = ResourceBoundleUtil.getInstance().value(getResourceBundle(), "VIEW.LEARNED_LESSON.ERROR.SAME_PROJECT_LINKED",
                            code, learnedLessonProject.getLearnedLesson().getName(),
                            codeProject, learnedLessonProject.getProject().getProjectName());

                    throw new RestServiceException(error);
                }


                LearnedLessonLink learnedLessonLink = new LearnedLessonLink();

                // Set relations
                learnedLessonLink.setLearnedLesson(new LearnedLesson(code));

                learnedLessonLink.setProject(new Project(codeProject));

                // Declare logic and save
                learnedLessonLinkLogic.save(learnedLessonLink);

                LogManager.getLog(getClass()).info("Learned lesson link saved");

                // Find lessons learned and send data
                LearnedLesson learnedLesson = getLearnedLesson(code);
                LearnedLessonDTOConverter lessonDetailDTOConverter = new LearnedLessonDetailDTOConverter();
                response = Response.ok().entity(lessonDetailDTOConverter.toDTO(learnedLesson)).build();

            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "linked()", e);
            }
        }

        return response;
    }

}
