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
 * File: NotificationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.ContactnotificationDAO;
import es.sm2.openppm.core.dao.NotificationDAO;
import es.sm2.openppm.core.dao.SettingDAO;
import es.sm2.openppm.core.exceptions.MailException;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Contactnotification.ContactnotificationType;
import es.sm2.openppm.core.model.impl.Notification;
import es.sm2.openppm.core.model.impl.Notification.NotificationMode;
import es.sm2.openppm.core.model.impl.Notification.NotificationStatus;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.core.model.search.NotificationSearch;
import es.sm2.openppm.core.utils.EmailUtil;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



/**
 * Logic object for domain model class Notification
 * @see es.sm2.openppm.logic.Notification
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class NotificationLogic extends AbstractGenericLogic<Notification, Integer> {

	public final static Logger LOGGER = Logger.getLogger(NotificationLogic.class);

    /**
     * Send notifications
     *
     * @throws Exception
     */
	public void sendNotifications() throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			HashMap<String, String> settings;
			List<Notification> notifications;
			
			// Declare DAO
			CompanyDAO companyDAO 			= new CompanyDAO(session);
			NotificationDAO notificationDAO = new NotificationDAO(session);
			
			// Find companies
			List<Company> companies = companyDAO.findActives();

			if (ValidateUtil.isNotNull(companies)) {
				
				for (Company company : companies) {
					
					// Get settings
					settings = getSettings(company, session);
					
					// Notifications activated 
					if (SettingUtil.getBoolean(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS)) {

                        // Notifications activated
                        List<String> notificationsForNotify = new ArrayList<String>();

                        for (NotificationType notificationType : NotificationType.values()) {

                            if (SettingUtil.getBoolean(settings, notificationType.getName(), notificationType.getDefaultValue())) {

                                notificationsForNotify.add(notificationType.getName());
                            }
                        }

						// Find notifications pendings
						notifications = notificationDAO.findPendings(company, notificationsForNotify);

                        if (ValidateUtil.isNotNull(notifications)) {

                            LOGGER.debug("\t\tNotifications: - [" + notifications.size() + "] notifications for this company[" + company.getName() + "]");

                            for (Notification notification : notifications) {

                                // Send mail and update notification
                                //
                                notification.setChangeStatusDate(new Date());

                                try {
                                    sendMail(settings, notification);

                                    notification.setStatus(NotificationStatus.SEND.toString());
                                } catch (Exception exception) {

                                    notification.setStatus(NotificationStatus.ERROR.toString());
                                    notification.setMessageError(exception.getMessage());

                                    LOGGER.error("\t\tNotifications: - Not send email because error send[" + exception.getMessage() + "]");
                                }

                                notificationDAO.makePersistent(notification);
                            }
                        }
					}
				}
			}

			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
	}

	/**
	 * Get settings
	 * 
	 * @param company
	 * @param session 
	 * @return
	 */
	private HashMap<String, String> getSettings(Company company, Session session) {
		
		// Declare DAO
		SettingDAO settingDAO = new SettingDAO(session);
		
		HashMap<String, String> settings = new HashMap<String, String>();
		
		// DAO
		List<Setting> settingsList = settingDAO.findByCompany(company, null);
		
		// Set in hash
		if (ValidateUtil.isNotNull(settingsList)) {
			
			for(Setting setting : settingsList) {
				settings.put(setting.getName(), setting.getValue());
			}
		}
		
		return settings;
	}

	/**
	 * Send mail
	 * 
	 * @param settings
	 * @param notification
	 * @throws MessagingException 
	 * @throws MailException 
	 * @throws AddressException 
	 */
	private void sendMail(HashMap<String, String> settings, Notification notification) throws AddressException, MailException, MessagingException  {
		
		String mailsTO = "";
		String mailsCC = "";
		String mailsCCOO = "";
		
		// Get contact notification 
		//
		if (notification != null && ValidateUtil.isNotNull(notification.getContactnotifications())) {
			
			for (Contactnotification contactNotify : notification.getContactnotifications()) {
				
				if (ContactnotificationType.TO == contactNotify.getContactNotificationType()) {
					
					mailsTO += (ValidateUtil.isNull(mailsTO)?StringPool.BLANK:StringPool.SEMICOLON) + contactNotify.getContact().getEmail();

                    LOGGER.info("Mails to: " + mailsTO);
				}
				else if (ContactnotificationType.COPY == contactNotify.getContactNotificationType()) {

					mailsCC += (ValidateUtil.isNull(mailsCC)?StringPool.BLANK:StringPool.SEMICOLON) + contactNotify.getContact().getEmail();

                    LOGGER.info("Mails CC: " + mailsCC);
				}
				else if (ContactnotificationType.HIDDEN_COPY == contactNotify.getContactNotificationType()) {

					mailsCCOO += (ValidateUtil.isNull(mailsCCOO)?StringPool.BLANK:StringPool.SEMICOLON) + contactNotify.getContact().getEmail();

                    LOGGER.info("Mails CCOO: " + mailsCCOO);
				}
			}
		}
		
		//  Send mail
		//
		if (ValidateUtil.isNotNull(mailsTO)
				|| ValidateUtil.isNotNull(mailsCC)
				|| ValidateUtil.isNotNull(mailsCCOO)) {
			
			EmailUtil email = new EmailUtil(settings, null, mailsTO);
			
			email.setSubject(notification.getSubject());
			email.setBodyText(notification.getBody());
			email.setCc(mailsCC);
			email.setCcoo(mailsCCOO);
			email.send();
		}
		else {
			LOGGER.info("\tNotifications: - Not send email because no data");
		}
	}

    /**
     * Create notification
     *
     * @param subject
     * @param body
     * @param type
     * @param contacts
     * @throws Exception
     */
	public void createNotification(String subject, String body, NotificationType type, List<Contact> contacts) throws Exception {
		
		// Transaction notification 
		//
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			createNotification(session, subject, body, type, contacts);
			
			
			tx.commit();
			
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
	}


    /**
     * Create notification
     *
     * @param session
     * @param subject
     * @param body
     * @param notificationType
     * @param contacts
     * @throws Exception
     */
	public void createNotification(Session session, String subject, String body,
			NotificationType notificationType, List<Contact> contacts) throws Exception {
		
		boolean successfully 		= false;
		Notification notification 	= new Notification();
			
		if (ValidateUtil.isNotNull(subject) && (subject.length() <= 100) && ValidateUtil.isNotNull(body) && notificationType != null && ValidateUtil.isNotNull(contacts)) {
			
			// Declare DAO
			NotificationDAO notificationDAO 				= new NotificationDAO(session);
			ContactnotificationDAO contactnotificationDAO 	= new ContactnotificationDAO(session);
			
			// Set notification
			//
			notification.setSubject(subject);
			notification.setBody(body);
			notification.setStatus(notificationType.getNotificationStatus().name());
			notification.setType(notificationType.name());
			notification.setModeNotification(NotificationMode.AUTOMATIC.name());
			notification.setCreationDate(new Date());
			
			// Save notification
			notification = notificationDAO.makePersistent(notification);
			
			LOGGER.debug("\tNotifications: - Created notification [" + notificationType + "]");

			// Set contactnotifications 
			//
			LOGGER.debug("\tNotifications: - Created [" + contacts.size() + "] contact notification..");
			
			for (Contact contact : contacts) {

				Contactnotification contactNotification = new Contactnotification();

				contactNotification.setNotification(notification);
				contactNotification.setType(ContactnotificationType.TO.toString());
				contactNotification.setContact(contact);

				// Save contact notification
				contactnotificationDAO.makePersistent(contactNotification);

				LOGGER.debug("\t\tNotifications: - Created contact notification to [" + contact.getFullName() + "]..");
			}
			
			successfully = true;
		}
		
		if (successfully) {
			LOGGER.debug("\tNotifications: - Created contact notification successfully");
		}
        else if (subject.length() > 100) {
            LOGGER.warn("\tNotifications: - Not notifications have been created because overflow subject: "+subject);
        }
		else {
			LOGGER.debug("\tNotifications: - Not notifications have been created because there was not information");
		}
	}

    /**
	 * Find notifications by contact
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public JSONArray findByContact(Contact user) throws Exception {

		JSONArray notificationsJSON = new JSONArray();;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			if (user != null) {
				
				// Declare DAO
				ContactnotificationDAO contactnotificationDAO = new ContactnotificationDAO(session);
				
				// DAO
				List<Contactnotification> notifications = contactnotificationDAO.findByContact(user);
				
				// Parse list notification to array JSON 
				//
				if (ValidateUtil.isNotNull(notifications)) {
					
					for (Contactnotification contactnotification : notifications) {
						
						JSONObject notificationJSON = new JSONObject();
						
						notificationJSON.put("read", contactnotification.isReadNotify());
						notificationJSON.put(Contactnotification.IDCONTACTNOTIFICATION, contactnotification.getIdContactNotification());
						
						if (contactnotification.getNotification() != null) {
							
							SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_PATTERN_SIMPLE);
							notificationJSON.put("message", dateFormat.format(contactnotification.getNotification().getCreationDate()) + StringPool.BLANK_DASH + contactnotification.getNotification().getSubject());
							notificationJSON.put("body", contactnotification.getNotification().getBody());
							
							dateFormat = new SimpleDateFormat(Constants.TIME_PATTERN);
							notificationJSON.put("creationDate", dateFormat.format(contactnotification.getNotification().getCreationDate()));
							notificationJSON.put("sendToMail", contactnotification.getNotification().getStatus());
							notificationJSON.put("messageError", contactnotification.getNotification().getMessageError() == null ? "Not information" : contactnotification.getNotification().getMessageError());
						}
						else {
							LOGGER.debug("\tNotifications: - No join notification");
						}
						
						notificationsJSON.add(notificationJSON);
					}
				}
				else {
					LOGGER.debug("\tNotifications: - There are no notifications for this user");
				}
			}
			
			tx.commit();
 		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		
		return notificationsJSON;
	}

    /**
     * Find notifications
     *
     * @param notificationSearch
     * @return
     * @throws Exception
     */
    public List<Contactnotification> find(NotificationSearch notificationSearch) throws Exception {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
        List<Contactnotification> notifications = null;

		try {

			tx = session.beginTransaction();

			if (notificationSearch.getContact() != null) {

				// Declare DAO
				ContactnotificationDAO contactnotificationDAO = new ContactnotificationDAO(session);

				// DAO
				notifications = contactnotificationDAO.find(notificationSearch);
			}
            else {
                throw new NoDataFoundException();
            }

			tx.commit();
 		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}

		return notifications;
	}


}

