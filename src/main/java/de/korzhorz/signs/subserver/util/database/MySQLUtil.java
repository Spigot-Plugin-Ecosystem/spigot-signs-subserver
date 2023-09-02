package de.korzhorz.signs.subserver.util.database;

import de.korzhorz.signs.subserver.configs.ConfigFiles;

import java.sql.Connection;

public class MySQLUtil {
    private static Connection connection;
    private static final String host = ConfigFiles.config.getString("mysql.host");
    private static final int port = ConfigFiles.config.getInt("mysql.port");
    private static final String database = ConfigFiles.config.getString("mysql.database");
    private static final String username = ConfigFiles.config.getString("mysql.username");
    private static final String password = ConfigFiles.config.getString("mysql.password");

    public static boolean connect() {
        if(!(MySQLUtil.isConnected())) {
            try {
                MySQLUtil.connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + MySQLUtil.host + ":" + MySQLUtil.port + "/" + MySQLUtil.database, MySQLUtil.username, MySQLUtil.password);
                return true;
            } catch(java.sql.SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static void disconnect() {
        if(MySQLUtil.isConnected()) {
            try {
                MySQLUtil.connection.close();
            } catch(java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return MySQLUtil.connection != null;
    }

    public static Connection getConnection() {
        return MySQLUtil.connection;
    }
}
