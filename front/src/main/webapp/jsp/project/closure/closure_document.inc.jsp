<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@ page import="es.sm2.openppm.core.common.Constants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<%--
  ~ Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program has been created in the hope that it will be useful.
  ~ It is distributed WITHOUT ANY WARRANTY of any Kind,
  ~ without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  ~ See the GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program. If not, see http://www.gnu.org/licenses/.
  ~
  ~ For more information, please contact SM2 Software & Services Management.
  ~ Mail: info@talaia-openppm.com
  ~ Web: http://www.talaia-openppm.com
  ~
  ~ Module: front
  ~ File: closure_document.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--

var docRepositoryTable;

var docRepository = {
	//Download document
	download: function(id) {
		
		var f = document.forms["frm_project"];
		
		f.action = "<%=ProjectServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectServlet.DOWNLOAD_DOC %>";
		f.idDocument.value = id;
		f.submit();
		return false;
	},
	// Create table
	createTable: function() {
		return $('#tb_doc_repository').dataTable({
			"oLanguage": datatable_language,		
			"bInfo": false,
			"bAutoWidth": false,
			"sPaginationType": "full_numbers",
			"aaSorting": [],
			"aoColumns": [
			              { "bVisible": false },
			              { "sClass": "left" },
			              { "sClass": "left"},
			              { "sClass": "left"},
                          { "sClass": "left"},
                          { "sClass": "center", sType: "date-euro"},
			              { "sClass": "center", "bSortable": false }
	     				],
	     	"bRetrieve": true
		});
	}
};

//When document is ready
readyMethods.add(function() {
	
	docRepositoryTable = docRepository.createTable();

	$('#tb_doc_repository tbody tr').live('click', function (event) {		
		docRepositoryTable .fnSetSelectable(this,'selected_internal');
	});	
});

//-->
</script>


<table id="tb_doc_repository" class="tabledata" cellspacing="1" style="width:100%">
	<thead>
		<tr>
			<th width="0%">&nbsp;</th>
			<th width="20%"><fmt:message key="document_type"/></th>						
			<th width="25%"><fmt:message key="document_name"/></th>
			<th width="35%"><fmt:message key="document_comment"/></th>
            <th width="10%"><fmt:message key="user"/></th>
            <th width="10%"><fmt:message key="date"/></th>
			<th width="3%">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="doc" items="${docsRepository}">
		
			<c:choose>
				<c:when test="${not empty doc.mime}">
					<c:set var="isFile">true</c:set>
				</c:when>
				<c:otherwise>
					<c:set var="isFile">false</c:set>
				</c:otherwise>
			</c:choose>
			
			<tr>
				<td>${doc.idDocumentProject}</td>
				<td><fmt:message key="documentation.${doc.type}"/></td>
				<c:choose>
					<c:when test="${isFile}">
						<td>${tl:escape(doc.name)} </td>
					</c:when>
					<c:otherwise>
						<td>
                            <a target="_blank" <c:if test="${doc.link ne doc.reducedLink}">class="btitle" title="${tl:escape(doc.link)}"</c:if> href="${tl:escape(doc.link)}">${tl:escape(doc.reducedLink)}</a>
						</td>
					</c:otherwise>
				</c:choose>
				<td>${tl:escape(doc.contentComment)}</td>
                <td>${tl:escape(doc.creationContact)}</td>
                <td><fmt:formatDate value="${doc.creationDate}" pattern="<%= Constants.TIME_PATTERN %>" /></td>
				<%-- is file to edit --%>
				<c:choose>
					<c:when test="${isFile}">
						<td>
							<img class="link" onclick="docRepository.download(${doc.idDocumentProject})" src="images/download.png" title="<fmt:message key="download"/>"/>
						</td>
					</c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	</tbody>
</table>
