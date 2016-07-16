package com.geekday.customer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerRepositoryTest {

    @Test
    public void shouldSaveCustomer() throws Exception {
        CustomerRepository.initialize();
        CustomerRepository repository = new CustomerRepository();
        repository.save(new Customer("name", "address"));
        Customer c = repository.get("name");
        assertEquals("name", c.getName());
        assertEquals("address", c.getAddress());
    }
}