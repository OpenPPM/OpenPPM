<%@page import="es.sm2.openppm.core.model.impl.Closurecheckproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page import="es.sm2.openppm.core.model.impl.Employee" %>
<%@ page import="es.sm2.openppm.utils.DateUtil" %>
<%@ page import="es.sm2.openppm.core.model.impl.Timeline" %>

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
  ~ File: search_document_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="upload" var="upload"/>
<fmt:message key="cancel" var="cancel"/>

<fmt:message key="update" var="update"/>
<fmt:message key="close" var="close"/>

<%-- Message for validations --%>
<fmt:message key="msg.error.no_selected_file" var="error_no_selected_file" />
<fmt:message key="msg.error.no_selected_file_info" var="error_no_selected_file_msg" />
<fmt:message key="maintenance.error_msg_a" var="fmtLinkRequired">
	<fmt:param><b><fmt:message key="documentation.link"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFileRequired">
	<fmt:param><b><fmt:message key="documentation.file_system"/></b></fmt:param>
</fmt:message>

<% String type = (String)request.getParameter("documentationType"); %>

<script type="text/javascript">
<!--
var projectAjax = new AjaxCall('<%=ProjectServlet.REFERENCE%>','<fmt:message key="error" />');

var documentPopup = {
		
	formLink: document.forms["frm_link"],
	formFile: document.forms["frm_file_upload"],
	
	// Show file form	
	showFile: function() {
		
		$("#linkOption").hide();
		$("#buttonLink").hide();
		
		if (documentPopup.formFile.idDocumentProject.value != -1) {
			
			$("#buttonFile").val('${update}');
			$("#buttonCancel").val('${close}');
		}
		else {
			$("#buttonFile").val('${upload}');
			$("#buttonCancel").val('${cancel}');
		}

		$("#buttonFile").show();
		$("#radioFile").attr('checked', 'checked');
		$("#fileOption").show('blind');	
	},
	// Show link form	
	showLink : function() {
		
		$("#document-popup").css("min-height","0px");
		
		$("#fileOption").hide();
		$("#buttonFile").hide();

		if (documentPopup.formLink.idDocumentProject.value != -1) {
			
			$("#buttonLink").val('${update}');
			$("#buttonCancel").val('${close}');
		}
		else {
			$("#buttonLink").val('${upload}');
			$("#buttonCancel").val('${cancel}');
		}
		
		$("#buttonLink").show();
		$("#radioLink").attr('checked', 'checked');
		$("#linkOption").show('blind');
	},
	// Initialize
	initialize: function() {

		$("#document-popup").dialog({
			autoOpen: false,
			width: 500,
			resizable: false,
			modal: true,
			open: function(event, ui) {
				documentPopup.formLinkValidator().resetForm();
				documentPopup.formFileValidator().resetForm(); 
			}
		});
		
		// Initialize callback
		documentPopup.saveCallback = documentPopup.addData;
	},
	// Open
	open : function (documentType, type) {

        // Type assign
        $(".documentType").val(type);

		documentPopup.formLinkValidator().resetForm();
		documentPopup.formFileValidator().resetForm();

        try {
            $('#document-popup').dialog('open');
        }
        catch (e) {
            $('#document-popup').dialog('open');
        }

		if (documentType == '<%= DocumentType.FILE_SYSTEM.getName() %>') {

			$("#document-popup .selection").hide();
			$('input[name=optionsDoc]').attr("disabled", true);
			documentPopup.showFile();
		}
		else if (documentType == '<%= DocumentType.LINK.getName() %>') {

			$("#document-popup .selection").hide();
			$('input[name=optionsDoc]').attr("disabled", true);
			documentPopup.showLink();
		}
		else {
			$('input[name=optionsDoc]').attr("disabled", false);
			$("#document-popup").css("min-height","0px");
			$("#document-popup .selection").show();
			$("#document-popup .option").hide();

			$("#buttonLink").hide();
			$("#buttonFile").hide();
			$("#buttonCancel").val('${cancel}');
		}
	},
	// Button close
	close : function () {
		$('#document-popup').dialog('close');
	},
	// File upload
	fileUpload : function () {
		
		if (documentPopup.formFileValidator().form()) {
			
			var f = documentPopup.formFile;
			
			    $.ajaxFileUpload ({
				url: '<%=ProjectServlet.REFERENCE%>',
				secureuri: false,
				fileElementId: 'fileToUpload',
				dataType: 'json',			
				data: [['accion', "<%=ProjectServlet.JX_UPLOAD_FILESYSTEM%>"], 
				       ['<%= Documentproject.PROJECT%>', f.<%= Documentproject.PROJECT%>.value],
				       ['<%= Documentproject.IDDOCUMENTPROJECT%>', f.<%= Documentproject.IDDOCUMENTPROJECT%>.value], 
				       ['<%= Documentproject.TYPE%>', f.<%= Documentproject.TYPE%>.value],
				       ['<%= Documentproject.CONTENTCOMMENT%>', f.<%= Documentproject.CONTENTCOMMENT%>.value],
				       ['<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>', f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value], //pendiente input generico
                       ['<%= Timeline.IDTIMELINE %>', f.<%= Timeline.IDTIMELINE %>.value]
				      ], 
				beforeSend: function () {$("#loading").show('fast');},
				success: function (data) {
					
					if (data.error) {
						alertUI("${fmt_error}",data.error);
					}
					else {
						
						// Execute callback function
						documentPopup.saveCallback(data);
						
						// Reset callback function
						documentPopup.saveCallback = documentPopup.addData;
				   		
				   		documentPopup.close();
					}			
				},
				error: function (data, status, e) {
					alertUI(e);
				}
			});	
		}
	},
	addData: function(data) {

		if ($("#linkOption").css("display") == "block") {
			documentPopup.addDataLink(data);
		}
		else {
			documentPopup.addDataFile(data);
		}
	},
	// Update document table
	addDataLink: function (data) {
		
		var f = documentPopup.formLink;

        var urlLink = '<a target="_blank" href="'+ escape(data.<%=Documentproject.LINK%>) +'">'+ escape(data.<%=Documentproject.LINK%>) +'</a>';

        var reducedLink = escape(data.<%=Documentproject.LINK%>);

        // Hide large url
        //
        if (reducedLink.length > 80) {

            reducedLink = reducedLink.substring(0,77) + "...";

            urlLink = '<a target="_blank" title="'+ escape(data.<%=Documentproject.LINK%>) +'" class="btitle" href="'+ escape(data.<%=Documentproject.LINK%>) +'">'+ reducedLink +'</a>';
        }

		var dataRow = [
   			data.<%=Documentproject.IDDOCUMENTPROJECT%>,
   			escape(data.<%=Documentproject.TYPE%>),
            urlLink,
   			escape(data.<%=Documentproject.CONTENTCOMMENT%>),
            escape(data.<%= Documentproject.CREATIONCONTACT %>),
            data.<%= Documentproject.CREATIONDATE %>,
   			'<nobr>'+
   			'<img class="link" onclick="documentProject.edit(this)" src="images/modify.png" title="<fmt:message key="edit"/>"/>&nbsp;&nbsp;' +
   			'<img class="link" onclick="documentProject.delete(this)" src="images/delete.jpg" title="<fmt:message key="delete"/>"/>'+
   			'</nobr>',
   			'<%= DocumentType.LINK.getName() %>',
   			escape(data.<%=Documentproject.LINK%>)
   		];
		
		if (f.<%=Documentproject.IDDOCUMENTPROJECT%>.value == data.<%=Documentproject.IDDOCUMENTPROJECT%>) {
   			documentTable.fnUpdateAndSelect(dataRow);
   		}
   		else {
   			documentTable.fnAddDataAndSelect(dataRow);
   		}

        chargeBT();

		// Submit if closure tab
   		if(<%= Constants.DOCUMENT_CLOSURE.equals(type) %>) {
   			toTab(5);
   		}
	},
	// Update document table
	addDataFile: function (data) {
		
		var f = documentPopup.formFile;
		
		// Update document table
		var dataRow = [
   			data.<%=Documentproject.IDDOCUMENTPROJECT%>,
   			escape(data.<%=Documentproject.TYPE%>),
   			escape(data.<%=Documentproject.NAME%>),
   			escape(data.<%=Documentproject.CONTENTCOMMENT%>),
            escape(data.<%= Documentproject.CREATIONCONTACT %>),
            data.<%= Documentproject.CREATIONDATE %>,
   			'<nobr>'+
   			'<img class="link" onclick="documentProject.download(' + data.<%=Documentproject.IDDOCUMENTPROJECT%> + ')" src="images/download.png" title="<fmt:message key="download"/>"/>&nbsp;&nbsp;' +
   			'<img class="link" onclick="documentProject.edit(this)" src="images/modify.png" title="<fmt:message key="edit"/>"/>&nbsp;&nbsp;' +
   			'<img class="link" onclick="documentProject.delete(this)" src="images/delete.jpg" title="<fmt:message key="delete"/>"/>'+
   			'</nobr>',
   			'<%= DocumentType.FILE_SYSTEM.getName() %>',
   			escape(data.<%=Documentproject.NAME%>)
   		];
   		
   		if(f.<%=Documentproject.IDDOCUMENTPROJECT%>.value == data.<%=Documentproject.IDDOCUMENTPROJECT%>) {
   			documentTable.fnUpdateAndSelect(dataRow);
   		}
   		else {
   			documentTable.fnAddDataAndSelect(dataRow);
   		}	   
   		
   		// Submit if closure tab
   		if(<%= Constants.DOCUMENT_CLOSURE.equals(type) %>) {
   			toTab(5);
   		}
	},
	// Save callback
	saveCallback: null,
	// Save link
	saveLink : function () {
		
		if (documentPopup.formLinkValidator().form()) {
			
			var f = documentPopup.formLink;

			var params = {
				accion: 											"<%= ProjectServlet.JX_SAVE_DOCUMENT %>",
				<%= Documentproject.PROJECT%>: 						f.<%= Documentproject.PROJECT%>.value,
				<%= Documentproject.IDDOCUMENTPROJECT%>: 			f.<%= Documentproject.IDDOCUMENTPROJECT%>.value,
				<%= Documentproject.TYPE%>: 						f.<%= Documentproject.TYPE%>.value,
				<%= Documentproject.LINK%>: 						f.<%= Documentproject.LINK%>.value,
				<%= Documentproject.CONTENTCOMMENT %>:				f.<%= Documentproject.CONTENTCOMMENT%>.value,
				<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>: 	f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value,
                <%= Timeline.IDTIMELINE %>: 	                    f.<%= Timeline.IDTIMELINE %>.value
			};
			
			projectAjax.call(params, function(data) {
				
				// Execute callback function
				documentPopup.saveCallback(data);
				
				// Reset callback function
				documentPopup.saveCallback = documentPopup.addData;
				
		   		documentPopup.close();
			});
		}
	},
	// Reset forms
	resetPopup: function() {

		$('input[name=optionsDoc]').attr('checked',false);
        documentPopup.resetFile();
        documentPopup.resetLink();
	},
	// Reset file form 
	resetFile: function() {

		var f = documentPopup.formFile;
		
		f.filesystem_path.value = "";
		f.fileToUpload.value = "";
		f.<%= Documentproject.CONTENTCOMMENT%>.value = "";
		f.<%= Documentproject.IDDOCUMENTPROJECT%>.value = "-1";	
		f.<%= Documentproject.PROJECT%>.value = $("#<%=Documentproject.PROJECT%>_${param.documentationType}").val();
		$('#doc_name_row').hide();

        // Reset ids
        f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value = "";
        f.<%= Timeline.IDTIMELINE %>.value = "";
	},
	// Reset link form
	resetLink: function() {

		var f = documentPopup.formLink;
		
		f.<%= Documentproject.IDDOCUMENTPROJECT%>.value = "-1";	
		f.<%= Documentproject.PROJECT%>.value = $("#<%=Documentproject.PROJECT%>_${param.documentationType}").val();
		f.<%= Documentproject.LINK %>.value = "";
		f.<%= Documentproject.CONTENTCOMMENT%>.value = "";
		$("#linkHRef").attr("href", "");
        $("#linkHRef").html("");

        // Reset ids
        f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value = "";
        f.<%= Timeline.IDTIMELINE %>.value = "";
	},
	// Form link validator
	formLinkValidator: function() {
		
		return  $("#frm_link").validate({
			errorContainer: 'div#document-errors',
			errorLabelContainer: 'div#document-errors ol',
			wrapper: 'li',
			showErrors: function(errorMap, errorList) {
				$('span#document-numerrors').html(this.numberOfInvalids());
				this.defaultShowErrors();
			},
			rules: {
				<%= Documentproject.LINK %>: {required: true, maxlength: 3000 }
			},
			messages: {
				<%= Documentproject.LINK %>: { required: '${fmtLinkRequired}' }
			}
		});
	},
	// Form file validator
	formFileValidator: function() {
		
		return  $("#frm_file_upload").validate({
			errorContainer: 'div#document-errors',
			errorLabelContainer: 'div#document-errors ol',
			wrapper: 'li',
			showErrors: function(errorMap, errorList) {
				$('span#document-numerrors').html(this.numberOfInvalids());
				this.defaultShowErrors();
			},
			rules: {
				filesystem_path: "required"
			},
			messages: {
				filesystem_path: { required: '${fmtFileRequired}' }
			}
		});
	},
    // Callback function after delete document
    removeCallback: null,
    // id row table to delete icons
    idRow: null,
    remove: function() {

        var f;

        if ($("#linkOption").is(":visible")) {
            f = documentPopup.formLink;
        }
        else if ($("#fileOption").is(":visible")) {
            f = documentPopup.formFile;
        }

        if (f !== undefined) {

            var params = {
                accion: 								    "<%= ProjectServlet.JX_DELETE_DOC %>",
                <%= Documentproject.IDDOCUMENTPROJECT %>:   f.<%= Documentproject.IDDOCUMENTPROJECT%>.value,
                <%= Documentproject.TYPE %>:                $(".documentType").val()
            };

            projectAjax.call(params, function(data) {

                // Execute callback function
                documentPopup.removeCallback();

                documentPopup.close();
            });
        }
    }
};

$(document).ready(function() {

	documentPopup.initialize();
	
	$("#res_document_link").keyup(function(){
		$("#linkHRef").attr("href", $("#res_document_link").val());
	});
	
	$('#buttonLink').on('click', function (event) {
		documentPopup.saveLink();
	});	
	
	$('#buttonFile').on('click', function (event) {
		documentPopup.fileUpload();
	});	
	
	$('#buttonCancel').on('click', function (event) {
		documentPopup.close();
	});

    $('#buttonDelete').on('click', function () {
        documentPopup.remove();
    });
});
//-->
</script>

<div id="document-popup" class="popup" style="min-height: 0px;!important">
	
	<%-- ERRORS --%>
	<div id="document-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="document-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<%-- SELECT OPTION --%>
	<div class="selection" style="text-align: center; margin-bottom: 12px; margin-top: 14px; display:block;">
		<fieldset>
			<legend><fmt:message key="wbs.selection"/></legend>
			<fmt:message key="<%= DocumentType.LINK.getMessageKey() %>"/>:&nbsp;<input type="radio" name="optionsDoc" id="radioLink" value="link" style="margin-right: 10px; width: 15px;" onclick="documentPopup.showLink();"/>
			<fmt:message key="<%= DocumentType.FILE_SYSTEM.getMessageKey() %>"/>:&nbsp;<input type="radio" name="optionsDoc" id="radioFile" value="file" style="width:15px;" onclick="documentPopup.showFile();"/>
		</fieldset>
	</div>
	
	<%-- FILE --%>
	<div id="fileOption" class="option" style="display:block">	
		<fieldset>
			<legend><fmt:message key="<%= DocumentType.FILE_SYSTEM.getMessageKey() %>" /></legend>
			<form id="frm_file_upload" name="frm_file_upload" method="POST" enctype="multipart/form-data" >
				<input type="hidden" name="accion" value="" />
				<input type="hidden" name="<%= Documentproject.TYPE%>" value="<%=type%>" class="documentType"/>
				<input type="hidden" name="<%= Documentproject.IDDOCUMENTPROJECT%>" value="-1" />
				<input type="hidden" name="<%= Documentproject.PROJECT%>" value="${project.idProject}"/>

                <%-- ids --%>
				<input type="hidden" name="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>" value="" class="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>"/>
                <input type="hidden" name="<%= Timeline.IDTIMELINE %>" value="" class="<%= Timeline.IDTIMELINE %>"/>
				
				<table id="search-filesystem-table" border="0" cellpadding="2" cellspacing="1" style="width:100%">
					<tr id="doc_name_row">
						<th class="left" width="10%"><fmt:message key="document.name"/>:&nbsp;</th>
						<td width="90%" id="name_column"></td>
					</tr>				
					<tr>
						<td colspan = 2>
							<input type="text" name="filesystem_path" id="filesystem_path" value="" style="width: 90%; border: 1px solid #000000;" readonly />
							<label class="file_change" style="margin-right:5px; *margin-top: -15px !important;">
								<input type="file" name="fileToUpload" id="fileToUpload" onChange="documentProject.setFile(this,'filesystem_path');" style="width: 0;"/>
							</label>
						</td>					
					</tr>	
					<tr>
						<th class="left" colspan = 2><fmt:message key="document_comment" /></th>						
					</tr>
					<tr>
						<td class="center" colspan = 2>
							<textarea name="<%= Documentproject.CONTENTCOMMENT%>" class="campo show_scroll" rows="3" style="width:97%;"></textarea>
						</td>						
					</tr>			 		
				</table>
			</form>		
		</fieldset>	
	</div>
	
	<%-- LINK --%>
	<div id="linkOption" class="option" style="display:block">
		<fieldset>
			<legend><fmt:message key="<%= DocumentType.LINK.getMessageKey() %>" /></legend>
			<form id="frm_link" name="frm_link" method="POST" >
				<input type="hidden" name="accion" value="" />
				<input type="hidden" name="<%= Documentproject.TYPE%>" value="${param.documentationType}" class="documentType"/>
				<input type="hidden" name="<%= Documentproject.IDDOCUMENTPROJECT%>" value="-1"/>
				<input type="hidden" name="<%= Documentproject.PROJECT%>" value="${project.idProject}"/>

                <%-- ids --%>
				<input type="hidden" name="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>" value="" class="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>"/>
                <input type="hidden" name="<%= Timeline.IDTIMELINE %>" value="" class="<%= Timeline.IDTIMELINE %>"/>
				
				<table id="link-table" border="0" cellpadding="2" cellspacing="1" style="width:100%">
					<tr>
						<td>
							<input type="text" name="<%= Documentproject.LINK %>" id="res_document_link" class="campo" value="" />
						</td>
					</tr>
					<tr>
						<td>
							<div style="margin-left: 6px;margin-top: 5px;margin-bottom: 5px;">
								<a id="linkHRef" href="" target="_blank"><fmt:message key="<%= DocumentType.LINK.getMessageKey() %>" /></a>
							</div>
						</td>
					</tr>
					<tr>
						<th class="left" colspan = 2><fmt:message key="document_comment" /></th>						
					</tr>
					<tr>
						<td class="center" colspan = 2>
							<textarea name="<%= Documentproject.CONTENTCOMMENT%>" class="campo show_scroll" rows="3" style="width:97%;"></textarea>
						</td>						
					</tr>			 		
				</table>
			</form>
		</fieldset>
	</div>
	
	<div class="right" style="margin-top:10px;">
        <input type="button" class="boton" id="buttonDelete" value='<fmt:message key="remove" />' style="float: left;display: none;"/>
		<c:if test="${op:hasPermission(user,project.status,tab)}">
			<input type="button" class="boton" id="buttonLink" value=""/>
		</c:if>
		<input type="button" class="boton" id="buttonFile" value=""/>
		<input type="button" class="boton" id="buttonCancel" value=""/>
   	</div>	
</div>
