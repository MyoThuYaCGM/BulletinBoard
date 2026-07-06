package com.example.test.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.test.entity.Employee;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {

	void exportEmployees(HttpServletResponse response) throws IOException;

	void importEmployees(MultipartFile file) throws IOException;

	List<Employee> previewEmployees(MultipartFile file) throws IOException;

	void downloadTemplate(HttpServletResponse response) throws IOException;
}
