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
 * File: ResourceCapacityRunningLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.ResourceCapacityRunning;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.javabean.Week;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.wrap.ResourceTimeWrap;
import es.sm2.openppm.core.utils.EntityComparator;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;


public class ResourceCapacityRunningLogic {

	/**
	 * Update capacity running
	 * 
	 * @param idProjects
	 * @param idPMs
	 * @param idJobCategories
	 * @param idResourcePools 
	 * @param idSellers 
	 * @param fullName
	 * @param since
	 * @param until
	 * @param order
	 * @param resourceBundle
	 * @param user
	 * @param idSellers 
	 * @param idCategories 
	 * @param showOperations 
	 * @return
	 * @throws Exception
	 */
	public ResourceCapacityRunning updateCapacityRunning(
			Integer[] idProjects, 
			Integer[] idPMs, 
			Integer[] idJobCategories,
			Integer[] idSellers, 
			Integer[] idResourcePools, 
			Integer[] idCategories, 
			String fullName,
			Date since, 
			Date until, 
			String order, 
			ResourceBundle resourceBundle, 
			Employee user, Boolean showOperations) throws Exception {
		
		ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
		
	 	List<String> listDates						= null;
    	List<TeamMembersFTEs> ftEs 					= null;
    	List<ResourceTimeWrap> resourceTimeWraps 	= null;
		
		EmployeeLogic employeeLogic 	= new EmployeeLogic();
		TimesheetLogic timesheetLogic 	= new TimesheetLogic();
		
		// Dates
		//
		if (since == null) {
			since = DateUtil.getFirstMonthDay(new Date());
		}
		
		if (until == null ) {
			
			Calendar untilCal = DateUtil.getCalendar();
			
			untilCal.add(Calendar.MONTH, +1);
			
			until = DateUtil.getLastMonthDay(untilCal.getTime());
		}
		
		// Resources by filters
		List<Employee> resources = employeeLogic.findInputedInProjects(user, idProjects, idPMs, idJobCategories,
                idResourcePools, idSellers, idCategories,fullName, since, until, Contact.FULLNAME, order);
		
		// Get timesheets in app3
		resourceTimeWraps = timesheetLogic.capacityRunning(resources, DateUtil.getFirstWeekDay(since),
                DateUtil.getLastWeekDay(until), showOperations, idProjects, user, idPMs);
		
		// Parse TimesheetWrap to TeamMembersFTEs
		ftEs = parse(resources, resourceTimeWraps, since, until);
		
		// List dates
		listDates = lisDates(since, until, resourceBundle);
		
		// Response
		if (ValidateUtil.isNotNull(listDates)) {
			resourceCapacityRunning.setListDates(listDates);
		}
		if (ValidateUtil.isNotNull(ftEs)) {
			resourceCapacityRunning.setFtEs(ftEs);
		}
		
		return resourceCapacityRunning;
	}

	/**
	 * Parse TimesheetWrap to TeamMembersFTEs
	 * @param resources
     * @param resourceTimeWraps
     * @param until
	 * @param since 
	 * @return
	 */
	private List<TeamMembersFTEs> parse(List<Employee> resources, List<ResourceTimeWrap> resourceTimeWraps, Date since, Date until) {
		
		List<TeamMembersFTEs> ftes 	= new ArrayList<TeamMembersFTEs>();
		
		for (Employee resource : resources) {
			
			// Weeks
			List<Week> listDates = listDates(since, until); 
			
			Teammember member 	= new Teammember();
			Employee employee 	= new Employee();
			Contact contact 	= new Contact();
			
			contact.setFullName(resource.getContact().getFullName());
			
			employee.setIdEmployee(resource.getIdEmployee());
			employee.setContact(contact);
			
			member.setEmployee(employee);
			
			TeamMembersFTEs fte = new TeamMembersFTEs(member);
			
			// Set hours
			List<Double> listHours = new ArrayList<Double>();
			
			int i;
			for (Week week : listDates) {
				
				boolean foundWeek = false;
				
				i = 0;
				while (!foundWeek && i < resourceTimeWraps.size()) {

					if (DateUtil.equals(week.getSinceDay().getTime(), resourceTimeWraps.get(i).getIniDate()) &&
							DateUtil.equals(week.getUntilDay().getTime(), resourceTimeWraps.get(i).getFinishDate()) &&
							resource.getIdEmployee().equals(resourceTimeWraps.get(i).getIdEmployee())
							) {
						
						foundWeek = true;
						
						listHours.add(resourceTimeWraps.get(i).getHours()); 
					}
					
					i++;
				}
				
				if (!foundWeek && i == resourceTimeWraps.size()) {
					listHours.add(0.0);
				}
				
			}
			
			// Set ftes
			double[] listHoursDouble 	= new double[listDates.size()];
			int[] listFtEsInt 			= new int[listDates.size()];
			
			int j = 0;
			for (Double hours : listHours) {
				
				listHoursDouble[j] 	= hours;
				//FIXME hay que modificar el default hours week por el que tenga el empleyoee en calendarbase
				listFtEsInt[j] 		= (int) (hours * 100 / Constants.DEFAULT_HOUR_WEEK);
				
				j++;
			}
			
			fte.setListHours(listHoursDouble);
			fte.setFtes(listFtEsInt);
			
			ftes.add(fte);
		}
		
		return ftes;
	}

	/**
	 * Return vector dates 
	 * 
	 * @param since
	 * @param until
	 * @return
	 */
	private List<Week> listDates(Date since, Date until) {
		
		List<Week> listDates = new ArrayList<Week>();
		
		Calendar sinceCal		= DateUtil.getCalendar();
		Calendar untilCal		= DateUtil.getCalendar();
	
		// Parse to calendar
		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
		untilCal.setTime(DateUtil.getLastWeekDay(until));
		
		// Dates 
		//
		while (!sinceCal.after(untilCal)) {

			Calendar sinceDay = DateUtil.getCalendar();
			sinceDay.setTime(sinceCal.getTime());
			
			Calendar untilDay = DateUtil.getLastWeekDay(sinceCal);
			
			Week week = new Week(sinceDay, untilDay);
			
			listDates.add(week);
			
			sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
		}
		
		return listDates;
	}

	/**
	 * Return string list dates by since and until
	 * 
	 * @param since
	 * @param until
	 * @param resourceBundle 
	 * @return
	 */
	private List<String> lisDates(Date since, Date until, ResourceBundle resourceBundle) {
		
		List<String> listDates = new ArrayList<String>();
		
		Calendar sinceCal		= DateUtil.getCalendar();
		Calendar untilCal		= DateUtil.getCalendar();
	
		SimpleDateFormat dfYear = new SimpleDateFormat("yy");
		
		// Parse to calendar
		sinceCal.setTime(DateUtil.getFirstWeekDay(since));
		untilCal.setTime(DateUtil.getLastWeekDay(until));
		
		// Dates 
		//
		while (!sinceCal.after(untilCal)) {

			int sinceDay 		= sinceCal.get(Calendar.DAY_OF_MONTH);
			
			Calendar calWeek 	= DateUtil.getLastWeekDay(sinceCal);
			
			int untilDay 		= calWeek.get(Calendar.DAY_OF_MONTH);
			
			listDates.add(sinceDay + StringPool.DASH + untilDay + StringPool.SPACE + 
					resourceBundle.getString("month.min_"+(calWeek.get(Calendar.MONTH)+1)) + 
					StringPool.SPACE + dfYear.format(calWeek.getTime()));
			
			sinceCal.add(Calendar.WEEK_OF_MONTH, 1);
		}
		
		return listDates;
	}

	/**
	 * Update capacity planning resource by project
	 * 
	 * @param idEmployee
	 * @param since
	 * @param until
	 * @param resourceBundle
	 * @param user
     * @param settings
     * @return
	 * @throws Exception
	 */
	public ResourceCapacityRunning updateCapacityRunningResourceByProject(Integer idEmployee, Date since, Date until,
                                                                          ResourceBundle resourceBundle, Employee user,
                                                                          HashMap<String, String> settings) throws Exception {
		
		ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
	
		List<String> listDates						= null;
		List<TeamMembersFTEs> ftEs 					= null;
		List<ResourceTimeWrap> resourceTimeWraps 	= null;
		
		TimesheetLogic timesheetLogic 	= new TimesheetLogic();
		EmployeeLogic employeeLogic		= new EmployeeLogic();
		
		// Dates
		//
		if (since == null) {
			since = DateUtil.getFirstMonthDay(new Date());
		}
		
		if (until == null ) {
			
			Calendar untilCal = DateUtil.getCalendar();
			
			untilCal.add(Calendar.MONTH, +1);
			
			until = DateUtil.getLastMonthDay(untilCal.getTime());
		}
		
		// Resource
		List<String> joins = new ArrayList<String>();
		
		joins.add(Employee.CONTACT);
		
		Employee resource = employeeLogic.findById(idEmployee, joins);
		
		// Get timesheets in app3 by resources and projects and dates
		resourceTimeWraps = timesheetLogic.capacityRunningResourceByProject(resource, DateUtil.getFirstWeekDay(since),
                DateUtil.getLastWeekDay(until), user, settings);
		
		// Select projects and sort
		//
		Hashtable<String, String> hashProjects = new Hashtable<String, String>();
		
		for (ResourceTimeWrap resourceTimeWrap : resourceTimeWraps) {
			
			if (resourceTimeWrap.getProjectName() != null) {
				hashProjects.put(resourceTimeWrap.getProjectName(), resourceTimeWrap.getProjectName());
			}
		}
		
		List<Project> projects = new ArrayList<Project>();
		
		for (String projectName : hashProjects.keySet()) {
			
			Project project = new Project();
			
			project.setProjectName(projectName);
			
			projects.add(project);
		}
		
		Collections.sort(projects, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));

		// Select operations and sort
		//
		Hashtable<String, String> hashOperations = new Hashtable<String, String>();
		
		for (ResourceTimeWrap resourceTimeWrap : resourceTimeWraps) {
			
			if (ValidateUtil.isNotNull(resourceTimeWrap.getOperation())) {
				hashOperations.put(resourceTimeWrap.getOperation(), resourceTimeWrap.getOperation());
			}
		}
		
		List<Operation> operations = new ArrayList<Operation>();
		
		for (String operationName : hashOperations.keySet()) {
			
			Operation operation = new Operation();
			
			operation.setOperationName(operationName);
			
			operations.add(operation);
		}
		
		Collections.sort(operations, new EntityComparator<Operation>(new Order(Operation.OPERATIONNAME, Order.ASC)));
		
		// Parse TimesheetWrap to TeamMembersFTEs
		ftEs = parseByProjects(projects, operations, resourceTimeWraps, since, until);
		
		// List dates
		listDates = lisDates(since, until, resourceBundle);
		
		// Response
		if (ValidateUtil.isNotNull(listDates)) {
			resourceCapacityRunning.setListDates(listDates);
		}
		if (ValidateUtil.isNotNull(ftEs)) {
			resourceCapacityRunning.setFtEs(ftEs);
		}
		
		return resourceCapacityRunning;
	}
	
	/**
	 *  Update capacity running resource by job category
	 * 
	 * @param idEmployee
	 * @param since
	 * @param until
	 * @param resourceBundle
	 * @param user
	 * @param settings
     * @return
	 * @throws Exception 
	 */
	public ResourceCapacityRunning updateCapacityRunningResourceByJobCategory(
            Integer idEmployee, Date since, Date until,
            ResourceBundle resourceBundle, Employee user, HashMap<String, String> settings) throws Exception {
		
		ResourceCapacityRunning resourceCapacityRunning = new ResourceCapacityRunning();
			
		List<String> listDates						= null;
		List<TeamMembersFTEs> ftEs 					= null;
		List<ResourceTimeWrap> resourceTimeWraps 	= null;
		
		// Declare logic
		TimesheetLogic timesheetLogic 	= new TimesheetLogic();
		EmployeeLogic employeeLogic		= new EmployeeLogic();
		
		// Dates
		//
		if (since == null) {
			since = DateUtil.getFirstMonthDay(new Date());
		}
		
		if (until == null ) {
			
			Calendar untilCal = DateUtil.getCalendar();
			
			untilCal.add(Calendar.MONTH, +1);
			
			until = DateUtil.getLastMonthDay(untilCal.getTime());
		}
		
		// Resource
		List<String> joins = new ArrayList<String>();
		
		joins.add(Employee.CONTACT);
		
		Employee resource = employeeLogic.findById(idEmployee, joins);
		
		// Logic
		resourceTimeWraps = timesheetLogic.capacityRunningResourceByJobCategory(resource, 
				DateUtil.getFirstWeekDay(since), DateUtil.getLastWeekDay(until), user, settings);
		
		// Select projects and sort
		Hashtable<String, String> hashProjects = new Hashtable<String, String>();
		
		for (ResourceTimeWrap resourceTimeWrap : resourceTimeWraps) {
			if (resourceTimeWrap.getProjectName() != null) {
				hashProjects.put(resourceTimeWrap.getProjectName(), resourceTimeWrap.getProjectName());
			}
		}
		
		List<Project> projects = new ArrayList<Project>();
		
		for (String projectName : hashProjects.keySet()) {
			
			Project project = new Project();
			
			project.setProjectName(projectName);
			
			projects.add(project);
		}
		
		Collections.sort(projects, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));
		
		// Select job categories and sort
		//
		Hashtable<String, String> hashJobCategories = new Hashtable<String, String>();
		
		for (ResourceTimeWrap resourceTimeWrap : resourceTimeWraps) {
			
			if (ValidateUtil.isNotNull(resourceTimeWrap.getJobCategory())) {
				hashJobCategories.put(resourceTimeWrap.getJobCategory(), resourceTimeWrap.getJobCategory());
			}
		}
		
		List<Jobcategory> jobCategories = new ArrayList<Jobcategory>();
		
		for (String jobCategoryName : hashJobCategories.keySet()) {
			
			Jobcategory jobcategory = new Jobcategory();
			
			jobcategory.setName(jobCategoryName);
			
			jobCategories.add(jobcategory);
		}
		
		Collections.sort(jobCategories, new EntityComparator<Jobcategory>(new Order(Jobcategory.NAME, Order.ASC)));
		
		// Select operations and sort
		//
		Hashtable<String, String> hashOperations = new Hashtable<String, String>();
		
		for (ResourceTimeWrap resourceTimeWrap : resourceTimeWraps) {
			
			if (ValidateUtil.isNotNull(resourceTimeWrap.getOperation())) {
				hashOperations.put(resourceTimeWrap.getOperation(), resourceTimeWrap.getOperation());
			}
		}
		
		List<Operation> operations = new ArrayList<Operation>();
		
		for (String operationName : hashOperations.keySet()) {
			
			Operation operation = new Operation();
			
			operation.setOperationName(operationName);
			
			operations.add(operation);
		}
		
		Collections.sort(operations, new EntityComparator<Operation>(new Order(Operation.OPERATIONNAME, Order.ASC)));
		
		// Parse TimesheetWrap to TeamMembersFTEs
		ftEs = parseByJobCategories(jobCategories, operations, projects, resourceTimeWraps, since, until);
		
		
		// List dates
		listDates = lisDates(since, until, resourceBundle);
		
		// Response
		if (ValidateUtil.isNotNull(listDates)) {
			resourceCapacityRunning.setListDates(listDates);
		}
		if (ValidateUtil.isNotNull(ftEs)) {
			resourceCapacityRunning.setFtEs(ftEs);
		}
		
		return resourceCapacityRunning;
	}

	/**
	 * Parse TimesheetWrap to TeamMembersFTEs
	 * 
	 * @param projects
	 * @param operations 
	 * @param resourceTimeWraps
	 * @param since
	 * @param until
	 * @return
	 */
	private List<TeamMembersFTEs> parseByProjects(List<Project> projects, List<Operation> operations, List<ResourceTimeWrap> resourceTimeWraps, Date since, Date until) {
		
		List<TeamMembersFTEs> ftes 	= new ArrayList<TeamMembersFTEs>();
		
		// Projects 
		//
		for (Project project : projects) {
			
			boolean setProject = false;
			
			// Weeks
			List<Week> listDates = listDates(since, until); 
			
			Teammember member 	= new Teammember();
			Employee employee 	= new Employee();
			
			member.setEmployee(employee);
			
			TeamMembersFTEs fte = new TeamMembersFTEs(member);
			
			List<Double> listHours = new ArrayList<Double>();
			
			// Set hours
			int i;
			for (Week week : listDates) {
				
				boolean foundWeek = false;
				
				i = 0;
				while (!foundWeek && i < resourceTimeWraps.size()) {
					
					// Projects
					if (DateUtil.equals(week.getSinceDay().getTime(), resourceTimeWraps.get(i).getIniDate()) &&
							DateUtil.equals(week.getUntilDay().getTime(), resourceTimeWraps.get(i).getFinishDate()) &&
							project.getProjectName().equals(resourceTimeWraps.get(i).getProjectName()) 
							) {
						
						foundWeek 	= true;
						setProject 	= true;
						
						listHours.add(resourceTimeWraps.get(i).getHours()); 
						
						// set project name and project manager name
						if (setProject) {
							
							Project newProject = new Project();
							
							newProject.setProjectName(resourceTimeWraps.get(i).getProjectName());
							
							Employee employeeByProjectManager = new Employee();
							
							Contact contact = new Contact();
							
							contact.setFullName(resourceTimeWraps.get(i).getFullName());
							
							employeeByProjectManager.setContact(contact);
							
							newProject.setEmployeeByProjectManager(employeeByProjectManager);
							
							fte.setProject(newProject);
						}
					}
					
					i++;
				}
				
				if (!foundWeek && i == resourceTimeWraps.size()) {
					listHours.add(0.0);
				}
			}
			
			// Set ftes
			double[] listHoursDouble 	= new double[listDates.size()];
			int[] listFtEsInt 			= new int[listDates.size()];
			
			int j = 0;
			for (Double hours : listHours) {
				
				listHoursDouble[j] 	= hours;
				
				//FIXME hay que modificar el default hours week por el que tenga el empleyoee en calendarbase, traerlo en la select
				listFtEsInt[j] 		= (int) (hours * 100 / Constants.DEFAULT_HOUR_WEEK);
				
				j++;
			}
			
			fte.setListHours(listHoursDouble);
			fte.setFtes(listFtEsInt);
			
			ftes.add(fte);
		}
		
		// Operations 
		//
		for (Operation operation : operations) {
			
			boolean setOperation = false;
			
			// Weeks
			List<Week> listDates = listDates(since, until); 
			
			Teammember member 	= new Teammember();
			Employee employee 	= new Employee();
			
			member.setEmployee(employee);
			
			TeamMembersFTEs fte = new TeamMembersFTEs(member);
			
			List<Double> listHours = new ArrayList<Double>();
			
			// Set hours
			int i;
			for (Week week : listDates) {
				
				boolean foundWeek = false;
				
				i = 0;
				while (!foundWeek && i < resourceTimeWraps.size()) {
					
					if (DateUtil.equals(week.getSinceDay().getTime(), resourceTimeWraps.get(i).getIniDate()) &&
							DateUtil.equals(week.getUntilDay().getTime(), resourceTimeWraps.get(i).getFinishDate()) &&
							operation.getOperationName().equals(resourceTimeWraps.get(i).getOperation()) 
							) {
						
						foundWeek 		= true;
						setOperation 	= true;
						
						listHours.add(resourceTimeWraps.get(i).getHours()); 
						
						// Set operation
						if (setOperation) {
							
							Operation tempOperation = new Operation();
							
							tempOperation.setOperationName(resourceTimeWraps.get(i).getOperation());
							
							fte.setOperation(tempOperation);
						}
					}
					
					i++;
				}
				
				if (!foundWeek && i == resourceTimeWraps.size()) {
					listHours.add(0.0);
				}
			}
			
			// Set ftes
			double[] listHoursDouble 	= new double[listDates.size()];
			int[] listFtEsInt 			= new int[listDates.size()];
			
			int j = 0;
			for (Double hours : listHours) {
				
				listHoursDouble[j] 	= hours;
				
				//FIXME hay que modificar el default hours week por el que tenga el empleyoee en calendarbase, traerlo en la select
				listFtEsInt[j] 		= (int) (hours * 100 / Constants.DEFAULT_HOUR_WEEK);
				
				j++;
			}
			
			fte.setListHours(listHoursDouble);
			fte.setFtes(listFtEsInt);
			
			ftes.add(fte);
		}
		
		return ftes;
	}
	
	/**
	 * Parse TimesheetWrap to TeamMembersFTEs
	 * 
	 * @param jobCategories 
	 * @param operations 
	 * @param projects
	 * @param resourceTimeWraps
	 * @param since
	 * @param until
	 * @return
	 */
	private List<TeamMembersFTEs> parseByJobCategories(List<Jobcategory> jobCategories, List<Operation> operations, List<Project> projects, List<ResourceTimeWrap> resourceTimeWraps, Date since, Date until) {
		
		List<TeamMembersFTEs> ftes 	= new ArrayList<TeamMembersFTEs>();
		
		// Job categories 
		//
		for (Jobcategory jobCategory : jobCategories) {
			
			boolean setJobCategory = false;
			
			// Weeks
			List<Week> listDates = listDates(since, until); 
			
			TeamMembersFTEs fte = new TeamMembersFTEs();
			
			List<Double> listHours = new ArrayList<Double>();
			
			Hashtable<String, Project> projectsByJobHash = new Hashtable<String, Project>();
			
			// Set hours
			int i;
			for (Week week : listDates) {
				
				boolean foundWeek = false;
				
				i = 0;
				while (!foundWeek && i < resourceTimeWraps.size()) {
					
					if (DateUtil.equals(week.getSinceDay().getTime(), resourceTimeWraps.get(i).getIniDate())
							&& DateUtil.equals(week.getUntilDay().getTime(), resourceTimeWraps.get(i).getFinishDate())
							&& jobCategory.getName().equals(resourceTimeWraps.get(i).getJobCategory())
							&& ValidateUtil.isNull(resourceTimeWraps.get(i).getOperation()) 
							) {
						
						foundWeek 		= true;
						setJobCategory 	= true;
						
						listHours.add(resourceTimeWraps.get(i).getHours()); 
						
						// set job category and project names
						if (setJobCategory) {
							
							Jobcategory newJobCategory = new Jobcategory();
							
							newJobCategory.setName(resourceTimeWraps.get(i).getJobCategory());
							
							Teammember member = new Teammember();
							
							member.setJobcategory(newJobCategory);
							
							// Filter projects by job
							for (Project project : projects) {
								
								if (project.getProjectName().equals(resourceTimeWraps.get(i).getProjectName())) {
									projectsByJobHash.put(project.getProjectName(), project);
								}
							}
							
							fte.setMember(member);
						}
					}
					
					i++;
				}
				
				if (!foundWeek && i == resourceTimeWraps.size()) {
					listHours.add(0.0);
				}
			}
			
			// Set ftes
			double[] listHoursDouble 	= new double[listDates.size()];
			int[] listFtEsInt 			= new int[listDates.size()];
			
			int j = 0;
			for (Double hours : listHours) {
				
				listHoursDouble[j] 	= hours;
				
				//FIXME hay que modificar el default hours week por el que tenga el empleyoee en calendarbase, traerlo en la select
				listFtEsInt[j] 		= (int) (hours * 100 / Constants.DEFAULT_HOUR_WEEK);
				
				j++;
			}
			
			fte.setListHours(listHoursDouble);
			fte.setFtes(listFtEsInt);
			
			// Set projects by job and sort
			ArrayList<Project> projectsByJob = new ArrayList<Project>(projectsByJobHash.values());
			
			Collections.sort(projectsByJob, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));
			
			fte.setProjectNames(projectsByJob);
			
			
			ftes.add(fte);
		}
	
		// Operations 
		//
		for (Operation operation : operations) {
			
			boolean setOperation = false;
			
			// Weeks
			List<Week> listDates = listDates(since, until); 
			
			TeamMembersFTEs fte = new TeamMembersFTEs();
			
			List<Double> listHours = new ArrayList<Double>();
			
			Hashtable<String, Project> projectsByOp = new Hashtable<String, Project>();
			
			// Set hours
			int i;
			for (Week week : listDates) {
				
				boolean foundWeek = false;
				
				i = 0;
				while (!foundWeek && i < resourceTimeWraps.size()) {
					
					if (DateUtil.equals(week.getSinceDay().getTime(), resourceTimeWraps.get(i).getIniDate()) &&
							DateUtil.equals(week.getUntilDay().getTime(), resourceTimeWraps.get(i).getFinishDate()) &&
							operation.getOperationName().equals(resourceTimeWraps.get(i).getOperation())
							) {
						
						foundWeek 		= true;
						setOperation 	= true;
						
						listHours.add(resourceTimeWraps.get(i).getHours()); 
						
						// Set operation
						if (setOperation) {
							
							Operation tempOperation = new Operation();
							
							tempOperation.setOperationName(resourceTimeWraps.get(i).getOperation());
							
							fte.setOperation(tempOperation);
						}
					}
					
					i++;
				}
				
				if (!foundWeek && i == resourceTimeWraps.size()) {
					listHours.add(0.0);
				}
			}
			
			// Set ftes
			double[] listHoursDouble 	= new double[listDates.size()];
			int[] listFtEsInt 			= new int[listDates.size()];
			
			int j = 0;
			for (Double hours : listHours) {
				
				listHoursDouble[j] 	= hours;
				
				//FIXME hay que modificar el default hours week por el que tenga el empleyoee en calendarbase, traerlo en la select
				listFtEsInt[j] 		= (int) (hours * 100 / Constants.DEFAULT_HOUR_WEEK);
				
				j++;
			}
			
			fte.setListHours(listHoursDouble);
			fte.setFtes(listFtEsInt);
			
			// Set projects by job and sort
			ArrayList<Project> projectsByJob = new ArrayList<Project>(projectsByOp.values());
			
			Collections.sort(projectsByJob, new EntityComparator<Project>(new Order(Project.PROJECTNAME, Order.ASC)));
			
			fte.setProjectNames(projectsByJob);
			
			ftes.add(fte);
		}
		
		return ftes;
	}
	

}
