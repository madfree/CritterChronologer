package com.udacity.jdnd.course3.critter.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.oracle.tools.packager.Log;
import com.udacity.jdnd.course3.critter.Views;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer newCustomer = customerService.saveCustomer(convertCustomerDtoToCustomer(customerDTO));
        return convertCustomerToCustomerDto(newCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customerList = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer: customerList) {
            customerDTOs.add(convertCustomerToCustomerDto(customer));
        }
        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = customerService.findCustomerByPet(petId);
        return convertCustomerToCustomerDto(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee newEmployee = employeeService.saveEmployee(convertEmployeeDtoToEmployee(employeeDTO));
        return convertEmployeeToEmployeeDto(newEmployee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        return convertEmployeeToEmployeeDto(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    private static CustomerDTO convertCustomerToCustomerDto(Customer customer) {
        CustomerDTO customerDto = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDto);
        List<Long> petIds = new ArrayList<>();
        List<Pet> pets = customer.getPets();
        if (pets != null) {
            for (Pet pet: pets) {
                petIds.add(pet.getId());
            }
        }
        customerDto.setPetIds(petIds);
        return customerDto;
    }

    private static Customer convertCustomerDtoToCustomer(CustomerDTO customerDto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }

    private static Employee convertEmployeeDtoToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private static EmployeeDTO convertEmployeeToEmployeeDto(Employee employee) {
        EmployeeDTO employeeDto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDto);
        return employeeDto;
    }

}
