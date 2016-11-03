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
 * File: RestClient.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:03
 */
package es.sm2.openppm.core.integration;

import es.sm2.openppm.core.integration.exceptions.BadRequestException;
import es.sm2.openppm.core.integration.exceptions.ConflictException;
import es.sm2.openppm.core.integration.exceptions.ForbiddenException;
import es.sm2.openppm.core.integration.exceptions.MethodNotAllowedException;
import es.sm2.openppm.core.integration.exceptions.NotAcceptableException;
import es.sm2.openppm.core.integration.exceptions.NotFoundException;
import es.sm2.openppm.core.integration.exceptions.UnauthorizedException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author javier.hernandez
 *
 */
public class RestClient {

	private static final Logger LOGGER = Logger.getLogger(RestClient.class);
	
	/**
	 * Codes for identificate response of HTTP REST Calls
	 * 
	 * @author javier.hernandez
	 *
	 */
	public enum HttpCode {
		RESPONSE_OK(200),
		RESPONSE_CREATED(201),
		RESPONSE_BAD_REQUEST(400),
		RESPONSE_UNAUTHORIZED(401),
		RESPONSE_FORBIDDEN(403),
		RESPONSE_NOT_FOUND(404),
		RESPONSE_METHOD_NOT_ALLOWED(405),
		RESPONSE_NOT_ACCEPTABLE(406),
		RESPONSE_CONFLICT(409);
		
		private int code;
		
		private HttpCode(int code) {
			this.code = code;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}
	}
	
	private String baseUrl;
	private WebTarget webResource;
    private Client client;
    private String mediaType;
    private String user;
    private String password;
    
    /**
     * Create client
     */
    public RestClient(String baseUrl, String user, String password) {
    	
    	setBaseUrl(baseUrl);
    	setUser(user);
    	setPassword(password);
    	
    	// Set default Media Type
    	setMediaType(MediaType.APPLICATION_XML);
    	
        // Create client
        setClient(ClientBuilder.newClient());
        getClient().register(new LoggingFilter());
        
        // Add security
        if (ValidateUtil.isNotNull(getUser()) && ValidateUtil.isNotNull(getPassword())) {
        	
        	setUsernamePassword();
        }
    }

    /**
     * Create Web Resources with paths
     * 
     * @param paths
     * @return
     */
    public RestClient createWebResource(String...paths) {
    	
    	// Create Web resource
    	setWebResource(getClient().target(getBaseUrl()));
    	
    	// Add paths
    	if (paths != null && paths.length > 0) {
    		
    		for (String path : paths) {
    			
    			// Create path
    			setWebResource(getWebResource().path(path));
    		}
    	}
    	
		return this; 
    }
    
    /**
     * Send Get message
     * 
     * @param responseClass
     * @return
     * @throws UnauthorizedException
     * @throws NotFoundException 
     * @throws NotAcceptableException 
     * @throws MethodNotAllowedException 
     * @throws ForbiddenException 
     * @throws ConflictException 
     * @throws BadRequestException
     */
    @SuppressWarnings({ "hiding", "unchecked" })
	public <T> T getMessage(Class<T> responseClass)
			throws BadRequestException,
			ConflictException, ForbiddenException,
			MethodNotAllowedException, NotAcceptableException, NotFoundException, UnauthorizedException {
    	
    	// Send message 
    	Response response = getWebResource().request(getMediaType()).get();
    	
    	// Validate response
    	validateResponse(response);
    	
        return response.readEntity(responseClass);
    }

	/**
     * Send post message
     * 
     * @param responseClass
     * @param requestEntity
     * @return
	 * @throws UnauthorizedException
	 * @throws NotFoundException 
	 * @throws NotAcceptableException 
	 * @throws MethodNotAllowedException 
	 * @throws ForbiddenException 
	 * @throws ConflictException 
	 * @throws BadRequestException
     */
    @SuppressWarnings({ "hiding", "unchecked" })
	public <T> T postMessage(Class<T> responseClass, Object requestEntity)
			throws BadRequestException, ConflictException, ForbiddenException, MethodNotAllowedException, NotAcceptableException,
			NotFoundException, UnauthorizedException {

    	// Send message
        T response = getWebResource().request(getMediaType()).post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), responseClass);
    	
    	// Validate response
//    	validateResponse(response);

        return response;
    }
    
    /**
     * Send put message
     * 
     * @param responseClass
     * @param requestEntity
     * @return
     * @throws UnauthorizedException
     * @throws NotFoundException 
     * @throws NotAcceptableException 
     * @throws MethodNotAllowedException 
     * @throws ForbiddenException 
     * @throws ConflictException 
     * @throws BadRequestException
     */
    @SuppressWarnings({ "hiding", "unchecked" })
	public <T> T putMessage(Class<T> responseClass, Object requestEntity)
    		throws BadRequestException,
    		ConflictException, ForbiddenException, MethodNotAllowedException, NotAcceptableException,
    		NotFoundException, UnauthorizedException {
    	
        // Send message
        T response = getWebResource().request(getMediaType()).put(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), responseClass);

        return response;
    }

    /**
     * Close client
     */
    public void close() {
    	
    	LOGGER.debug("Close client connection");
        client.close();
    }

    /**
     * Add login configuration
     * 
     */
    private void setUsernamePassword() {
    	
    	// Add configuration options for Base Authentication
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .nonPreemptive().credentials(getUser(), getPassword()).build();

        getClient().register(feature);
    }

    /**
	 * Validate response and encapsulate
	 * 
	 * @param response
     * @throws BadRequestException
     * @throws ConflictException 
     * @throws ForbiddenException 
     * @throws MethodNotAllowedException 
     * @throws NotAcceptableException 
     * @throws NotFoundException 
     * @throws UnauthorizedException 
	 */
	private static void validateResponse(Response response) throws BadRequestException, ConflictException, ForbiddenException,
            MethodNotAllowedException, NotAcceptableException, NotFoundException, UnauthorizedException {
		
		LOGGER.debug("Status - " + response.getStatus());
		LOGGER.debug("StatusCode - " + response.getStatusInfo());

		if (HttpCode.RESPONSE_BAD_REQUEST.getCode() == response.getStatus()) {
			throw new BadRequestException();
		}
		else if (HttpCode.RESPONSE_CONFLICT.getCode() == response.getStatus()) {
			throw new ConflictException();
		}
		else if (HttpCode.RESPONSE_FORBIDDEN.getCode() == response.getStatus()) {
			throw new ForbiddenException();
		}
		else if (HttpCode.RESPONSE_METHOD_NOT_ALLOWED.getCode() == response.getStatus()) {
			throw new MethodNotAllowedException();
		}
		else if (HttpCode.RESPONSE_NOT_ACCEPTABLE.getCode() == response.getStatus()) {
			throw new NotAcceptableException();
		}
		else if (HttpCode.RESPONSE_NOT_FOUND.getCode() == response.getStatus()) {
			throw new NotFoundException();
		}
		else if (HttpCode.RESPONSE_UNAUTHORIZED.getCode() == response.getStatus()) {
			throw new UnauthorizedException();
		}
	}
	
    /*
     * GETTERS AND SETTERS 
     */
    
	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the webResource
	 */
	public WebTarget getWebResource() {
		return webResource;
	}

	/**
	 * @param webResource the webResource to set
	 */
	public void setWebResource(WebTarget webResource) {
		this.webResource = webResource;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
