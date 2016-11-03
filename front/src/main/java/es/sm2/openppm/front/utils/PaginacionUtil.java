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
 * File: PaginacionUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import es.sm2.openppm.core.javabean.DatoColumna;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.core.logic.impl.DataTableLogic;
import es.sm2.openppm.utils.params.ParamUtil;

public class PaginacionUtil {
	
	private HttpServletRequest req;
	private ArrayList<DatoColumna> columnas;
	private ArrayList<String> joins;
	@SuppressWarnings("rawtypes")
	private Class tipo;
	@SuppressWarnings("rawtypes")
	private List lista;
	private JSONObject datos;
	private List<DatoColumna>filtros;

	@SuppressWarnings("rawtypes")
	public PaginacionUtil(HttpServletRequest req,ArrayList<DatoColumna > columnas,Class tipo){
		this.req = req;
		this.columnas = columnas;
		this.tipo = tipo;
		this.filtros = new ArrayList<DatoColumna>();
	};
	
	@SuppressWarnings("rawtypes")
	public PaginacionUtil(HttpServletRequest req,ArrayList<DatoColumna > columnas,Class tipo,ArrayList<String > joins){
		this.req = req;
		this.columnas = columnas;
		this.tipo = tipo;
		this.joins = joins;
		this.filtros = new ArrayList<DatoColumna>();
	};
	
	/**
	 * Añadir filtro
	 * @param filtro
	 */
	public void addFiltro(DatoColumna filtro) {
		filtros.add(filtro);
	}
	
	/**
	 * Lista con los datos filtrados
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List cargarLista() throws Exception {
		
		DataTableLogic dataTableLogic = new DataTableLogic();
		String sEcho = ParamUtil.getString(req, "sEcho");
		
		//Filtros extras
		List<DatoColumna> filtrosExtras = filtros;
		
		FiltroTabla filtro = crearFiltro();
		
		lista = dataTableLogic.findByFiltro(filtro, tipo, joins);
		
		int total = dataTableLogic.findTotal(filtrosExtras, tipo);
		int totalFiltered = dataTableLogic.findTotalFiltered(filtro, tipo);
		
		datos = new JSONObject();
		datos.put("sEcho", sEcho);
		datos.put("iTotalRecords", total);
		datos.put("iTotalDisplayRecords", totalFiltered);
		
		return lista;
		
	}
	
	/**
	 * Crea el filtro para aplicarlo
	 * @return
	 * @throws Exception
	 */
	public FiltroTabla crearFiltro() throws Exception {
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		// Paginacion
		int iDisplayStart = ParamUtil.getInteger(req, "iDisplayStart",-1);
		int iDisplayLength = ParamUtil.getInteger(req, "iDisplayLength",-1);
		
		// Ordenación por columnas
		int iSortingCols = ParamUtil.getInteger(req, "iSortingCols",-1); // Numero de columnas por las que se filtra
		int iSortCol_0 = ParamUtil.getInteger(req, "iSortCol_0", -1);
		
		List<DatoColumna> orderBy = new ArrayList<DatoColumna>();
		
		if ( iSortCol_0 >= 0) {
			
			for ( int i=0 ; i<  iSortingCols; i++ ) {
				
				int iSortCol = ParamUtil.getInteger(req, "iSortCol_"+i);
				
				if (ParamUtil.getBoolean(req, "bSortable_"+iSortCol)) {
					orderBy.add(
							new DatoColumna(columnas.get(iSortCol).getNombre(),
							columnas.get(iSortCol).getTipo(),
							ParamUtil.getString(req, "sSortDir_"+i))
						);
				}
				
			}
		}
		
		for (DatoColumna column : columnas) {
			
			if (column.getTipo() == List.class) {
				
				if (column.getSubTipo() != null && column.getSubTipo() == Date.class) {
					
					Date since = ParamUtil.getDate(req, column.getNombreList()[0], df,null);
					Date until = ParamUtil.getDate(req, column.getNombreList()[1], df,null);
					
					if (since != null && until != null) {
						
						Object[] objects = {since, until};
						column.setObjectList(objects);
						filtros.add(column);
					}
				}
				else {
					String[] values = ParamUtil.getStringValues(req, column.getNombre(), null);
					if (values != null) {
						column.setValorList(values);
						filtros.add(column);
					}
				}
			}
			else {
				String sSearch = ParamUtil.getString(req, column.getNombre(), "");
				
				if (!"".equals(sSearch)) {
					column.setValor(sSearch);
					filtros.add(column);
				}
			}
		}
		
		return new FiltroTabla(iDisplayStart, iDisplayLength, orderBy,filtros);
	}
	
	
	/**
	 * Obtenemos el objeto JSON para la tabla (Tablas simples)
	 * @param datosJSON
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String obtenerDatos(JSONArray datosJSON) throws UnsupportedEncodingException {

		this.datos.put("aaData", datosJSON);
		
		return this.datos.toString();
	}
	
	/**
	 * Obtenemos el objeto JSON para la tabla (Para tablas complejas)
	 * @param datosJSON
	 * @param total
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String obtenerDatos(JSONArray datosJSON,int total, int totalFiltrado, Double sumBudget, JSONObject infoJSON) throws UnsupportedEncodingException {
		
		String sEcho = ParamUtil.getString(req, "sEcho");
		
		datos = new JSONObject();
		datos.put("sEcho", sEcho);
		datos.put("iTotalRecords", total);
		datos.put("iTotalDisplayRecords", totalFiltrado);
		if (sumBudget != null) {
			datos.put("sumBudget", sumBudget);
		}
		this.datos.put("aaData", datosJSON);
		
		if (infoJSON != null) {
			this.datos.put("info", infoJSON);
		}
		
		String jsonOutput = this.datos.toString();
		byte[] byteArray = jsonOutput.getBytes("UTF8");

		return new String(byteArray);
	}

	public void setJoins(ArrayList<String> joins) {
		this.joins = joins;
	}

	public ArrayList<String> getJoins() {
		return joins;
	}

	public List<DatoColumna> getFiltros() {
		return filtros;
	}

	public void setFiltros(List<DatoColumna> filtros) {
		this.filtros = filtros;
	}
}
