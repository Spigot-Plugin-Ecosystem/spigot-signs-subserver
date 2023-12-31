package de.korzhorz.signs.subserver.database;

import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.data.Data;
import de.korzhorz.signs.subserver.data.ServerData;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelUtil;
import de.korzhorz.signs.subserver.util.database.DatabaseTableUtil;
import de.korzhorz.signs.subserver.util.database.MySQLUtil;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB_Signs extends DatabaseTableUtil {
    private final static DB_Signs instance = new DB_Signs();

    public static DB_Signs getInstance() {
        return instance;
    }

    private DB_Signs() {

    }

    @Override
    public void createTable() {
        if(!this.requireDatabaseConnection()) {
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

        try(PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ServerData getServerData(String serverName) {
        if(!this.requireDatabaseConnection()) {
            return null;
        }

        String sql = "SELECT * FROM `Signs_ServerInformation` WHERE `serverName` = ?;";

        try(PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql)) {
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

    private void updateServerData(String serverMotd, Integer serverMaxPlayers, Integer serverOnlinePlayers, Boolean online, Boolean maintenance, boolean shutdown) {
        if(!this.requireDatabaseConnection()) {
            return;
        }

        if(!shutdown && (ConfigFiles.server.getString("server-name") == null || !ConfigFiles.server.getBoolean("server-name-updated"))) {
            // Request server name
            long sentDate = System.currentTimeMillis();
            PluginChannelUtil.getInstance().sendPluginMessage("GetServer");

            long currentDate = System.currentTimeMillis();
            while(ConfigFiles.server.getString("server-name") == null && !ConfigFiles.server.getBoolean("server-name-updated") && currentDate - sentDate < 1000) {
                // Busy wait
                currentDate = System.currentTimeMillis();
            }
            if(ConfigFiles.server.getString("server-name") == null) {
                // Server name not received, abort database update
                return;
            }
        }

        String sql = "";
        boolean create = false;
        if(this.getServerData(ConfigFiles.server.getString("server-name")) == null) {
            // Create new entry
            if(serverMotd == null) {
                serverMotd = Bukkit.getMotd();
            }
            if(serverMaxPlayers == null) {
                serverMaxPlayers = Bukkit.getMaxPlayers();
            }
            if(serverOnlinePlayers == null) {
                serverOnlinePlayers = Bukkit.getOnlinePlayers().size();
            }
            if(online == null) {
                online = true;
            }
            if(maintenance == null) {
                maintenance = true;
            }

            sql = "INSERT INTO `Signs_ServerInformation` (`serverName`, `serverMotd`, `serverMaxPlayers`, `serverOnlinePlayers`, `online`, `maintenance`) VALUES (?, ?, ?, ?, ?, ?);";
            create = true;
        } else {
            // Update entry
            if(serverMotd == null && serverMaxPlayers == null && serverOnlinePlayers == null && online == null && maintenance == null) {
                return;
            }

            sql = "UPDATE `Signs_ServerInformation` SET ";
            if(serverMotd != null) {
                sql += "`serverMotd` = ?,";
            }
            if(serverMaxPlayers != null) {
                sql += "`serverMaxPlayers` = ?,";
            }
            if(serverOnlinePlayers != null) {
                sql += "`serverOnlinePlayers` = ?,";
            }
            if(online != null) {
                sql += "`online` = ?,";
            }
            if(maintenance != null) {
                sql += "`maintenance` = ?,";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += " WHERE `serverName` = ?;";
        }

        try(PreparedStatement preparedStatement = MySQLUtil.getConnection().prepareStatement(sql)) {
            int i = 1;
            if(serverMotd != null) {
                preparedStatement.setString(create ? 2 : i, serverMotd);
                i++;
            }
            if(serverMaxPlayers != null) {
                preparedStatement.setInt(create ? 3 : i, serverMaxPlayers);
                i++;
            }
            if(serverOnlinePlayers != null) {
                preparedStatement.setInt(create ? 4 : i, serverOnlinePlayers);
                i++;
            }
            if(online != null) {
                preparedStatement.setBoolean(create ? 5 : i, online);
                i++;
            }
            if(maintenance != null) {
                preparedStatement.setBoolean(create ? 6 : i, maintenance);
                i++;
            }
            preparedStatement.setString(create ? 1 : i, ConfigFiles.server.getString("server-name"));

            preparedStatement.executeUpdate();

            if(!shutdown) {
                PluginChannelUtil.getInstance().sendPluginMessage("Forward", "ALL", "signs:update", ConfigFiles.server.getString("server-name"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String serverMotd, Integer serverMaxPlayers, Integer serverOnlinePlayers, Boolean online, Boolean maintenance, boolean shutdown) {
        Thread thread = new Thread(() -> {
            DB_Signs.getInstance().updateServerData(
                    serverMotd,
                    serverMaxPlayers,
                    serverOnlinePlayers,
                    online,
                    maintenance,
                    shutdown
            );

            if(shutdown) {
                Data.shutdownBlocked = false;
            }
        });

        thread.start();
    }

    public boolean getMaintenance() {
        ServerData serverData = DB_Signs.getInstance().getServerData(ConfigFiles.server.getString("server-name"));
        if(serverData == null) {
            return false;
        }

        return serverData.getMaintenance();
    }
}
