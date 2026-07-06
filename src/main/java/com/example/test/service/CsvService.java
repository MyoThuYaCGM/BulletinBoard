package com.example.test.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

public interface CsvService {
	
	void downloadEmployees(HttpServletResponse response)
            throws IOException;
	
	void uploadEmployees(MultipartFile file)
	        throws IOException;
}
