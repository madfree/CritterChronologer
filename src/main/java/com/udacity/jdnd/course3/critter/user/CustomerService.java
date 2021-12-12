package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository customerRepository;

    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).get();
    }

    public Customer saveCustomer (Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findCustomerByPet(long petId) {
        return customerRepository.findByPets_Id(petId);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
