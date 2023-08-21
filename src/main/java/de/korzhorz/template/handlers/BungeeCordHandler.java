package de.korzhorz.template.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.korzhorz.template.Main;
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
    }

    public void sendPluginMessage(String subChannel, String[] message) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.write(subChannel.getBytes());
            for(String string : message) {
                dataOutputStream.write(string.getBytes());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        Bukkit.getServer().sendPluginMessage(JavaPlugin.getPlugin(Main.class), "BungeeCord", byteArrayOutputStream.toByteArray());
    }
}
