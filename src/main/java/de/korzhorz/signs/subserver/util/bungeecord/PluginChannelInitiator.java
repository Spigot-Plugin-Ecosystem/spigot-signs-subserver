package de.korzhorz.signs.subserver.util.bungeecord;

import com.google.common.io.ByteArrayDataInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PluginChannelInitiator {
    private static final HashMap<String, List<PluginChannelEvent>> pluginChannelEvents = new HashMap<>();

    private PluginChannelInitiator() {

    }

    public static void registerPluginChannelEvent(String subChannel, PluginChannelEvent pluginChannelEvent) {
        List<PluginChannelEvent> pluginChannelEvents = new ArrayList<>();
        if(PluginChannelInitiator.pluginChannelEvents.containsKey(subChannel)) {
            pluginChannelEvents = PluginChannelInitiator.pluginChannelEvents.get(subChannel);
        }

        pluginChannelEvents.add(pluginChannelEvent);

        PluginChannelInitiator.pluginChannelEvents.put(subChannel, pluginChannelEvents);
    }

    public static void pluginMessageReceived(String subChannel, ByteArrayDataInput byteArrayDataInput) {
        List<PluginChannelEvent> pluginChannelEvents = PluginChannelInitiator.pluginChannelEvents.get(subChannel);

        if(pluginChannelEvents == null) {
            return;
        }

        for(PluginChannelEvent pluginChannelEvent : pluginChannelEvents) {
            pluginChannelEvent.handle(byteArrayDataInput);
        }
    }
}
