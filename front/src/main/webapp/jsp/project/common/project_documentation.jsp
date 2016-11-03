<%@page import="es.sm2.openppm.utils.params.ParamUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.common.Settings.SettingType"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
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
  ~ File: project_documentation.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="msg.confirm_delete" var="msg_delete_document">
	<fmt:param><fmt:message key="document"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_delete_document">
	<fmt:param><fmt:message key="document"/></fmt:param>
</fmt:message>
<fmt:message key="maintenance.document.new_document" var="new_document_title" />
<fmt:message key="maintenance.document.edit_document" var="edit_document_title" />

<fmt:setLocale value="${locale}" scope="request"/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>
<% String type = (String)request.getParameter("documentationType"); %>

<script type="text/javascript">
<!--

var documentTable;

var documentProject = {
	
	// New document
	add: function() {

        $("#buttonDelete").hide();

        documentPopup.resetPopup();
        documentPopup.open('${documentStorage}', '<%= type %>');
    },
	// Update document
	edit : function(element) {

        $("#buttonDelete").hide();

        documentPopup.resetPopup();

		var doc;
		
		if (typeof element.parentNode === 'undefined') {

			doc = documentTable.fnGetData(element);

            //TODO force selected
            documentTable.fnSetSelectable(element,'selected_internal');
		}
		else {

			doc = documentTable.fnGetData(element.parentNode.parentNode.parentNode);

            //TODO force selected
            documentTable.fnSetSelectable(element.parentNode.parentNode.parentNode,'selected_internal');
		}
		
		// File
		if (doc[7] === '<%= DocumentType.FILE_SYSTEM.getName() %>') {
			
			var f = document.forms["frm_file_upload"];	
			
			f.filesystem_path.value 							= "";
			f.fileToUpload.value 								= "";
			f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 	= doc[0];
			f.<%= Documentproject.CONTENTCOMMENT %>.value 		= doc[3];
			f.<%= Documentproject.PROJECT %>.value 				= $("#<%=Documentproject.PROJECT%>_${param.documentationType}").val();
			$('#doc_name_row').show();
			$('#name_column').text(doc[1]);
			
			$("#filesystem_path").val(doc[2]);
			
			documentPopup.open('<%= DocumentType.FILE_SYSTEM.getName() %>', '<%= type %>');
		}
		// Link
		else {
			
			var f = document.forms["frm_link"];
			
			f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 	= doc[0];
			f.<%= Documentproject.CONTENTCOMMENT %>.value 		= doc[3];
			f.<%= Documentproject.LINK %>.value 				= doc[8];
			
			$("#linkHRef").attr("href", doc[2]);

			documentPopup.open('<%= DocumentType.LINK.getName() %>', '<%= type %>');
		}
	},
	// Delete document
	delete : function(element) {
		
		confirmUI(
				'${msg_title_delete_doc}','${msg_delete_document}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var doc;
                    var tr;

                    if (typeof element.parentNode === 'undefined') {
						doc = documentTable.fnGetData(element);
					}
					else {

                        tr = element.parentNode.parentNode.parentNode;

						doc = documentTable.fnGetData(element.parentNode.parentNode.parentNode);
					}
					
					var params = {
						accion : '<%=ProjectServlet.JX_DELETE_DOC %>',
						<%=Documentproject.IDDOCUMENTPROJECT%>: doc[0]
					};

					projectAjax.call(params,function(data) {

                        if (typeof tr === 'undefined') {
						    documentTable.fnDeleteSelected();
                        }
                        else {
                            documentTable.fnDeleteRow(tr); //selected does not work correctly
                        }

						if(<%=type.equals(Constants.DOCUMENT_CLOSURE)%>) {
							toTab(5);
				   		}
					});
			});
	},
	// Download document
	download: function(id) {
		
		var f = document.forms["frm_project"];
		
		f.action = "<%=ProjectServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectServlet.DOWNLOAD_DOC %>";
		f.idDocument.value = id;
		f.submit();
		return false;
	},
	// Set file
	setFile: function(dest, src) {
		$("#"+src).val($(dest).val());
	},
	// Create table
	createTable: function() {
		return $('#tb_document').dataTable({
			"oLanguage": datatable_language,
			"bFilter": false,
			"bInfo": false,
            "bDestroy": true,
			"iDisplayLength": 50,
			"sPaginationType": "full_numbers",
			"aaSorting": [[ 4, "desc" ], [ 1, "asc" ]],
			"aoColumns": [
			              { "bVisible": false },
			              { "sClass": "left" },
			              { "sClass": "left"},
			              { "sClass": "left"},
                          { "sClass": "left"},
                          { "sClass": "center", sType: "date-euro"},
			              { "sClass": "center", "bSortable": false },
			              { "bVisible": false },
			              { "bVisible": false }
			      		]
		});
	}
}

function chargeBT() {
    $('.btitle').bt({
        fill: '#F9FBFF',
        cssStyles: {color: '#343C4E', width: 'auto'},
        width: 250,
        padding: 10,
        cornerRadius: 5,
        spikeLength: 15,
        spikeGirth: 5,
        shadow: true,
        positions: 'top'
    });
}

//When document is ready
$(document).ready(function() {

    documentTable = documentProject.createTable();

	$('#tb_document tbody tr').bind('click', function (event) {
		documentTable.fnSetSelectable(this,'selected_internal');
	});

    chargeBT();
});

//-->
</script>
<form name="form_document_project_${param.documentationType }" id="form_document_project_${param.documentationType }" action="<%=ProjectServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="<%=ProjectServlet.JX_SAVE_DOCUMENT%>"/>
	<input type="hidden" name="<%=Documentproject.TYPE%>" value="${param.documentationType }"/>
	<input type="hidden" name="<%=Documentproject.IDDOCUMENTPROJECT%>" id="<%=Documentproject.IDDOCUMENTPROJECT%>" value=""/>
	<input type="hidden" name="<%=Documentproject.PROJECT%>" id="<%=Documentproject.PROJECT%>_${param.documentationType}" value="${project.idProject }"/>

	<table id="tb_document" class="tabledata" cellspacing="1" style="width:100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="20%"><fmt:message key="document_type"/></th>						
				<th width="45%"><fmt:message key="document_name"/></th>
				<th width="10%"><fmt:message key="document_comment"/></th>
                <th width="10%"><fmt:message key="user"/></th>
                <th width="10%"><fmt:message key="date"/></th>
				<th width="5%">
					<c:if test="${op:hasPermission(user,project.status,tab)}">
						<img onclick="documentProject.add()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
					</c:if>
				</th>
				<th width="0%">&nbsp;</th>
				<th width="0%">&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="doc" items="${docs}">
			
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
					<td>
						<nobr>
							<c:if test="${isFile}">
								<img class="link" onclick="documentProject.download(${doc.idDocumentProject})" src="images/download.png" title="<fmt:message key="download"/>"/>&nbsp;
							</c:if>
							<c:if test="${op:hasPermission(user,project.status,tab) && doc.type == param.documentationType}">
								<img class="link" onclick="documentProject.edit(this)" src="images/modify.png" title="<fmt:message key="edit"/>"/>&nbsp;
								<img class="link" onclick="documentProject.delete(this)" src="images/delete.jpg" title="<fmt:message key="delete"/>"/>
							</c:if>
						</nobr>
					</td>
					<%-- is file to edit --%>
					<c:choose>
						<c:when test="${isFile}">
							<td><%= DocumentType.FILE_SYSTEM.getName() %></td>
						</c:when>
						<c:otherwise>
							<td><%= DocumentType.LINK.getName() %></td>
						</c:otherwise>
					</c:choose>
					<%-- information raw --%>
					<c:choose>
						<c:when test="${isFile}">
							<td>${tl:escape(doc.name)}</td>
						</c:when>
						<c:otherwise>
							<td>${tl:escape(doc.link)}</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</form>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<jsp:include page="search_document_popup.jsp" flush="true" />
</c:if>