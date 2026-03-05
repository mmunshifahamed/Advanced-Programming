package com.hotel.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";              // change if different
    private static final String PASS = "12345";     // change to your real password

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // ✅ add this
        return DriverManager.getConnection(URL, USER, PASS);
    }
}