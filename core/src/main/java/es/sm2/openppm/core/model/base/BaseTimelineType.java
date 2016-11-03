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
 * File: BaseTimelineType.java
 * Create User: jordi.ripoll
 * Create Date: 18/11/2015 13:04:04
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Timeline;

import java.util.HashSet;
import java.util.Set;

/**
* Base Pojo object for domain model class Timeline
* @see es.sm2.openppm.core.model.base.BaseTimelineType
* @author Hibernate Generator by Javier Hernandez
* For implement your own methods use class Timeline
*/
public class BaseTimelineType implements java.io.Serializable {


    private static final long serialVersionUID = 1L;

    public static final String ENTITY = "timeline";

    public static final String IDTIMELINE = "idTimeline";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String COMPANY = "company";
    public static final String TIMELINES = "timelines";

    private Integer idTimelineType;
    private String name;
    private String description;
    private Company company;
    private Set<Timeline> timelines = new HashSet<Timeline>(0);

    public BaseTimelineType() {}

    public BaseTimelineType(Integer idTimelineType) {
        this.idTimelineType = idTimelineType;
    }

    public Set<Timeline> getTimelines() {
        return timelines;
    }

    public void setTimelines(Set<Timeline> timelines) {
        this.timelines = timelines;
    }

    public Integer getIdTimelineType() {
        return idTimelineType;
    }

    public void setIdTimelineType(Integer idTimelineType) {
        this.idTimelineType = idTimelineType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
   public boolean equals(Object other) {
       boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Timeline) )  { result = false; }
        else if (other != null) {
            Timeline castOther = (Timeline) other;
           if (castOther.getIdTimeline().equals(this.getIdTimelineType())) { result = true; }
        }
        return result;
  }



}


