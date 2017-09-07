package net.Vala.general;

import net.Vala.GUI.GUI;
import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.PickaxeFactory;
import net.Vala.pickaxe.Ore.Ores;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import util.GeneralUtil;

public class RPGCommands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("rpgtools")
				|| commandLabel.equalsIgnoreCase("rt")) {
			if (sender.isOp()) {
				if (args.length == 0) {
					sender.sendMessage(GeneralUtil.getHelpMessage());
					return false;
				}
				
				// Reload command
				if (args[0].equalsIgnoreCase("reload")) {
					for (YAMLFile yaml : YAMLFile.values()) {
						yaml.setConfig(YamlConfiguration.loadConfiguration(yaml.getFile()));
						sender.sendMessage(ChatColor.GREEN + "" + yaml + " reloaded.");
					}
					for (Player player : Bukkit.getOnlinePlayers()) {
						PlayerData playerD = PlayerData.getData(player);
						playerD.refreshAutoRegenTimers();
					}
					Ores.initializeOres();
				
				// GUI Open command
				} else if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("gui")) {
					GUI.openMain((Player) sender);
					
				// Modify pick command
				} else if (args[0].equalsIgnoreCase("modifypick") || args[0].equalsIgnoreCase("modifypickaxe")) {
					if (args.length < 4) {
						sender.sendMessage(ChatColor.RED + "Correct usage: /rt modifypick [stat] [player] [value]");
						return false;
					}
					if (Bukkit.getPlayer(args[2]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[2]);
						PlayerData playerData = PlayerData.getData(targetPlayer);
						String sendBack;
						if (args[3].equalsIgnoreCase("on") || args[3].equalsIgnoreCase("true")) {
							args[3] = "1";
						} else if (args[3].equalsIgnoreCase("off") || args[3].equalsIgnoreCase("false")) {
							args[3] = "0";
						}
						try {
							sendBack = inputToPickMethod(args[1], args[3], playerData.getPickaxeData());
						} catch (IllegalArgumentException e) {
							sender.sendMessage(ChatColor.RED + "Invalid argument!");
							return false;
						}
						playerData.reloadConfig();
						playerData.refreshAutoRegenTimers();
						PickaxeFactory.updatePickaxeInInventory(targetPlayer);
						sender.sendMessage(ChatColor.GREEN + args[2] + sendBack);
					} else {
						sender.sendMessage(ChatColor.RED + "Player not found!");
					}
				} else {
					sender.sendMessage(GeneralUtil.getHelpMessage());
				}
			} else {
			sender.sendMessage(ChatColor.RED + "Unknown command.");
			}
		}
		return false;
	}
	
	private String inputToPickMethod(String input, String value, PickaxeData pickaxeData) throws IllegalArgumentException {
		boolean onOrOff = "1".equals(Integer.parseInt(value));
		switch (input) {
			case "level":
				pickaxeData.setPickaxeLevel(Integer.parseInt(value));
				return ("'s pickaxe level set to " + value);
			case "sp":
				pickaxeData.setPickaxeSP(Integer.parseInt(value));
				return ("'s pickaxe SP set to " + value);
			case "dura":
			case "durability":
				if (value.equalsIgnoreCase("max")) {
					pickaxeData.setPickaxeCurrentDurability(pickaxeData.getPickaxeMaxDurability());
				} else {
					pickaxeData.setPickaxeCurrentDurability(Integer.parseInt(value));
				}
				return ("'s pickaxe current durability set to " + value);
			case "speed":
				pickaxeData.setPickaxeSpeed(Integer.parseInt(value));
				return ("'s pickaxe speed set to " + value);
			case "fortune":
				pickaxeData.setPickaxeFortune(Integer.parseInt(value));
				return ("'s pickaxe fortune set to " + value);
			case "autoregen":
				pickaxeData.setPickaxeAutoregen(Integer.parseInt(value));
				return ("'s pickaxe autoregen set to " + value);
			case "reinforced":
				pickaxeData.setPickaxeReinforced(Integer.parseInt(value));
				return ("'s pickaxe reinforced set to " + value);
			case "knockback":
				pickaxeData.setPickaxeKnockback(Integer.parseInt(value));
				return ("'s pickaxe knockback set to " + value);
			case "autosmelt":
				pickaxeData.setPickaxeAutosmelt(onOrOff);
				return ("'s pickaxe autosmelt set to " + onOrOff);
			case "silktouch":
				pickaxeData.setPickaxeAutosmelt(onOrOff);
				return ("'s pickaxe autosmelt set to " + onOrOff);
		}
		throw new IllegalArgumentException();
	}

}