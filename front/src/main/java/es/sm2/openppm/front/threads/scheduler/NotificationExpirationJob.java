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
 * File: NotificationExpirationJob.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.threads.scheduler;

import es.sm2.openppm.core.logic.impl.NotificationSpecificLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;
import java.util.List;

/**
 * Create notification when investment expired
 */
public class NotificationExpirationJob implements Job {

	public final static Logger LOGGER = Logger.getLogger(NotificationExpirationJob.class);

	public NotificationExpirationJob() {}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			
			LOGGER.info("\tNotification expired investments: - Start");

            // Declare logics
            ProjectLogic projectLogic                       = new ProjectLogic(null, null);
            NotificationSpecificLogic notificationsLogic    = new NotificationSpecificLogic();

            // Get investments filter by investment status equal in process
            List<Project> investments = projectLogic.findInvestmentsInProcess();

            if (ValidateUtil.isNotNull(investments)) {

                // Dates
                Calendar actualDate     = DateUtil.getCalendar();
                Calendar startBaseline  = DateUtil.getCalendar();

                // Format date
                actualDate.set(Calendar.HOUR_OF_DAY, 0);
                actualDate.set(Calendar.MINUTE, 0);
                actualDate.set(Calendar.SECOND, 0);
                actualDate.set(Calendar.MILLISECOND, 0);

                // Create notifications
                int count = 0;
                for (Project investment : investments) {

                    // Get date
                    startBaseline.setTime(investment.getPlannedInitDate());

                    // Format date
                    startBaseline.set(Calendar.HOUR_OF_DAY, 0);
                    startBaseline.set(Calendar.MINUTE, 0);
                    startBaseline.set(Calendar.SECOND, 0);
                    startBaseline.set(Calendar.MILLISECOND, 0);

                    // Only expired
                    if (startBaseline.before(actualDate) && !startBaseline.equals(actualDate)) {

                        notificationsLogic.expiredInvestment(investment);

                        count++;
                    }
                }

                LOGGER.info("\tNotification expired investments: number of investments with expired start baseline ("+count+")");
            }
            else {
                LOGGER.info("\tNotification expired investments: no investments");
            }

			LOGGER.info("\tNotification expired investments: - End");
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
	}
}
