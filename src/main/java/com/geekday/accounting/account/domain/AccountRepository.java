package com.geekday.accounting.account.domain;


import com.geekday.common.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AccountRepository extends Repository {

    protected String getDbName() {
        return "account";
    }

    @Override
    public List<String> getMigrations() {
        return Arrays.asList("create table account(id varchar(50), customerName varchar(50))");
    }


    public void save(Account account) {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("insert into account values(?, ?)");
            ps.setString(1, account.getAccountId());
            ps.setString(2, account.getCustomerName());
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account get(String id) {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from account where id = ?");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new Account(resultSet.getString("id"), resultSet.getString("customerName"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
