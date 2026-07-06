package com.example.test.serviceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.test.Enum.Role;
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
		
		Optional<Employee> existing = empRepo.findByEmail(employee.getEmail());

	    if (existing.isPresent()
	            && !existing.get().getId().equals(employee.getId())) {

	        throw new RuntimeException("Email already exists");
	    }
	    employee.setRole(Role.USER);
	    
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
