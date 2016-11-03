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
 * File: BaseProjectfundingsource.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfundingsource;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Projectfundingsource
 * @see es.sm2.openppm.core.model.base.Projectfundingsource
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectfundingsource
 */
public class BaseProjectfundingsource  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectfundingsource";
	
	public static final String IDPROJFUNDINGSOURCE = "idProjFundingSource";
	public static final String FUNDINGSOURCE = "fundingsource";
	public static final String PROJECT = "project";
	public static final String PERCENTAGE = "percentage";
	public static final String EPIGRAFEEURO = "epigrafeEuro";
	public static final String EPIGRAFEDOLAR = "epigrafeDolar";
	public static final String EUROS = "euros";
	public static final String DOLARES = "dolares";

     private Integer idProjFundingSource;
     private Fundingsource fundingsource;
     private Project project;
     private Integer percentage;
     private String epigrafeEuro;
     private String epigrafeDolar;
     private Double euros;
     private Double dolares;

    public BaseProjectfundingsource() {
    }
    
    public BaseProjectfundingsource(Integer idProjFundingSource) {
    	this.idProjFundingSource = idProjFundingSource;
    }
   
    public Integer getIdProjFundingSource() {
        return this.idProjFundingSource;
    }
    
    public void setIdProjFundingSource(Integer idProjFundingSource) {
        this.idProjFundingSource = idProjFundingSource;
    }
    public Fundingsource getFundingsource() {
        return this.fundingsource;
    }
    
    public void setFundingsource(Fundingsource fundingsource) {
        this.fundingsource = fundingsource;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Integer getPercentage() {
        return this.percentage;
    }
    
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
    public String getEpigrafeEuro() {
        return this.epigrafeEuro;
    }
    
    public void setEpigrafeEuro(String epigrafeEuro) {
        this.epigrafeEuro = epigrafeEuro;
    }
    public String getEpigrafeDolar() {
        return this.epigrafeDolar;
    }
    
    public void setEpigrafeDolar(String epigrafeDolar) {
        this.epigrafeDolar = epigrafeDolar;
    }
    public Double getEuros() {
        return this.euros;
    }
    
    public void setEuros(Double euros) {
        this.euros = euros;
    }
    public Double getDolares() {
        return this.dolares;
    }
    
    public void setDolares(Double dolares) {
        this.dolares = dolares;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectfundingsource) )  { result = false; }
		 else if (other != null) {
		 	Projectfundingsource castOther = (Projectfundingsource) other;
			if (castOther.getIdProjFundingSource().equals(this.getIdProjFundingSource())) { result = true; }
         }
		 return result;
   }


}


