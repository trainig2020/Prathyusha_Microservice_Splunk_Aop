package io.prathyusha.departmentservice.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.splunk.Receiver;
import com.splunk.Service;

import io.prathyusha.departmentservice.model.Department;
import io.prathyusha.departmentservice.model.DepartmentList;
import io.prathyusha.departmentservice.model.Employee;
import io.prathyusha.departmentservice.model.EmployeeList;
//import io.prathyusha.departmentservice.model.DepartmentList;
import io.prathyusha.departmentservice.service.DepartmentService;
import io.prathyusha.departmentservice.service.EmployeeService;



@RestController
@RequestMapping("/Department")
public class DepartmentController {
	
	Service service;
	Receiver receive;
	
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private DepartmentService departmentService;
	
	
    @Autowired 
	private EmployeeService employeeService;
	 
	
   @RequestMapping("/listDept")
		public DepartmentList getAllDepartments(){
		List<Department> list = departmentService.getAllDepartments();
		/*
		 * logger.info("Get All Departments....."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In Department controller getting List of Departments..."
		 * );
		 */
		DepartmentList listOfDepartments = new DepartmentList();
		listOfDepartments.setDeptList(list);
	    return listOfDepartments;	
	}
	
	@RequestMapping("/{deptId}")
	   public Optional<Department> getDepartment(@PathVariable("deptId") int deptId) {
		/*
		 * logger.info("Get Department by Id....."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller getting Departments by particular deptId..."
		 * );
		 */
		return departmentService.getDepartment(deptId);
	}
	
	
	@PostMapping("/addDepartment")
	    public Department addDepartment(@RequestBody Department department) {
		/*
		 * logger.info("Add Department....."); service = SplunkConnection.getService();
		 * receive= service.getReceiver();
		 * receive.log("main","In Department controller Add New Department...");
		 */
		return departmentService.addDepartment(department);
	}
	
	
	 @PutMapping("/updateDepartment/{deptId}")
	      public void updateDepartment(@RequestBody Department department, @PathVariable int deptId) { 
		/*
		 * logger.info("Update Department by Id...."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller Update Department by particular deptId...");
		 */
		  department.setDeptId(deptId);
	      departmentService.updateDepartment(department);
	  }
	
	@DeleteMapping("/deleteDepartment/{deptId}")
	      public void deleteDepartment(@PathVariable int deptId) {
		/*
		 * logger.info("Delete Department by Id...."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller Delete Department by particular deptId...");
		 */
		 departmentService.deleteDepartment(deptId);
	     employeeService.deleteEmployee(deptId);
	  }
	
	
	  @GetMapping("{eDid}/employees")
	  public EmployeeList getAllEmployees(@PathVariable int eDid){ 
	  EmployeeList lst =employeeService.getAllEmployees(eDid);
		/*
		 * logger.info("Getting Employees w.r.t DepartmentId.... "); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller getting List of Employees by particular eDid..."
		 * );
		 */
	  System.out.println(lst.getListOfEmployees().size()); 
	  System.out.println(lst.getListOfEmployees().size()); return lst;
	  
	  }
	   
	  @GetMapping("/employees/{empId}") public Employee getEmployee(@PathVariable int empId) {
		/*
		 * logger.info("Getting Employees w.r.t EmployeeId...."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller getting Employee by particular empId...");
		 */
		  return employeeService.getEmployee(empId); 
		  
	  }
	  
	  
	  
	  @PostMapping("/employees/{eDid}/addEmployee") 
	  public void addEmployee(@RequestBody Employee employee,@PathVariable int eDid) {
		/*
		 * logger.info("Add Employee in particular DepartmentId...."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller Add New Employee by particular eDid...");
		 */
	      employee.seteDid(eDid);
	     employeeService.addEmployee(employee,eDid);
	  }
	  
	  
	  
	  @PutMapping("/employees/{eDid}/updateEmployee/{empId}")
	  public void updateEmployee(@RequestBody Employee employee,@PathVariable int empId,@PathVariable int eDid) {
		/*
		 * logger.info("Update Employee by EmployeeId....."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller Update Employee by particular empId and eDid..."
		 * );
		 */
	      employee.setEmpId(empId);
	      employee.seteDid(eDid);
	      employeeService.updateEmployee(employee,eDid,empId);
	  }
	  
	  
	  @DeleteMapping("/employees/deleteEmployee/{eDid}")
	  public void deleteEmployee(@PathVariable int eDid) {
		/*
		 * logger.info("Delete Employee by DepartmentId...."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver(); receive.log(
		 * "main","In Department controller Delete Employees by particular eDid ...");
		 */
	      employeeService.deleteEmployee(eDid); 
	  }
	  
	  
	  @DeleteMapping("/employees/{eDid}/deleteEmployee/{empId}")
	  public void deleteSingleEmployee(@PathVariable int eDid,@PathVariable int empId) {
		/*
		 * logger.info("Delete Single Employee by EmployeeId and DepartmentId... ");
		 * service = SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log(
		 * "main","In Department controller Delete Single Employee by particular empId and eDid ..."
		 * );
		 */
	  employeeService.deleteSingleEmployee(eDid, empId); 
	  }
	  
	 
}


