package io.prathyusha.depempmvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.splunk.Receiver;
import com.splunk.Service;

import io.prathyusha.depempmvc.model.Department;
import io.prathyusha.depempmvc.model.DepartmentList;
import io.prathyusha.depempmvc.model.Employee;
import io.prathyusha.depempmvc.model.EmployeeList;

@RestController
public class DepEmpController {
	
	Service service;
	Receiver receive;
	

	private static final Logger logger = LoggerFactory.getLogger(DepEmpController.class);

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/DeptList")
	public ModelAndView getAllDepartments(HttpServletRequest request, @RequestParam(required = false) Integer page) {

		
		
		/*
		 * System.out.println("In Controller"); logger.info("Get All Departments ");
		 * service = SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller getting all departments...");
		 */
		//logger.warn("If there is problem in service then show errors... ");
		DepartmentList deptlist = restTemplate.getForObject("http://gateway-service/Department/listDept",DepartmentList.class);
		System.out.println(deptlist.getDeptList().get(0));
		List<Department> lstdept = new ArrayList<>();
		

		for (int i = 0; i < deptlist.getDeptList().size(); i++) {
			lstdept.add(deptlist.getDeptList().get(i));
		}
		for (Department department : lstdept) {
			System.out.println(department.getDeptId() + department.getDeptName());
		}
		
		
		ModelAndView modelAndView = new ModelAndView("home");

		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lstdept);
		pagedListHolder.setPageSize(3);
		modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		modelAndView.addObject("page", page);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		}
		HttpSession session = request.getSession();
		session.setAttribute("DeptList", lstdept);
		session.setAttribute("page", page);
		// modelAndView.addObject("DeptList", lstdept);
        modelAndView.addObject("homepage", "main");
	    return modelAndView;
	}
	

	public DepartmentList getListOfDepartments() {
		return restTemplate.getForObject("http://gateway-service/Department/listDept", DepartmentList.class);
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/NewDepartment", method = RequestMethod.GET)
	public ModelAndView newDepartment(HttpServletRequest request) {
		String Register = "newform";
		HttpSession session1 = request.getSession();
		List<Department> lst = (List<Department>) session1.getAttribute("DeptList");
		session1.setAttribute("DeptList", lst);
		ModelAndView modelAndView = new ModelAndView();
		
		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lst);
		 
		pagedListHolder.setPageSize(3);
		
		 Integer page = pagedListHolder.getPageCount();
	     System.out.println(page);
		 session1.setAttribute("pageAdd", page);
		 
		modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
		//Integer page =  (Integer) session1.getAttribute("page");
		 System.out.println( pagedListHolder.getPageCount());
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;
        modelAndView.addObject("page", page);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		}
		
		
		modelAndView.addObject("Register", Register);
		modelAndView.addObject("createdept", "newdept");
		modelAndView.setViewName("home");
		Department department = new Department();
		modelAndView.addObject("department", department);
		return modelAndView;
		
	}
	

	@RequestMapping(value = "/CreateDepartment", method = RequestMethod.POST)
	public ModelAndView insertDepartment(@ModelAttribute Department department,HttpServletRequest request) {
		/*
		 * logger.info("Adding a department..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Add New Department...");
		 */
		restTemplate.postForObject("http://gateway-service/Department/addDepartment", department, Department.class);
		 HttpSession session6 = request.getSession();
	     List<Department> lst = (List<Department>) session6.getAttribute("DeptList");
	     Integer page = (Integer) session6.getAttribute("pageAdd");
	     PagedListHolder<Department> plh= new PagedListHolder<Department>(lst);
	     int pagerowvalue =  plh.getNrOfElements();
	        System.out.println("Page in Dept "+page);
	          if((pagerowvalue %3) ==0) {
	              return new ModelAndView("redirect:/DeptList?page="+(page+1));
	          }
	          else {
	             return new ModelAndView("redirect:/DeptList?page="+page);
	          }
		
		
	}
	

	public Department addDepartment(Department department) {
		return restTemplate.postForObject("http://gateway-service/Department/addDepartment", department,
				Department.class);
	}

	@RequestMapping(value = "/UpdateDepartment", method = RequestMethod.POST)
	public ModelAndView updateDepartment(@ModelAttribute Department department, HttpServletRequest request) {
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		/*
		 * logger.info("Updating a department..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Update Department...");
		 */
		restTemplate.put("http://gateway-service/Department/updateDepartment/" + deptId, department);
		 HttpSession session7 = request.getSession();
	     Integer page = (Integer) session7.getAttribute("page");
	     if (page != null) {
			 return new ModelAndView("redirect:/DeptList?page="+page);
		 }
		 return new ModelAndView("redirect:/DeptList");
		
	}
	

	public void updateDept(Department department, int deptId) {
		restTemplate.put("http://gateway-service/Department/updateDepartment/" + deptId, department);
	}
	

	@RequestMapping(value = "/DeleteDepartment")
	public ModelAndView deleteDepartment(HttpServletRequest request,HttpServletResponse response) {
		 HttpSession session2 = request.getSession();
		int deptid = Integer.parseInt(request.getParameter("deptId"));
		/*
		 * logger.info("Delete department ..."); service =
		 * SplunkConnection.getService(); receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Delete Department...");
		 */
		deleteDept(deptid);
		Integer page=(Integer)session2.getAttribute("page");
		if(page!=null) {
			return new ModelAndView("redirect:/DeptList?page="+page);
		}
		return new ModelAndView("redirect:/DeptList");
	}

	public void deleteDept(int deptId) {
		restTemplate.delete("http://gateway-service/Department/deleteDepartment/" + deptId);
	}

	 @SuppressWarnings("unchecked")
	 @RequestMapping(value = "/GetDepartment", method = RequestMethod.GET)
	 public ModelAndView getDepartmentId(HttpServletRequest request) {
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		HttpSession session2 = request.getSession();
		List<Department> lst = (List<Department>) session2.getAttribute("DeptList");
		ModelAndView modelAndView = new ModelAndView("home");
		
		
		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lst);
		pagedListHolder.setPageSize(3);
		modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
		Integer page =  (Integer) session2.getAttribute("page");
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		modelAndView.addObject("page", page);

		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			modelAndView.addObject("DeptList", pagedListHolder.getPageList());
		}
		//modelAndView.addObject("DeptList", lst);
		modelAndView.addObject("departmentId", deptId);
		return modelAndView;
	}
	 

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showdepartments", method = RequestMethod.GET)
	public ModelAndView showDepartments(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) Integer page) {
		HttpSession session3 = request.getSession();
		List<Department> lstdept1 = (List<Department>) session3.getAttribute("DeptList");
		session3.setAttribute("DeptListemp", lstdept1);
		 session3.setAttribute("pageEmp", page);
		ModelAndView modelAndView = new ModelAndView("home");
		
		modelAndView.addObject("DeptListemp", lstdept1);
		int deptId = lstdept1.get(0).getDeptId();
		modelAndView.addObject("name", "names");
		if (page != null) {
			 return new ModelAndView("redirect:/emplist?deptId="+deptId+"&page="+page);
		 }
		 return new ModelAndView("redirect:/emplist?deptId="+deptId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/emplist")
	public ModelAndView getAllEmployees(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer page) {
		int deptId = Integer.parseInt(request.getParameter("deptId"));
		List<Employee> lstemp = new ArrayList<>();
		/*
		 * logger.info("Get Employee List..."); service = SplunkConnection.getService();
		 * receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller getting all Employees...");
		 */
		EmployeeList lst = restTemplate.getForObject("http://gateway-service/Department/" + deptId + "/employees",
				EmployeeList.class);
		for (int i = 0; i < lst.getListOfEmployees().size(); i++) {
			lstemp.add(lst.getListOfEmployees().get(i));
		}
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("EmpList", lstemp);
		httpSession.setAttribute("page1", page);
		List<Department> lstdept1 = (List<Department>) httpSession.getAttribute("DeptList");
		ModelAndView modelAndView = new ModelAndView("home");
		
		PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lstemp);
		pagedListHolder.setPageSize(2);

		modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
		// Integer page =Integer.valueOf(request.getParameter("page")) ;
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		modelAndView.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			modelAndView.addObject("EmpList", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			modelAndView.addObject("EmpList", pagedListHolder.getPageList());
		}
		
		modelAndView.addObject("DeptId", deptId);
		modelAndView.addObject("DeptListemp", lstdept1);
		//modelAndView.addObject("EmpList", lstemp);
		modelAndView.addObject("homepage", "emppage");
		modelAndView.addObject("name", "names");
		return modelAndView;
	}
	

	public EmployeeList getAllDepartments(int deptId) {
		return restTemplate.getForObject("http://gateway-service/Department/" + deptId + "/employees",
				EmployeeList.class);
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newEmployee", method = RequestMethod.GET)
	public ModelAndView newContact(HttpServletRequest request, ModelAndView modelAndView) {
		String Register = "NewForm";
		HttpSession session1 = request.getSession();
		List<Employee> lst = (List<Employee>) session1.getAttribute("EmpList");
		ModelAndView model = new ModelAndView("home");

		
		  PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lst); 
		  pagedListHolder.setPageSize(2);
		  Integer page = pagedListHolder.getPageCount();
			 session1.setAttribute("pageAdd", page);
		  
		  model.addObject("maxPages", pagedListHolder.getPageCount());
		  System.out.println( pagedListHolder.getPageCount());
		 // Integer page = (Integer) session1.getAttribute("page"); 
		  if (page == null || page < 1 || page > pagedListHolder.getPageCount()) 
			  page = 1;
		  
		  model.addObject("page", page);
		  if (page == null || page < 1 || page >  pagedListHolder.getPageCount()) {
			  pagedListHolder.setPage(0);
		  model.addObject("EmpList", pagedListHolder.getPageList());
		  } else if (page <= pagedListHolder.getPageCount()) {
			  pagedListHolder.setPage(page - 1);
		  model.addObject("EmpList", pagedListHolder.getPageList()); }
		 
		// model.addObject("EmpList", lst);
		model.addObject("Register", Register);
		model.addObject("insertEmployee", "newemployee");
		model.addObject("homepage", "emppage");
		return model;
	}
	

	public Employee addEmployee(Employee employee, int eDid) {
		/*
		 * logger.info("Add Employee..."); service = SplunkConnection.getService();
		 * receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Add New Employee...");
		 */
		return restTemplate.postForObject("http://gateway-service/Department/employees/" + eDid + "/addEmployee",
				employee, Employee.class);
	}

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public ModelAndView saveEmployee(@ModelAttribute Employee employee, HttpServletRequest request,
			HttpServletResponse response) {
		int eDid = Integer.parseInt(request.getParameter("eDid"));
		restTemplate.postForObject("http://gateway-service/Department/employees/" + eDid + "/addEmployee", employee,
				Employee.class);
		HttpSession session8 = request.getSession();
		List<Employee> lst =(List<Employee>) session8.getAttribute("EmpList");
		Integer page = (Integer) session8.getAttribute("pageAdd");
		PagedListHolder<Employee> plh= new PagedListHolder<Employee>(lst);
	     int pagerowvalue =  plh.getNrOfElements();
	        System.out.println("Page in Dept "+page);
		 if (pagerowvalue%2==0) {
			 return new ModelAndView("redirect:/emplist?deptId="+eDid+"&page="+(page+1));
		 }
		 else {
		return new ModelAndView("redirect:/emplist?deptId="+eDid+"&page="+(page));	
		 }
		//return new ModelAndView("redirect:/emplist?deptId=" + eDid);
	}

	@RequestMapping(value = "/deleteEmployee")
	public ModelAndView deleteEmployee(HttpServletRequest request) {
		 HttpSession session9 = request.getSession();
		int employeeId = Integer.parseInt(request.getParameter("id"));
		int edid = Integer.parseInt(request.getParameter("did"));
		deleteEmp(employeeId, edid);
		
		
		  Integer page=(Integer)session9.getAttribute("page1"); 
		  if(page!=null) {
			  return new ModelAndView("redirect:/emplist?deptId=" +edid + "&page=" +(page)); 
			  } 
		  return new ModelAndView("redirect:/emplist?deptId=" + edid);
		 
		//return new ModelAndView("redirect:/emplist?deptId=" + edid);
	}

	public void deleteEmp(int employeeId, int edid) {
		/*
		 * logger.info("Delete employee.."); service = SplunkConnection.getService();
		 * receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Delete Employee...");
		 */
		restTemplate.delete("http://gateway-service/Department/employees/" + edid + "/deleteEmployee/" + employeeId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
	public ModelAndView editContact(HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("id"));
		int did = Integer.parseInt(request.getParameter("did"));
		HttpSession session2 = request.getSession();
		List<Employee> lst = (List<Employee>) session2.getAttribute("EmpList");
		session2.setAttribute("EmpList", lst);
		ModelAndView model = new ModelAndView("home");
		Integer page = (Integer) session2.getAttribute("page1");
		
		 PagedListHolder<Employee> pagedListHolder = new
				  PagedListHolder<Employee>(lst); pagedListHolder.setPageSize(2);
				  
				  model.addObject("maxPages", pagedListHolder.getPageCount()); 
				//  Integer page =(Integer) session2.getAttribute("page");
				  System.out.println( pagedListHolder.getPageCount());
				  if (page == null || page < 1 || page
				  > pagedListHolder.getPageCount()) page = 1;
				  
				  model.addObject("page", page); if (page == null || page < 1 || page >
				  pagedListHolder.getPageCount()) { pagedListHolder.setPage(0);
				  model.addObject("EmpList", pagedListHolder.getPageList()); } else if (page <=
				  pagedListHolder.getPageCount()) { pagedListHolder.setPage(page - 1);
				  model.addObject("EmpList", pagedListHolder.getPageList()); }
				 
		
		model.addObject("homepage", "emppage");
	  //model.addObject("EmpList", lst);
		model.addObject("employeeid", employeeId);
		model.addObject("Did", did);
		return model;
	}

	@RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
	public ModelAndView updateEmployee(@ModelAttribute Employee employee, HttpServletRequest request) {
		int employeeId = Integer.parseInt(request.getParameter("empId"));
		int did = Integer.parseInt(request.getParameter("eDid"));
		/*
		 * logger.info("Update Employee..."); service = SplunkConnection.getService();
		 * receive= service.getReceiver();
		 * receive.log("main","In DepEmpMvc controller Update Employee...");
		 */
		restTemplate.put("http://gateway-service/Department/employees/" + did + "/updateEmployee/" + employeeId,
				employee);
		System.out.println("In Method Update");
		HttpSession session9 = request.getSession();
		Integer page = (Integer) session9.getAttribute("page1");
		 if (page != null) {
			 return new ModelAndView("redirect:/emplist?deptId="+did+"&page="+page);
		 }
		return new ModelAndView("redirect:/emplist?deptId="+did);
		
	}

	public void updateEmp(Employee employee, int employeeId, int did) {
		
		restTemplate.put("http://gateway-service/Department/employees/" + did + "/updateEmployee/" + employeeId,
				employee);
	}

}
