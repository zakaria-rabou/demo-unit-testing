package com.rabou.unittests.mapper;


import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.entities.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerMapper {

    ModelMapper modelMapper = new ModelMapper();

    public CustomerDTO fromCustomer(Customer customer){
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO, Customer.class);
    }

    public List<CustomerDTO> fromCustomersList(List<Customer> customers){
        return customers
            .stream()
            .map((customer)-> modelMapper.map(customer, CustomerDTO.class))
            .collect(Collectors.toList());
    }


}
