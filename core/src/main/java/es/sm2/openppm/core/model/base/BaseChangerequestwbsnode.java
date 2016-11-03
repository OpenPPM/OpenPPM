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
 * File: BaseChangerequestwbsnode.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Changerequestwbsnode;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Changerequestwbsnode
 * @see es.sm2.openppm.core.model.base.Changerequestwbsnode
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Changerequestwbsnode
 */
public class BaseChangerequestwbsnode  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "changerequestwbsnode";
	
	public static final String IDCHANGEREQUESTWBSNODE = "idChangeRequestWbsnode";
	public static final String WBSNODE = "wbsnode";
	public static final String CHANGECONTROL = "changecontrol";
	public static final String ESTIMATEDEFFORT = "estimatedEffort";
	public static final String ESTIMATEDCOST = "estimatedCost";

     private Integer idChangeRequestWbsnode;
     private Wbsnode wbsnode;
     private Changecontrol changecontrol;
     private Double estimatedEffort;
     private Double estimatedCost;

    public BaseChangerequestwbsnode() {
    }
    
    public BaseChangerequestwbsnode(Integer idChangeRequestWbsnode) {
    	this.idChangeRequestWbsnode = idChangeRequestWbsnode;
    }
   
    public Integer getIdChangeRequestWbsnode() {
        return this.idChangeRequestWbsnode;
    }
    
    public void setIdChangeRequestWbsnode(Integer idChangeRequestWbsnode) {
        this.idChangeRequestWbsnode = idChangeRequestWbsnode;
    }
    public Wbsnode getWbsnode() {
        return this.wbsnode;
    }
    
    public void setWbsnode(Wbsnode wbsnode) {
        this.wbsnode = wbsnode;
    }
    public Changecontrol getChangecontrol() {
        return this.changecontrol;
    }
    
    public void setChangecontrol(Changecontrol changecontrol) {
        this.changecontrol = changecontrol;
    }
    public Double getEstimatedEffort() {
        return this.estimatedEffort;
    }
    
    public void setEstimatedEffort(Double estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }
    public Double getEstimatedCost() {
        return this.estimatedCost;
    }
    
    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Changerequestwbsnode) )  { result = false; }
		 else if (other != null) {
		 	Changerequestwbsnode castOther = (Changerequestwbsnode) other;
			if (castOther.getIdChangeRequestWbsnode().equals(this.getIdChangeRequestWbsnode())) { result = true; }
         }
		 return result;
   }


}


