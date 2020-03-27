package com.example.tokenback.controllers;

import com.example.tokenback.models.Customer;
import com.example.tokenback.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    // Get All Customers
    @GetMapping("/")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Create a new Customer
    @PostMapping("/")
    public Customer createCustomer(@Valid @RequestBody Customer customerDetails) {
        Customer customer = new Customer(customerDetails.getName(),
                customerDetails.getNumber(), customerDetails.getContact(),
                customerDetails.getAge());
        return customerRepository.save(customer);
    }

    // Get a Single Customer
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable(value = "id") Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Error: Customer not found."));
    }

    // Update a Customer
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable(value = "id") Long customerId,
                                   @Valid @RequestBody Customer customerDetails) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Error: Customer not found."));

        customer.setName(customerDetails.getName() != null ? customerDetails.getName() : customer.getName());
        customer.setNumber(customerDetails.getNumber() != null ? customerDetails.getNumber() : customer.getNumber());
        customer.setContact(customerDetails.getContact() != null ? customerDetails.getContact() : customer.getContact());
        customer.setAge(customerDetails.getAge() != null ? customerDetails.getAge() : customer.getAge());

        return customerRepository.save(customer);
    }

    // Delete a Customer
    @DeleteMapping("/{id}")
    public Customer deleteCustomer(@PathVariable(value = "id") Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Error: Customer not found."));

        customerRepository.delete(customer);

        return customer;
    }

}
