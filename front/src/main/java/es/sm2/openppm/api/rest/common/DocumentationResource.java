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
 * File: DocumentationResource.java
 * Create User: javier.hernandez
 * Create Date: 23/09/2015 09:37:59
 */

package es.sm2.openppm.api.rest.common;

import es.sm2.openppm.api.converter.common.DocumentDTOConverter;
import es.sm2.openppm.api.model.common.DocumentDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.logic.impl.ContentFileLogic;
import es.sm2.openppm.core.logic.impl.DocumentationLogic;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.utils.LogManager;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by javier.hernandez on 23/09/2015.
 */
@Path("/document")

@Produces({MediaType.APPLICATION_JSON})
public class DocumentationResource extends BaseResource {

    /**
     * Get user
     *
     * @return
     */
    @GET
    public Response find() {

        // Check permissions
        isUserLogged();

        List<DocumentDTO> docs = null;

        try {

            // Find documents
            DocumentationLogic docLogic = new DocumentationLogic();
            List<Documentation> documentation = docLogic.findByRelation(Documentation.COMPANY, getCompany());

            // Convert documents
            DocumentDTOConverter converter = new DocumentDTOConverter();
            docs = converter.toDTOList(documentation);
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "find()", e);
        }


        return Response.ok().entity(docs).build();
    }


    /**
     * Download document
     *
     * @param code
     * @return
     */
    @GET
    @Path("{CODE}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@NotNull @PathParam("CODE") Integer code) {

        // Check permissions
        isUserLogged();

        Documentation doc = null;
        try {

            // Find info of documentation
            DocumentationLogic docLogic = new DocumentationLogic();
            doc = docLogic.findById(code);

            if (doc == null) {
                throw new RestServiceException(Response.Status.BAD_REQUEST, "document not found for code: "+code);
            }

            // Find file
            ContentFileLogic fileLogic = new ContentFileLogic();
            Contentfile docFile = fileLogic.findByDocumentation(doc);

            if (docFile == null || docFile.getContent() == null) {
                throw new RestServiceException(Response.Status.BAD_REQUEST, "file not found for code: "+code);
            }

            getResponse().setContentType(docFile.getMime());
            getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + doc.getNameFile() + "\"");
            getResponse().setContentLength(docFile.getContent().length);

            // Add file to response
            ByteArrayInputStream inputStream	= new ByteArrayInputStream(docFile.getContent());
            OutputStream outs			= getResponse().getOutputStream();

            int start	= 0;
            int length	= 4 * 1024; // Buffer 4KB
            byte[] buff = new byte[length];

            while (inputStream.read(buff, start, length) != -1) {
                outs.write(buff, start, length);
            }

            inputStream.close();

        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "download()", e);
        }

        return Response.ok().header("Content-Disposition", "filename=\"" + doc.getNameFile() + "\"").build();
    }

                
}
