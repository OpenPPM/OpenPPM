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
 * File: BaseProjectUsedLesson.java
 * Create User: jordi.ripoll
 * Create Date: 17/08/2015 10:05:24
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Project;

/**
 * Created by jordi.ripoll on 17/08/2015.
 */
public class BaseLearnedLessonLink {

    public static final String IDLEARNEDLESSONLINK = "idLearnedLessonLink";
    public static final String PROJECT = "project";
    public static final String LEARNEDLESSON = "learnedLesson";

    private Integer idLearnedLessonLink;
    private Project project;
    private LearnedLesson learnedLesson;

    public BaseLearnedLessonLink(){}
    public BaseLearnedLessonLink(int idLearnedLessonLink){this.idLearnedLessonLink = idLearnedLessonLink;}

    /**
     * Getter for property 'idLearnedLessonLink'.
     *
     * @return Value for property 'idLearnedLessonLink'.
     */
    public Integer getIdLearnedLessonLink() {
        return idLearnedLessonLink;
    }

    /**
     * Setter for property 'idLearnedLessonLink'.
     *
     * @param idLearnedLessonLink Value to set for property 'idLearnedLessonLink'.
     */
    public void setIdLearnedLessonLink(Integer idLearnedLessonLink) {
        this.idLearnedLessonLink = idLearnedLessonLink;
    }

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     */
    public void setProject(Project project) {
        this.project = project;
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
