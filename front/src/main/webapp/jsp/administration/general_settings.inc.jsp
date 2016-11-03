<%@page import="es.sm2.openppm.core.logic.setting.GeneralSetting"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@page import="es.sm2.openppm.core.common.Settings.SettingType"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page import="es.sm2.openppm.core.logic.security.actions.AdminAction" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSettingAuthorization" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSettingTimeAndCosts" %>
<%@ page import="es.sm2.openppm.core.reports.annotations.ReportExtension" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: general_settings.inc.jsp
  ~ Create User: javier.hernandez  
  ~ Create Date: 15/03/2015 12:48:26
  --%>

<%-- Messages confirmation --%>
<fmt:message key="msg.title_confirm_update_poc" var="msg_title_confirm_update_poc"/>
<fmt:message key="msg.confirm_update_poc" var="msg_confirm_update_poc"/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>

    var adminAjax = new AjaxCall('<%=AdministrationServlet.REFERENCE%>','<fmt:message key="error"/>');

    var generalValidator;

    readyMethods.add(function() {

        generalValidator = $("#frm_general_settings").validate({
            errorContainer: 'div#general-errors',
            errorLabelContainer: 'div#general-errors ol',
            wrapper: 'li',
            showErrors: function(errorMap, errorList) {
                $('span#general-numerrors').html(this.numberOfInvalids());
                this.defaultShowErrors();
            }
        });

        $('#saveGeneral').on('click', function() {

            if (generalValidator.form()) {
                loadingPopup();
                document.forms.frm_general_settings.submit();
            }
        });

        var optsSpinnerDays = {
            decimals:0,
            min:0,
            max:365
        };

        $('#spinnerStatusReportDays').spinner(optsSpinnerDays);

        // Insert button recalculate
        //
        var f = document.frm_general_settings;

        var tdInsertButton = f.<%= GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET.getName() %>.parentNode;

        if (typeof tdInsertButton !== 'undefined') {
            $(tdInsertButton).append('<input type="button" class="boton" id="<%= GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET.getName() %>Button" value="<fmt:message key="update_poc_projects" />" title="<fmt:message key="update_poc_projects.info" />"/>');
        }

        // Event
        $('#<%= GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET.getName() %>Button').on('click', function() {

            confirmUI(
                    '${msg_title_confirm_update_poc}','${msg_confirm_update_poc}',
                    '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                    function() {

                        var checkBox = f.<%= GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET.getName() %>;

                        // Update POC Projects
                        //
                        var params = {
                            accion: "<%= AdminAction.JX_UPDATE_POC_PROJECTS %>",
                            exclude: $(checkBox).is(':checked')
                        };

                        // Call
                        adminAjax.call(params);
                    });
        });

    // Event migration documents
    $('#migrationDocuments').on('click', function() {

        document.forms.frm_general_settings.accion.value = "<%=AdminAction.MIGRATION_DOCUMENTS.getAction() %>";

        loadingPopup();

        // Submit
        document.forms.frm_general_settings.submit();

    });
    });
</script>
<fmt:message key="general" var="titleGeneral"/>
<visual:panel title="${titleGeneral}">
<form name="frm_general_settings" id="frm_general_settings" action="<%=AdministrationServlet.REFERENCE%>" method="post">
<input type="hidden" name="accion" value="<%=AdministrationServlet.SAVE_GENERAL_SETTINGS%>" />

<div id="general-errors" class="ui-state-error ui-corner-all hide">
    <p>
        <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
        <strong><fmt:message key="msg.error_title"/></strong>
        &nbsp;(<b><span id="general-numerrors"></span></b>)
    </p>
    <ol></ol>
</div>
<fmt:message key="authorization" var="titleAuthorization"/>
<visual:panel title="${titleAuthorization }">
    <table width="100%" cellpadding="5px" class="settingTable">
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.last_level_for_approve_sheet_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET %>"/></th>
            <td>
                <select name="<%=Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET %>" class="campo" style="width:100%;">
                    <option value="" selected><fmt:message key="select_opt"/></option>
                    <option value="<%= Constants.ROLE_PM%>" <%=SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PM)) ? "selected" : ""%>>
                        <fmt:message key="project_manager" />
                    </option>
                    <option value="<%= Constants.ROLE_FM%>" <%=SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_FM)) ? "selected" : ""%>>
                        <fmt:message key="functional_manager" />
                    </option>
                    <option value="<%= Constants.ROLE_PMO%>" <%=SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PMO)) ? "selected" : ""%>>
                        <fmt:message key="pmo" />
                    </option>
                </select>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.rm_approve_operation_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION%>"/></th>
            <td>
                <select name="<%=Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION %>" class="campo" style="width:100%;">
                    <option value="" selected><fmt:message key="select_opt"/></option>
                    <option value="<%= Constants.TIMESTATUS_APP0%>" <%=SettingUtil.getString(settings, Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION, Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION).equals(Constants.TIMESTATUS_APP0) ? "selected" : ""%>>
                        <fmt:message key="applevel.app0" />
                    </option>
                    <option value="<%= Constants.TIMESTATUS_APP3%>" <%=SettingUtil.getString(settings, Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION, Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION).equals(Constants.TIMESTATUS_APP3) ? "selected" : ""%>>
                        <fmt:message key="applevel.app3" />
                    </option>
                </select>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.type_approbation_resource_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.type_approbation_resource"/></th>
            <td>
                <select name="<%=Settings.SETTING_TYPE_APPROBATION_RESOURCE%>" class="campo" style="width:100%;">
                    <option value="" selected><fmt:message key="select_opt"/></option>
                    <option value="<%=Constants.TYPE_APPROBATION_RM%>" <%=SettingUtil.getString(settings, Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT).equals(String.valueOf(Constants.TYPE_APPROBATION_RM)) ? "selected" : ""%>>
                        <fmt:message key="setting.type_approbation_rm" />
                    </option>
                    <option value="<%=Constants.TYPE_APPROBATION_AUTOMATIC%>" <%=SettingUtil.getString(settings, Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT).equals(String.valueOf(Constants.TYPE_APPROBATION_AUTOMATIC)) ? "selected" : ""%>>
                        <fmt:message key="setting.type_approbation_automatic" />
                    </option>
                    <option value="<%=Constants.TYPE_APPROBATION_AUTOMATIC_PM%>" <%=SettingUtil.getString(settings, Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT).equals(String.valueOf(Constants.TYPE_APPROBATION_AUTOMATIC_PM)) ? "selected" : ""%>>
                        <fmt:message key="setting.type_approbation_automatic_pm" />
                    </option>
                </select>
            </td>

            <td>&nbsp;</td>

            <visual:settings startColumn="2" numberOfColumns="2" fields="<%=GeneralSettingAuthorization.values()%>"></visual:settings>
        </tr>
    </table>
</visual:panel>
<fmt:message key="timeAndCost" var="titleTimeAndCost"/>
<visual:panel title="${titleTimeAndCost }">
    <table width="100%" cellpadding="5px" class="settingTable">
        <tr>
            <td><img class="btitle" title="<fmt:message key="<%= SettingType.BASELINE_DATES.getMessageInfo() %>"/>" src="images/info.png"></td>
            <th><fmt:message key="<%= SettingType.BASELINE_DATES.getMessage() %>"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%= SettingType.BASELINE_DATES.getName() %>" value="true"
                        <%=SettingUtil.getString(settings, SettingType.BASELINE_DATES.getName(), SettingType.BASELINE_DATES.getDefaultValue()).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="<%= SettingType.PRELOAD_HOURS_WITH_WORKLOAD.getMessageInfo() %>"/>" src="images/info.png"></td>
            <th><fmt:message key="<%= SettingType.PRELOAD_HOURS_WITH_WORKLOAD.getMessage() %>"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%= SettingType.PRELOAD_HOURS_WITH_WORKLOAD.getName() %>" value="true"
                        <%=SettingUtil.getString(settings, SettingType.PRELOAD_HOURS_WITH_WORKLOAD.getName(), SettingType.PRELOAD_HOURS_WITH_WORKLOAD.getDefaultValue()).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.time.session_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_TIME_SESSION %>"/></th>
            <td><input maxlength="200" type="text" class="campo integer" name="<%=Settings.SETTING_TIME_SESSION%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_TIME_SESSION, Settings.DEFAULT_TIME_SESSION) %>" /></td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.time.session.advise_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_TIME_SESSION_ADVISE %>"/></th>
            <td><input maxlength="200" type="text" class="campo integer" name="<%=Settings.SETTING_TIME_SESSION_ADVISE%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_TIME_SESSION_ADVISE, Settings.DEFAULT_TIME_SESSION_ADVISE) %>" /></td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.workingcosts.internal_costs_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_WORKINGCOSTS_INTERNAL_COST%>"/></th>
            <td><input maxlength="200" type="text" class="campo integer" name="<%=Settings.SETTING_WORKINGCOSTS_INTERNAL_COST%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_WORKINGCOSTS_INTERNAL_COST, Settings.DEFAULT_WORKINGCOSTS_INTERNAL_COST) %>" /></td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.exclude_hours_resource_with_provider_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.exclude_hours_resource_with_provider"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER, Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.calculate_evm_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.calculate_evm"/></th>
            <td>
                <select name="<%=Settings.SETTING_CALCULATE_EVM%>" class="campo" style="width:100%;">
                    <option value="<%=Constants.CALCULATE_EVM_HOURS_COST%>" <%=SettingUtil.getString(settings, Settings.SETTING_CALCULATE_EVM, Settings.SETTING_CALCULATE_EVM_DEFAULT).equals(String.valueOf(Constants.CALCULATE_EVM_HOURS_COST)) ? "selected" : ""%>>
                        <fmt:message key="setting.calculate.evm_hours_cost" />
                    </option>
                    <option value="<%=Constants.CALCULATE_EVM_EXTERNAL_TM%>" <%=SettingUtil.getString(settings, Settings.SETTING_CALCULATE_EVM, Settings.SETTING_CALCULATE_EVM_DEFAULT).equals(String.valueOf(Constants.CALCULATE_EVM_EXTERNAL_TM)) ? "selected" : ""%>>
                        <fmt:message key="setting.calculate.evm_external_tm" />
                    </option>
                </select>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.bac_for_evm_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.bac_for_evm"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%= Settings.SETTING_USE_BAC_PLANNING %>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_USE_BAC_PLANNING, Settings.SETTING_USE_BAC_PLANNING_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <visual:settings startColumn="1" numberOfColumns="2" fields="<%=GeneralSettingTimeAndCosts.values()%>"></visual:settings>
        </tr>

    </table>
</visual:panel>
<fmt:message key="reports" var="titleReports"/>
<visual:panel title="${titleReports }">
    <table width="100%" cellpadding="5px" class="settingTable">
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.days_status_report_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_DAYS_STATUS_REPORT %>"/></th>
            <td>
                <c:set var="statusReportDays"><%= SettingUtil.getString(settings, Settings.SETTING_DAYS_STATUS_REPORT, Settings.DEFAULT_SETTING_DAYS_STATUS_REPORT) %></c:set>
                <div style="position:relative;float:left;margin-right: 2px;"><fmt:message key="until"/>&nbsp;</div>
                <div style="float:left;">
                    <input type="text" class="campo" maxlength="3" id="spinnerStatusReportDays" name="<%=Settings.SETTING_DAYS_STATUS_REPORT%>" value="${statusReportDays}" />
                </div>
                <div style="position:relative;float:left"> <fmt:message key="days"/></div>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.status_report_order_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_STATUS_REPORT_ORDER %>"/></th>
            <td>
                <select name="<%=Settings.SETTING_STATUS_REPORT_ORDER %>" class="campo" style="width:100%;">
                    <option value="" selected><fmt:message key="select_opt"/></option>
                    <option value="<%= Constants.ASCENDENT%>" <%=SettingUtil.getString(settings, Settings.SETTING_STATUS_REPORT_ORDER, Settings.DEFAULT_STATUS_REPORT_ORDER).equals(String.valueOf(Constants.ASCENDENT)) ? "selected" : ""%>>
                        <fmt:message key="order.asc" />
                    </option>
                    <option value="<%= Constants.DESCENDENT%>" <%=SettingUtil.getString(settings, Settings.SETTING_STATUS_REPORT_ORDER, Settings.DEFAULT_STATUS_REPORT_ORDER).equals(String.valueOf(Constants.DESCENDENT)) ? "selected" : "" %>>
                        <fmt:message key="order.desc" />
                    </option>
                </select>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.show_executivereport_in_status_report_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.show_executivereport_in_status_report"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT, Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.show_statusreport_in_investment_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.show_statusreport_in_investment"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
    </table>
</visual:panel>
<fmt:message key="messages" var="titleMsg"/>
<visual:panel title="${titleMsg}">
    <table width="100%" cellpadding="5px" class="settingTable">
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.project.exceeded_budget_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_PROJECT_EXCEEDED_BUDGET %>"/></th>
            <td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_EXCEEDED_BUDGET %>" value="true"
                    <%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_EXCEEDED_BUDGET, Settings.DEFAULT_PROJECT_EXCEEDED_BUDGET).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.colummn.csv.idproject_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_CSV_COLUMN_IDPROJECT %>"/></th>
            <td><input type="checkbox" class="campo" name="<%=Settings.SETTING_CSV_COLUMN_IDPROJECT%>" value="true"
                    <%=SettingUtil.getString(settings, Settings.SETTING_CSV_COLUMN_IDPROJECT, Settings.DEFAULT_CSV_COLUMN_IDPROJECT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.show_inactivated_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.show_inactivated"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_SHOW_INACTIVATED%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_SHOW_INACTIVATED, Settings.SETTING_SHOW_INACTIVATED_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.show_status_for_resource_role_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.show_status_for_resource_role"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="<%= SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING.getMessageInfo() %>"/>" src="images/info.png"></td>
            <th><fmt:message key="<%= SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING.getMessage() %>"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%= SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING.getName() %>" value="true"
                        <%=SettingUtil.getString(settings, SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING.getName(), SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING.getDefaultValue()).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
    </table>
</visual:panel>
<fmt:message key="others" var="titleOthers"/>
<visual:panel title="${titleOthers }">
    <table width="100%" cellpadding="5px" class="settingTable">
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.project.document.storage_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_PROJECT_DOCUMENT_STORAGE%>"/></th>
            <td>
                <select name="<%=Settings.SETTING_PROJECT_DOCUMENT_STORAGE %>" class="campo" style="width:100%;">
                    <% for (DocumentType documentType : DocumentType.values()) { %>
                    <option value="<%= documentType.getName() %>" <%=SettingUtil.getString(settings, SettingType.DOCUMENT_STORAGE).equals(String.valueOf(documentType.getName())) ? "selected" : ""%>>
                        <fmt:message key="<%= documentType.getMessageKey() %>" />
                    </option>
                    <% } %>
                </select>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.project.document.folder_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.project.document.folder"/></th>
            <td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_PROJECT_DOCUMENT_FOLDER%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER) %>" /></td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.workingcosts.departments_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_WORKINGCOSTS_DEPARTMENTS%>"/></th>
            <td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_WORKINGCOSTS_DEPARTMENTS%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_WORKINGCOSTS_DEPARTMENTS, Settings.DEFAULT_WORKINGCOSTS_DEPARTMENTS) %>" /></td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.project.probability_info"/>" src="images/info.png"></td>
            <th><fmt:message key="<%=Settings.SETTING_PROJECT_PROBABILITY %>"/></th>
            <td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_PROJECT_PROBABILITY%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY) %>" /></td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.validate.duplicate_chart_label_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.validate.duplicate_chart_label"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td width="3%"><img class="btitle" title="<fmt:message key="setting.project.disable_info"/>" src="images/info.png"></td>
            <th width="25%"><fmt:message key="setting.project.disable"/></th>
            <td width="20%">
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_DISABLE_PROJECT%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_DISABLE_PROJECT, Settings.DEFAULT_DISABLE_PROJECT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.disable.edition_initiating_pm_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.disable.edition_initiating_pm"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_DISABLE_EDITION_INITIATING_PM%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_DISABLE_EDITION_INITIATING_PM, Settings.DEFAULT_DISABLE_EDITION_INITIATING_PM).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.project.close_change_priority_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.project.close_change_priority"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="setting.create_followup_with_project_creation_info"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.create_followup_with_project_creation"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%=Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION, Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>

            <td>&nbsp;</td>

            <td><img class="btitle" title="<fmt:message key="setting.project.exclude_investments_rejected_into_closed_projects"/>" src="images/info.png"></td>
            <th><fmt:message key="setting.project.exclude_investments_rejected_into_closed_projects"/></th>
            <td>
                <input type="checkbox" class="campo" name="<%= Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS%>" value="true"
                        <%=SettingUtil.getString(settings, Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS, Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS_DEFAULT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
            </td>
        </tr>
        <tr>
            <td><img class="btitle" title="<fmt:message key="<%= SettingType.REPORT_EXTENSION.getMessageInfo() %>"/>" src="images/info.png"></td>
            <th><fmt:message key="<%= SettingType.REPORT_EXTENSION.getMessage() %>"/></th>
            <td>
                <select name="<%= SettingType.REPORT_EXTENSION %>" class="campo" style="width:100%;">
                    <option value="<%= ReportExtension.ODT %>" <%= SettingUtil.getString(settings, SettingType.REPORT_EXTENSION).equals(ReportExtension.ODT.name()) ? "selected" : ""%>>
                       <%= ReportExtension.ODT.data()%>
                    </option>
                    <option value="<%= ReportExtension.DOC %>" <%= SettingUtil.getString(settings, SettingType.REPORT_EXTENSION).equals(ReportExtension.DOC.name()) ? "selected" : ""%>>
                        <%= ReportExtension.DOC.data()%>
                    </option>
                </select>
            </td>
        </tr>

        <%-- Documents migration --%>
        <c:if test="<%= SettingUtil.getBoolean(settings, SettingType.MIGRATION_DOCUMENTS) %>">
            <tr>
                <td><img class="btitle" title="<fmt:message key="<%= SettingType.MIGRATION_DOCUMENTS.getMessageInfo() %>"/>" src="images/info.png"></td>
                <td>
                    <input type="button" class="boton" id="migrationDocuments" value="<fmt:message key="<%= SettingType.MIGRATION_DOCUMENTS.getMessage() %>" />" />
                </td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
        </c:if>
    </table>
</visual:panel>

<div align="right" style="margin:10px;">
    <input type="button" class="boton" id="saveGeneral" value="<fmt:message key="save" />"/>
</div>
</form>
</visual:panel>