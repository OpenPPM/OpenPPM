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
 * File: BaseCalendarbaseexceptions.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Calendarbaseexceptions;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Calendarbaseexceptions
 * @see es.sm2.openppm.core.model.base.Calendarbaseexceptions
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Calendarbaseexceptions
 */
public class BaseCalendarbaseexceptions  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "calendarbaseexceptions";
	
	public static final String IDCALENDARBASEEXCEPTION = "idCalendarBaseException";
	public static final String CALENDARBASE = "calendarbase";
	public static final String STARTDATE = "startDate";
	public static final String FINISHDATE = "finishDate";
	public static final String DESCRIPTION = "description";

     private Integer idCalendarBaseException;
     private Calendarbase calendarbase;
     private Date startDate;
     private Date finishDate;
     private String description;

    public BaseCalendarbaseexceptions() {
    }
    
    public BaseCalendarbaseexceptions(Integer idCalendarBaseException) {
    	this.idCalendarBaseException = idCalendarBaseException;
    }
   
    public Integer getIdCalendarBaseException() {
        return this.idCalendarBaseException;
    }
    
    public void setIdCalendarBaseException(Integer idCalendarBaseException) {
        this.idCalendarBaseException = idCalendarBaseException;
    }
    public Calendarbase getCalendarbase() {
        return this.calendarbase;
    }
    
    public void setCalendarbase(Calendarbase calendarbase) {
        this.calendarbase = calendarbase;
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
		 else if (!(other instanceof Calendarbaseexceptions) )  { result = false; }
		 else if (other != null) {
		 	Calendarbaseexceptions castOther = (Calendarbaseexceptions) other;
			if (castOther.getIdCalendarBaseException().equals(this.getIdCalendarBaseException())) { result = true; }
         }
		 return result;
   }


}


