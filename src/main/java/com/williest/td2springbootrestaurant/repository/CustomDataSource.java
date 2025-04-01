package com.williest.td2springbootrestaurant.repository;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class CustomDataSource {
    private final String url = System.getenv("DB_URL");
    private final String user = System.getenv("DB_USERNAME");
    private final String password = System.getenv("DB_PASSWORD");

    public CustomDataSource() {}

    public Connection getConnection() {
        try{
            return DriverManager.getConnection(url, user, password);
        }
        catch(SQLException e){
            throw new RuntimeException("CONNECTION FAILED : "+e);
        }
    }
}