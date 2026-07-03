package com.example.test.common;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.test.Enum.Role;
import com.example.test.entity.Employee;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor{

	 @Override
	    public boolean preHandle(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            Object handler)
	            throws Exception {

	        HttpSession session =
	                request.getSession(false);

	        Employee user =
	                (Employee) session.getAttribute("loggedInUser");

	        if (user.getRole() != Role.ADMIN) {
	            response.sendRedirect("/employees");
	            return false;
	        }

	        return true;
	    }
}
