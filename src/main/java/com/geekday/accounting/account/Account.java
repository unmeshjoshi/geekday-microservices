package com.geekday.accounting.account;

public class Account {
    private String accountId;
    private String customerName;

    public Account() {
    }

    public Account(String accountId, String customerName) {

        this.accountId = accountId;
        this.customerName = customerName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
