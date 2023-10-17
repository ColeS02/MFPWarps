package com.unclecole.mfpwarps.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public enum TL {
	NO_PERMISSION("messages.no-permission", "&cYou don't have the permission to do that."),
	INVALID_ARGUMENTS("messages.invalid-arguments", "%prefix%&a<command>"),
	CANT_SET_WARP("messages.cant-set-warp", "&4&lERROR &fYou can't set a Player Warp here!"),
	SET_WARP("messages.set-warp", "&a%player% &fhas created a new warp &b%warp%!"),
	WRITE_NAME("messages.write-name", "&fPlease enter the name of your warp! Type '&c&lCANCEL&f' to cancel!"),
	SET_WARP_NAME("messages.set-warp-name", "&a&lSUCCESS! &fYou successfully set warpname to %name%&f!"),
	NAME_TOO_LONG("messages.name-too-long", "&c&lERROR! &fName too long, Must be under &a%length% &fcharacters!"),
	CANCELLED_NAME_CHANGE("messages.cancelled-name-change", "&a&lSUCCESS! &fYou successfully cancelled the name change!"),
	NAME_TAKEN("messages.name-taken", "&c&lERROR! &fWarp name already taken!"),
	PWARP_DELETED("messages.pwarp-deleted", "&a&lSUCCESS! &fPWarp has been Deleted!");

	private final String path;

	private String def;
	private static ConfigFile config;

	TL(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}

	public void setDefault(String message) {
		this.def = message;
	}

	public void send(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(C.color(getDefault()));
		} else {
			sender.sendMessage(C.strip(getDefault()));
		}
	}

	public static void loadMessages(ConfigFile configFile) {
		config = configFile;
		FileConfiguration data = configFile.getConfig();
		for (TL message : values()) {
			if (!data.contains(message.getPath())) {
				data.set(message.getPath(), message.getDefault());
			}
		}
		configFile.save();
	}



	public void send(CommandSender sender, PlaceHolder... placeHolders) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			sender.sendMessage(C.color(getDefault(), placeHolders));
		} else {
			sender.sendMessage(C.strip(getDefault(), placeHolders));
		}
	}
	public void broadcast(PlaceHolder... placeHolders) {
		Bukkit.broadcastMessage(C.color(getDefault(), placeHolders));
	}


	public static void message(CommandSender sender, String message) {
		sender.sendMessage(C.color(message));
	}

	public static void message(CommandSender sender, String message, PlaceHolder... placeHolders) {
		sender.sendMessage(C.color(message, placeHolders));
	}

	public static void message(CommandSender sender, List<String> message) {
		message.forEach(m -> sender.sendMessage(C.color(m)));
	}

	public static void message(CommandSender sender, List<String> message, PlaceHolder... placeHolders) {
		message.forEach(m -> sender.sendMessage(C.color(m, placeHolders)));
	}

	public static void log(Level lvl, String message) {
		Bukkit.getLogger().log(lvl, message);
	}
}
