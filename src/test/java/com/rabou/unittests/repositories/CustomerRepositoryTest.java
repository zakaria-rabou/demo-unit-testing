package com.rabou.unittests.repositories;

import com.rabou.unittests.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;


    @BeforeEach
    void loadDb(){
        customerRepository.save(Customer.builder()
            .email("zakariarabou4@gmail.com")
            .firstName("zakaria")
            .lastName("rabou")
            .build());
        customerRepository.save(Customer.builder()
            .email("test1@gmail.com")
            .firstName("test1")
            .lastName("test1")
            .build());
        customerRepository.save(Customer.builder()
            .email("test2@gmail.com")
            .firstName("test2")
            .lastName("test2")
            .build());
    }


    @Test
    public void shouldFindCustomerByEmail(){
        Customer expectedCustomer = Customer.builder()
            .email("zakariarabou4@gmail.com")
            .lastName("rabou")
            .firstName("zakaria")
            .build();
        Optional<Customer> actualCustomer = customerRepository.findByEmail("zakariarabou4@gmail.com");
        assertThat(actualCustomer.isPresent());
        assertThat(actualCustomer.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedCustomer);
    }

    @Test
    public void shouldNotFindCustomerByEmail(){
        String email = "test3@gmail.com";
        Optional<Customer> actualCustomer = customerRepository.findByEmail(email);
        assertThat(actualCustomer.isEmpty());
    }

    @Test
    public void shouldFindCustomerByFirstName(){
        List<Customer> expectedCustomers = List.of(
            Customer.builder().firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            Customer.builder().firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        List<Customer> actualCustomers = customerRepository.findCustomerByFirstNameContainingIgnoreCase("st");
        assertThat(!actualCustomers.isEmpty());
        assertThat(actualCustomers).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedCustomers);
    }


}
