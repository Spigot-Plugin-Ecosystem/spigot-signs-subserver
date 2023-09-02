package de.korzhorz.signs.subserver.listeners.pluginchannels;

import com.google.common.io.ByteArrayDataInput;
import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.util.bungeecord.PluginChannelEvent;

public class PC_GetServer implements PluginChannelEvent {
    @Override
    public String getHandledSubChannel() {
        return "GetServer";
    }

    @Override
    public void handle(ByteArrayDataInput byteArrayDataInput) {
        ConfigFiles.server.set("server-name", byteArrayDataInput.readUTF());
        ConfigFiles.server.set("server-name-updated", true);
        ConfigFiles.server.save();
    }
}
