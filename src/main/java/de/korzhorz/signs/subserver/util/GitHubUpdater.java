package de.korzhorz.signs.subserver.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import de.korzhorz.signs.subserver.Main;
import de.korzhorz.signs.subserver.configs.ConfigFiles;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitHubUpdater {
    private static final String user = "Spigot-Plugin-Ecosystem";
    private static final String repository = "spigot-signs-subserver";
    private static String latestVersion = null;
    private static final String currentVersion = JavaPlugin.getPlugin(Main.class).getDescription().getVersion();

    public static boolean updateAvailable() {
        GitHubUpdater.checkUpdates();
        if(latestVersion == null) {
            return false;
        }

        // Check if current version is more recent than latest version
        String[] currentVersionSplit = currentVersion.split("\\.");
        String[] latestVersionSplit = latestVersion.split("\\.");

        for(int i = 0; i < currentVersionSplit.length; i++) {
            if(Integer.parseInt(currentVersionSplit[i]) < Integer.parseInt(latestVersionSplit[i])) {
                return true;
            } else if(Integer.parseInt(currentVersionSplit[i]) > Integer.parseInt(latestVersionSplit[i])) {
                return false;
            }
        }

        return false;
    }
    
    public static void checkUpdates() {
        try {
            Date lastChecked = new SimpleDateFormat("yyyy-MM-dd").parse(ConfigFiles.updater.getString("last-checked"));

            if(lastChecked.after(new Date(System.currentTimeMillis() - 86400000L))) {
                latestVersion = ConfigFiles.updater.getString("latest");
                return;
            }
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        
        try {
            String url = "https://api.github.com/repos/" + user + "/" + repository + "/releases";

            try(InputStream inputStream = new URL(url).openStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                int cp;
                while((cp = bufferedReader.read()) != -1) {
                    stringBuilder.append((char) cp);
                }
                String json = stringBuilder.toString();
                JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);

                if(jsonArray.size() == 0) {
                    JavaPlugin.getPlugin(Main.class).getServer().getConsoleSender().sendMessage(ColorTranslator.translate("&7[&9GitHub&7] &cGitHub update checker unavailable"));
                    latestVersion = ConfigFiles.updater.getString("latest");
                    return;
                }

                latestVersion = jsonArray.get(0).getAsJsonObject().get("tag_name").getAsString();
                
                ConfigFiles.updater.set("latest", latestVersion);
                ConfigFiles.updater.set("last-checked", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                ConfigFiles.updater.save();
            } catch(FileNotFoundException e) {
                JavaPlugin.getPlugin(Main.class).getServer().getConsoleSender().sendMessage(ColorTranslator.translate("&7[&9GitHub&7] &cGitHub update checker unavailable"));
                latestVersion = ConfigFiles.updater.getString("latest");
            }
        } catch(IOException e) {
            JavaPlugin.getPlugin(Main.class).getServer().getConsoleSender().sendMessage(ColorTranslator.translate("&7[&9GitHub&7] &cGitHub update checker unavailable"));
            latestVersion = ConfigFiles.updater.getString("latest");
        }
    }
}
