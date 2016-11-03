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
 * File: ProfileDTOConverterTest.java
 * Create User: javier.hernandez
 * Create Date: 07/09/2015 18:29:54
 */

package es.sm2.openppm.api.converter.test;

import es.sm2.openppm.api.converter.common.ProfileDTOConverter;
import es.sm2.openppm.api.model.common.ProfileDTO;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public class ProfileDTOConverterTest {

    private ProfileDTOConverter profileDTOConverter;

    @Before
    public void init() {

        // Instance converter
        profileDTOConverter = new ProfileDTOConverter();
    }

    @Test
    public void testToDTO() {

        // BD instance
        Employee employee = new Employee();
        employee.setIdEmployee(1);
        employee.setPerformingorg(new Performingorg());
        employee.getPerformingorg().setIdPerfOrg(2);
        employee.getPerformingorg().setName("PO Name");
        employee.setResourceprofiles(new Resourceprofiles());
        employee.getResourceprofiles().setIdProfile(Resourceprofiles.Profile.PMO.getID());
        employee.getResourceprofiles().setProfileName(Resourceprofiles.Profile.PMO.name());


        ProfileDTO dto = profileDTOConverter.toDTO(employee);

        // Check conversion
        Assert.assertEquals("ProfileDTO DTO getCodeEmployee not equals", employee.getIdEmployee(), dto.getCodeEmployee());
        Assert.assertEquals("ProfileDTO DTO getPerformingOrganization name not equals", employee.getPerformingorg().getName(), dto.getPerformingOrganization().getName());
        Assert.assertEquals("ProfileDTO DTO getPerformingOrganization code not equals", employee.getPerformingorg().getIdPerfOrg(), dto.getPerformingOrganization().getCode());
        Assert.assertEquals("ProfileDTO DTO getCode not equals", employee.getResourceprofiles().getIdProfile(), dto.getCode().intValue());
        Assert.assertEquals("ProfileDTO DTO getName not equals", employee.getResourceprofiles().getProfileName(), dto.getName());
    }


}
