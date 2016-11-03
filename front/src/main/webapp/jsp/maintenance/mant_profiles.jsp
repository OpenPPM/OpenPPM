<%@page import="es.sm2.openppm.api.model.enums.MaintenanceEnum"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
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
  ~ File: mant_profiles.jsp
  ~ Create User: jordi.ripoll
  ~ Create Date: 01/08/2015 12:48:08
  --%>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_profile">
    <fmt:param><fmt:message key="profile"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_profile">
    <fmt:param><fmt:message key="profile"/></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
    // GLOBAL VARS
    var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');
    var profileTable;

    // OBJECTS
    var profile = {
        createTable: function() {

            return $('#profileTable').dataTable({
                "sPaginationType": "full_numbers",
                "oLanguage": datatable_language,
                "bInfo": false,
                "fnRowCallback": function( nRow, aData) {
                    $('td:eq(2)', nRow).html( profile.buttons(aData[4]) );
                    return nRow;
                },
                "aoColumns": [
                    { "bVisible": false },
                    { "sClass": "left" },
                    { "sClass": "left" },
                    { "sClass": "center", "bSortable": false }
                ]
            });
        },
        add: function() {

            profilePopup.formReset(document.profilePopupForm);

            profilePopup.open();
        },
        edit: function(element) {

            var row = profileTable.fnGetData(element.parentNode.parentNode.parentNode);

            var f = document.profilePopupForm;

            profilePopup.formReset(f);

            f.id.value 			= row[0];
            f.name.value        = unEscape(row[1]);
            f.description.value = unEscape(row[2]);

            profilePopup.open();
        },
        save: function() {

            var f = document.profilePopupForm;

            var params = {
                code: f.id.value,
                name: f.name.value,
                description: f.description.value
            };

            $.ajax({
                url: 'rest/maintenance/' + '<%= MaintenanceEnum.PROFILE.name() %>',
                type: f.id.value === '' ? 'POST' : 'PUT',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(params),
                success: function (data) {

                    var dataRow = [
                        data.code,
                        escape(data.name != null != null ? data.name : ''),
                        escape(data.description != null != null ? data.description : ''),
                        ''
                    ];

                    if (f.id.value == data.code) {
                        profileTable.fnUpdateAndSelect(dataRow);
                    }
                    else {
                        profileTable.fnAddDataAndSelect(dataRow);
                    }

                    profilePopup.close();
                }
            });
        },
        del: function(element) {
            confirmUI(
                '${msg_title_confirm_delete_profile}','${msg_confirm_delete_profile}',
                '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                function() {

                    var row = profileTable.fnGetData(element.parentNode.parentNode.parentNode);

                    $.ajax({
                        url: 'rest/maintenance/' + '<%= MaintenanceEnum.PROFILE.name() %>' + '/' + row[0],
                        type: 'DELETE',
                        dataType: 'json',
                        contentType: 'application/json',
                        data: {},
                        success: function () {
                            profileTable.fnDeleteSelected();
                        },
                        error: function (jqXHR) {

                            if (typeof jqXHR.responseText !== 'undefined' && jqXHR.responseText.length > 0) {
                                alertUI("Error", JSON.parse(jqXHR.responseText).message);
                            }
                        }
                    });
                });
        },
        buttons: function(disable) {
            var $buttons = $('<nobr/>');
            $buttons.append($('<img/>', {onclick: 'profile.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
                    .append('&nbsp;&nbsp;&nbsp;')
                    .append($('<img/>', {onclick: 'profile.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
                    .append('&nbsp;&nbsp;&nbsp;');
            return $buttons;
        },
        loadList : function() {

            $.ajax({
                url: 'rest/maintenance/' + '<%= MaintenanceEnum.PROFILE.name() %>',
                type: 'GET',
                dataType: 'json',
                contentType: 'application/json',
                data: {},
                success: function (data) {

                    // Clear table
                    profileTable.fnClearTable();

                    // Add list
                    $(data).each(function() {

                        var row = [
                            this.code,
                            escape(this.name != null != null ? this.name : ''),
                            escape(this.description != null != null ? this.description : ''),
                            ''
                        ];

                        // Add row
                        profileTable.fnAddData(row);
                    });
                }
            });
        }
    };

    readyMethods.add(function () {

        // Create table
        profileTable = profile.createTable();

        // Events
        $('#profileTable tbody tr').live('click',  function (event) {
            profileTable.fnSetSelectable(this,'selected_internal');
        });

        // Load list
        profile.loadList();
    });

</script>

<fieldset>
    <legend><fmt:message key="maintenance.profiles" /></legend>

    <%-- Profile table --%>
    <table id="profileTable" class="tabledata" width="100%">
        <thead>
        <tr>
            <th width="0%"></th>
            <th width="20%"><fmt:message key="name" /></th>
            <th width="70%"><fmt:message key="description" /></th>
            <th width="10%">
                <img onclick="profile.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
            </th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</fieldset>

<div class="spacer"></div>

<jsp:include page="popups/profile_popup.jsp" flush="true" />
