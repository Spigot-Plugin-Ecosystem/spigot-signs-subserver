package de.korzhorz.signs.subserver.configs;

public class Messages {
    public static String get(String path) {
        return ConfigFiles.messages.getString(path);
    }
}
