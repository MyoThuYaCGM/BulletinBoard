package com.example.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.test.dto.loginRequest;
import com.example.test.entity.Employee;
import com.example.test.service.EmployeeService;

@Controller
public class HomeController {

	private final EmployeeService employeeService;

	public HomeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
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

}
