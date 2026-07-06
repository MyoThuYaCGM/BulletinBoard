package com.example.test.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendEmail(String to, String subject, String body);
	
	void sendResetPassword(String to) throws MessagingException;
}
