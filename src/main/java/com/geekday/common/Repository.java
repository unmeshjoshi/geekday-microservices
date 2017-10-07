package com.geekday.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class Repository {
    public abstract List<String> getMigrations();

    protected abstract String getDbName();

    protected Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:hsqldb:mem:" + getDbName(), "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void runMigrations() {
        List<String> migrations = getMigrations();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            for (String migration : migrations) {
                System.out.println(migration);
                statement.execute(migration);
            }
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
