package com.example.test.service;

import java.util.List;

import com.example.test.entity.Employee;

public interface EmployeeService {
	
    Employee saveEmployee(Employee employee);
    
	List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);
    
    Employee getEmpByName(String name);
    
    

    void deleteEmployee(Long id);
}
