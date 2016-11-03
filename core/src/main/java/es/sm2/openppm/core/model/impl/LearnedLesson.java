/*
 *   Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program has been created in the hope that it will be useful.
 *   It is distributed WITHOUT ANY WARRANTY of any Kind,
 *   without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see http://www.gnu.org/licenses/.
 *
 *   For more information, please contact SM2 Software & Services Management.
 *   Mail: info@talaia-openppm.com
 *   Web: http://www.talaia-openppm.com
 *
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseLearnedLesson;

/**
 * Model class LearnedLesson
 * Created by francisco.bisquerra on 31/07/2015.
 */
public class LearnedLesson extends BaseLearnedLesson implements Cloneable{

    public LearnedLesson() {
        super();
    }

    public LearnedLesson(Integer idLearnedLesson) {
        super(idLearnedLesson);
    }

    /**
     * Process Group
     */
    public enum ProcessGroupLesson {
        INITIATING,
        PLANNING,
        EXECUTION,
        CONTROL,
        CLOSED;

        private ProcessGroupLesson() {
        }
    }

    /**
     * Type
     */
    public enum TypeLesson {
        OPPORTUNITY,
        THREAT;

        private TypeLesson() {
        }
    }

    /**
     * Clone object
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
