package com.rabou.unittests.services;

import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.exceptions.CustomerNotFoundException;
import com.rabou.unittests.exceptions.EmailAlreadyExistsException;

import java.util.List;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistsException;
    List<CustomerDTO> getAllCustomers();
    CustomerDTO findCustomerById(Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
    CustomerDTO updateCustomer(CustomerDTO customerDTO, Long customerId) throws CustomerNotFoundException;
    void deleteCustomer(Long id) throws CustomerNotFoundException;

}
