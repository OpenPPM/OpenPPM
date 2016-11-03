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
 * File: SellerActionImpl.java
 * Create User: mariano.fontana
 * Create Date: 02/09/2015 09:40:12
 */

package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.api.converter.maintenance.SellerDTOConverter;
import es.sm2.openppm.api.model.common.ErrorDTO;
import es.sm2.openppm.api.model.maintenance.SellerDTO;
import es.sm2.openppm.core.logic.impl.SellerLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by mariano.fontana on 02/09/2015.
 */
public class SellerActionImpl implements MaintenanceAction {

    //TODO mariano.fontana - 02/09/2015 - implementar el resto de llamadas
    @Override
    public Response save(Object object, Company company) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response update(Object object, Company company) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response findAll(Company company, Employee user) {

        Response response;

        try{

            SellerLogic sellerLogic = new SellerLogic();

            //Find by Company
            List<Seller> sellers = sellerLogic.findByRelation(Customer.COMPANY, company, Seller.NAME, Order.ASC);

            //DTO Converter
            SellerDTOConverter sellerDTOConverter = new SellerDTOConverter();
            List <SellerDTO> sellersDTO = sellerDTOConverter.toDTOList(sellers);

            //Response construction
            response = Response.ok().entity(sellersDTO).build();

        }catch (Exception e){


            LogManager.getLog(this.getClass()).error(e);

            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage())).build();

        }

        return response;


    }

    @Override
    public Response findByID(Integer id) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response remove(Integer id, ResourceBundle resourceBundle) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
