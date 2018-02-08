package com.distributeddb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlAccess.class);

    public static Connection getConnection() throws SQLException {
        final String dbUrl = System.getProperty("sql.url");
        final String user = System.getProperty("sql.user");
        final String pass = System.getProperty("sql.password");
        final Connection conn = DriverManager.getConnection(dbUrl, user, pass);

        return conn;
    }
}