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
 * File: UpdateTeammemberAbstractListener.java
 * Create User: jordi.ripoll
 * Create Date: 14/04/2015 13:32:05
 */

package es.sm2.openppm.core.listener.impl.project;

import es.sm2.openppm.core.listener.common.ActionListener;
import es.sm2.openppm.core.listener.common.GenericActionListener;
import es.sm2.openppm.core.listener.exceptions.ActionListenerException;
import es.sm2.openppm.core.model.impl.Projectactivity;

/**
 * Created by javier.hernandez on 22/10/2014.
 */
public abstract class UpdateTeammemberAbstractListener extends GenericActionListener implements ActionListener<Projectactivity> {

    /**
     * Process action listener
     *
     * @param oldInstance - Instance with old values
     * @param newInstance - Instance with new values saved
     * @throws es.sm2.openppm.core.listener.exceptions.ActionListenerException - Error process action listener
     */
    public abstract void run(Projectactivity oldInstance, Projectactivity newInstance) throws ActionListenerException;
}
