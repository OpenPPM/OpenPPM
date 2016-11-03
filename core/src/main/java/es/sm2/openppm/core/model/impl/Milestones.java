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
 * File: Milestones.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseMilestones;
import java.util.Date;


/**
 * Model class Milestones.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Milestones extends BaseMilestones {

	private static final long serialVersionUID = 1L;
	
	public static final String ORDER_DATE = "orderDate";
	
	private Date orderDate;

    public Milestones() {
		super();
    }
    public Milestones(Integer idMilestone) {
		super(idMilestone);
    }
    
	public enum MilestonePending {	
		YES("yes"),
		NO("no"),
		ALL("all");
		
		private String text;
		
		private MilestonePending(String text) {
			this.text = text;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}
	}
	
	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		
		if (orderDate == null) {
			orderDate = this.getPlanned();
			
			if (this.getAchieved() != null) {
				orderDate = this.getAchieved();
			}
			else if (this.getAchieved() == null || this.getEstimatedDate() != null) {
				orderDate = this.getEstimatedDate();
			}
		}
		
		return orderDate;
	}
    
}

