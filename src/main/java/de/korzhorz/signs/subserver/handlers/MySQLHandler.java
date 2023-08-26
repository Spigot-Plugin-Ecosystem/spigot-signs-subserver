package de.korzhorz.signs.subserver.handlers;

import de.korzhorz.signs.subserver.configs.ConfigFiles;

import java.sql.Connection;

public class MySQLHandler {
    private static Connection connection;
    private static final String host = ConfigFiles.config.getString("mysql.host");
    private static final int port = ConfigFiles.config.getInt("mysql.port");
    private static final String database = ConfigFiles.config.getString("mysql.database");
    private static final String username = ConfigFiles.config.getString("mysql.username");
    private static final String password = ConfigFiles.config.getString("mysql.password");

    public static boolean connect() {
        if(!(MySQLHandler.isConnected())) {
            try {
                MySQLHandler.connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + MySQLHandler.host + ":" + MySQLHandler.port + "/" + MySQLHandler.database, MySQLHandler.username, MySQLHandler.password);
                return true;
            } catch(java.sql.SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static void disconnect() {
        if(MySQLHandler.isConnected()) {
            try {
                MySQLHandler.connection.close();
            } catch(java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return MySQLHandler.connection != null;
    }

    public static Connection getConnection() {
        return MySQLHandler.connection;
    }
}
