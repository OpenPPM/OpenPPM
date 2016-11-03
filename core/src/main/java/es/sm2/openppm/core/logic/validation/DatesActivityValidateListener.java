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
 * Module: core
 * File: DatesActivityValidateListener.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.validation;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.listener.exceptions.ActionListenerException;
import es.sm2.openppm.core.listener.impl.project.SavePlanActivityAbstractListener;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DatesActivityValidateListener extends SavePlanActivityAbstractListener {


    @Override
    public void run(Projectactivity oldInstance, Projectactivity newInstance) throws ActionListenerException {

        if (oldInstance != null && oldInstance.getPlanEndDate() != null && oldInstance.getPlanInitDate() != null &&
                newInstance != null && newInstance.getPlanEndDate() != null && newInstance.getPlanInitDate() != null &&
                oldInstance.getProject() != null) {

            // Set dates
            //
            Calendar planInitOld = DateUtil.getCalendar();
            planInitOld.setTime(oldInstance.getPlanInitDate());

            Calendar planEndOld = DateUtil.getCalendar();
            planEndOld.setTime(oldInstance.getPlanEndDate());

            Calendar planInitNew = DateUtil.getCalendar();
            planInitNew.setTime(newInstance.getPlanInitDate());

            Calendar planEndNew = DateUtil.getCalendar();
            planEndNew.setTime(newInstance.getPlanEndDate());

            //  Error control zombie imputations
            //
            if (planInitNew.after(planInitOld) || planEndNew.before(planEndOld)) {

                List<Teammember> teammembers;

                try {

                    // Declare logic
                    TeamMemberLogic teamMemberLogic = new TeamMemberLogic();

                    // Get teammembers by dates and activity and status
                    List<String> statusList = new ArrayList<String>();

                    statusList.add(Constants.RESOURCE_PRE_ASSIGNED);
                    statusList.add(Constants.RESOURCE_ASSIGNED);

                    teammembers = teamMemberLogic.findByActivityAndStatus(oldInstance, statusList, null);
                }
                catch (Exception e) {
                    throw new ActionListenerException(e);
                }

                // Do not allow the planned move date if there team members preassigned or assigned within these.
                //
                if (ValidateUtil.isNotNull(teammembers)) {

                    // Sort by date in
                    Collections.sort(teammembers, new EntityComparator<Teammember>(new Order(Teammember.DATEIN, Order.ASC)));

                    Calendar minorDate = DateUtil.getCalendar();
                    minorDate.setTime(teammembers.get(0).getDateIn());

                    // Sort by date out
                    Collections.sort(teammembers, new EntityComparator<Teammember>(new Order(Teammember.DATEOUT, Order.DESC)));

                    Calendar higherDate = DateUtil.getCalendar();
                    higherDate.setTime(teammembers.get(0).getDateOut());

                    if (planInitNew.after(minorDate) || planEndNew.before(higherDate)) {

                        //TODO jordi.ripoll - 03/03/2015 - Upgrade to perform: paint more information
                        throw new ActionListenerException("msg.error.not_reschedule_plan_dates");
                    }
                }
            }
        }
    }
}
