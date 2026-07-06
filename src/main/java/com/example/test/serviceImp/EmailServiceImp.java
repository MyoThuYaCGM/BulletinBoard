package com.example.test.serviceImp;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.test.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImp implements EmailService {

	final private JavaMailSender mailSender;

	public EmailServiceImp(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);

	}

	@Override
	public void sendResetPassword(String to) throws MessagingException {

		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject("Password Reset");

		String body = """
				<html>
				    <body>
				        <p>Hello,</p>

				        <p>
				            <a href="%s">
				                Click here to change your password
				            </a>
				        </p>

				        <p>Thank you.</p>
				    </body>
				</html>
				""".formatted("http://localhost:8080/change-password?email=" + to);

		helper.setText(body, true);

		mailSender.send(message);
	}

}
