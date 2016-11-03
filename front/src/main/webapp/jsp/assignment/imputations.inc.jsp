<%@ page import="es.sm2.openppm.front.servlets.AssignmentServlet" %>
<%@ page import="es.sm2.openppm.core.model.impl.Timesheet" %>
<%@ page import="es.sm2.openppm.core.model.wrap.ImputationWrap" %>
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
  ~ File: imputations.inc.jsp
  ~ Create User: jordi.ripoll
  ~ Create Date: 27/04/2015 10:25:15
  --%>

<%--
  Created by IntelliJ IDEA.
  User: jordi.ripoll
  Date: 27/04/2015
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Print messages --%>
<fmt:message key="print" var="print" />
<fmt:message key="print_message" var="printMessage" />
<fmt:message key="print_title" var="printTitle" />

<script type="text/javascript">

    var imputationsTable;

    var imputations = {

        resetFilter: function() {
            $("#imputationDateSince").val($("#dateIn").val());
            $("#imputationDateUntil").val($("#dateOut").val());
        },
        search: function() {

            var params = {
                accion: '<%= AssignmentServlet.JX_VIEW_IMPUTATIONS %>',
                '<%= Timesheet.INITDATE %>': $("#imputationDateSince").val(),
                '<%= Timesheet.ENDDATE %>': $("#imputationDateUntil").val()
            };

            // Reset
            imputationsTable.fnClearTable();
            $('#legendImputations').empty();

            // AJAX call
            assignmentAjax.call(params,function(data) {

               if (data.length > 0) {

                   var sumHours = 0.0;

                   // Add rows
                   $(data).each(function() {

                       var row = [
                           escape(this.<%= ImputationWrap.PROJECTNAME %> != null ? this.<%= ImputationWrap.PROJECTNAME %> : this.<%= ImputationWrap.OPERATIONNAME %>),
                           escape(this.<%= ImputationWrap.PROJECTNAME %> != null ? this.<%= ImputationWrap.SHORTNAME %> : ''),
                           toCurrency(this.<%= ImputationWrap.HOURSAPP3 %>)
                       ];

                       // Add row
                       imputationsTable.fnAddData(row);

                       // Sum hours
                       sumHours = sumHours + parseFloat(this.<%= ImputationWrap.HOURSAPP3 %>);
                   });

                   // Set sumhours
                   $("#sumHours").text(toCurrency(sumHours));

                   // Add class operation
                   $("#tb_imputations tr").each(function() {

                       var tdList = this.childNodes;

                       var childTD = tdList[1];

                       // convert dom element to jquery element
                       var jchildTD = $(childTD);

                       if (jchildTD.text() === "") {
                           $(tdList).addClass("operations");
                       }
                   });

                   createLegend('#legendImputations','<fmt:message key="project" />','#FFFFFF');
                   createLegend('#legendImputations','<fmt:message key="operation" />','#41C159');
               }
               else {
                   $("#sumHours").text("");
               }
            });
        },
        exportCsv : function () {

            var f = document.forms.frmImputations;
            f.accion.value = "<%= AssignmentServlet.EXPORT_IMPUTATIONS %>";
            f.submit();
        }
    }

    readyMethods.add(function() {

        // Init datepickers
        $('#imputationDateSince, #imputationDateUntil').datepicker({
            dateFormat: '${datePickerPattern}',
            firstDay: 1,
            showOn: 'button',
            buttonImage: 'images/calendario.png',
            buttonImageOnly: true,
            numberOfMonths: ${numberOfMonths},
            changeMonth: true,
            onSelect: function(selectedDate) {}
        });

        // Create table
        imputationsTable = $('#tb_imputations').dataTable({
            "sPaginationType": "full_numbers",
            "oLanguage": datatable_language,
            "bInfo": false,
            "aaSorting": [],
            "aoColumns": [
                { "sClass": "left" },
                { "sClass": "left" },
                { "sClass": "right" }
            ],
            "bLengthChange": false,
            "bPaginate": false
        });

        // Reset event
        $("#resetFiltersImputations").click(function() {
            imputations.resetFilter();
        });

        // Search event
        $("#searchImputations").click(function() {
            imputations.search();
        });

        // Search by submit
        imputations.search();

        $('#imputationsToCsv').on('click',function(e) {
            e.stopPropagation();
            imputations.exportCsv();
        });
    });

</script>

<form id="frmImputations" name="frmImputations" method="post" action="<%= AssignmentServlet.REFERENCE%>">
    <input type="hidden" name="accion" value=""/>

    <fieldset style="margin-bottom: 15px; padding-top: 10px; padding-bottom: 5px;">
        <div class="hColor"><fmt:message key="filter"/></div>
        <div style="padding-top:10px;margin-bottom: 10px;float: left;">
            <%-- Filters dates --%>
        <span style="margin-right:5px;">
            <fmt:message key="dates.since"/>:&nbsp;
            <input type="text" id="imputationDateSince" name="imputationDateSince" class="campo fecha alwaysEditable" value="<fmt:formatDate value="${dateIn}" pattern="${datePattern}"/>"/>
        </span>
        <span style="margin-right:5px;">
            <fmt:message key="dates.until"/>:&nbsp;
            <input type="text" id="imputationDateUntil" name="imputationDateUntil" class="campo fecha alwaysEditable" value="<fmt:formatDate value="${dateOut}" pattern="${datePattern}"/>"/>
        </span>
        </div>
        <%-- Buttons --%>
        <div class="popButtons">
            <a id="searchImputations" class="boton"><fmt:message key="search"/></a>
            <a id="resetFiltersImputations" class="boton"><fmt:message key="filter.default"/></a>
        </div>
    </fieldset>

    <div>
        <table id="tb_imputations" class="tabledata" cellspacing="1" style="width:100%">
            <thead>
            <tr>
                <th width="70%"><fmt:message key="project"/>/<fmt:message key="operation"/></th>
                <th width="20%"><fmt:message key="project.chart_label"/></th>
                <th width="10%"><fmt:message key="hours"/> <fmt:message key="applevel.app3"/></th>
            </tr>
            </thead>
            <tbody></tbody>
            <tfoot>
                <tr class="total" style="background:lightgray;" >
                    <td><fmt:message key="total"/></td>
                    <td>&nbsp;&nbsp;</td>
                    <td class="right" id="sumHours"></td>
                </tr>
            </tfoot>
        </table>
        <div>&nbsp;</div>
        <div id="legendImputations" class="legendChart" style="float: left;"></div>
    </div>
</form>