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
 * File: BaseActionLesson.java
 * Create User: jordi.ripoll
 * Create Date: 10/08/2015 12:58:25
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.LearnedLesson;

/**
 * Created by jordi.ripoll on 10/08/2015.
 */
public class BaseActionLesson {

    public static final String IDACTIONLESSON = "idActionLesson";
    public static final String NAME = "name";
    public static final String IMPORTANCE = "importance";
    public static final String LEARNEDLESSON = "learnedLesson";

    private Integer idActionLesson;
    private String name;
    private String importance;
    private LearnedLesson learnedLesson;

    public BaseActionLesson(){
    }

    public BaseActionLesson(Integer idActionLesson){
    }

    /**
     * Getter for property 'idActionLesson'.
     *
     * @return Value for property 'idActionLesson'.
     */
    public Integer getIdActionLesson() {
        return idActionLesson;
    }

    /**
     * Setter for property 'idActionLesson'.
     *
     * @param idActionLesson Value to set for property 'idActionLesson'.
     */
    public void setIdActionLesson(Integer idActionLesson) {
        this.idActionLesson = idActionLesson;
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
     * Getter for property 'importance'.
     *
     * @return Value for property 'importance'.
     */
    public String getImportance() {
        return importance;
    }

    /**
     * Setter for property 'importance'.
     *
     * @param importance Value to set for property 'importance'.
     */
    public void setImportance(String importance) {
        this.importance = importance;
    }

    /**
     * Getter for property 'learnedLesson'.
     *
     * @return Value for property 'learnedLesson'.
     */
    public LearnedLesson getLearnedLesson() {
        return learnedLesson;
    }

    /**
     * Setter for property 'learnedLesson'.
     *
     * @param learnedLesson Value to set for property 'learnedLesson'.
     */
    public void setLearnedLesson(LearnedLesson learnedLesson) {
        this.learnedLesson = learnedLesson;
    }
}
