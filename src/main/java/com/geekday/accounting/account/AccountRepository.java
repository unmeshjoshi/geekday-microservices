package com.geekday.accounting.account;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountRepository {

    public static void initialize() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("create table account(id varchar(50), customerName varchar(50))");
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:hsqldb:mem:account", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
