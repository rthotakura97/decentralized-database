package com.distributeddb.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlAccessTest {

    @Before
    public void setup() {
        System.setProperty("sql.user", "reno");
        System.setProperty("sql.password", "mangietangie");
        System.setProperty("sql.url", "jdbc:mysql://localhost?useSSL=false");
    }

    @Test
    public void testSqlConnection() throws SQLException {
        Connection conn = SqlAccess.getConnection();

        Assert.assertNotNull(conn);
        conn.close();
    }
}