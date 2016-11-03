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
 * File: ProjectLearnedLessonServlet.java
 * Create User: jordi.ripoll
 * Create Date: 09/09/2015 08:24:04
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.LearnedLessonProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.LearnedLessonLinkLogic;
import es.sm2.openppm.core.logic.security.actions.LearnedLessonTabAction;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 09/09/2015.
 */
public class ProjectLearnedLessonServlet extends AbstractGenericServlet {

    private static final long serialVersionUID = 1L;

    public final static String REFERENCE = "projectlearnedlesson";

    /**
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp)
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.service(req, resp);

        String accion = ParamUtil.getString(req, "accion");

        if (ValidateUtil.isNull(accion)) {
            accion = LearnedLessonTabAction.LEARNED_LESSON.getAction();
        }

        LogManager.getLog(getClass()).info("Accion: " + accion);

        /***************** Actions ****************/
        if (SecurityUtil.hasPermission(req, LearnedLessonTabAction.LEARNED_LESSON, accion)) {
            viewLearnedLessons(req, resp);
        }
        else if (!isForward()) {
            forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
        }
    }

    /**
     *
     *
     * @param req
     * @param resp
     */
    private void viewLearnedLessons(HttpServletRequest req, HttpServletResponse resp) {

        try {

            Integer idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));

            List<String> joins = new ArrayList<String>();
            joins.add(Project.PROGRAM);
            joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE);
            joins.add(Project.PROGRAM + "." + Program.EMPLOYEE + "." + Employee.CONTACT);
            joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
            joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
            joins.add(Project.PROJECSTDATA);

            // Declare logic
            ProjectLogic projectLogic  = new ProjectLogic(getSettings(req), getResourceBundle(req));

            // Logic
            Project project = projectLogic.consProject(new Project(idProject), joins);

            boolean hasPermission = SecurityUtil.hasPermission(req, project, Constants.TAB_LEARNED_LESSON);

            if (hasPermission) {

                // Declare logic
                LearnedLessonLinkLogic learnedLessonLinkLogic = new LearnedLessonLinkLogic();
                LearnedLessonProjectLogic projectGenLessLogic = new LearnedLessonProjectLogic();

                List<LearnedLessonLink> useds           = learnedLessonLinkLogic.findByProject(new Project(idProject));
                List<LearnedLessonProject> generateds   = projectGenLessLogic.findByProject(new Project(idProject));

                // Response
                //
                req.setAttribute("generateds", generateds);
                req.setAttribute("useds", useds);
                req.setAttribute("title", ValidateUtil.isNotNull(project.getChartLabel()) ?
                        project.getProjectName() + " ("+project.getChartLabel()+")" : project.getProjectName());
                req.setAttribute("project", project);
                req.setAttribute("tab", Constants.TAB_LEARNED_LESSON);

                forward("/index.jsp?nextForm=project/learned_lesson/learned_lesson", req, resp);
            }
            else {
                forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
            }
        }
        catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LogManager.getLog(getClass()), e);
        }
    }
}
