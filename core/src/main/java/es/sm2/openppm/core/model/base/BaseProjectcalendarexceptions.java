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
 * File: BaseProjectcalendarexceptions.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.core.model.impl.Projectcalendarexceptions;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Projectcalendarexceptions
 * @see es.sm2.openppm.core.model.base.Projectcalendarexceptions
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectcalendarexceptions
 */
public class BaseProjectcalendarexceptions  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectcalendarexceptions";
	
	public static final String IDPROJECTCALENDAREXCEPTION = "idProjectCalendarException";
	public static final String PROJECTCALENDAR = "projectcalendar";
	public static final String STARTDATE = "startDate";
	public static final String FINISHDATE = "finishDate";
	public static final String DESCRIPTION = "description";

     private Integer idProjectCalendarException;
     private Projectcalendar projectcalendar;
     private Date startDate;
     private Date finishDate;
     private String description;

    public BaseProjectcalendarexceptions() {
    }
    
    public BaseProjectcalendarexceptions(Integer idProjectCalendarException) {
    	this.idProjectCalendarException = idProjectCalendarException;
    }
   
    public Integer getIdProjectCalendarException() {
        return this.idProjectCalendarException;
    }
    
    public void setIdProjectCalendarException(Integer idProjectCalendarException) {
        this.idProjectCalendarException = idProjectCalendarException;
    }
    public Projectcalendar getProjectcalendar() {
        return this.projectcalendar;
    }
    
    public void setProjectcalendar(Projectcalendar projectcalendar) {
        this.projectcalendar = projectcalendar;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getFinishDate() {
        return this.finishDate;
    }
    
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectcalendarexceptions) )  { result = false; }
		 else if (other != null) {
		 	Projectcalendarexceptions castOther = (Projectcalendarexceptions) other;
			if (castOther.getIdProjectCalendarException().equals(this.getIdProjectCalendarException())) { result = true; }
         }
		 return result;
   }


}


