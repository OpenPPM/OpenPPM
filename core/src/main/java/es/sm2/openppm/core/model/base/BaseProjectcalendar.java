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
 * File: BaseProjectcalendar.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.core.model.impl.Projectcalendarexceptions;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Projectcalendar
 * @see es.sm2.openppm.core.model.base.Projectcalendar
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectcalendar
 */
public class BaseProjectcalendar  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectcalendar";
	
	public static final String IDPROJECTCALENDAR = "idProjectCalendar";
	public static final String CALENDARBASE = "calendarbase";
	public static final String WEEKSTART = "weekStart";
	public static final String FISCALYEARSTART = "fiscalYearStart";
	public static final String STARTTIME1 = "startTime1";
	public static final String STARTTIME2 = "startTime2";
	public static final String ENDTIME1 = "endTime1";
	public static final String ENDTIME2 = "endTime2";
	public static final String HOURSDAY = "hoursDay";
	public static final String HOURSWEEK = "hoursWeek";
	public static final String DAYSMONTH = "daysMonth";
	public static final String PROJECTS = "projects";
	public static final String PROJECTCALENDAREXCEPTIONSES = "projectcalendarexceptionses";

     private Integer idProjectCalendar;
     private Calendarbase calendarbase;
     private Integer weekStart;
     private Integer fiscalYearStart;
     private Double startTime1;
     private Double startTime2;
     private Double endTime1;
     private Double endTime2;
     private Double hoursDay;
     private Double hoursWeek;
     private Integer daysMonth;
     private Set<Project> projects = new HashSet<Project>(0);
     private Set<Projectcalendarexceptions> projectcalendarexceptionses = new HashSet<Projectcalendarexceptions>(0);

    public BaseProjectcalendar() {
    }
    
    public BaseProjectcalendar(Integer idProjectCalendar) {
    	this.idProjectCalendar = idProjectCalendar;
    }
   
    public Integer getIdProjectCalendar() {
        return this.idProjectCalendar;
    }
    
    public void setIdProjectCalendar(Integer idProjectCalendar) {
        this.idProjectCalendar = idProjectCalendar;
    }
    public Calendarbase getCalendarbase() {
        return this.calendarbase;
    }
    
    public void setCalendarbase(Calendarbase calendarbase) {
        this.calendarbase = calendarbase;
    }
    public Integer getWeekStart() {
        return this.weekStart;
    }
    
    public void setWeekStart(Integer weekStart) {
        this.weekStart = weekStart;
    }
    public Integer getFiscalYearStart() {
        return this.fiscalYearStart;
    }
    
    public void setFiscalYearStart(Integer fiscalYearStart) {
        this.fiscalYearStart = fiscalYearStart;
    }
    public Double getStartTime1() {
        return this.startTime1;
    }
    
    public void setStartTime1(Double startTime1) {
        this.startTime1 = startTime1;
    }
    public Double getStartTime2() {
        return this.startTime2;
    }
    
    public void setStartTime2(Double startTime2) {
        this.startTime2 = startTime2;
    }
    public Double getEndTime1() {
        return this.endTime1;
    }
    
    public void setEndTime1(Double endTime1) {
        this.endTime1 = endTime1;
    }
    public Double getEndTime2() {
        return this.endTime2;
    }
    
    public void setEndTime2(Double endTime2) {
        this.endTime2 = endTime2;
    }
    public Double getHoursDay() {
        return this.hoursDay;
    }
    
    public void setHoursDay(Double hoursDay) {
        this.hoursDay = hoursDay;
    }
    public Double getHoursWeek() {
        return this.hoursWeek;
    }
    
    public void setHoursWeek(Double hoursWeek) {
        this.hoursWeek = hoursWeek;
    }
    public Integer getDaysMonth() {
        return this.daysMonth;
    }
    
    public void setDaysMonth(Integer daysMonth) {
        this.daysMonth = daysMonth;
    }
    public Set<Project> getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    public Set<Projectcalendarexceptions> getProjectcalendarexceptionses() {
        return this.projectcalendarexceptionses;
    }
    
    public void setProjectcalendarexceptionses(Set<Projectcalendarexceptions> projectcalendarexceptionses) {
        this.projectcalendarexceptionses = projectcalendarexceptionses;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectcalendar) )  { result = false; }
		 else if (other != null) {
		 	Projectcalendar castOther = (Projectcalendar) other;
			if (castOther.getIdProjectCalendar().equals(this.getIdProjectCalendar())) { result = true; }
         }
		 return result;
   }


}


