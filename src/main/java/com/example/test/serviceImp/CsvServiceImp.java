package com.example.test.serviceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.test.Enum.Role;
import com.example.test.entity.Employee;
import com.example.test.service.CsvService;
import com.example.test.service.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CsvServiceImp implements CsvService {

	private final EmployeeService employeeService;

	public CsvServiceImp(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	public void downloadEmployees(HttpServletResponse response) throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=employees.csv");

		List<Employee> employees = employeeService.getAllEmployees();

		PrintWriter writer = response.getWriter();

		writer.println("Id,Name,Email,Role");

		for (Employee employee : employees) {
			writer.println(
					employee.getId() + "," + 
					employee.getName() + "," + 
					employee.getEmail() + "," + 
					(employee.getRole() == null ? "-" : employee.getRole()));
		}

		writer.flush();
	}

	@Override
	public void uploadEmployees(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

		String line;

		// Skip header row
		reader.readLine();

		while ((line = reader.readLine()) != null) {

			String[] data = line.split(",");

			Employee employee = new Employee();

			employee.setName(data[0]);
			employee.setEmail(data[1]);
			employee.setPassword(data[2]);
			employee.setRole(Role.valueOf(data[3]));

			employeeService.saveEmployee(employee);
		}
	}

}
