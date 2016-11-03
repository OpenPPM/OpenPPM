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
 * File: DocumentprojectLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.Settings.SettingType;
import es.sm2.openppm.core.dao.DocumentprojectDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.exceptions.DocumentprojectNotFoundException;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Documentproject.DocumentType;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Logic object for domain model class Documentproject
 * @see DocumentprojectLogic
 * @author Hibernate Generator by Javier Hernandez
 */
public class DocumentprojectLogic extends AbstractGenericLogic<Documentproject, Integer>{

    private Map<String, String> settings;

    public DocumentprojectLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    /**
     * Getter for property 'settings'.
     *
     * @return Value for property 'settings'.
     */
    public Map<String, String> getSettings() {
        return settings;
    }

    /**
     * Setter for property 'settings'.
     *
     * @param settings Value to set for property 'settings'.
     */
    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    /**
	 * Save or update document project
	 * @param idDocumentProject
	 * @param idProject
	 * @param type
	 * @param link
	 * @param comment 
	 * @return 
	 * @throws Exception
	 */
	public Documentproject saveDocumentproject(int idDocumentProject,
			int idProject, String type, String link, String comment, String creationContact, Date creationDate) throws Exception {
		
		if (type == null || idProject == -1) { throw new DocumentprojectNotFoundException(); }

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Transaction tx		= null;
		Documentproject doc = null;
		
		try {
			tx = session.beginTransaction();
			
			DocumentprojectDAO documentprojectDAO = new DocumentprojectDAO(session);
			
			if (idDocumentProject == -1) {
				doc = new Documentproject();
				doc.setProject(new Project(idProject));
				doc.setType(type);
			}
			else {
				doc = documentprojectDAO.findById(idDocumentProject, false);
			}
			
			doc.setLink(link);
			doc.setContentComment(comment);
			doc.setCreationContact(creationContact);
			doc.setCreationDate(creationDate);

			doc = documentprojectDAO.makePersistent(doc);
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return doc;
	}

	/**
	 * Find by type in the project
	 * @param project
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Documentproject findByType(Project project,
			String type) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Transaction tx		= null;
		Documentproject doc = null;
		
		try {
			tx = session.beginTransaction();
			
			DocumentprojectDAO documentprojectDAO = new DocumentprojectDAO(session);
			doc = documentprojectDAO.findByType(project,type);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return doc;
	}
	
	/**
	 * Find documents by type in the project
	 * @param project
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Documentproject> findListByType(Project project,
			String type) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Transaction tx		= null;
		List<Documentproject> docs = null;
		
		try {
			tx = session.beginTransaction();
			
			DocumentprojectDAO documentprojectDAO = new DocumentprojectDAO(session);
			docs = documentprojectDAO.findListByType(project,type);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return docs;
	}

	/**
	 * Get documents by settings
	 * 
	 * @param session
	 * @param settings
	 * @param project
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Documentproject> getDocuments(Session session, HashMap<String, String> settings, Project project, String type) throws Exception {
		
		List<Documentproject> docs 	= null;
		
		// Declare DAO
		DocumentprojectDAO documentprojectDAO = new DocumentprojectDAO(session);
		
		String documentStorage = SettingUtil.getString(settings, SettingType.DOCUMENT_STORAGE);
		
		// Link setting
		//
		if (DocumentType.LINK.getName().equals(documentStorage)) {
			
			if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_SHOW_DOCUMENTS, Settings.DEFAULT_SETTING_SHOW_DOCUMENTS))) {
				docs = documentprojectDAO.findListByType(project, null, false);
			}
			else {
				docs = documentprojectDAO.findListByType(project, type, false);
			}
		}
		// File setting
		//
		else if (DocumentType.FILE_SYSTEM.getName().equals(documentStorage)) {
			
			if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_SHOW_DOCUMENTS, Settings.DEFAULT_SETTING_SHOW_DOCUMENTS))) {
				docs = documentprojectDAO.findListByType(project, null, true);
			} 
			else {
				docs = documentprojectDAO.findListByType(project, type, true);
			}
		}
		// Mixed setting
		//
		else if (DocumentType.MIXED.getName().equals(documentStorage)) {
			
			if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_SHOW_DOCUMENTS, Settings.DEFAULT_SETTING_SHOW_DOCUMENTS))) {
				docs = documentprojectDAO.findListByType(project, null);
			}
			else {
				docs = documentprojectDAO.findListByType(project, type);
			}
		}
			
		return docs;
	}
	
	/**
	 * Get documents by settings
	 * 
	 * @param settings
	 * @param project 
	 * @param type 
	 * @return
	 * @throws Exception 
	 */
	public List<Documentproject> getDocuments(HashMap<String, String> settings, Project project, String type) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		Transaction tx				= null;
		List<Documentproject> docs 	= null;
		
		try {
			tx = session.beginTransaction();
			
			docs = getDocuments(session, settings, project, type);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return docs;
	}

    /**
     * Remove physical and logic
     *
     * @param documentproject
     */
    public void remove(Documentproject documentproject) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Transaction tx				= null;

        try {
            tx = session.beginTransaction();

            remove(documentproject, session);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            SessionFactoryUtil.getInstance().close();
        }
    }

    /**
     * Remove physical and logic
     *
     * @param documentproject
     * @param session
     */
    public void remove(Documentproject documentproject, Session session) {

        DocumentprojectDAO documentprojectDAO = new DocumentprojectDAO(session);

        // Get document
        Documentproject doc = documentprojectDAO.findById(documentproject.getIdDocumentProject(), false);

        // Delete logic document
        documentprojectDAO.makeTransient(doc);

        // Delete file
        if (ValidateUtil.isNotNull(doc.getName())) {

            String dirBySetting = SettingUtil.getString(getSettings(), Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);

            String nameFile		= doc.getIdDocumentProject() + StringPool.UNDERLINE + (doc.getName().lastIndexOf("\\") != -1 ?
                    StringUtils.formatting(doc.getName().substring(doc.getName().lastIndexOf("\\") + 1)) :
                    StringUtils.formatting(doc.getName()));

            File file = new File(dirBySetting, nameFile);

            // Search in new path
            if (!file.exists()) {

                // Declare logic
                ProjectDAO projectDAO = new ProjectDAO(session);

                // Logic get project
                Project project = projectDAO.consProject(doc.getProject(), null);

                String docFolder = dirBySetting.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + project.getIdProject());
                docFolder = docFolder.concat(File.separator + doc.getType());

                file = new File(docFolder, nameFile);
            }

            // Validate file and permissions
            if (ValidateUtil.isNull(validateFile(file))) {
                // Delete physical file
                file.delete();
            }
        }
    }

    /**
     * Validate file
     *
     * @param file
     * @return
     */
    public String validateFile(File file) {

        String error = StringPool.BLANK;

        if (!file.exists()) {

            LogManager.getLog(getClass()).error(file.getAbsolutePath() + " file not found");

            // Add message
            //
            ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.file_not_found", true, file.getAbsolutePath());

            paramResourceBundle.setBlack(false);

            error = paramResourceBundle.getMessage(getBundle());
        }
        else if (!file.canWrite() || !file.canRead()) {

            if (!file.canWrite()) {
                LogManager.getLog(getClass()).error(file.getAbsolutePath() + " cannot write");
            }

            if (!file.canRead()) {
                LogManager.getLog(getClass()).error(file.getAbsolutePath() + " cannot read");
            }

            // Add message
            //
            ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.no_permissions", true, file.getAbsolutePath());

            paramResourceBundle.setBlack(false);

            error = paramResourceBundle.getMessage(getBundle());
        }

        return error;
    }
}

