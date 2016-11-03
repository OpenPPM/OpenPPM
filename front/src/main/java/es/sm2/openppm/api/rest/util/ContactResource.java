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
 * File: ContactResource.java
 * Create User: javier.hernandez
 * Create Date: 15/09/2015 15:04:14
 */

package es.sm2.openppm.api.rest.util;

import es.sm2.openppm.api.converter.maintenance.ContactDTOConverter;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.logic.impl.ContactLogic;
import es.sm2.openppm.core.logic.impl.ResourcepoolLogic;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource to handle notifications.
 */
@Path("/util/contact")
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource extends BaseResource {

    /**
     * List of contacts
     *
     * @param query
     * @return
     * @throws RestServiceException
     */
	@GET
    public Response getContacts(@QueryParam(value = "query") String query,
                                @QueryParam(value = "codePerformingOrganization") Integer codePerformingOrganization,
                                @QueryParam(value = "codePool") Integer codePool,
                                @QueryParam(value = "codeSkill") List<Integer> codeSkill,
                                @QueryParam(value = "codeJobCategory") List<Integer> codeJobCategory,
                                @QueryParam(value = "usedInLLAA") Boolean usedInLLAA) throws RestServiceException {

        // Check permission for user logged
        isUserLogged(
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.ADMIN,
                Resourceprofiles.Profile.LOGISTIC,
                Resourceprofiles.Profile.RESOURCE_MANAGER,
                Resourceprofiles.Profile.PROJECT_MANAGER,
                Resourceprofiles.Profile.PROGRAM_MANAGER
        );


        Response response = null;

        try {

            // Find contacts
            ContactLogic contactLogic = new ContactLogic();

                // Logic find by filters
                List<Contact> contacts;

                if (usedInLLAA != null && usedInLLAA) {
                    contacts = contactLogic.findUsedInLLAA(getCompany());
                }
                else {

                    List<Resourcepool> resourcepools = new ArrayList<Resourcepool>();

                    // Get pool by filter
                    if (codePool != null) {
                        resourcepools.add(new Resourcepool(codePool));
                    }
                    // Get all pools by role
                    else {

                        ResourcepoolLogic resourcepoolLogic = new ResourcepoolLogic();

                        resourcepools.addAll(resourcepoolLogic.findByRole(getUser()));
                    }

                    // Get skills
                    //
                    List<Skill> skills = null;

                    if (ValidateUtil.isNotNull(codeSkill)) {

                        skills = new ArrayList<Skill>();

                        for (Integer idSkill : codeSkill) {
                            skills.add(new Skill(idSkill));
                        }
                    }

                    // Get job categories
                    //
                    List<Jobcategory> jobcategories = null;

                    if (ValidateUtil.isNotNull(codeJobCategory)) {

                        jobcategories = new ArrayList<Jobcategory>();

                        for (Integer idJobCategory : codeJobCategory) {
                            jobcategories.add(new Jobcategory(idJobCategory));
                        }
                    }

                    contacts = contactLogic.searchContacts(query, null, new Performingorg(codePerformingOrganization),
                            getCompany(), resourcepools, skills, jobcategories);
                }

            // Convert to DTO and send Data
            ContactDTOConverter converter = new ContactDTOConverter();
            response = Response.ok(converter.toDTOList(contacts)).build();
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "getContacts()", e);
        }

        return response;
	}

}
