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
 * File: ChartWBS.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.util.*;

import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.wrap.WbsNodeWrap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.logic.impl.ActivitysellerLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.logic.impl.WBSNodeLogic;
import es.sm2.openppm.core.logic.impl.WBSTemplateLogic;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Wbstemplate;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;

/**
 * @author javier.hernandez
 *
 */
public class ChartWBS {
	
	private Wbsnode wbsNodeParent;
	private Wbstemplate wbstemplateParent;
	private ResourceBundle idioma;
	private boolean showPoc;
	private double globalPoc;
	private double globalBudget;
	private HashMap<String, String> settings;
	
	public ResourceBundle getIdioma() {
		return idioma;
	}

	public void setIdioma(ResourceBundle idioma) {
		this.idioma = idioma;
	}

	public ChartWBS(Wbsnode wbsNodeParent, boolean showPoc, ResourceBundle idioma) {
		this.wbsNodeParent = wbsNodeParent;
		this.showPoc = showPoc;
		this.idioma = idioma;
	}

	public ChartWBS(Wbsnode wbsNodeParent, boolean showPoc, Double globalPoc, Double globalBudget, ResourceBundle idioma, HashMap<String, String> settings) {
		this.wbsNodeParent = wbsNodeParent;
		this.showPoc = showPoc;
		this.setGlobalPoc(globalPoc == null?0:globalPoc);
		this.setGlobalBudget(globalBudget == null?0:globalBudget);
		this.idioma = idioma;
        this.settings = settings;
	}

	public ChartWBS(Wbstemplate wbstemplateParent) {
		this.setWbstemplateParent(wbstemplateParent);
	}

	public Wbsnode getWbsNodeParent() {
		return wbsNodeParent;
	}

	public void setWbsNodeParent(Wbsnode wbsNodeParent) {
		this.wbsNodeParent = wbsNodeParent;
	}
	
	public boolean isShowPoc() {
		return showPoc;
	}

	public void setShowPoc(boolean showPoc) {
		this.showPoc = showPoc;
	}

	/**
	 * Generate JSON code for view chart
     *
	 * @return JSONObject
	 * @throws LogicException 
	 */
	public JSONObject generate() throws Exception {

        // Parse WbsNodeWrap to JSON Object
        return generateWbsNodeWrap().toJSONObject();
	}

    /**
     * Generate WbsNodeWrap code for view StatusReport Chart
     *
     * @return WbsNodeWrap
     * @throws Exception
     */
    public WbsNodeWrap generateWbsNodeWrap() throws Exception {
        int model = 0;

        WBSNodeLogic wbsNodeLogic 	= new WBSNodeLogic();
        WbsNodeWrap nodeWrap = null;

        if (wbsNodeParent != null && wbsNodeParent.getName() != null) {

            List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(wbsNodeParent);

            if (showPoc) {

                model = modelWBS(childs);

                if (SettingUtil.getBoolean(settings, GeneralSetting.POC_EXCLUDE_CA_NO_BUDGET) && model == 2) {
                    model = 1;
                }

				if (model == 1) {
					globalPoc = globalPoc / 100;
				}
				else {
					globalPoc = calcPocZeroValues(childs, totalCA(childs)).get(0);
				}

				ArrayList<Double> arrayPoc = new ArrayList<Double>();

				arrayPoc.add(0, null);
				arrayPoc.add(1, globalPoc);

                nodeWrap = createNodeJSON(wbsNodeParent, arrayPoc);
			}
			else {
                nodeWrap = createNodeJSON(wbsNodeParent, new ArrayList<Double>());
			}

			if (!childs.isEmpty()) {
                nodeWrap.setChilds(generateWBSJSON(childs, 0, showPoc, model, totalCA(childs)));
			}
		}
        return nodeWrap;
    }


    private int totalCA(List<Wbsnode> wbsnodes) throws Exception {
		
		int counter = 0;
		
		for(Wbsnode wbsnode : wbsnodes){
			
			if(wbsnode.getIsControlAccount()){
				counter++;
			}
			else {
				WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
				
				List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(wbsnode);
				
				counter += totalCA(childs);
			}
		}
		
		return counter;
	}

	/**
	 * Generate JSON tree nodes
     *
	 * @param wbsnodes
	 * @param level
	 * @param showPoc
	 * @return
	 * @throws Exception
	 */
	private List<WbsNodeWrap> generateWBSJSON(List<Wbsnode> wbsnodes, int level, Boolean showPoc, int model, int totalCA) throws Exception {

        double poc;
		
		WBSNodeLogic wbsNodeLogic 	= new WBSNodeLogic();

        List<WbsNodeWrap> childsNodewrap = new ArrayList<WbsNodeWrap>();
		
		//MODEL 1: NOT ZERO VALUES
		if(model == 1){
			for (Wbsnode child: wbsnodes) {
				poc = 0;
				ArrayList<Double> realPoc 	= new ArrayList<Double>();
				JSONObject childJSON 		= null;
                WbsNodeWrap nodeWrap        = null;
				
				List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(child);
				
				if (showPoc) {

					Set<Projectactivity> activitys = child.getProjectactivities();

					for (Projectactivity activity: activitys) {

						if(activity.getPoc() != null){
							poc = activity.getPoc()/100;
						}
					}
					
					if(child.getIsControlAccount()){

						ArrayList<Double> arrayPoc = new ArrayList<Double>();

                        arrayPoc.add(0, null);
						arrayPoc.add(1, poc);
						
						realPoc = arrayPoc;
					}
					else if(!child.getIsControlAccount() && !childs.isEmpty()){

						realPoc = calcPocNotZeroValues(childs);

						// Control NaN
                        Double pocTemp = realPoc.get(1)/getSumBAC(childs);

                        if (!pocTemp.isNaN()){
                            realPoc.set(1, pocTemp);
                        }
                        else {
                            realPoc.set(1, 0.0);
                        }
					}
				}

                nodeWrap = createNodeJSON(child, showPoc ? realPoc : new ArrayList<Double>());

				if (!childs.isEmpty()) {
                    nodeWrap.setChilds(generateWBSJSON(childs, level + 1, showPoc, model, totalCA));
				}

                if (nodeWrap != null) { childsNodewrap.add(nodeWrap); }
			}
		}
		// MODEL 2: ZERO VALUES
		else {

			for (Wbsnode child: wbsnodes) {

				poc = 0;
				ArrayList<Double> realPoc 	= new ArrayList<Double>();
				JSONObject childJSON 		= null;
                WbsNodeWrap nodeWrap        = null;
				
				List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(child);
				
				if (showPoc) {

					Set<Projectactivity> activitys = child.getProjectactivities();

					for (Projectactivity activity: activitys) {

						if(activity.getPoc() != null){
							poc = activity.getPoc()/100;
						}
					}
					
					if(child.getIsControlAccount()){

						ArrayList<Double> arrayPoc = new ArrayList<Double>();

						arrayPoc.add(0, null);
						arrayPoc.add(1, poc);
						
						realPoc = arrayPoc;
					}
					else if(!child.getIsControlAccount() && !childs.isEmpty()){
						realPoc = calcPocZeroValues(childs, totalCA); 
					}
				}

                nodeWrap = createNodeJSON(child, showPoc ? realPoc : new ArrayList<Double>());
				
				if (!childs.isEmpty()) {
                    nodeWrap.setChilds(generateWBSJSON(childs, level + 1, showPoc, model, totalCA));
				}

                if (nodeWrap != null) { childsNodewrap.add(nodeWrap); }
			}
			
		}
		
		return childsNodewrap;
	}

	private int modelWBS(List<Wbsnode> wbsnodes) throws Exception {

		int model = 1;
		
		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
		
		for(Wbsnode wbsnode : wbsnodes){
			
			if(wbsnode.getIsControlAccount()){

				if(wbsnode.getBudget() == null){

					model = 2;

					return model;
				}
				else if(wbsnode.getBudget() == 0.0){

					model = 2;

					return model;
				}
			}
			else {

				List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(wbsnode);
				
				model = modelWBS(childs);
				
				if(model == 2){
					return model;
				}
			}
		}
		
		return model;
	}

	/**
	 * Create JSON node
	 * @param child
	 * @param pocs
	 * @return
	 * @throws Exception 
	 */
	private WbsNodeWrap createNodeJSON(Wbsnode child, ArrayList<Double> pocs) throws Exception {

		ProjectActivityLogic projectActivityLogic 	= new ProjectActivityLogic(getSettings(), getIdioma());
		ActivitysellerLogic activitysellerLogic 	= new ActivitysellerLogic();
		List<String> joins 							= new ArrayList<String>();
		List<Activityseller> activitysellers 		= null;
		String sellers 								= new String();
		
		String code = ValidateUtil.isNullCh(child.getCode(), StringPool.BLANK);
		String name = code + StringPool.SPACE + child.getName();

        WbsNodeWrap nodeWrap = new WbsNodeWrap(code, name);

        nodeWrap.setType(child.getIsControlAccount() == true ? Constants.CONTROL_ACCOUNT : Constants.WORK_GROUP);

		// When showPoc = false (plan) do not visualize pocs (pocs=empty)
		if(!pocs.isEmpty()){
			// When showPoc = true (control) fill and visualize both pocs
			if (pocs.get(0) != null) {
                nodeWrap.setPocRelative(pocs.get(0));
			}

			if (pocs.get(1) != null) {
                nodeWrap.setPocAbsolute(pocs.get(1));
			}
		}

		/* Seller */
		List<Projectactivity> activitys = projectActivityLogic.findByRelation(Projectactivity.WBSNODE, child);
		joins.add(Activityseller.SELLER);

		for (Projectactivity activity : activitys) {
			
			if (activity.getHasSellers()) {
				activitysellers = activitysellerLogic.findSellerAssociatedProject(activity, joins);
				
				for (Activityseller activityseller : activitysellers) {
					sellers += activityseller.getSeller().getName() + StringPool.SPACE;
				}
				
				if (activitysellers.size() > 0) {
                    nodeWrap.setSeller(getIdioma().getString("seller")+ StringPool.COLON_SPACE  + sellers);
				}
			}
		}

        // Remark CA with no budget
        if (child.getIsControlAccount() && (child.getBudget() == null || child.getBudget().equals(0.0))) {
            nodeWrap.setNoBudget(true);
        }

		return nodeWrap;
	}
	
	/**
	 * Calc POC for model 1, not zero values
	 * @param childs
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Double> calcPocNotZeroValues(List<Wbsnode> childs) throws Exception {
		
		double pocRelative	= 0;
		double pocAbsolute	= 0;
		
		ArrayList<Double> results = new ArrayList<Double>();
		
		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
		
		for (Wbsnode child : childs) {
			
			List<Wbsnode> childsForChild = wbsNodeLogic.findChildsWbsnode(child);
			
			if (child.getIsControlAccount()) {
				Set<Projectactivity> activitys = child.getProjectactivities();
				for (Projectactivity activity: activitys) {
					if(getGlobalBudget() != 0.0){
						pocRelative += (activity.getEv() == null ? 0.0 : activity.getEv()/getGlobalBudget());
						pocAbsolute += (activity.getPoc()== null ? 0.0 : activity.getEv());
					}
				}
			}
			else if (!childsForChild.isEmpty()) {

				ArrayList<Double> tempResults = calcPocNotZeroValues(childsForChild);
				
				pocRelative += tempResults.get(0) == null ? 0 : tempResults.get(0);
				pocAbsolute += tempResults.get(1) == null ? 0 : tempResults.get(1);
			}
			
		}
		
		results.add(0, pocRelative);
		results.add(1, pocAbsolute);
		
		return results;
	}
	
	
	/**
	 * Sum of list wbsnodes BAC
	 * @param childs
	 * @return
	 * @throws Exception 
	 */
	private Double getSumBAC(List<Wbsnode> childs) throws Exception {

		Double totalBAC = 0.0;
		
		for(Wbsnode child : childs){
			
			if(child.getIsControlAccount()){
				totalBAC += child.getBudget() == null ? 0.0 : child.getBudget();
			}
			else {

				WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
				
				List<Wbsnode> childsForChild = wbsNodeLogic.findChildsWbsnode(child);
				
				totalBAC += getSumBAC(childsForChild);
			}
			
		}
		
		return totalBAC;
	}
	

	/**
	 * Calc POC for model 2, zero values
     *
	 * @param childs
	 * @param totalCA
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Double> calcPocZeroValues(List<Wbsnode> childs, int totalCA) throws Exception {
		
		double pocRelative	= 0;
		double pocAbsolute	= 0;
		
		ArrayList<Double> results = new ArrayList<Double>();
		
		WBSNodeLogic wbsNodeLogic = new WBSNodeLogic();
		
		for (Wbsnode child : childs) {
			
			List<Wbsnode> childsForChild = wbsNodeLogic.findChildsWbsnode(child);
			
			if (child.getIsControlAccount()) {

				for (Projectactivity activity: child.getProjectactivities()) {

					if (activity.getPoc() != null) {

						pocRelative += (activity.getPoc()/totalCA)/100;
						pocAbsolute += (activity.getPoc()== null ? 0 : activity.getPoc())/100;
					}
				}
			}
			else if (!childsForChild.isEmpty()) {

				ArrayList<Double> tempResults = calcPocZeroValues(childsForChild, totalCA);
				
				pocRelative += tempResults.get(0) == null ? 0 : tempResults.get(0);
				pocAbsolute += tempResults.get(1) == null ? 0 : tempResults.get(1);
			}
		}
		
		results.add(0, pocRelative);
		results.add(1, pocAbsolute/childs.size());
		
		return results;
	}
	
	public void setGlobalPoc(double globalPoc) {
		this.globalPoc = globalPoc;
	}

	public double getGlobalPoc() {
		return globalPoc;
	}

	public void setGlobalBudget(double globalBudget) {
		this.globalBudget = globalBudget;
	}

	public double getGlobalBudget() {
		return globalBudget;
	}

	public Wbstemplate getWbstemplateParent() {
		return wbstemplateParent;
	}

	public void setWbstemplateParent(Wbstemplate wbstemplateParent) {
		this.wbstemplateParent = wbstemplateParent;
	}

	/**
	 * Generate JSON code for view chart
     *
	 * @return
	 * @throws Exception 
	 */
	public JSONObject generateTemplate() throws Exception {
		
		WBSTemplateLogic wbsTemplateLogic 	= new WBSTemplateLogic();
		JSONObject wbsJSON 					= new JSONObject();
		
		if (wbstemplateParent != null) {
			
			wbsJSON = createNodeTemplateJSON(wbstemplateParent);
			
			List<Wbstemplate> childs = wbsTemplateLogic.findChildsWbsnode(wbstemplateParent);
			
			if (!childs.isEmpty()) {
				wbsJSON.put("childs",generateWBSTemplateJSON(childs, 0));
			}
		}
		return wbsJSON;
	}

	
	/**
	 * Generate JSON tree nodes for template
     *
	 * @param childs
	 * @param level
	 * @return
	 * @throws Exception
	 */
	private JSONArray generateWBSTemplateJSON(List<Wbstemplate> childs, int level) throws Exception {
		
		WBSTemplateLogic wbsTemplateLogic 	= new WBSTemplateLogic();
		JSONArray childsJSON 				= new JSONArray();
		
		for (Wbstemplate child : childs) {

			JSONObject childJSON = null;
			
			List<Wbstemplate> childrenOfTheChild = wbsTemplateLogic.findChildsWbsnode(child);
			
			childJSON = createNodeTemplateJSON(child);
			
			if (!childrenOfTheChild.isEmpty()) {
				childJSON.put("childs", generateWBSTemplateJSON(childrenOfTheChild, level + 1));
			}
			
			if (childJSON != null) { childsJSON.add(childJSON); }
		}
		
		return childsJSON;
		
	}

	/**
	 * Create JSON node for template
     *
	 * @param wbstemplate
	 * @return
	 */
	private JSONObject createNodeTemplateJSON(Wbstemplate wbstemplate) {
		
		String code = ValidateUtil.isNullCh(wbstemplate.getCode(), StringPool.BLANK);
		String name = code + StringPool.SPACE + wbstemplate.getName();
		
		JSONObject nodeJSON = new JSONObject();

		nodeJSON.put("name", name);
		nodeJSON.put("type", wbstemplate.getIsControlAccount() == true ? Constants.CONTROL_ACCOUNT : Constants.WORK_GROUP);
		
		return nodeJSON;
	}
	
	/**
	 * Generate JSON code for OBS chart
     *
	 * @return
	 * @throws LogicException 
	 */
	public JSONObject generateOBS() throws Exception {
		
		WBSNodeLogic wbsNodeLogic 	= new WBSNodeLogic();
        WbsNodeWrap wbsNodeWrap     = null;

        if (wbsNodeParent != null && wbsNodeParent.getName() != null) {

            List<Wbsnode> childs = wbsNodeLogic.findChildsWbsnode(wbsNodeParent);

            wbsNodeWrap = createNodeJSON(wbsNodeParent, new ArrayList<Double>());
			
			if (!childs.isEmpty()) {
                wbsNodeWrap.setChilds(generateOBSJSON(childs, 0));
			}
		}

        // Parse WbsNodeWrap to JSON Object
		return wbsNodeWrap.toJSONObject();
	}

	/**
	 * Generate JSON tree nodes for OBS
     *
	 * @param childs
	 * @param level
	 * @return
	 * @throws Exception 
	 */
	private List<WbsNodeWrap> generateOBSJSON(List<Wbsnode> childs, int level) throws Exception {
		
		WBSNodeLogic wbsNodeLogic 	= new WBSNodeLogic();
		JSONArray childsJSON 		= new JSONArray();

        List<WbsNodeWrap> childsNodewrap = new ArrayList<WbsNodeWrap>();
		
		for (Wbsnode child : childs) {

            WbsNodeWrap wbsNodeWrap = null;
			
			List<Wbsnode> childrenOfTheChild = wbsNodeLogic.findChildsObsnode(child);
			
			if (!childrenOfTheChild.isEmpty() && !child.getIsControlAccount()) {

                wbsNodeWrap = createNodeJSON(child, new ArrayList<Double>());

                wbsNodeWrap.setChilds(generateOBSJSON(childrenOfTheChild, level + 1));
			}
			else if (child.getIsControlAccount()) {

                wbsNodeWrap = createNodeJSON(child, new ArrayList<Double>());
				
				TeamMemberLogic teamMemberLogic 	= new TeamMemberLogic();
				ArrayList<PropertyRelation> list 	= new ArrayList<PropertyRelation>();
				List<String> joins 					= new ArrayList<String>();
				
				joins.add(Teammember.EMPLOYEE);
				joins.add(Teammember.EMPLOYEE + "." + Employee.CONTACT);
				
				for (Projectactivity projectactivity : child.getProjectactivities()) {
					
					list.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Teammember.PROJECTACTIVITY, projectactivity));
					list.add(new PropertyRelation(Constants.EQUAL_RESTRICTION, Teammember.STATUS, Constants.RESOURCE_ASSIGNED));
					
					List<Teammember> teammembers = teamMemberLogic.findByFilters(list, Teammember.DATEIN, Constants.ASCENDENT, joins);

                    wbsNodeWrap.setChilds(createNodesTeammembersJSON(teammembers));
				}
			}
			
			if (wbsNodeWrap != null) { childsNodewrap.add(wbsNodeWrap); }
		}
		
		return childsNodewrap;
	}

	/**
	 * Create JSON nodes for team members
     *
	 * @param teammembers
	 * @return
	 */
	private List<WbsNodeWrap> createNodesTeammembersJSON(List<Teammember> teammembers) {

        List<WbsNodeWrap> childsWbsNode = new ArrayList<WbsNodeWrap>();
		
		for (Teammember teammember : teammembers) {

			WbsNodeWrap wbsNodeWrap = new WbsNodeWrap(String.valueOf(teammember.getEmployee().getContact().getIdContact()),
                                                      teammember.getEmployee().getContact().getFullName());

            wbsNodeWrap.setType(Constants.OBS_TEAMMEMBER);

            childsWbsNode.add(wbsNodeWrap);
		}
		
		return childsWbsNode;
	}

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }
}
