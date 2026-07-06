package com.example.test.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.test.dto.loginRequest;
import com.example.test.entity.Employee;
import com.example.test.repository.EmployeeRepository;
import com.example.test.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	final private EmployeeRepository empRepo;
	final private EmailService emailService;
	public LoginController(EmployeeRepository empRepo, EmailService emailService) {
		this.empRepo = empRepo;
		this.emailService = emailService;
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
	    model.addAttribute("loginRequest", new loginRequest());
	    return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute loginRequest loginReq,
            HttpSession session,
            Model model) {
		//System.out.println(loginReq.getEmail());
		Optional<Employee> employee = empRepo.findByEmail(loginReq.getEmail());
		if (employee.isPresent() && employee.get().getPassword().equals(loginReq.getPassword())) {
			session.setAttribute("loggedInUser", employee.get());

			return "redirect:/employees";
		}

		model.addAttribute("error", "Invalid email or password");
		model.addAttribute("loginRequest", loginReq);

		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/";
	}

	@GetMapping("/forgot-password")
	public String forgotPasswordPage() {
		return "forgot-password";
	}

	/*
	 * @PostMapping("/forgot-password") public String forgotPassword(@RequestParam
	 * String email, RedirectAttributes redirectAttributes, Model model) {
	 * 
	 * Optional<Employee> employee = empRepo.findByEmail(email);
	 * 
	 * if (employee.isEmpty()) { model.addAttribute("error", "Invalid Email");
	 * return "forgot-password"; }
	 * 
	 * redirectAttributes.addAttribute("email", email);
	 * 
	 * return "redirect:/change-password"; }
	 */
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email, Model model) throws MessagingException {

	    Optional<Employee> emp = empRepo.findByEmail(email);

	    if (emp.isEmpty()) {
	        model.addAttribute("error", "Email not found.");
	        return "forgot-password";
	    }

	    emailService.sendResetPassword(email);

	    model.addAttribute("success",
	            "A password reset link has been sent to your email.");

	    return "forgot-password";
	}

	@GetMapping("/change-password")
	public String changePasswordPage(@RequestParam String email, Model model) {
		model.addAttribute("email", email);
		return "change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String email, @RequestParam String newPassword,
			@RequestParam String confirmPassword, Model model) {

		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("error", "Passwords do not match.");
			model.addAttribute("email", email);
			return "change-password";
		}

		Optional<Employee> employeeOpt = empRepo.findByEmail(email);

		if (employeeOpt.isEmpty()) {
			return "redirect:/forgot-password";
		}

		Employee employee = employeeOpt.get();
		employee.setPassword(newPassword);

		empRepo.save(employee);

		return "redirect:/login";
	}
}
