package com.geekday.customer;

import com.geekday.common.DomainEvent;
import com.geekday.common.DomainEventPublisher;

public class CustomerService {
    public void newCustomer(String name, String address) {
        Customer customer = new Customer(name, address);

        saveCustomer(customer);
        publishEvent(customer);
    }

    private void publishEvent(Customer customer) {
        DomainEvent event = new DomainEvent("CustomerCreated", customer.getName() + "," + customer.getAddress());
        new DomainEventPublisher().publish(event);
    }

    private void saveCustomer(Customer customer) {
        CustomerRepository repository = new CustomerRepository();
        repository.save(customer);
    }
}
