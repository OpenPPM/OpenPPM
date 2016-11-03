<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.front.servlets.MyAccountServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: token_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript">
var tokensTable;

function changeToken(element) {
	
	var aData = tokensTable.fnGetData(element.parentNode.parentNode);
	
	var params = {
		accion: '<%=MyAccountServlet.JX_GENERATE_TOKEN%>',	
		<%=Employee.IDEMPLOYEE%>: aData[1]	
	};
	accountAjax.call(params,function(data) {
		
		aData[3] = data.token;
		tokensTable.fnUpdateAndSelect(aData);
	});
}

readyMethods.add(function () {
	
	tokensTable = $('#tb_tokens').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"iDisplayLength": 50,
		"bPaginate": false,
		"aoColumns": [ 
            { "bVisible": false },
            { "bVisible": false },
            { "bVisible": true },
            { "sClass": "center" },
            { "sClass": "center", "bSortable" : false }
    	],
    	"fnDrawCallback": function ( oSettings ) {
            if ( oSettings.aiDisplay.length == 0 ) { return; }
             
            var nTrs = $('#tb_tokens tbody tr');
            var iColspan = nTrs[0].getElementsByTagName('td').length;
            var sLastGroup = "";
            for ( var i=0 ; i<nTrs.length ; i++ ) {
                var iDisplayIndex = oSettings._iDisplayStart + i;
                var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
                if ( sGroup != sLastGroup ) {
                    var nGroup = document.createElement( 'tr' );
                    var nCell = document.createElement( 'td' );
                    nCell.colSpan = iColspan;
                    nCell.className = "groupRow";
                    nCell.innerHTML = sGroup;
                    nGroup.appendChild( nCell );
                    nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
                    sLastGroup = sGroup;
                }
            }
        },
        "aoColumnDefs": [
			{ "bVisible": false, "aTargets": [ 0 ] }
		],
		"aaSortingFixed": [[ 0, 'asc' ]],
		"aaSorting": [[ 2, 'asc' ]],
        "sDom": 'lfr<"giveHeight"t>ip'
	});
	
	$('#tb_tokens tbody tr').live('click',  function (event) {
		tokensTable.fnSetSelectable(this);
	});
});
</script>

<table id="tb_tokens" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th width="45%"><fmt:message key="profile" /></th>
			<th width="50%"><fmt:message key="token" /></th>
			<th width="5%">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="employee" items="${employees }">
			<tr>
				<td>${tl:escape(employee.performingorg.name) }</td>
				<td>${employee.idEmployee }</td>
				<td>${tl:escape(employee.resourceprofiles.profileName) }</td>
				<td>${employee.token }</td>
				<td><img src="images/reload.png" class="link" onclick="changeToken(this)" title="<fmt:message key="token.change"/>"/></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div>&nbsp;</div>