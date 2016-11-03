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
 * Module: plugin-project-change
 * File: ProjectChangeOpenPPM.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 16:52:47
 */

package es.sm2.openppm.plugin.change.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.ChangeControlDAO;
import es.sm2.openppm.core.dao.ChangerequestwbsnodeDAO;
import es.sm2.openppm.core.dao.ChargescostsDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.WorkingcostsDAO;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.ReportDataSource;
import es.sm2.openppm.core.reports.annotations.ReportConfiguration;
import es.sm2.openppm.core.reports.annotations.ReportExtension;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.NombreReport;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.reports.exceptions.ReportException;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.plugin.change.model.ChangeReport;
import es.sm2.openppm.plugin.change.model.ProjectActivityReport;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.DocumentUtils;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.HtmlUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.web.servlets.ImageServlet;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
@ReportConfiguration(type = ReportType.CHANGE_REQUEST)
public class ProjectChangeOpenPPM implements GenerateReportInterface {

    private static final String PATH_REPORT 			= "/es/sm2/openppm/plugin/change/jasper/";
    private static final String JASPERFILE				= "ProjectChangeOpenPPM.jasper";

    @Override
    public ReportFile generate(ReportParams reportParams) throws ReportException {


        LogManager.getLog(this.getClass()).debug("GENERATE CHANGE REQUEST" + this.getClass());

        ReportFile reportFile = null;
        Transaction tx 		= null;
        Session session;

        try {

            session = SessionFactoryUtil.getInstance().getCurrentSession();
            tx = session.beginTransaction();

            final ReportConfiguration configuration = this.getClass().getAnnotation(ReportConfiguration.class);

            // Set extension
            //
            String extension = configuration.extension().data();

            if (SettingUtil.getString(reportParams.getSettings(), Settings.SettingType.REPORT_EXTENSION).equals(ReportExtension.ODT.name())) {
                extension = ReportExtension.ODT.data();
            }
            else if (SettingUtil.getString(reportParams.getSettings(), Settings.SettingType.REPORT_EXTENSION).equals(ReportExtension.DOC.name())) {
                extension = ReportExtension.DOC.data();
            }

            // Create DAOS
            ProjectDAO projectDAO = new ProjectDAO(session);
            ChangeControlDAO changeDAO = new ChangeControlDAO(session);

            // Find data
            Project project = projectDAO.findById(reportParams.getProject().getIdProject());
            Changecontrol change = changeDAO.findById((Integer) reportParams.getParam(ReportParams.Type.CHANGE_ID));

            // Set data for generate report
            ChangeReport changeReport = new ChangeReport();
            changeReport.setTranslationMap(generateTranslationMap(reportParams.getBundle()));
            changeReport.setPo(ValidateUtil.isNullCh(project.getPerformingorg().getName(), StringPool.BLANK));
            changeReport.setProjectName(ValidateUtil.isNullCh(project.getProjectName(), StringPool.BLANK));
            changeReport.setProgram(ValidateUtil.isNullCh(project.getProgram().getProgramName(), StringPool.BLANK));
            changeReport.setPriority(project.getPriority() + StringPool.BLANK);

            if (project.getEmployeeByProjectManager() != null) {
                changeReport.setProjectManager(project.getEmployeeByProjectManager().getContact().getFullName());
            }

            if (project.getEmployeeByFunctionalManager() != null) {
                changeReport.setFunctionalManager(project.getEmployeeByFunctionalManager().getContact().getFullName());
            }

            if (project.getCustomer() != null) {

                String customer = (project.getCustomer().getCustomertype() != null?project.getCustomer().getCustomertype().getName()+StringPool.BLANK_DASH:StringPool.BLANK);
                changeReport.setCustomer(customer+project.getCustomer().getName());
            }
            else { changeReport.setCustomer(StringPool.BLANK); }

            changeReport.setOriginator(ValidateUtil.isNullCh(change.getOriginator(), StringPool.BLANK));
            changeReport.setChangeType(change.getChangetype() != null && change.getChangetype().getDescription() != null ? change.getChangetype().getDescription() : StringPool.BLANK);
            changeReport.setChangeDescription(ValidateUtil.isNullCh(change.getDescription(), StringPool.BLANK));
            changeReport.setRecommendedSolution(ValidateUtil.isNullCh(change.getRecommendedSolution(), StringPool.BLANK));
            changeReport.setImpactDescription(ValidateUtil.isNullCh(change.getImpactDescription(), StringPool.BLANK));
            changeReport.setValidatedBy(ValidateUtil.isNullCh(change.getResolutionName(), StringPool.BLANK));
            changeReport.setComments(ValidateUtil.isNullCh(change.getResolutionReason(), StringPool.BLANK));
            changeReport.setRequestDate(DateUtil.format(reportParams.getBundle(), change.getChangeDate()));
            changeReport.setResolutionDate(DateUtil.format(reportParams.getBundle(), change.getResolutionDate()));

            ChangerequestwbsnodeDAO changerequestwbsnodeDAO = new ChangerequestwbsnodeDAO(session);
            List<Changerequestwbsnode> changerequestwbsnodeList = changerequestwbsnodeDAO.findByChangeControl(change);

            if (ValidateUtil.isNotNull(changerequestwbsnodeList)) {

                List<ProjectActivityReport> activities = new ArrayList<ProjectActivityReport>();

                for (Changerequestwbsnode changerequestwbsnode : changerequestwbsnodeList) {

                    ProjectActivityReport activityReport = new ProjectActivityReport();

                    // Name
                    activityReport.setName(changerequestwbsnode.getWbsnode() != null && changerequestwbsnode.getWbsnode().getName() != null ?
                            changerequestwbsnode.getWbsnode().getName(): StringPool.BLANK);

                    activityReport.setEstimatedEffort(changerequestwbsnode.getEstimatedEffort() == null ? StringPool.BLANK : ValidateUtil.toCurrency(changerequestwbsnode.getEstimatedEffort()));
                    activityReport.setEstimatedCost(changerequestwbsnode.getEstimatedCost() == null ? StringPool.BLANK : ValidateUtil.toCurrency(changerequestwbsnode.getEstimatedCost()));


                    activities.add(activityReport);
                }

                changeReport.setActivities(new ReportDataSource<ProjectActivityReport>(activities));
            }
            // State Change
            if (ValidateUtil.isNull(changerequestwbsnodeList)) {

                changeReport.setStatus(reportParams.getBundle().getString("change.open"));
            }
            else if (change.getResolution() == null) {
                changeReport.setStatus(reportParams.getBundle().getString("analyzed"));
            }
            else if (change.getResolution()) {
                changeReport.setStatus(reportParams.getBundle().getString("labels.accepted"));
            }
            else {
                changeReport.setStatus(reportParams.getBundle().getString("change.rejected"));
            }

            // Budget
            ChargescostsDAO chargescostsDAO = new ChargescostsDAO(session);

            List<Chargescosts> sellersCosts = chargescostsDAO.consChargescostsByProject(project, Constants.SELLER_CHARGE_COST);

            double totalSellersCost = 0;

            List<NombreReport> sellers = new ArrayList<NombreReport>();

            for (Chargescosts sellerCost : sellersCosts) {

                sellers.add(new NombreReport(
                        HtmlUtil.escape(sellerCost.getName()),
                        ValidateUtil.toCurrency(sellerCost.getCost())));

                totalSellersCost += sellerCost.getCost();
            }

            // Expenses - Coste de infraestructura
            //
            List<Chargescosts> infrastructureCosts = chargescostsDAO.consChargescostsByProject(project, Constants.INFRASTRUCTURE_CHARGE_COST);

            List<NombreReport> infrastructures = new ArrayList<NombreReport>();

            double totalInfraestructureCost = 0;

            for (Chargescosts infraCost : infrastructureCosts) {

                infrastructures.add(new NombreReport(
                        HtmlUtil.escape(infraCost.getName()),
                        ValidateUtil.toCurrency(infraCost.getCost()) ));

                totalInfraestructureCost += infraCost.getCost();
            }

            // Expenses - Licencias.
            //
            List<Chargescosts> licensesCosts = chargescostsDAO.consChargescostsByProject(project, Constants.LICENSE_CHARGE_COST);

            List<NombreReport> licenses = new ArrayList<NombreReport>();

            double totalLicensesCost = 0;

            for (Chargescosts licenseCost : licensesCosts) {

                totalLicensesCost += licenseCost.getCost();

                licenses.add(new NombreReport(
                        HtmlUtil.escape(licenseCost.getName()),
                        ValidateUtil.toCurrency(licenseCost.getCost()) ));
            }

            double totalWorkCost = 0;

            WorkingcostsDAO workingcostsDAO = new WorkingcostsDAO(session);
            List<Workingcosts> workingCostList = workingcostsDAO.consByProject(project);

            for (Workingcosts workingcost : workingCostList) {

                totalWorkCost 	+= workingcost.getWorkCost();
            }

            double expenses = totalSellersCost + totalInfraestructureCost + totalLicensesCost;
            changeReport.setBudget(ValidateUtil.toCurrency(expenses + totalWorkCost) );

            // Parameters
            Map<String, Object> parameters = new HashMap<String, Object>();

            // Find files in Jar
            InputStream logoFile = ImageServlet.class.getResourceAsStream("/"+Settings.LOGO);
            InputStream jasperFile  = null; // ReportUtil.getInstance().getDeveloperInputStream("D://iReport/" + JASPERFILE);
//            InputStream jasperFile  =  ReportUtil.getInstance().getDeveloperInputStream("D://iReport/" + JASPERFILE);

            // Load production mode
            if (jasperFile == null) {
                jasperFile = this.getClass().getResourceAsStream(PATH_REPORT + JASPERFILE);
            }

            changeReport.setLogo(logoFile);

            // Data source
            ReportDataSource<ChangeReport> dataSource = new ReportDataSource<ChangeReport>(DataUtil.toList(changeReport));

            // Create report
            JasperPrint print = JasperFillManager.fillReport(jasperFile, parameters, dataSource);

            // Close inputs
            jasperFile.close();
            logoFile.close();

            String fileName = StringUtil.clearSlashes(changeReport.getProjectName() + StringPool.BLANK_DASH + "Change Request");

            File file = new File(Settings.TEMP_DIR_GENERATE_DOCS + Constants.PATH_SEPARATOR + fileName + extension);

            // Format exporter
            //
            if (extension.equals(ReportExtension.ODT.data())) {

                JROdtExporter exporter = new JROdtExporter();

                exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE, file);
                exporter.exportReport();
            }
            else if (extension.equals(ReportExtension.DOC.data())) {

                JRDocxExporter exporter = new JRDocxExporter();

                exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE, file);
                exporter.exportReport();
            }

            // Create response file
            reportFile = new ReportFile();
            reportFile.setExtension(extension);
            reportFile.setFile(DocumentUtils.getBytesFromFile(file));
            reportFile.setName(fileName);

            // delete temporal file
            DocumentUtils.deleteFile(file);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new ReportException(e);
        }
        finally {
            SessionFactoryUtil.getInstance().close();
        }

        return reportFile;
    }

    /**
     * Generate translation map
     *
     * @param bundle
     * @return translatioMap
     */
    private HashMap<String, String> generateTranslationMap(ResourceBundle bundle) {


        HashMap<String, String> translation = new HashMap<String, String>();

        translation.put("CHANGE_REQUEST_FORM", bundle.getString("change_request"));
        translation.put("PROJECT_NAME", bundle.getString("project_name"));
        translation.put("CUSTOMER", bundle.getString("change_request.customer"));
        translation.put("BUDGET", bundle.getString("wbs.budget"));
        translation.put("PROJECT_MANAGER", bundle.getString("change_request.project_manager"));
        translation.put("FUNCTIONAL_MANAGER", bundle.getString("change_request.business_manager"));
        translation.put("PROGRAM", bundle.getString("maintenance.program"));
        translation.put("PRIORITY", bundle.getString("change.priority"));
        translation.put("ORIGINATOR", bundle.getString("change.originator"));
        translation.put("REQUEST_DATE", bundle.getString("change_request.date"));
        translation.put("INFORMATION", bundle.getString("msg.info"));
        translation.put("CHANGE_TYPE", bundle.getString("change_type"));
        translation.put("CHANGE_DESCRIPTION", bundle.getString("change.desc"));
        translation.put("RECOMMENDED_SOLUTION", bundle.getString("change.recommended_solution"));
        translation.put("AFFECTED_ACTIVITIES", bundle.getString("CHANGE.AFFECTED_ACTIVITIES"));
        translation.put("ESTIMATED_EFFORT", bundle.getString("change.estimated_effort"));
        translation.put("ESTIMATED_COST", bundle.getString("change.estimated_cost"));
        translation.put("IMPACT_DESCRIPTION", bundle.getString("change.impact_desc"));
        translation.put("FINAL_DISPOSITION", bundle.getString("risk.final_disposition"));
        translation.put("STATUS", bundle.getString("proposal.status"));
        translation.put("VALIDATE_BY", bundle.getString("CHANGE.RESOLUTION_APPROVED_BY"));
        translation.put("RESOLUTION_DATE", bundle.getString("change.resolution_date"));
        translation.put("COMMENTS", bundle.getString("comments"));
        translation.put("SIGN_OFF", bundle.getString("SIGN_OFF"));

        return translation;
    }
}
