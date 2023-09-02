package de.korzhorz.signs.subserver.util.messages;

import de.korzhorz.signs.subserver.configs.ConfigFiles;

public class Messages {
    private Messages() {

    }

    public static String get(String path) {
        return ConfigFiles.messages.getString(path);
    }
}
