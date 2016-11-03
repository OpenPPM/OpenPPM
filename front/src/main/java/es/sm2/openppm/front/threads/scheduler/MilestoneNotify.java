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
 * File: MilestoneNotify.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.threads.scheduler;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.NotificationSpecificLogic;
import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.utils.EmailUtil;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.rendersnake.HtmlCanvas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MilestoneNotify implements Job {

	// FIXME renombrar clase a: MilestoneNotifyJob
	
	public final static Logger LOGGER = Logger.getLogger(MilestoneNotify.class);
	
	public MilestoneNotify() {}
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			LOGGER.info("Start Milestones for notify");
			
			// Declare logic
			NotificationSpecificLogic notificationSpecificLogic = new NotificationSpecificLogic();
			
			// Logic
			notificationSpecificLogic.milestones();

			LOGGER.info("End Milestones for notify");
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
		
		
		/**
		 * Enviar un mail al PM si se ha superado la fecha del status report y no se ha rellenado
		 */
		try {
			
			// FIXME Crear otro job con esta funcionalidad. FollowupNotifyJob
			// FIXME no enviar email. Hay que crear una notificacion. Crear la logica en ProjectFollowupLogic
			
			ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();

			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
			
			LOGGER.info("Start followups for notify");
			
			List<Projectfollowup> projectfollowups = projectFollowupLogic.findForNotify();
			
			LOGGER.info("Followups for notify: "+ projectfollowups.size());

            String[] locale;
            ResourceBundle resourceBundle;

            for (Projectfollowup projectfollowup : projectfollowups) {

                Employee pm = projectfollowup.getProject().getEmployeeByProjectManager();

				if (pm != null && !ValidateUtil.defBoolean(pm.getDisable()) &&
                        (projectfollowup.getProject().getDisable() == null || !projectfollowup.getProject().getDisable())) {
					
					Contact contact = pm.getContact();

                    // Get settings
					HashMap<String, String> settings = SettingUtil.getSettings(contact.getCompany());

                    // TODO 28/10/2014 jordi ripoll el idioma de las notificaciones se modificara en el nuevo desarrollo
                    // Set bundle
                    //
                    locale = SettingUtil.getString(settings, Settings.SettingType.LOCALE).split(StringPool.UNDERLINE);

                    resourceBundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", new Locale(locale[0], locale[1]));
					
					Calendar notificationDate = DateUtil.getCalendar();
					notificationDate.setTime(projectfollowup.getFollowupDate());
					notificationDate.add(Calendar.DAY_OF_MONTH, + Integer.parseInt(SettingUtil.getString(settings, Settings.SETTING_MAIL_DAYS_STATUS_REPORT, Settings.DEFAULT_MAIL_DAYS_STATUS_REPORT)));
					
					Calendar actualDate = DateUtil.getCalendar(); 
					
					if (SettingUtil.getBoolean(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS) &&
                            Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_MAIL_STATUS_REPORT, Settings.DEFAULT_MAIL_STATUS_REPORT)) &&
							!ValidateUtil.isNull(contact.getEmail())) {
						
						if (notificationDate.compareTo(actualDate) <= 0) {

                            // Set subject
                            String subject 	= new ParamResourceBundle("SETTING.STATUS_REPORT_SUBJECT", projectfollowup.getProject().getProjectName()).toString(resourceBundle);

                            // Set body
                            String body		= new ParamResourceBundle("SETTING.STATUS_REPORT_BODY", createStrongTextWithHTML(dateFormat.format(projectfollowup.getFollowupDate()))).toString(resourceBundle);

                            EmailUtil email = new EmailUtil(settings, contact.getEmail(), contact.getEmail());
							email.setSubject(subject);
							email.setBodyText(body);
							email.send();
							LOGGER.info("Send notification followup to:"+ contact.getFullName());
						}
					}
					else if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_MAIL_STATUS_REPORT, Settings.SETTING_MAIL_STATUS_REPORT))) {
						LOGGER.error(contact.getFullName()+" not mail configured for followup notification");
					}
				}
                else if (pm != null && ValidateUtil.defBoolean(pm.getDisable())) {

                    LOGGER.debug("Not add contact("+ pm.getContact().getIdContact() +"): "+
                            pm.getContact().getFullName()+" because employee("+ pm.getIdEmployee()+")disabled");
                }
			}
			LOGGER.info("End followups for notify");
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
	}

    /**
     * Create html strong text
     *
     * @param text
     * @return
     * @throws Exception
     */
    private String createStrongTextWithHTML(String text) throws Exception {

        String textHtml = StringPool.BLANK;

        if (ValidateUtil.isNotNull(text)) {

            HtmlCanvas html = new HtmlCanvas();

            // Bold text
            html.b().content(text);

            textHtml = html.toHtml();
        }

        return textHtml;
    }

}
