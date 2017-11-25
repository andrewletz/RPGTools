package net.Vala.general;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import net.Vala.GUI.GUI;
import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.Pickaxe;
import net.Vala.pickaxe.Ore.Ores;
import net.Vala.util.GeneralUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RPGCommands implements CommandExecutor {
	
	//private static final Set<String> VALIDMODIFYINPUT = new HashSet<String>(Arrays.asList(new String[] {"level","sp","dura","durability","speed",
	//		"fortune","autoregen","reinforced","knockback","autosmelt","au","autosmeltul","autosmeltunlock","su","silktouchul","silktouchunlock"}));
	
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
						playerD.reloadConfig();
						playerD.refreshConfig();
						playerD.refreshAutoRegenTimers();
						Pickaxe.updatePickaxeInInventory(player);
					}
					sender.sendMessage(ChatColor.GREEN + "Player files reloaded.");
					Ores.initializeOres();
					Logger.log("&aReload complete.");
					
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
							sender.sendMessage(ChatColor.RED + args[1] + " is an invalid argument!");
							return false;
						}
						playerData.reloadConfig();
						playerData.refreshAutoRegenTimers();
						Pickaxe.updatePickaxeInInventory(targetPlayer);
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
		boolean onOrOff = false;
		if (Integer.parseInt(value) == 1) {
			onOrOff = true;
		} else if(Integer.parseInt(value) == 0) {
			onOrOff = false;
		}
		switch (input.toLowerCase()) {
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
				if (pickaxeData.getPickaxeSilktouch()) {
					return ("'s pick " + ChatColor.RED + "has not been modified. " + ChatColor.GREEN + "Cannot modify autosmelt while silktouch is active.");
				}
				pickaxeData.setPickaxeAutosmelt(onOrOff);
				return ("'s pickaxe autosmelt set to " + onOrOff);
			case "silktouch":
				if (pickaxeData.getPickaxeAutosmelt()) {
					return ("'s pick " + ChatColor.RED + "has not been modified. " + ChatColor.GREEN + "Cannot modify silktouch while autosmelt is active.");
				}
				pickaxeData.setPickaxeSilktouch(onOrOff);
				return ("'s pickaxe silktouch set to " + onOrOff);
			case "au":
			case "autosmeltul":
			case "autosmeltunlock":
				pickaxeData.setPickaxeAutosmeltUnlocked(onOrOff);
				return ("'s pickaxe autosmelt unlock state set to " + onOrOff);
			case "su":
			case "silktouchul":
			case "silktouchunlock":
				pickaxeData.setPickaxeSilktouchUnlocked(onOrOff);
				return ("'s pickaxe silktouch unlock state set to " + onOrOff);
		}
		throw new IllegalArgumentException();
	}

}