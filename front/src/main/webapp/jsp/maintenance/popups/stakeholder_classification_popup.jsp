<%--
  Created by IntelliJ IDEA.
  User: jose.llobera
  Date: 13/10/2015
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>



<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
  <fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
  // GLOBAL VARS
  var stakeholderClassificationValidator;

  // OBJECTS
  var stakeholderClassificationPopup = {

    initialize: function() {

      $('div#stakeholderClassificationPopup').dialog({

        autoOpen: false,
        modal: true,
        width: 500,
        minWidth: 500,
        resizable: false,
        open: function(event, ui) { stakeholderClassificationValidator.resetForm(); }
      });
    },

    open: function () {

      $('div#stakeholderClassificationPopup').dialog('open');
    },

    close: function() {

      $('div#stakeholderClassificationPopup').dialog('close');
    },

    formValidator: function() {

      return  $("#stakeholderClassificationPopupForm").validate({
        errorContainer: 'div#stakeholderClassification-errors',
        errorLabelContainer: 'div#stakeholderClassification-errors ol',
        wrapper: 'li',
        showErrors: function(errorMap, errorList) {

          $('span#stakeholderClassification-numerrors').html(this.numberOfInvalids());
          this.defaultShowErrors();
        },
        rules: {

          name: "required"
        },
        messages: {

          name: { required: '${fmtNameRequired}' }
        }
      });
    },

    save: function(){

      if (stakeholderClassificationValidator.form()) {

        stakeholderClassification.save();
      }
    },

    formReset: function(form) {

      form.reset();
      // los hidden se tienen que resetear a mano
      form.id.value = '';
    }
  };

  readyMethods.add(function() {

    stakeholderClassificationPopup.initialize();

    stakeholderClassificationValidator = stakeholderClassificationPopup.formValidator();

    $("#saveStakeholderClassification").on('click', stakeholderClassificationPopup.save);

    $("#closeStakeholderClassification").on('click', stakeholderClassificationPopup.close);
  });

</script>

<div id="stakeholderClassificationPopup" class="popup">

  <div id="StakeholderCLassification-errors" class="ui-state-error ui-corner-all hide">
    <p>
      <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
      <strong><fmt:message key="msg.error_title"/></strong>
      &nbsp;(<b><span id="stakeholderClassification-numerrors"></span></b>)
    </p>
    <ol></ol>
  </div>

  <form name="stakeholderClassificationPopupForm" id="stakeholderClassificationPopupForm">
    <input type="hidden" name="id" id="id" value="" />
    <fieldset>
      <legend><fmt:message key="STAKEHOLDER_CLASSIFICATION"/></legend>
      <table border="0" cellpadding="2" cellspacing="1" width="100%">
        <tr>
          <th class="left" width="80%"><fmt:message key="name"/>*</th>
        </tr>
        <tr>
          <td>
            <input type="text" name="name" class="campo" maxlength="300" />
          </td>
        </tr>
        <tr>
          <th class="left" colspan="2"><fmt:message key="description"/></th>
        </tr>
        <tr>
          <td><textarea rows="4" name="description" class="campo" style="width: 98%" maxlength="2000"></textarea></td>
        </tr>
      </table>
    </fieldset>
    <div class="popButtons">
      <input type="button" class="boton" id="saveStakeholderClassification" value="<fmt:message key="save" />" />
      <input type="submit" class="boton" id="closeStakeholderClassification" value="<fmt:message key="close" />" />
    </div>
  </form>
</div>
