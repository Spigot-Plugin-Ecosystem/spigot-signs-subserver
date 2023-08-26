package de.korzhorz.signs.subserver.data;

import java.util.Objects;

public class ServerData {
    private final String name;
    private final String motd;
    private final int maxPlayers;
    private final int onlinePlayers;
    private final boolean online;
    private final boolean maintenance;

    public ServerData(String name, String motd, int maxPlayers, int onlinePlayers, boolean online, boolean maintenance) {
        this.name = name;
        this.motd = motd;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
        this.online = online;
        this.maintenance = maintenance;
    }

    public String getName() {
        return this.name;
    }

    public String getMotd() {
        return this.motd;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public boolean getOnline() {
        return this.online;
    }

    public boolean getMaintenance() {
        return this.maintenance;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || getClass() != object.getClass()) return false;
        ServerData that = (ServerData) object;
        return maxPlayers == that.maxPlayers && onlinePlayers == that.onlinePlayers && online == that.online && maintenance == that.maintenance && Objects.equals(name, that.name) && Objects.equals(motd, that.motd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, motd, maxPlayers, onlinePlayers, online, maintenance);
    }
}
