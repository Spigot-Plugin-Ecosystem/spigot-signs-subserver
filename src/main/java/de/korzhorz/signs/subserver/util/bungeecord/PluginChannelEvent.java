package de.korzhorz.signs.subserver.util.bungeecord;

import com.google.common.io.ByteArrayDataInput;

public interface PluginChannelEvent {
    String getHandledSubChannel();

    void handle(ByteArrayDataInput byteArrayDataInput);
}
