package io.prathyusha.departmentservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Employee {
	
	private int empId;
	//@Size(min = 2 ,max = 30)
	//@NotNull
	private String empName;
	 //@NotNull 
	 //@Min(18) 
	 //@Max(100)
	private int empAge;
	private int eDid;
	
	
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public int getEmpAge() {
		return empAge;
	}
	public void setEmpAge(int empAge) {
		this.empAge = empAge;
	}
	public int geteDid() {
		return eDid;
	}
	public void seteDid(int eDid) {
		this.eDid = eDid;
	}
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(int empId, String empName, int empAge, int eDid) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empAge = empAge;
		this.eDid = eDid;
	}
	
	
	
	
	
	
	
	
	
	
	

}
