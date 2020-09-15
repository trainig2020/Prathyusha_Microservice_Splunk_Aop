package io.prathyusha.departmentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.prathyusha.departmentservice.model.Department;
import io.prathyusha.departmentservice.model.Employee;
import io.prathyusha.departmentservice.model.EmployeeList;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static Logger log=LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	  @Autowired
	  private RestTemplate restTemplate;
	  
	  
	  @Override 
	  public EmployeeList getAllEmployees(int eDid) { 
		  log.info("EmployeeServiceImpl - getAllEmployees");
	  EmployeeList listEmp =restTemplate.getForObject("http://employee-service:8087/employees/listEmp/"+eDid, EmployeeList.class); 
	  return listEmp;
	  }
	  
	
	  @Override
	  public Employee getEmployee(int empId) { 
		  log.info("EmployeeServiceImpl - getEmployee");
	  Employee employee=restTemplate.getForObject("http://employee-service:8087/employees/"+empId,Employee.class); 
	  return employee;
	  }
	  
	 // @HystrixCommand(fallbackMethod = "getFallbackAddEmp") 
	  @Override
	  public Employee addEmployee(Employee employee,int eDid) { 
		  log.info("EmployeeServiceImpl - addEmployee");
	  return restTemplate.postForObject("http://employee-service:8087/employees/"+eDid+"/addEmployee",employee, Employee.class); 
	  }
	  
	
	
	  @Override
	  public void updateEmployee(Employee employee,int eDid,int empId) {
		  log.info("EmployeeServiceImpl - updateEmployee");
	  restTemplate.put("http://employee-service:8087/employees/"+eDid+ "/updateEmployee/"+empId,employee);
	  }
	 
	  @Override 
	  public void deleteEmployee(int eDid) {
		  log.info("EmployeeServiceImpl - deleteEmployees");
	  restTemplate.delete("http://employee-service:8087/employees/deleteEmployee/"+eDid, eDid);
	  
	  }
	
	   @Override 
		  public void deleteSingleEmployee(int eDid, int empId) {
		   log.info("EmployeeServiceImpl - deleteSingleEmployee");
		  restTemplate.delete("http://employee-service:8087/employees/deleteAll/"+eDid+ "/"+empId);
		  
		  }
	   
	   
	   public Employee getFallbackAddEmp(@RequestBody Employee employee) {
		   return new Employee(0, "No Employee available", 0, 0); 
		   }
		 
	
}

