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
 * File: NotificationJob.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.threads.scheduler;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.sm2.openppm.core.logic.impl.NotificationLogic;
import es.sm2.openppm.front.utils.ExceptionUtil;

public class NotificationJob implements Job {

	public final static Logger LOGGER = Logger.getLogger(NotificationJob.class);
	
	public NotificationJob() {}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			
			// Declare logic
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Logic
			notificationLogic.sendNotifications();
			
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
	}
}
