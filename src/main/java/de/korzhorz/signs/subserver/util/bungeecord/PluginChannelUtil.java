package de.korzhorz.signs.subserver.util.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.korzhorz.signs.subserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PluginChannelUtil implements PluginMessageListener {
    private static final PluginChannelUtil instance = new PluginChannelUtil();

    private PluginChannelUtil() {}

    public static PluginChannelUtil getInstance() {
        return instance;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!(channel.equals("BungeeCord"))) {
            return;
        }

        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(message);
        String subChannel = byteArrayDataInput.readUTF();

        PluginChannelInitiator.pluginMessageReceived(subChannel, byteArrayDataInput);
    }

    private ByteArrayOutputStream constructOutputStream(String subChannel, Object... message) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF(subChannel);
            for(Object object : message) {
                if(object instanceof String) {
                    dataOutputStream.writeUTF((String) object);
                } else if(object instanceof Integer) {
                    dataOutputStream.writeInt((Integer) object);
                } else if(object instanceof Float) {
                    dataOutputStream.writeFloat((Float) object);
                } else if(object instanceof Double) {
                    dataOutputStream.writeDouble((Double) object);
                } else if(object instanceof Boolean) {
                    dataOutputStream.writeBoolean((Boolean) object);
                } else if(object instanceof Byte) {
                    dataOutputStream.writeByte((Byte) object);
                } else if(object instanceof Short) {
                    dataOutputStream.writeShort((Short) object);
                } else if(object instanceof Long) {
                    dataOutputStream.writeLong((Long) object);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }

    public void sendPluginMessage(String subChannel, Object... message) {
        ByteArrayOutputStream byteArrayOutputStream = this.constructOutputStream(subChannel, message);
        Bukkit.getServer().sendPluginMessage(JavaPlugin.getPlugin(Main.class), "BungeeCord", byteArrayOutputStream.toByteArray());
    }

    public void sendPluginMessage(Player player, String subChannel, Object... message) {
        ByteArrayOutputStream byteArrayOutputStream = this.constructOutputStream(subChannel, message);
        player.sendPluginMessage(JavaPlugin.getPlugin(Main.class), "BungeeCord", byteArrayOutputStream.toByteArray());
    }
}
