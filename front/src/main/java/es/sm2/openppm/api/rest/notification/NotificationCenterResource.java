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
 * File: NotificationCenterResource.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 19:18:19
 */

package es.sm2.openppm.api.rest.notification;

import es.sm2.openppm.api.converter.notification.NotificationDTOConverter;
import es.sm2.openppm.api.model.notification.NotificationDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.logic.impl.ContactnotificationLogic;
import es.sm2.openppm.core.logic.impl.NotificationLogic;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.search.NotificationSearch;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Resource to handle notifications.
 */
@Path("/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationCenterResource extends BaseResource {

    /**
     * List of notifications of user by filters
     *
     * @param since
     * @param until
     * @param subject
     * @param body
     * @param status
     * @param type
     * @param includeRead
     * @return
     * @throws RestServiceException
     */
	@GET
    public Response getNotifications(@QueryParam(value = "since") Date since,
                                     @QueryParam(value = "until") Date until,
                                     @QueryParam(value = "subject") String subject,
                                     @QueryParam(value = "body") String body,
                                     @QueryParam(value = "status") String status,
                                     @QueryParam(value = "includeRead") boolean includeRead) throws RestServiceException {

        // Check permission for user logged
        Response response = checkPermission();

        if (response == null) {

            try {

                // Add filters
                NotificationSearch notificationSearch = new NotificationSearch(getUser().getContact());
                notificationSearch.setSince(since);
                notificationSearch.setUntil(until);
                notificationSearch.setSubject(subject);
                notificationSearch.setBody(body);
                notificationSearch.setStatus(status);
                notificationSearch.setIncludeRead(includeRead);

                // Find notifications
                NotificationLogic notificationLogic = new NotificationLogic();
                List<Contactnotification> notifications = notificationLogic.find(notificationSearch);

                // Convert to DTO and send Data
                NotificationDTOConverter converter = new NotificationDTOConverter();
                response = Response.ok(converter.toDTOList(notifications)).build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "getNotifications()", e);
            }
        }
		return response;
	}

	/**
	 *
	 * @param code
	 * @return
	 * @throws es.sm2.openppm.api.rest.error.RestServiceException
	 */
	@PUT
	@Path("{CODE}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markRead(@PathParam("CODE") Integer code) throws RestServiceException {

        // Check if user is logged
        Response response = checkPermission();

        if (response == null) {
            try {

                // Declare Logic
                ContactnotificationLogic contactnotificationLogic = new ContactnotificationLogic();

                // Logic Find contact notification
                Contactnotification contactnotification = contactnotificationLogic.findById(code);

                if (contactnotification == null) {
                    throw new RestServiceException(Response.Status.BAD_REQUEST, "Notification [code] is not valid");
                }
                else {
                    // Set data
                    contactnotification.setReadDate(new Date());
                    contactnotification.setReadNotify(true);

                    // Logic save contact notification
                    contactnotificationLogic.save(contactnotification);

                    // Convert to DTO and send Data
                    contactnotification = contactnotificationLogic.findById(code, DataUtil.toList(Contactnotification.NOTIFICATION));
                    NotificationDTOConverter converter = new NotificationDTOConverter();
                    response = Response.ok(converter.toDTO(contactnotification)).build();
                }

            } catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "markRead()", e);
            }
        }

		return response;
	}
}
