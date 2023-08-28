package de.korzhorz.signs.subserver.commands;

import de.korzhorz.signs.subserver.configs.Messages;
import de.korzhorz.signs.subserver.util.ColorTranslator;
import de.korzhorz.signs.subserver.util.SignDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMD_Maintenance implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("maintenance"))) {
            sender.sendMessage(ColorTranslator.translate(Messages.get("prefix") + "&r " + Messages.get("commands.errors.no-permission")));
            return false;
        }

        if(args.length != 1) {
            String message = Messages.get("commands.errors.bad-usage");
            message = message.replaceAll("%usage%", command.getUsage());
            sender.sendMessage(ColorTranslator.translate(Messages.get("prefix") + "&r " + message));
            return false;
        }

        String message = "";
        boolean maintenance = false;
        switch(args[0].toLowerCase()) {
            case "status":
                message = Messages.get("commands.maintenance.status." + (SignDatabase.getMaintenance() ? "enabled" : "disabled"));
                sender.sendMessage(ColorTranslator.translate(Messages.get("prefix") + "&r " + message));
                return true;
            case "on", "enable":
                maintenance = true;
                break;
            case "off", "disable":
                break;
            default:
                message = Messages.get("commands.errors.bad-usage");
                message = message.replaceAll("%usage%", command.getUsage());
                sender.sendMessage(ColorTranslator.translate(Messages.get("prefix") + "&r " + message));
                return false;
        }

        SignDatabase.update(
                null,
                null,
                null,
                null,
                maintenance,
                false
        );

        message = Messages.get("commands.maintenance." + (maintenance ? "enable" : "disable"));
        sender.sendMessage(ColorTranslator.translate(Messages.get("prefix") + "&r " + message));

        return true;
    }
}
