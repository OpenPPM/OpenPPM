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
 * File: OpenppmUtil.java
 * Create User: jordi.ripoll
 * Create Date: 28/04/2015 15:36:48
 */

package es.sm2.openppm.core.utils;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.List;


public class OpenppmUtil {

	public final static Logger LOGGER = Logger.getLogger(OpenppmUtil.class);
	
	private OpenppmUtil() {}

	/**
	 * Calculate ES
	 * @param list
	 * @param followup
	 * @return
	 * @throws Exception
	 */
	public static Double getES(List<Projectfollowup> list, Projectfollowup followup, Project project, Session session) throws Exception {

		Integer minWorkdays = null;
		Double minPv = null;

		Projectfollowup f0 = null;
		Projectfollowup f1 = null;

		Double ev = followup.getEv();
		Double es = null;

		if (ev != null) {

			// Get Followup fot T0, PV0 and Min Days to date
			for (Projectfollowup item : list) {
				if (item.getPv()!= null) {
					if (minPv == null || (item.getPv() >= minPv && item.getPv() <= ev)) {
						minPv = item.getPv();
						f0 = item;
					}

					// Min Days to Date
					Integer daysToDate = item.getDaysToDate(project, session);
					daysToDate = (daysToDate == null?0:daysToDate);

					if (minWorkdays == null || minWorkdays > daysToDate) {
						minWorkdays = daysToDate;
					}
				}
			}

			// Get followup for T1 PV1
			for (Projectfollowup item : list) {
				if (item.getPv()!= null &&
						((f1 == null && f0.getPv() < item.getPv()) ||
						(f1 != null && f0.getPv() < item.getPv() && f1.getPv() > item.getPv()))) {
					f1 = item;
				}
			}

			if (minPv != null && minPv > ev) { es = (minWorkdays == null?null:minWorkdays.doubleValue()); }
			else {
				double t0 = (f0 != null && f0.getDaysToDate(project, session) != null?f0.getDaysToDate(project, session):0);
				double t1 = (f1 != null && f1.getDaysToDate(project, session) != null?f1.getDaysToDate(project, session):0);

				double pv0 = (f0 != null?f0.getPv():0);
				double pv1 = (f1 != null?f1.getPv():0);

				if ((pv1-pv0) != 0) {
					es = t0+(t1-t0)*(ev-pv0)/(pv1-pv0);
				}
			}
		}
		return es;
	}
}
