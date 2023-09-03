package de.korzhorz.signs.subserver.listeners;

import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.database.DB_Signs;
import de.korzhorz.signs.subserver.util.messages.CTUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class EVT_LoginEvent implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if(!(DB_Signs.getInstance().getMaintenance())) {
            return;
        }

        Player player = event.getPlayer();

        if(player.hasPermission("maintenance.bypass")) {
            return;
        }

        List<String> messages = ConfigFiles.messages.getStringList("maintenance.kick");
        StringBuilder kickMessageBuilder = new StringBuilder();
        for(String message : messages) {
            kickMessageBuilder.append(message).append("\n");
        }

        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, CTUtil.translate(kickMessageBuilder.toString().trim()));
    }
}
