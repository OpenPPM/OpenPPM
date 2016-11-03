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
 * File: AuthenticateResource.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 08:37:36
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.model.common.AuthenticateDTO;
import es.sm2.openppm.api.model.common.LoginDTO;
import es.sm2.openppm.api.model.common.UserDTO;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.utils.DataUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Resource to handle authentication of users.
 */
@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticateResource extends BaseResource {

	protected static final int TOKEN_TIMEOUT =  30 * 60 * 1000; // Token timeout on millis

	/**
	 * Authenticate a user.
	 *
	 * @param user	User to authenticate
	 * @return	The authenticated subject
	 * @throws RestServiceException
	 */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(LoginDTO user) throws RestServiceException {

		Employee userLogged = checkUser(user);

		JwtBuilder builder = Jwts.builder().signWith(SignatureAlgorithm.HS256, BaseResource.SIGNING_KEY);

		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.MILLISECOND, TOKEN_TIMEOUT);
		Date expired = calendar.getTime();

		builder.setSubject(user.getUsername());
		builder.setNotBefore(now);
		builder.setExpiration(expired);
		builder.claim(BaseResource.EMPLOYEE_ID_KEY, userLogged.getIdEmployee());

        // TODO javier.hernandez - 06/08/2015 - user mockup
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Javier Hernandez");
        userDTO.setLocale("es_ES");



		return Response.status(Response.Status.OK)
				.entity(new AuthenticateDTO(builder.compact(), userDTO))
                .build();
    }

    @POST
    @Path("/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response test(Object object) throws RestServiceException {

        // Check permissions
        Response  response = checkPermission(Constants.ROLE_PMO);

        if (response == null) {
            Employee user = getUser();

            List<String> test = new ArrayList<String>();
            test.add("test1");
            test.add("test2");
            test.add("test3");
            test.add("test4");

            ObjectMapper mapper = new ObjectMapper();
            LoginDTO loginDTO = mapper.convertValue(object, LoginDTO.class);

            response = Response.ok(test).build();
        }

		return response;
    }

    /**
     * Find user on BD for login
     *
     * @param user
     * @return
     * @throws RestServiceException
     */
	private Employee checkUser(LoginDTO user) throws RestServiceException {

        // TODO javier.hernandez - 05/08/2015 - check user

        return new Employee();
	}
}
