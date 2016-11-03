package es.sm2.openppm.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.sm2.openppm.api.converter.NotificationDTOConverter;
import es.sm2.openppm.api.model.NotificationDTO;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.NotificationCenterLogic;
import es.sm2.openppm.core.model.impl.Notificationtype;
import es.sm2.openppm.utils.LogManager;

/**
 * Resource to handle notifications.
 */
@Path("/config/notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationCenterResource extends BaseResource {

	/**
	 * 
	 * @return
	 * @throws RestServiceException
	 */
	@GET
    public Response getNotifications() throws RestServiceException {
		
		// Check permissions (se puede pasar mas de un ROL)
        //Response  response = checkPermission(Constants.ROLE_PMO);
		
        // Si el response es NULL, es que tiene permisos y puedes continuar.
        // Sino, el response contiene el error que se devolvera directamente.

        Response response = checkPermission(Constants.ROLE_ADMIN);

        if (response == null) {
            NotificationCenterLogic notifCenterLogic = new NotificationCenterLogic();

            List<Notificationtype> notificationTypeList = notifCenterLogic.getNotifications(
                    getCompany());

		// Convert model to dto, Notificationtype to NotificationDTO
            NotificationDTOConverter notificationDTOConverter = new NotificationDTOConverter();

            List<NotificationDTO> notificationDTOList = new ArrayList<NotificationDTO>();
            for (Notificationtype notification : notificationTypeList) {
                notificationDTOList.add(notificationDTOConverter.toDTO(notification));
            }

            response = Response.ok(notificationDTOList).build();
        }
		return response;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws RestServiceException
	 */
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertNotification(NotificationDTO dto) throws RestServiceException {

		Response response = null;
		
		// Convert dto to model - NotificationDTO to Notificationtype
		NotificationDTOConverter notificationDTOConverter = new NotificationDTOConverter();
		Notificationtype notification = notificationDTOConverter.toModel(dto);

		try {
			notification.setCompany(getCompany());
			
			// Insert notification
			NotificationCenterLogic notifCenterLogic = new NotificationCenterLogic();
			notification = notifCenterLogic.insertNotification(notification);
		
			dto.setCode(notification.getIdNotificationType());
			
			response = Response.ok(dto).build();
		}
		catch (Exception e) {
            response = getErrorResponse(LogManager.getLog(getClass()), "insertNotification()", e);
		}
		
		return response;
	}

	/**
	 * 
	 * @param dto
	 * @param code
	 * @return
	 * @throws RestServiceException
	 */
	@PUT
	@Path("{CODE}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNotification(NotificationDTO dto, @PathParam("CODE") String code) throws RestServiceException {

		Response response = Response.ok().build();
		
		return response;
	}
}
