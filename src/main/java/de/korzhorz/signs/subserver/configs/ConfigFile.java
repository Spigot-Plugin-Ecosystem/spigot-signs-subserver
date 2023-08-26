package de.korzhorz.signs.subserver.configs;

import de.korzhorz.signs.subserver.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfigFile {
    private final File file;
    private final FileConfiguration fileConfiguration;

    public ConfigFile(String fileName) {
        this.file = new File(JavaPlugin.getPlugin(Main.class).getDataFolder(), fileName);
        if(!(this.file.exists())) {
            try {
                this.file.createNewFile();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void setDefault(String path, Object value) {
        if(!(this.fileConfiguration.contains(path))) {
            this.fileConfiguration.set(path, value);
        }
    }

    public void set(String path, Object value) {
        this.fileConfiguration.set(path, value);
    }

    public void set(String path, Location value) {
        this.fileConfiguration.set(path + ".world", Objects.requireNonNull(value.getWorld()).getName());
        this.fileConfiguration.set(path + ".x", value.getX());
        this.fileConfiguration.set(path + ".y", value.getY());
        this.fileConfiguration.set(path + ".z", value.getZ());
        this.fileConfiguration.set(path + ".yaw", value.getYaw());
        this.fileConfiguration.set(path + ".pitch", value.getPitch());
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return this.fileConfiguration.getString(path);
    }

    public int getInt(String path) {
        return this.fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return this.fileConfiguration.getDouble(path);
    }

    public long getLong(String path) {
        return this.fileConfiguration.getLong(path);
    }

    public boolean getBoolean(String path) {
        return this.fileConfiguration.getBoolean(path);
    }

    public List<?> getList(String path) {
        return this.fileConfiguration.getList(path);
    }

    public List<String> getStringList(String path) {
        return this.fileConfiguration.getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return this.fileConfiguration.getIntegerList(path);
    }

    public List<Double> getDoubleList(String path) {
        return this.fileConfiguration.getDoubleList(path);
    }

    public List<Float> getFloatList(String path) {
        return this.fileConfiguration.getFloatList(path);
    }

    public List<Long> getLongList(String path) {
        return this.fileConfiguration.getLongList(path);
    }

    public List<Byte> getByteList(String path) {
        return this.fileConfiguration.getByteList(path);
    }

    public List<Character> getCharacterList(String path) {
        return this.fileConfiguration.getCharacterList(path);
    }

    public List<Short> getShortList(String path) {
        return this.fileConfiguration.getShortList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return this.fileConfiguration.getMapList(path);
    }

    public Location getLocation(String path) {
        return new Location(
                Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getServer().getWorld(this.fileConfiguration.getString(path + ".world"))),
                this.fileConfiguration.getDouble(path + ".x"),
                this.fileConfiguration.getDouble(path + ".y"),
                this.fileConfiguration.getDouble(path + ".z"),
                (float) this.fileConfiguration.getDouble(path + ".yaw"),
                (float) this.fileConfiguration.getDouble(path + ".pitch")
        );
    }

    public boolean contains(String path) {
        return this.fileConfiguration.contains(path);
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    public File getFile() {
        return this.file;
    }
}
