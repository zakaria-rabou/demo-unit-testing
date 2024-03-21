package com.rabou.unittests.services;

import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.entities.Customer;
import com.rabou.unittests.exceptions.CustomerNotFoundException;
import com.rabou.unittests.exceptions.EmailAlreadyExistsException;
import com.rabou.unittests.mapper.CustomerMapper;
import com.rabou.unittests.repositories.CustomerRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    void shouldSaveCustomer() {
        CustomerDTO customerDTOGiven = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        CustomerDTO customerDTOReturned = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        when(customerRepository.findByEmail(customerDTOGiven.getEmail())).thenReturn(Optional.empty());
        when(customerMapper.fromCustomerDTO(customerDTOGiven)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.fromCustomer(savedCustomer)).thenReturn(customerDTOReturned);
        CustomerDTO result = underTest.saveCustomer(customerDTOGiven);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(customerDTOReturned);
    }

    @Test
    void shouldNotSaveCustomerWhenEmailExists() {
        CustomerDTO customerDTOGiven = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        when(customerRepository.findByEmail(customerDTOGiven.getEmail())).thenReturn(Optional.of(customer));
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.saveCustomer(customerDTOGiven)).isInstanceOf(EmailAlreadyExistsException.class);

    }

    @Test
    void shouldGetAllCustomers() {
        List<Customer> customers = List.of(
            Customer.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            Customer.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        List<CustomerDTO> customerDTOS = List.of(
            CustomerDTO.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            CustomerDTO.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.fromCustomersList(customers)).thenReturn(customerDTOS);
        List<CustomerDTO> allCustomers = underTest.getAllCustomers();
        assertThat(allCustomers).usingRecursiveComparison().isEqualTo(customerDTOS);
    }

    @Test
    void shouldFindCustomerById() {
        Long customerId = 1L;
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(customer)).thenReturn(customerDTO);
        CustomerDTO customerById = underTest.findCustomerById(customerId);
        assertThat(customerById).usingRecursiveComparison().isEqualTo(customerDTO);

    }

    @Test
    void shouldNotFindCustomerById() {
        Long customerId = 8L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.findCustomerById(customerId)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldSearchCustomers() {
        String keyword = "st";
        List<Customer> customers = List.of(
            Customer.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            Customer.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        List<CustomerDTO> customerDTOS = List.of(
            CustomerDTO.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            CustomerDTO.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        when(customerRepository.findCustomerByFirstNameContainingIgnoreCase(keyword)).thenReturn(customers);
        when(customerMapper.fromCustomersList(customers)).thenReturn(customerDTOS);
        List<CustomerDTO> result = underTest.searchCustomers(keyword);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).usingRecursiveComparison().isEqualTo(customerDTOS);
    }

    @Test
    void shouldUpdateCustomer() {
        Long customerId = 6L;
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer updatedCustomer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);
        CustomerDTO result = underTest.updateCustomer(customerDTO, customerId);
        assertThat(result).isNotNull();
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldDeleteCustomer() {
        Long customerId = 1L;
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void shouldNotDeleteCustomerIfNotExist() {
        Long customerId = 9L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.deleteCustomer(customerId))
            .isInstanceOf(CustomerNotFoundException.class);
    }
}
