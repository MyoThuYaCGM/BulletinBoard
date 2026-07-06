package com.example.test.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.test.dto.loginRequest;
import com.example.test.entity.Employee;
import com.example.test.service.EmailService;
import com.example.test.service.EmployeeService;
import com.example.test.service.ExcelService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	private final EmployeeService employeeService;
	private final ExcelService excelService;
	private final EmailService emailService;


	public HomeController(EmployeeService employeeService, ExcelService excelService, EmailService emailService) {
		this.employeeService = employeeService;
		this.excelService = excelService;
		this.emailService = emailService;
	}

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("loginRequest", new loginRequest());
		return "login";
	}

	@GetMapping("/employees")
	public String listEmployees(Model model) {
		model.addAttribute("employees", employeeService.getAllEmployees());
		return "employees";
	}

	@GetMapping("/employees/add")
	public String addEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "addEmployee";
	}

	@PostMapping("/employees/save")
	public String saveEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
		try {
			employeeService.saveEmployee(employee);
			return "redirect:/employees";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/employees/add";
		}
	}

	@GetMapping("/employees/edit/{id}")
	public String editEmployee(@PathVariable Long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		return "editEmployee";
	}

	@PostMapping("/employees/update")
	public String updateEmployee(@ModelAttribute Employee employee) {
		employeeService.saveEmployee(employee);
		return "redirect:/employees";
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return "redirect:/employees";
	}

	/*
	 * @GetMapping("/employees/download") public void
	 * downloadCsv(HttpServletResponse response) throws IOException {
	 * 
	 * csvService.downloadEmployees(response); }
	 * 
	 * @PostMapping("/employees/upload") public String
	 * uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
	 * 
	 * csvService.uploadEmployees(file);
	 * 
	 * return "redirect:/employees"; }
	 */
	@GetMapping("/employees/export")
	public void exportExcel(HttpServletResponse response)
	        throws IOException {

	    excelService.exportEmployees(response);
	}
	
	@PostMapping("/employees/import")
	public String importEmployees(@RequestParam("file") MultipartFile file)
	        throws IOException {

	    excelService.importEmployees(file);

	    return "redirect:/employees";
	}
	
	@PostMapping("/employees/preview")
	@ResponseBody
	public List<Employee> previewEmployees(
	        @RequestParam("file") MultipartFile file)
	        throws IOException {

	    return excelService.previewEmployees(file);
	}
	
	@GetMapping("/employees/template")
	public void downloadTemplate(HttpServletResponse response)
	        throws IOException {

	    excelService.downloadTemplate(response);
	}
	
	
	@GetMapping("/test-mail")
	@ResponseBody
	public String testMail() {

	    emailService.sendEmail(
	            "thu200184@gmail.com",
	            "Spring Boot Test",
	            "Hello! This email was sent from Spring Boot."
	    );

	    return "Email sent!";
	}

}
