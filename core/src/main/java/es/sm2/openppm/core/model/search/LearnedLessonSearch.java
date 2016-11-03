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

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.impl.Employee;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by javier.hernandez on 11/09/2015.
 */
public class LearnedLessonSearch extends ProjectSearch {

    public static final String GREATHER_EQUAL	= "greaterEqual";
    public static final String LESS_EQUAL		= "lessEqual";
    public static final String BETWEEN			= "between";

    private String llaaQuery;

    private Boolean llaaIncludeGlobal;
    private List<Integer> llaaPrograms;
    private List<Integer> llaaCustomers;
    private List<Integer> llaaSellers;
    private List<Integer> llaaGeographicAreas;
    private List<Integer> llaaCustomerTypes;
    private List<Integer> llaaFundingSources;
    private List<String> llaaProcessGroup;
    private List<Integer> llaaKnowledgeArea;
    private List<String> llaaImportanceActions;
    private List<String> llaaImportanceRecommendation;
    private List<Integer> llaaOwner;
    private List<Integer> llaaJobProfile;
    private List<String> llaaType;
    private List<String> llaaImpactSatisfaction;
    private Boolean llaaInternalProject;
    private Boolean llaaIsGeoSelling;
    private String llaaRag;
    private String llaaImpactTime;
    private BigDecimal llaaMaxImpactTime;
    private BigDecimal llaaMinImpactTime;
    private String llaaImpactCost;
    private BigDecimal llaaMinImpactCost;
    private BigDecimal llaaMaxImpactCost;
    private String llaaRanking;
    private BigDecimal llaaMaxRanking;
    private BigDecimal llaaMinRanking;
    private Date llaaSince;
    private Date llaaUntil;

    public LearnedLessonSearch(Employee user, HashMap<String, String> settings) {

        super(user, settings);
    }

    /**
     * Getter for property 'llaaQuery'.
     *
     * @return Value for property 'llaaQuery'.
     */
    public String getLlaaQuery() {

        return llaaQuery;
    }

    /**
     * Setter for property 'llaaQuery'.
     *
     * @param llaaQuery Value to set for property 'llaaQuery'.
     */
    public void setLlaaQuery(String llaaQuery) {

        this.llaaQuery = llaaQuery;
    }

    /**
     * Getter for property 'llaaIncludeGlobal'.
     *
     * @return Value for property 'llaaIncludeGlobal'.
     */
    public Boolean getLlaaIncludeGlobal() {

        return llaaIncludeGlobal;
    }

    /**
     * Setter for property 'llaaIncludeGlobal'.
     *
     * @param llaaIncludeGlobal Value to set for property 'llaaIncludeGlobal'.
     */
    public void setLlaaIncludeGlobal(Boolean llaaIncludeGlobal) {

        this.llaaIncludeGlobal = llaaIncludeGlobal;
    }

    /**
     * Getter for property 'llaaPrograms'.
     *
     * @return Value for property 'llaaPrograms'.
     */
    public List<Integer> getLlaaPrograms() {

        return llaaPrograms;
    }

    /**
     * Setter for property 'llaaPrograms'.
     *
     * @param llaaPrograms Value to set for property 'llaaPrograms'.
     */
    public void setLlaaPrograms(List<Integer> llaaPrograms) {

        this.llaaPrograms = llaaPrograms;
    }

    /**
     * Getter for property 'llaaCustomers'.
     *
     * @return Value for property 'llaaCustomers'.
     */
    public List<Integer> getLlaaCustomers() {

        return llaaCustomers;
    }

    /**
     * Setter for property 'llaaCustomers'.
     *
     * @param llaaCustomers Value to set for property 'llaaCustomers'.
     */
    public void setLlaaCustomers(List<Integer> llaaCustomers) {

        this.llaaCustomers = llaaCustomers;
    }

    /**
     * Getter for property 'llaaSellers'.
     *
     * @return Value for property 'llaaSellers'.
     */
    public List<Integer> getLlaaSellers() {

        return llaaSellers;
    }

    /**
     * Setter for property 'llaaSellers'.
     *
     * @param llaaSellers Value to set for property 'llaaSellers'.
     */
    public void setLlaaSellers(List<Integer> llaaSellers) {

        this.llaaSellers = llaaSellers;
    }

    /**
     * Getter for property 'llaaGeographicAreas'.
     *
     * @return Value for property 'llaaGeographicAreas'.
     */
    public List<Integer> getLlaaGeographicAreas() {

        return llaaGeographicAreas;
    }

    /**
     * Setter for property 'llaaGeographicAreas'.
     *
     * @param llaaGeographicAreas Value to set for property 'llaaGeographicAreas'.
     */
    public void setLlaaGeographicAreas(List<Integer> llaaGeographicAreas) {

        this.llaaGeographicAreas = llaaGeographicAreas;
    }

    /**
     * Getter for property 'llaaCustomerTypes'.
     *
     * @return Value for property 'llaaCustomerTypes'.
     */
    public List<Integer> getLlaaCustomerTypes() {

        return llaaCustomerTypes;
    }

    /**
     * Setter for property 'llaaCustomerTypes'.
     *
     * @param llaaCustomerTypes Value to set for property 'llaaCustomerTypes'.
     */
    public void setLlaaCustomerTypes(List<Integer> llaaCustomerTypes) {

        this.llaaCustomerTypes = llaaCustomerTypes;
    }

    /**
     * Getter for property 'llaaFundingSources'.
     *
     * @return Value for property 'llaaFundingSources'.
     */
    public List<Integer> getLlaaFundingSources() {

        return llaaFundingSources;
    }

    /**
     * Setter for property 'llaaFundingSources'.
     *
     * @param llaaFundingSources Value to set for property 'llaaFundingSources'.
     */
    public void setLlaaFundingSources(List<Integer> llaaFundingSources) {

        this.llaaFundingSources = llaaFundingSources;
    }

    /**
     * Getter for property 'llaaProcessGroup'.
     *
     * @return Value for property 'llaaProcessGroup'.
     */
    public List<String> getLlaaProcessGroup() {

        return llaaProcessGroup;
    }

    /**
     * Setter for property 'llaaProcessGroup'.
     *
     * @param llaaProcessGroup Value to set for property 'llaaProcessGroup'.
     */
    public void setLlaaProcessGroup(List<String> llaaProcessGroup) {

        this.llaaProcessGroup = llaaProcessGroup;
    }

    /**
     * Getter for property 'llaaKnowledgeArea'.
     *
     * @return Value for property 'llaaKnowledgeArea'.
     */
    public List<Integer> getLlaaKnowledgeArea() {

        return llaaKnowledgeArea;
    }

    /**
     * Setter for property 'llaaKnowledgeArea'.
     *
     * @param llaaKnowledgeArea Value to set for property 'llaaKnowledgeArea'.
     */
    public void setLlaaKnowledgeArea(List<Integer> llaaKnowledgeArea) {

        this.llaaKnowledgeArea = llaaKnowledgeArea;
    }

    /**
     * Getter for property 'llaaImportanceActions'.
     *
     * @return Value for property 'llaaImportanceActions'.
     */
    public List<String> getLlaaImportanceActions() {

        return llaaImportanceActions;
    }

    /**
     * Setter for property 'llaaImportanceActions'.
     *
     * @param llaaImportanceActions Value to set for property 'llaaImportanceActions'.
     */
    public void setLlaaImportanceActions(List<String> llaaImportanceActions) {

        this.llaaImportanceActions = llaaImportanceActions;
    }

    /**
     * Getter for property 'llaaImportanceRecommendation'.
     *
     * @return Value for property 'llaaImportanceRecommendation'.
     */
    public List<String> getLlaaImportanceRecommendation() {

        return llaaImportanceRecommendation;
    }

    /**
     * Setter for property 'llaaImportanceRecommendation'.
     *
     * @param llaaImportanceRecommendation Value to set for property 'llaaImportanceRecommendation'.
     */
    public void setLlaaImportanceRecommendation(List<String> llaaImportanceRecommendation) {

        this.llaaImportanceRecommendation = llaaImportanceRecommendation;
    }

    /**
     * Getter for property 'llaaOwner'.
     *
     * @return Value for property 'llaaOwner'.
     */
    public List<Integer> getLlaaOwner() {

        return llaaOwner;
    }

    /**
     * Setter for property 'llaaOwner'.
     *
     * @param llaaOwner Value to set for property 'llaaOwner'.
     */
    public void setLlaaOwner(List<Integer> llaaOwner) {

        this.llaaOwner = llaaOwner;
    }

    /**
     * Getter for property 'llaaJobProfile'.
     *
     * @return Value for property 'llaaJobProfile'.
     */
    public List<Integer> getLlaaJobProfile() {

        return llaaJobProfile;
    }

    /**
     * Setter for property 'llaaJobProfile'.
     *
     * @param llaaJobProfile Value to set for property 'llaaJobProfile'.
     */
    public void setLlaaJobProfile(List<Integer> llaaJobProfile) {

        this.llaaJobProfile = llaaJobProfile;
    }

    /**
     * Getter for property 'llaaType'.
     *
     * @return Value for property 'llaaType'.
     */
    public List<String> getLlaaType() {

        return llaaType;
    }

    /**
     * Setter for property 'llaaType'.
     *
     * @param llaaType Value to set for property 'llaaType'.
     */
    public void setLlaaType(List<String> llaaType) {

        this.llaaType = llaaType;
    }

    /**
     * Getter for property 'llaaImpactSatisfaction'.
     *
     * @return Value for property 'llaaImpactSatisfaction'.
     */
    public List<String> getLlaaImpactSatisfaction() {

        return llaaImpactSatisfaction;
    }

    /**
     * Setter for property 'llaaImpactSatisfaction'.
     *
     * @param llaaImpactSatisfaction Value to set for property 'llaaImpactSatisfaction'.
     */
    public void setLlaaImpactSatisfaction(List<String> llaaImpactSatisfaction) {

        this.llaaImpactSatisfaction = llaaImpactSatisfaction;
    }

    /**
     * Getter for property 'llaaInternalProject'.
     *
     * @return Value for property 'llaaInternalProject'.
     */
    public Boolean getLlaaInternalProject() {

        return llaaInternalProject;
    }

    /**
     * Setter for property 'llaaInternalProject'.
     *
     * @param llaaInternalProject Value to set for property 'llaaInternalProject'.
     */
    public void setLlaaInternalProject(Boolean llaaInternalProject) {

        this.llaaInternalProject = llaaInternalProject;
    }

    /**
     * Getter for property 'llaaIsGeoSelling'.
     *
     * @return Value for property 'llaaIsGeoSelling'.
     */
    public Boolean getLlaaIsGeoSelling() {

        return llaaIsGeoSelling;
    }

    /**
     * Setter for property 'llaaIsGeoSelling'.
     *
     * @param llaaIsGeoSelling Value to set for property 'llaaIsGeoSelling'.
     */
    public void setLlaaIsGeoSelling(Boolean llaaIsGeoSelling) {

        this.llaaIsGeoSelling = llaaIsGeoSelling;
    }

    /**
     * Getter for property 'llaaRag'.
     *
     * @return Value for property 'llaaRag'.
     */
    public String getLlaaRag() {

        return llaaRag;
    }

    /**
     * Setter for property 'llaaRag'.
     *
     * @param llaaRag Value to set for property 'llaaRag'.
     */
    public void setLlaaRag(String llaaRag) {

        this.llaaRag = llaaRag;
    }

    /**
     * Getter for property 'llaaMaxImpactTime'.
     *
     * @return Value for property 'llaaMaxImpactTime'.
     */
    public BigDecimal getLlaaMaxImpactTime() {

        return llaaMaxImpactTime;
    }

    /**
     * Setter for property 'llaaMaxImpactTime'.
     *
     * @param llaaMaxImpactTime Value to set for property 'llaaMaxImpactTime'.
     */
    public void setLlaaMaxImpactTime(BigDecimal llaaMaxImpactTime) {

        this.llaaMaxImpactTime = llaaMaxImpactTime;
    }

    /**
     * Getter for property 'llaaMinImpactTime'.
     *
     * @return Value for property 'llaaMinImpactTime'.
     */
    public BigDecimal getLlaaMinImpactTime() {

        return llaaMinImpactTime;
    }

    /**
     * Setter for property 'llaaMinImpactTime'.
     *
     * @param llaaMinImpactTime Value to set for property 'llaaMinImpactTime'.
     */
    public void setLlaaMinImpactTime(BigDecimal llaaMinImpactTime) {

        this.llaaMinImpactTime = llaaMinImpactTime;
    }

    /**
     * Getter for property 'llaaMinImpactCost'.
     *
     * @return Value for property 'llaaMinImpactCost'.
     */
    public BigDecimal getLlaaMinImpactCost() {

        return llaaMinImpactCost;
    }

    /**
     * Setter for property 'llaaMinImpactCost'.
     *
     * @param llaaMinImpactCost Value to set for property 'llaaMinImpactCost'.
     */
    public void setLlaaMinImpactCost(BigDecimal llaaMinImpactCost) {

        this.llaaMinImpactCost = llaaMinImpactCost;
    }

    /**
     * Getter for property 'llaaMaxImpactCost'.
     *
     * @return Value for property 'llaaMaxImpactCost'.
     */
    public BigDecimal getLlaaMaxImpactCost() {

        return llaaMaxImpactCost;
    }

    /**
     * Setter for property 'llaaMaxImpactCost'.
     *
     * @param llaaMaxImpactCost Value to set for property 'llaaMaxImpactCost'.
     */
    public void setLlaaMaxImpactCost(BigDecimal llaaMaxImpactCost) {

        this.llaaMaxImpactCost = llaaMaxImpactCost;
    }

    /**
     * Getter for property 'llaaMaxRanking'.
     *
     * @return Value for property 'llaaMaxRanking'.
     */
    public BigDecimal getLlaaMaxRanking() {

        return llaaMaxRanking;
    }

    /**
     * Setter for property 'llaaMaxRanking'.
     *
     * @param llaaMaxRanking Value to set for property 'llaaMaxRanking'.
     */
    public void setLlaaMaxRanking(BigDecimal llaaMaxRanking) {

        this.llaaMaxRanking = llaaMaxRanking;
    }

    /**
     * Getter for property 'llaaMinRanking'.
     *
     * @return Value for property 'llaaMinRanking'.
     */
    public BigDecimal getLlaaMinRanking() {

        return llaaMinRanking;
    }

    /**
     * Setter for property 'llaaMinRanking'.
     *
     * @param llaaMinRanking Value to set for property 'llaaMinRanking'.
     */
    public void setLlaaMinRanking(BigDecimal llaaMinRanking) {

        this.llaaMinRanking = llaaMinRanking;
    }

    /**
     * Getter for property 'llaaSince'.
     *
     * @return Value for property 'llaaSince'.
     */
    public Date getLlaaSince() {

        return llaaSince;
    }

    /**
     * Setter for property 'llaaSince'.
     *
     * @param llaaSince Value to set for property 'llaaSince'.
     */
    public void setLlaaSince(Date llaaSince) {

        this.llaaSince = llaaSince;
    }

    /**
     * Getter for property 'llaaUntil'.
     *
     * @return Value for property 'llaaUntil'.
     */
    public Date getLlaaUntil() {

        return llaaUntil;
    }

    /**
     * Setter for property 'llaaUntil'.
     *
     * @param llaaUntil Value to set for property 'llaaUntil'.
     */
    public void setLlaaUntil(Date llaaUntil) {

        this.llaaUntil = llaaUntil;
    }

    public String getLlaaRanking() {
        return llaaRanking;
    }

    public void setLlaaRanking(String llaaRanking) {
        this.llaaRanking = llaaRanking;
    }

    public String getLlaaImpactCost() {
        return llaaImpactCost;
    }

    public void setLlaaImpactCost(String llaaImpactCost) {
        this.llaaImpactCost = llaaImpactCost;
    }

    public String getLlaaImpactTime() {
        return llaaImpactTime;
    }

    public void setLlaaImpactTime(String llaaImpactTime) {
        this.llaaImpactTime = llaaImpactTime;
    }
}
