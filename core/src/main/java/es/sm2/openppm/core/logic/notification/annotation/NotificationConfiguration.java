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
 * File: NotificationConfiguration.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.notification.annotation;

import es.sm2.openppm.core.model.impl.Resourceprofiles.Profile;
import es.sm2.openppm.core.logic.notification.configuration.NotificationMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by javier.hernandez on 22/10/2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NotificationConfiguration {

    /* Unique key of notification */
    String key();

    /* Available profiles for these notification */
    Profile[] availableProfiles() default {
            Profile.RESOURCE,
            Profile.PROJECT_MANAGER,
            Profile.INVESTMENT_MANAGER,
            Profile.FUNCTIONAL_MANAGER,
            Profile.PROGRAM_MANAGER,
            Profile.RESOURCE_MANAGER,
            Profile.PMO,
            Profile.SPONSOR,
            Profile.PORFOLIO_MANAGER,
            Profile.STAKEHOLDER
    };

    /* Default Active notification for these profiles */
    Profile[] defaultProfiles() default {};

    /* Default mode of notification */
    NotificationMode mode() default NotificationMode.BOTH;

    /* The notification is active by default */
    boolean active() default true;
}
