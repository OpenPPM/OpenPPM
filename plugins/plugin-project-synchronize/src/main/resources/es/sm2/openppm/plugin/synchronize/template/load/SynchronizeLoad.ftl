

<form name="frm_project" id="frm_project" method="POST" action="plugin">
    <input type="hidden" name="operationPlugin" value="SynchronizeProject" />
    <input type="hidden" name="pluginAction" value="SaveSynchronizeAction" />
    <input type="hidden" id="id" name="id" value="${project.idProject!""}" />
    <input type="hidden" id="idprojectcharter" name="idprojectcharter" value="${projectCharter.idProjectCharter!""}" />

    <div id="project-errors" class="ui-state-error ui-corner-all hide">
        <p>
            <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
            <strong>${resourceBundle.getString('msg.error_title')}</strong>
            &nbsp;(<b><span id="numerrors"></span></b>)
        </p>
        <ol></ol>
    </div>

    <div>&nbsp;</div>

    <div class="panel">

    <div class="legend" style="cursor:pointer;">
        <div>
        ${resourceBundle.getString('file.import_data')}
        </div>
    </div>

    <div class="content" style="display: block">
        <table width="100%">
            <tr>
                <th class="left" width="25%">${resourceBundle.getString('proposal.bid_manager')}</th>
                <th class="left" width="25%">${resourceBundle.getString('project_manager')}</th>
                <th class="left" width="25%">${resourceBundle.getString('program')}</th>
                <th class="left" width="25%">${resourceBundle.getString('perforg')}</th>
            </tr>
            <tr>
                <td><#if (projectSync.employeeByInvestmentManager)??>${(projectSync.employeeByInvestmentManager.fullName)!""}</#if></td>
                <td><#if (projectSync.employeeByProjectManager)??>${(projectSync.employeeByProjectManager.fullName)!""}</#if></td>
                <td>${(projectSync.program)!""}</td>
                <td>${(projectSync.performingorg)!""}</td>
            </tr>
            <tr>
                <th class="left">${resourceBundle.getString('business_manager')}</th>
                <th class="left">${resourceBundle.getString('sponsor')}</th>
                <th class="left">${resourceBundle.getString('customer')}</th>
                <th class="left">${resourceBundle.getString('category')}</th>
            </tr>
            <tr>
                <td><#if (projectSync.employeeByFunctionalManager)??>${(projectSync.employeeByFunctionalManager.fullName)!""}</#if></td>
                <td><#if (projectSync.employeeBySponsor)??>${(projectSync.employeeBySponsor.fullName)!""}</#if></td>
                <td>${(projectSync.customer)!""}</td>
                <td>${(projectSync.category)!"" }</td>
            </tr>
            <tr>
                <th class="left">${resourceBundle.getString('contract_type')}</th>
                <th class="left">${resourceBundle.getString('geography')}</th>
                <th class="left">${resourceBundle.getString('stage_gate')}</th>
                <th></th>
            </tr>
            <tr>
                <td>${(projectSync.contracttype)!""}</td>
                <td>${(projectSync.geography)!""}</td>
                <td>${(projectSync.stageGate.name)!""}</td>
                <td></td>
            </tr>
        </table>
        <div class="right">
            <button id="saveImport" class="boton" type="button">${resourceBundle.getString('save_and_import')}</button>
        </div>

    <div style="width: 100px;">&nbsp;</div>
    </div>
    </div>


    <div>&nbsp;</div>

    <div class="panel">

    <div class="legend" style="cursor:pointer;">
        <div>
            ${resourceBundle.getString('basic_data')}
        </div>
    </div>

<div class="content"  style="display: block">
        <table width="100%">
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th class="left" width="25%">${resourceBundle.getString('project.chart_label')}*</th>
                <th class="left" colspan="3">${resourceBundle.getString('project_name')}*</th>
                <th class="center" width="12%">${resourceBundle.getString('project.budget_year')}</th>
                <th class="left" width="13%">${resourceBundle.getString('project.accounting_code')}</th>
            </tr>
            <tr>
                <td><input type="text" class="campo" name="chartLabel" id="chartLabel" maxlength="25" value="${project.chartLabel!""}" style="width: 162px;" /></td>
                <td colspan="3">
                    <input type="text" name="projectname" class="campo" value="${project.projectName!""}" maxlength="80"/>
                </td>
                <td class="center"><input type="text" id="budgetYear" name="budgetYear" maxlength="4" class="campo" style="width: 32px;"
																					value="<#if project.budgetYear??>${project.budgetYear?c}</#if>"></td>
                <td><input type="text" name="accountingCode" id="accountingCode" class="campo" maxlength="25" value="${project.accountingCode!""}" style="width: 162px;" /></td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th class="left">
                    ${resourceBundle.getString('proposal.bid_manager')}*
                    <img class="position_right icono"  title="${resourceBundle.getString('search')}" src="images/search.png" onclick="searchEmployeePop('employeeByInvestmentManager', jQuery('performingorg').val(), 3);" />
                </th>
                <th class="left" width="25%">
                    ${resourceBundle.getString('project_manager')}*
                    <img class="position_right icono"  title="${resourceBundle.getString('search')}" src="images/search.png" onclick="return searchProjectMember('projectmanager', 7);"/>
                </th>
                <th class="left" colspan="2">${resourceBundle.getString('program')}&nbsp;*</th>
                <th colspan="2">${resourceBundle.getString('category')}</th>
            </tr>
            <tr>
                <td>
										<input type="hidden" id="employeeByInvestmentManager" name="employeeByInvestmentManager"
												 value="<#if project.employeeByInvestmentManager??>${project.employeeByInvestmentManager.idEmployee!""}</#if>"/>
										<input type="text" id="employeeByInvestmentManager_name" class="campo" name="employeeByInvestmentManager_name"
											 value="<#if project.employeeByInvestmentManager??>${project.employeeByInvestmentManager.contact.fullName!""}</#if>" readonly/>
                </td>
                <td>
									<input type="hidden" id="projectmanager" name="projectmanager"
												 value="<#if (project.employeeByProjectManager)??>${project.employeeByProjectManager.idEmployee!""}</#if>"/>
									<input type="text" id="projectmanager_name" class="campo" name="projectmanager_name"
												 value="<#if (project.employeeByProjectManager)??>${project.employeeByProjectManager.contact.fullName!""}</#if>" readonly/>
                </td>
                <td colspan="2">
                    <select name="program" id="programSelect" class="campo">
                        <option value="">${resourceBundle.getString('select_opt')}</option>
                    <#list programs as program>
                        <option class='${program.initBudgetYear!"empty"}'
                            <#assign initBudgetYear=(program.initBudgetYear)!"empty">
                            until='<#if initBudgetYear != "empty">""<#else>${program.finishBudgetYear}</#if>'
                            value="${program.idProgram!""}"
                            <#if project.program.idProgram == program.idProgram>"selected"<#else>""</#if>>
                        ${program.programName!""}
                        </option>
                    </#list>
                    </select>
                </td>
                <td colspan="2">
                    <select name="category" id="category" class="campo">
                        <option value="">${resourceBundle.getString('select_opt')}</option>
                    <#list categories as category>
                        <option value="${category.idCategory}"
                            <#if category.equals(project.category)>"selected"<#else>""</#if>>
                            ${category.name!""}
                        </option>
                    </#list>
                    </select>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th class="left">
                    ${resourceBundle.getString('business_manager')}
                    <img class="position_right icono"  title="${resourceBundle.getString('search')}" src="images/search.png" onclick="return searchProjectMember('functionalmanager',4);" />
                </th>
                <th class="left">
                    ${resourceBundle.getString('sponsor')}
                    <img class="position_right icono"  title="${resourceBundle.getString('search')}" src="images/search.png" onclick="return searchProjectMember('sponsor', 8);" />
                </th>
                <th class="left" colspan="2">${resourceBundle.getString('customer')}
                    <img class="position_right icono"  title="${resourceBundle.getString('add')}" src="images/AddContact.png" onclick="return newCustomerPopup();"/>
                </th>
                <th class="center">${resourceBundle.getString('tcv')}</th>
                <th class="center">${resourceBundle.getString('bac')}</th>
            </tr>
            <tr>
                <td>
									<input type="hidden" id="functionalmanager" name="functionalmanager"
												 value="<#if (projectSync.employeeByFunctionalManager)??>${(project.employeeByFunctionalManager.idEmployee)!""}</#if>"/>
									<input type="text" class="campo" id="functionalmanager_name" name="functionalmanager_name"
												 value="<#if (projectSync.employeeByFunctionalManager)??>${(project.employeeByFunctionalManager.contact.fullName)!""}</#if>" readonly/>
                </td>
                <td>
									<input type="hidden" id="sponsor" name="sponsor"
												 value="	<#if (projectSync.employeeBySponsor)??>${(project.employeeBySponsor.idEmployee)!""}</#if>"/>
									<input type="text" class="campo" id="sponsor_name" name="sponsor_name"
												 value="	<#if (projectSync.employeeBySponsor)??>${(project.employeeBySponsor.contact.fullName)!""}</#if>" readonly/>
                </td>
                <td colspan="2">
                    <select name="customer" id="customer" class="campo">
                        <option value="" selected>${resourceBundle.getString('select_opt')}</option>
                    <#list customers as customer>
                        <option value="${customer.idCustomer}"
                        <#if customer.equals(project.customer)>"selected"</#if>>
                        ${customer.name!""}
                        </option>
                    </#list>
                    </select>
                </td>
                <td class="center">
                    <input type="text" id="tcv" name="tcv" title="${resourceBundle.getString('tcv_desc')}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;"
													 value="${project.tcv?string[currencyPattern]!0?string[currencyPattern]}" />
                </td>
                <td class="center">
									<input type="text" id="bac" name="bac" title="${resourceBundle.getString('bac_desc')}" class="campo importe"
												 value="<#if project.bac??>${project.bac?string[currencyPattern]!0?string[currencyPattern]}</#if>"
                         style="width:85px;margin:0px 2px 0px 0px;"/>
								</td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th class="left">${resourceBundle.getString('baseline_start')}&nbsp;*</th>
                <th class="left">${resourceBundle.getString('baseline_finish')}&nbsp;*</th>
                <th class="left">${resourceBundle.getString('sac')}</th>
                <th class="center">${resourceBundle.getString('strategic_value')}</th>
                <th class="center">${resourceBundle.getString('proposal.win_probability')}</th>
                <th class="center">
                <#if defaultCostVariance??>
                ${resourceBundle.getString('external_costs')}
                <#else>
                &nbsp;
                </#if>
                </th>
            </tr>
            <tr>
                <td>
                    <input type="text" name="plannedInitDate" id="plannedInitDate" class="campo fecha"
                           value="${project.plannedInitDate?date?string(datePattern)!""}"
                    />
                </td>
                <td>
                    <input type="text" name="plannedFinishDate" id="plannedFinishDate" class="campo fecha"
                           value="${project.plannedFinishDate?date?string(datePattern)!""}"
                    />
                </td>
                <td>
                    <nobr>
                        <input type="text" name="sac" id="sac" class="campo" style="width:40px;margin:0px 2px 0px 5px;" value="${(project.duration)!""}"/>d
                    </nobr>
                </td>
                <td class="center">
                    <nobr>
                        <input type="text" name="strategic_value" maxlength="3" style="width: 24px;" class="campo right" value="${project.priority!""}"/>%
                    </nobr>
                </td>
                <td class="center">
                    <nobr>
                    <#if defaultProbabilityVariance??>
                        <input type="text" id="probability" name="probability" title="${resourceBundle.getString('bac_desc')}" class="campo right" style="width:24px;margin:0px 2px 0px 0px;" maxlength="3" value="${project.probability!}"/>%
                    <#else>
                        <select id="probability" name="probability" class="campo" style="width:50px;">
                        <#if defaultProbability??>
                            <#list defaultProbability as value>
                                <option value="${value}" <#if value == project.probability>"selected"</#if>>${value}</option>
                            </#list>
                            </select>%
                        </#if>
                    </#if>
                    </nobr>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th>${resourceBundle.getString('contract_type')}</th>
                <th>${resourceBundle.getString('geography')}</th>
                <th>${resourceBundle.getString('stage_gate')}</th>
                <th colspan="3">&nbsp;</th>
            </tr>
            <tr>
            <td>
                <select name="contracttype" class="campo">
                <option value='' selected>${resourceBundle.getString('select_opt')}</option>
                    <#list contractTypes as contractType>
                        <option value="${contractType.idContractType}"
                        <#assign projectIdContractType=(project.contracttype.idContractType)!-1>
                        <#assign idContractType = (contractType.idContractType)!-1>
                        <#if projectIdContractType == idContractType && (projectIdContractType != -1 && idContractType != -1)>"selected"</#if>>
                            ${contractType.description!""}
                        </option>
                    </#list>
                </select>
                </td>
                <td>
                    <select name="geography" class="campo">
                        <option value=''>${resourceBundle.getString('select_opt')}</option>
                        <#list geographies as geography>
                            <option value="${geography.idGeography}"
                            <#if geography.equals(project.geography)>"selected"</#if>>
                            ${geography.name!""}</option>
                        </#list>
                    </select>
                </td>
                <td class="center">
                    <select name="stagegate" class="campo">
                        <option value=''>${resourceBundle.getString('sac')}</option>
                        <#list stageGates as stageGate>
                            <option value="${stageGate.idStageGate!""}"
                            <#assign projectIdStageGate=(project.stagegate.idStageGate)!-1>
                            <#assign idStageGate = (stageGate.idStageGate)!-1>
                            <#if projectIdStageGate == idStageGate && (projectIdStageGate != -1 && idStageGate != -1)>"selected"</#if>>
                            ${stageGate.name!""}</option>
                        </#list>
                    </select>
                </td>
                <td class="center" colspan="3">
                    <div style="width: 50%; float: left;">
                        <nobr>
                            <input name="isGeoSelling" style="width:20px;" value="true" type="checkbox" <#if project.isGeoSelling??>"checked"</#if>/>
                            ${resourceBundle.getString('proposal.requires_travelling')}
                        </nobr>
                    </div>
                    <div style="width: 50%; float: right;">
                        <nobr>
                            <input name="internalProject" style="width:20px;" id="internalProject" value="true" type="checkbox" <#if project.internalProject??>"checked"</#if>/>
                            ${resourceBundle.getString('project.internal_project')}
                        </nobr>
                    </div>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <th colspan="2">${resourceBundle.getString('document_link')}</th>
                <#if optionaCurrency?? >
                    <th class="center">${resourceBundle.getString('currency.opcional1')}</th>
                    <th class="center">${resourceBundle.getString('currency.opcional2')}</th>
                    <th class="center">${resourceBundle.getString('currency.opcional3')}</th>
                    <th class="center">${resourceBundle.getString('currency.opcional4')}</th>
                <#else>
                    <th colspan="4"></th>
                </#if>
            </tr>
            <tr>
                <td colspan="2">
                        <#assign linkDoc = (project.linkDoc)!"">
                        <#if linkDoc == "" || linkDoc = "empty">
                            <#assign "linkDocument">style="display:none;"</#assign>
                        <#else>
                        <#assign "inputDocument">style="display:none;"</#assign>
                        </#if>
                    <div ${(inputDocument)!""} id="toggleEditLinkIni">
                      <#if hasPermission??>
                            <img style="width:16px;" src="images/approve.png" onclick="confirmIniLink()"/>
                        </#if>
                        <input type="text" name="linkDoc" id="linkDoc" style="width: 375px; *width: 365px !important;" maxlength="200" class="campo" value="${project.linkDoc!""}" />
                    </div>
                    <div ${linkDocument!""} id="toggleALinkIni">
                        <#if hasPermission??>
                            <img style="width:16px;" src="images/modify.png" onclick="modifyIniLink()"/>
                        </#if>
                        <a href="${project.linkDoc!""}" target="_blank" id="a_linkIni">${project.linkDoc!""}</a>
                    </div>
                </td>
                    <#if optionalCurrency??>
                        <td class="center"><input type="text" name="currencyOptional1" id="currencyOptional1" title="${resourceBundle.getString('currency.opcional1')}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional1!""}"/></td>
                        <td class="center"><input type="text" name="currencyOptional2" id="currencyOptional2" title="${resourceBundle.getString('currency.opcional2')}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional2!""}"/></td>
                        <td class="center"><input type="text" name="currencyOptional3" id="currencyOptional3" title="${resourceBundle.getString('currency.opcional3')}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional3!""}" /></td>
                        <td class="center"><input type="text" name="currencyOptional4" id="currencyOptional4" title="${resourceBundle.getString('currency.opcional4')}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional4!""}" /></td>
                    <#else><td colspan="4"></td>
                </#if>
            </tr>
        </table>
    <div style="width: 100px;">&nbsp;</div>
    </div>
    </div>
    <#--PROJECT CHARTER-->
        <div>&nbsp;</div>

        <div class="panel">

            <div class="legend" style="cursor:pointer;">
                <div>
                ${resourceBundle.getString('project_charter')}
                </div>
            </div>

            <div class="content"  style="display: block">

        <table width="100%">
            <tr>
                <th class="left">${resourceBundle.getString('project_charter.project_description')}</th>
                <th class="left">${resourceBundle.getString('project_charter.project_objectives')}</th>
            </tr>
            <tr>
                <td class="center">
                    <textarea name="successcriteria" class="campo show_scroll" rows="8" style="width:90%;">${projectCharter.sucessCriteria!""}</textarea>
                </td>
                <td class="center">
                    <textarea name="projectobjectives" id="projectobjectives" class="campo show_scroll" rows="8" style="width:90%;">${projectCharter.projectObjectives!""}</textarea>
                </td>
            </tr>
            <tr>
                <th class="left">${resourceBundle.getString('project_charter.business_need')}</th>
                <th class="left">${resourceBundle.getString('project_charter.main_constraints')}</th>
            </tr>
            <tr>
                <td class="center">
                    <textarea name="businessneed" id="businessneed" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.businessNeed!""}</textarea>
                </td>
                <td class="center">
                    <textarea name="mainconstraints" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainConstraints!""}</textarea>
                </td>
            </tr>
            <tr>
                <th class="left">${resourceBundle.getString('project_charter.main_risks')}</th>
                <th class="left">${resourceBundle.getString('project_charter.main_assumptions')}</th>
            </tr>
            <tr>
                <td class="center">
                    <textarea name="mainrisks" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainRisks!""}</textarea>
                </td>
                <td class="center">
                    <textarea name="mainassumptions" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainAssumptions!""}</textarea>
                </td>
            </tr>
            <tr>
                <th class="left">${resourceBundle.getString('project_charter.main_deliverables')}</th>
                <th class="left">${resourceBundle.getString('project_charter.exclusions')}</th>
            </tr>
            <tr>
                <td class="center">
                    <textarea name="mainDeliverables" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainDeliverables!""}</textarea>
                </td>
                <td class="center">
                    <textarea name="exclusions" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.exclusions!""}</textarea>
                </td>
            </tr>
        </table>

        <table width="100%">
            <tr>
                <th width="50%">&nbsp;</th>
                <th width="30%" align="right">${resourceBundle.getString('internal_effort')}</th>
                <th width="20%" align="right">${resourceBundle.getString('project.net_value')}</th>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td align="right">
									<input type="text" name="effort" id="effort" class="campo right" maxlength="5"
												 style="width:40px;margin:0px 2px 0px 5px;" value="${project.effort!""}" />h
                </td>
                <td align="right">
									<input type="text" name="ni" title="${resourceBundle('ni_desc')}" class="campo importe"
												 style="width:85px;margin:0px 2px 0px 0px;" value="${project.netIncome!""}"/>
                </td>
            </tr>
        </table>
        <div style="width: 100px;">&nbsp;</div>
        </div>
        </div>
</form>

<script type="text/javascript">
  <!--
  var validator;
  jQuery(document).ready(function() {

    validator = jQuery("#frm_project").validate({
      errorContainer: 'div#project-errors',
      errorLabelContainer: 'div#project-errors ol',
      wrapper: 'li',
      showErrors: function(errorMap, errorList) {
        jQuery('span#numerrors').html(this.numberOfInvalids());
        this.defaultShowErrors();
      },
      rules: {
        chartLabel: { required: true },
        projectname: { required: true },
        projectmanager_name : { required: true },
        employeeByInvestmentManager_name : { required: true },
        program: { required: true },
        plannedInitDate : { required: true, date : true, minDateTo: '#plannedFinishDate' },
        plannedFinishDate : { required: true, date : true, maxDateTo: '#plannedInitDate' },
        strategic_value: { range: [0,99] },
        'probability${disableValidate}': { range: [0,99] },
        sac: { min: 0, integerPositive: true },
        effort: { min: 0 },
        successcriteria: { maxlength: 1500 },
        projectobjectives: { maxlength: 1500 },
        businessneed: { maxlength: 1500 },
        mainconstraints: { maxlength: 1500 },
        mainassumptions: { maxlength: 1500 },
        mainDeliverables: { maxlength: 1500 },
        exclusions: { maxlength: 1500 },
        mainrisks: { maxlength: 1500 },
        tcv: { maxlength: 14 },
        bac: { maxlength: 14},
        ni: { maxlength: 14}
      },
      messages: {
        chartLabel : {required: '${data["fmtShortNameRequired"]}' },
        projectname : {required: '${data["fmtProjectNameRequired"]}' },
        projectmanager_name : {required: '${data["fmtProjectManagerRequired"]}' },
        employeeByInvestmentManager_name : {required: '${data["fmtInvestmentManagerRequired"]}' },
        program : {required: '${data["fmtProgramRequired"]}' },
        plannedInitDate: { required: '${data["fmtPlannedStartDateRequired"]}', date: '${data["fmtPlannedStartDateFormat"]}', minDateTo: '${data["fmtPlannedStartDateAfterFinish"]}' },
        plannedFinishDate: { required: '${data["fmtPlannedFinishDateRequired"]}', date: '${data["fmtPlannedFinishDateFormat"]}', maxDateTo: '${data["fmtPlannedFinishDateBeforeStart"]}' },
        strategic_value : { range : '${data["fmtPriorityOutOfRange"]}' },
        'probability${disableValidate}' : { range : '${data["fmtProbabilityOutOfRange"]}' },
        sac : { min : '${data["fmtSacMinValue"]}' },
        effort : { min : '${data["fmtEffortMinValue"]}' },
        tcv: { maxlength: '${data["fmtTcvMax"]}' },
        bac: { maxlength: '${data["fmtBacMax"]}' },
        ni: { maxlength: '${data["fmtNiMax"]}' }
      }
    });

    jQuery('#budgetYear').filterSelect({
      selectFilter:'program',
      emptyAll: true,
      showEmpty: true,
      between: true
    });

    jQuery('#programSelect').val('${project.program.idProgram}');

    var dates = jQuery('#plannedInitDate, #plannedFinishDate').datepicker({
      showOn: 'button',
      buttonImage: 'images/calendario.png',
      buttonImageOnly: true,
      onSelect: function(selectedDate) {
        var option = this.id == "plannedInitDate" ? "minDate" : "maxDate";
        var instance = jQuery(this).data("datepicker");
        var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
        dates.not(this).datepicker("option", option, date);
        if (validator.numberOfInvalids() > 0) validator.form();
      }
    });

    jQuery("#saveImport").on('click',function() {
      confirmUI('${resourceBundle.getString('msg.confirm')}',
          '${resourceBundle.getString('msg.warning.import_project')}',
          '${resourceBundle.getString('yes')}',
          '${resourceBundle.getString('no')}',
          function() {
            if (validator.form() && validateDates()) {
              loadingPopup();
              document.frm_project.submit();
            }
          });
    });

    jQuery("#saveImport").button();
  });
  //-->
</script>