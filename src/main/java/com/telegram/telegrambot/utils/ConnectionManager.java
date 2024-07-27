package com.telegram.telegrambot.utils;

import org.apache.logging.log4j.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = "db.url";

    private static final String USERNAME = "db.username";

    private static final String PASSWORD = "db.password";

    private ConnectionManager() {}

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(PropertyManager.get(URL),
            PropertyManager.get(USERNAME),
            PropertyManager.get(PASSWORD));
    }
}
