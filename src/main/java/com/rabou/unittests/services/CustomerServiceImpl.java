package com.rabou.unittests.services;

import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.entities.Customer;
import com.rabou.unittests.exceptions.CustomerNotFoundException;
import com.rabou.unittests.exceptions.EmailAlreadyExistsException;
import com.rabou.unittests.mapper.CustomerMapper;
import com.rabou.unittests.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws EmailAlreadyExistsException {
        log.info(String.format("Saving new Customer => %s ", customerDTO.toString()));
        Optional<Customer> customerByEmail = customerRepository.findByEmail(customerDTO.getEmail());
        if (customerByEmail.isPresent()) {
            log.error(String.format("This email %s already exists", customerDTO.getEmail()));
            throw new EmailAlreadyExistsException();
        }
        Customer customerToSave = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customerToSave);
        CustomerDTO customerDTOSaved = customerMapper.fromCustomer(savedCustomer);
        return customerDTOSaved;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.fromCustomersList(customerRepository.findAll());
    }

    @Override
    public CustomerDTO findCustomerById(Long customerId) throws CustomerNotFoundException {
        Optional<Customer> customerById = customerRepository.findById(customerId);
        if (customerById.isEmpty()){
            throw new CustomerNotFoundException();
        }
        return customerMapper.fromCustomer(customerById.get());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customerByKeyword = customerRepository.findCustomerByFirstNameContainingIgnoreCase(keyword);
        return customerMapper.fromCustomersList(customerByKeyword);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, Long customerId) throws CustomerNotFoundException {
        Optional<Customer> customerById = customerRepository.findById(customerId);
        if (customerById.isEmpty()){
            throw new CustomerNotFoundException();
        }
        customerDTO.setId(customerId);
        Customer customerToUpdate = customerMapper.fromCustomerDTO(customerDTO);
        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return customerMapper.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Optional<Customer> customerById = customerRepository.findById(customerId);
        if (customerById.isEmpty()){
            throw new CustomerNotFoundException();
        }
        customerRepository.deleteById(customerId);
    }
}
