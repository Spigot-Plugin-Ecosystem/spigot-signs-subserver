package de.korzhorz.signs.subserver.util;

import de.korzhorz.signs.subserver.Data;
import de.korzhorz.signs.subserver.Main;
import de.korzhorz.signs.subserver.data.ServerData;
import de.korzhorz.signs.subserver.handlers.DatabaseHandler;
import de.korzhorz.signs.subserver.handlers.MySQLHandler;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SignDatabase extends DatabaseHandler {
    @Override
    public void createTables() {
        if(this.requireDatabaseConnection()) {
            return;
        }

        String sql = "CREATE TABLE IF NOT EXISTS `Signs_ServerInformation` (";
        sql += "`serverName` VARCHAR(63) NOT NULL PRIMARY KEY,";
        sql += "`serverMotd` VARCHAR(255) NOT NULL DEFAULT \"\",";
        sql += "`serverMaxPlayers` INT(11) NOT NULL DEFAULT 0,";
        sql += "`serverOnlinePlayers` INT(11) NOT NULL DEFAULT 0,";
        sql += "`online` TINYINT(1) NOT NULL DEFAULT 0,";
        sql += "`maintenance` TINYINT(1) NOT NULL DEFAULT 0";
        sql += ");";

        try(PreparedStatement preparedStatement = MySQLHandler.getConnection().prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ServerData getServerData(String serverName) {
        if(this.requireDatabaseConnection()) {
            return null;
        }

        String sql = "SELECT * FROM `Signs_ServerInformation` WHERE `serverName` = ?;";

        try(PreparedStatement preparedStatement = MySQLHandler.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, serverName);

            ResultSet result = preparedStatement.executeQuery();

            if(result.next()) {
                String serverMotd = result.getString("serverMotd");
                int serverMaxPlayers = result.getInt("serverMaxPlayers");
                int serverOnlinePlayers = result.getInt("serverOnlinePlayers");
                boolean online = result.getBoolean("online");
                boolean maintenance = result.getBoolean("maintenance");

                return new ServerData(serverName, serverMotd, serverMaxPlayers, serverOnlinePlayers, online, maintenance);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateServerData(boolean online, boolean maintenance) {
        if(this.requireDatabaseConnection()) {
            return;
        }

        if(Data.serverName == null) {
            // Request server name
            Date sentDate = new Date();
            Main.bungeeCordHandler.sendPluginMessage("GetServer");

            Date currentDate = new Date();
            while(Data.serverName == null && currentDate.getTime() - sentDate.getTime() < 1000) {
                // Busy wait
            }
            if(Data.serverName == null) {
                // Server name not received, abort database update
                return;
            }
        }

        String sql = "";
        boolean create = false;
        if(this.getServerData(Data.serverName) == null) {
            // Create new entry
            sql = "INSERT INTO `Signs_ServerInformation` (`serverName`, `serverMotd`, `serverMaxPlayers`, `serverOnlinePlayers`, `online`, `maintenance`) VALUES (?, ?, ?, ?, ?, ?);";
            create = true;
        } else {
            // Update entry
            sql = "UPDATE `Signs_ServerInformation` SET `serverMotd` = ?, `serverMaxPlayers` = ?, `serverOnlinePlayers` = ?, `online` = ?, `maintenance` = ? WHERE `serverName` = ?;";
        }

        String serverMotd = Bukkit.getMotd();
        int serverMaxPlayers = Bukkit.getMaxPlayers();
        int serverOnlinePlayers = Bukkit.getOnlinePlayers().size();
        try(PreparedStatement preparedStatement = MySQLHandler.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(create ? 1 : 6, Data.serverName);
            preparedStatement.setString(create ? 2 : 1, serverMotd);
            preparedStatement.setInt(create ? 3 : 2, serverMaxPlayers);
            preparedStatement.setInt(create ? 4 : 3, serverOnlinePlayers);
            preparedStatement.setBoolean(create ? 5 : 4, online);
            if(create) {
                preparedStatement.setBoolean(6, true);
            } else {
                preparedStatement.setBoolean(5, maintenance);
            }

            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
