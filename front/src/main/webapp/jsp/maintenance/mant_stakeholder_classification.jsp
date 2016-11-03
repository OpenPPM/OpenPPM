<%--
  Created by IntelliJ IDEA.
  User: jose.llobera
  Date: 13/10/2015
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.api.model.enums.MaintenanceEnum"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_stakeholder_classification">
  <fmt:param><fmt:message key="STAKEHOLDER_CLASSIFICATION"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_stakeholder_classification">
  <fmt:param><fmt:message key="STAKEHOLDER_CLASSIFICATION"/></fmt:param>
</fmt:message>

<%--
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
  // GLOBAL VARS
  var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');
  var stakeholderClassificationTable;

  // OBJECTS
  var stakeholderClassification = {

    createTable: function() {

      return $('#stakeholderClassificationTable').dataTable({

        "sPaginationType": "full_numbers",
        "oLanguage": datatable_language,
        "bInfo": false,
        "fnRowCallback": function( nRow, aData) {

          $('td:eq(2)', nRow).html( stakeholderClassification.buttons(aData[4]) );
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

      stakeholderClassificationPopup.formReset(document.stakeholderClassificationPopupForm);

      stakeholderClassificationPopup.open();
    },

    edit: function(element) {

      var row = stakeholderClassificationTable.fnGetData(element.parentNode.parentNode.parentNode);

      var f = document.stakeholderClassificationPopupForm;

      stakeholderClassificationPopup.formReset(f);

      f.id.value 			= row[0];
      f.name.value        = unEscape(row[1]);
      f.description.value = unEscape(row[2]);

      stakeholderClassificationPopup.open();
    },

    save: function() {

      var f = document.stakeholderClassificationPopupForm;

      var params = {
        code: f.id.value,
        name: f.name.value,
        description: f.description.value
      };

      $.ajax({

        url: 'rest/maintenance/' + '<%= MaintenanceEnum.STAKEHOLDER_CLASSIFICATION.name() %>',
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

            stakeholderClassificationTable.fnUpdateAndSelect(dataRow);
          }
          else {

            stakeholderClassificationTable.fnAddDataAndSelect(dataRow);
          }

          stakeholderClassificationPopup.close();
        }
      });
    },

    del: function(element) {

      confirmUI(

              '${msg_title_confirm_delete_stakeholder_classification}','${msg_confirm_delete_stakeholder_classification}',
              '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
              function() {

                var row = stakeholderClassificationTable.fnGetData(element.parentNode.parentNode.parentNode);

                $.ajax({

                  url: 'rest/maintenance/' + '<%= MaintenanceEnum.STAKEHOLDER_CLASSIFICATION.name() %>' + '/' + row[0],
                  type: 'DELETE',
                  dataType: 'json',
                  contentType: 'application/json',
                  data: {},
                  success: function () {

                    stakeholderClassificationTable.fnDeleteSelected();
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
      $buttons.append($('<img/>', {onclick: 'stakeholderClassification.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
              .append('&nbsp;&nbsp;&nbsp;')
              .append($('<img/>', {onclick: 'stakeholderClassification.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
              .append('&nbsp;&nbsp;&nbsp;');
      return $buttons;
    },

    loadList : function() {

      $.ajax({
        url: 'rest/maintenance/' + '<%= MaintenanceEnum.STAKEHOLDER_CLASSIFICATION.name() %>',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        data: {},
        success: function (data) {

          // Clear table
          stakeholderClassificationTable.fnClearTable();

          // Add list
          $(data).each(function() {

            var row = [
              this.code,
              escape(this.name != null != null ? this.name : ''),
              escape(this.description != null != null ? this.description : ''),
              ''
            ];

            // Add row
            stakeholderClassificationTable.fnAddData(row);
          });
        }
      });
    }
  };

  readyMethods.add(function () {

    // Create table
    stakeholderClassificationTable = stakeholderClassification.createTable();

    // Events
    $('#stakeholderClassificationTable tbody tr').live('click',  function (event) {
      stakeholderClassificationTable.fnSetSelectable(this,'selected_internal');
    });

    // Load list
    stakeholderClassification.loadList();
  });

</script>

<fieldset>
  <legend><fmt:message key="STAKEHOLDER_CLASSIFICATION" /></legend>

  <%-- KnowledgeArea table --%>
  <table id="stakeholderClassificationTable" class="tabledata" width="100%">
    <thead>
    <tr>
      <th width="0%"></th>
      <th width="20%"><fmt:message key="name" /></th>
      <th width="70%"><fmt:message key="description" /></th>
      <th width="10%">
        <img onclick="stakeholderClassification.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
      </th>
    </tr>
    </thead>
    <tbody></tbody>
  </table>
</fieldset>

<div class="spacer"></div>

<jsp:include page="popups/stakeholder_classification_popup.jsp" flush="true" />

