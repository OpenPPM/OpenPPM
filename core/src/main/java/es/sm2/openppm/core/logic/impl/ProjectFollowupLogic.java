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
 * File: ProjectFollowupLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.*;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.enums.PeriodicityEnum;
import es.sm2.openppm.core.dao.*;
import es.sm2.openppm.core.model.impl.*;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProjectFollowupLogic extends AbstractGenericLogic<Projectfollowup, Integer> {

    private Map<String, String> settings;

    public ProjectFollowupLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    public ProjectFollowupLogic() {

    }

    /**
     * Return Project followup
     * @param idFollowup
     * @return
     * @throws Exception
     */
    public Projectfollowup consFollowup (Integer idFollowup) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            if (idFollowup != -1) {
                followup = followupDAO.findById(idFollowup, false);
                followup.getProject();
            }
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
        return followup;
    }


    /**
     * Find last followup with general flag
     * @param project
     * @return
     * @throws Exception
     */
    public Projectfollowup findLastByProject(Project project) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followup = followupDAO.findLastByProject(project);

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
        return followup;
    }


    /**
     * Find last followup
     * @param project
     * @return
     * @throws Exception
     */
    public Projectfollowup findLast(Project project) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followup = followupDAO.findLast(project);

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
        return followup;
    }


    /**
     * Find last followup with AC
     * @param project
     * @return
     * @throws Exception
     */
    public Projectfollowup findLastByProjectWithAC(Project project) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followup = followupDAO.findLastByProjectWithAC(project);

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
        return followup;
    }


    /**
     * Find last followup
     * @param project
     * @return
     * @throws Exception
     */
    public Projectfollowup findLastWithDataByProject(Project project) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followup = followupDAO.findLastWithDataByProject(project);

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
        return followup;
    }


    /**
     * List of project followups
     * @param project
     * @return
     * @throws Exception
     */
    public List<Projectfollowup> consFollowups (Project project) throws Exception {

        List<Projectfollowup> followups = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followups = followupDAO.findByProject(project, Constants.ASCENDENT);

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
        return followups;
    }


    /**
     * @param projectfollowup
     * @return
     * @throws Exception
     */
    public Projectfollowup consFollowupWithProject(Projectfollowup projectfollowup) throws Exception {

        Projectfollowup followup = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
            followup = followupDAO.consFollowupWithProject(projectfollowup);

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
        return followup;
    }


    /**
     * Find last followup of month
     * @param project
     * @param since
     * @return
     * @throws Exception
     */
    public Projectfollowup findLasInMonth(Project project, Date since) throws Exception {

        Projectfollowup followup = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();


            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);

            followup = followupDAO.findLasInMonth(
                    project,
                    DateUtil.getFirstMonthDay(since),
                    DateUtil.getLastMonthDay(since));

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return followup;
    }

    /**
     * Find followup before date
     * @param project
     * @param date
     * @param propertyOrder
     * @param typeOrder
     * @param joins
     * @return
     * @throws Exception
     */
    public List<Projectfollowup> findByProject(Project project, Date date, String propertyOrder, String typeOrder, List<String> joins) throws Exception {

        List<Projectfollowup> followups = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();


            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);

            followups = followupDAO.findByProject(project, date, propertyOrder, typeOrder, joins);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return followups;

    }


    /**
     * Update RAG project
     * @param project
     * @throws Exception
     */
    public void updateRAG(Project project) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO	= new ProjectFollowupDAO(session);
            ProjectDAO projectDAO			= new ProjectDAO(session);

            Projectfollowup followup = followupDAO.findLastByProject(project);

            project.setRag(followup == null?null:followup.getGeneralFlag());

            projectDAO.makePersistent(project);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

/**
 * followup returns before the date
 * @param project 
 * @param date
 * @return
 * @throws Exception
 */
    public Projectfollowup beforeFollowup(Project project, Date date) throws Exception {

        Projectfollowup followup = null;
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);

            followup = followupDAO.beforeFollowup(project, date);
            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return followup;
    }

/**
 * followup returns after the date
 * @param project 
 * @param date
 * @return
 * @throws Exception
 */
    public Projectfollowup afterFollowup(Project project, Date date) throws Exception {

        Projectfollowup followup = null;
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);

            followup = followupDAO.afterFollowup(project, date);
            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return followup;
    }

/**
 * 
 * @param project
 * @param followupdate
 * @param date
 * @return
 * @throws Exception
 */
    public Projectfollowup findByDate(Integer id,Project project, String followupdate, Date date) throws Exception {

        Projectfollowup followup = null;
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);

            followup = followupDAO.findBydDate(id, project, followupdate, date);
            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return followup;
    }

    /**
     * Find follow ups for notify
     * @return
     * @throws Exception
     */
    public List<Projectfollowup> findForNotify() throws Exception {

        List<Projectfollowup> projectfollowups = null;

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO projectFollowupDAO = new ProjectFollowupDAO(session);
            projectfollowups = projectFollowupDAO.findForNotify();

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return projectfollowups;
    }

    /**
     * Save followup and update automatic kpis
     *
     * @param followup
     * @param user
     * @throws Exception
     */
    public List<Info> saveFollowup(Projectfollowup followup, Employee user) throws Exception {

        List<Info> infos = new ArrayList<Info>();

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAOs
            ProjectFollowupDAO projectFollowupDAO   = new ProjectFollowupDAO(session);
            ProjectFollowupDAO followupDAO          = new ProjectFollowupDAO(session);

            // Update project followup
            projectFollowupDAO.makePersistent(followup);

            Info infoUpdateFollowup = new Info(StringPool.InfoType.SUCCESS, "msg.info.updated", "followup");

            infos.add(infoUpdateFollowup);


            // Update automatic kpis
            //

            // Select last informed followup
            //
            Calendar calendar = DateUtil.getCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, +1);

            List<Projectfollowup> followups = followupDAO.findByProject(followup.getProject(), calendar.getTime(), Projectfollowup.FOLLOWUPDATE, Constants.ASCENDENT, null);

            Projectfollowup lastInformedFollowup = null;
            for (Projectfollowup projectfollowup : followups) {

                if ((lastInformedFollowup == null) || (projectfollowup.getEv() != null && projectfollowup.getAc() != null)) {
                    lastInformedFollowup = projectfollowup;
                }
            }

            // Conditions to update kpis
            //
            Calendar followupDate = DateUtil.getCalendar();
            followupDate.setTime(followup.getFollowupDate());

            Calendar lastInformedFollowupDate = DateUtil.getCalendar();
            lastInformedFollowupDate.setTime(lastInformedFollowup.getFollowupDate());

            if (followupDate.equals(lastInformedFollowupDate) || followupDate.after(lastInformedFollowupDate)) {

                ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic(settings, getBundle());

                // Update kpis
                infos.addAll(projectKpiLogic.updateAutomaticKPIs(session, followups, followup, user));
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return infos;
    }

    public List<Projectfollowup> recalculatePV(int projectID) throws Exception {

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        List <Projectfollowup> followups;
        try{

            tx = session.beginTransaction();

            // Init Logics
            ProjectDAO projectDAO = new ProjectDAO(session);
            ProjectFollowupDAO projectFollowupDAO = new ProjectFollowupDAO(session);
            ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
            ProjectcalendarDAO projectCalendarDAO = new ProjectcalendarDAO(session);
            ProjectCalendarExceptionsDAO projectCalendarExceptionsDAO = new ProjectCalendarExceptionsDAO(session);

            // JOINS
            List<String> joins = new ArrayList();
            joins.add(Projectactivity.WBSNODE);

            // get project related data (project, activities, costs)
            Project project = projectDAO.findById(projectID);
            followups = projectFollowupDAO.findByProject(project, project.getFinishDate(), null, null, null);
            List <Projectactivity> projectActivities = activityDAO.findByProject(project, joins);

            // Init exception days, so they are not counted as workdays
            Projectcalendar projectCalendar = projectCalendarDAO.findByProject(project);
            List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsDAO.findByRelation(Projectcalendarexceptions.PROJECTCALENDAR,
                                                                                                        projectCalendar);
            List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

            // Clear all the followup pv
            for (Projectfollowup followup: followups) {

                followup.setPv(0.0);
            }

            //for all the nodes related to the project
            for (Projectactivity projectactivity : projectActivities) {


                if (projectactivity.getWbsnode().getIsControlAccount()) {

                    Date initDate, finishDate;

                    //if it is a control account, get it and obtain init and finish dates
                    initDate = projectactivity.getPlanInitDate();
                    finishDate = projectactivity.getPlanEndDate();

                    // calculate workdays for the control account
                    int workdays = ValidateUtil.calculateWorkDays(initDate, finishDate, exceptionDates);

                    //calculate budget per day, linear extrapolation
                    double budgetPerDay = projectactivity.getWbsnode().getBudget() / workdays;

                    for (Projectfollowup followup : followups) {

                        double pv = 0;
                        double followupPv = followup.getPv();

                        // if followup happens before the initial date of the activity, this activity has no impact on the followup
                        // if its after or the same day, it does have an impact
                        if (followup.getFollowupDate().after(initDate) || followup.getFollowupDate().equals(initDate)) {

                            //if the follow up date is in the date range of the activity, calculate budget used
                            if (followup.getFollowupDate().before(finishDate)) {

                                int daysToDate = ValidateUtil.calculateWorkDays(initDate, followup.getFollowupDate(), exceptionDates);
                                pv = budgetPerDay * daysToDate;

                                //else use the entire budget
                            } else {

                                pv = projectactivity.getWbsnode().getBudget();
                            }

                            // We sum to the total follow up pv the amount calculated
                            followup.setPv(followupPv + pv);
                        }

                    }
                }

            }
            for (Projectfollowup followup: followups) {

                projectFollowupDAO.makePersistent(followup);
            }
            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

    return followups;
    }


    public double calculatePV (int idProject, Date date) throws Exception{

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        double pv = 0;

        try {

            tx = session.beginTransaction();
            ProjectDAO projectDAO = new ProjectDAO(session);
            ProjectActivityDAO projectActivityDAO = new ProjectActivityDAO(session);
            ProjectcalendarDAO projectCalendarDAO = new ProjectcalendarDAO(session);
            ProjectCalendarExceptionsDAO projectCalendarExceptionsDAO = new ProjectCalendarExceptionsDAO(session);

            // joins
            List <String> joins = new ArrayList();
            joins.add(Projectactivity.WBSNODE);


            // init Project and CAs related to that project
            Project project = projectDAO.findById(idProject);
            List <Projectactivity> CAs = projectActivityDAO.findByProject(project, joins);

            // Init exception days, so they are not counted as workdays
            Projectcalendar projectCalendar = projectCalendarDAO.findByProject(project);
            List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsDAO.findByRelation(Projectcalendarexceptions.PROJECTCALENDAR,
                                                                                                        projectCalendar);

            List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

            for (Projectactivity CA : CAs){

                if (CA.getWbsnode().getIsControlAccount()){

                    Date firstDate = CA.getPlanInitDate();
                    Date lastDate = CA.getPlanEndDate();

                    if (firstDate.before(date) ||firstDate.equals(date)){

                        if (lastDate.after(date)){

                            int workdays = ValidateUtil.calculateWorkDays(firstDate, lastDate, exceptionDates);
                            double budgetPerDay = CA.getWbsnode().getBudget() / workdays;
                            int daysToDate = ValidateUtil.calculateWorkDays(firstDate, date, exceptionDates);
                            pv = pv + (budgetPerDay * daysToDate);
                        }
                        else {

                            pv = pv + CA.getWbsnode().getBudget();
                        }
                    }
                }
            }
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return pv;
    }


    public List<Projectfollowup> createFollowups (Integer idProject, Date dateSince, Date dateUntil,
                                                  PeriodicityEnum periodicity, Integer periodicityCount) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        // Initialize reponse
        List<Projectfollowup> followups;

        try {

            tx = session.beginTransaction();
            followups = new ArrayList<Projectfollowup>();

            List<String> joins = new ArrayList<String>();
            joins.add(Projectcalendarexceptions.PROJECTCALENDAR);


            ProjectcalendarDAO projectCalendarDAO = new ProjectcalendarDAO(session);
            Projectcalendar projectCalendar = projectCalendarDAO.findByProject(new Project(idProject));

            ProjectCalendarExceptionsDAO projectCalendarExceptionsDAO = new ProjectCalendarExceptionsDAO(session);
            List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsDAO.findByRelation(Projectcalendarexceptions.PROJECTCALENDAR,
                                                                                                        projectCalendar);

            List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

            // No data
            if (idProject == -1) {
                throw new Exception(getBundle().getString("msg.error.data"));
            }

            // Boolean to info message
            boolean createFollowups = false;

            if (dateSince != null && dateUntil != null && (periodicity != null || (periodicityCount != -1 && periodicityCount > 0))) {

                // Create list dates
                //
                Calendar since      = DateUtil.getCalendar();
                Calendar until      = DateUtil.getCalendar();

                since.setTime(dateSince);
                until.setTime(dateUntil);

                Calendar sinceTemp  = since;

                List<Date> dates = new ArrayList<Date>();

                // Option generic
                if (periodicity != null) {

                    if (PeriodicityEnum.START_MONTH.equals(periodicity)) {
                        sinceTemp.setTime(DateUtil.getFirstMonthDay(sinceTemp.getTime()));
                    }
                    else if (PeriodicityEnum.HALF_MONTH.equals(periodicity)) {
                        sinceTemp.setTime(DateUtil.getFirstMonthDay(sinceTemp.getTime()));
                        sinceTemp.add(Calendar.DAY_OF_MONTH, 14);
                    }
                    else if (PeriodicityEnum.END_MONTH.equals(periodicity)) {
                        sinceTemp.setTime(DateUtil.getLastMonthDay(sinceTemp.getTime()));
                    }

                    while (sinceTemp.before(until) || DateUtil.equals(sinceTemp, until)) {

                        // This operation it's done only for skiping non working days
                        Calendar auxSinceTemp = DateUtil.getCalendar();
                        auxSinceTemp.setTime(sinceTemp.getTime());

    //                        Calendar auxSinceTemp = new GregorianCalendar(sinceTemp.get(Calendar.YEAR), sinceTemp.get(Calendar.MONTH), sinceTemp.get(Calendar.DAY_OF_MONTH));
                        if (sinceTemp.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {

                            sinceTemp.add(Calendar.DAY_OF_WEEK, -1);
                        }
                        else if(sinceTemp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {

                            sinceTemp.add(Calendar.DAY_OF_WEEK, -2);
                        }
                        // Add date

                        if (dateSince.before(sinceTemp.getTime()) || DateUtil.equals(sinceTemp.getTime(), dateSince)) {

                            dates.add(sinceTemp.getTime());
                        }

                        // Increasing calendar
                        sinceTemp = auxSinceTemp;
                        sinceTemp.add(periodicity.getCalendarField(), 1);
                    }
                }
                // Option specific
                else {

                    while (sinceTemp.before(until) || DateUtil.equals(sinceTemp, until)) {



                        Calendar auxSinceTemp = DateUtil.getCalendar();
                        auxSinceTemp.setTime(sinceTemp.getTime());

                        if (sinceTemp.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {

                            sinceTemp.add(Calendar.DAY_OF_WEEK, -1);
                        }
                        else if(sinceTemp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) {

                            sinceTemp.add(Calendar.DAY_OF_WEEK, -2);
                        }

                        // Add date
                        if (dateSince.before(sinceTemp.getTime())) {

                            dates.add(sinceTemp.getTime());
                        }

                        // Increasing calendar
                        sinceTemp = auxSinceTemp;
                        sinceTemp.add(Calendar.DAY_OF_MONTH, periodicityCount);
                    }
                }

                // Create followups
                //

                // Declare logic
                ProjectFollowupDAO projectFollowupDAO 	= new ProjectFollowupDAO(session);
                WBSNodeDAO wbsNodeDAO 				    = new WBSNodeDAO(session);

    //                es.sm2.openppm.utils.LogManager.getLog(getClass()).debug("Load IMAGE: "+requestedImage);

                for (Date date : dates) {

                    //followup control
                    if (projectFollowupDAO.findBydDate(idProject.intValue(),
                                                        new Project(idProject),
                                                        Projectfollowup.FOLLOWUPDATE,
                                                        date) == null
                            && (!exceptionDates.contains(date))) {

                        Projectfollowup followup = new Projectfollowup();

                        // Set followup
                        followup.setFollowupDate(date);
                        followup.setProject(new Project(idProject));

                        followup = projectFollowupDAO.makePersistent(followup);

                        // Set settings and globalBudget for POC and EAC
                        followup.setSettings((HashMap <String,String>)settings);
                        followup.setBudget(wbsNodeDAO.consTotalBudget(new Project(idProject)));

                        // Add the followup to the response list
                        followups.add(followup);

                        // Update boolean
                        createFollowups = true;
                    }
                }
            }
    }
    catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
    finally { SessionFactoryUtil.getInstance().close(); }

        return followups;
    }
}