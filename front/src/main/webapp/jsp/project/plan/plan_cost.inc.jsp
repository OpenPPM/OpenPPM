<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>

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
  ~ File: plan_cost.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="actions.projects.approve_project" var="act_approve" />
<fmt:message key="actions.projects.delete_income" var="act_del_income" />
<fmt:message key="actions.projects.delete_followup" var="act_del_followup" />
<fmt:message key="actions.projects.delete_cost" var="act_del_cost" />
<fmt:message key="actions.projects.delete_iwo" var="act_del_iwo" />
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_income">
  <fmt:param><fmt:message key="income"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_income">
  <fmt:param><fmt:message key="income"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_followup">
  <fmt:param><fmt:message key="followup"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_followup">
  <fmt:param><fmt:message key="followup"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_cost">
  <fmt:param><fmt:message key="cost"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_cost">
  <fmt:param><fmt:message key="cost"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_iwo">
  <fmt:param><fmt:message key="iwo"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_iwo">
  <fmt:param><fmt:message key="iwo"/></fmt:param>
</fmt:message>
<fmt:message key="select_opt" var="msg_select_opt" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="data_not_found" var="dataNotFound" />
<fmt:message key="pl_chart.warning_pl_data" var="warning_pl" />
<fmt:message key="msg_to_excel" var="msg_to_excel" />

<fmt:message key="loading_chart" var="loading_chart" />

<c:set var="expense_id"><%=Constants.COST_TYPE_EXPENSE%></c:set>
<fmt:message key="cost.type.expense" var="expense_value"></fmt:message>
<c:set var="direct_id"><%=Constants.COST_TYPE_DIRECT%></c:set>
<fmt:message key="cost.type.direct" var="direct_value"></fmt:message>
<fmt:message key="error" var="fmt_error" />
<c:set var="type_expenses"><fmt:message key="expenses"/></c:set>
<c:set var="type_direct"><fmt:message key="direct_costs"/></c:set>


<%-- 
Request Attributes:
  programs: 		Program list
  project:		Project to plan
  followups:		List of project Followup
  costs:			List of project detailed Costs
  iwos:			List of project IWOs
  budgetaccounts:	List of budget accounts
--%>


<script type="text/javascript">
<!--

var incomesTable;
var followupsTable;
var costsTable;
var iwosTable;
var projectPLChart;
var cbChart;
var fundingChart;

function plChart() {

  var params = {
    accion: "<%=ProjectPlanServlet.JX_PL_CHART%>",
    id: $("#id").val()
  };

  planAjax.call(params, function (data) {

    //recogemos las series
    for(var i=1;i<=6;i++){
      eval('s' + i + ' = new Array();');
      for (var j=0;j<=5;j++) {
        eval('s' + i)[j]=Number(eval('data.s'+ i +'['+ j +']'));
      }
    }

    //JQPlot parameters
    var dataSeries = [s1,s2,s3,s4,s5,s6];
    var optionsObj = {
      title:	'Project P&amp;L',
      ticks: data.categories,
      labelX: '',
      labelY:'Thousands',
      seriesColors: data.colors,
      seriesParameters: data.seriesParameters
      //animate: !$.jqplot.use_excanvas
    };

    if (typeof projectPLChart === 'undefined') {
      projectPLChart =  drawBarChart('chartPL', dataSeries, optionsObj, '${dataNotFound}');

    }
    else {
      projectPLChart.destroy();
      projectPLChart =  drawBarChart('chartPL', dataSeries, optionsObj, '${dataNotFound}');
    }
    if (data.warning) { $("#chart_pl_warning").html('${warning_pl}'); }
  });
}

function cbChart(){

  var params = {
    accion: "<%=ProjectPlanServlet.JX_PROJECT_COSTS_CHART%>",
    id: $("#id").val()
  };

  planAjax.call(params, function (data) {

    //JQPlot parameters
    var optionsObj = {
      labelX: '<fmt:message key="days"/>',
      labelY: '<fmt:message key="values"/>',
      enableCursor:true
    };

    if (typeof projectCbChart === 'undefined') {
      projectCbChart = drawLineChart('chartDivFollouwps', [data], optionsObj, '${dataNotFound}');
    }
    else {
      projectCbChart.destroy();
      projectCbChart = drawLineChart('chartDivFollouwps', [data], optionsObj, '${dataNotFound}');
    }
  });
 }

// Add Income for Finance plan
function addIncome() {
  var f = document.forms["frm_income"];
  f.reset();
  f.income_id.value = -1;
  $("#income_description").text('');

  $('#income-popup').dialog('open');
  return false;
}
function editIncome(element) {

  var income = incomesTable.fnGetData( element.parentNode.parentNode );

  var f = document.forms["frm_income"];
  f.income_id.value = income[0];
  f.income_planned_date.value = income[1];
  f.income_planned_amount.value = income[3];
  $("#income_description").text(unEscape(income[4]));

  $('#income-popup').dialog('open');
}
function deleteIncomeConfirmated(id) {
  $('#dialog-confirm').dialog("close");
  var f = document.forms["frm_project"];

  f.action = '<%=ProjectPlanServlet.REFERENCE%>';
  f.accion.value = "<%=ProjectPlanServlet.DEL_INCOME%>";
  f.income_id.value = id;
  loadingPopup();
  f.submit();
}

function deleteIncome(element) {

  var income = incomesTable.fnGetData( element.parentNode.parentNode );

  $('#dialog-confirm-msg').html('${msg_confirm_delete_income}');
  $('#dialog-confirm').dialog(
      'option',
      'buttons',
      {
        "${msg_no}": function() {
          $('#dialog-confirm').dialog("close");
        },
        "${msg_yes}": function() {
          deleteIncomeConfirmated(income[0]);
        }
      }
  );

  $('#dialog-confirm').dialog(
      'option',
      'title',
      '${msg_title_confirm_delete_income}'
  );
  $('#dialog-confirm').dialog('open');
}

function addFollowup() {
  var f = document.forms["frm_followup"];
  f.reset();
  f.followup_id.value = -1;

  $('#followup-popup').dialog('open');
  return false;
}

function editFollowup(element) {

  var followup = followupsTable.fnGetData( element.parentNode.parentNode );

  var f = document.forms["frm_followup"];
  f.followup_id.value	= followup[0];
  f.date.value		= followup[1];
  f.daysToDate.value	= followup[2];
  f.pv.value			= followup[3];

  $('#followup-popup').dialog('open');
}

// Create  dates periodically
function createFollowupDates() {

    var f = document.forms["frmFollowupCreateDates"];
    f.reset();

    $(".optionFollowup").hide();

    $('#followupCreateDates-popup').dialog('open');
}

function deleteFollowupConfirmated(id) {
  $('#dialog-confirm').dialog("close");
  var f = document.forms["frm_project"];

  f.action = '<%=ProjectPlanServlet.REFERENCE%>';
  f.accion.value = "<%=ProjectPlanServlet.DEL_FOLLOWUP%>";
  f.followup_id.value = id;
  loadingPopup();
  f.submit();
}

function deleteFollowup(element) {

  var followup = followupsTable.fnGetData( element.parentNode.parentNode );

  $('#dialog-confirm-msg').html('${msg_confirm_delete_followup}');
  $('#dialog-confirm').dialog(
      'option',
      'buttons',
      {
        "${msg_no}": function() {
          $('#dialog-confirm').dialog("close");
        },
        "${msg_yes}": function() {
          deleteFollowupConfirmated(followup[0]);
        }
      }
  );

  $('#dialog-confirm').dialog(
      'option',
      'title',
      '${msg_title_confirm_delete_followup}'
  );
  $('#dialog-confirm').dialog('open');
  return false;
}

function addCost(type, id) {

  var f = document.forms.frm_cost;
  f.reset();
  f.cost_id.value = '';

  $('#legend').text(type);
  $('#cost_cost_type').val(id);

  filterCost.filterSelect('filter');

  $('#cost-popup').dialog('open');
}
function editCost(element, type, id) {

  var cost = costsTable.fnGetData( element.parentNode.parentNode.parentNode );
  var f = document.forms.frm_cost;

  f.reset();
  f.cost_id.value			= cost[0];
  f.cost_cost_type.value	= <%=Constants.COST_TYPE_DIRECT%>;
  f.cost_planned.value	= cost[4];
  f.desc.value			= unEscape(cost[3]);

  $('#legend').text(type);
  $('#cost_cost_type').val(id);

  filterCost.filterSelect('filter', cost[6]);

  $('#cost-popup').dialog('open');
}

function editExpense(element, type, id) {

  var expense = expensesTable.fnGetData( element.parentNode.parentNode.parentNode );
  var f = document.forms["frm_cost"];

  f.reset();
  f.cost_id.value			= expense[0];
  f.cost_cost_type.value	= <%=Constants.COST_TYPE_EXPENSE%>;
  f.cost_planned.value	= expense[4];
  f.desc.value			= unEscape(expense[3]);

  $('#legend').text(type);
  $('#cost_cost_type').val(id);

  filterCost.filterSelect('filter', expense[6]);

  $('#cost-popup').dialog('open');
}

function deleteCostConfirmated(id, type) {
  $('#dialog-confirm').dialog("close");

  var f = document.forms["frm_project"];

  f.action = '<%=ProjectPlanServlet.REFERENCE%>';
  f.accion.value = "<%=ProjectPlanServlet.DEL_COST%>";
  f.cost_id.value = id;
  f.cost_type.value = type;
  loadingPopup();
  f.submit();
}

function deleteCost(element, type) {

  var tableData	= (type == '${direct_id}'?costsTable:expensesTable);
  var expense		= tableData.fnGetData( element.parentNode.parentNode.parentNode );

  $('#dialog-confirm-msg').html('${msg_confirm_delete_cost}');
  $('#dialog-confirm').dialog(
      'option',
      'buttons',
      {
        "${msg_no}": function() {
          $('#dialog-confirm').dialog("close");
        },
        "${msg_yes}": function() {
          deleteCostConfirmated(expense[0], type);
        }
      }
  );

  $('#dialog-confirm').dialog(
      'option',
      'title',
      '${msg_title_confirm_delete_cost}'
  );
  $('#dialog-confirm').dialog('open');
}

function addIWO() {
  var f = document.forms["frm_iwo"];
  f.reset();
  f.iwo_id.value = '';
  $('#iwo-popup').dialog('open');
}
function editIWO(element) {

  var iwo = iwosTable.fnGetData( element.parentNode.parentNode.parentNode );

  var f = document.forms["frm_iwo"];
  f.iwo_id.value		= iwo[0];
  f.iwo_planned.value = iwo[1];
  f.iwo_desc.value	= unEscape(iwo[2]);

  $('#iwo-popup').dialog('open');
}
function deleteIWOConfirmated(id) {
  $('#dialog-confirm').dialog("close");

  var f = document.forms["frm_project"];
  f.action = '<%=ProjectPlanServlet.REFERENCE%>';
  f.accion.value = "<%=ProjectPlanServlet.DEL_IWO%>";
  f.iwo_id.value = id;
  loadingPopup();
  f.submit();
}

function deleteIWO(element) {

  var iwo = iwosTable.fnGetData( element.parentNode.parentNode.parentNode );

  $('#dialog-confirm-msg').html('${msg_confirm_delete_iwo}');
  $('#dialog-confirm').dialog(
      'option',
      'buttons',
      {
        "${msg_no}": function() {
          $('#dialog-confirm').dialog("close");
        },
        "${msg_yes}": function() {
          deleteIWOConfirmated(iwo[0]);
        }
      }
  );

  $('#dialog-confirm').dialog(
      'option',
      'title',
      '${msg_title_confirm_delete_iwo}'
  );
  $('#dialog-confirm').dialog('open');
}

function showFundingChart() {

  var params = {
    accion: "<%=ProjectPlanServlet.JX_FINANCE_CHART%>",
    id: $("#id").val()
  };

  planAjax.call(params, function (data) {

    var optionsObj = {
      labelX: '<fmt:message key="days"/>',
      labelY: '<fmt:message key="values"/>',
      enableCursor: false,
      seriesColors: ["rgba(75, 178, 197, 1)"],
      showPointLabels: false,
      animate: !$.jqplot.use_excanvas
    };

    if (typeof fundingChart === 'undefined') {
      fundingChart = drawLineChart('chartDivIncomes', [data.values], optionsObj, '${dataNotFound}');
    }
    else {
      if (fundingChart != null) { fundingChart.destroy(); }
      fundingChart = drawLineChart('chartDivIncomes', [data.values], optionsObj, '${dataNotFound}');
    }
  });
}

function recalculatePV() {

  var params = {
    accion: "<%=ProjectPlanServlet.JX_CHECK_VIABILITY_RECALCULATE%>",
    id: $("#id").val()
  };

  planAjax.call(params, function(data) {

    $('#dialog-confirm-msg').html('<fmt:message key="msg_confirm_recalculate_pv"/>');
    $('#dialog-confirm').dialog(
        'option',
        'buttons',
        {
          "${msg_no}": function() {
            $('#dialog-confirm').dialog("close");
          },
          "${msg_yes}": function() {
            recalculatePVConfirmed();
          }
        }
    );

    $('#dialog-confirm').dialog('open');


  });

}

function recalculatePVConfirmed() {

  $('#dialog-confirm').dialog("close");

  var params = {
    accion: '<%=ProjectPlanServlet.JX_RECALCULATE_PV%>',
    id: $("#id").val()
  };

  planAjax.call(params, function(data) {

    followupsTable.fnClearTable();
    console.log(data);

    // Formatting date strings to locale and pv to currencies
    for(var i = 0; i < data.length; i++){

      var followupDate = new Date(data[i][1]);

      var dd = followupDate.getDate();
      var mm = followupDate.getMonth()+1; //January is 0!

      var yyyy = followupDate.getFullYear();

      if(dd<10){

        dd='0'+dd
      }

      if(mm<10){

        mm='0'+mm
      }

      data[i][1] = dd+'/'+mm+'/'+yyyy;
    }
    console.log(data);
    followupsTable.fnAddData(data, true);
  })

}

// When document is ready
readyMethods.add(function() {
  incomesTable = $('#tb_incomes').dataTable({
    "oLanguage": datatable_language,
    "bFilter": false,
    "bInfo": false,
    "bPaginate": true,
    "bAutoWidth": false,
    "sPaginationType": "full_numbers",
    "aaSorting": [[ 1, "asc" ]],
    "aoColumns": [
                  { "bVisible": false },
                  { "sClass": "center", "sType": "es_date" },
                  { "sClass": "center"},
                  { "sClass": "right"},
                  { "sClass": "left"},
                  { "sClass": "center", "bSortable": false }
              ]
  });
  $('#tb_incomes tbody tr').live('click', function (event) {
    incomesTable.fnSetSelectable(this,'selected_internal');
  } );

  followupsTable = $('#tb_followups').dataTable({
    "oLanguage": datatable_language,
    "bFilter": false,
    "bInfo": false,
    "bPaginate": true,
    "iDisplayLength": 50,
    "bAutoWidth": false,
    "sPaginationType": "full_numbers",
    "aaSorting": [[ 1, "asc" ]],
    "aoColumns": [
                  { "bVisible": false },
                  { "sClass": "center", "sType": "es_date"},
                  { "sClass": "center"},
                  { "sClass": "right", "sType": "numeric", "bUseRendered": false,
                    "fnRender": function ( data ) {

                      return toCurrency(data.aData[3], 2);
                  }},
                  { "sClass": "center", "bSortable": false }
              ]
  });
  $('#tb_followups tbody tr').live('click', function (event) {
    followupsTable.fnSetSelectable(this,'selected_internal');
  } );


  costsTable = $('#tb_costs').dataTable({
    "oLanguage": datatable_language,
    "bFilter": false,
    "bInfo": false,
    "bPaginate": true,
    "bAutoWidth": false,
    "sPaginationType": "full_numbers",
    "aoColumns": [
                  { "bVisible": false },
                  { "bVisible": false },
                  { "sClass": "left", "sWidth": "40%" },
                  { "sClass": "left", "sWidth": "42%" },
                  { "sClass": "right", "sWidth": "10%" },
                  { "sClass": "center", "bSortable": false, "sWidth": "8%" },
                  { "bVisible": false }
              ]
  });

  $('#tb_costs tbody tr').live('click', function (event) {
    costsTable.fnSetSelectable(this,'selected_internal');
  } );

  expensesTable = $('#tb_expenses').dataTable({
    "oLanguage": datatable_language,
    "bFilter": false,
    "bInfo": false,
    "bPaginate": true,
    "bAutoWidth": false,
    "sPaginationType": "full_numbers",
    "aoColumns": [
                  { "bVisible": false },
                  { "bVisible": false },
                  { "sClass": "left" },
                  { "sClass": "left" },
                  { "sClass": "right" },
                  { "sClass": "center", "bSortable": false },
                  { "bVisible": false }
              ]
  });
  $('#tb_expenses tbody tr').live('click', function (event) {
    expensesTable.fnSetSelectable(this,'selected_internal');
  } );

  iwosTable = $('#tb_iwos').dataTable({
    "oLanguage": datatable_language,
    "bFilter": false,
    "bInfo": false,
    "bPaginate": true,
    "bAutoWidth": false,
    "sPaginationType": "full_numbers",
    "aoColumns": [
                  { "bVisible": false },
                  { "sClass": "right" },
                  { "sClass": "left"},
                  { "sClass": "center", "bSortable": false }
              ]
  });

  $('#tb_iwos tbody tr').live('click', function (event) {
    iwosTable.fnSetSelectable(this,'selected_internal');
  } );

    $.each( $(".formatNumbers"), function() {
    number = $(this).html();
    $(this).html(formatNumber(number,"${f_separator}","${f_decimal}"));
  });

    $('#botonResetZoom').click(function() {
        if (typeof projectCbChart !== 'undefined') { projectCbChart.resetZoom(); }
    });

    createBT('.btitle');
    
    <%-- Export finance planning --%>
    $('#financeToCSV').on('click',function(event) {
    event.stopPropagation();
    var f = document.frm_project;
    f.accion.value = "<%= ProjectPlanServlet.EXPORT_FINANCE_PLANNING_CSV %>";
    f.submit();
  });

    $("#createFollowupDates").click(createFollowupDates);
});
//-->
</script>

<%-- FUNDING --%>
<fmt:message key="funding" var="titleFunding"/>
<c:set var="btnFinance"><img id="financeToCSV" src="images/csv.png" title="${msg_to_excel}"></c:set>
<visual:panel id="fieldFinancePlan" title="${titleFunding }" cssClass="panel2" buttons="${btnFinance}">
  <table id="tb_incomes" class="tabledata" cellspacing="1" width="100%">
    <thead>
      <tr>
        <th width="0%">&nbsp;</th>
        <th width="15%"><fmt:message key="funding.due_date"/></th>
        <th width="10%"><fmt:message key="funding.days_date"/></th>
        <th width="15%"><fmt:message key="funding.due_value"/></th>
        <th width="52%"><fmt:message key="income.description"/></th>
        <th width="8%">
          <c:if test="${op:hasPermission(user,project.status,tab)}">
            <img onclick="addIncome()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
          </c:if>
        </th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="income" items="${project.incomeses}">
        <tr>
          <td>${income.idIncome}</td>
          <td><fmt:formatDate value="${income.plannedBillDate}" pattern="${datePattern}"/></td>
          <td>${income.planDaysToDate}</td>
          <td>${tl:toCurrency(income.plannedBillAmmount)}</td>
          <td>${tl:escape(income.plannedDescription)}</td>
          <td>
            <c:if test="${op:hasPermission(user,project.status,tab)}">
              <img onclick="editIncome(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
              &nbsp;&nbsp;&nbsp;
              <img onclick="deleteIncome(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <fmt:message key="funding.chart" var="titleFundingChart"/>
  <visual:panel id="fieldChartDivIncomes" title="${titleFundingChart }" cssClass="panel3" callback="showFundingChart">
    <div style="padding:10px;">
      <a href="javascript:showFundingChart()" class="boton"><fmt:message key="chart.refresh" /></a>
    </div>
    <div id="chartDivIncomes" style="margin: 20px auto;"></div>
  </visual:panel>
</visual:panel>

<%-- PLANNED VALUE --%>
<fmt:message key="planned_value" var="titlePlanedValue"/>
<visual:panel id="fieldBaseLinePlan" title="${titlePlanedValue }" cssClass="panel2">
  <table id="tb_followups" class="tabledata" cellspacing="1" width="100%">
    <thead>
      <tr>
        <th width="0%">&nbsp;</th>
        <th width="35%"><fmt:message key="followup.date"/></th>
        <th width="30%"><fmt:message key="planned_value.days_date"/></th>
        <th width="27%"><fmt:message key="planned_value.value"/></th>
        <th width="8%">
          <c:if test="${op:hasPermission(user,project.status,tab)}">
            <img onclick="addFollowup()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
            &nbsp;
            <img id="createFollowupDates" title="<fmt:message key="followup.periodicity"/>" class="link" src="images/calendar.png">
            &nbsp;
            <img src="images/panel/reload.png" title="<fmt:message key="followup.recalculate_pv"/>" onclick="recalculatePV()" class="link">
          </c:if>
        </th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="followup" items="${followups}">
        <tr>
          <td>${followup.idProjectFollowup}</td>
          <td><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}"/></td>
          <td>${followup.daysToDate}</td>
          <td>${followup.pv}</td>
          <td>
            <c:if test="${op:hasPermission(user,project.status,tab)}">
              <img onclick="editFollowup(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
              &nbsp;&nbsp;&nbsp;
              <img onclick="deleteFollowup(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
  <fmt:message key="planned_value.chart" var="titlePlannedValueChart"/>
  <visual:panel id="fieldPlannedValueChart" title="${titlePlannedValueChart }" cssClass="panel3" callback="cbChart">
    <div style="padding:10px;">
      <a href="javascript:cbChart();" class="boton"><fmt:message key="chart.refresh" /></a>
      <input id="botonResetZoom" type="button" class="boton" value="<fmt:message key="chart.reset_zoom" />"/>
      <img src="images/info.png" title="<fmt:message key="chart.reset_zoom.info"/>" class="btitle">
    </div>
    <div id="chartDivFollouwps" style="margin: 20px auto;"></div>
  </visual:panel>
</visual:panel>

<%-- COST DETAIL PLAN --%>

<fmt:message key="cost_detail_plan" var="titleCostDetailPlan"/>
<visual:panel id="fieldCostsPlan" title="${titleCostDetailPlan }" cssClass="panel2">
  <%-- EXPENSES --%>
  <div class="hColor"><fmt:message key="expenses"/></div>
  <table id="tb_expenses" class="tabledata" cellspacing="1" width="100%">
    <thead>
      <tr>
        <th width="0%">&nbsp;</th>
        <th width="0%">&nbsp;</th>
        <th width="40%"><fmt:message key="cost.charge_account"/></th>
        <th width="42%"><fmt:message key="cost.desc"/></th>
        <th width="10%"><fmt:message key="cost.value"/></th>
        <th width="8%">
          <c:if test="${op:hasPermission(user,project.status,tab)}">
            <img onclick="addCost('${type_expenses}', '${expense_id}')" title="<fmt:message key="add"/>" class="link" src="images/add.png">
          </c:if>
        </th>
        <th width="0%">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="cost" items="${costs}">
        <c:forEach var="expense" items="${cost.expenseses }">
          <tr>
            <td>${expense.idExpense}</td>
            <td>${expense_id }</td>
            <td>${tl:escape(expense.budgetaccounts.description) }</td>
            <td>${tl:escape(expense.description )}</td>
            <td>${tl:toCurrency(expense.planned )}</td>
            <td>
              <nobr>
                <img onclick="editExpense(this,'${type_expenses}', '${expense_id}')" title="<fmt:message key="view"/>" class="link" src="images/view.png">
                <c:if test="${op:hasPermission(user,project.status,tab)}">
                  &nbsp;&nbsp;&nbsp;
                  <img onclick="deleteCost(this, ${expense_id })" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
                </c:if>
              </nobr>
            </td>
            <td>${expense.budgetaccounts.idBudgetAccount }</td>
          </tr>
        </c:forEach>
      </c:forEach>
    </tbody>
  </table>

  <%-- DIRECT COSTS --%>
  <div class="hColor"><fmt:message key="direct_costs"/></div>
  <table id="tb_costs" class="tabledata" cellspacing="1" width="100%">
    <thead>
      <tr>
        <th >&nbsp;</th>
        <th >&nbsp;</th>
        <th ><fmt:message key="cost.charge_account"/></th>
        <th ><fmt:message key="cost.desc"/></th>
        <th ><fmt:message key="cost.value"/></th>
        <th >
          <c:if test="${op:hasPermission(user,project.status,tab)}">
            <img onclick="addCost('${type_direct}', '${direct_id}')" title="<fmt:message key="add"/>" class="link" src="images/add.png">
          </c:if>
        </th>
        <th width="0%">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="cost" items="${costs}">
        <c:forEach var="direct" items="${cost.directcostses }">
          <tr>
            <td>${direct.idDirectCosts}</td>
            <td>${direct_id }</td>
            <td>${tl:escape(direct.budgetaccounts.description) }</td>
            <td>${tl:escape(direct.description )}</td>
            <td>${tl:toCurrency(direct.planned )}</td>
            <td>
              <nobr>
                <img onclick="editCost(this,'${type_direct}', '${direct_id}')" title="<fmt:message key="view"/>" class="link" src="images/view.png">
                <c:if test="${op:hasPermission(user,project.status,tab)}">
                  &nbsp;&nbsp;&nbsp;
                  <img onclick="deleteCost(this, ${direct_id })" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
                </c:if>
              </nobr>
            </td>
            <td>${direct.budgetaccounts.idBudgetAccount }</td>
          </tr>
        </c:forEach>
      </c:forEach>
    </tbody>
  </table>

  <%-- RESERVES --%>
  <div class="hColor"><fmt:message key="reserves"/></div>
  <table id="tb_iwos" class="tabledata" cellspacing="1" width="100%">
    <thead>
      <tr>
        <th width="0%"><fmt:message key="iwo"/></th>
        <th width="10%"><fmt:message key="iwo.value"/></th>
        <th width="82%"><fmt:message key="iwo.desc"/></th>
        <th width="8%">
          <c:if test="${op:hasPermission(user,project.status,tab)}">
            <img onclick="addIWO()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
          </c:if>
        </th>
      </tr>
    </thead>

    <tbody>
      <c:forEach var="iwo" items="${iwos}">
        <tr>
          <td>${iwo.idIwo}</td>
          <td>${tl:toCurrency(iwo.planned)}</td>
          <td>${tl:escape(iwo.description)}</td>
          <td>
            <c:if test="${op:hasPermission(user,project.status,tab)}">
              <nobr>
                <img onclick="editIWO(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
                &nbsp;&nbsp;&nbsp;
                <img onclick="deleteIWO(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
              </nobr>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <%-- CHART --%>
  <fmt:message key="finance_chart" var="titleFinanceChart"/>
  <visual:panel id="chartPL_block" title="${titleFinanceChart }" cssClass="panel3" callback="plChart">
    <div style="padding:10px;">
      <a href="javascript:plChart();" class="boton" id="botonRefresco"><fmt:message key="chart.refresh" /></a>
    </div>
    <div id="chartPL" style="margin: 20px auto;"></div>
    <div id="chart_pl_warning" align="center"></div>
  </visual:panel>
</visual:panel>