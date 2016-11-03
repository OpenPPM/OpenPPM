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
 * File: NodeMSP.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Jueves, 21 de Julio, 2011
*
* -------------------------------------------------------------------------
*/

package es.sm2.openppm.core.javabean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NodeMSP {
	
	private String name;
	private String description;
	private String code;
	private Date start;
	private Date finish;
	private Date plannedStart;
	private Date plannedFinish;
	private double budget;
	private boolean controlAcount;
	private List<NodeMSP> childs = new ArrayList<NodeMSP>();
	private Double ev;
	private Double ac;
	private String notes;

    /**
     * Getter for property 'duration'.
     *
     * @return Value for property 'duration'.
     */
    public Double getDuration() {
        return duration;
    }

    /**
     * Setter for property 'duration'.
     *
     * @param duration Value to set for property 'duration'.
     */
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    private Double duration;
	
	public NodeMSP() {
		super();
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void addChild(NodeMSP child) {
		this.childs.add(child);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPlannedStart() {
		return plannedStart;
	}

	public void setPlannedStart(Date plannedStart) {
		this.plannedStart = plannedStart;
	}

	public Date getPlannedFinish() {
		return plannedFinish;
	}

	public void setPlannedFinish(Date plannedFinish) {
		this.plannedFinish = plannedFinish;
	}

	public boolean isControlAcount() {
		return controlAcount;
	}

	public void setControlAcount(boolean controlAcount) {
		this.controlAcount = controlAcount;
	}

	public List<NodeMSP> getChilds() {
		return childs;
	}

	public void setChilds(List<NodeMSP> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getFinish() {
		return finish;
	}
	public void setFinish(Date finish) {
		this.finish = finish;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}

	public void setEv(Double ev) {
		this.ev = ev;
	}

	public Double getEv() {
		return ev;
	}

	public void setAc(Double ac) {
		this.ac = ac;
	}

	public Double getAc() {
		return ac;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return (notes != null && notes.length() > 300?notes.substring(0, 300):notes);
	}
}
