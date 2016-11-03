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

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.search.LearnedLessonSearch;
import es.sm2.openppm.core.utils.FilterUtil;
import es.sm2.openppm.core.utils.IntegerUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * DAO object for domain model class LearnedLesson
 * Created by francisco.bisquerra on 31/07/2015.
 */
public class LearnedLessonDAO extends AbstractGenericHibernateDAO<LearnedLesson, Integer> {


    public LearnedLessonDAO(Session session) { super(session); }


    /**
     * Create query
     *
     * @param queryFormat
     * @param filter
     * @return
     */
    private Query createQuery(String queryFormat, LearnedLessonSearch filter) {

        Query query = getSession().createQuery(queryFormat + createWhere(filter));

        addParameters(query, filter);

        return query;
    }

    /**
     * Add parameters
     *
     * @param query
     * @param filter
     */
    private void addParameters(Query query, LearnedLessonSearch filter) {

        // LearnedLesson search parameters
        //

        // Name and id
        if (ValidateUtil.isNotNull(filter.getLlaaQuery())) { query.setString("llaaQuery", "%"+filter.getLlaaQuery().toUpperCase()+"%"); }

        Integer[] idsLLAA = IntegerUtil.parseStringSequence(filter.getLlaaQuery(), StringPool.COMMA);

        if (ValidateUtil.isNotNull(idsLLAA)) {
            query.setParameterList("idsLLAA", idsLLAA);
        }

        // Programs
        if (ValidateUtil.isNotNull(filter.getLlaaPrograms())) { query.setParameterList("llaaPrograms", filter.getLlaaPrograms()); }

        // Customers
        if (ValidateUtil.isNotNull(filter.getLlaaCustomers())) { query.setParameterList("llaaCustomers", filter.getLlaaCustomers()); }

        // Sellers
        if (ValidateUtil.isNotNull(filter.getLlaaSellers())) { query.setParameterList("llaaSellers", filter.getLlaaSellers()); }

        // Geographic Areas
        if (ValidateUtil.isNotNull(filter.getLlaaGeographicAreas())) { query.setParameterList("llaaGeographicAreas", filter.getLlaaGeographicAreas()); }

        // Customer types
        if (ValidateUtil.isNotNull(filter.getLlaaCustomerTypes())) { query.setParameterList("llaaCustomerTypes", filter.getLlaaCustomerTypes()); }

        // Funding sources
        if (ValidateUtil.isNotNull(filter.getLlaaFundingSources())) { query.setParameterList("llaaFundingSources", filter.getLlaaFundingSources()); }

        // Knowledge Area
        if (ValidateUtil.isNotNull(filter.getLlaaKnowledgeArea())) { query.setParameterList("llaaKnowledgeArea", filter.getLlaaKnowledgeArea()); }

        // Owners
        if (ValidateUtil.isNotNull(filter.getLlaaOwner())) { query.setParameterList("llaaOwner", filter.getLlaaOwner()); }

        // Profiles
        if (ValidateUtil.isNotNull(filter.getLlaaJobProfile())) { query.setParameterList("llaaJobProfile", filter.getLlaaJobProfile()); }

        // Min impact time
        if (filter.getLlaaMinImpactTime() != null) { query.setBigDecimal("llaaMinImpactTime", filter.getLlaaMinImpactTime()); }

        // Max impact time
        if (filter.getLlaaMaxImpactTime() != null) { query.setBigDecimal("llaaMaxImpactTime", filter.getLlaaMaxImpactTime()); }

        // Min impact cost
        if (filter.getLlaaMinImpactCost() != null) { query.setBigDecimal("llaaMinImpactCost", filter.getLlaaMinImpactCost()); }

        // Max impact cost
        if (filter.getLlaaMaxImpactCost() != null) { query.setBigDecimal("llaaMaxImpactCost", filter.getLlaaMaxImpactCost()); }

        // Min ranking
        if (filter.getLlaaMinRanking() != null) { query.setBigDecimal("llaaMinRanking", filter.getLlaaMinRanking()); }

        // Max ranking
        if (filter.getLlaaMaxRanking() != null) { query.setBigDecimal("llaaMaxRanking", filter.getLlaaMaxRanking()); }

        // Since
        if (filter.getLlaaSince() != null) { query.setDate("llaaSince", filter.getLlaaSince()); }

        // Until
        if (filter.getLlaaUntil() != null) { query.setDate("llaaUntil", filter.getLlaaUntil()); }
    }

    /**
     * Create where for filter projects
     *
     * @param filter
     * @return
     */
    private String createWhere(LearnedLessonSearch filter) {

        String where = "";

        // LearnedLesson search where
        //

        // Name and id
        if (ValidateUtil.isNotNull(filter.getLlaaQuery())) {

            String queryFilter = "(l.name LIKE :llaaQuery OR l.description LIKE :llaaQuery OR act.name LIKE :llaaQuery OR rec.name LIKE :llaaQuery) ";

            Integer[] idsLLAA = IntegerUtil.parseStringSequence(filter.getLlaaQuery(), StringPool.COMMA);

            if (ValidateUtil.isNotNull(idsLLAA)) {
                queryFilter += "OR l.idLearnedLesson IN (:idsLLAA) ";
            }

            where += FilterUtil.addFilterAnd(where, queryFilter);
        }

        // Only global LLAA
        if (filter.getLlaaIncludeGlobal() != null && filter.getLlaaIncludeGlobal()) {

            where += FilterUtil.addFilterAnd(where,
                    "l.idLearnedLesson NOT IN (" +
                        "SELECT llaa.idLearnedLesson FROM LearnedLesson llaa " +
                        "INNER JOIN llaa.learnedLessonProjects) ");

        }
        // Not Include Global LLAA
        else if (filter.getLlaaIncludeGlobal() != null && !filter.getLlaaIncludeGlobal()) {

            where += FilterUtil.addFilterAnd(where,
                    "l.idLearnedLesson IN (" +
                            "SELECT llaa.idLearnedLesson FROM LearnedLesson llaa " +
                            "INNER JOIN llaa.learnedLessonProjects) ");
        }

        // Programs
        if (ValidateUtil.isNotNull(filter.getLlaaPrograms())) {
            where += FilterUtil.addFilterAnd(where, "lpg.idProgram IN (:llaaPrograms) ");
        }

        // Customers
        if (ValidateUtil.isNotNull(filter.getLlaaCustomers())) {
            where += FilterUtil.addFilterAnd(where, "lcust.idCustomer IN (:llaaCustomers) ");
        }

        // Sellers
        if (ValidateUtil.isNotNull(filter.getLlaaSellers())) {
            where += FilterUtil.addFilterAnd(where, "ls.idSeller IN (:llaaSellers) ");
        }

        // Geographic Areas
        if (ValidateUtil.isNotNull(filter.getLlaaGeographicAreas())) {
            where += FilterUtil.addFilterAnd(where, "lg.idGeography IN (:llaaGeographicAreas) ");
        }

        // Customer type
        if (ValidateUtil.isNotNull(filter.getLlaaCustomerTypes())) {
            where += FilterUtil.addFilterAnd(where, "lcustt.idCustomerType IN (:llaaCustomerTypes) ");
        }

        // Funding source
        if (ValidateUtil.isNotNull(filter.getLlaaFundingSources())) {
            where += FilterUtil.addFilterAnd(where, "lfs.idFundingSource IN (:llaaFundingSources) ");
        }

        // Process Group
        if (ValidateUtil.isNotNull(filter.getLlaaProcessGroup())) {

            String orClause = "";

            for (int i=0; i <= filter.getLlaaProcessGroup().size() - 1; i++) {

                if (i == 0) {
                    orClause = "(";
                }

                orClause += "l.processGroup = '" + filter.getLlaaProcessGroup().get(i) + "' ";

                if (i < filter.getLlaaProcessGroup().size() - 1) {
                    orClause += "OR ";
                }
                else {
                    orClause += ")";
                }
            }

            where += FilterUtil.addFilterAnd(where, orClause);
        }

        // Knowledge Area
        if (ValidateUtil.isNotNull(filter.getLlaaKnowledgeArea())) {
            where += FilterUtil.addFilterAnd(where, "ka.idKnowledgeArea IN (:llaaKnowledgeArea) ");
        }

        // Importance actions
        if (ValidateUtil.isNotNull(filter.getLlaaImportanceActions())) {

            String orClause = "";

            for (int i=0; i <= filter.getLlaaImportanceActions().size() - 1; i++) {

                if (i == 0) {
                    orClause = "(";
                }

                orClause += "act.importance = '" + filter.getLlaaImportanceActions().get(i) + "' ";

                if (i < filter.getLlaaImportanceActions().size() - 1) {
                    orClause += "OR ";
                }
                else {
                    orClause += ")";
                }
            }

            where += FilterUtil.addFilterAnd(where, orClause);
        }

        // Importance recommendations
        if (ValidateUtil.isNotNull(filter.getLlaaImportanceRecommendation())) {

            String orClause = "";

            for (int i=0; i <= filter.getLlaaImportanceRecommendation().size() - 1; i++) {

                if (i == 0) {
                    orClause = "(";
                }

                orClause += "rec.importance = '" + filter.getLlaaImportanceRecommendation().get(i) + "' ";

                if (i < filter.getLlaaImportanceRecommendation().size() - 1) {
                    orClause += "OR ";
                }
                else {
                    orClause += ")";
                }
            }

            where += FilterUtil.addFilterAnd(where, orClause);
        }

        // Owners
        if (ValidateUtil.isNotNull(filter.getLlaaOwner())) {
            where += FilterUtil.addFilterAnd(where, "owner.idContact IN (:llaaOwner) ");
        }

        // Profiles
        if (ValidateUtil.isNotNull(filter.getLlaaJobProfile())) {
            where += FilterUtil.addFilterAnd(where, "pf.idProfile IN (:llaaJobProfile) ");
        }

        // Type
        if (ValidateUtil.isNotNull(filter.getLlaaType())) {

            String orClause = "";

            for (int i=0; i <= filter.getLlaaType().size() - 1; i++) {

                if (i == 0) {
                    orClause = "(";
                }

                orClause += "l.type = '" + filter.getLlaaType().get(i) + "' ";

                if (i < filter.getLlaaType().size() - 1) {
                    orClause += "OR ";
                }
                else {
                    orClause += ")";
                }
            }

            where += FilterUtil.addFilterAnd(where, orClause);
        }

        // Impact of satisfaction
        if (ValidateUtil.isNotNull(filter.getLlaaImpactSatisfaction())) {

            String orClause = "";

            for (int i=0; i <= filter.getLlaaImpactSatisfaction().size() - 1; i++) {

                if (i == 0) {
                    orClause = "(";
                }

                orClause += "l.impactSatisfaction = '" + filter.getLlaaImpactSatisfaction().get(i) + "' ";

                if (i < filter.getLlaaImpactSatisfaction().size() - 1) {
                    orClause += "OR ";
                }
                else {
                    orClause += ")";
                }
            }

            where += FilterUtil.addFilterAnd(where, orClause);
        }

        // Impact time between
        if (filter.getLlaaMinImpactTime() != null && filter.getLlaaMaxImpactTime() != null) {
            where += FilterUtil.addFilterAnd(where, "(l.impactTime BETWEEN :llaaMinImpactTime AND :llaaMaxImpactTime) ");
        }
        // Min
        else if (filter.getLlaaMinImpactTime() != null) {
            where += FilterUtil.addFilterAnd(where, "l.impactTime >= :llaaMinImpactTime ");
        }
        // Max
        else if (filter.getLlaaMaxImpactTime() != null) {
            where += FilterUtil.addFilterAnd(where, "l.impactTime <= :llaaMaxImpactTime ");
        }

        // Impact cost between
        if (filter.getLlaaMinImpactCost() != null && filter.getLlaaMaxImpactCost() != null) {
            where += FilterUtil.addFilterAnd(where, "(l.impactCost BETWEEN :llaaMinImpactCost AND :llaaMaxImpactCost) ");
        }
        // Min
        else if (filter.getLlaaMinImpactCost() != null) {
            where += FilterUtil.addFilterAnd(where, "l.impactCost >= :llaaMinImpactCost ");
        }
        // Max
        else if (filter.getLlaaMaxImpactCost() != null) {
            where += FilterUtil.addFilterAnd(where, "l.impactCost <= :llaaMaxImpactCost ");
        }

        // Ranking
        String rankingCount = "(SELECT COUNT(link.learnedLesson.idLearnedLesson) FROM LearnedLessonLink link where link.learnedLesson.idLearnedLesson = l.idLearnedLesson)";

        // Between
        if (filter.getLlaaMinRanking() != null && filter.getLlaaMaxRanking() != null) {
            where += FilterUtil.addFilterAnd(where, "("+ rankingCount +" BETWEEN :llaaMinRanking AND :llaaMaxRanking) ");
        }
        // Min
        else if (filter.getLlaaMinRanking() != null) {
            where += FilterUtil.addFilterAnd(where, rankingCount + " >= :llaaMinRanking ");
        }
        // Max
        else if (filter.getLlaaMaxRanking() != null) {
            where += FilterUtil.addFilterAnd(where, rankingCount + " <= :llaaMaxRanking ");
        }

        // Since and until dates
        if (filter.getLlaaSince() != null && filter.getLlaaUntil() != null) {
            where += FilterUtil.addFilterAnd(where, "((l.dateRaised BETWEEN :llaaSince AND :llaaUntil) OR (l.dateRaised >= :llaaSince AND l.dateRaised <= :llaaUntil)) ");
        }
        else if (filter.getLlaaSince() != null) {
            where += FilterUtil.addFilterAnd(where, "l.dateRaised >= :llaaSince ");
        }
        else if (filter.getLlaaUntil() != null) {
            where += FilterUtil.addFilterAnd(where, "l.dateRaised <= :llaaUntil ");
        }

        return where;
    }


    /**
     * Find generated learned lessons
     *
     * @param searchParams
     * @return
     */
    public List<LearnedLesson> findGenerated(LearnedLessonSearch searchParams) {

        // LearnedLesson search query
        String q = "SELECT DISTINCT l FROM LearnedLesson l " +

                // Project joins
                "LEFT JOIN FETCH l.learnedLessonProjects llp " +
                "LEFT JOIN FETCH llp.project p " +
                "LEFT JOIN FETCH p.employeeByProjectManager pm " +
                "LEFT JOIN FETCH pm.contact pmc " +
                "LEFT JOIN FETCH l.learnedLessonLinks llk " +
                "LEFT JOIN FETCH l.actionsLesson act " +
                "LEFT JOIN FETCH l.recsLesson rec " +
                "LEFT JOIN FETCH l.contact owner " +
                "LEFT JOIN FETCH l.knowledgeArea ka " +
                "LEFT JOIN FETCH l.profile pf " +
                "LEFT JOIN FETCH l.program lpg " +
                "LEFT JOIN FETCH l.customer lcust " +
                "LEFT JOIN FETCH lcust.customertype lcustt " +
                "LEFT JOIN FETCH l.seller ls " +
                "LEFT JOIN FETCH l.geography lg " +
                "LEFT JOIN FETCH l.fundingSource lfs ";

        return createQuery(q, searchParams).list();
    }

    /**
     * Find generated learned lesson by id
     *
     * @param id
     * @param joins
     * @return
     */
    public LearnedLesson findGenerated(Integer id, List<String> joins) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(LearnedLesson.IDLEARNEDLESSON, id))
                .setFetchMode(LearnedLesson.LEARNEDLESSONPROJECTS, FetchMode.JOIN);

        addJoins(crit, joins);

        return (LearnedLesson) crit.uniqueResult();
    }
}
