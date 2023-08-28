package de.korzhorz.signs.subserver;

import de.korzhorz.signs.subserver.commands.CMD_Maintenance;
import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.handlers.BungeeCordHandler;
import de.korzhorz.signs.subserver.handlers.MySQLHandler;
import de.korzhorz.signs.subserver.listeners.EVT_UpdatePlayerCount;
import de.korzhorz.signs.subserver.util.ColorTranslator;
import de.korzhorz.signs.subserver.util.GitHubUpdater;
import de.korzhorz.signs.subserver.util.SignDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        final String consolePrefix = "&7[&6Signs&7]&r ";

        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Enabling"));
        
        this.getDataFolder().mkdir();

        // Plugin channels
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Setting up plugin channels"));
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", BungeeCordHandler.getInstance());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aPlugin channels set up"));
        
        // Configuration files
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Loading files"));
        ConfigFiles.initFileContents();
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aFiles loaded"));

        // MySQL database
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Connecting to MySQL database"));
        if(MySQLHandler.connect()) {
            // Create database tables
            SignDatabase signDatabase = new SignDatabase();
            signDatabase.createTables();
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aConnected to MySQL database"));
        } else {
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&cCouldn't connect to MySQL database"));
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&cDisabling plugin"));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Commands
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Loading commands"));
        loadCommands();
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aCommands loaded"));
        
        // Events
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Loading events"));
        loadEvents();
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aEvents loaded"));
        
        // Update Checker
        if(GitHubUpdater.updateAvailable()) {
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix));
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&9A new update for this plugin is available"));
            this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix));
        }
        
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aPlugin enabled &7- Version: &6v" + this.getDescription().getVersion()));
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aDeveloped by &6KorzHorz"));

        // Update server data
        String serverMotd = Bukkit.getMotd();
        int serverMaxPlayers = Bukkit.getMaxPlayers();
        int serverOnlinePlayers = Bukkit.getOnlinePlayers().size();
        SignDatabase.update(
                serverMotd,
                serverMaxPlayers,
                serverOnlinePlayers,
                true,
                null,
                false
        );

        // Check for changed MOTD every 5 seconds
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                String serverMotd = Bukkit.getMotd();
                if(Data.oldMotd != null && Data.oldMotd.equals(serverMotd)) {
                    return;
                }

                Data.oldMotd = serverMotd;
                SignDatabase.update(
                        serverMotd,
                        null,
                        null,
                        null,
                        null,
                        false
                );
            }
        }, 0L, 100L);
    }

    @Override
    public void onDisable() {
        Data.shutdownBlocked = true;

        // Update server data
        String serverMotd = Bukkit.getMotd();
        int serverMaxPlayers = Bukkit.getMaxPlayers();
        SignDatabase.update(
                serverMotd,
                serverMaxPlayers,
                0,
                false,
                null,
                true
        );

        while(Data.shutdownBlocked) {
            // Busy wait
        }

        MySQLHandler.disconnect();
    }
    
    public void loadCommands() {
        Objects.requireNonNull(this.getCommand("maintenance")).setExecutor(new CMD_Maintenance());
    }
    
    public void loadEvents() {
        Bukkit.getPluginManager().registerEvents(new EVT_UpdatePlayerCount(), this);
    }
}
