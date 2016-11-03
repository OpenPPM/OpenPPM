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
 * File: BaseFundingsource.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Projectfundingsource;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Fundingsource
 * @see es.sm2.openppm.core.model.base.BaseFundingsource
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Fundingsource
 */
public class BaseFundingsource  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "fundingsource";
	
	public static final String IDFUNDINGSOURCE = "idFundingSource";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String PROJECTFUNDINGSOURCES = "projectfundingsources";
     public static final String LEARNEDLESSONS = "learnedLessons";

     private Integer idFundingSource;
     private Company company;
     private String name;
     private Set<Projectfundingsource> projectfundingsources = new HashSet<Projectfundingsource>(0);
     private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseFundingsource() {
    }
    
    public BaseFundingsource(Integer idFundingSource) {
    	this.idFundingSource = idFundingSource;
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

     public Integer getIdFundingSource() {
        return this.idFundingSource;
    }
    
    public void setIdFundingSource(Integer idFundingSource) {
        this.idFundingSource = idFundingSource;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set<Projectfundingsource> getProjectfundingsources() {
        return this.projectfundingsources;
    }
    
    public void setProjectfundingsources(Set<Projectfundingsource> projectfundingsources) {
        this.projectfundingsources = projectfundingsources;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Fundingsource) )  { result = false; }
		 else if (other != null) {
		 	Fundingsource castOther = (Fundingsource) other;
			if (castOther.getIdFundingSource().equals(this.getIdFundingSource())) { result = true; }
         }
		 return result;
   }


}


