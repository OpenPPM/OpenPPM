package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.dao.NotificationtypeDAO;
import es.sm2.openppm.core.logic.notification.annotation.NotificationConfiguration;
import es.sm2.openppm.core.model.enums.StatusNotificationEnum;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Notificationprofile;
import es.sm2.openppm.core.model.impl.Notificationtype;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;

public class NotificationCenterLogic {

	private static final Logger LOGGER = Logger.getLogger(NotificationCenterLogic.class);

	public List<Notificationtype> getNotifications(Company company) {
		
		List<Notificationtype> notificationList = new ArrayList<Notificationtype>();
		
		Set<Class<?>> notificationClasses = CacheStatic.getNotifications();
		
		if (ValidateUtil.isNotNull(notificationClasses)) {

            // Proccess anotations
            Iterator<Class<?>> iterator = notificationClasses.iterator();

            while (iterator.hasNext()) {
                Class<?> notificationClass = iterator.next();

                // Get annotation configuration
                NotificationConfiguration notification = notificationClass.getAnnotation(
                		NotificationConfiguration.class);

                Notificationtype notificationType = 
                		convertNotificationConfigurationToNotificationType(notification);

                notificationList.add(notificationType);
            }
		}
		
		// Recuperar de base de datos
		List<Notificationtype> notificationDbList = new ArrayList<Notificationtype>();
		try {
			notificationDbList = findByCompany(company);
			
			for (Notificationtype notification : notificationDbList) {
				LOGGER.info("NotificationKey (DB): " + notification.getKey());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Rellenar config con datos de BD

		return notificationList;
	}

	/**
	 * Insert a new notification
	 * 
	 * @param notificationType
	 * @return
	 * @throws Exception
	 */
	public Notificationtype insertNotification (Notificationtype notificationType)
	throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			NotificationtypeDAO notificationtypeDAO = new NotificationtypeDAO(session);
			notificationType = notificationtypeDAO.makePersistent(notificationType);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return notificationType;
	}
	
	/**
	 * 
	 * @param notification
	 * @return
	 */
	private Notificationtype convertNotificationConfigurationToNotificationType(
			NotificationConfiguration notification) {
		
        Notificationtype notificationType = new Notificationtype();
        
        // Key
        notificationType.setKey(notification.key());
        
        // Status
        if (notification.active()) {
        	notificationType.setStatus(StatusNotificationEnum.ENABLED.name());
        }
        else {
        	notificationType.setStatus(StatusNotificationEnum.DISABLED.name());	
        }
        
        // Profiles
        Set<Notificationprofile> profilesList = new HashSet<Notificationprofile>();
        for (Resourceprofiles.Profile profile : notification.availableProfiles()) {
        	
        	// Profile name
        	Resourceprofiles resourceProfile = new Resourceprofiles();
        	resourceProfile.setProfileName(profile.getProfileName());
        	resourceProfile.setIdProfile(profile.getID());
        	
        	Notificationprofile notificationProfile = new Notificationprofile();
        	notificationProfile.setResourceProfile(resourceProfile);
        	
        	// Mode
        	notificationProfile.setMode(notification.mode().name());
        	
        	// TODO Status (Mirar en la lista de defaultProfiles)
        	notificationProfile.setStatus(StatusNotificationEnum.DISABLED.name());
        	
        	profilesList.add(notificationProfile);
        }
        notificationType.setProfiles(profilesList);

        return notificationType;
	}
	
	/**
	 * Find NotificationType by Company
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	private List<Notificationtype> findByCompany(Company company) throws Exception {
		
		List<Notificationtype> list = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			NotificationtypeDAO notificationtypeDAO = new NotificationtypeDAO(session);
			list = notificationtypeDAO.findByCompany(company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
}
