package com.example.test.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	 @Override
	    public void addInterceptors(
	            InterceptorRegistry registry) {

	        registry.addInterceptor(new SessionInterceptor())
	                .addPathPatterns(
	                		"/employees/**");
	        
	        registry.addInterceptor(
	                new AdminInterceptor())
	                .addPathPatterns(
	                        "/employees/add",
	                        "/employees/edit/**",
	                        "/employees/delete/**",
	                        "/employees/export",
	                        "/employees/import",
	                        "/employees/preview",
	                        "/employees/template");
	 }
}
