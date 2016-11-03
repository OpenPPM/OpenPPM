/*
 * <%--
 *   ~ Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 *   ~ This program is free software: you can redistribute it and/or modify
 *   ~ it under the terms of the GNU General Public License as published by
 *   ~ the Free Software Foundation, either version 3 of the License, or
 *   ~ (at your option) any later version.
 *   ~
 *   ~ This program has been created in the hope that it will be useful.
 *   ~ It is distributed WITHOUT ANY WARRANTY of any Kind,
 *   ~ without even the implied warranty of
 *   ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   ~ See the GNU General Public License for more details.
 *   ~
 *   ~ You should have received a copy of the GNU General Public License
 *   ~ along with this program. If not, see http://www.gnu.org/licenses/.
 *   ~
 *   ~ For more information, please contact SM2 Software & Services Management.
 *   ~ Mail: info@talaia-openppm.com
 *   ~ Web: http://www.talaia-openppm.com
 *   ~
 *   ~ Module: core
 *   ~ File: BaseProfile.java
 *   ~ Create User: mariano.fontana
 *   ~ Create Date: 24/07/2015 12:48:08
 *   --%>
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.LearnedLesson;

import java.lang.Integer;import java.lang.String;import java.util.HashSet;
import java.util.Set;

/**
 * Created by mariano.fontana on 31/07/2015.
 */
public class BaseProfile {
    public static final String IDPROFILE = "idProfile";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String COMPANY = "company";
    public static final String LEARNEDLESSONS = "learnedLessons";

    private Integer idProfile;
    private String name;
    private String description;
    private Company company;
    private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseProfile(){
    }

    public BaseProfile(int idProfile){this.idProfile = idProfile;    }

    /**
     * Getter for property 'idProfile'.
     *
     * @return Value for property 'idProfile'.
     */
    public Integer getIdProfile() {
        return idProfile;
    }

    /**
     * Setter for property 'idProfile'.
     *
     * @param idProfile Value to set for property 'idProfile'.
     */
    public void setIdProfile(Integer idProfile) {
        this.idProfile = idProfile;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property 'description'.
     *
     * @return Value for property 'description'.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for property 'description'.
     *
     * @param description Value to set for property 'description'.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property 'company'.
     *
     * @return Value for property 'company'.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Setter for property 'company'.
     *
     * @param company Value to set for property 'company'.
     */
    public void setCompany(Company company) {
        this.company = company;
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
}
