package com.rabou.unittests.mapper;

import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.entities.Customer;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerMapperTest {
    private CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void shouldMapCustomerToCustomerDTO() {
        Customer customer = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        CustomerDTO expected = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        CustomerDTO actualResult = customerMapper.fromCustomer(customer);
        assertThat(expected).usingRecursiveComparison().isEqualTo(actualResult);
    }

    @Test
    void shouldMapCustomerDTOToCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer expected = Customer.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build();
        Customer actualResult = customerMapper.fromCustomerDTO(customerDTO);
        assertThat(expected).usingRecursiveComparison().isEqualTo(actualResult);
    }

    @Test
    void shouldMapListOfCustomersToListCustomerDTOs() {
        List<Customer> customers = List.of(
            Customer.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            Customer.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        List<CustomerDTO> expectedCustomerDTOS = List.of(
            CustomerDTO.builder().id(1L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            CustomerDTO.builder().id(2L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
        List<CustomerDTO> actualResult = customerMapper.fromCustomersList(customers);
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedCustomerDTOS);
    }

    @Test
    public void shouldNotMapNullCustomer(){
        assertThatThrownBy(()-> customerMapper.fromCustomer(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
