package de.korzhorz.signs.subserver.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.korzhorz.signs.subserver.Main;
import de.korzhorz.signs.subserver.configs.ConfigFiles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeCordHandler implements PluginMessageListener {
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!(channel.equals("BungeeCord"))) {
            return;
        }

        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(message);
        String subChannel = byteArrayDataInput.readUTF();
        if(subChannel.equals("GetServer")) {
            ConfigFiles.server.set("server-name", byteArrayDataInput.readUTF());
            ConfigFiles.server.set("server-name-updated", true);
            ConfigFiles.server.save();
        }
    }

    public void sendPluginMessage(String subChannel) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF(subChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(JavaPlugin.getPlugin(Main.class), "BungeeCord", byteArrayOutputStream.toByteArray());
    }

    public void sendPluginMessage(String subChannel, Object[] message) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF(subChannel);
            for(Object object : message) {
                if(object instanceof String) {
                    dataOutputStream.writeUTF((String) object);
                } else if(object instanceof Integer) {
                    dataOutputStream.writeInt((Integer) object);
                } else if(object instanceof Boolean) {
                    dataOutputStream.writeBoolean((Boolean) object);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(JavaPlugin.getPlugin(Main.class), "BungeeCord", byteArrayOutputStream.toByteArray());
    }
}
