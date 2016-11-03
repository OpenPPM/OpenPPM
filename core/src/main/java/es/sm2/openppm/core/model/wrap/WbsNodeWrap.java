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
 * File: WbsNodeWrap.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.wrap;

import es.sm2.openppm.core.reports.ReportDataSource;
import es.sm2.openppm.core.reports.beans.WbsnodeReport;
import es.sm2.openppm.utils.functions.ValidateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier.hernandez on 01/12/2014.
 */
public class WbsNodeWrap {

    private String code;
    private String name;
    private String type;
    private Double pocRelative;
    private Double pocAbsolute;
    private String seller;
    private Boolean noBudget;
    private List<WbsNodeWrap> childs;

    public WbsNodeWrap(String code, String name) {
        this.code = code;
        this.name = name;
        childs = new ArrayList<WbsNodeWrap>();
        noBudget = false;
    }

    public void addChild(WbsNodeWrap child) {

        getChilds().add(child);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPocRelative() {
        return pocRelative;
    }

    public void setPocRelative(Double pocRelative) {
        this.pocRelative = pocRelative;
    }

    public Double getPocAbsolute() {
        return pocAbsolute;
    }

    public void setPocAbsolute(Double pocAbsolute) {
        this.pocAbsolute = pocAbsolute;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Boolean getNoBudget() {
        return noBudget;
    }

    public void setNoBudget(Boolean noBudget) {
        this.noBudget = noBudget;
    }

    public List<WbsNodeWrap> getChilds() {
        return childs;
    }

    public void setChilds(List<WbsNodeWrap> childs) {
        this.childs = childs;
    }

    /**
     * Method to convert nodeWBSWrap to JSONObject
     *
     */
    public JSONObject toJSONObject() {

        JSONObject node = new JSONObject();
        node.put("name", this.getName());
        node.put("type", this.getType());

        if (this.getPocRelative() != null){

            DecimalFormat decimalFormat = new DecimalFormat("###.##%");
            node.put("pocRelative", decimalFormat.format(this.getPocRelative()));

        }

        if (this.getPocAbsolute() != null){

            DecimalFormat decimalFormat = new DecimalFormat("###.##%");
            node.put("pocAbsolute", decimalFormat.format(this.getPocAbsolute()));

        }

        node.put("seller", this.getSeller());
        node.put("noBudget", this.getNoBudget());

        if (ValidateUtil.isNotNull(this.getChilds())) {

            JSONArray childs = new JSONArray();

            for (WbsNodeWrap nodeWrap : this.getChilds()) {
                childs.add(nodeWrap.toJSONObject());
            }

            node.put("childs", childs);

        }

        return node;
    }

    /**
     * Method to parse List<WbsNodeWrap> to List<WbsnodeReport>
     *
     */
    public List<WbsnodeReport> toListWbsNodeReport(List<WbsNodeWrap> wbsNodeWraps){

        List<WbsnodeReport> wbsnodeReports = new ArrayList<WbsnodeReport>();
        DecimalFormat decimalFormat        = new DecimalFormat("###.##%");

        for (WbsNodeWrap wbsNodeWrap : wbsNodeWraps){

            if (ValidateUtil.isNotNull(wbsNodeWrap.getChilds())) {

                List<WbsnodeReport> childsWbsNodeReport = new ArrayList<WbsnodeReport>();

                for (WbsNodeWrap nodeWrap : wbsNodeWrap.getChilds()) {
                    childsWbsNodeReport.add(nodeWrap.toWbsNodeReport());
                }
            }

            WbsnodeReport wbsnodeReport = new WbsnodeReport(wbsNodeWrap.getName(),
                                                            decimalFormat.format(wbsNodeWrap.getPocAbsolute()),
                                                            new ReportDataSource<WbsnodeReport>(toListWbsNodeReport(wbsNodeWrap.getChilds())),
                                                            String.valueOf(wbsNodeWrap.getChilds().size()));
            wbsnodeReports.add(wbsnodeReport);
        }


        return wbsnodeReports;
    }

    /**
     * Method to parse WbsNodeWrap to WbsnodeReport
     *
      */
    public WbsnodeReport toWbsNodeReport(){

        List<WbsnodeReport> childsWbsNodeReport = new ArrayList<WbsnodeReport>();
        DecimalFormat decimalFormat             = new DecimalFormat("###.##%");

        if (ValidateUtil.isNotNull(this.getChilds())) {

            for (WbsNodeWrap nodeWrap : this.getChilds()) {
                childsWbsNodeReport.add(nodeWrap.toWbsNodeReport());
            }
        }

        WbsnodeReport wbsnodeReport = new WbsnodeReport(this.getName(),
                                                        decimalFormat.format(this.getPocAbsolute()),
                                                        new ReportDataSource<WbsnodeReport>(childsWbsNodeReport),
                                                        String.valueOf(childsWbsNodeReport.size()));

        return wbsnodeReport;
    }

}
