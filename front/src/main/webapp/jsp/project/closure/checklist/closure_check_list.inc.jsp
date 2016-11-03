<%@page import="es.sm2.openppm.core.model.impl.Closurecheckproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@ page import="es.sm2.openppm.core.common.GenericSetting" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page import="es.sm2.openppm.core.model.impl.Employee" %>
<%@ page import="es.sm2.openppm.front.utils.SecurityUtil" %>
<%@ page import="es.sm2.openppm.core.common.Constants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: closure_check_list.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<%-- Messages --%>
<fmt:message key="closure_check_list" var="titleClosureCheckList"/>
<fmt:message key="documentation.closure_check_list" var="msgDocumentType"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDeleteChecklist">
	<fmt:param><fmt:message key="closure_check_list"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDeleteChecklist">
	<fmt:param><fmt:message key="closure_check_list"/></fmt:param>
</fmt:message>


<fmt:setLocale value="${locale}" scope="request"/>

<%
    HashMap<String, String> settings = SettingUtil.getSettings(request);

    Employee user = (Employee)request.getSession().getAttribute("user");
%>

<script type="text/javascript">
<!--

var closureAjax = new AjaxCall('<%=ProjectClosureServlet.REFERENCE%>','<fmt:message key="error" />');
var checkListTable;

var checkList = {
		
		edit: function(element) {
			
			// Get data
			var row = checkListTable.fnGetData(element.parentNode.parentNode.parentNode);
			
			var f = document.frm_checklist_popup;
 			
			// Set data
			//
	 		f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value 	=  row[0];	
	 		f.<%= Closurecheckproject.CLOSURECHECK %>.value 			=  row[1];	
	 		$("#closureCheckName").html(unEscape(row[2]));
	 		$("#closureCheckDescription").attr("bt-xtitle", unEscape(row[3]));
 			f.<%= Closurecheckproject.COMMENTS %>.value 			 	= 	unEscape(row[4]);

            // Autocomplete date and manager by setting
            //
            if (<%= SettingUtil.getBoolean(settings, GeneralSetting.AUTOCOMPLETE_CHECKLIST_ITEM)%>) {

                f.<%= Closurecheckproject.DATEREALIZED %>.value 			= $("#actualDate").val();
                $("#dateRealized").attr("readonly","readonly");
                $("#dateRealized").addClass("disabled");
                $("#dateRealized").parent().find("img").remove();

                f.<%= Closurecheckproject.MANAGER %>.value 			        = "<%= user.getContact().getFullName() %>";
                $("#manager").attr("readonly","readonly");
                $("#manager").addClass("disabled");
            }
            else {

                f.<%= Closurecheckproject.DATEREALIZED %>.value 			= 	row[5];
                f.<%= Closurecheckproject.MANAGER %>.value 					= 	unEscape(row[8]);
            }

            f.<%= Closurecheckproject.REALIZED %>.checked 			 	= 	row[7] === 'true' ? true : false;
 			f.<%= Closurecheckproject.DEPARTAMENT %>.value 				= 	unEscape(row[9]);
 			
 			// Set document data
 			f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 			= row[11];
 			f.documentComments.value 									= unEscape(row[12]);
 			f.documentFile.value 										= unEscape(row[13]);
 			f.documentLink.value 										= unEscape(row[14]);
 			
 			// Info select
			$('#<%= Closurecheckproject.CLOSURECHECK %>').trigger('change');
 			
 			// Open pop up
 			$("#checklist-popup").dialog('open');
		},
		add: function() { 	// FUNCIONALIDAD SIN UTILIZAR
			
			// Reset form
			//
			var f = document.frm_checklist_popup;
			
			f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value 	= '';
			f.<%= Closurecheckproject.CLOSURECHECK %>.value 			= '';
			$("#closureCheckProjectComments").val("");
			$("#<%= Closurecheckproject.DATEREALIZED %>").val("");
			$("#<%= Closurecheckproject.REALIZED %>").attr("checked", false);
			f.<%= Closurecheckproject.MANAGER %>.value 					= '';
			f.<%= Closurecheckproject.DEPARTAMENT %>.value 				= '';
			
			// Reset document data
			f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 			= '';
 			f.documentComments.value 									= '';
 			f.documentFile.value 										= '';
 			f.documentLink.value 										= '';
			
			$('#<%= Closurecheckproject.CLOSURECHECK %>').trigger('change');
			
			$("#checklist-popup").dialog('open');
		},
		del: function(element) { // FUNCIONALIDAD SIN UTILIZAR
			
			confirmUI(
				'${msgTitleConfirmDeleteChecklist}','${msgConfirmDeleteChecklist}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var row = checkListTable.fnGetData(element.parentNode.parentNode.parentNode);
					
					var params={
						accion: 											"<%=ProjectClosureServlet.JX_DELETE_CLOSURE_CHECK_PROJECT %>",
						<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>: 	row[0],
						<%= Closurecheckproject.CLOSURECHECK %>: 			row[1],
						<%= Project.IDPROJECT %>: 							$("#id").val(),
						<%= Documentproject.IDDOCUMENTPROJECT %>: 			row[11]
					};
					
					closureAjax.call(params, function(data){
						checkListTable.fnDeleteSelected();
					});	
			});
		},
		// Update document
		editDocument : function(doc) {

            // Delete functionality show
            $("#buttonDelete").show();

			// File
			if (doc[2] !== "") {
				
				var f = document.forms["frm_file_upload"];	
				
				f.filesystem_path.value 							= "";
				f.fileToUpload.value 								= "";
				f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 	= doc[0];
				f.<%= Documentproject.CONTENTCOMMENT %>.value 		= doc[1];
				f.<%= Documentproject.PROJECT %>.value 				= $("#id").val();
				$('#doc_name_row').show();
				$('#name_column').text("${msgDocumentType}");
				
				$("#filesystem_path").val(doc[2]);
				
				documentPopup.open('<%= DocumentType.FILE_SYSTEM.getName() %>', '<%= Constants.DOCUMENT_CLOSURE_CHECK_LIST %>');
			}
			// Link
			else {
				
				var f = document.forms["frm_link"];
				
				f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 	= doc[0];
				f.<%= Documentproject.CONTENTCOMMENT %>.value 		= doc[1];
				f.<%= Documentproject.LINK %>.value 				= doc[3];
				
				$("#linkHRef").attr("href", doc[3]);
				$("#linkHRef").html(doc[3]);
				
				documentPopup.open('<%= DocumentType.LINK.getName() %>', '<%= Constants.DOCUMENT_CLOSURE_CHECK_LIST %>');
			}
		},
		// New document
		addDocument: function(){
            documentPopup.open(undefined, '<%= Constants.DOCUMENT_CLOSURE_CHECK_LIST %>');
		},
		addData: function(data) {

			var row = checkListTable.fnFindData(data.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>);
			
			if (row != null) {
				
				var f;
				
				// File
				if (data.documentType == '<%= DocumentType.FILE_SYSTEM.getName() %>') {
					
					f = document.forms["frm_file_upload"];	
					
					row[13] = $("#filesystem_path").val();
					
					// Reset icons
					row[10] = 	'<nobr>'+
									'<img title="<fmt:message key="download"/>: '+ row[13] + '" class="link download" src="images/download.png">'+
									'&nbsp;&nbsp;&nbsp;'+
									'<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
									'&nbsp;&nbsp;&nbsp;'+
									'<img title="<fmt:message key="view"/>" class="link view" src="images/view.png">'+
								'</nobr>';
				}
				// Link
				else { 
					
					f = document.forms["frm_link"];
					
					row[14] = f.<%= Documentproject.LINK %>.value;
					
					// Reset icons
					row[10] = 	'<nobr>'+
									'<img title="<fmt:message key="link"/>: '+ row[14] + '" class="link hyperlink" src="images/link.png"/>'+
									'&nbsp;&nbsp;&nbsp;'+
									'<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
									'&nbsp;&nbsp;&nbsp;'+
									'<img title="<fmt:message key="view"/>" class="link view" src="images/view.png">'+
								'</nobr>';
								
				}
				
				// Update document
				row[11] = data.<%= Documentproject.IDDOCUMENTPROJECT %>;
				row[12] = f.<%= Documentproject.CONTENTCOMMENT %>.value;
				
				checkListTable.fnUpdateAndSelect(row); 
				
				// Reset form
				f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value = "";
			}
		},
        delDocument: function() {

            var idClosureCheckProject = documentPopup.idRow;

            if (idClosureCheckProject !== undefined && idClosureCheckProject !== "") {

                var row = checkListTable.fnFindData(idClosureCheckProject);

                if (row !== undefined) {

                    // Set document information into new array
                    for (var i = 11; i < row.length; i++) {
                        row[i] = "";
                    }

                    // Reset icons
                    row[10] = 	'<nobr>'+
                    '<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
                    '&nbsp;&nbsp;&nbsp;'+
                    '<img title="<fmt:message key="view"/>" class="link view" src="images/view.png">'+
                    '</nobr>';

                    checkListTable.fnUpdateAndSelect(row);
                }
            }
        }
};

readyMethods.add(function() {
	
	checkListTable = $('#tb_checklist').dataTable({
		"oLanguage": datatable_language,		
		"bInfo": false,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aoColumns": [ 
  				{ "bVisible": false }, <%-- Id closure check project --%>
  				{ "bVisible": false }, <%-- Id closure check --%>
  				{ "sClass": "left"}, <%-- name closure check --%>
  				{ "sClass": "left"}, <%-- description closure check --%>
            	{ "sClass": "left"}, <%-- comments --%>
            	{ "sClass": "center", "sType": "es_date"}, <%-- date realized--%>
              	{ "sClass": "center"}, <%-- checkbox --%>
              	{ "bVisible": false }, <%-- checkbox value --%>
              	{ "sClass": "left"}, <%-- manager --%>
              	{ "bVisible": false}, <%-- departament --%>
              	{ "sClass": "right", "bSortable": false, "bVisible": ${op:hasPermission(user,project.status,tab)} },
              	<%-- Document information --%>
              	{ "bVisible": false }, <%-- idDocumentProject --%>
              	{ "bVisible": false }, <%-- contentComment --%>
              	{ "bVisible": false }, <%-- name --%>
              	{ "bVisible": false } <%-- link --%>
		]
	});
	$('#tb_checklist tbody tr').live('click', function (event) {
		checkListTable.fnSetSelectable(this,'selected_internal');
	});	
	
	$('#tb_checklist .view').live('click', function (event) {		
		checkList.edit(this);
	});	
	
	$('#tb_checklist .download').live('click', function (event) {		
		
		var row = checkListTable.fnGetData(this.parentNode.parentNode.parentNode);
		
		documentProject.download(row[11]);
	});	
	
	$('#tb_checklist .hyperlink').live('click', function (event) {		
		
		var row = checkListTable.fnGetData(this.parentNode.parentNode.parentNode);
		
		if (row[14] != "") {
			window.open(row[14],'_blank');
		}
	});
	
	$('#tb_checklist .append').live('click', function (event) {
		
		// Get data
		var row = checkListTable.fnGetData(this.parentNode.parentNode.parentNode);

        if (row !== undefined) {

            // Rewrite functions
            documentPopup.saveCallback      = checkList.addData;
            documentPopup.removeCallback    = checkList.delDocument;
            documentPopup.idRow             = row[0];

            // Reset popup
            documentPopup.resetPopup();

            // Set document information into new array
            var doc = [];
            for (var i = 11; i < row.length; i++) {
                doc[doc.length] = row[i];
            }

            // Update document
            if (row[11] != "") {

                $("#buttonDelete").show();

                checkList.editDocument(doc);
            }
            // New document
            else {

                $("#buttonDelete").hide();

                checkList.addDocument();
            }

            $(".<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>").val(row[0]);
        }
	});
});
//-->
</script>

<visual:panel id="fieldClousureCheckList" title="${titleClosureCheckList }">
	
	<table id="tb_checklist" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%"></th> <%-- Id closure check project --%>
				<th width="0%"></th> <%-- Id closure check --%>
				<th width="20%"><fmt:message key="name" /></th>
				<th width="20%"><fmt:message key="description" /></th>
				<th width="23%"><fmt:message key="comments" /></th>
				<th width="10%"><fmt:message key="date" /></th>
				<th width="5%"><fmt:message key="realized" /></th>
				<th width="0%"></th> <%-- realized value --%>
				<th width="17%"><fmt:message key="owner" /></th>
				<th width="0%"><fmt:message key="workingcosts.department" /></th>
				<th width="2%"></th>
				
				<%-- Document information --%>
				<th width="0%"></th> <%-- idDocumentProject --%>
				<th width="0%"></th> <%-- contentComment --%>
				<th width="0%"></th> <%-- name --%>
				<th width="0%"></th> <%-- link --%>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="check" items="${project.closurecheckprojects}">
				<tr>
					<td>${check.idClosureCheckProject }</td>
					<td>${check.closurecheck.idClosureCheck }</td>
					<td>${tl:escape(check.closurecheck.name)}</td>
					<td>${tl:escape(check.closurecheck.description)}</td>
					<td>${tl:escape(check.comments)}</td>
					<td><fmt:formatDate value="${check.dateRealized}" pattern="${datePattern}" /></td>
					<td>
						<input type="checkbox" disabled="disabled" name="closureCheckRealized" class="closureCheckRealized" style="width: 16px;"
							<c:if test="${check.realized eq true}"> checked</c:if>
						/>
					</td>
					<td>${check.realized}</td>
					<td>${tl:escape(check.manager)}</td>
					<td>${tl:escape(check.departament)}</td>
					<td>
						<nobr>
                            <%-- Documents images --%>
							<c:choose>
								<c:when test="${not empty check.documentproject.name}">
									<img title="<fmt:message key="download"/>: ${check.documentproject.name}" class="link download" src="images/download.png"/>
									&nbsp;
								</c:when>
								<c:when test="${not empty check.documentproject.link}">
									<img title="<fmt:message key="link"/>: ${check.documentproject.link}" class="link hyperlink" src="images/link.png"/>
									&nbsp;
								</c:when>
							</c:choose>
							<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">
							&nbsp;
							<img title="<fmt:message key="view"/>" class="link view" src="images/view.png">
						</nobr>
					</td>
					
					<%-- Document information --%>
					<td>${check.documentproject.idDocumentProject}</td>
					<td>${check.documentproject.contentComment}</td>
					<td>${check.documentproject.name}</td>
					<td>${check.documentproject.link}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</visual:panel>