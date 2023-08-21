package de.korzhorz.template;

import de.korzhorz.template.configs.ConfigFiles;
import de.korzhorz.template.handlers.BungeeCordHandler;
import de.korzhorz.template.util.ColorTranslator;
import de.korzhorz.template.util.GitHubUpdater;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static BungeeCordHandler bungeeCordHandler = new BungeeCordHandler();

    @Override
    public void onEnable() {
        final String consolePrefix = "&7[&6Template&7]&r ";

        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Enabling"));
        
        this.getDataFolder().mkdir();

        // Plugin Channels
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Setting up plugin channels"));
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", Main.bungeeCordHandler);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aPlugin channels set up"));
        
        // Configuration Files
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&7Loading files"));
        ConfigFiles.initFileContents();
        this.getServer().getConsoleSender().sendMessage(ColorTranslator.translate(consolePrefix + "&aFiles loaded"));
        
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
    }

    @Override
    public void onDisable() {

    }
    
    public void loadCommands() {

    }
    
    public void loadEvents() {

    }
}
