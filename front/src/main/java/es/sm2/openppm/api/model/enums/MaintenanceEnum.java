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
 * File: MaintenanceEnum.java
 * Create User: jordi.ripoll
 * Create Date: 03/08/2015 10:11:53
 */

package es.sm2.openppm.api.model.enums;

import es.sm2.openppm.api.impl.maintenance.CategoryActionImpl;
import es.sm2.openppm.api.impl.maintenance.ClassificationLevelActionImpl;
import es.sm2.openppm.api.impl.maintenance.ContractTypeActionImpl;
import es.sm2.openppm.api.impl.maintenance.CustomerActionImpl;
import es.sm2.openppm.api.impl.maintenance.CustomerTypeActionImpl;
import es.sm2.openppm.api.impl.maintenance.FundingSourceActionImpl;
import es.sm2.openppm.api.impl.maintenance.GeographyActionImpl;
import es.sm2.openppm.api.impl.maintenance.JobCategoryActionImpl;
import es.sm2.openppm.api.impl.maintenance.KnowledgeAreaActionImpl;
import es.sm2.openppm.api.impl.maintenance.LabelActionImpl;
import es.sm2.openppm.api.impl.maintenance.MaintenanceAction;
import es.sm2.openppm.api.impl.maintenance.PerformingOrganizationActionImpl;
import es.sm2.openppm.api.impl.maintenance.PoolActionImpl;
import es.sm2.openppm.api.impl.maintenance.ProfileActionImpl;
import es.sm2.openppm.api.impl.maintenance.SellerActionImpl;
import es.sm2.openppm.api.impl.maintenance.SkillActionImpl;
import es.sm2.openppm.api.impl.maintenance.StageGateActionImpl;
import es.sm2.openppm.api.impl.maintenance.StakeholderClassificationActionImpl;
import es.sm2.openppm.api.impl.maintenance.TechnologyActionImpl;
import es.sm2.openppm.api.impl.maintenance.TimelineTypeActionImpl;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public enum MaintenanceEnum {
    PROFILE(ProfileActionImpl.class),
    KNOWLEDGE_AREA(KnowledgeAreaActionImpl.class),
    STAKEHOLDER_CLASSIFICATION(StakeholderClassificationActionImpl.class),
    CONTRACT_TYPE(ContractTypeActionImpl.class),
    GEOGRAPHIC_AREA(GeographyActionImpl.class),
    CUSTOMER(CustomerActionImpl.class),
    FUNDING_SOURCE(FundingSourceActionImpl.class),
    CUSTOMER_TYPE(CustomerTypeActionImpl.class),
    CATEGORY(CategoryActionImpl.class),
    PERFORMING_ORGANIZATION(PerformingOrganizationActionImpl.class),
    LABEL(LabelActionImpl.class),
    STAGE_GATE(StageGateActionImpl.class),
    CLASSIFICATION_LEVEL(ClassificationLevelActionImpl.class),
    TECHNOLOGY(TechnologyActionImpl.class),
    POOL(PoolActionImpl.class),
    SKILL(SkillActionImpl.class),
    JOB_CATEGORY(JobCategoryActionImpl.class),
    SELLER(SellerActionImpl.class),
    TIMELINE_TYPE(TimelineTypeActionImpl.class);

    private Class<? extends MaintenanceAction> maintenanceAction;

    MaintenanceEnum(Class<? extends MaintenanceAction> maintenanceAction){

        this.maintenanceAction = maintenanceAction;
    }

    public Class<? extends MaintenanceAction>  getMaintenanceAction() {

        return maintenanceAction;
    }

}
