<%@page import="es.sm2.openppm.core.model.impl.Timeline"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page import="es.sm2.openppm.core.model.enums.ImportanceEnum" %>
<%@ page import="es.sm2.openppm.api.model.enums.MaintenanceEnum" %>
<%@ page import="es.sm2.openppm.core.model.impl.Documentproject" %>

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
  ~ File: timeline_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for colors --%>
<fmt:message key="imp.high_importance" var="high" />
<fmt:message key="imp.low_importance" var="low" />
<fmt:message key="imp.medium_importance" var="medium" />
<fmt:message key="imp.normal_importance" var="normal" />


<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select_a" var="fmtTimelineDateRequired">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
</fmt:message>

<fmt:message key="maintenance.error_msg_select_a" var="fmtTimelineImportanceRequired">
    <fmt:param><b><fmt:message key="imp"/></b></fmt:param>
</fmt:message>

<fmt:message key="maintenance.error_msg_select_a" var="fmtTimelineNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<fmt:message key="msg.error.invalid_format" var="fmtDateFormat">
    <fmt:param><b><fmt:message key="date"/></b></fmt:param>
    <fmt:param>${datePattern}</fmt:param>
</fmt:message>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="timeline"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="timeline"/></fmt:param>
</fmt:message>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<c:set var="buttonsTimeline">
        <img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">&nbsp;&nbsp;&nbsp;<img title="<fmt:message key="view"/>" class="link viewTimeline" src="images/view.png">&nbsp;&nbsp;&nbsp;<img title="<fmt:message key="delete"/>" class="link removeTimeline" src="images/delete.jpg">
    </c:set>
</c:if>

<script language="javascript" type="text/javascript" >
<!--
var timelineValidator,timelinePopup;

var timeline = {
	view : function() {
		
		timelineValidator.resetForm();

		var row = timelineTable.fnGetData( this.parentNode.parentNode.parentNode );

		var f = document.forms["frm_timeline_popup"];
		f.reset();

		f.<%=Timeline.IDTIMELINE%>.value 				= row[0];
		f.<%=Project.IDPROJECT%>.value		 			= row[1];
		f.<%=Timeline.TIMELINEDATE%>.value		 		= row[2];

        f.<%=Timeline.IMPORTANCE%>.value		 		= row[3];
        f.<%=Timeline.NAME%>.value				 		= unEscape(row[7]);

        $('#<%=Timeline.IMPORTANCE%>').removeClass();
        $('#<%=Timeline.IMPORTANCE%>').addClass('campo');
        $('#<%=Timeline.IMPORTANCE%>').addClass($("#<%=Timeline.IMPORTANCE%>").find("option:selected").attr("class"));
        $('#<%=Timeline.IMPORTANCE%>').attr("title" , $("#<%=Timeline.IMPORTANCE%>").find("option:selected").attr('title'));

        $(f.<%=Timeline.DESCRIPTION%>).text(unEscape(row[8]));

        // Set document data
        f.<%= Documentproject.IDDOCUMENTPROJECT %>.value	= row[10];
        f.<%= Documentproject.CONTENTCOMMENT %>.value		= row[11];
        f.filesystem_path.value		 		                = row[12];
        f.<%= Documentproject.LINK %>.value		 		    = row[13];

        // Load timeline types
        timeline.loadTimelineTypes(row[5], timeline.open);
	},
	add : function() {
		
		timelineValidator.resetForm();
		
		var f = document.forms["frm_timeline_popup"];
		f.reset();
		f.<%=Timeline.IDTIMELINE%>.value = '';
		$(f.<%=Timeline.DESCRIPTION%>).text('');
        $('#<%=Timeline.IMPORTANCE%>').removeClass();
        $('#<%=Timeline.IMPORTANCE%>').addClass('campo');
        $('#<%=Timeline.IMPORTANCE%>').addClass('select_color');

        // Reset data document
        f.<%= Documentproject.IDDOCUMENTPROJECT %>.value	= '';
        f.<%= Documentproject.CONTENTCOMMENT %>.value		= '';
        f.filesystem_path.value		 		                = '';
        f.<%= Documentproject.LINK %>.value		 		    = '';

        // Load timeline types
        timeline.loadTimelineTypes(undefined, timeline.open);
	},
    loadTimelineTypes: function(optionSelected, callback) {

        $.ajax({
            url: 'rest/maintenance/' + '<%= MaintenanceEnum.TIMELINE_TYPE.name() %>',
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            data: {},
            success: function (data) {

                // initialize
                $("#"+'<%=Timeline.TIMELINETYPE%>').empty();
                $("#"+'<%=Timeline.TIMELINETYPE%>').append('<option value=""><fmt:message key="sel_opt"/></option>');

                if (typeof data !== 'undefined') {

                    // Add list
                    $(data).each(function() {
                        $("#"+'<%=Timeline.TIMELINETYPE%>').append('<option value="'+this.code+'">' + this.name + '</option>');
                    });
                }

                if (optionSelected !== 'undefined'){
                    $("#"+'<%=Timeline.TIMELINETYPE%>').val(optionSelected);
                }
                else {
                    $("#"+'<%=Timeline.TIMELINETYPE%>').val('');
                }

                // Open popup
                callback();
            }
        });
    },
	save : function() {

		if (timelineValidator.form()) {
			
			controlAjax.call($("#frm_timeline_popup").serializeArray(), function (data) {

                var f = document.forms["frm_timeline_popup"];


                // Load buttons
                //
                var buttons = '<nobr>';

                if (f.<%= Documentproject.IDDOCUMENTPROJECT %>.value !== undefined &&
                        f.<%= Documentproject.IDDOCUMENTPROJECT %>.value !== "" &&
                        f.filesystem_path.value !== "") {

                    buttons += '<img title="<fmt:message key="download"/>: '+ f.filesystem_path.value + '" class="link download" src="images/download.png">'+
                                '&nbsp;&nbsp;&nbsp;';
                }
                else if (f.<%= Documentproject.IDDOCUMENTPROJECT %>.value !== undefined &&
                        f.<%= Documentproject.IDDOCUMENTPROJECT %>.value !== "" &&
                        f.<%= Documentproject.LINK %>.value !== "") {

                    buttons += '<img title="<fmt:message key="link"/>: '+ f.<%= Documentproject.LINK %>.value + '" class="link hyperlink" src="images/link.png"/>'+
                                '&nbsp;&nbsp;&nbsp;';
                }

                buttons += '${buttonsTimeline}' + '</nobr>';


                var importanceClass = $("#importance").find("option:selected").attr("class");
                var importanceTitle = $("#importance").find("option:selected").attr("title");
                var importanceOrder = $("#importance").find("option:selected").attr("order");

				var dataRow = [
	      		    data.<%=Timeline.IDTIMELINE %>,
	      		  	f.<%=Project.IDPROJECT %>.value,
	                f.<%=Timeline.TIMELINEDATE%>.value,
                    f.<%=Timeline.IMPORTANCE%>.value,
                    '<div style="margin: 0px auto; width: 80px;">'+
                    '<div style="display:none;" class="importance">' + importanceOrder + '</div>'+
                    '<div class="' + importanceClass + '" title="' + importanceTitle + '" title>&nbsp;</div>',
                    $("#timelineType option:selected").val(),
                    $("#timelineType option:selected").val() === "" ? "" : $("#timelineType option:selected").html(),
                    escape(f.<%=Timeline.NAME%>.value),
					escape(f.<%=Timeline.DESCRIPTION%>.value),
					buttons,
                    // Data documents
                    f.<%= Documentproject.IDDOCUMENTPROJECT %>.value,
                    f.<%= Documentproject.CONTENTCOMMENT %>.value,
                    f.filesystem_path.value,
                    f.<%= Documentproject.LINK %>.value
	  			];

				if (f.<%=Timeline.IDTIMELINE %>.value == data.<%=Timeline.IDTIMELINE %>) {
					timelineTable.fnUpdateAndSelect(dataRow);
				}
				else {
                    timelineTable.fnAddDataAndSelect(dataRow);
                }

				timelinePopup.dialog('close');
			});
		}
	},
	remove : function() {
		
		var row = timelineTable.fnGetData( this.parentNode.parentNode.parentNode );
		
		var params = {
			accion: "<%=ProjectControlServlet.JX_DELETE_TIMELINE%>", 
			<%=Timeline.IDTIMELINE%>: row[0]
		};
		
		confirmUI('${msgTitleConfirmDelete}','${msgConfirmDelete}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>', function() {
		
			controlAjax.call(params, function (data) { timelineTable.fnDeleteSelected(); });
		});
	},
	load : function() {
		
		$(".removeTimeline").live('click', timeline.remove);
		$(".viewTimeline").live('click', timeline.view);
		$("#addTimeline").on('click', timeline.add);
		$("#saveTimeline").on('click', timeline.save);
		$("#closeTimeline").on('click', timeline.close);
	},
	close: function() { timelinePopup.dialog('close'); },
    open: function() {timelinePopup.dialog('open');},
    // New document
    addDocument: function(){
        documentPopup.resetPopup();
        documentPopup.open('mixed', '<%= Constants.DocumentProjectType.DOCUMENT_TIMELINE.getName() %>');
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

            documentPopup.open('<%= Documentproject.DocumentType.FILE_SYSTEM.getName() %>', '<%= Constants.DocumentProjectType.DOCUMENT_TIMELINE.getName() %>');
        }
        // Link
        else {

            var f = document.forms["frm_link"];

            f.<%= Documentproject.IDDOCUMENTPROJECT %>.value 	= doc[0];
            f.<%= Documentproject.CONTENTCOMMENT %>.value 		= doc[1];
            f.<%= Documentproject.LINK %>.value 				= doc[3];

            $("#linkHRef").attr("href", doc[3]);
            $("#linkHRef").html(doc[3]);

            documentPopup.open('<%= Documentproject.DocumentType.LINK.getName() %>', '<%= Constants.DocumentProjectType.DOCUMENT_TIMELINE.getName() %>');
        }
    },
    // Update data row
    addData: function(data) {

        var row = timelineTable.fnFindData(data.<%= Timeline.IDTIMELINE %>);

        if (row != null && row !== undefined) {

            var f;

            // File
            if (data.documentType == '<%= Documentproject.DocumentType.FILE_SYSTEM.getName() %>') {

                f = document.forms["frm_file_upload"];

                row[12] = $("#filesystem_path").val();

                // Reset icons
                row[9] = 	'<nobr>'+
                                '<img title="<fmt:message key="download"/>: '+ row[12] + '" class="link download" src="images/download.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="view"/>" class="link viewTimeline" src="images/view.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="delete"/>" class="link removeTimeline" src="images/delete.jpg">'+
                            '</nobr>';
            }
            // Link
            else {

                f = document.forms["frm_link"];

                row[13] = f.<%= Documentproject.LINK %>.value;

                // Reset icons
                row[9] = 	'<nobr>'+
                                '<img title="<fmt:message key="link"/>: '+ row[13] + '" class="link hyperlink" src="images/link.png"/>'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="view"/>" class="link viewTimeline" src="images/view.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="delete"/>" class="link removeTimeline" src="images/delete.jpg">'+
                            '</nobr>';
            }

            // Update document
            row[10] = data.<%= Documentproject.IDDOCUMENTPROJECT %>;
            row[11] = f.<%= Documentproject.CONTENTCOMMENT %>.value;

            timelineTable.fnUpdateAndSelect(row);

            // Reset form
            f.<%= Timeline.IDTIMELINE %>.value = "";
        }
    },
    delDocument: function() {

        var idTimeline = documentPopup.idRow;

        if (idTimeline !== undefined && idTimeline !== "") {

            var row = timelineTable.fnFindData(idTimeline);

            if (row !== undefined) {

                // Set document information into new array
                for (var i = 10; i < row.length; i++) {
                    row[i] = "";
                }

                // Reset icons
                row[9] = 	'<nobr>'+
                                '<img title="<fmt:message key="add"/>" class="link append" src="images/clip.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="view"/>" class="link viewTimeline" src="images/view.png">'+
                                '&nbsp;&nbsp;&nbsp;'+
                                '<img title="<fmt:message key="delete"/>" class="link removeTimeline" src="images/delete.jpg">'+
                            '</nobr>';

                timelineTable.fnUpdateAndSelect(row);
            }
        }
    }
};

function changeColorTimeLine(idSelect) {

    if (idSelect != '') {
        $('#'+idSelect).removeClass();
        $('#'+idSelect).addClass('campo');
        $('#'+idSelect).addClass($('#'+idSelect).children("[selected]").attr('class'));
        $('#'+idSelect).attr("title" , $('#'+idSelect).children("[selected]").attr('title'));
    }
}

//When document is ready
readyMethods.add(function() {
	
	timelinePopup = $('div#timeline_popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 300, 
		resizable: false,
		open: function(event, ui) { timelineValidator.resetForm(); }
	});
	
	// validate the form when it is submitted
	timelineValidator = $("#frm_timeline_popup").validate({
		errorContainer: 'div#timeline-errors',
		errorLabelContainer: 'div#timeline-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Timeline.TIMELINEDATE%> : { required: true, date: true },
            <%=Timeline.IMPORTANCE%> : { required: true },
			<%=Timeline.NAME%> : { required: true },
			<%=Timeline.DESCRIPTION%>: { maxlength: 2000 }
		},
		messages: {
			<%=Timeline.TIMELINEDATE %> : {required: '${fmtTimelineDateRequired}', date: '${fmtDateFormat}'},
            <%=Timeline.IMPORTANCE %> : {required: '${fmtTimelineImportanceRequired}' },
			<%=Timeline.NAME %> : {required: '${fmtTimelineNameRequired}' }
		}
	});
	
	$('#<%=Timeline.TIMELINEDATE%>').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (timelineValidator.numberOfInvalids() > 0) timelineValidator.form();
		}
	});

	timeline.load();

    $('#frm_timeline_popup .select_color').change(function() {
        changeColorTimeLine($(this).attr('id'));
    });

});
//-->
</script>

<div id="timeline_popup" class="popup">

	<div id="timeline-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_timeline_popup" id="frm_timeline_popup">
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_SAVE_TIMELINE %>" />
		<input type="hidden" name="<%=Timeline.IDTIMELINE%>"/>
		<input type="hidden" name="idProject" value="${project.idProject}"/>

        <%-- Document data --%>
        <input type="hidden" name="<%= Documentproject.IDDOCUMENTPROJECT %>" value=""/>
        <input type="hidden" name="<%= Documentproject.CONTENTCOMMENT %>" value=""/>
        <input type="hidden" name="filesystem_path" value=""/>
        <input type="hidden" name="<%= Documentproject.LINK %>" value=""/>

		<fieldset>
			<legend><fmt:message key="timeline"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" colspan="3"><fmt:message key="name"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="3"><input type="text" name="<%=Timeline.NAME%>" id="<%=Timeline.NAME %>" maxlength="50" class="campo" /></td>
				</tr>
                <tr>
                    <th class="left" width="25%"><fmt:message key="date"/>&nbsp;*</th>
                    <th class="left" width="30%"><fmt:message key="imp"/>&nbsp;*</th>
                    <th class="left" width="45%"><fmt:message key="TIMELINE_TYPE"/>&nbsp;</th>
                </tr>
                <tr>
                    <td><input type="text" name="<%=Timeline.TIMELINEDATE%>" id="<%=Timeline.TIMELINEDATE %>" class="campo fecha" /></td>
                    <td>
                        <div id="impor">
                            <select name="<%=Timeline.IMPORTANCE%>" id="<%=Timeline.IMPORTANCE%>" class="campo select_color" >
                                <option value=""><fmt:message key="sel_opt"/></option>
                                <c:forEach var="importance" items="<%= ImportanceEnum.values() %>" >
                                    <option class="${importance.classHtml}" value="${importance}" title="<fmt:message key="${importance.label}"/>" order="${importance.order}"></option>
                                </c:forEach>
                            </select>
                        </div>
                    </td>
                    <td>
                        <select name="<%=Timeline.TIMELINETYPE%>" id="<%=Timeline.TIMELINETYPE%>" class="campo" >
                            <option value=""><fmt:message key="sel_opt"/></option>
                        </select>
                    </td>
                </tr>
				<tr>
					<th class="left" colspan=3><fmt:message key="description"/></th>
				</tr>
				<tr>
					<td colspan=3>
						<textarea rows="6" name="<%=Timeline.DESCRIPTION%>" id="<%=Timeline.DESCRIPTION%>" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
			</table>			
    	</fieldset>
    	<div class="popButtons">
			<input type="button" class="boton" id="saveTimeline" value="<fmt:message key="save" />" />
			<input type="button" class="boton" id="closeTimeline" value="<fmt:message key="close" />" />
    	</div>
    </form>
</div>