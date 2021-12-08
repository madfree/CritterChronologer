package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    public CustomerRepository customerRepository;

    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Customer saveCustomer (Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findCustomerByPet(long petId) {
        return customerRepository.findByPetsId(petId);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
