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
 * File: BaseProgram.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Program
 * @see es.sm2.openppm.core.model.base.Program
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Program
 */
public class BaseProgram  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "program";
	
	public static final String IDPROGRAM = "idProgram";
	public static final String PERFORMINGORG = "performingorg";
	public static final String EMPLOYEE = "employee";
	public static final String PROGRAMCODE = "programCode";
	public static final String PROGRAMNAME = "programName";
	public static final String PROGRAMTITLE = "programTitle";
	public static final String DESCRIPTION = "description";
	public static final String PROGRAMDOC = "programDoc";
	public static final String BUDGET = "budget";
	public static final String INITBUDGETYEAR = "initBudgetYear";
	public static final String FINISHBUDGETYEAR = "finishBudgetYear";
	public static final String PROJECTS = "projects";
    public static final String LEARNEDLESSONS = "learnedLessons";

    private Integer idProgram;
    private Performingorg performingorg;
    private Employee employee;
    private String programCode;
    private String programName;
    private String programTitle;
    private String description;
    private String programDoc;
    private Double budget;
    private String initBudgetYear;
    private String finishBudgetYear;
    private Set<Project> projects = new HashSet<Project>(0);
    private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseProgram() {
    }
    
    public BaseProgram(Integer idProgram) {
    	this.idProgram = idProgram;
    }
   
    public Integer getIdProgram() {
        return this.idProgram;
    }
    
    public void setIdProgram(Integer idProgram) {
        this.idProgram = idProgram;
    }
    public Performingorg getPerformingorg() {
        return this.performingorg;
    }
    
    public void setPerformingorg(Performingorg performingorg) {
        this.performingorg = performingorg;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public String getProgramCode() {
        return this.programCode;
    }
    
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }
    public String getProgramName() {
        return this.programName;
    }
    
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    public String getProgramTitle() {
        return this.programTitle;
    }
    
    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getProgramDoc() {
        return this.programDoc;
    }
    
    public void setProgramDoc(String programDoc) {
        this.programDoc = programDoc;
    }
    public Double getBudget() {
        return this.budget;
    }
    
    public void setBudget(Double budget) {
        this.budget = budget;
    }
    public String getInitBudgetYear() {
        return this.initBudgetYear;
    }
    
    public void setInitBudgetYear(String initBudgetYear) {
        this.initBudgetYear = initBudgetYear;
    }
    public String getFinishBudgetYear() {
        return this.finishBudgetYear;
    }
    
    public void setFinishBudgetYear(String finishBudgetYear) {
        this.finishBudgetYear = finishBudgetYear;
    }
    public Set<Project> getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

     /**
      * Getter for property 'learnedLessons'.
      *
      * @return Value for property 'learnedLessons'.
      */
     public Set<LearnedLesson> getLearnedLessons() {
         return learnedLessons;
     }

     /**
      * Setter for property 'learnedLessons'.
      *
      * @param learnedLessons Value to set for property 'learnedLessons'.
      */
     public void setLearnedLessons(Set<LearnedLesson> learnedLessons) {
         this.learnedLessons = learnedLessons;
     }

     @Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Program) )  { result = false; }
		 else if (other != null) {
		 	Program castOther = (Program) other;
			if (castOther.getIdProgram().equals(this.getIdProgram())) { result = true; }
         }
		 return result;
   }


}


