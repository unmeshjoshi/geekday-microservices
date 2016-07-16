package com.geekday.account;

import com.geekday.common.DomainEvent;
import com.geekday.common.DomainEventPublisher;

public class AccountService {
    public void newAccount(String customerName) {
        AccountId id = new AccountId();
        Account account = new Account(id.getValue(), customerName);

        saveAccount(account);

        System.out.println("Account created for " + customerName);

        publishEvent(account);
    }

    private void publishEvent(Account account) {
        DomainEvent event = new DomainEvent("AccountCreated", account.getAccountId() + "," + account.getCustomerName());
        new DomainEventPublisher().publish(event);
    }

    private void saveAccount(Account account) {
        AccountRepository repository = new AccountRepository();
        repository.save(account);
    }

}
