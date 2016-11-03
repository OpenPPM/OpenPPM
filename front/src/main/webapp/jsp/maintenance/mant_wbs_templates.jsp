<%@page import="es.sm2.openppm.core.model.impl.Changetype"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

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
  ~ File: mant_wbs_templates.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_wbs_template">
	<fmt:param><fmt:message key="maintenance.wbs_templates"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_wbs_template">
	<fmt:param><fmt:message key="maintenance.wbs_templates"/></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_wbs">
	<fmt:param><fmt:message key="wbs_node"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_wbs">
	<fmt:param><fmt:message key="wbs_node"/></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >

var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');

var wbsTemplatesTable;
var childsWbsTemplatesTable;

var wbsTemplateObject = constructorWBSTemplate();

function constructorWBSTemplate(){
	
	return {
		// CREATE ROOTS TABLE
		createRootsTable: function(){
			return $('#tb_wbstemplates').dataTable({
				"sPaginationType": "full_numbers",
				"oLanguage": datatable_language,
				"bInfo": false,
				"aaSorting": [[0,'asc'], [1,'asc']],
				"aoColumns": [ 
					{ "sClass": "center", "bVisible": false },
					{ "sClass": "left" },
			        { "sClass": "center", "bSortable" : false}
		      	]
			});
		},
		// CREATE CHILDS TABLE
		createChildsTable: function(){
			return $('#tb_childs_wbstemplate').dataTable({
				"sPaginationType": "full_numbers",
				"oLanguage": datatable_language,		
				"bInfo": false,
				"iDisplayLength": 25,
				"bAutoWidth": false,
				"aaSorting": [[ 1, "asc" ]],
				"aoColumns": [ 
		             { "bVisible": false }, 
		             { "sClass": "center" }, 
		             { "bVisible": true }, 
		             { "bVisible": false }, 
		             { "sClass": "center", "bSortable": false },
		             { "sClass": "center", "bSortable": false }
		     	]
			});
		},
		// EDIT ROOT
		editWBSTemplate:function(element){
			var wbsTemplate = wbsTemplatesTable.fnGetData( element.parentNode.parentNode );
			
			var params = {
				accion: "<%=MaintenanceServlet.JX_VIEW_NODE_WBS_TEMPLATE %>",
				idRoot: -1,
				idWbsnode: wbsTemplate[0]
			};
			
			mainAjax.call(params, function (wbsNode) {
				
				var f = document.forms["frm_wbsnode"];
				
				f.reset();
				$("#wbs_parent").empty();
				
				f.id.value			= ''; //No father
				f.wbs_id.value 		= wbsNode.idWbsnode;
				f.wbs_code.value 	= unEscape(wbsNode.code);
				f.wbs_name.value 	= unEscape(wbsNode.name);
				f.wbs_parent.value 	= wbsNode.idParent;
				f.wbs_desc.value	= unEscape(wbsNode.description);
				f.wbs_ca.checked 	= wbsNode.isControlAccount;
				
				$("#wbs_parent").attr("disabled", "disabled");
				
				$('#wbsnode-popup').dialog('open');
			});
		},
		// VIEW CHILDS
		viewWBSTemplate: function(element){
			var wbsTemplate = wbsTemplatesTable.fnGetData( element.parentNode.parentNode);
			
			wbsTemplateObject.showChilds(wbsTemplate[0]);
			
			$("#id").val(wbsTemplate[0]);
			$("#idChild").val("");
			
			$("#viewWBSTemplate").show();
			$("#WBSPanel").show();
			$("#graphicWBSPanel").hide();
			
			if($("#graphicWBSPanel").css("display") == "none"){
				$("#graphicWBSPanel"+"Btn").attr("src","images/ico_mas.gif");
			}		
		},
		showChilds: function(idParent){
			var params = {
				accion: "<%=MaintenanceServlet.JX_VIEW_CHILDS_WBS_TEMPLATE %>",
				id: idParent
			};
			
			mainAjax.call(params, function (data) {
				
				childsWbsTemplatesTable.fnClearTable();
				
				$(data.childs).each(function() {
					
					if(this.isControlAccount){
						var inputCA = '<input type="checkbox" checked disabled/>';
					}
					else {
						var inputCA = '<input type="checkbox" disabled/>';
					}
					
					wbsnode = [
					           this.idWbsnode,
					           unEscape(this.code),
					           unEscape(this.name),
					           this.idParent,
					           inputCA,
					           '<img onclick="wbsTemplateObject.editChildWBSTemplate(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/>' + 
								'&nbsp;&nbsp;&nbsp;' +
								'<img src="images/delete.jpg" class="link" onclick="wbsTemplateObject.deleteChildWBSTemplate(this)" title="<fmt:message key="delete"/>" />'
					           ];
					
					childsWbsTemplatesTable.fnAddData(wbsnode);
					
				});
			});
		},
		// DELETE ROOT
		deleteWBSTemplate: function(element){
			confirmUI(
					'${msg_title_confirm_delete_wbs_template}','${msg_confirm_delete_wbs_template}',
					'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
					function() {
						
						var wbsTemplate = wbsTemplatesTable.fnGetData(element.parentNode.parentNode);
						
						var f = document.frm_wbstemplates;
						f.accion.value = "<%=MaintenanceServlet.DEL_WBS_TEMPLATE%>";
						f.id.value = wbsTemplate[0];
						f.idManten.value = $('select#idManten').val();
						loadingPopup();
						f.submit();
					}
			);
		},
		// EDIT CHILD
		editChildWBSTemplate:function(element){
			var childWbsTemplate = childsWbsTemplatesTable.fnGetData(element.parentNode.parentNode);
			
			var params = {
				accion: "<%=MaintenanceServlet.JX_VIEW_NODE_WBS_TEMPLATE %>",
				idRoot: $("#id").val(),
				idWbsnode: childWbsTemplate[0]
			};
				
			mainAjax.call(params, function (wbsNode) {
				
				var f = document.forms["frm_wbsnode"];
				
				f.reset();
				f.id.value			= $("#id").val();
				f.wbs_id.value 		= wbsNode.idWbsnode;
				f.wbs_code.value 	= unEscape(wbsNode.code);
				f.wbs_name.value 	= unEscape(wbsNode.name);
				f.wbs_parent.value 	= wbsNode.idParent;
				f.wbs_desc.value	= unEscape(wbsNode.description);
				f.wbs_ca.checked 	= wbsNode.isControlAccount;
				
				var options = [];
		    	for (var i = 0; i < wbsNode.parents.length; i++) {
		    		
			    	options[i] = $('<option>').attr("value",wbsNode.parents[i].idWbsnode).append(wbsNode.parents[i].name);
			    	
			    	if(wbsNode.parents[i].idWbsnode == wbsNode.idParent){
			    		$(options[i]).attr("selected","selected");
			    	}
		   		}
		    	
		    	$("#wbs_parent").empty();
		      	$("#wbs_parent").removeAttr("disabled");
		    	
		    	for(var i = 0; i < options.length; i++){
		    		$("#wbs_parent").append(options[i]);
		    	}
				
		    	$('#wbsnode-popup').dialog('open');
			});	
		},
		// DELETE CASCADE CHILD
		deleteChildWBSTemplate:function(element){
			confirmUI(
					'${msg_title_confirm_delete_wbs}','${msg_confirm_delete_wbs}',
					'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
					function() {
						
						var childWbsTemplate = childsWbsTemplatesTable.fnGetData(element.parentNode.parentNode);
						
						var params = {
							accion: "<%=MaintenanceServlet.JX_DEL_WBS_TEMPLATE_NODE %>",
							id: childWbsTemplate[0]
						};
							
						mainAjax.call(params, function (data) {
							
							wbsTemplateObject.showChilds($("#id").val());
							
							if ($("#graphicWBSPanel").css("display") == "block") {
								wbsTemplateObject.chartWBS();	
							}					
						});	
						
					}
			);
		},
		// CREATE CHILD
		addChildWBSTemplate: function(){
			var f = document.forms["frm_wbsnode"];
			
			f.reset();
			f.id.value 		= $("#id").val();
			f.wbs_id.value 	= "";
			
			var params = {
				accion: "<%=MaintenanceServlet.JX_VIEW_NODE_WBS_TEMPLATE %>",
				idRoot: $("#id").val(),
				idWbsnode: -1
			};
					
			mainAjax.call(params, function (wbsNode) {
			
				var options = [];
		    	for (var i = 0; i < wbsNode.parents.length; i++) {
		    		
			    	options[i] = $('<option>').attr("value",wbsNode.parents[i].idWbsnode).append(wbsNode.parents[i].name);
			    	
			    	if(wbsNode.parents[i].idWbsnode == wbsNode.idParent){
			    		$(options[i]).attr("selected","selected");
			    	}
		   		}
		    	
		    	$("#wbs_parent").empty();
		    	$("#wbs_parent").removeAttr("disabled");
		    	
		    	for(var i = 0; i < options.length; i++){
		    		$("#wbs_parent").append(options[i]);
		    	}
				
		    	$('#wbsnode-popup').dialog('open');
			});		
		},
		//SAVE CHILD
		saveChildWBSTemplate: function(){
			var f = document.forms["frm_wbsnode"];
			
			var params = {
				accion: "<%= MaintenanceServlet.JX_SAVE_WBS_TEMPLATE_NODE %>",
				id: f.id.value,
				wbs_id: f.wbs_id.value,
				wbs_code: f.wbs_code.value,
				wbs_parent: f.wbs_parent.value,
				wbs_name: f.wbs_name.value,
				wbs_desc: f.wbs_desc.value,
				wbs_ca: f.wbs_ca.checked
			};
			
			mainAjax.call(params, function (data) {
				
				if ( f.id.value == '') {
					
					var dataRow = [
									data.idWbsnode,
									unEscape(data.name),
									'<img onclick="wbsTemplateObject.viewWBSTemplate(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">' +
									'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
									'<img onclick="wbsTemplateObject.editWBSTemplate(this)" title="<fmt:message key="view"/>" class="link" src="images/modify.png">' +
									'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
									'<img onclick="wbsTemplateObject.deleteWBSTemplate(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
									];
					
					wbsTemplatesTable.fnUpdateRow(dataRow); 
				}
				else {
					
					var inputCA = '';
					if(data.isControlAccount){
						inputCA = '<input type="checkbox" checked disabled/>';
					}
					else {
						inputCA = '<input type="checkbox" disabled/>';
					}
					
					var dataRow = [
									data.idWbsnode,
									unEscape(data.code),
									unEscape(data.name),
									data.idParent,
									inputCA,
									'<img onclick="wbsTemplateObject.editChildWBSTemplate(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/>' + 
									'&nbsp;&nbsp;&nbsp;' +
									'<img onclick="wbsTemplateObject.deleteChildWBSTemplate(this)" class="link" src="images/delete.jpg" title="<fmt:message key="delete"/>" />'
									];
					
					if ( f.wbs_id.value == "" ) { childsWbsTemplatesTable.fnAddDataAndSelect(dataRow); } 
					else { childsWbsTemplatesTable.fnUpdateRow(dataRow); }
				}
				
				
				if ($("#graphicWBSPanel").css("display") == "block") {
					wbsTemplateObject.chartWBS();
				}
				
				$('div#wbsnode-popup').dialog('close');
			});
		},
		// DRAW CHART
		chartWBS: function(){
			var params = {
				accion: "<%= MaintenanceServlet.JX_WBS_CHART %>",
				id: $("#id").val()
			};
			
			$('#wbsChart').html('${loading_chart}');

			mainAjax.call(params, function (data) {

				if (typeof data.name === 'undefined') {
					$("#wbsChart").html('${data_not_found}');
				}
				else {
					$('#wbsChart').html(initWbs(data));	
				    $("#wbsOrg").jOrgChart({
						chartElement : '#wbsChart'
					});
				    
				    $('#legendWbs').empty();
				    createLegend('#legendWbs', '<fmt:message key="work_group"/>', "white",  "gray");
					createLegend('#legendWbs', '<fmt:message key="wbs.ca_long"/>', "#FFD876", "gray");
				}
			});
		}
		
	};
	
}

readyMethods.add(function () {
	
	// CREATE TABLES
	wbsTemplatesTable 		= wbsTemplateObject.createRootsTable();
	childsWbsTemplatesTable = wbsTemplateObject.createChildsTable();
	
	
	// EVENTS
	$('#tb_wbstemplates tbody tr').live('click',  function (event) {
		wbsTemplatesTable.fnSetSelectable(this,'DTTT_selected');
	});
	
	$('#tb_childs_wbstemplate tbody tr').live('click',  function (event) {
		childsWbsTemplatesTable.fnSetSelectable(this);
	});
	
	$('#WBSChartReload').on('click', function(e){
		e.stopPropagation();
		wbsTemplateObject.chartWBS();	
	});
	
});

</script>

<form id="frm_wbstemplates" name="frm_wbstemplates" method="post" action="<%=MaintenanceServlet.REFERENCE%>">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	
	<div class="fieldset">
		<span class="legendFieldset"><fmt:message key="maintenance.wbs_templates" /></span>
		
		<%-- WBStemplate roots table --%>
		<table id="tb_wbstemplates" class="tabledata" width="100%">
			<thead>
				<tr>
				 <th width="0%"></th>
				 <th width="92%"><fmt:message key="wbs.name" /></th>
				 <th width="8%"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="wbsTemplate" items="${list}">
					<tr>
						<td>${wbsTemplate.idWbsnode}</td>
						<td>${tl:escape(wbsTemplate.name)}</td>
						<td>
							<img onclick="wbsTemplateObject.viewWBSTemplate(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
							&nbsp;&nbsp;&nbsp;
							<img onclick="wbsTemplateObject.editWBSTemplate(this)" title="<fmt:message key="view"/>" class="link" src="images/modify.png">
							&nbsp;&nbsp;&nbsp;
							<img onclick="wbsTemplateObject.deleteWBSTemplate(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div id="viewWBSTemplate" class="content" style="display:none;">
			<fmt:message key="wbs" var="titleChildsWBS"/>
			<visual:panel id="WBSPanel" title="${titleChildsWBS}">
			
				<%-- WBStemplate all childs of root table --%>
				<table id="tb_childs_wbstemplate" class="tabledata" width="100%">
					<thead>
						<tr>
							<th width="0%"><fmt:message key="wbs.wbs"/></th>
							<th width="10%"><fmt:message key="wbs.code"/></th>
							<th width="77%"><fmt:message key="wbs.name"/></th>
							<th width="0%"><fmt:message key="wbs.parent"/></th>
							<th width="5%"><fmt:message key="wbs.ca"/></th>
							<th width="8%">
								<img src="images/add.png" class="link" onclick="wbsTemplateObject.addChildWBSTemplate()" title="<fmt:message key="add"/>"/>
							</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
				
				<%-- WBSTemplate graphic --%>
				<c:set var="WBSChartBTN">
					<img id="WBSChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
				</c:set>
				<fmt:message key="wbs_chart" var="titleWBSGraphic"/>
				<visual:panel id="graphicWBSPanel" title="${titleWBSGraphic}" callback="wbsTemplateObject.chartWBS" buttons="${WBSChartBTN}" cssClass="panel2" >
					<div style="overflow-y: hidden; overflow-x: auto; margin:0 auto;">
						<div id="wbsChart"></div>		
					</div>
					<div id="legendWbs" class="legendChart"></div>
				</visual:panel>
				
			</visual:panel>
		</div>
		
		<div class="spacer"></div>
		
	</div>
</form>

<jsp:include page="popups/new_wbs_template_node_popup.jsp" flush="true" />
