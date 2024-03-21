package com.rabou.unittests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabou.unittests.dto.CustomerDTO;
import com.rabou.unittests.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDTO> customers;

    @BeforeEach
    void SetUp() {
        this.customers = List.of(
            CustomerDTO.builder().id(1L).firstName("zakaria").lastName("rabou").email("zakariarabou4@gmail.com").build(),
            CustomerDTO.builder().id(2L).firstName("test1").lastName("test1").email("test1@gmail.com").build(),
            CustomerDTO.builder().id(3L).firstName("test2").lastName("test2").email("test2@gmail.com").build()
        );
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(get("/api/customers"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(customers)))
            .andExpect(jsonPath("$.size()", Matchers.is(3)));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        Long id = 1L;
        when(customerService.findCustomerById(id)).thenReturn(customers.get(0));
        mockMvc.perform(get("/api/customers/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(customers.get(0))));
    }

    @Test
    void shouldGetSearchedCustomers() throws Exception {
        String keyword = "m";
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(get("/api/customers?keyword=" + keyword))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.is(3)))
            .andExpect(content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldSaveCustomer() throws Exception {
        String expected = """
            {
            "id":1, "firstName":"zakaria", "lastName":"rabou", "email":"zakariarabou4@gmail.com"
            }
            """;
        when(customerService.saveCustomer(Mockito.any())).thenReturn(customers.get(0));
        mockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(customers.get(0))))
            .andExpect(status().isCreated())
            .andExpect(content().json(expected));

    }


    @Test
    void shouldDeleteCustomer() throws Exception {
        Long id = 2L;
        mockMvc.perform(delete("/api/customers/{id}", id))
            .andExpect(status().is2xxSuccessful());
    }
}
