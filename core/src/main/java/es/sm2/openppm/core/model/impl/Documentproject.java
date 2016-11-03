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
 * File: Documentproject.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseDocumentproject;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;


/**
 * Model class Documentproject.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Documentproject extends BaseDocumentproject {

	private static final long serialVersionUID = 1L;

    public Documentproject() {
		super();
    }
    public Documentproject(Integer idDocumentProject) {
		super(idDocumentProject);
    }
    
    public enum DocumentType {
    	LINK("link", "documentation.link"),
    	FILE_SYSTEM("filesystem", "documentation.file_system"),
    	MIXED("mixed", "documentation.mixed");
    	
    	private String name;
    	private String messageKey;
    	
    	private DocumentType (String name, String messageKey) {
    		this.name = name;
    		this.messageKey = messageKey;
    	}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the messageKey
		 */
		public String getMessageKey() {
			return messageKey;
		}

		/**
		 * @param messageKey the messageKey to set
		 */
		public void setMessageKey(String messageKey) {
			this.messageKey = messageKey;
		}
    }

    /**
     * Reduced link
     *
     * @return
     */
    public String getReducedLink() {

        String reducedLink = getLink();

        if (ValidateUtil.isNotNull(getLink()) && getLink().length() > 80) {

            reducedLink = getLink().substring(0, 77) + "...";
        }

        return reducedLink;
    }

}

