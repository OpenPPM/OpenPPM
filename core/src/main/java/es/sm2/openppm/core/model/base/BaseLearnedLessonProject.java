/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
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
 * File: BaseProjectGeneratedLesson.java
 * Create User: mariano.fontana
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Project;

/**
 * Created by mariano.fontana on 31/07/2015.
 */
public class BaseLearnedLessonProject {

    public static final String IDLEARNEDLESSONPROJ = "idLearnedLessonProj";
    public static final String PROJECT = "project";
    public static final String LEARNEDLESSON = "learnedLesson";
    public static final String PROJECTUSEDLESSONS = "projectUsedLessons";

    private Integer idLearnedLessonProj;
    private Project project;
    private LearnedLesson learnedLesson;

    public BaseLearnedLessonProject(){}
    public BaseLearnedLessonProject(int idLearnedLessonProj){this.idLearnedLessonProj = idLearnedLessonProj;}

    /**
     * Getter for property 'idLearnedLessonProj'.
     *
     * @return Value for property 'idLearnedLessonProj'.
     */
    public Integer getIdLearnedLessonProj() {
        return idLearnedLessonProj;
    }

    /**
     * Setter for property 'idLearnedLessonProj'.
     *
     * @param idLearnedLessonProj Value to set for property 'idLearnedLessonProj'.
     */
    public void setIdLearnedLessonProj(Integer idLearnedLessonProj) {
        this.idLearnedLessonProj = idLearnedLessonProj;
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
