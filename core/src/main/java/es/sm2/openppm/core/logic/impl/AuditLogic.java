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
 * File: AuditLogic.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:01:00
 */

package es.sm2.openppm.core.logic.impl;

import com.thoughtworks.xstream.XStream;
import es.sm2.openppm.core.audit.XStreamAuditUtil;
import es.sm2.openppm.core.audit.model.ProjectAudit;
import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.dao.AuditDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.logic.setting.AuditSetting;
import es.sm2.openppm.core.model.impl.Audit;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.search.AuditSearch;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 10/08/2015.
 */
public class AuditLogic extends AbstractGenericLogic<Audit, Integer> {


    /**
     * Create Audit
     *
     * @param locationAuditEnum
     * @param project
     * @param user
     * @param auditSetting
     * @param session
     * @throws Exception
     */
    public void createAudit(LocationAuditEnum locationAuditEnum, Project project, Employee user, AuditSetting auditSetting, Session session) throws Exception {

        // Create audit if location is activated
        if (SettingUtil.getBoolean(session, user.getContact().getCompany(), auditSetting)) {

            ProjectDAO projectDAO = new ProjectDAO(session);

            project = projectDAO.findById(project.getIdProject());

            // Project audit data
            ProjectAudit projectAudit = new ProjectAudit(project, session);

            // Exclude data for audit
            projectAudit.setWbsnodes(null);
            projectAudit.setFollowups(null);
            projectAudit.setStakeholders(null);
            projectAudit.setIncomes(null);
            projectAudit.setAssumptions(null);
            projectAudit.setIssuelogs(null);
            projectAudit.setRisks(null);
            projectAudit.setChargeCosts(null);
            projectAudit.setWorkingCosts(null);
            projectAudit.setLabels(null);

            // Audit data project
            XStream xstream = XStreamAuditUtil.createXStrean();
            String data = xstream.toXML(projectAudit);

            Audit audit = new Audit(locationAuditEnum, user, project);
            audit.setDataObject(data.getBytes());

            AuditDAO auditDAO = new AuditDAO(session);
            auditDAO.makePersistent(audit);
        }
    }

    /**
     * Create Audit
     *
     * @param project
     * @param user
     * @throws Exception
     */
    public void createAudit(LocationAuditEnum locationAuditEnum, Project project, Employee user, AuditSetting auditSetting) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {

            tx = session.beginTransaction();

            createAudit(locationAuditEnum, project, user, auditSetting, session);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

    }

    /**
     * Find audit by filters
     *
     * @param search
     * @return
     */
    public List<Audit> find(AuditSearch search) throws Exception {

        List<Audit> audits = new ArrayList<Audit>();

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {

            tx = session.beginTransaction();

            AuditDAO auditDAO = new AuditDAO(session);
            audits = auditDAO.find(search);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return audits;
    }

    /**
     * Indicate if filter exceeded maxim number of registers to show
     *
     * @param search
     * @return
     */
    public boolean exceededQuota(AuditSearch search) throws Exception {

        boolean exceeded = true;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {

            tx = session.beginTransaction();

            AuditDAO auditDAO = new AuditDAO(session);
            exceeded = auditDAO.exceededQuota(search);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return exceeded;
    }
}
