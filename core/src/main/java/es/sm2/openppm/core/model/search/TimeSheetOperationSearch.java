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
 * File: OperationSearch.java
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 13:03:10
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.base.BaseSearch;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
public class TimeSheetOperationSearch extends BaseSearch {

    private Integer codeOperation;
    private Date since;
    private Date until;
    private Integer codePool;
    private List<Integer> codeContactList;
    private List<Integer> codeJobCategoryList;
    private List<Integer> codeSkillList;

    public TimeSheetOperationSearch(Company company, Integer codeOperation, Date since, Date until) {

        super(company);
        this.codeOperation = codeOperation;
        this.since = since;
        this.until = until;
    }

    /**
     * Add code contact
     *
     * @param code
     */
    public void addCodeContact(Integer code) {

        if (ValidateUtil.isNull(codeContactList)) {
            codeContactList = new ArrayList<Integer>();
        }

        if (code != null) {
            codeContactList.add(code);
        }
    }

    public Integer getCodeOperation() {

        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {

        this.codeOperation = codeOperation;
    }

    public Date getSince() {

        return since;
    }

    public void setSince(Date since) {

        this.since = since;
    }

    public Date getUntil() {

        return until;
    }

    public void setUntil(Date until) {

        this.until = until;
    }

    public Integer getCodePool() {

        return codePool;
    }

    public void setCodePool(Integer codePool) {

        this.codePool = codePool;
    }

    /**
     * Getter for property 'codeContactList'.
     *
     * @return Value for property 'codeContactList'.
     */
    public List<Integer> getCodeContactList() {

        return codeContactList;
    }

    /**
     * Setter for property 'codeContactList'.
     *
     * @param codeContactList Value to set for property 'codeContactList'.
     */
    public void setCodeContactList(List<Integer> codeContactList) {

        this.codeContactList = codeContactList;
    }

    public List<Integer> getCodeJobCategoryList() {

        return codeJobCategoryList;
    }

    public void setCodeJobCategoryList(List<Integer> codeJobCategoryList) {

        this.codeJobCategoryList = codeJobCategoryList;
    }

    public List<Integer> getCodeSkillList() {

        return codeSkillList;
    }

    public void setCodeSkillList(List<Integer> codeSkillList) {

        this.codeSkillList = codeSkillList;
    }
}
