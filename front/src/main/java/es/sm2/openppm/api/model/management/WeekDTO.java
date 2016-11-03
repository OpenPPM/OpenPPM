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
 * File: WeekDTO.java
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 13:12:13
 */

package es.sm2.openppm.api.model.management;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekDTO extends EntityDTO {

    public enum Status {
        APP0,
        APP1,
        APP2,
        APP3,
    }

    private Double hoursDay1;
    private Double hoursDay2;
    private Double hoursDay3;
    private Double hoursDay4;
    private Double hoursDay5;
    private Double hoursDay6;
    private Double hoursDay7;
    private Status status;
    private Date initDate;
    private Date endDate;
    private Boolean suggestReject;
    private String suggestRejectComment;
    private Double sumHours;

    public WeekDTO() {

    }

    public WeekDTO(Integer code) {

        super(code);
    }

    /**
     * Getter for property 'sumHours'.
     *
     * @return Value for property 'sumHours'.
     */
    public Double getSumHours() {
        return sumHours;
    }

    /**
     * Setter for property 'sumHours'.
     *
     * @param totalHours Value to set for property 'sumHours'.
     */
    public void setSumHours(Double totalHours) {
        this.sumHours = totalHours;
    }

    /**
     * Getter for property 'initDate'.
     *
     * @return Value for property 'initDate'.
     */
    public Date getInitDate() {

        return initDate;
    }

    /**
     * Setter for property 'initDate'.
     *
     * @param initDate Value to set for property 'initDate'.
     */
    public void setInitDate(Date initDate) {

        this.initDate = initDate;
    }

    /**
     * Getter for property 'endDate'.
     *
     * @return Value for property 'endDate'.
     */
    public Date getEndDate() {

        return endDate;
    }

    /**
     * Setter for property 'endDate'.
     *
     * @param endDate Value to set for property 'endDate'.
     */
    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    /**
     * Getter for property 'suggestReject'.
     *
     * @return Value for property 'suggestReject'.
     */
    public Boolean getSuggestReject() {

        return suggestReject;
    }

    /**
     * Setter for property 'suggestReject'.
     *
     * @param suggestReject Value to set for property 'suggestReject'.
     */
    public void setSuggestReject(Boolean suggestReject) {

        this.suggestReject = suggestReject;
    }

    /**
     * Getter for property 'suggestRejectComment'.
     *
     * @return Value for property 'suggestRejectComment'.
     */
    public String getSuggestRejectComment() {

        return suggestRejectComment;
    }

    /**
     * Setter for property 'suggestRejectComment'.
     *
     * @param suggestRejectComment Value to set for property 'suggestRejectComment'.
     */
    public void setSuggestRejectComment(String suggestRejectComment) {

        this.suggestRejectComment = suggestRejectComment;
    }

    /**
     * Getter for property 'hoursDay1'.
     *
     * @return Value for property 'hoursDay1'.
     */
    public Double getHoursDay1() {

        return hoursDay1;
    }

    /**
     * Setter for property 'hoursDay1'.
     *
     * @param hoursDay1 Value to set for property 'hoursDay1'.
     */
    public void setHoursDay1(Double hoursDay1) {

        this.hoursDay1 = hoursDay1;
    }

    /**
     * Getter for property 'hoursDay2'.
     *
     * @return Value for property 'hoursDay2'.
     */
    public Double getHoursDay2() {

        return hoursDay2;
    }

    /**
     * Setter for property 'hoursDay2'.
     *
     * @param hoursDay2 Value to set for property 'hoursDay2'.
     */
    public void setHoursDay2(Double hoursDay2) {

        this.hoursDay2 = hoursDay2;
    }

    /**
     * Getter for property 'hoursDay3'.
     *
     * @return Value for property 'hoursDay3'.
     */
    public Double getHoursDay3() {

        return hoursDay3;
    }

    /**
     * Setter for property 'hoursDay3'.
     *
     * @param hoursDay3 Value to set for property 'hoursDay3'.
     */
    public void setHoursDay3(Double hoursDay3) {

        this.hoursDay3 = hoursDay3;
    }

    /**
     * Getter for property 'hoursDay4'.
     *
     * @return Value for property 'hoursDay4'.
     */
    public Double getHoursDay4() {

        return hoursDay4;
    }

    /**
     * Setter for property 'hoursDay4'.
     *
     * @param hoursDay4 Value to set for property 'hoursDay4'.
     */
    public void setHoursDay4(Double hoursDay4) {

        this.hoursDay4 = hoursDay4;
    }

    /**
     * Getter for property 'hoursDay5'.
     *
     * @return Value for property 'hoursDay5'.
     */
    public Double getHoursDay5() {

        return hoursDay5;
    }

    /**
     * Setter for property 'hoursDay5'.
     *
     * @param hoursDay5 Value to set for property 'hoursDay5'.
     */
    public void setHoursDay5(Double hoursDay5) {

        this.hoursDay5 = hoursDay5;
    }

    /**
     * Getter for property 'hoursDay6'.
     *
     * @return Value for property 'hoursDay6'.
     */
    public Double getHoursDay6() {

        return hoursDay6;
    }

    /**
     * Setter for property 'hoursDay6'.
     *
     * @param hoursDay6 Value to set for property 'hoursDay6'.
     */
    public void setHoursDay6(Double hoursDay6) {

        this.hoursDay6 = hoursDay6;
    }

    /**
     * Getter for property 'hoursDay7'.
     *
     * @return Value for property 'hoursDay7'.
     */
    public Double getHoursDay7() {

        return hoursDay7;
    }

    /**
     * Setter for property 'hoursDay7'.
     *
     * @param hoursDay7 Value to set for property 'hoursDay7'.
     */
    public void setHoursDay7(Double hoursDay7) {

        this.hoursDay7 = hoursDay7;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public Status getStatus() {

        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(Status status) {

        this.status = status;
    }
}
