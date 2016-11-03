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
 * File: ProjectFollowupDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ProjectFollowupDAO extends AbstractGenericHibernateDAO<Projectfollowup, Integer> {

    public ProjectFollowupDAO(Session session) {
        super(session);
    }

    /**
     * Return Project Followups
     * @param proj
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Projectfollowup> findByProject(Project proj, String order) {
        List<Projectfollowup> followups = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass())
                .setFetchMode(Projectfollowup.PROJECT, FetchMode.JOIN);

            crit.add(Restrictions.eq("project.idProject", proj.getIdProject()));
            if(order.equals(Constants.ASCENDENT)) {
                crit.addOrder(Order.asc("followupDate"));
            }
            else if(order.equals(Constants.DESCENDENT)) {
                crit.addOrder(Order.desc("followupDate"));
            }

            followups = crit.list();
        }
        return followups;
    }


    /**
     * Return Last Projectfollowup with general flag
     * @param proj
     * @return
     */
    public Projectfollowup findLastByProject(Project proj) {
        Projectfollowup followup = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Projectfollowup.PROJECT, proj))
                .add(Restrictions.isNotNull(Projectfollowup.GENERALFLAG))
                .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE))
                .setMaxResults(1);

            followup = (Projectfollowup) crit.uniqueResult();
        }
        return followup;
    }


    /**
     * Return Last Followup
     * @param proj
     * @return
     */
    public Projectfollowup findLast(Project proj) {
        Projectfollowup followup = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Projectfollowup.PROJECT, proj))
                .add(Restrictions.isNotNull(Projectfollowup.PV))
                .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE))
                .setMaxResults(1);

            followup = (Projectfollowup) crit.uniqueResult();
        }
        return followup;
    }


    /**
     * Return Last Projectfollowup with AC
     * @param proj
     * @return
     */
    public Projectfollowup findLastByProjectWithAC(Project proj) {
        Projectfollowup followup = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Projectfollowup.PROJECT, proj))
                .add(Restrictions.isNotNull(Projectfollowup.AC))
                .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE))
                .setMaxResults(1);

            followup = (Projectfollowup) crit.uniqueResult();
        }
        return followup;
    }

    /**
     * Return Last Projectfollowup with risk flag
     * @param proj
     * @return
     */
    public Projectfollowup findLastByProjectRisk(Project proj) {
        Projectfollowup followup = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Projectfollowup.PROJECT, proj))
                .add(Restrictions.isNotNull(Projectfollowup.RISKFLAG))
                .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE))
                .setMaxResults(1);

            followup = (Projectfollowup) crit.uniqueResult();
        }
        return followup;
    }


    public Projectfollowup findLastWithDataByProject(Project proj) {
        Projectfollowup followup = null;
        if (proj != null) {
            Criteria crit = getSession().createCriteria(getPersistentClass());
            crit.add(Restrictions.eq("project.idProject", proj.getIdProject()))
                .add(Restrictions.disjunction()
                    .add(Restrictions.isNotNull("generalFlag"))
                    .add(Restrictions.isNotNull("riskFlag"))
                    .add(Restrictions.isNotNull("costFlag"))
                    .add(Restrictions.isNotNull("scheduleFlag"))
                )
                .addOrder(Order.desc("followupDate"))
                .setMaxResults(1);

            followup = (Projectfollowup) crit.uniqueResult();
        }
        return followup;
    }


    /**
     * Get EV from program projects until a date
     * @param program
     * @param tempDate
     * @return
     */
    public Double getProgramEV(Program program, Date date, boolean projectsDisabled) {
        Double ev = 0D;

        if (program != null) {
            ProjectDAO projectDAO = new ProjectDAO(getSession());

            List<Project> projects = projectDAO.findByProgramAndProjectsDisabled(program, projectsDisabled);

            for (Project p : projects) {
                Criteria crit = getSession().createCriteria(getPersistentClass());
                crit.add(Restrictions.eq("project.idProject", p.getIdProject()));
                crit.add(Restrictions.lt("followupDate", date));
                crit.add(Restrictions.isNotNull("ev"));
                crit.addOrder(Order.desc("followupDate"));
                crit.setMaxResults(1);

                Projectfollowup followup = (Projectfollowup) crit.uniqueResult();
                if (followup != null) {
                    ev += followup.getEv();
                }
            }
        }

        return ev;
    }


    /**
     * Consult backlog for a program in a month
     * CurrentMonth is for calculate number of months of a project to end
     * @param program
     * @param date
     * @return
     */
    public Double getProgramBacklog(Program program, Date date, Date currentMonth, boolean projectsDisabled) {
        Double backlog = 0D;

        if (program != null) {
            ProjectDAO projectDAO = new ProjectDAO(getSession());

            List<Project> projects = projectDAO.findByProgramAndProjectsDisabled(program, projectsDisabled);

            for (Project p : projects) {
                if (p.getPlannedFinishDate() != null && p.getPlannedFinishDate().after(date)) {
                    Integer months = DateUtil.monthsBetween(currentMonth, p.getPlannedFinishDate());
                    if (months <= 0) months = 1;

                    Criteria crit = getSession().createCriteria(getPersistentClass());
                    crit.add(Restrictions.eq("project.idProject", p.getIdProject()));
                    crit.add(Restrictions.lt("followupDate", date));
                    crit.add(Restrictions.isNotNull("ev"));
                    crit.addOrder(Order.desc("followupDate"));
                    crit.setMaxResults(1);

                    Projectfollowup followup = (Projectfollowup) crit.uniqueResult();

                    if (followup != null) {
                        //Extra is project pending incomes divided by months pending to close project
                        double netIncome = (p.getNetIncome() == null?0:p.getNetIncome());
                        backlog += (netIncome-followup.getEv())/months;
                    }
                }
            }
        }

        return backlog;
    }

    /**
     * Delete all project followup by project
     * @param project
     */
    public void deleteByProject(Project project) {

        Query query = getSession().createQuery(
                "delete from Projectfollowup as fo " +
                "where fo.project.idProject = :idProject "
            );
        query.setInteger("idProject", project.getIdProject());
        query.executeUpdate();
    }

    /**
     * Cons followup with project
     * @param projectfollowup
     * @return
     */
    public Projectfollowup consFollowupWithProject(
            Projectfollowup projectfollowup) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
            .setFetchMode(Projectfollowup.PROJECT, FetchMode.JOIN)
            .add(Restrictions.idEq(projectfollowup.getIdProjectFollowup()));

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     * Find last followup of month
     * @param project
     * @param firstMonthDay
     * @param lastMonthDay
     * @return
     */
    public Projectfollowup findLasInMonth(Project project, Date firstMonthDay,
            Date lastMonthDay) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
            .add(Restrictions.eq(Projectfollowup.PROJECT, project))
            .add(Restrictions.between(Projectfollowup.FOLLOWUPDATE, firstMonthDay, lastMonthDay))
            .add(Restrictions.isNotNull(Projectfollowup.PV))
            .setMaxResults(1)
            .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE));

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     * Find followups in range dates
     * @param project
     * @param since
     * @param until
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Projectfollowup> findByDates(Project project, Date since,
            Date until) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
            .add(Restrictions.eq(Projectfollowup.PROJECT, project))
            .add(Restrictions.between(Projectfollowup.FOLLOWUPDATE, since, until))
            .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE));

        return crit.list();
    }

    public Projectfollowup findPrevius(Project project, Date date) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
            .setMaxResults(1)
            .add(Restrictions.eq(Projectfollowup.PROJECT, project))
            .add(Restrictions.lt(Projectfollowup.FOLLOWUPDATE, date))
            .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE));

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     * Find followups before date
     * @param project
     * @param date
     * @param propertyOrder
     * @param typeOrder
     * @param joins
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Projectfollowup> findByProject(Project project, Date date,
            String propertyOrder, String typeOrder, List<String> joins) {

        Criteria crit = getSession().createCriteria(getPersistentClass());

        addJoins(crit, joins);

        crit.add(Restrictions.eq(Projectfollowup.PROJECT, project));

        if (date != null) {
            crit.add(Restrictions.le(Projectfollowup.FOLLOWUPDATE, date));
        }

        addOrder(crit, propertyOrder, typeOrder);

        return crit.list();
    }

/**
 * followup returns before the date
 * @param project
 * @param date
 * @return
 */
    public Projectfollowup beforeFollowup(Project project, Date date) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                        .setMaxResults(1)
                        .add(Restrictions.eq(Projectfollowup.PROJECT, project))
                        .add(Restrictions.lt(Projectfollowup.FOLLOWUPDATE, date))
                        .addOrder(Order.desc(Projectfollowup.FOLLOWUPDATE));

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     * followup returns after the date
     * @param project
     * @param date
     * @return
     */
    public Projectfollowup afterFollowup(Project project, Date date) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                        .setMaxResults(1)
                        .add(Restrictions.eq(Projectfollowup.PROJECT, project))
                        .add(Restrictions.gt(Projectfollowup.FOLLOWUPDATE, date))
                        .addOrder(Order.asc(Projectfollowup.FOLLOWUPDATE));

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     *
     * @param project
     * @param followupdate
     * @param date
     * @return
     */
    public Projectfollowup findBydDate(Integer id, Project project, String followupdate, Date date) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                        .setMaxResults(1)
                        .add(Restrictions.eq(Projectfollowup.PROJECT, project))
                        .add(Restrictions.eq(Projectfollowup.FOLLOWUPDATE, date));

        if(id != null){
            crit.add(Restrictions.ne(Projectfollowup.IDPROJECTFOLLOWUP, id));
        }

        return (Projectfollowup) crit.uniqueResult();
    }

    /**
     * Find follow ups for notify
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Projectfollowup> findForNotify() {

        Calendar actualDate = DateUtil.getCalendar();

        Criteria crit = getSession().createCriteria(getPersistentClass())
            .add(Restrictions.le(Projectfollowup.FOLLOWUPDATE, actualDate.getTime()))
            .add(Restrictions.isNull(Projectfollowup.GENERALFLAG))
            .add(Restrictions.isNull(Projectfollowup.RISKFLAG))
            .add(Restrictions.isNull(Projectfollowup.COSTFLAG))
            .add(Restrictions.isNull(Projectfollowup.SCHEDULEFLAG))
            .setFetchMode(Projectfollowup.PROJECT, FetchMode.JOIN)
            .setFetchMode(Projectfollowup.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER, FetchMode.JOIN)
            .setFetchMode(Projectfollowup.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT, FetchMode.JOIN);

        Criteria crit2 = crit.createCriteria(Projectfollowup.PROJECT);
        crit2.add(Restrictions.eq(Project.STATUS, Constants.STATUS_CONTROL));

        return crit.list();
    }

}
