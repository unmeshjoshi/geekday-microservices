package com.geekday.customer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepository {

    public static void initialize() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("create table customer(name varchar(50), address varchar(50))");
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:hsqldb:mem:customer", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Customer customer) {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("insert into customer values(?, ?)");
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer get(String name) {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from customer where name = ?");
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return new Customer(resultSet.getString("name"), resultSet.getString("address"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
