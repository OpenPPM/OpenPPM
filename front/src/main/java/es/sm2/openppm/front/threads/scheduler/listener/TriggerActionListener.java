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
 * File: TriggerActionListener.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.threads.scheduler.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;

public class TriggerActionListener implements TriggerListener {
	
	private static final Logger logger = Logger.getLogger(TriggerActionListener.class);

	@Override
	public String getName() {
		return "triggerActionListener";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		TriggerLogDetail logDetail = new TriggerLogDetail(trigger,context);
		logAction("TriggerFired",logDetail);
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		TriggerLogDetail logDetail = new TriggerLogDetail(trigger);
		logAction("TriggerMisfired",logDetail);		
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		TriggerLogDetail logDetail = new TriggerLogDetail(trigger,context,triggerInstructionCode);
		logAction("TriggerComplete",logDetail);
	}
	
	private void logAction(String action, TriggerLogDetail logDetail){
		logger.info(action + ": " + logDetail.getExecutionDate() + " - " + logDetail.getTriggerKey() + " - " + logDetail.getJobKey() + " - " + logDetail.getDetails() );
	}
	
	private String getInetAddress(){
		String ip;
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			ip = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			ip = "";
		}
	    return ip;
	}
	
	public class TriggerLogDetail{
		
		private TriggerKey triggerKey;
		private JobKey jobKey;
		private String ip;
		private String schedulerName;
		private String schedulerInstanceId;
		private String details;
		private Date executionDate;
		
		public TriggerLogDetail(Trigger trigger){
			this(trigger,null);
		}
		
		public TriggerLogDetail(Trigger trigger, JobExecutionContext context){
			this(trigger,context,null);
		}
		
		public TriggerLogDetail(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode){
			this.triggerKey = trigger.getKey();
			this.jobKey = trigger.getJobKey();
			this.ip = getInetAddress();
			this.executionDate = new Date();
			
			if (context != null){
				try{
					Scheduler scheduler = context.getScheduler();
					this.schedulerInstanceId = scheduler.getSchedulerInstanceId();
					this.schedulerName = scheduler.getSchedulerName();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			if (triggerInstructionCode != null) {
				this.details = triggerInstructionCode.toString();
			}
		}

		public TriggerKey getTriggerKey() {
			return triggerKey;
		}

		public void setTriggerKey(TriggerKey triggerKey) {
			this.triggerKey = triggerKey;
		}

		public JobKey getJobKey() {
			return jobKey;
		}

		public void setJobKey(JobKey jobKey) {
			this.jobKey = jobKey;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getSchedulerName() {
			return schedulerName;
		}

		public void setSchedulerName(String schedulerName) {
			this.schedulerName = schedulerName;
		}

		public String getSchedulerInstanceId() {
			return schedulerInstanceId;
		}

		public void setSchedulerInstanceId(String schedulerInstanceId) {
			this.schedulerInstanceId = schedulerInstanceId;
		}

		public Date getExecutionDate() {
			return executionDate;
		}

		public void setExecutionDate(Date executionDate) {
			this.executionDate = executionDate;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}
	}

}
