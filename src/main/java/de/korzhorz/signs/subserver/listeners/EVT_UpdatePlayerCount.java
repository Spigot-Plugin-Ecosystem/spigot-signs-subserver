package de.korzhorz.signs.subserver.listeners;

import de.korzhorz.signs.subserver.Main;
import de.korzhorz.signs.subserver.util.SignDatabase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EVT_UpdatePlayerCount implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String serverMotd = Bukkit.getMotd();
        Integer serverMaxPlayers = Bukkit.getMaxPlayers();
        Integer serverOnlinePlayers = Bukkit.getOnlinePlayers().size();
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                SignDatabase.update(
                        serverMotd,
                        serverMaxPlayers,
                        serverOnlinePlayers,
                        null,
                        null,
                        false
                );
            }
        }, 1L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String serverMotd = Bukkit.getMotd();
        Integer serverMaxPlayers = Bukkit.getMaxPlayers();
        Integer serverOnlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                SignDatabase.update(
                        serverMotd,
                        serverMaxPlayers,
                        serverOnlinePlayers,
                        null,
                        null,
                        false
                );
            }
        }, 1L);
    }
}
