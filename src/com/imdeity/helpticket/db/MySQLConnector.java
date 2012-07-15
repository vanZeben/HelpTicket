package com.imdeity.helpticket.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.PreparedStatement;

import com.avaje.ebeaninternal.server.lib.sql.DataSourceException;
import com.imdeity.helpticket.*;

public class MySQLConnector {
    
    private Connection conn;
    
    public MySQLConnector() throws Exception {
        // Load the driver instance
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            throw new DataSourceException("[HelpTicket] Failed to initialize JDBC driver");
        }
        
        // make the connection
        conn = DriverManager.getConnection("jdbc:mysql://" + HelpTicket.plugin.config.getMySQLServerAddress() + ":" + HelpTicket.plugin.config.getMySQLServerPort() + "/" + HelpTicket.plugin.config.getMySQLDatabaseName() + "?user=" + HelpTicket.plugin.config.getMySQLDatabaseUsername() + "&password="
                + HelpTicket.plugin.config.getMySQLDatabasePassword());
        System.out.println("[HelpTicket] Connection Sucessful");
    }
    
    public void validataDatabaseTables() {
        
        Write("CREATE TABLE IF NOT EXISTS " + tableName("data") + " (" + "`id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ," + "`owner` VARCHAR(16) NOT NULL ," + "`world` VARCHAR(16) NOT NULL ," + "`x_coord` DOUBLE NOT NULL ," + "`y_coord` DOUBLE NOT NULL ," + "`z_coord` DOUBLE NOT NULL ,"
                + "`pitch` FLOAT NOT NULL ," + "`yaw` FLOAT NOT NULL ," + "`info` VARCHAR(256) NOT NULL ," + "`assignee` VARCHAR(40) NULL DEFAULT NULL ," + "`status` INT(1) NOT NULL DEFAULT '0'," + "`has_read` INT(1) NOT NULL DEFAULT '0'," + "`creation_time` DATETIME NOT NULL,"
                + "`priority` INT(1) NOT NULL DEFAULT '0'," + "PRIMARY KEY (`id`)," + "INDEX (`owner`),  INDEX (`priority`))" + " ENGINE = MYISAM;");
        
        Write("CREATE TABLE IF NOT EXISTS " + tableName("comments") + " (" + "`id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT ," + "`ticket_id` INT( 16 ) NOT NULL DEFAULT '0'," + "`owner` VARCHAR( 16 ) NOT NULL ," + "`commenter` VARCHAR( 16 ) NOT NULL ," + "`comment` TEXT NOT NULL,"
                + "PRIMARY KEY (`id`)," + "INDEX (`ticket_id`)" + ") ENGINE = MYISAM;");
    }
    
    // check if its closed
    private void reconnect() {
        HelpTicket.plugin.out("Reconnecting to MySQL...");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + HelpTicket.plugin.config.getMySQLServerAddress() + ":" + HelpTicket.plugin.config.getMySQLServerPort() + "/" + HelpTicket.plugin.config.getMySQLDatabaseName() + "?user=" + HelpTicket.plugin.config.getMySQLDatabaseUsername()
                    + "&password=" + HelpTicket.plugin.config.getMySQLDatabasePassword());
            HelpTicket.plugin.out("Connection success!");
        } catch (SQLException ex) {
            HelpTicket.plugin.out("Connection to MySQL failed! Check status of MySQL server!");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    // write query
    public boolean Write(String sql, Object... params) {
        /*
         * Double check connection to MySQL
         */
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int counter = 1;
        try {
            PreparedStatement stmt = null;
            stmt = this.conn.prepareStatement(sql);
            for (Object o : params) {
                if (o instanceof Integer) {
                    stmt.setInt(counter, (Integer) o);
                } else if (o instanceof Float) {
                    stmt.setFloat(counter, (Float) o);
                } else if (o instanceof Double) {
                    stmt.setDouble(counter, (Double) o);
                } else if (o instanceof String) {
                    stmt.setString(counter, (String) o);
                } else if (o instanceof Boolean) {
                    stmt.setBoolean(counter, (Boolean) o);
                } else {
                    System.out.printf("Database:Write -> Unsupported data type '%s'", o.getClass().getSimpleName());
                }
                counter++;
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
    }
    
    // write query
    public boolean WriteNoError(String sql) {
        try {
            PreparedStatement stmt = null;
            stmt = this.conn.prepareStatement(sql);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    // Get Int
    // only return first row / first field
    public Integer GetInt(String sql) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Integer result = 0;
        
        /*
         * Double check connection to MySQL
         */
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            stmt = this.conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                if (rs.next()) {
                    result = rs.getInt(1);
                } else {
                    result = 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        
        return result;
    }
    
    public static String tableName(String nameOfTable) {
        return (String.format("`%s`.`%s`", HelpTicket.plugin.config.getMySQLDatabaseName(), HelpTicket.plugin.config.getMySQLDatabaseTablePrefix() + nameOfTable));
    }
    
    // read query
    public HashMap<Integer, ArrayList<String>> Read(String sql) {
        
        /*
         * Double check connection to MySQL
         */
        try {
            if (!conn.isValid(5)) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<Integer, ArrayList<String>> Rows = new HashMap<Integer, ArrayList<String>>();
        
        try {
            stmt = this.conn.prepareStatement(sql);
            if (stmt.executeQuery() != null) {
                stmt.executeQuery();
                rs = stmt.getResultSet();
                while (rs.next()) {
                    ArrayList<String> Col = new ArrayList<String>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        Col.add(rs.getString(i));
                    }
                    Rows.put(rs.getRow(), Col);
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // release dataset
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
        return Rows;
    }
    
}
