package de.korzhorz.signs.subserver;

import de.korzhorz.signs.subserver.commands.CMD_Maintenance;
import de.korzhorz.signs.subserver.commands.CMD_MotD;
import de.korzhorz.signs.subserver.database.DB_Signs;
import de.korzhorz.signs.subserver.listeners.EVT_LoginEvent;
import de.korzhorz.signs.subserver.listeners.EVT_UpdatePlayerCount;
import de.korzhorz.signs.subserver.listeners.pluginchannels.PC_GetServer;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelEvent;
import de.korzhorz.signs.subserver.util.data.Command;
import de.korzhorz.signs.subserver.util.database.DatabaseTableUtil;
import org.bukkit.event.Listener;

public class PluginConfig {
    public static String pluginName = "Signs-Subserver";

    public static boolean requireBungeeCord = true;
    public static boolean pluginChannels = true;
    public static boolean mySql = true;
    public static boolean requireMySql = true;

    public static PluginChannelEvent[] pluginChannelEvents = new PluginChannelEvent[]{
        new PC_GetServer()
    };
    public static DatabaseTableUtil[] databaseTableUtils = new DatabaseTableUtil[]{
        DB_Signs.getInstance()
    };
    public static Command[] commands = new Command[]{
        new Command("maintenance", new CMD_Maintenance()),
        new Command("motd", new CMD_MotD())
    };
    public static Listener[] listeners = new Listener[]{
        new EVT_UpdatePlayerCount(),
        new EVT_LoginEvent()
    };

    public static String gitHubUser = "Spigot-Plugin-Ecosystem";
    public static String gitHubRepo = "spigot-signs-subserver";

    private PluginConfig() {

    }
}
