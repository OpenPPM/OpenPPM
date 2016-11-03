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
 * Module: utils
 * File: SessionFactoryUtil.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.hibernate;

import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.annotation.HibernateConfig;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.util.ConfigHelper;
import org.reflections.Reflections;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author hennebrueder This class garanties that only one single SessionFactory is instanciated and
 *         that the configuration is done thread safe as singleton. Actually it only wraps the
 *         Hibernate SessionFactory. You are free to use any kind of JTA or Thread
 *         transactionFactories.
 */
public final class SessionFactoryUtil {

	/** The single instance of hibernate SessionFactory */
	private static org.hibernate.SessionFactory sessionFactory;

	/**
	 * disable contructor to guaranty a single instance
	 */
	private SessionFactoryUtil() {
	}

	static {
		
		// Logger
		Logger logger = LogManager.getLog(SessionFactoryUtil.class);

        // Load configurations
        List<String> configurations = getConfigurations();

        if (ValidateUtil.isNotNull(configurations)) {

            // Add default file
            configurations.add(0,"hibernate.cfg.xml");

            // Configurations for merge
            InputStream[] files = new InputStream[configurations.size()];

            // Merge file and configure it
            for (int i = 0; i < configurations.size(); i++) {

                String configFile = configurations.get(i);

                try {

                    files[i] = ConfigHelper.getResourceAsStream(configFile);
                    logger.debug("Load config file: "+configFile);
                }
                catch (Exception e) {
                    logger.error("Loading config file: " + configFile, e);
                }
            }

            try {
                // Merge configuration file
                Document doc = merge("/hibernate-configuration/session-factory", files);

                sessionFactory = new Configuration().configure(doc).buildSessionFactory();

            } catch (Exception e) {
                logger.error("Not merged configurations files: "+configurations, e);
            }
		}
		else {
			logger.info("Hibernate file from CORE");
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
	}

    /**
     * Get annotations configurations
     *
     * @return
     */
    private static List<String> getConfigurations() {

        Logger logger = LogManager.getLog(SessionFactoryUtil.class);

        List<String> configurations = new ArrayList<String>();

        // Find in package annotation
        Reflections reflections = new Reflections("es.sm2");
        Set<Class<?>> configuredClasses = reflections.getTypesAnnotatedWith(HibernateConfig.class);

        if (ValidateUtil.isNotNull(configuredClasses)) {

            // Proccess anotations
            for (Class<?> configuredClass : configuredClasses) {

                // Get annotation configuration
                HibernateConfig annotation = configuredClass.getAnnotation(HibernateConfig.class);

                if (ValidateUtil.isNotNull(annotation.files())) {

                    // Add files configurations
                    for (String configurationFile : annotation.files()) {

                        if (ValidateUtil.isNotNull(configurationFile)) {

                            logger.info("Hibernate file from HibernateConfig Annotation: "+configurationFile);
                            configurations.add(configurationFile);
                        }
                    }
                }
            }
        }

        return configurations;
    }

    private static Document merge(String expression, InputStream... files) throws Exception {

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression compiledExpression = xpath.compile(expression);
        return merge(compiledExpression, files);
    }

    private static Document merge(XPathExpression expression, InputStream... files) throws Exception {

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document base = docBuilder.parse(files[0]);

        Node results = (Node) expression.evaluate(base, XPathConstants.NODE);

        if (results == null) {
            throw new IOException(files[0] + ": expression does not evaluate to node");
        }

        for (int i = 1; i < files.length; i++) {

            Document merge = docBuilder.parse(files[i]);
            Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);

            while (nextResults.hasChildNodes()) {

                Node kid = nextResults.getFirstChild();
                nextResults.removeChild(kid);
                kid = base.importNode(kid, true);
                results.appendChild(kid);
            }
        }

        return base;
    }

	public static SessionFactory getInstance() {
		return sessionFactory;
	}

	/**
	 * Opens a session and will not bind it to a session context
	 * 
	 * @return the session
	 */
	public Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 * Returns a session from the session context. If there is no session in the context it opens a
	 * session, stores it in the context and returns it. This factory is intended to be used with a
	 * hibernate.cfg.xml including the following property <property
	 * name="current_session_context_class">thread</property> This would return the current open
	 * session or if this does not exist, will create a new session
	 * 
	 * @return the session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * closes the session factory
	 */
	public static void close() {
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;
	}
}