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
 * File: java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Configurations;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ConfigurationDAO;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.search.LearnedLessonSearch;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Logic object for domain model class Configuration
 * @see es.sm2.openppm.core.logic.impl.ConfigurationLogic
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ConfigurationLogic extends AbstractGenericLogic<Configuration, Integer> {

	/**
	 * Find configurations by types
	 * 
	 * @param types
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String, String> findByTypes(Employee user, String...types) throws Exception {
		
		if (types == null) {
			throw new NoDataFoundException();
		}
		
		HashMap<String, String> configurations = new HashMap<String, String>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ConfigurationDAO configurationDAO = new ConfigurationDAO(session);
			
			List<Configuration> confs = configurationDAO.findByTypes(user, types);
			
			for (Configuration item : confs) {
			
				if (ValidateUtil.isNotNull(item.getValue())) {
					
					configurations.put(item.getName(), item.getValue());
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return configurations;
	}

    /**
     * Find configurations by types
     *
     * @param types
     * @return
     * @throws Exception
     */
    public List<Configuration> findByTypes(Employee user, List<String> types) throws Exception {

        if (types == null) {
            throw new NoDataFoundException();
        }

        List<Configuration> configurations = new ArrayList<Configuration>();

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ConfigurationDAO configurationDAO = new ConfigurationDAO(session);

            configurations = configurationDAO.findByTypes(user, types.toArray(new String[types.size()]));

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return configurations;
    }

	/**
	 * Save or update Configuration
	 *
	 * @param user
	 * @param type
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public void saveConfiguration(Employee user, String type, String name, String value)
			throws Exception {

		// Create configuration for call generic method
		HashMap<String, String> configuration = new HashMap<String, String>();
		configuration.put(name, value);

		// Save or update configuration
		saveConfigurations(user, configuration, type);
	}
	
	/**
	 * Save or update configurations
	 * 
	 * @param user
	 * @param configurations
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> saveConfigurations(Employee user, HashMap<String, String> configurations, String type) throws Exception {
		
		if (type == null
				|| configurations == null
				|| user == null) {
			throw new NoDataFoundException();
		}
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ConfigurationDAO configurationDAO = new ConfigurationDAO(session);
			
			for (String name : configurations.keySet()) {
				
				// Find if exist
				Configuration configuration = configurationDAO.findByName(user, name, type);
				
				// If not exist initialize configuration
				if (configuration == null) {
					
					configuration = new Configuration();
					configuration.setContact(user.getContact());
					configuration.setName(name);
					configuration.setType(type);
				}
				
				// Set new value
				configuration.setValue(configurations.get(name));
				
				// Save configuration
				configurationDAO.makePersistent(configuration);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return configurations;
	}

    /**
     * Save configurations filters
     *
     * @param search
     * @param searchProject
     */
    public void saveFiltersLearnedLesson(LearnedLessonSearch search, ProjectSearch searchProject) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN_DASH); //TODO jordi.ripoll - mirar con que formato hay que guardarlo - vendra por setting

        // Filters LLAA
        //
        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAQUERY.name(),
                search.getLlaaQuery() != null ? search.getLlaaQuery() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAINCLUDEGLOBAL.name(),
                search.getLlaaIncludeGlobal() != null ? search.getLlaaIncludeGlobal().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAPROGRAMS.name(), StringUtil.toStringListInt(search.getLlaaPrograms()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAACUSTOMERS.name(), StringUtil.toStringListInt(search.getLlaaCustomers()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAASELLERS.name(), StringUtil.toStringListInt(search.getLlaaSellers()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAGEOGRAPHIES.name(), StringUtil.toStringListInt(search.getLlaaGeographicAreas()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAFUNDINGSOURCES.name(), StringUtil.toStringListInt(search.getLlaaFundingSources()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAACUSTOMERTYPES.name(), StringUtil.toStringListInt(search.getLlaaCustomerTypes()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAPROCESSGROUPS.name(), StringUtil.toStringListStr(search.getLlaaProcessGroup()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAKNOWLEDGEAREAS.name(), StringUtil.toStringListInt(search.getLlaaKnowledgeArea()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAIMPORTANCEACTIONS.name(), StringUtil.toStringListStr(search.getLlaaImportanceActions()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAIMPORTANCERECOMMENDATIONS.name(), StringUtil.toStringListStr(search.getLlaaImportanceRecommendation()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAOWNER.name(), StringUtil.toStringListInt(search.getLlaaOwner()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAJOBPROFILE.name(), StringUtil.toStringListInt(search.getLlaaJobProfile()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAATYPE.name(), StringUtil.toStringListStr(search.getLlaaType()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAIMPACTSATISFACTION.name(), StringUtil.toStringListStr(search.getLlaaImpactSatisfaction()));

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMINIMPACTTIME.name(),
                search.getLlaaMinImpactTime() != null ? search.getLlaaMinImpactTime().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMAXIMPACTTIME.name(),
                search.getLlaaMaxImpactTime() != null ? search.getLlaaMaxImpactTime().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMINIMPACTCOST.name(),
                search.getLlaaMinImpactCost() != null ? search.getLlaaMinImpactCost().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMAXIMPACTCOST.name(),
                search.getLlaaMaxImpactCost() != null ? search.getLlaaMaxImpactCost().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMINRANKING.name(),
                search.getLlaaMinRanking() != null ? search.getLlaaMinRanking().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAMAXRANKING.name(),
                search.getLlaaMaxRanking() != null ? search.getLlaaMaxRanking().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAASINCE.name(),
                search.getLlaaSince() != null ? dateFormat.format(search.getLlaaSince()) : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LLAAUNTIL.name(),
                search.getLlaaUntil() != null ? dateFormat.format(search.getLlaaUntil()) : StringPool.BLANK);

        // Filters Project
        //
        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.PROJECTNAME.name(),
                searchProject.getProjectName() != null ? searchProject.getProjectName() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.STATUS.name(), StringUtil.toStringListStr(searchProject.getStatus()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.CUSTOMERTYPES.name(), StringUtil.toStringListInt(searchProject.getCustomertypes()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.CUSTOMERS.name(), StringUtil.toStringListInt(searchProject.getCustomers()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.PROGRAMS.name(), StringUtil.toStringListInt(searchProject.getPrograms()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.CATEGORIES.name(), StringUtil.toStringListInt(searchProject.getCategories()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.PERFORMINGORGANIZATIONS.name(), StringUtil.toStringListInt(searchProject.getPerformingorgs()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.PROJECTMANAGERS.name(), StringUtil.toStringListInt(searchProject.getEmployeeByProjectManagers()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.FUNCTIONALMANAGERS.name(), StringUtil.toStringListInt(searchProject.getEmployeeByFunctionalManagers()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.SELLERS.name(), StringUtil.toStringListInt(searchProject.getSellers()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.LABELS.name(), StringUtil.toStringListInt(searchProject.getLabels()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.STAGEGATES.name(), StringUtil.toStringListInt(searchProject.getStageGates()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.CONTRACTTYPES.name(), StringUtil.toStringListInt(searchProject.getContractTypes()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.GEOGRAPHIES.name(), StringUtil.toStringListInt(searchProject.getGeography()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.CLASSIFICATIONLEVELS.name(), StringUtil.toStringListInt(searchProject.getClassificationsLevel()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.TECHNOLOGIES.name(), StringUtil.toStringListInt(searchProject.getTechnologies()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.SPONSORS.name(), StringUtil.toStringListInt(searchProject.getEmployeeBySponsors()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.FUNDINGSOURCES.name(), StringUtil.toStringListInt(searchProject.getFundingsources()));

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.INTERNALPROJECT.name(),
                searchProject.getInternalProject() != null ? searchProject.getInternalProject().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.ISGEOSELLING.name(),
                searchProject.getIsGeoSelling() != null ? searchProject.getIsGeoSelling().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.ISGEOSELLING.name(),
                searchProject.getIsGeoSelling() != null ? searchProject.getIsGeoSelling().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.RAG.name(),
                searchProject.getRag() != null ? searchProject.getRag().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.ISINDIRECTSELLER.name(),
                searchProject.getIsIndirectSeller() != null ? searchProject.getIsIndirectSeller().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.MINPRIORITY.name(),
                searchProject.getFirstPriority() != null ? searchProject.getFirstPriority().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.MAXPRIORITY.name(),
                searchProject.getLastPriority() != null ? searchProject.getLastPriority().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.MINRISKRATING.name(),
                searchProject.getFirstRiskRating() != null ? searchProject.getFirstRiskRating().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.MAXRISKRATING.name(),
                searchProject.getLastRiskRating() != null ? searchProject.getLastRiskRating().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.INCLUDINGADJUSTMENT.name(),
                searchProject.getIncludingAdjustament() != null ? searchProject.getIncludingAdjustament().toString() : StringPool.BLANK);

        saveConfiguration(searchProject.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.BUDGETYEAR.name(),
                searchProject.getBudgetYear() != null ? searchProject.getBudgetYear().toString() : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.SINCE.name(),
                searchProject.getSince() != null ? dateFormat.format(searchProject.getSince()) : StringPool.BLANK);

        saveConfiguration(search.getUser(), Configurations.TYPE_LEARNED_LESSON,
                Configurations.LearnedLesson.UNTIL.name(),
                searchProject.getUntil() != null ? dateFormat.format(searchProject.getUntil()) : StringPool.BLANK);

    }
}

