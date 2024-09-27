package com.ureca.compliment.util;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBUtil {
    @Autowired
    private Dotenv dotenv;

    private final String driverName = "com.mysql.cj.jdbc.Driver";
    private static DBUtil instance = new DBUtil();

    private DBUtil() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBUtil getInstance() {
        return instance;
    }

    public Connection getConnection() throws SQLException {
        final String dbHost = dotenv.get("DB_HOST");
        final String dbUser = dotenv.get("DB_USER");
        final String dbPassword = dotenv.get("DB_PASSWORD");

        return DriverManager.getConnection(dbHost, dbUser, dbPassword);
    }

    public void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
