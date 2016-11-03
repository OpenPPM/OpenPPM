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
 * File: EmailUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import es.sm2.openppm.core.logic.setting.EmailType;
import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.exceptions.DestinationMailNotFoundException;
import es.sm2.openppm.core.exceptions.FromMailNotFoundException;
import es.sm2.openppm.core.exceptions.MailException;
import es.sm2.openppm.core.plugin.PluginTemplate;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

/**
 * Prepare and send email.
 */
public class EmailUtil {

	public final static Logger LOGGER = Logger.getLogger(EmailUtil.class);
	
	private static final String HEAD = "[TALAIA] ";
	private static final String CONTENT = "text/html; charset=utf-8";
	
	private String from;
	private String to;
	private String cc;
	private String ccoo;
	private String subject;
	private String bodyText;
	private Properties properties;
	private List<File> files;
	private String user;
	private String password;
    private String imgSignature;
    private String imgSignatureTemplate;
    
    /**
     * Constructor. Set from and to
     * @param from
     * @param to
     */
    public EmailUtil(HashMap<String, String> settings, String from, String to) {
    	
    	this.from		= ValidateUtil.isNullCh(from, SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_NO_REPLY, Settings.DEFAULT_MAIL_SMTP_NO_REPLY));
    	this.to			= to;
    	this.subject	= StringPool.BLANK;
    	this.bodyText	= StringPool.BLANK;
    	this.user		= SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_USER, Settings.DEFAULT_MAIL_SMTP_USER);
    	this.password	= SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_PASS, Settings.DEFAULT_MAIL_SMTP_PASS);
        this.imgSignature = SettingUtil.getString(settings, EmailType.EMAIL_SIGNATURE);
        this.imgSignatureTemplate = SettingUtil.getString(settings, EmailType.EMAIL_SIGNATURE_TEMPLATE);

    	this.properties = new Properties();
    	this.properties.put("mail.transport.protocol", "smtp");
    	this.properties.put("mail.smtp.user", this.from);
    	this.properties.put("mail.smtp.host", SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_HOST, Settings.DEFAULT_MAIL_SMTP_HOST));
        this.properties.put("mail.smtp.auth", "false");
    	this.properties.put("mail.smtp.port", SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_PORT, Settings.DEFAULT_MAIL_SMTP_PORT));
        this.properties.put("mail.smtp.starttls.enable", SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_TLS, Settings.DEFAULT_MAIL_SMTP_TLS));
    }
    
    public void setSubject(String subject) {
		this.subject = HEAD+subject;
	}
    
    /**
     * Set body content by template
     * 
     * @param bodyText
     */
    public void setBodyText(String bodyText) {
    	
		try {

			// Create data for send to template
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("bodyText", bodyText);
			data.put("versionAppName", Settings.VERSION_NAME_APP);
			data.put("versionApp", Settings.VERSION_APP);
            data.put("imgSignature", getImgSignature());
			
			// Call template and set body
			this.bodyText = PluginTemplate.getTemplate(imgSignatureTemplate, data);
		}
		catch (Exception e) {
			LOGGER.warn("Template not found for send mail: "+e.getMessage());
			this.bodyText = bodyText;
		}
	}
    
    public void addFile(File file) {
    	if (files == null) { files = new ArrayList<File>(); }
    	files.add(file);
    }
    
    /**
     * Send Email
     * 
     * @throws MailException
     * @throws AddressException
     * @throws MessagingException
     */
    public void send() throws MailException, AddressException, MessagingException {
    	
    	if (this.to == null || StringPool.BLANK.equals(this.to)) {
    		throw new DestinationMailNotFoundException();
    	}
    	if (this.from == null || StringPool.BLANK.equals(this.from)) {
    		throw new FromMailNotFoundException();
    	}
    	
    	javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.from));
        
		// Add destination TO
        addDestination(message, getTo(), RecipientType.TO);
        
		// Add destination CC
        addDestination(message, getCc(), RecipientType.CC);
        
		// Add destination CCOO
        addDestination(message, getCcoo(), RecipientType.BCC);
        
        message.setSubject(subject);
        message.setSentDate(new Date());
        
        Multipart multipart = new MimeMultipart();
        
        // Set the email message text.
        MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setContent(bodyText, CONTENT);
        multipart.addBodyPart(messagePart);
        
        // Attach files
        if (files != null) {
        	MimeBodyPart filePart = new MimeBodyPart();
        	for (File file : files) {
		        FileDataSource fds = new FileDataSource(file);
		        filePart.setDataHandler(new DataHandler(fds));
		        filePart.setFileName(fds.getName());
        	}
        	multipart.addBodyPart(filePart);
        }
        
        
        message.setContent(multipart);
        
        Transport transport = session.getTransport("smtp");
        
        if (ValidateUtil.isNotNull(getUser()) && ValidateUtil.isNotNull(getPassword())) {
        	
        	transport.connect(getUser(), getPassword());
        }
        else {
        	transport.connect();
        }
        
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();
        
        LOGGER.info("Email send from "+this.from+" to "+ this.to +" CC: "+this.getCc() +" CCOO: "+getCcoo());
    }

	/**
	 * Add mails destination
	 * 
	 * @param message
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private void addDestination(MimeMessage message, String emails, RecipientType recipientType) throws AddressException, MessagingException {
		
		if (ValidateUtil.isNotNull(emails)) {
			
			// List of mails
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			
			// Add mails to list
			for (String mail : emails.split(";")) {
	
				if (ValidateUtil.isNotNull(mail)) {
					addressList.add(new InternetAddress(mail));
				}
	
			}
			
			// Set mails
			InternetAddress[] address = addressList.toArray(new InternetAddress[addressList.size()]);
			message.setRecipients(recipientType, address);
		}
	}
    
    /**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the ccoo
	 */
	public String getCcoo() {
		return ccoo;
	}

	/**
	 * @param ccoo the ccoo to set
	 */
	public void setCcoo(String ccoo) {
		this.ccoo = ccoo;
	}

    /**
     *
     * @return
     */
    public String getImgSignature() {
        return imgSignature;
    }

    /**
     *
     * @param imgSignature
     */
    public void setImgSignature(String imgSignature) {
        this.imgSignature = imgSignature;
    }

    public String getImgSignatureTemplate() {
        return imgSignatureTemplate;
    }

    public void setImgSignatureTemplate(String imgSignatureTemplate) {
        this.imgSignatureTemplate = imgSignatureTemplate;
    }
}
