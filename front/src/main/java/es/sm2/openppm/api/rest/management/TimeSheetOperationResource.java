/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: front
 * File: OperationResource.java
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 12:57:35
 */

package es.sm2.openppm.api.rest.management;

import es.sm2.openppm.api.converter.management.EmployeeDTOConverter;
import es.sm2.openppm.api.model.common.EmployeeDTO;
import es.sm2.openppm.api.model.management.WeekDTO;
import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.EmployeeLogic;
import es.sm2.openppm.core.logic.impl.TimesheetLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.search.TimeSheetOperationSearch;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
@Path("/management/time-sheet/operation")
@Produces({MediaType.APPLICATION_JSON})
public class TimeSheetOperationResource extends BaseResource {

    /**
     * Get user
     *
     * @return
     */
    @GET
    public Response find(
            @QueryParam(value = "codeOperation") Integer codeOperation,
            @QueryParam(value = "since") String since,
            @QueryParam(value = "until") String until,
            @QueryParam(value = "pool") Integer codePool,
            @QueryParam(value = "codeContact") List<Integer> codeContactList,
            @QueryParam(value = "codeJobCategory") List<Integer> codeJobCategoryList,
            @QueryParam(value = "codeSkill") List<Integer> codeSkillList) {

        // Check permissions
        isUserLogged(
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.RESOURCE,
                Resourceprofiles.Profile.LOGISTIC,
                Resourceprofiles.Profile.RESOURCE_MANAGER
        );

        if (codeOperation == null) {
            throw new RestServiceException(Response.Status.BAD_REQUEST, "Param [codeOperation] is required");
        }

        if (ValidateUtil.isNull(since)) {
            throw new RestServiceException(Response.Status.BAD_REQUEST, "Param [since] is required");
        }

        if (ValidateUtil.isNull(until)) {
            throw new RestServiceException(Response.Status.BAD_REQUEST, "Param [since] is required");
        }

        TimeSheetOperationSearch search = new TimeSheetOperationSearch(
                getCompany(),
                codeOperation,
                formatDate(since),
                formatDate(until));

        //TODO 09/11/2015 - jordi.ripoll - comentada restriccion a peticion del cliente BV
//        if (DateUtil.daysBetween(search.getSince(), search.getUntil()) > 95) {
//            throw new RestServiceException(Response.Status.BAD_REQUEST, "Maxim 95 days between [since] and [until] dates");
//        }

        search.setCodePool(codePool);
        search.setCodeContactList(codeContactList);
        search.setCodeJobCategoryList(codeJobCategoryList);
        search.setCodeSkillList(codeSkillList);

        // Add user if is resource
        if (isUserInRole(Resourceprofiles.Profile.RESOURCE)) {
            search.addCodeContact(getUser().getContact().getIdContact());
        }

        List<EmployeeDTO> employeeList = null;

        try {

            EmployeeLogic logic = new EmployeeLogic();
            List<Employee> employees = logic.find(search, getUser());

            EmployeeDTOConverter converter = new EmployeeDTOConverter();

            employeeList = converter.toDTOList(employees);
        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "find()", e);
        }

        return Response.ok().entity(employeeList).build();
    }


    /**
     * Approve time sheet operations
     *
     * @return
     */
    @PUT
    @Path("{CODE}/approve")
    public Response approve(@PathParam("CODE") Integer code,  EmployeeDTO employeeDTO) {

        // Check permissions
        isUserLogged(
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.RESOURCE,
                Resourceprofiles.Profile.LOGISTIC,
                Resourceprofiles.Profile.RESOURCE_MANAGER
        );

        try {

            // Logic
            TimesheetLogic timesheetLogic = new TimesheetLogic();

            if (isUserInRole(Resourceprofiles.Profile.RESOURCE)) {

                List<Timesheet> timeSheetValidateList = new ArrayList<Timesheet>();

                for (WeekDTO week : employeeDTO.getWeekList()) {

                    if (week.getCode() == null) {
                        throw new RestServiceException(Response.Status.BAD_REQUEST," [weekList.code] is required");
                    }

                    List<String> joins = new ArrayList<String>();
                    joins.add(Timesheet.OPERATION);
                    joins.add(Timesheet.PROJECTACTIVITY);
                    joins.add(Timesheet.PROJECTACTIVITY + "." + Projectactivity.PROJECT);
                    joins.add(Timesheet.EMPLOYEE);
                    joins.add(Timesheet.EMPLOYEE + "." + Employee.PERFORMINGORG);
                    joins.add(Timesheet.EMPLOYEE + "." + Employee.CONTACT);

                    Timesheet timesheet = timesheetLogic.findById(week.getCode(), joins);

                    if (week.getCode() == null) {
                        throw new RestServiceException(Response.Status.BAD_REQUEST," [weekList.code] is required");
                    }

                    if (!getUser().getIdEmployee().equals(timesheet.getEmployee().getIdEmployee())) {
                        throw new RestServiceException(Response.Status.METHOD_NOT_ALLOWED," [weekList.code] is not the logged user");
                    }

                    if (!Constants.TIMESTATUS_APP3.equals(timesheet.getStatus())) {

                        timesheet.setHoursDay1(week.getHoursDay1());
                        timesheet.setHoursDay2(week.getHoursDay2());
                        timesheet.setHoursDay3(week.getHoursDay3());
                        timesheet.setHoursDay4(week.getHoursDay4());
                        timesheet.setHoursDay5(week.getHoursDay5());
                        timesheet.setHoursDay6(week.getHoursDay6());
                        timesheet.setHoursDay7(week.getHoursDay7());

                        timeSheetValidateList.add(timesheet);
                    }
                }

                // Save changes
                for (Timesheet timesheet : timeSheetValidateList) {
                    timesheetLogic.updateTimeSheet(getUser(), getResourceBundle(), getSettings(), getExtraData(), true, timesheet, StringPool.BLANK);
                }
            }
            else {


                changeHours(timesheetLogic, employeeDTO, true);
            }

        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "approve()", e);
        }

        return Response.ok().build();
    }

    /**
     * Approve time sheet operations
     *
     * @return
     */
    @PUT
    @Path("{CODE}/reject")
    public Response reject(@PathParam("CODE") Integer code,  EmployeeDTO employeeDTO) {

        isUserLogged(
                Resourceprofiles.Profile.PMO,
                Resourceprofiles.Profile.LOGISTIC,
                Resourceprofiles.Profile.RESOURCE_MANAGER
        );

        try {

            // Logic
            TimesheetLogic timesheetLogic = new TimesheetLogic();
            changeHours(timesheetLogic, employeeDTO, false);

        }
        catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "reject()", e);
        }

        return Response.ok().build();
    }

    /**
     * Change status of time sheet
     *
     * @param timesheetLogic
     * @param employeeDTO
     * @param approve
     * @throws Exception
     */
    private void changeHours(TimesheetLogic timesheetLogic, EmployeeDTO employeeDTO, boolean approve) throws Exception {

        List<Integer> ids = new ArrayList<Integer>();
        for (WeekDTO week : employeeDTO.getWeekList()) {

            if (week.getCode() == null) {
                throw new RestServiceException(Response.Status.BAD_REQUEST," [weekList.code] is required");
            }
            ids.add(week.getCode());
        }

        timesheetLogic.changeHours(getUser(), ids.toArray(new Integer[ids.size()]), StringPool.BLANK, approve,
                getSettings(), getExtraData(), getResourceBundle());
    }
}
