package com.udacity.jdnd.course3.critter.user;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeById(long employeeId) {
        return employeeRepository.findById(employeeId).get();
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, LocalDate date) {
        List<Employee> employees = employeeRepository.findAll();
        List<Employee> matchingEmployees = new ArrayList<>();
        Set<DayOfWeek> dayOfWeekRequest = Sets.newHashSet(date.getDayOfWeek());
        for (Employee employee: employees) {
            if (employee.getDaysAvailable().containsAll(dayOfWeekRequest)
                    && employee.getSkills().containsAll(skills)) {
                matchingEmployees.add(employee);
            }
        }
        return matchingEmployees;
    }
}
