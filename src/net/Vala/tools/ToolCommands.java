package net.Vala.tools;

import net.Vala.GUI.PickaxeGUI;
import net.Vala.config.PlayerData;
import net.Vala.util.GeneralUtil;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToolCommands implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		switch (commandLabel.toLowerCase()) {
		case "pick":
		case "pickaxe":
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					return false;
				}
				Player player = (Player) sender;
				PlayerData playerData = PlayerData.getData(player);
				if (playerData.getPickaxeData().hasToolInInventory()) {
					player.sendMessage(ChatColor.RED + "You already have your pickaxe!");
					return false;
				}
				// Spawn pickaxe
				if (playerData.getPickaxeData().giveNew(player)) {
					player.sendMessage(ChatColor.GREEN + "Your pickaxe appeared in your inventory.");
					player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.25F, 1.8F);
					return true;
				}
				player.sendMessage(ChatColor.RED + "Your inventory is full.");
				return false;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("gui") || args[0].equalsIgnoreCase("menu")) {
					if (!(sender instanceof Player)) {
						return false;
					}
					Player player = (Player) sender;
					PickaxeGUI.openManagementInventory(player);
					return true;
				} else if (args[0].equalsIgnoreCase("repair")) {
					if (!(sender instanceof Player)) {
						return false;
					}
					Player player = (Player) sender;
					PickaxeGUI.openScrapInventory(player);
				} else {
					sender.sendMessage(GeneralUtil.getHelpMessage());
				}
			} else {
				sender.sendMessage(GeneralUtil.getHelpMessage());
				return false;
			}
			break;
		}
		return false;
	}

}