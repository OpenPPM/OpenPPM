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
 * File: CacheStatic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.cache;

import es.sm2.openppm.core.cache.exceptions.CacheException;
import es.sm2.openppm.core.common.ApplicationSetting;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.listener.common.ActionListener;
import es.sm2.openppm.core.logic.notification.annotation.NotificationConfiguration;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.core.plugin.annotations.PluginLoad;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.annotations.ReportConfiguration;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by javier.hernandez on 01/03/2015.
 */
public class CacheStatic {

    private static final Reflections reflections = new Reflections(GenericAction.PACKAGE);

    private static Set<Class<?>> pluginLoads;
    private static Set<Class<?>> pluginActions;
    private static Set<Class<? extends GenericSetting>> configurationSettings;
    private static Set<Class<? extends ApplicationSetting>> applicationSettingList;
    private static Set<Class<? extends GenerateReportInterface>> generateReports;
    private static HashMap <Class<?>,PluginAction> pluginAction = new HashMap<Class<?>, PluginAction>();
    private static HashMap<Class<? extends ActionListener>, Set<? extends Class<? extends ActionListener>>> actionListeners = new HashMap<Class<? extends ActionListener>, Set<? extends Class<? extends ActionListener>>>();
    private static Set<Class<?>> notifications;
    
    /**
     * Get classes configured for plugin load
     *
     * @return
     */
    public synchronized static Set<Class<?>> getPluginLoads() {

        // Initialize cache for plugin load
        if (pluginLoads == null) {
            pluginLoads = reflections.getTypesAnnotatedWith(PluginLoad.class);
        }

        return pluginLoads;
    }

    /**
     * Get classes configured for plugin load
     *
     * @return
     */
    public synchronized static Set<Class<? extends GenericSetting>> getConfigurationSettings() {

        // Initialize cache for plugin load
        if (configurationSettings == null) {
            configurationSettings = reflections.getSubTypesOf(GenericSetting.class);
        }

        return configurationSettings;
    }

    /**
     * Get classes configured for application setting
     *
     * @return
     */
    public synchronized static Set<Class<? extends ApplicationSetting>> getApplicationSettingList() {

        // Initialize cache
        if (applicationSettingList == null) {
            applicationSettingList = reflections.getSubTypesOf(ApplicationSetting.class);
        }

        return applicationSettingList;
    }

    /**
     * Get instance for generate report
     *
     * @param reportType
     * @return
     * @throws CacheException
     */
    public synchronized static GenerateReportInterface getGenerateReport(ReportType reportType) throws CacheException {

        // Initialize cache for generate reports
        if (generateReports == null) {
            generateReports = reflections.getSubTypesOf(GenerateReportInterface.class);
        }

        Class<? extends GenerateReportInterface> generateReportClass = null;

        if (ValidateUtil.isNotNull(generateReports)) {

            for (Class<? extends GenerateReportInterface> generateReport : generateReports) {

                // Report configuration
                ReportConfiguration configuration = generateReport.getAnnotation(ReportConfiguration.class);

                // Find type of report needed
                if (configuration.type().equals(reportType)) {

                    if (generateReportClass == null) {

                        // Report find
                        generateReportClass = generateReport;
                    }
                    else {
                        LogManager.getLog(CacheStatic.class).warn("Generate class " + generateReport.getName()
                                + " is ignored for type of report:" + reportType.name()
                                + ". Only one type class generator for application");
                    }
                }
            }
        }

        // Response generator
        GenerateReportInterface generatorBean = null;

        if (generateReportClass != null) {

            try {

                // Create new instance generator class
                generatorBean = generateReportClass.newInstance();

            } catch (InstantiationException e) {
                throw  new CacheException(e);
            } catch (IllegalAccessException e) {
                throw  new CacheException(e);
            }
        }

        return generatorBean;
    }

    /**
     * Get classes configured for plugin action
     *
     * @return
     */
    public synchronized static Set<Class<?>> getPluginActions() {

        // Initialize cache for plugin load
        if (pluginActions == null) {
            pluginActions = reflections.getTypesAnnotatedWith(PluginAction.class);
        }

        return pluginActions;
    }

    /**
     * Get configuration plugin action from annotated class
     *
     * @param annotatedClass
     * @return
     */
    public synchronized static PluginAction getPluginAction(Class<?> annotatedClass) {

        // Find class in cache
        PluginAction pluginActionClass = pluginAction.get(annotatedClass);

        if (pluginActionClass == null) {

            // Find plugin action class from reflect
            annotatedClass.getAnnotation(PluginLoad.class);
            pluginActionClass = annotatedClass.getAnnotation(PluginAction.class);

            // Push to static cache
            pluginAction.put(annotatedClass, pluginActionClass);
        }

        return pluginActionClass;
    }

    /**
     * Get listeners form parent class
     *
     * @param parentClass
     * @return
     */
    public synchronized static <S> Set<? extends Class<? extends ActionListener>> getActionListeners(Class<? extends ActionListener> parentClass) {

        // Find class in cache
        Set<? extends Class<? extends ActionListener>> listeners = actionListeners.get(parentClass);

        if (listeners == null) {

            // Find plugin sub types of parent class from reflect
            parentClass.getAnnotation(PluginLoad.class);
            listeners = reflections.getSubTypesOf(parentClass);

            // Push to static cache
            actionListeners.put(parentClass, listeners);
        }

        return listeners;
    }
    
    /**
     * Get classes configured for notification
     *
     * @return
     */
    public synchronized static Set<Class<?>> getNotifications() {

        // Initialize cache for notification load
        if (notifications == null) {
            notifications = reflections.getTypesAnnotatedWith(NotificationConfiguration.class);
        }

        return notifications;
    }

}
