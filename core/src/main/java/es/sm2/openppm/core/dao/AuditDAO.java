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
 * File: AuditDAO.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:00:25
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.model.impl.Audit;
import es.sm2.openppm.core.model.search.AuditSearch;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 11/08/2015.
 */
public class AuditDAO extends AbstractGenericHibernateDAO<Audit, Integer> {

    public static final int MAX_RESULTS = 500;

    public AuditDAO(Session session) {
        super(session);
    }

    /**
     * Find audit by filters
     *
     * @param search
     * @return
     */
    public List<Audit> find(AuditSearch search) {

        Criteria crit = getSession().createCriteria(getPersistentClass());

        Criterion[] restrictions = getRestrictions(search);

        for (Criterion restriction : restrictions) {
            crit.add(restriction);
        }

        crit.addOrder(Order.asc(Audit.CREATIONDATE));

        // Max results
        crit.setMaxResults(MAX_RESULTS);

        return crit.list();
    }

    /**
     * Indicate if filter exceeded maxim number of registers to show
     *
     * @param search
     * @return
     */
    public boolean exceededQuota(AuditSearch search) {


        int rows = rowCount(getRestrictions(search));

        LogManager.getLog(getClass()).debug("Number of records found for audit: "+rows+ " "+search.toString());

        return rows > MAX_RESULTS;
    }

    private Criterion[] getRestrictions(AuditSearch search) {

        List<Criterion> restrictions = new ArrayList<Criterion>();

        restrictions.add(Restrictions.eq(Audit.IDCOMPANY, search.getCompany().getIdCompany()));
        restrictions.add(Restrictions.eq(Audit.LOCATION, search.getLocation().name()));

        if (search.getIdProject() != null) {
            restrictions.add(Restrictions.eq(Audit.IDPROJECT, search.getIdProject()));
        }

        if (search.getIdContact() != null) {
            restrictions.add(Restrictions.eq(Audit.IDCONTACT, search.getIdContact()));
        }

        if (ValidateUtil.isNotNull(search.getProjectStatus())) {
            restrictions.add(Restrictions.eq(Audit.PROJECTSTATUS, search.getProjectStatus()));
        }

        if (search.getSince() != null && search.getUntil() != null) {
            restrictions.add(Restrictions.between(Audit.CREATIONDATE, search.getSince(), search.getUntil()));
        }
        else if (search.getSince() != null) {
            restrictions.add(Restrictions.ge(Audit.CREATIONDATE, search.getSince()));
        }
        else if (search.getUntil() != null) {
            restrictions.add(Restrictions.le(Audit.CREATIONDATE, search.getUntil()));
        }

        return restrictions.toArray(new Criterion[restrictions.size()]);

    }
}
