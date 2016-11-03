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
 * File: ProjectInitServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.javabean.ProjectInfoJavaBean;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.listener.impl.project.SaveProjectAbstractListener;
import es.sm2.openppm.core.logic.charter.ProjectCharterTemplate;
import es.sm2.openppm.core.logic.impl.CategoryLogic;
import es.sm2.openppm.core.logic.impl.ChargescostsLogic;
import es.sm2.openppm.core.logic.impl.ClassificationlevelLogic;
import es.sm2.openppm.core.logic.impl.CurrencyLogic;
import es.sm2.openppm.core.logic.impl.CustomerLogic;
import es.sm2.openppm.core.logic.impl.CustomertypeLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.logic.impl.ExecutivereportLogic;
import es.sm2.openppm.core.logic.impl.FundingsourceLogic;
import es.sm2.openppm.core.logic.impl.GeographyLogic;
import es.sm2.openppm.core.logic.impl.LabelLogic;
import es.sm2.openppm.core.logic.impl.LogprojectstatusLogic;
import es.sm2.openppm.core.logic.impl.NotificationSpecificLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProjectCharterLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.ProjectTechnologyLogic;
import es.sm2.openppm.core.logic.impl.ProjectfundingsourceLogic;
import es.sm2.openppm.core.logic.impl.ProjectlabelLogic;
import es.sm2.openppm.core.logic.impl.ResourceProfilesLogic;
import es.sm2.openppm.core.logic.impl.StagegateLogic;
import es.sm2.openppm.core.logic.impl.StakeholderLogic;
import es.sm2.openppm.core.logic.impl.StakeholderclassificationLogic;
import es.sm2.openppm.core.logic.impl.TechnologyLogic;
import es.sm2.openppm.core.logic.impl.WorkingcostsLogic;
import es.sm2.openppm.core.logic.security.actions.InitTabAction;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.ProjectTechnology;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfundingsource;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.core.model.impl.Technology;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.front.utils.BeanUtil;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.RequestUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servlet implementation class ProjectInitiatingServlet
 */
public class ProjectInitServlet extends AbstractGenericServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(ProjectInitServlet.class);

	public final static String REFERENCE = "projectinit";

	/***************** Actions ****************/
	public final static String VIEW_INVESTMENT 		= "view-investment";
	public final static String VIEW_CHARTER 		= "view-charter";
	public final static String SAVE_STAKEHOLDER		= "save-stakeholder";
	public final static String DELETE_STAKEHOLDER 	= "delete-stakeholder";

	/************** Actions AJAX **************/
	public final static String JX_SAVE_PROJECT 				= "ajax-save-project";
	public final static String JX_UPDATE_STAKEHOLDER_ORDER 	= "ajax-update-stakeholder-order";
	public final static String JX_SAVE_SELLERCOST			= "ajax-save-sellercost";
	public final static String JX_DELETE_SELLERCOST 		= "ajax-delete-sellercost";
	public final static String JX_SAVE_INFRASTRUCTURECOST	= "ajax-save-infrastructurecost";
	public final static String JX_DELETE_INFRASTRUCTURECOST = "ajax-delete-infrastructurecost";
	public final static String JX_SAVE_LICENSECOST			= "ajax-save-licensecost";
	public final static String JX_DELETE_LICENSECOST 		= "ajax-delete-licensecost";
	public final static String JX_SAVE_WORKINGCOST			= "ajax-save-workingcost";
	public final static String JX_DELETE_WORKINGCOST 		= "ajax-delete-workingcost";
    public final static String JX_UPDATE_EFFORT 		    = "ajax-update-effort";
	// FUNDINGSOURCE
	public final static String JX_SAVE_FUNDINGSOURCE		= "ajax-save-fundingsource";
	public final static String JX_DELETE_FUNDINGSOURCE		= "ajax-delete-fundingsource";
	public final static String JX_LIST_FUNDINGSOURCES		= "ajax-list-fundingsource";
	// LABEL
	public final static String JX_SAVE_LABEL				= "ajax-save-label";
	public final static String JX_DELETE_LABEL				= "ajax-delete-label";
	// EXECUTIVE REPORT
	public final static String JX_SAVE_EXECUTIVEREPORT		= "ajax-save-executivereport";

	/**
	 * @throws IOException
	 * @throws ServletException
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	super.service(req, resp);

		String accion = ParamUtil.getString(req, "accion");

		LOGGER.debug("Accion: " + accion);

		if (SecurityUtil.consUserRole(req) != -1) {

			/***************** Actions ****************/
			if (accion == null || StringPool.BLANK.equals(accion) || VIEW_INVESTMENT.equals(accion)) {
				viewInitProject(req, resp, null, accion);
			}
			else if (VIEW_CHARTER.equals(accion)) { viewProjectCharter(req, resp); }
			else if (SAVE_STAKEHOLDER.equals(accion)) { saveStakeholder(req, resp); }
			else if (DELETE_STAKEHOLDER.equals(accion)) { deleteStakeholder(req, resp); }

			/************** Actions AJAX **************/
			else if (JX_SAVE_PROJECT.equals(accion)) { saveProjectJX(req, resp); }
			else if (JX_UPDATE_STAKEHOLDER_ORDER.equals(accion)) { updateStakeholderOrderJX(req, resp); }
			else if (JX_SAVE_SELLERCOST.equals(accion)) { saveSellerCostJX(req, resp); }
			else if (JX_DELETE_SELLERCOST.equals(accion)) { deleteSellerCostJX(req, resp); }
			else if (JX_SAVE_INFRASTRUCTURECOST.equals(accion)) { saveInfrastructureCostJX(req, resp); }
			else if (JX_DELETE_INFRASTRUCTURECOST.equals(accion)) { deleteInfrastructureCostJX(req, resp); }
			else if (JX_SAVE_LICENSECOST.equals(accion)) { saveLicenseCostJX(req, resp); }
			else if (JX_DELETE_LICENSECOST.equals(accion)) { deleteLicenseCostJX(req, resp); }
			else if (JX_SAVE_WORKINGCOST.equals(accion)) { saveWorkingCostJX(req, resp); }
			else if (JX_DELETE_WORKINGCOST.equals(accion)) { deleteWorkingCostJX(req, resp); }
            else if (JX_UPDATE_EFFORT.equals(accion)) { updateEffortJX(req, resp); }
			// FUNDINGSOURCE
			else if (JX_SAVE_FUNDINGSOURCE.equals(accion)) { saveFundingsourceJX(req, resp); }
			else if (JX_DELETE_FUNDINGSOURCE.equals(accion)) { deleteFundingsourceJX(req, resp); }
			else if (JX_LIST_FUNDINGSOURCES.equals(accion)) { listFundingsourceJX(req, resp); }
			// LABEL
			else if (JX_SAVE_LABEL.equals(accion)) { saveLabelJX(req, resp); }
			else if (JX_DELETE_LABEL.equals(accion)) { deleteLabelJX(req, resp); }
			// EXECUTIVE REPORT
			else if (JX_SAVE_EXECUTIVEREPORT.equals(accion)) { saveExecutivereportJX(req, resp); }

            // New security
            else if (SecurityUtil.hasPermission(req, InitTabAction.JX_SAVE_PROJECT_TECHNOLOGIES, accion)) { saveProjectTechnologiesJX(req, resp); }
            else if (SecurityUtil.hasPermission(req, InitTabAction.JX_DELETE_PROJECT_TECHNOLOGY, accion)) { deleteProjectTechnologyJX(req, resp); }
            else if (SecurityUtil.hasPermission(req, InitTabAction.JX_SEARCH_TECHNOLOGY, accion)) { searchTechnologyJX(req, resp); }
		}
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Search technologies except those who already have the project
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void searchTechnologyJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Request
            Integer idProject = ParamUtil.getInteger(req, "idProject", -1);

            // Declare logic
            TechnologyLogic technologyLogic = new TechnologyLogic();

            // Logic search by project
            List<Technology> technologies = technologyLogic.findNotHave(new Project(idProject), getCompany(req));

            // Parse to JSON
            //
            JSONArray technologiesJSON = new JSONArray();

            for (Technology technology : technologies) {

                JSONObject technologyJSON = JSONModelUtil.technologyToJSON(technology);

                technologiesJSON.add(technologyJSON);
            }

            // Response
            out.print(technologiesJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Delete project technology
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteProjectTechnologyJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Request
            int idProjectTechnology	= ParamUtil.getInteger(req, ProjectTechnology.IDPROJECTTECHNOLOGY, -1);

            // Declare logic
            ProjectTechnologyLogic logic = new ProjectTechnologyLogic();

            // Logic delete
            logic.delete(logic.findById(idProjectTechnology));

            // Response
            out.print(infoDeleted(getResourceBundle(req), "TECHNOLOGY"));
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Save project technology
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveProjectTechnologiesJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Request
            Integer idProject           = ParamUtil.getInteger(req, "idProject", -1);
            List<Integer> idsTechnology = ParamUtil.getIntegersSplit(req, "idsTechnology");

            // Declare logic
            ProjectTechnologyLogic projectTechnologyLogic   = new ProjectTechnologyLogic();
            TechnologyLogic technologyLogic                 = new TechnologyLogic();

            List<ProjectTechnology> projectTechnologies     = new ArrayList<ProjectTechnology>();

            JSONObject infoJSON = new JSONObject();

            if (ValidateUtil.isNotNull(idsTechnology)) {

                for (Integer idTechnology : idsTechnology) {

                    ProjectTechnology projectTechnology = new ProjectTechnology();

                    // Set data
                    projectTechnology.setProject(new Project(idProject));
                    projectTechnology.setTechnology(new Technology(idTechnology));

                    // Logic save project technology
                    projectTechnology = projectTechnologyLogic.save(projectTechnology);

                    // Set technology
                    projectTechnology.setTechnology(technologyLogic.findById(idTechnology));

                    // Logic save
                    projectTechnologies.add(projectTechnology);
                }

                // Response
                //

                // Parse object to JSON
                JSONArray projTechnologiesJSON = new JSONArray();

                for (ProjectTechnology projectTechnology : projectTechnologies) {

                    JSONObject projTechJSON = new JSONObject();

                    projTechJSON.put(ProjectTechnology.IDPROJECTTECHNOLOGY, projectTechnology.getIdProjectTechnology());
                    projTechJSON.put(Technology.IDTECHNOLOGY, projectTechnology.getTechnology().getIdTechnology());
                    projTechJSON.put(Technology.NAME, projectTechnology.getTechnology().getName());
                    projTechJSON.put(Technology.DESCRIPTION, projectTechnology.getTechnology().getDescription());

                    projTechnologiesJSON.add(projTechJSON);
                };

                infoJSON.put("projTechnologies", projTechnologiesJSON);

                // Message
                infoCreated(getResourceBundle(req), infoJSON, "TECHNOLOGY");
            }


            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Update effort
     *
     * @param req
     * @param resp
     */
    private void updateEffortJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Get request
            Project project = BeanUtil.loadBean(req, Project.class, getDateFormat(req));

            // Declare Logic
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

            // Logic
            projectLogic.updateEffort(project);

            // Send message
            out.print(infoUpdated(getResourceBundle(req), new JSONObject(), "internal_effort"));
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Delete label
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteLabelJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();

		try {

            int idProjectLabel	= ParamUtil.getInteger(req, Projectlabel.IDPROJECTLABEL, -1);

            ProjectlabelLogic logic = new ProjectlabelLogic();

            logic.delete(logic.findById(idProjectLabel));

            out.print(infoDeleted(getResourceBundle(req), "label"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

    /**
     * Save label
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveLabelJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();

		Integer idProject		= ParamUtil.getInteger(req, "idProject", -1);
		int idLabel				= ParamUtil.getInteger(req, Label.IDLABEL);
		int idProjectLabel		= ParamUtil.getInteger(req, Projectlabel.IDPROJECTLABEL, -1);

		try {

			ProjectlabelLogic logic = new ProjectlabelLogic();

			Projectlabel projectlabel = null;

			JSONObject infoJSON = new JSONObject();

			if (idProjectLabel != -1) {
				projectlabel = logic.findById(idProjectLabel);
				infoUpdated(getResourceBundle(req), infoJSON, "label");
			}
			else {
				projectlabel = new Projectlabel();
				infoCreated(getResourceBundle(req), infoJSON, "label");
			}

			projectlabel.setProject(new Project(idProject));
			projectlabel.setLabel(new Label(idLabel));

			projectlabel = logic.save(projectlabel);

			infoJSON.put(Projectlabel.IDPROJECTLABEL, projectlabel.getIdProjectLabel());

			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Save executive report
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveExecutivereportJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();

		try {

			// Get parameters
			Integer idProject			= ParamUtil.getInteger(req, "idProject", -1);
	    	Date statusDate 			= ParamUtil.getDate(req, Executivereport.STATUSDATE, getDateFormat(req), null);
	    	String internal				= ParamUtil.getString(req, Executivereport.INTERNAL, null);
	    	String external				= ParamUtil.getString(req, Executivereport.EXTERNAL, null);

			Project project = new Project(idProject);

			// Instance logic
			ExecutivereportLogic logic = new ExecutivereportLogic();
			Executivereport executivereport = null;
			JSONObject infoJSON = new JSONObject();

			if (idProject == -1) {
				throw new Exception(getResourceBundle(req).getString("msg.error.data"));
			}
			executivereport = logic.findByProject(project);

			if (executivereport != null) {
				infoUpdated(getResourceBundle(req), infoJSON, "executive_report");
			}
			else {
				executivereport = new Executivereport();
				infoCreated(getResourceBundle(req), infoJSON, "executive_report");
			}

			executivereport.setProject(project);
			executivereport.setStatusDate(statusDate);
			executivereport.setInternal(internal);
			executivereport.setExternal(external);

			// Save executive report
			logic.save(executivereport);

			// Send data
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * List funding source of project
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void listFundingsourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();

    	Integer idProject = ParamUtil.getInteger(req, "id", -1);

    	try {

    		ProjectfundingsourceLogic logic = new ProjectfundingsourceLogic();

    		List<String> joins = new ArrayList<String>();
    		joins.add(Projectfundingsource.FUNDINGSOURCE);

    		List<Projectfundingsource> list = logic.findByRelation(Projectfundingsource.PROJECT, new Project(idProject), joins);

    		JSONArray itemsJSON = new JSONArray();

    		for (Projectfundingsource item : list){

    			JSONObject json = new JSONObject();
    			json.put(Projectfundingsource.IDPROJFUNDINGSOURCE, item.getIdProjFundingSource());
				json.put(Fundingsource.IDFUNDINGSOURCE, item.getFundingsource().getIdFundingSource());
				json.put(Fundingsource.NAME, item.getFundingsource().getName());
				json.put(Projectfundingsource.PERCENTAGE, item.getPercentage() == null?StringPool.BLANK: item.getPercentage());
				json.put(Projectfundingsource.EPIGRAFEEURO, ValidateUtil.isNullCh(item.getEpigrafeEuro(), StringPool.BLANK));
				json.put(Projectfundingsource.EUROS, ValidateUtil.toCurrency(item.getEuros()));
				json.put(Projectfundingsource.EPIGRAFEDOLAR, ValidateUtil.isNullCh(item.getEpigrafeDolar(), StringPool.BLANK));
				json.put(Projectfundingsource.DOLARES, ValidateUtil.toCurrency(item.getDolares()));

				itemsJSON.add(json);
    		}
    		out.print(itemsJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
     * Save Founding Source
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveFundingsourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();

		Integer idProject		= ParamUtil.getInteger(req, "idProject", -1);
		int idFundingSource		= ParamUtil.getInteger(req, Fundingsource.IDFUNDINGSOURCE);
		int idProjFundingSource	= ParamUtil.getInteger(req, Projectfundingsource.IDPROJFUNDINGSOURCE, -1);
		int percentage			= ParamUtil.getInteger(req, Projectfundingsource.PERCENTAGE);
		String epigrafeEuro		= ParamUtil.getString(req, Projectfundingsource.EPIGRAFEEURO);
		double euros			= ParamUtil.getCurrency(req, Projectfundingsource.EUROS);
		String epigrafeDolar	= ParamUtil.getString(req, Projectfundingsource.EPIGRAFEDOLAR);
		double dolares			= ParamUtil.getCurrency(req, Projectfundingsource.DOLARES);

		try {

			ProjectfundingsourceLogic logic = new ProjectfundingsourceLogic();

			Projectfundingsource projectfundingsource = null;

			JSONObject infoJSON = new JSONObject();

			if (idProjFundingSource != -1) {
				projectfundingsource = logic.findById(idProjFundingSource);
				infoUpdated(getResourceBundle(req), infoJSON, "funding_source");
			}
			else {
				projectfundingsource = new Projectfundingsource();
				infoCreated(getResourceBundle(req), infoJSON, "funding_source");
			}

			projectfundingsource.setProject(new Project(idProject));
			projectfundingsource.setFundingsource(new Fundingsource(idFundingSource));
			projectfundingsource.setPercentage(percentage);
			projectfundingsource.setEpigrafeEuro(epigrafeEuro);
			projectfundingsource.setEuros(euros);
			projectfundingsource.setEpigrafeDolar(epigrafeDolar);
			projectfundingsource.setDolares(dolares);

			projectfundingsource = logic.save(projectfundingsource);

			infoJSON.put(Projectfundingsource.IDPROJFUNDINGSOURCE, projectfundingsource.getIdProjFundingSource());
			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
	 * Delete FundingSource
	 *
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void deleteFundingsourceJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		PrintWriter out = resp.getWriter();

		try {

			int idProjFundingSource	= ParamUtil.getInteger(req, Projectfundingsource.IDPROJFUNDINGSOURCE, -1);

			ProjectfundingsourceLogic logic = new ProjectfundingsourceLogic();

			logic.delete(logic.findById(idProjFundingSource));

			out.print(infoDeleted(getResourceBundle(req), "funding_source"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


	/**
     * Delete Seller Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteSellerCostJX (HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

		int sellerId = ParamUtil.getInteger(req, "sel_id", -1);

		deleteChargeCostJX(req, resp, sellerId, "sellercost");
	}

    /**
     * Delete Infrastructure Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteInfrastructureCostJX (HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

		int sellerId = ParamUtil.getInteger(req, "inf_id", -1);

		deleteChargeCostJX(req, resp, sellerId, "infrastructurecost");
	}

    /**
     * Delete License Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteLicenseCostJX (HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

		int sellerId = ParamUtil.getInteger(req, "lic_id", -1);

		deleteChargeCostJX(req, resp, sellerId, "licensecost");
	}


    /**
     * Delete Charge Cost
     *
     * @param req
     * @param resp
     * @param id
     * @throws IOException
     */
    private void deleteChargeCostJX (HttpServletRequest req, HttpServletResponse resp, int id, String key)
    throws IOException {

    	PrintWriter out = resp.getWriter();

		try {
			ChargescostsLogic chargescostsLogic = new ChargescostsLogic();
			chargescostsLogic.delete(chargescostsLogic.findById(id));

			JSONObject infoJSON = infoDeleted(getResourceBundle(req), key);

			if (Settings.FUNDINGSOURCE_CURRENCY) {

				Project project = new Project(ParamUtil.getInteger(req, "id", -1));

				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

				project = projectLogic.updateTCVs(project);

				infoJSON.put(Project.CURRENCYOPTIONAL1, ValidateUtil.toCurrency(project.getCurrencyOptional1()));
				infoJSON.put(Project.CURRENCYOPTIONAL2, ValidateUtil.toCurrency(project.getCurrencyOptional2()));
			}

			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


    /**
     * Delete Working Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void deleteWorkingCostJX (HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

    	PrintWriter out = resp.getWriter();
		int wc_id = ParamUtil.getInteger(req, "wc_id", -1);
		try {
			WorkingcostsLogic workingcostsLogic = new WorkingcostsLogic();

			workingcostsLogic.delete(workingcostsLogic.findById(wc_id));

			JSONObject infoJSON = infoDeleted(getResourceBundle(req), "direct_cost");

			if (Settings.FUNDINGSOURCE_CURRENCY) {

				Project project = new Project(ParamUtil.getInteger(req, "id", -1));

				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

				project = projectLogic.updateTCVs(project);

				infoJSON.put(Project.CURRENCYOPTIONAL1, ValidateUtil.toCurrency(project.getCurrencyOptional1()));
				infoJSON.put(Project.CURRENCYOPTIONAL2, ValidateUtil.toCurrency(project.getCurrencyOptional2()));
			}

			out.print(infoJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

    /**
     * Delete StakeHolder
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws ServletException
     */
    private void deleteStakeholder(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		int idStakeholder	= ParamUtil.getInteger(req, "stk_id", -1);
		int idProject		= ParamUtil.getInteger(req, "project_id", -1);

		try {
			StakeholderLogic stakeholderLogic = new StakeholderLogic();

			Stakeholder stakeholder = new Stakeholder(idStakeholder);
			stakeholder.setProject(new Project(idProject));

			stakeholderLogic.delete(stakeholder);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		viewInitProject(req, resp, idProject, null);
	}

    /**
     * Update Stakeholder Order
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void updateStakeholderOrderJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();
    	String idsStr	= ParamUtil.getString(req, "ids","");
    	int idProject	= ParamUtil.getInteger(req, "idProject");

		try {

			if (!ValidateUtil.isNull(idsStr)) {

				StakeholderLogic logic = new StakeholderLogic();
				List<Stakeholder> stakeholders = logic.findByProject(new Project(idProject), Order.asc(Stakeholder.ORDERTOSHOW), Order.asc(Stakeholder.IDSTAKEHOLDER));

				Integer[] ids = StringUtil.splitStrToIntegers(idsStr, null);

				int orderToShow = 1;
				for (Integer idStakeholder :ids) {

					Stakeholder stakeholder = logic.findById(idStakeholder);
					stakeholder.setOrderToShow(orderToShow++);
					logic.save(stakeholder);
				}

				for (Stakeholder stakeholder : stakeholders) {

					boolean update = true;
					int i = 0;
					while (update && i < ids.length) {
						if (ids[i].equals(stakeholder.getIdStakeholder())) { update = false; }
						i++;
					}
					if (update) {
						stakeholder.setOrderToShow(orderToShow++);
						logic.save(stakeholder);
					}
				}
			}
			out.print(info(getResourceBundle(req), StringPool.SUCCESS, "msg.info.order_updated", new JSONObject(),"stakeholder"));
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}


    /**
     * Save Seller Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveSellerCostJX (HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		int idChargesCosts	= ParamUtil.getInteger	(req, "sel_id", -1);
		String name 		= ParamUtil.getString	(req, "sel_name", StringPool.BLANK);
		double cost 		= ParamUtil.getCurrency	(req, "sel_cost");
		int idCurrency		= ParamUtil.getInteger	(req, Currency.IDCURRENCY, -1);

		Chargescosts chargescosts = new Chargescosts();

		if (idChargesCosts != -1) {
			chargescosts.setIdChargesCosts(idChargesCosts);
		}

		chargescosts.setCurrency(idCurrency == -1?null: new Currency(idCurrency));
		chargescosts.setName(name);
		chargescosts.setCost(cost);
		chargescosts.setIdChargeType(Constants.SELLER_CHARGE_COST);

		saveChargeCostJX (req, resp, chargescosts);
	}


    /**
     * Save Infrastructure Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveInfrastructureCostJX (HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		int idChargesCosts	= ParamUtil.getInteger	(req, "inf_id", -1);
		String name 		= ParamUtil.getString	(req, "inf_name", StringPool.BLANK);
		double cost 		= ParamUtil.getCurrency	(req, "inf_cost");
		int idCurrency		= ParamUtil.getInteger	(req, Currency.IDCURRENCY, -1);

		Chargescosts chargescosts = new Chargescosts();

		if (idChargesCosts != -1) {
			chargescosts.setIdChargesCosts(idChargesCosts);
		}

		chargescosts.setCurrency(idCurrency == -1?null: new Currency(idCurrency));
		chargescosts.setName(name);
		chargescosts.setCost(cost);
		chargescosts.setIdChargeType(Constants.INFRASTRUCTURE_CHARGE_COST);

		saveChargeCostJX (req, resp, chargescosts);
	}


    /**
     * Save License Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveLicenseCostJX (HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		int idChargesCosts	= ParamUtil.getInteger	(req, "lic_id", -1);
		String name 		= ParamUtil.getString	(req, "lic_name", StringPool.BLANK);
		double cost 		= ParamUtil.getCurrency	(req, "lic_cost");
		int idCurrency		= ParamUtil.getInteger	(req, Currency.IDCURRENCY, -1);

		Chargescosts chargescosts = new Chargescosts();

		if (idChargesCosts != -1) {
			chargescosts.setIdChargesCosts(idChargesCosts);
		}

		chargescosts.setCurrency(idCurrency == -1?null: new Currency(idCurrency));
		chargescosts.setName(name);
		chargescosts.setCost(cost);
		chargescosts.setIdChargeType(Constants.LICENSE_CHARGE_COST);

		saveChargeCostJX (req, resp, chargescosts);
	}


    /**
     * Save Charge Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveChargeCostJX (HttpServletRequest req, HttpServletResponse resp, Chargescosts chargescosts)
	throws IOException {

		PrintWriter out = resp.getWriter();

		Integer idProject = ParamUtil.getInteger(req, "id", -1);
		Project project = new Project(idProject);

		try {
			ChargescostsLogic chargescostsLogic = new ChargescostsLogic();
			chargescosts.setProject(project);
			chargescostsLogic.save(chargescosts);

			JSONObject chargescostsJSON = new JSONObject();
			chargescostsJSON.put("id", chargescosts.getIdChargesCosts());

			if (Settings.FUNDINGSOURCE_CURRENCY) {

				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

				project = projectLogic.updateTCVs(project);

				chargescostsJSON.put(Project.CURRENCYOPTIONAL1, ValidateUtil.toCurrency(project.getCurrencyOptional1()));
				chargescostsJSON.put(Project.CURRENCYOPTIONAL2, ValidateUtil.toCurrency(project.getCurrencyOptional2()));
			}

			out.print(chargescostsJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

    /**
     * Save Working Cost
     *
     * @param req
     * @param resp
     * @throws IOException
     */
	private void saveWorkingCostJX (HttpServletRequest req, HttpServletResponse resp)
	throws IOException {

		PrintWriter out = resp.getWriter();

		Integer idProject = ParamUtil.getInteger(req, "id", -1);
		Project project = new Project(idProject);

		try {
			WorkingcostsLogic workingcostsLogic = new WorkingcostsLogic();

			Workingcosts workingcost = setWorkingcostFromRequest(req);
			workingcost.setProject(project);
			workingcostsLogic.save(workingcost);

			JSONObject workingcostJSON = new JSONObject();
			workingcostJSON.put("id", workingcost.getIdWorkingCosts());

			if (Settings.FUNDINGSOURCE_CURRENCY) {

				ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

				project = projectLogic.updateTCVs(project);

				workingcostJSON.put(Project.CURRENCYOPTIONAL1, ValidateUtil.toCurrency(project.getCurrencyOptional1()));
				workingcostJSON.put(Project.CURRENCYOPTIONAL2, ValidateUtil.toCurrency(project.getCurrencyOptional2()));
			}

			out.print(workingcostJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}

	/**
	 * Save StakeHolder
	 *
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void saveStakeholder(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		Integer project_id = ParamUtil.getInteger(req, "project_id", -1);

		try {
			StakeholderLogic stakeholderLogic = new StakeholderLogic();

			Stakeholder stakeholder = setStakeholderFromRequest(req);
			stakeholder.setProject(new Project(project_id));
			stakeholderLogic.save(stakeholder);

		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }

		viewInitProject(req, resp, project_id, null);
	}


	/**
     * Save Project
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void saveProjectJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    	PrintWriter out = resp.getWriter();

    	// Dependencies projects
    	String dependent 	= ParamUtil.getString(req, "infoDependens",null);
    	String leads 		= ParamUtil.getString(req, "infoLeads",null);

		Date plannedInitDate 		 	= ParamUtil.getDate(req, "plannedInitDate", getDateFormat(req), null);

		try {

			JSONArray leadsJSON			= JSONArray.fromObject(leads);
			JSONArray dependentsJSON	= JSONArray.fromObject(dependent);

			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

			Project project = RequestUtil.getInitiatingProjectFromRequest(req, getDateFormat(req));

			JSONObject projectJSON = new JSONObject();

			// Validate chart label
			boolean chartLabelInUse = projectLogic.chartLabelInUse(project, getUser(req));

			if (chartLabelInUse) {

                // Exception by setting
                if (SettingUtil.getBoolean(req, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL_DEFAULT)) {
                    throw new LogicException("msg.info.in_use", "project.chart_label", project.getChartLabel());
                }

                // Message
                info(getResourceBundle(req), StringPool.INFORMATION, "msg.info.in_use", projectJSON, "project.chart_label", project.getChartLabel());
            }

			Projectcharter projCharter = RequestUtil.getProjectCharterFromRequest(req);

			projectJSON.put("id", project.getIdProject());

			// Old project to notifications
            //
            List<String> joins = new ArrayList<String>();

            if (SettingUtil.getBoolean(getSettings(req), NotificationType.CHANGE_STAGE_GATE)) {
                joins.add(Project.STAGEGATE);
            }
            if (SettingUtil.getBoolean(getSettings(req), NotificationType.CHANGE_GEOGRAPHY)) {
                joins.add(Project.GEOGRAPHY);
            }
            if (SettingUtil.getBoolean(getSettings(req), NotificationType.ASSIGNMENT_PROJECT)) {
                joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
            }

            Project projectOld = projectLogic.findById(project.getIdProject(), joins);

			// Logic save project
			List<String> newInfo = projectLogic.saveInitiatingProject(project, projCharter, leadsJSON, dependentsJSON,
                    plannedInitDate, getUser(req));


            // Rename dir for save documents
            //

            // Dir
            String docFolderSetting    = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);
            String docFolder           = docFolderSetting.concat(File.separator + StringUtils.formatting(projectOld.getChartLabel()) + StringPool.UNDERLINE + projectOld.getIdProject());

            File dirDocs = new File(docFolder);

            if (dirDocs.isDirectory()) {

                docFolder               = docFolderSetting.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + project.getIdProject());

                boolean successRename = dirDocs.renameTo(new File(docFolder));

                if (!successRename) {
                    LogManager.getLog(getClass()).error("Rename failed");
                }
            }


			// FIXME mirar porque falla al meter la notificacion dentro del logic(no trae los contactos) son problemas de la sesion

            NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(getSettings(req));

            // Notification - The project has changed stage gate
            if (SettingUtil.getBoolean(getSettings(req), NotificationType.CHANGE_STAGE_GATE) &&
                    projectOld != null && projectOld.getStagegate() != null && project.getStagegate() != null &&
                    !projectOld.getStagegate().getIdStageGate().equals(project.getStagegate().getIdStageGate())) {

                notificationsLogic.changeStageGate(project.getIdProject(), projectOld.getStagegate());
            }

            // Notification - The project has changed geography
            if (SettingUtil.getBoolean(getSettings(req), NotificationType.CHANGE_GEOGRAPHY) &&
                    projectOld != null && projectOld.getGeography() != null && project.getGeography() != null &&
                    !projectOld.getGeography().getIdGeography().equals(project.getGeography().getIdGeography())) {

                notificationsLogic.changeGeography(project.getIdProject(), projectOld.getGeography());
            }

            // Notification - Assignment project to PM
            if (SettingUtil.getBoolean(getSettings(req), NotificationType.ASSIGNMENT_PROJECT) &&
                    projectOld != null && projectOld.getEmployeeByProjectManager() != null && project.getEmployeeByProjectManager() != null &&
                    !projectOld.getEmployeeByProjectManager().getIdEmployee().equals(project.getEmployeeByProjectManager().getIdEmployee())) {

                notificationsLogic.assignedProject(project.getIdProject());
            }

			for (String inf : newInfo) { info(getResourceBundle(req), StringPool.INFORMATION, inf, projectJSON); }

			infoUpdated(getResourceBundle(req), projectJSON, "project");

			project = projectLogic.consProject(project.getIdProject());
			projectJSON.put(Project.PLANNEDINITDATE, formatDate(req, project.getPlannedInitDate()));
			projectJSON.put(Project.PLANNEDFINISHDATE, formatDate(req, project.getPlannedFinishDate()));

            // Call to listener
            ProcessListener.getInstance().process(
                    SaveProjectAbstractListener.class,
                    projectOld,
                    project,
                    getExtraData(req));

			out.print(projectJSON);
		}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally {
			out.close();
		}
	}


    /**
	 * View Initiating Project
	 *
	 * @param req
	 * @param resp
	 * @param idProject
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewInitProject(HttpServletRequest req,
			HttpServletResponse resp, Integer idProject, String accion) throws ServletException, IOException {

		ProjectInfoJavaBean projectInfo = null;

		String type = ParamUtil.getString(req, "type");

		if (VIEW_INVESTMENT.equals(accion) || String.valueOf(Constants.TYPE_INVESTMENT).equals(type)) {
			req.setAttribute("type", Constants.TYPE_INVESTMENT);
			req.setAttribute("tab", Constants.TAB_INVESTMENT);
		}
		else {
			req.setAttribute("tab", Constants.TAB_INITIATION);
		}

		if (idProject == null) {
			idProject = ParamUtil.getInteger(req, "id", (Integer)req.getSession().getAttribute("idProject"));
		}

		boolean hasPermission = false;

		try {
			DocumentprojectLogic documentprojectLogic 	= new DocumentprojectLogic(getSettings(req), getResourceBundle(req));
			ProjectLogic projectLogic 					= new ProjectLogic(getSettings(req), getResourceBundle(req));

			List<Documentproject> docs = new ArrayList<Documentproject>();

			Project project = new Project(idProject);

			hasPermission = SecurityUtil.hasPermission(req, project, Constants.TAB_INITIATION);

			if (hasPermission) {
				ChargescostsLogic chargescostsLogic			= new ChargescostsLogic();
				ResourceProfilesLogic resourceProfilesLogic = new ResourceProfilesLogic();

				List<Resourceprofiles> profiles = resourceProfilesLogic.findAll();

				PerformingOrgLogic performingOrgLogic = new PerformingOrgLogic();
				List<Performingorg> perfOrgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Order.ASC);

				projectInfo = projectLogic.findById(idProject, getUser(req).getPerformingorg(), getCompany(req));

                //Update effort if check enabled
                //
                if (SettingUtil.getBoolean(getSettings(req), GeneralSetting.SUMMATION_EFFORT)) {

                    Project projectTemp = projectInfo.getProject();

                    int summationEffort = 0;
                    for (Workingcosts workingcost : projectTemp.getWorkingcostses()) {
                        summationEffort += workingcost.getEffort() != null ? workingcost.getEffort() : 0;
                    }

                    // Set summation
                    projectTemp.setEffort(summationEffort);

                    // Logic update project
                    projectLogic.save(projectTemp);

                    // Set update project
                    projectInfo.setProject(projectTemp);
                }

				List<Chargescosts> sellersCosts = chargescostsLogic.consChargescostsByProject(
						project, Constants.SELLER_CHARGE_COST);
				List<Chargescosts> infrastructureCosts = chargescostsLogic.consChargescostsByProject(
						project, Constants.INFRASTRUCTURE_CHARGE_COST);
				List<Chargescosts> licensesCosts = chargescostsLogic.consChargescostsByProject(
						project, Constants.LICENSE_CHARGE_COST);

				String documentStorage = SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);

				// Get documents
				docs = documentprojectLogic.getDocuments(getSettings(req), project, Constants.DOCUMENT_INITIATING);

				req.setAttribute("docs", docs);
				req.setAttribute("documentStorage", documentStorage);

				StakeholderLogic stakeholderLogic		            = new StakeholderLogic();
				CategoryLogic categoryLogic				            = new CategoryLogic();
				GeographyLogic geoLogic					            = new GeographyLogic();
				CustomerLogic customerLogic 			            = new CustomerLogic();
				CustomertypeLogic customertypeLogic 	            = new CustomertypeLogic();
				FundingsourceLogic fundingsourceLogic 	            = new FundingsourceLogic();
				LogprojectstatusLogic logprojectLogic	            = new LogprojectstatusLogic();
				CurrencyLogic currencyLogic				            = new CurrencyLogic();
				LabelLogic labelLogic					            = new LabelLogic();
				ProjectlabelLogic projectlabelLogic		            = new ProjectlabelLogic();
				StagegateLogic stagegateLogic			            = new StagegateLogic();
				ExecutivereportLogic executivereportLogic	        = new ExecutivereportLogic();
				StakeholderclassificationLogic classificationLogic  = new StakeholderclassificationLogic();
				ClassificationlevelLogic classificationlevelLogic   = new ClassificationlevelLogic();
				ProjectfundingsourceLogic projectfundingsourceLogic = new ProjectfundingsourceLogic();
                TechnologyLogic technologyLogic                     = new TechnologyLogic();
                ProjectTechnologyLogic projectTechnologyLogic       = new ProjectTechnologyLogic();

				// Find funding sources of project
	    		List<String> joins = new ArrayList<String>();
				joins.add(Projectfundingsource.FUNDINGSOURCE);

				req.setAttribute("fundingsources", fundingsourceLogic.findByCompany(getUser(req)));
				req.setAttribute("projFundingsources", projectfundingsourceLogic.findByRelation(Projectfundingsource.PROJECT, project, joins));

				// Find labels of project
				joins = new ArrayList<String>();
				joins.add(Projectlabel.LABEL);
				req.setAttribute("projectLabels", projectlabelLogic.findByRelation(Projectlabel.PROJECT, project, joins));

                // Find technologies of project
                joins = new ArrayList<String>();
                joins.add(ProjectTechnology.TECHNOLOGY);
                req.setAttribute("projectTechnologies", projectTechnologyLogic.findByRelation(ProjectTechnology.PROJECT, project, joins));

				// Find executive report
				req.setAttribute("executivereport", executivereportLogic.findByProject(project));

				req.setAttribute("sellersCosts", sellersCosts);
				req.setAttribute("infrastructureCosts", infrastructureCosts);
				req.setAttribute("licensesCosts", licensesCosts);

				// Classification level
				List<Classificationlevel> classificationsLevel = classificationlevelLogic.findByRelation(Classificationlevel.COMPANY, getCompany(req), Classificationlevel.NAME, Constants.ASCENDENT);
				req.setAttribute("classificationsLevel", classificationsLevel);

				// Warning for select correct classification level
				Classificationlevel classificationlevel = null;

				Double tcv = project.getTcv()== null? new Double(0):project.getTcv();

				boolean foundClassification = false;
				for (Classificationlevel temp : classificationsLevel) {

					if (temp.getThresholdMin() != null
							|| temp.getThresholdMax() != null) {
						foundClassification = true;
					}

					if (temp.getThresholdMin() != null && temp.getThresholdMax() != null
							&& tcv > temp.getThresholdMin()
							&& tcv <= temp.getThresholdMax()) {
						classificationlevel = temp;
					}
					else if (temp.getThresholdMin() != null && tcv < temp.getThresholdMin()) {
						classificationlevel = temp;
					}
					else if (temp.getThresholdMax() != null && tcv >= temp.getThresholdMax()) {
						classificationlevel = temp;
					}
					else if (classificationlevel == null) {
						classificationlevel = temp;
					}
				}
				req.setAttribute("classificationlevel", foundClassification?classificationlevel:null);

				joins = new ArrayList<String>();
				joins.add(Logprojectstatus.EMPLOYEE);
				joins.add(Logprojectstatus.EMPLOYEE+"."+ Employee.CONTACT);
				req.setAttribute("stageGates", stagegateLogic.findByRelation(Stagegate.COMPANY, getCompany(req), Stagegate.NAME, Order.ASC));
				req.setAttribute("labels", labelLogic.findEnableByCompany(getUser(req)));
                req.setAttribute("technologies", technologyLogic.findByRelation(Stagegate.COMPANY, getCompany(req), Stagegate.NAME, Order.ASC));
				req.setAttribute("currencies", currencyLogic.findByRelation(Currency.COMPANY, getCompany(req)));
				req.setAttribute("logs", logprojectLogic.findByRelation(Logprojectstatus.PROJECT, projectInfo.getProject(), joins));
				req.setAttribute("geographies", geoLogic.findByRelation(Geography.COMPANY, getCompany(req), Geography.NAME, Order.ASC));
				req.setAttribute("categories", categoryLogic.findByCompanyAndEnabled(getCompany(req), projectInfo.getProject().getCategory()));
				req.setAttribute("customers", customerLogic.searchByCompany(getCompany(req)));
				req.setAttribute("customertypes", customertypeLogic.findByCompany(getUser(req)));
				req.setAttribute("project", projectInfo.getProject());
				req.setAttribute("stakeholders", stakeholderLogic.findByRelation(Stakeholder.PROJECT, projectInfo.getProject(),
							DataUtil.toList(Logprojectstatus.EMPLOYEE, Logprojectstatus.EMPLOYEE + "." + Employee.CONTACT, Stakeholder.STAKEHOLDERCLASSIFICATION)));
				req.setAttribute("stakeholderClassifications", classificationLogic.findByRelation(
						Stakeholderclassification.COMPANY, getCompany(req), Stakeholderclassification.NAME, Order.ASC));
				req.setAttribute("projCharter", projectInfo.getProjectCharter());
				req.setAttribute("employees", projectInfo.getEmployees());
				req.setAttribute("programs", projectInfo.getPrograms());
				req.setAttribute("contractTypes", projectInfo.getContractTypes());
				req.setAttribute("profiles", profiles);
				req.setAttribute("perforgs", perfOrgs);
				req.setAttribute("fundingsources", fundingsourceLogic.findByCompany(getUser(req)));
				req.setAttribute("performingOrgs", performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req), Performingorg.NAME, Constants.ASCENDENT));
				req.setAttribute("title", ValidateUtil.isNotNull(projectInfo.getProject().getChartLabel()) ?
						projectInfo.getProject().getProjectName() + " ("+projectInfo.getProject().getChartLabel()+")" : projectInfo.getProject().getProjectName());
			}
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));

		if (hasPermission) {
			forward("/index.jsp?nextForm=project/initiation/initiating_project", req, resp);
		}
		else {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}


	/**
	 * View Project Charter
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewProjectCharter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer idProject			= ParamUtil.getInteger(req, "id", -1);
		Integer idProjectCharter	= ParamUtil.getInteger(req, "idprojectcharter", -1);

        boolean error		= false;

        try {

            GenerateReportInterface generateReport = CacheStatic.getGenerateReport(ReportType.PROJECT_CHARTER);

            if (generateReport != null) {

                // Call to generate report file
                ReportFile reportFile = generateReport.generate(new ReportParams(new Project(idProject), getResourceBundle(req), getSettings(req), getUser(req)));

                if (reportFile == null) {
                    error = true;
                }
                else {
                    // Send report
                    sendFile(req, resp, reportFile.getFile(), reportFile.getName()+ reportFile.getExtension());
                }
            }
            else {

                // TODO javier.hernandez - 21/03/2015 - deprecated code migrate to GenerateReportInterface

                Project proj		= null;
                byte[] charterDoc	= null;


                ProjectCharterLogic projectCharterLogic = new ProjectCharterLogic();
                ProjectLogic projectLogic 				= new ProjectLogic(getSettings(req), getResourceBundle(req));

                proj = projectLogic.consProject(idProject);
                Projectcharter projCharter = projectCharterLogic.consProjectCharter(idProjectCharter);

                ProjectCharterTemplate projCharterTemplate = ProjectCharterTemplate.getProjectChaterTemplate(getSettings(req));

                File charterDocFile = projCharterTemplate.generateCharter(
                        getResourceBundle(req), proj, projCharter, getSettings(req), getUser(req).getContact().getFullName());

                if (charterDocFile != null) {
                    // parse file to bytes
                    charterDoc = DocumentUtils.getBytesFromFile(charterDocFile);

                    // delete temporal file
                    DocumentUtils.deleteFile(charterDocFile);
                }

                if (charterDoc != null) {
                    sendFile(req, resp, charterDoc, proj.getProjectName() + "_" + proj.getAccountingCode() + SettingUtil.getString(getSettings(req), Settings.SETTING_PROJECT_CHARTER_EXTENSION, Settings.DEFAULT_PROJECT_CHARTER_EXTENSION));
                }
                else {
                    error = true;
                }
            }
		}
		catch (Exception e) {
			error = true;
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}

        // Redirect to initiation
        if (error) {
            viewInitProject(req, resp, idProject, null);
        }

	}


	/**
	 * Set Stakeholder instance from HTTPRequest parameters
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	private Stakeholder setStakeholderFromRequest(HttpServletRequest req) throws Exception {
		Stakeholder stakeholder = null;

		// Get parameters
		int idStakeholder		= ParamUtil.getInteger(req, "stk_id", -1);
		String rol				= ParamUtil.getString(req, "stk_rol", StringPool.BLANK);
		Integer	idClassification= ParamUtil.getInteger(req, "stk_classification", null);
		Character type			= ParamUtil.getString (req, "stk_type", StringPool.BLANK).charAt(0);
		String businessNeed		= ParamUtil.getString(req, "stk_businessneed", StringPool.BLANK);
		String expectations		= ParamUtil.getString(req, "stk_expectations", StringPool.BLANK);
		String influence		= ParamUtil.getString(req, "stk_influence", StringPool.BLANK);
		String mgtStrategy		= ParamUtil.getString(req, "stk_mgtstrategy", StringPool.BLANK);
		String contactName		= ParamUtil.getString(req, "stk_contact_name", StringPool.BLANK);
		String department		= ParamUtil.getString(req, "stk_department", StringPool.BLANK);
		int order				= ParamUtil.getInteger(req, "stk_order", 100);
		Integer idEmployee		= ParamUtil.getInteger(req, "stk_contact", null);
		String comments			= ParamUtil.getString(req, "stk_comments", StringPool.BLANK);

		// Set stakeholder's parameters
		if (idStakeholder != -1 || !StringPool.BLANK.equals(contactName)
				|| !StringPool.BLANK.equals(rol) || !StringPool.BLANK.equals(businessNeed)
				|| !StringPool.BLANK.equals(expectations) || !StringPool.BLANK.equals(influence)
				|| !StringPool.BLANK.equals(mgtStrategy)) {

			stakeholder = new Stakeholder();

			// Search stakeholder by id if it's passed by request
			if (idStakeholder != -1) {

				StakeholderLogic logic = new StakeholderLogic();
				stakeholder = logic.findById(idStakeholder);
				order = stakeholder.getOrderToShow();
			}

			// Set parameters
			stakeholder.setContactName(contactName);
			stakeholder.setProjectRole(rol);
			stakeholder.setType(type);
			stakeholder.setRequirements(businessNeed);
			stakeholder.setExpectations(expectations);
			stakeholder.setInfluence(influence);
			stakeholder.setMgtStrategy(mgtStrategy);
			stakeholder.setDepartment(department);
			stakeholder.setOrderToShow(order);
			stakeholder.setComments(comments);

			// Set employee
			if (idEmployee == null) {

				stakeholder.setEmployee(null);
			}
			else {

				stakeholder.setContactName(null);
				stakeholder.setEmployee(new Employee(idEmployee));
			}

			// Set stakeholder classification
			if (idClassification == null) {

				stakeholder.setStakeholderclassification(null);
			}
			else {

				stakeholder.setStakeholderclassification(new Stakeholderclassification(idClassification));
			}

		}

		return stakeholder;
	}


	/**
	 * Set Workingcost instance from HTTPRequest parameters
	 *
	 * @param req
	 * @return
	 */
	private Workingcosts setWorkingcostFromRequest (HttpServletRequest req) {

		Workingcosts workingcost = null;

		int idWorkingCost			= ParamUtil.getInteger(req, "wc_id", -1);

		String resourceName			= ParamUtil.getString (req, "wc_resource", StringPool.BLANK);
		String resourceDepartment	= ParamUtil.getString (req, "wc_department", StringPool.BLANK);
		int effort					= ParamUtil.getInteger(req, "wc_effort", -1);
		int realEffort				= ParamUtil.getInteger(req, "wc_real_effort", 0);
		double rate					= ParamUtil.getCurrency (req, "wc_rate", -1d);
		Integer q1					= ParamUtil.getInteger(req, Workingcosts.Q1, null);
		Integer q2					= ParamUtil.getInteger(req, Workingcosts.Q2, null);
		Integer q3					= ParamUtil.getInteger(req, Workingcosts.Q3, null);
		Integer q4					= ParamUtil.getInteger(req, Workingcosts.Q4, null);
		int idCurrency				= ParamUtil.getInteger	(req, Currency.IDCURRENCY, -1);

		if (idWorkingCost != -1 || effort != -1 || rate != -1) {

			workingcost = new Workingcosts();

			if (idWorkingCost != -1) {
				workingcost.setIdWorkingCosts(idWorkingCost);
			}

			workingcost.setCurrency(idCurrency == -1?null: new Currency(idCurrency));
			workingcost.setResourceName(resourceName);
			workingcost.setResourceDepartment(resourceDepartment);
			workingcost.setEffort(effort);
			workingcost.setRealEffort(realEffort);
			workingcost.setRate(rate);
			workingcost.setWorkCost(effort * rate);
			workingcost.setQ1(q1);
			workingcost.setQ2(q2);
			workingcost.setQ3(q3);
			workingcost.setQ4(q4);
		}

		return workingcost;
	}
}
