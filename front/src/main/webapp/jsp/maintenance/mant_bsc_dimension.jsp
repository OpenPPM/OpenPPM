<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: mant_bscdimension.jsp
  ~ Create User: claudia.beatriz
  ~ Date: 20/05/2015 11:34
  --%>

<%-- Message --%>
<fmt:message key="maintenance.bscdimension.new" var="new_bscdimension_title" />
<fmt:message key="maintenance.bscdimension.edit" var="edit_bscdimension_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_bsc_dimension">
    <fmt:param><fmt:message key="metric.bsc_dimension"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_bsc_dimension">
    <fmt:param><fmt:message key="metric.bsc_dimension"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtBscDimensionNameRequired">
    <fmt:param><b><fmt:message key="metric.name"/></b></fmt:param>
</fmt:message>


<%--
Request Attributes:
	list_maintenance: Bscdimensions list
--%>

<script language="javascript" type="text/javascript" >

    var bscdimensionTable;
    var bscdimensionValidator;

    <%--
        Object that contain methods for add, save, edit and delete a BSC dimension.
    --%>
    var bscDimension = {
        <%-- Add a new BSC dimension --%>
        add: function() {
            var f = document.forms["frm_bscdimension_pop"];
            f.reset();
            f.id.value = "";
            $('#bscdimension-popup legend').html('${new_bscdimension_title}');
            $('#bscdimension-popup').dialog('open');
        },
        <%-- Save a new BSC dimension or an existing BSC dimension recently modified --%>
        save: function() {

            var f = document.frm_bscdimension_pop;

            var params={
                accion: "<%=MaintenanceServlet.JX_SAVE_BSC_DIMENSION %>",
                id: f.id.value,
                name: f.bscdimension_name.value
            };

            if (bscdimensionValidator.form()) {

                mainAjax.call(params, function (data) {
                    var dataRow = [
                        data.id,
                        escape(data.name),
                                '<nobr><img onclick="bscDimension.edit(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
                                '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
                                '<img onclick="bscDimension.delete(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
                    ];

                    if (f.id.value == data.id) { bscdimensionTable.fnUpdateAndSelect(dataRow); }
                    else { bscdimensionTable.fnAddDataAndSelect(dataRow); }

                    f.reset();
                    $('#bscdimension-popup').dialog('close');
                });
            }
        },
        <%-- Edit an existing BSC dimension identified by ID --%>
        edit: function(element) {

            var  bscDim = bscdimensionTable.fnGetData( element.parentNode.parentNode.parentNode );

            var f = document.forms["frm_bscdimension_pop"];

            f.id.value 				= bscDim[0];
            f.bscdimension_name.value= unEscape(bscDim[1]);
            // Display followup info
            $('#bscdimension-popup legend').html('${edit_bscdimension_title}');
            $('#bscdimension-popup').dialog('open');
        },
        <%-- Delete an existing BSC dimension identified by ID --%>
        delete: function(element) {

            confirmUI(
                    '${msg_title_confirm_delete_bsc_dimension}','${msg_confirm_delete_bsc_dimension}',
                    '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                    function() {

                        var bscDim = bscdimensionTable.fnGetData( element.parentNode.parentNode.parentNode );

                        var f = document.frm_bscdimension;

                        f.accion.value = "<%=MaintenanceServlet.DEL_BSC_DIMENSION %>";
                        f.id.value = bscDim[0];
                        f.idManten.value = $('select#idManten').val();

                        loadingPopup();
                        f.submit();
                    }
            );
        }
    };


    readyMethods.add(function () {

        bscdimensionTable = $('#tb_bscdimension').dataTable({
            "sPaginationType": "full_numbers",
            "oLanguage": datatable_language,
            "aaSorting": [[ 1, "asc" ]],
            "aoColumns": [
                { "sClass": "center", "bVisible": false },
                { "sClass": "left" },
                { "sClass": "center", "bSortable" : false}
            ]
        });

        $('#tb_bscdimension tbody tr').live('click',  function (event) {
            bscdimensionTable.fnSetSelectable(this,'selected_internal');
        });

        $('div#bscdimension-popup').dialog({
            autoOpen: false,
            modal: true,
            width: 550,
            resizable: false,
            open: function(event, ui) { bscdimensionValidator.resetForm(); }
        });

        bscdimensionValidator = $("#frm_bscdimension_pop").validate({
            errorContainer: $('div#bscdimension-errors'),
            errorLabelContainer: 'div#bscdimension-errors ol',
            wrapper: 'li',
            showErrors: function(errorMap, errorList) {
                $('span#bscdimension-numerrors').html(this.numberOfInvalids());
                this.defaultShowErrors();
            },
            rules: {
                name: {required: true , maxlength : 100 }
            },
            messages:{
                name:{ required:'${fmtBscDimensionNameRequired}'}
            }
        });

    });

</script>


<%-- Form that contain the BSC dimension information table.
     @param id BSC dimension ID
     @param metric.name BSC dimension name
     @return id and name for add, edit, save and delete options.
--%>
<form id="frm_bscdimension" name="frm_bscdimension" method="post">
    <input type="hidden" name="accion" value="" />
    <input type="hidden" name="idManten" value="" />
    <input type="hidden" id="id" name="id" />
    <%-- List of BSC dimension and options to add, edit and delete. --%>
    <fieldset>
        <legend><fmt:message key="metric.bsc_dimension" /></legend>
        <table id="tb_bscdimension" class="tabledata" cellspacing="1" width="100%">
            <thead>
            <tr>
                <th width="0%"><fmt:message key="id" /></th>
                <th width="92%"><fmt:message key="metric.name" /></th>
                <th width="8%">
                    <img onclick="bscDimension.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="bscdimension" items="${list}">
                <tr>
                    <td>${bscdimension.idBscDimension}</td>
                    <td>${tl:escape(bscdimension.name)}</td>
                    <td>
                        <nobr>
                            <img onclick="bscDimension.edit(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
                            &nbsp;&nbsp;&nbsp;
                            <img onclick="bscDimension.delete(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
                        </nobr>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </fieldset>
</form>

<%-- Popup to add or edit a BSC dimension --%>
<div id="bscdimension-popup">

    <%-- Validation message: input data error. --%>
    <div id="bscdimension-errors" class="ui-state-error ui-corner-all hide">
        <p>
            <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
            <strong><fmt:message key="msg.error_title"/></strong>
            &nbsp;(<b><span id="bscdimension-numerrors"></span></b>)
        </p>
        <ol></ol>

    </div>

    <%-- Form that contain the BSC dimension name field and a save option. --%>
    <form name="frm_bscdimension_pop" id="frm_bscdimension_pop" novalidate="">
        <fieldset>
            <legend><fmt:message key="metric.bsc_dimension"/></legend>

            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                <tr>
                    <th><fmt:message key="metric.name" />&nbsp;*</th>
                    <th>&nbsp;</th>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="id" id="bscdimension_id" />
                        <input type="text" name="name" id="bscdimension_name" class="campo" maxlength="100"/>

                    </td>
                </tr>
            </table>
        </fieldset>
        <div class="popButtons">
            <input type="submit" class="boton" onclick="bscDimension.save(); return false;" value="<fmt:message key="save" />" />
        </div>
    </form>
</div>