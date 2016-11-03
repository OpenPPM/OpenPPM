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
 * File: BaseProjectactivity.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Wbsnode;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Projectactivity
 * @see es.sm2.openppm.core.model.base.Projectactivity
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectactivity
 */
public class BaseProjectactivity  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectactivity";
	
	public static final String IDACTIVITY = "idActivity";
	public static final String WBSNODE = "wbsnode";
	public static final String PROJECT = "project";
	public static final String ACTIVITYNAME = "activityName";
	public static final String WBSDICTIONARY = "wbsdictionary";
	public static final String PLANINITDATE = "planInitDate";
	public static final String ACTUALINITDATE = "actualInitDate";
	public static final String PLANENDDATE = "planEndDate";
	public static final String ACTUALENDDATE = "actualEndDate";
	public static final String EV = "ev";
	public static final String PV = "pv";
	public static final String AC = "ac";
	public static final String POC = "poc";
	public static final String COMMENTSPOC = "commentsPoc";
	public static final String COMMENTSDATES = "commentsDates";
	public static final String ACTIVITYSELLERS = "activitysellers";
	public static final String TEAMMEMBERS = "teammembers";
	public static final String TIMESHEETS = "timesheets";
	public static final String MILESTONESES = "milestoneses";

     private Integer idActivity;
     private Wbsnode wbsnode;
     private Project project;
     private String activityName;
     private String wbsdictionary;
     private Date planInitDate;
     private Date actualInitDate;
     private Date planEndDate;
     private Date actualEndDate;
     private Double ev;
     private Double pv;
     private Double ac;
     private Double poc;
     private String commentsPoc;
     private String commentsDates;
     private Set<Activityseller> activitysellers = new HashSet<Activityseller>(0);
     private Set<Teammember> teammembers = new HashSet<Teammember>(0);
     private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
     private Set<Milestones> milestoneses = new HashSet<Milestones>(0);

    public BaseProjectactivity() {
    }
    
    public BaseProjectactivity(Integer idActivity) {
    	this.idActivity = idActivity;
    }
   
    public Integer getIdActivity() {
        return this.idActivity;
    }
    
    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }
    public Wbsnode getWbsnode() {
        return this.wbsnode;
    }
    
    public void setWbsnode(Wbsnode wbsnode) {
        this.wbsnode = wbsnode;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getActivityName() {
        return this.activityName;
    }
    
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    public String getWbsdictionary() {
        return this.wbsdictionary;
    }
    
    public void setWbsdictionary(String wbsdictionary) {
        this.wbsdictionary = wbsdictionary;
    }
    public Date getPlanInitDate() {
        return this.planInitDate;
    }
    
    public void setPlanInitDate(Date planInitDate) {
        this.planInitDate = planInitDate;
    }
    public Date getActualInitDate() {
        return this.actualInitDate;
    }
    
    public void setActualInitDate(Date actualInitDate) {
        this.actualInitDate = actualInitDate;
    }
    public Date getPlanEndDate() {
        return this.planEndDate;
    }
    
    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }
    public Date getActualEndDate() {
        return this.actualEndDate;
    }
    
    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }
    public Double getEv() {
        return this.ev;
    }
    
    public void setEv(Double ev) {
        this.ev = ev;
    }
    public Double getPv() {
        return this.pv;
    }
    
    public void setPv(Double pv) {
        this.pv = pv;
    }
    public Double getAc() {
        return this.ac;
    }
    
    public void setAc(Double ac) {
        this.ac = ac;
    }
    public Double getPoc() {
        return this.poc;
    }
    
    public void setPoc(Double poc) {
        this.poc = poc;
    }
    public String getCommentsPoc() {
        return this.commentsPoc;
    }
    
    public void setCommentsPoc(String commentsPoc) {
        this.commentsPoc = commentsPoc;
    }
    public String getCommentsDates() {
        return this.commentsDates;
    }
    
    public void setCommentsDates(String commentsDates) {
        this.commentsDates = commentsDates;
    }
    public Set<Activityseller> getActivitysellers() {
        return this.activitysellers;
    }
    
    public void setActivitysellers(Set<Activityseller> activitysellers) {
        this.activitysellers = activitysellers;
    }
    public Set<Teammember> getTeammembers() {
        return this.teammembers;
    }
    
    public void setTeammembers(Set<Teammember> teammembers) {
        this.teammembers = teammembers;
    }
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }
    
    public void setTimesheets(Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }
    public Set<Milestones> getMilestoneses() {
        return this.milestoneses;
    }
    
    public void setMilestoneses(Set<Milestones> milestoneses) {
        this.milestoneses = milestoneses;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectactivity) )  { result = false; }
		 else if (other != null) {
		 	Projectactivity castOther = (Projectactivity) other;
			if (castOther.getIdActivity().equals(this.getIdActivity())) { result = true; }
         }
		 return result;
   }


}


