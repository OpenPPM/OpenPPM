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
 * File: ProcessListener.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.listener;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.listener.common.ActionListener;
import es.sm2.openppm.core.listener.common.OverrideListener;
import es.sm2.openppm.core.listener.exceptions.ActionListenerException;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by javier.hernandez on 22/10/2014.
 */
public class ProcessListener {

    private static final Logger LOGGER = Logger.getLogger(ProcessListener.class);
    private static ProcessListener ourInstance = new ProcessListener();

    public static ProcessListener getInstance() {
        return ourInstance;
    }

    public static final String EXTRA_DATA_RESOURCE_BUNDLE   = "RESOURCE_BUNDLE";
    public static final String EXTRA_DATA_SETTINGS          = "SETTINGS";
    public static final String EXTRA_DATA_USER              = "USER";
    public static final String EXTRA_DATA_IDPROJECT         = "IDPROJECT";
    public static final String EXTRA_DATA_SESSION           = "SESSION";
    public static final String EXTRA_DATA_INFO              = "INFO";

    private ProcessListener() {
    }

    /**
     * Process specific action listener
     *
     * @param actionListenerClass - Type process listener
     * @param oldInstance - Instance with old values
     * @param newInstance - Instance with new values saved
     * @param extraData - Extra data send by event
     * @param <I> - Type of param action listener
     * @param <S> - Type of listener
     * @throws ActionListenerException - Error process action listener
     */
    public <I,S extends ActionListener<I>> void process(Class<S> actionListenerClass, I oldInstance, I newInstance, Map<String, Object> extraData) throws ActionListenerException {

        // Find subtypes of process listener
        Set<? extends Class<? extends ActionListener>> configuredListenerClasses = CacheStatic.getActionListeners(actionListenerClass);

        // Listeners for execute
        Set<Class<? extends ActionListener>> executeListeners = new HashSet<Class<? extends ActionListener>>();

        if (ValidateUtil.isNotNull(configuredListenerClasses)) {

            // Listeners for exclude
            HashSet<Class<? extends ActionListener>> excludeListeners = new HashSet<Class<? extends ActionListener>>();

            for (Class<? extends ActionListener> configuredListenerClass : configuredListenerClasses) {

                // Add listeners for exclude
                if (configuredListenerClass.isAnnotationPresent(OverrideListener.class)) {

                    LogManager.getLog(getClass()).debug("Exclude listener: " + configuredListenerClass.getSimpleName());

                    excludeListeners.add(configuredListenerClass.getAnnotation(OverrideListener.class).listener());
                }
            }

            for (Class<? extends ActionListener> configuredListenerClass : configuredListenerClasses) {

                if (!excludeListeners.contains(configuredListenerClass)) {
                    executeListeners.add(configuredListenerClass);
                }
            }
        }

        if (ValidateUtil.isNotNull(executeListeners)) {

            // Iterate for each action listener
            for (Class<? extends ActionListener> configuredListenerClass : executeListeners) {

                try {

                    // Call to action Listener
                    ActionListener actionListener = configuredListenerClass.newInstance();
                    actionListener.setExtraData(extraData);
                    actionListener.run(oldInstance, newInstance);

                } catch (InstantiationException e) {
                    LOGGER.error("Instantiation action listener: "+configuredListenerClass.getName() , e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Illegal access to action listener: "+configuredListenerClass.getName() , e);
                }
            }
        }
    }
}
