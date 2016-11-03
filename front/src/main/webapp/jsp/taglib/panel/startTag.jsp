<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
  ~ File: startTag.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<div>&nbsp;</div>

<c:set var="print"> ${tl:isNull(id) ? "": "hidePrint"}</c:set>

<div class="panel ${cssClass} ${print}">
	<c:choose>
		<c:when test="${!tl:isNull(id)}">
			<c:set var="tagOnclick">onclick="changeCookie('${id}'${tl:isNull(callback)?"":","}${callback })" style="cursor:pointer;"</c:set>
			<c:set var="tagId">id="${id}"</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="tagStyle">style="display:block !important;"</c:set>
		</c:otherwise>
	</c:choose>
	
	<div class="legend" ${tagOnclick }>
		<div>
			${tl:isNull(title)?"":title}
			<c:if test="${!tl:isNull(id)}">
				<img id="${id }Btn" src="images/ico_mas.gif">
				${buttons }
			</c:if>
		</div>
	</div>
	<div class="content" ${tagId } ${showTiltePanel?tagTitle:""} ${tagStyle}>