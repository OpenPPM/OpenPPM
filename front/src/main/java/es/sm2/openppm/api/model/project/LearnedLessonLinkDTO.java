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
 * Module: front
 * File: UsedLessonDTO.java
 * Create User: jordi.ripoll
 * Create Date: 07/09/2015 17:37:59
 */

package es.sm2.openppm.api.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;

/**
 * Created by jordi.ripoll on 07/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearnedLessonLinkDTO extends EntityDTO {

    private ProjectDTO project;
    private LearnedLessonDTO generic;
    private LearnedLessonProjectDTO specific;

    /**
     * Getter for property 'generic'.
     *
     * @return Value for property 'generic'.
     */
    public LearnedLessonDTO getGeneric() {
        return generic;
    }

    /**
     * Setter for property 'generic'.
     *
     * @param generic Value to set for property 'generic'.
     */
    public void setGeneric(LearnedLessonDTO generic) {
        this.generic = generic;
    }

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public ProjectDTO getProject() {
        return project;
    }

    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     */
    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    /**
     * Getter for property 'specific'.
     *
     * @return Value for property 'specific'.
     */
    public LearnedLessonProjectDTO getSpecific() {
        return specific;
    }

    /**
     * Setter for property 'specific'.
     *
     * @param specific Value to set for property 'specific'.
     */
    public void setSpecific(LearnedLessonProjectDTO specific) {
        this.specific = specific;
    }
}
