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
 * File: PaymentProjectsServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

package es.sm2.openppm.front.servlets;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ProcurementpaymentsLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.logic.security.actions.PaymentProjectAction;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jordi.ripoll on 11/09/2014.
 */
public class PaymentProjectsServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 1L;

	public final static Logger LOGGER = Logger.getLogger(PaymentProjectsServlet.class);

	public final static String REFERENCE = "paymentprojects";

	/***************** Actions ****************/


	/************** Actions AJAX **************/
    public final static String JX_CONSULT_DOCUMENTS	= "ajax-consult-documents";



	/**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		super.service(req, resp);

		String accion = ParamUtil.getString(req, "accion");
        if (ValidateUtil.isNull(accion)) { accion = PaymentProjectAction.PAYMENT_PROJECTS.getAction(); }

		LOGGER.debug("Accion: " + accion);

        /***************** Actions ****************/
		if (SecurityUtil.hasPermission(req, PaymentProjectAction.PAYMENT_PROJECTS, accion)) {
            viewPaymentProjects(req, resp);
        }
        /************** Actions AJAX **************/
        else if (SecurityUtil.hasPermission(req, PaymentProjectAction.JX_CONSULT_SELLERS, accion)) {
            consultSellersJX(req, resp);
        }
        else if (SecurityUtil.hasPermission(req, PaymentProjectAction.JX_ADD_PAYMENT, accion)) {
            addPaymentJX(req, resp);
        }
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE + "?accion=403", req, resp);
		}
	}

    /**
     * Add payment
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void addPaymentJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Request
            Integer idProject   = ParamUtil.getInteger(req, "idProject", -1);
            Integer idSeller 	= ParamUtil.getInteger(req, "idSeller", -1);
            String order		= ParamUtil.getString(req, Procurementpayments.PURCHASEORDER, StringPool.BLANK);
            Date actualDate		= ParamUtil.getDate(req, Procurementpayments.ACTUALDATE, getDateFormat(req), null);
            Double actualPay 	= ParamUtil.getCurrency(req, Procurementpayments.ACTUALPAYMENT, 0);
            String info			= ParamUtil.getString(req, Procurementpayments.PAYMENTSCHEDULEINFO, StringPool.BLANK);
            String concept      = ParamUtil.getString(req, Procurementpayments.CONCEPT, StringPool.BLANK);
            Boolean deductible  = ParamUtil.getBoolean(req, Procurementpayments.DEDUCTIBLE);

            JSONObject infoJSON = new JSONObject();

            if (idSeller == -1 || idProject == -1) {
                throw new Exception(getResourceBundle(req).getString("msg.error.data"));
            }

            // Declare logic
            ProcurementpaymentsLogic procurementpaymentsLogic 	= new ProcurementpaymentsLogic();
            SellerLogic sellerLogic 							= new SellerLogic();
            ProjectLogic projectLogic 							= new ProjectLogic(getSettings(req), getResourceBundle(req));

            // Set data
            Procurementpayments payment = new Procurementpayments();

            payment.setSeller(sellerLogic.findById(idSeller, false));
            payment.setProject(projectLogic.consProject(idProject));
            payment.setPurchaseOrder(order);
            payment.setActualDate(actualDate);
            payment.setActualPayment(actualPay);
            payment.setPaymentScheduleInfo(info);
            payment.setConcept(concept);
            payment.setDeductible(deductible);

            // Logic save
            procurementpaymentsLogic.save(payment);

            // Response
            infoJSON = infoCreated(getResourceBundle(req), infoJSON, "payment.new");

            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }
    }

    /**
     * Consult sellers by procurement
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void consultSellersJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            int idProject = ParamUtil.getInteger(req, "idProject");

            JSONObject infoJSON = new JSONObject();

            if (idProject != -1) {

                // Declare logic
                SellerLogic sellerLogic = new SellerLogic();

                // Get sellers by procurement
                List<Seller> sellers = sellerLogic.findByProcurement(new Project(idProject));

                if (ValidateUtil.isNotNull(sellers)) {

                    // Parse JSON
                    //
                    JSONArray sellersJSON = new JSONArray();

                    for (Seller seller : sellers) {

                        JSONObject sellerJSON = new JSONObject();

                        sellerJSON.put("idSeller", seller.getIdSeller());
                        sellerJSON.put("name", ValidateUtil.isNotNull(seller.getName()) ? seller.getName() : StringPool.BLANK);

                        sellersJSON.add(sellerJSON);
                    }

                    infoJSON.put("sellers", sellersJSON);
                }
            }

            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally {
            out.close();
        }
    }

    /**
     * Payment projects
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void viewPaymentProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {

			// Declare logic
			ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));


			List<String> projectStatus = new ArrayList<String>();
			projectStatus.add(Constants.STATUS_PLANNING);
			projectStatus.add(Constants.STATUS_CONTROL);
			projectStatus.add(Constants.STATUS_CLOSED);
            projectStatus.add(Constants.STATUS_ARCHIVED);

			// Logic
			List<Project> projects = projectLogic.findByPOAndProjectStatus(getUser(req).getPerformingorg(), projectStatus);

            // Restriction for dates
            //
            SimpleDateFormat date = new SimpleDateFormat(Constants.DATE_PATTERN);

            Calendar calendar = DateUtil.getCalendar();

            calendar.add(Calendar.YEAR, -2);

			// Response
			//
			req.setAttribute("projects", projects);
            req.setAttribute("minDate", date.format(calendar.getTime()));

			forward("/index.jsp?nextForm=payment/payment", req, resp);
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}


}
