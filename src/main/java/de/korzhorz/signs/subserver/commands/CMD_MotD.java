package de.korzhorz.signs.subserver.commands;

import de.korzhorz.signs.subserver.configs.ConfigFiles;
import de.korzhorz.signs.subserver.database.DB_Signs;
import de.korzhorz.signs.subserver.util.messages.CTUtil;
import de.korzhorz.signs.subserver.util.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMD_MotD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("motd"))) {
            sender.sendMessage(CTUtil.translate(Messages.get("prefix") + "&r " + Messages.get("commands.errors.no-permission")));
            return true;
        }

        if(args.length < 1) {
            String message = Messages.get("commands.errors.bad-usage");
            message = message.replaceAll("%usage%", command.getUsage());
            sender.sendMessage(CTUtil.translate(Messages.get("prefix") + "&r " + message));
            return true;
        }

        StringBuilder motdBuilder = new StringBuilder();
        for(String arg : args) {
            motdBuilder.append(arg).append(" ");
        }
        String motd = motdBuilder.toString().trim();

        Bukkit.setMotd(CTUtil.translate(motd));

        ConfigFiles.config.set("motd", motd);
        ConfigFiles.config.save();

        String message = Messages.get("commands.motd.set");
        sender.sendMessage(CTUtil.translate(Messages.get("prefix") + "&r " + message));

        DB_Signs.getInstance().update(
                motd,
                null,
                null,
                null,
                null,
                false
        );

        return true;
    }
}
