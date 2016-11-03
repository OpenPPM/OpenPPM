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
 * File: ImageServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.ContentFileLogic;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;

public class ImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Prepare streams.
		BufferedOutputStream output = null;
		try {
			
			String requestedImage = request.getPathInfo();
			
			if (requestedImage == null || requestedImage.length() == 0) {
				
				LogManager.getLog(getClass()).error("Load IMAGE. Not found data");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			// Remove first character
			requestedImage = requestedImage.substring(1);
			
			LogManager.getLog(getClass()).debug("Load IMAGE: "+requestedImage);
			
			if (requestedImage.split("/").length == 1 ||
                    "head".equals(requestedImage.split("/")[0])) {

                // Name Image to load
                String imageName = requestedImage.split("/").length == 1?requestedImage.split("/")[0]:requestedImage.split("/")[1];

                if ("headBySetting".equals(imageName)) {

                    Object user = request.getSession().getAttribute("user");
                    if (user != null) {
                        imageName = SettingUtil.getSetting(((Employee)user).getContact().getCompany(), Settings.SETTING_LOGO, null);

                        if (imageName == null) { imageName = Settings.LOGO; }
                    }
                }
                // Load image
				InputStream stream = ImageServlet.class.getResourceAsStream("/"+imageName);

                // Set content type
                String contentType = "image/"+imageName.split("\\.")[1];

				// Init servlet response.
				response.reset();
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setContentType(contentType);
				response.setHeader("Content-Disposition", "inline; filename=\""+imageName+"\"");

				// Open streams.
				output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

				BufferedInputStream buffIn = new BufferedInputStream(stream);

				byte []arr = new byte [DEFAULT_BUFFER_SIZE];
				int available;
				while((available = buffIn.read(arr)) > 0) {
					output.write(arr, 0, available);
				}

			}
			else {
				// Set entity and ID
				String entity	= requestedImage.split("/")[0];
				Integer id		= Integer.parseInt(requestedImage.split("/")[1]);

				// Declare logic
				ContentFileLogic contentFileLogic = new ContentFileLogic();
				Contentfile file = contentFileLogic.findByEntity(entity, id);

				// Init servlet response.
				response.reset();
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setContentType(file.getMime());
				response.setHeader("Content-Disposition", "inline; filename=\"image."+file.getExtension()+"\"");

				// Open streams.
				output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

				if (file != null) output.write(file.getContent());
			}

        }
        catch (Exception e) {
        	LogManager.getLog(getClass()).error("Creating image",e);
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        finally { close(output); }
		
	}

	
	/**
	 * Helper to close a Stream
	 * @param resource
	 */
	private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } 
            catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }
}
