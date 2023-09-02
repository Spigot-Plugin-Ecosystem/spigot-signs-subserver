package de.korzhorz.signs.subserver;

import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.database.DB_Signs;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelEvent;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelInitiator;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelUtil;
import de.korzhorz.signs.subserver.util.data.Command;
import de.korzhorz.signs.subserver.util.database.DatabaseTableUtil;
import de.korzhorz.signs.subserver.util.database.MySQLUtil;
import de.korzhorz.signs.subserver.util.messages.CTUtil;
import de.korzhorz.signs.subserver.util.meta.Data;
import de.korzhorz.signs.subserver.util.meta.GitHubUpdater;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        final String consolePrefix = "&7[&6" + PluginConfig.pluginName + "&7]&r ";
        final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Enabling"));

        this.getDataFolder().mkdir();

        // Configuration files
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Loading files"));
        ConfigFiles.initFileContents();
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aFiles loaded"));

        // Check if server is running in BungeeCord network
        if(this.detectBungeeCord()) {
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cWARNING: The BungeeCord mode was forced to be enabled"));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cWARNING: This could cause problems if the server is not actually running in a BungeeCord network"));
        }
        if(PluginConfig.requireBungeeCord && !(Data.bungeeCord)) {
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cServer is not running in a BungeeCord network"));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cIf this is a mistake, please set the option \"bungeecord.enforce\" in the config.yml to \"true\""));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cDisabling plugin"));
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Plugin channels
        if(PluginConfig.pluginChannels) {
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Setting up plugin channels"));
            this.loadPluginChannels();
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aPlugin channels set up"));
        }

        // MySQL database
        if(PluginConfig.mySql) {
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Connecting to MySQL database"));
            Data.mySql = MySQLUtil.connect();

            if(Data.mySql) {
                this.loadDatabase();
                consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aConnected to MySQL database"));
            } else {
                consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cCouldn't connect to MySQL database"));

                if(PluginConfig.requireMySql) {
                    consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&cDisabling plugin"));
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
                    return;
                }
            }
        }

        // Commands
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Loading commands"));
        this.loadCommands();
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aCommands loaded"));
        
        // Events
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&7Loading events"));
        this.loadEvents();
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aEvents loaded"));
        
        // Update checker
        if(GitHubUpdater.updateAvailable()) {
            consoleSender.sendMessage(CTUtil.translate(consolePrefix));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&9A new update for this plugin is available"));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&9You can download it at " + GitHubUpdater.getGitHubUrl()));
            consoleSender.sendMessage(CTUtil.translate(consolePrefix));
        }

        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aPlugin enabled &7- Version: &6v" + this.getDescription().getVersion()));
        consoleSender.sendMessage(CTUtil.translate(consolePrefix + "&aDeveloped by &6KorzHorz"));

        // Update server data
        String serverMotd = Bukkit.getMotd();
        int serverMaxPlayers = Bukkit.getMaxPlayers();
        int serverOnlinePlayers = Bukkit.getOnlinePlayers().size();
        DB_Signs.getInstance().update(
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
                if(de.korzhorz.signs.subserver.data.Data.oldMotd != null && de.korzhorz.signs.subserver.data.Data.oldMotd.equals(serverMotd)) {
                    return;
                }

                de.korzhorz.signs.subserver.data.Data.oldMotd = serverMotd;
                DB_Signs.getInstance().update(
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
        de.korzhorz.signs.subserver.data.Data.shutdownBlocked = true;

        // Update server data
        String serverMotd = Bukkit.getMotd();
        int serverMaxPlayers = Bukkit.getMaxPlayers();
        DB_Signs.getInstance().update(
                serverMotd,
                serverMaxPlayers,
                0,
                false,
                null,
                true
        );

        while(de.korzhorz.signs.subserver.data.Data.shutdownBlocked) {
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        MySQLUtil.disconnect();
    }

    public boolean detectBungeeCord() {
        try {
            Data.bungeeCord = Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord");
        } catch(Exception e) {
            // Catch block left empty on purpose
        }

        if(!(Data.bungeeCord) && ConfigFiles.config.getBoolean("bungeecord.enforce")) {
            Data.bungeeCord = true;
            return true;
        }

        return false;
    }

    public void loadPluginChannels() {
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", PluginChannelUtil.getInstance());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.loadPluginMessages();
    }

    public void loadPluginMessages() {
        for(PluginChannelEvent pluginChannelEvent : PluginConfig.pluginChannelEvents) {
            PluginChannelInitiator.registerPluginChannelEvent(pluginChannelEvent.getHandledSubChannel(), pluginChannelEvent);
        }
    }

    public void loadDatabase() {
        for(DatabaseTableUtil databaseTableUtil : PluginConfig.databaseTableUtils) {
            databaseTableUtil.createTable();
        }
    }
    
    public void loadCommands() {
        for(Command command : PluginConfig.commands) {
            PluginCommand pluginCommand = this.getCommand(command.name());
            if(pluginCommand == null) {
                continue;
            }

            pluginCommand.setExecutor(command.commandExecutor());
        }
    }
    
    public void loadEvents() {
        for(Listener listener : PluginConfig.listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
