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
 * File: DocumentUtils.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import es.sm2.openppm.core.exceptions.UserSendingException;
import es.sm2.openppm.core.logic.impl.DocumentationLogic;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;

public final class DocumentUtils extends es.sm2.openppm.utils.DocumentUtils {

	public static final  Logger LOGGER = Logger.getLogger(DocumentUtils.class);
	
	private DocumentUtils() {
		super();
	}
	
	/**
	 * Create PDF for Control Change
	 * @param idioma
	 * @param project
	 * @param change
	 * @param preparedBy
	 * @return
	 * @throws DocumentException
	 * @throws LogicException
	 */
	public static byte[] toPdf(ResourceBundle idioma, Project project, Changecontrol change, 
			Employee preparedBy, final Image headerImg, final Image footerImg) throws DocumentException, LogicException {
		
		if (change == null) {
			throw new DocumentException("No change control found.");
		}
		
		if (preparedBy == null || preparedBy.getContact() == null) {
			throw new UserSendingException();
		}
		
		Document document = new Document(PageSize.A4);
		document.setMargins(70F, 70F, 38F, 38F); // Total Height: 842pt, Total Width: 595pt
		byte[] file = null;
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		@SuppressWarnings("unused")
		PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
		
		document.open();
		
		Font fontHeader = new Font(FontFamily.TIMES_ROMAN, 9, Font.BOLD);
		Font fontCell = new Font(FontFamily.TIMES_ROMAN, 9);
		Font tituloFont = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD);
		
		document.add(new Paragraph(" ", tituloFont));
		document.add(new Paragraph(" ", tituloFont));
		document.add(new Paragraph(" ", fontHeader));
		Paragraph title = new Paragraph(idioma.getString("change_request").toUpperCase(), tituloFont);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		
		// Header Table
		// Project info
		PdfPTable tableHeader = new PdfPTable(3);
		tableHeader.setWidthPercentage(100);
		tableHeader.setSpacingBefore(10);
		tableHeader.setSpacingAfter(15);
		
		int[] colWidth = new int[3];
		colWidth[0] = 40;
		colWidth[1] = 30;
		colWidth[2] = 30;
		tableHeader.setWidths(colWidth);
		
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.project_name"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.prepared_by"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.date"), fontHeader, 1F, 1F, 0F, 1F));
		
		tableHeader.addCell(prepareCell(project.getProjectName() +" / "+ project.getAccountingCode(), fontCell, 0F, 0F, 0F, 1F));
		tableHeader.addCell(prepareCell(preparedBy.getContact().getFullName(), fontCell, 0F, 0F, 0F, 1F));
		tableHeader.addCell(prepareCell(DateUtil.format(idioma, new Date()), fontCell, 0F, 1F, 0F, 1F));
		
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.customer"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.contact_name"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.customer_type"), fontHeader, 1F, 1F, 0F, 1F));
		
		tableHeader.addCell(prepareCell(project.getCustomer() != null?project.getCustomer().getName():"", fontCell, 0F, 0F, 0F, 1F));
		tableHeader.addCell(prepareCell(
				(project.getCustomer() != null ? project.getCustomer().getName() : "-")
				, fontCell, 0F, 0F, 0F, 1F));
		
		Customertype cusType = (project.getCustomer()!= null?project.getCustomer().getCustomertype():null);
		tableHeader.addCell(prepareCell(cusType == null?"":cusType.getName(), fontCell, 0F, 1F, 0F, 1F));
		
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.business_manager"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.project_manager"), fontHeader, 1F, 0F, 0F, 1F));
		tableHeader.addCell(prepareHeaderCell(idioma.getString("change_request.originator"), fontHeader, 1F, 1F, 0F, 1F));
		
		Employee bm = project.getEmployeeByFunctionalManager();
		Employee pm = project.getEmployeeByProjectManager();
		
		tableHeader.addCell(prepareCell(bm == null?"":bm.getContact().getFullName(), fontCell, 0F, 0F, 1F, 1F));
		tableHeader.addCell(prepareCell(pm == null?"":pm.getContact().getFullName(), fontCell, 0F, 0F, 1F, 1F));
		tableHeader.addCell(prepareCell(change.getOriginator(), fontCell, 0F, 1F, 1F, 1F));
		
		document.add(tableHeader);
		
		// Change Information
		document.add(new Paragraph(idioma.getString("change_information")));
		
		PdfPTable tableInfo = new PdfPTable(1);
		tableInfo.setWidthPercentage(100);
		tableInfo.setSpacingBefore(10);
		tableInfo.setSpacingAfter(15);
		
		tableInfo.addCell(prepareHeaderCell(idioma.getString("change.change_type"), fontHeader, 1F, 1F, 0F, 1F));
		tableInfo.addCell(prepareCell(change.getChangetype().getDescription(), fontCell, 0F, 1F, 0F, 1F));
		
		String priorityDesc = "";
		if (change.getPriority().equals('H')) priorityDesc = idioma.getString("change.priority.high");
		if (change.getPriority().equals('N')) priorityDesc = idioma.getString("change.priority.normal");
		if (change.getPriority().equals('L')) priorityDesc = idioma.getString("change.priority.low");

		tableInfo.addCell(prepareHeaderCell(idioma.getString("change.priority"), fontHeader, 1F, 1F, 0F, 1F));
		tableInfo.addCell(prepareCell(priorityDesc, fontCell, 0F, 1F, 0F, 1F));
		
		tableInfo.addCell(prepareHeaderCell(idioma.getString("change.desc"), fontHeader, 1F, 1F, 0F, 1F));
		tableInfo.addCell(prepareCell(change.getDescription(), fontCell, 0F, 1F, 0F, 1F));
		
		tableInfo.addCell(prepareHeaderCell(idioma.getString("change.recommended_solution"), fontHeader, 1F, 1F, 0F, 1F));
		tableInfo.addCell(prepareCell(change.getRecommendedSolution(), fontCell, 0F, 1F, 1F, 1F));
		
		PdfPTable tableSubInfo = new PdfPTable(3);
		tableSubInfo.setWidthPercentage(100);
		
		//TODO MIGRACION
		tableSubInfo.addCell(prepareSubCell(idioma.getString("change.wbs_node"), fontHeader));
		tableSubInfo.addCell(prepareSubCell(idioma.getString("change.estimated_effort"), fontHeader));
		tableSubInfo.addCell(prepareSubCell(idioma.getString("change.estimated_cost"), fontHeader));
		
		tableSubInfo.addCell(prepareSubCell((change.getWbsnode()!=null ? change.getWbsnode().getName() : ""), fontCell));
		tableSubInfo.addCell(prepareSubCell((change.getEstimatedEffort()!=null ? String.valueOf(change.getEstimatedEffort()) : ""), fontCell));
		tableSubInfo.addCell(prepareSubCell((change.getEstimatedCost()!=null ? ValidateUtil.toCurrency(change.getEstimatedCost()) : ""), fontCell));
		
		PdfPCell subTable = new PdfPCell(tableSubInfo);
		subTable.setBorderWidth(1F);
		
		tableInfo.addCell(subTable);
		
		tableInfo.addCell(prepareHeaderCell(idioma.getString("change.impact_desc"), fontHeader, 1F, 1F, 0F, 1F));
		tableInfo.addCell(prepareCell(change.getImpactDescription(), fontCell, 0F, 1F, 1F, 1F));
		
		document.add(tableInfo);
		
		document.add(new Paragraph(idioma.getString("change.resolution")));
		
		PdfPTable tableResolution = new PdfPTable(1);
		tableResolution.setWidthPercentage(100);
		tableResolution.setSpacingBefore(10);
		tableResolution.setSpacingAfter(15);
		
		tableResolution.addCell(prepareHeaderCell(idioma.getString("change.resolution"), fontHeader, 1F, 1F, 0F, 1F));
		tableResolution.addCell(prepareCell(
				(change.getResolution()!=null && change.getResolution()? idioma.getString("yes") : idioma.getString("no"))
				, fontCell, 0F, 1F, 0F, 1F));
		
		tableResolution.addCell(prepareHeaderCell(idioma.getString("change.resolution_date"), fontHeader, 1F, 1F, 0F, 1F));
		tableResolution.addCell(prepareCell(DateUtil.format(idioma, change.getResolutionDate()), fontCell, 0F, 1F, 0F, 1F));
		
		tableResolution.addCell(prepareHeaderCell(idioma.getString("change.resolution_reason"), fontHeader, 1F, 1F, 0F, 1F));
		tableResolution.addCell(prepareCell(change.getResolutionReason(), fontCell, 0F, 1F, 1F, 1F));
		
		document.add(tableResolution);
		
		document.close();
		
		try {
        	
            PdfReader reader = new PdfReader(outputStream.toByteArray());
			PdfStamper stamper = new PdfStamper(reader, outputStream);
				
			int numPag = reader.getNumberOfPages();
			
			for(int i=1; i<= reader.getNumberOfPages(); i++){
				setHeaderFooter(i, numPag, headerImg, footerImg, reader, stamper, idioma);				
			}
				
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		file = outputStream.toByteArray();
		
		return file;
	}
	
	/**
	 * 
	 * @param content
	 * @param font
	 * @param borderTop
	 * @param borderRight
	 * @param borderBottom
	 * @param borderLeft
	 * @return
	 */
	private static PdfPCell prepareHeaderCell(String content, Font font, 
			Float borderTop, Float borderRight, Float borderBottom, Float borderLeft) {
		PdfPCell cell = new PdfPCell(new Paragraph(content, font));
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthBottom(borderBottom);
		cell.setBorderWidthLeft(borderLeft);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(5F);
		
		return cell;
	}
	
	/**
	 * 
	 * @param content
	 * @param font
	 * @param borderTop
	 * @param borderRight
	 * @param borderBottom
	 * @param borderLeft
	 * @return
	 */
	private static PdfPCell prepareCell(String content, Font font, 
			Float borderTop, Float borderRight, Float borderBottom, Float borderLeft) {
		PdfPCell cell = new PdfPCell(new Paragraph(content, font));
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthBottom(borderBottom);
		cell.setBorderWidthLeft(borderLeft);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(3F);
		cell.setPaddingLeft(5F);
		
		return cell;
	}
	
	/**
	 * 
	 * @param content
	 * @param font
	 * @return
	 */
	private static PdfPCell prepareSubCell(String content, Font font) {
		PdfPCell cell = new PdfPCell(new Paragraph(content, font));
		cell.setBorderWidth(0F);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		return cell;
	}
	
	/**
	 * 
	 * @param pagActual
	 * @param pagTotal
	 * @param headerImg
	 * @param footerImg
	 * @param reader
	 * @param stamper
	 * @param idioma
	 * @throws DocumentException
	 * @throws IOException
	 */
	private static void setHeaderFooter (int pagActual, int pagTotal, Image headerImg, Image footerImg,
			PdfReader reader, PdfStamper stamper, ResourceBundle idioma) throws DocumentException, IOException {
		
		PdfContentByte content = stamper.getUnderContent(pagActual);	
		content.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 8);
		headerImg.setAbsolutePosition(74f, 745f);
	    content.addImage(headerImg);
	    footerImg.setAbsolutePosition(75f, 20f);
	    content.addImage(footerImg);
		content.beginText();
		content.showTextAligned(Element.ALIGN_BOTTOM, "_____________________________________________________________________________________________________", 75f, 55f, 0f);
		content.showTextAligned(Element.ALIGN_BOTTOM, "©2010 OPEN PPM - Projet Portfolio Management. (Open PPM)", 75f, 45f, 0f);
		content.showTextAligned(Element.ALIGN_BOTTOM, "www.sourceforce.net/openppm", 75f, 35f, 0f);				
	    content.showTextAligned(Element.ALIGN_BOTTOM, new ParamResourceBundle("pdf.pagination", pagActual, pagTotal).toString(idioma), 475f, 45f, 0f);			    
		content.endText();
		content.stroke();
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static List<Documentation> getDocumentationList(HttpServletRequest req) {
		
		List<Documentation> docs = null;
		
		try {
			DocumentationLogic docLogic = new DocumentationLogic();
			
			Employee loguedUser = SecurityUtil.consUser(req);
			
			if (loguedUser != null) {
				docs = docLogic.findByRelation(Documentation.COMPANY, loguedUser.getContact().getCompany());
			}
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, req); }
		
		return docs;
	}
}