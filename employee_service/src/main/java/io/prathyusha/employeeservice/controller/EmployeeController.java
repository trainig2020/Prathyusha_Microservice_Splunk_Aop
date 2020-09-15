package io.prathyusha.employeeservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.splunk.Receiver;
import com.splunk.Service;


import io.prathyusha.employeeservice.model.Employee;
import io.prathyusha.employeeservice.model.EmployeeList;
import io.prathyusha.employeeservice.service.EmployeeService;
@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	Service service;
	Receiver receive;
	
	private static Logger logger=LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	
	@GetMapping("/listEmp/{eDid}")
	public EmployeeList getAllEmployees(@PathVariable int eDid){
		/*
		 * logger.info("Getting all Employees ..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Employee controller getting all Employees bases on deptId...");
		 */
		return employeeService.getAllEmployess(eDid);
	}
	
	
	@GetMapping("/{empId}")
	public Optional<Employee> getEmployee(@PathVariable int empId) {
		/*
		 * logger.info("Getting an Employee with Id ..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In Employee controller getting particular empId...");
		 */
		return employeeService.GetEmployee(empId);
	}
	
	  @PostMapping("/{eDid}/addEmployee") 
	  public void addEmployee(@RequestBody Employee employee,@PathVariable int eDid) {
		/*
		 * logger.info("Add Employee ...");
		 * logger.info("Getting an Employee with Id ..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In Employee controller Add New Employee...");
		 */
	  employee.seteDid(eDid);
	  employeeService.addEmployee(employee); 
	  }
	 
	
	 @PutMapping("/{eDid}/updateEmployee/{empId}")
	 public void updateEmployee(@RequestBody Employee employee,@PathVariable int empId,@PathVariable int eDid ) {
		/*
		 * logger.info("Update Employee ..."); service = SplunkConnection.getService();
		 * receive= service.getReceiver(); receive.log(
		 * "main","In Employee controller update Employee with empId and deptId...");
		 */
		employee.setEmpId(empId);
		employee.seteDid(eDid);
		employeeService.updateEmployee(employee);
	 }
	
	
	  @DeleteMapping("/deleteEmployee/{eDid}") 
	   public void deleteEmployee(@PathVariable int eDid) {
		/*
		 * logger.info("Delete All Employees with deptId ..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In Employee controller Delete Employee with eDid...");
		 */
	      employeeService.deleteEmployee(eDid);
	    
	  }
	
	  @DeleteMapping("/deleteAll/{eDid}/{empId}") 
	  public void deleteEmployeeByEdidAndEmpid(@PathVariable int eDid,@PathVariable int empId)
	  { 
		/*
		 * logger.info("Deleting a single Employee with deptId and EmployeeId...");
		 * service = SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log(
		 * "main","In Employee controller Delete employee with eDid and empId...");
		 */
		  employeeService.deleteEmployeeByEdid(eDid, empId);
	}
	

}

