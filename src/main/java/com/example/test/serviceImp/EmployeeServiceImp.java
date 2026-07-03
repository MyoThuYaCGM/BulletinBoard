package com.example.test.serviceImp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.test.entity.Employee;
import com.example.test.repository.EmployeeRepository;
import com.example.test.service.EmployeeService;

@Service
public class EmployeeServiceImp implements EmployeeService{
	
	private final EmployeeRepository empRepo;
	
	public EmployeeServiceImp(EmployeeRepository empRepo) {
	        this.empRepo = empRepo;
	    }

	@Override
	public List<Employee> getAllEmployees() {
		
		return empRepo.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id) {
		return empRepo.findById(id).orElse(null);
	}
	
	@Override
	public Employee saveEmployee(Employee employee) {
		
		if (empRepo.existsByEmail(employee.getEmail())) {
	        throw new RuntimeException("Email already exists");
	    }
		return empRepo.save(employee);
	}

	@Override
	public void deleteEmployee(Long id) {
		
		 empRepo.deleteById(id);
	}

	@Override
	public Employee getEmpByName(String name) {
		return empRepo.findByName(name);
	}

}
