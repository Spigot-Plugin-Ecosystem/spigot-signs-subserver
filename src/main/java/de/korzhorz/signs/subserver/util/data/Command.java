package de.korzhorz.signs.subserver.util.data;

import org.bukkit.command.CommandExecutor;

public record Command(String name, CommandExecutor commandExecutor) {
}
