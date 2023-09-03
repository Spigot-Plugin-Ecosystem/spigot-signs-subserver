package de.korzhorz.signs.subserver.configs;

import de.korzhorz.signs.subserver.Main;
import de.korzhorz.signs.subserver.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigFiles {
    public static ConfigFile config = new ConfigFile("config.yml");
    public static ConfigFile messages = new ConfigFile("messages.yml");
    public static ConfigFile updater = new ConfigFile("updater.yml");
    public static ConfigFile server = new ConfigFile("server.yml");

    public static void initFileContents() {
        // Config
        if(PluginConfig.mySql) {
            config.setDefault("mysql.host", "localhost");
            config.setDefault("mysql.port", 3306);
            config.setDefault("mysql.database", "database");
            config.setDefault("mysql.username", "username");
            config.setDefault("mysql.password", "password");
        }
        if(PluginConfig.requireBungeeCord) {
            config.setDefault("bungeecord.enforce", false);
        }
        config.save();

        // Messages
        messages.setDefault("prefix", "&6&l" + PluginConfig.pluginName + " &8»");

        messages.setDefault("commands.errors.no-player", "&cDu musst ein Spieler sein um diesen Befehl auszuführen.");
        messages.setDefault("commands.errors.no-permission", "&cDu hast keine Rechte um diesen Befehl auszuführen.");
        messages.setDefault("commands.errors.bad-usage", "&cBenutze: &7%usage%");
        messages.setDefault("commands.errors.save-failed", "&cDie Änderungen konnten nicht gespeichert werden.");

        messages.setDefault("commands.maintenance.status.enabled", "&7Der &6Wartungsmodus &7ist &aaktiviert&7.");
        messages.setDefault("commands.maintenance.status.disabled", "&7Der &6Wartungsmodus &7ist &cdeaktiviert&7.");
        messages.setDefault("commands.maintenance.enable", "&7Der &6Wartungsmodus &7wurde &aaktiviert&7.");
        messages.setDefault("commands.maintenance.disable", "&7Der &6Wartungsmodus &7wurde &cdeaktiviert&7.");

        messages.setDefault("commands.motd.set", "&7Die &6Serverinformation &7wurde &agespeichert &7.");

        List<String> maintenanceKick = new ArrayList<>();
        maintenanceKick.add("&cMomentan finden &4&lWartungsarbeiten &cstatt.");
        maintenanceKick.add("&cDu kannst den Server deshalb nicht betreten.");
        messages.setDefault("maintenance.kick", maintenanceKick);

        messages.save();

        // Updater
        updater.setDefault("latest", JavaPlugin.getPlugin(Main.class).getDescription().getVersion());
        updater.setDefault("last-checked", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        updater.save();

        // Server
        server.setDefault("server-name", null);
        server.setDefault("server-name-updated", false);

        server.save();
    }
}
