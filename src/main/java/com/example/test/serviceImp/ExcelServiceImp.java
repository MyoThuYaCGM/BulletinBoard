package com.example.test.serviceImp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.test.Enum.Role;
import com.example.test.entity.Employee;
import com.example.test.repository.EmployeeRepository;
import com.example.test.service.EmployeeService;
import com.example.test.service.ExcelService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelServiceImp implements ExcelService {

	private final EmployeeService employeeService;
	private final EmployeeRepository employeeRepository;

	public ExcelServiceImp(EmployeeService employeeService, EmployeeRepository employeeRepository) {
		this.employeeService = employeeService;
		this.employeeRepository = employeeRepository;
	}

	DataFormatter formatter = new DataFormatter();

	@Override
	public void exportEmployees(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(headerFont);

		String[] headers = { "ID", "Name", "Email", "Password", "Role" };

		Row header = sheet.createRow(0);

		for (int i = 0; i < headers.length; i++) {
			Cell cell = header.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		List<Employee> employees = employeeService.getAllEmployees();
		int rowNum = 1;

		for (Employee employee : employees) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(employee.getId());
			row.createCell(1).setCellValue(employee.getName());
			row.createCell(2).setCellValue(employee.getEmail());
			row.createCell(3).setCellValue(employee.getPassword());
			row.createCell(4).setCellValue(employee.getRole() == null ? "-" :
			 employee.getRole().name());
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=employees_List.xlsx");

		workbook.write(response.getOutputStream());
		workbook.close();
	}

	@Override
	public void importEmployees(MultipartFile file) throws IOException {

	    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

	        Sheet sheet = workbook.getSheetAt(0);

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

	            Row row = sheet.getRow(i);

	            if (row == null) {
	                continue;
	            }

	            Employee employee = new Employee();

	            employee.setName(formatter.formatCellValue(row.getCell(0)));

	            String email = formatter.formatCellValue(row.getCell(1));

	            if (employeeRepository.existsByEmail(email)) {
	                throw new RuntimeException("Email already exists");
	            }

	            employee.setEmail(email);
	            employee.setPassword(formatter.formatCellValue(row.getCell(2)));

	            String role = formatter.formatCellValue(row.getCell(3)).trim();

	            if (!role.isEmpty()) {
	                employee.setRole(Role.valueOf(role.toUpperCase()));
	            }

	            employeeService.saveEmployee(employee);
	        }
	    } 
	}

	@Override
	public List<Employee> previewEmployees(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		List<Employee> employees = new ArrayList<>();

		Workbook workbook = new XSSFWorkbook(file.getInputStream());

		Sheet sheet = workbook.getSheetAt(0);

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);

			if (row == null) {
				continue;
			}

			Employee employee = new Employee();

			employee.setName(formatter.formatCellValue(row.getCell(0)));
			employee.setEmail(formatter.formatCellValue(row.getCell(1)));
			employee.setPassword(formatter.formatCellValue(row.getCell(2)));

			String role = formatter.formatCellValue(row.getCell(3)).trim().toUpperCase();

			try {
				employee.setRole(Role.valueOf(role));
			} catch (IllegalArgumentException e) {
				employee.setRole(Role.USER);
			}

			employees.add(employee);
		}

		workbook.close();

		return employees;
	}

	@Override
	public void downloadTemplate(HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Employee Template");

		Font font = workbook.createFont();
		font.setBold(true);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(font);

		String[] headers = { "Name", "Email", "Password", "Role" };

		Row header = sheet.createRow(0);

		for (int i = 0; i < headers.length; i++) {

			Cell cell = header.createCell(i);

			cell.setCellValue(headers[i]);

			cell.setCellStyle(headerStyle);

			sheet.autoSizeColumn(i);
		}

		// Example row
		Row example = sheet.createRow(1);

		example.createCell(0).setCellValue("John Doe");
		example.createCell(1).setCellValue("john@gmail.com");
		example.createCell(2).setCellValue("123456");
		example.createCell(3).setCellValue("USER");

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		response.setHeader("Content-Disposition", "attachment; filename=Employee_Import_Template.xlsx");

		workbook.write(response.getOutputStream());

		workbook.close();
	}

}
