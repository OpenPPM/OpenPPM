/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This project has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this project. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: front
 * File: ProjectResource.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 13:37:01
 */

package es.sm2.openppm.api.rest.project;

import es.sm2.openppm.api.converter.project.ProjectDTOConverter;
import es.sm2.openppm.api.model.project.ProjectDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
@Path("/project")

@Produces({MediaType.APPLICATION_JSON})
public class ProjectResource extends BaseResource {

    /**
     * Find all by user and filters
     *
     * @return
     */
    @GET
    public Response find(@QueryParam(value = "projectName") String projectName,
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
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_FM,
                Constants.ROLE_IM, Constants.ROLE_SPONSOR, Constants.ROLE_ADMIN);

        if (response == null) {

            try {

                // Search Object
                ProjectSearch searchParams = new ProjectSearch(getUser(), SettingUtil.getSettings(getCompany()));

                searchParams.setProjectName(projectName);
                searchParams.setCustomertypes(customerTypes);
                searchParams.setCustomers(customers);
                searchParams.setPrograms(programs);
                searchParams.setCategories(categories);
                searchParams.setEmployeeByProjectManagers(projectManagers);
                searchParams.setEmployeeByFunctionalManagers(functionalManagers);
                searchParams.setSellers(sellers);
                searchParams.setLabels(labels);
                searchParams.setStageGates(stageGates);
                searchParams.setContractTypes(contractTypes);
                searchParams.setGeography(geographies);
                searchParams.setClassificationsLevel(classificationLevels);
                searchParams.setTechnologies(technologies);
                searchParams.setEmployeeBySponsors(sponsors);
                searchParams.setFundingsources(fundingSources);
                searchParams.setPerformingorgs(performingOrganizations);
                searchParams.setIsGeoSelling(isGeoSelling);
                searchParams.setRag(rag);
                searchParams.setFirstPriority(minPriority);
                searchParams.setLastPriority(maxPriority);
                searchParams.setIsIndirectSeller(isIndirectSeller);
                searchParams.setFirstRiskRating(minRiskRating);
                searchParams.setLastRiskRating(maxRiskRating);
                searchParams.setIncludingAdjustament(includingAdjustment);
                searchParams.setBudgetYear(budgetYear);
                searchParams.setInternalProject(internalProject);
                searchParams.setSince(formatDate(since));
                searchParams.setUntil(formatDate(until));
                searchParams.setStatus(status);

                // Not include disable projects
                searchParams.setIncludeDisabled(false);

                // Find projects
                ProjectLogic projectLogic = new ProjectLogic(null);
                List<Project> projects = projectLogic.find(searchParams, true);

                LogManager.getLog(getClass()).info("Find all Projects");

                // Send response
                ProjectDTOConverter projectDTOConverter = new ProjectDTOConverter();
                response = Response.ok().entity(projectDTOConverter.toDTOList(projects)).build();
            }
            catch (Exception e) {

                response = getErrorResponse(LogManager.getLog(getClass()), "find()", e);
            }
        }

        return response;
    }

    /**
     * Find all by po
     *
     * @return
     */
    @GET
    @Path("{CODE}")
    public Response findByID(@PathParam("CODE") Integer idProject) {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO, Constants.ROLE_PM, Constants.ROLE_FM,
                Constants.ROLE_IM, Constants.ROLE_SPONSOR, Constants.ROLE_PROGM);

        if (response == null) {

            try {

                // Declare logic
                ProjectLogic projectLogic = new ProjectLogic(null);

                // Joins
                List<String> joins = new ArrayList<String>();
                joins.add(Project.PERFORMINGORG);
                joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
                joins.add(Project.EMPLOYEEBYPROJECTMANAGER + "." + Employee.CONTACT);
                joins.add(Project.PROGRAM);
                joins.add(Project.PROGRAM + "." + Program.EMPLOYEE);
                joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
                joins.add(Project.CUSTOMER);
                joins.add(Project.GEOGRAPHY);
                joins.add(Project.CONTRACTTYPE);
                joins.add(Project.EMPLOYEEBYSPONSOR);
                joins.add(Project.EMPLOYEEBYSPONSOR + "." + Employee.CONTACT);

                // Logic Find projects
                Project project = projectLogic.findById(idProject, joins);

                LogManager.getLog(this.getClass()).info("Find Project");

                // Converter model object to dto object
                //
                ProjectDTOConverter projectDTOConverter = new ProjectDTOConverter();

                ProjectDTO projectDTO = projectDTOConverter.toDTO(project);

                // Constructor response
                response = Response.ok().entity(projectDTO).build();
            }
            catch (Exception e) {

                response = getErrorResponse(LogManager.getLog(getClass()), "findByID()", e);
            }
        }

        return response;
    }
}
