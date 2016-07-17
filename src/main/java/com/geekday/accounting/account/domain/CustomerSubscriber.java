package com.geekday.accounting.account.domain;

import com.geekday.common.DomainEvent;
import com.geekday.common.DomainEventSubcriber;

public class CustomerSubscriber {

    public void start() {
        new Thread(()-> {
            while(true) {
                try {
                    DomainEvent customerEvent = new DomainEventSubcriber("CustomerCreated").receive();
                    String customerCsv = customerEvent.getCsv();
                    String[] customerAttributes = customerCsv.split(",");
                    String customerName = customerAttributes[0];
                    System.out.println("Customer event received " + customerCsv);
                    AccountService service = new AccountService();
                    service.newAccount(customerName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
