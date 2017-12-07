package com.decentralizeddatabase.reno.jailcellaccess;

import java.util.ArrayList;
import java.util.List;

import com.decentralizeddatabase.utils.SqlAccess;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JailCellAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(JailCellAccess.class);

    /**
     * @return List of JailCellInfo containing ips and urls
     * 
     * This method retrieves all active JailCells
     */
    public static List<JailCellInfo> getJailCells() {
        final List<JailCellInfo> jailCells = new ArrayList<>();
        Connection conn = null;

        try {
            conn = SqlAccess.getConnection();
            final String query = "select * from renodata.jailcell_access;";
            final Statement statement = conn.createStatement();
            final ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                final String ip = result.getString("ip");
                final String url = result.getString("url");
                jailCells.add(new JailCellInfo(ip, url));
            }
        } catch (SQLException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.error("{}", e.getStackTrace().toString());
            }
        }

        return jailCells;
    }
}