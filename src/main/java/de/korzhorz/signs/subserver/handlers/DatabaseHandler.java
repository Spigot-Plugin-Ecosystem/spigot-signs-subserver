package de.korzhorz.signs.subserver.handlers;

public class DatabaseHandler {
    public void createTables() {

    }

    protected boolean requireDatabaseConnection() {
        if(MySQLHandler.isConnected()) {
            return true;
        }

        System.err.println("Database connection is not established");
        return false;
    }
}
