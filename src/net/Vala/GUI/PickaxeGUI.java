package net.Vala.GUI;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.repair.Scrap;
import net.Vala.tools.RPGPickaxe;
import net.Vala.traits.DropChances;
import net.Vala.traits.TraitUtil;
import net.Vala.traits.Reinforced;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PickaxeGUI {
	
	public static void openManagementInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Management");
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		TraitUtil pickaxeUtil = new TraitUtil(YAMLFile.PICKAXESPCOSTS);
		
		// Get our pick icon for the first slot
		ItemStack pickIcon = pickaxeData.getTool();
		
		// Set up speed icon/button
		Icon speedIcon = new Icon(Material.NETHER_STAR);
		int currentSpeed = pickaxeData.getSpeed();
		if (pickaxeData.isMaxSpeed()) {
			speedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Mining Speed" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			speedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Mining Speed" + ChatColor.AQUA + " [" + pickaxeUtil.getSpeedSPReq(currentSpeed + 1) + " SP to level]");
		}
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + currentSpeed);
		speedIcon.addLore("");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + RPGPickaxe.convertSpeedToReadable(RPGPickaxe.convertPickSpeedToDamagePerTick(currentSpeed)));
		if (!pickaxeData.isMaxSpeed()) {
			speedIcon.addLore(ChatColor.BLUE + "" + ChatColor.BOLD + "     -> " + ChatColor.BLUE + RPGPickaxe.convertSpeedToReadable(RPGPickaxe.convertPickSpeedToDamagePerTick(currentSpeed + 1)));
			speedIcon.addLore("");
			speedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			speedIcon.addLore(ChatColor.GRAY + "your max pick speed to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (currentSpeed + 1) + ".");
		}
		speedIcon.addLore("");
		speedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Shift-click" + ChatColor.GRAY + " here to manually set your");
		speedIcon.addLore(ChatColor.GRAY + "pick speed to anything up to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (currentSpeed) + ".");
		speedIcon.addLore("");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Manual setting doesn't use SP, it is for");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "when you want your pick to go slower!");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "(this option will prompt you in chat)");
		
		// Set up fortune icon/button
		DropChances currentLevel = new DropChances(pickaxeData.getFortune(), YAMLFile.PICKAXECONFIG);
		double currDoubleDrop = (double)Math.round(currentLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
		double currTripleDrop = (double)Math.round(currentLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
		
		DropChances nextLevel = new DropChances(pickaxeData.getFortune() + 1, YAMLFile.PICKAXECONFIG);
		double nextDoubleDrop = (double)Math.round(nextLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
		double nextTripleDrop = (double)Math.round(nextLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
		
		Icon fortuneIcon = new Icon(Material.EMERALD);
		if (pickaxeData.isMaxFortune()) {
			fortuneIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Fortune" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			fortuneIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Fortune" + ChatColor.AQUA + " [" + pickaxeUtil.getFortuneSPReq(pickaxeData.getFortune() + 1) + " SP to level]");
		}
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getFortune());
		fortuneIcon.addLore("");
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Double Drop: " + ChatColor.BLUE + currDoubleDrop + "%" + 
				(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextDoubleDrop + "%" : ""));
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Triple Drop: " + ChatColor.BLUE + currTripleDrop + "%" + 
				(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextTripleDrop + "%" : ""));
		if (!pickaxeData.isMaxFortune()) {
			fortuneIcon.addLore("");
			fortuneIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			fortuneIcon.addLore(ChatColor.GRAY + "your pick fortune to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getFortune() + 1) + ".");
		}
		
		// Set up auto-regeneration icon/button
		Icon regenIcon = new Icon(Material.VINE);
		if (pickaxeData.isMaxAutoregen()) {
			regenIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Auto-regen" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			regenIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Auto-regen" + ChatColor.AQUA + " [" + pickaxeUtil.getAutoRegenSPReq(pickaxeData.getAutoregen() + 1) + " SP to level]");
		}
		regenIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getAutoregen());
		regenIcon.addLore("");
		regenIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Rate: " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass(false).convertLevelToRandomTick(pickaxeData.getAutoregen()) + " ticks");
		if (!pickaxeData.isMaxAutoregen()) {
			regenIcon.addLore(ChatColor.BLUE + "" + ChatColor.BOLD + "   -> " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass(false).convertLevelToRandomTick(pickaxeData.getAutoregen() + 1) + " ticks");
			regenIcon.addLore("");
			regenIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			regenIcon.addLore(ChatColor.GRAY + "your pick auto-regen to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getAutoregen() + 1) + ".");
		}
		
		// Set up reinforcement icon/button
		Reinforced reinforced = new Reinforced(YAMLFile.PICKAXECONFIG);
		Icon reinforcedIcon = new Icon(Material.ANVIL);
		if (pickaxeData.isMaxReinforced()) {
			reinforcedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Reinforced" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			reinforcedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Reinforced" + ChatColor.AQUA + " [" + pickaxeUtil.getReinforcedSPReq(pickaxeData.getReinforced() + 1) + " SP to level]");
		}
		reinforcedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getReinforced());
		reinforcedIcon.addLore("");
		reinforcedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Protection chance: " + (pickaxeData.isMaxReinforced() ? ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getReinforced()) + "%" : ""));
		if(!pickaxeData.isMaxReinforced()) {
			reinforcedIcon.addLore(ChatColor.BLUE + "         " + reinforced.getTotalPercent(pickaxeData.getReinforced()) + "%" + ChatColor.BOLD + " -> " 
					+ ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getReinforced() + 1) + "%");
			reinforcedIcon.addLore("");
			reinforcedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			reinforcedIcon.addLore(ChatColor.GRAY + "your reinforced to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getReinforced() + 1) + ".");
		}
		
		// Set up fortune icon/button
//		Icon knockbackIcon = new Icon(Material.ARROW);
//		ItemMeta knockbackIconMeta = knockbackIcon.getItemMeta();
//		knockbackIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Knockback");
//		knockbackIcon.setItemMeta(knockbackIconMeta);
		
		// Set up autosmelt icon/button
		Icon autosmeltIcon = new Icon(Material.BLAZE_POWDER);
		if (pickaxeData.getAutosmeltUnlocked()) {
			autosmeltIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.AQUA + " [UNLOCKED]");
		} else {
			autosmeltIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.AQUA + " [" + pickaxeUtil.getAutosmeltSPReq() + " SP TO UNLOCK]");
		}
		autosmeltIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Toggled: " + ChatColor.WHITE + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + String.valueOf(pickaxeData.getAutosmelt()).toUpperCase());
		autosmeltIcon.addLore("");
		if (!pickaxeData.getAutosmeltUnlocked()) {
			autosmeltIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to unlock");
			autosmeltIcon.addLore(ChatColor.GRAY + "autosmelt permanently.");
		} else {
			autosmeltIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to toggle");
			autosmeltIcon.addLore(ChatColor.GRAY + "autosmelt on your pickaxe.");
		}
		if (pickaxeData.getAutosmelt()) {
			autosmeltIcon.addGlow();
		}
		
		// Set up silktouch icon/button
		Icon silktouchIcon = new Icon(Material.GRASS);
		if (pickaxeData.getSilktouchUnlocked()) {
			silktouchIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Silktouch" + ChatColor.AQUA + " [UNLOCKED]");
		} else {
			silktouchIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Silktouch" + ChatColor.AQUA + " [" + pickaxeUtil.getSilktouchSPReq() + " SP TO UNLOCK]");
		}
		silktouchIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Toggled: " + ChatColor.WHITE + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + String.valueOf(pickaxeData.getSilktouch()).toUpperCase());
		silktouchIcon.addLore("");
		if (!pickaxeData.getSilktouchUnlocked()) {
			silktouchIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to unlock");
			silktouchIcon.addLore(ChatColor.GRAY + "silktouch permanently.");
		} else {
			silktouchIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to toggle");
			silktouchIcon.addLore(ChatColor.GRAY + "silktouch on your pickaxe.");
		}
		if (pickaxeData.getSilktouch()) {
			silktouchIcon.addGlow();
		}
		
		// Set all the inventory slots to their respective icons
		inv.setItem(0, pickIcon);
		inv.setItem(2, speedIcon);
		inv.setItem(3, fortuneIcon);
		inv.setItem(4, regenIcon);
		inv.setItem(5, reinforcedIcon);
		inv.setItem(7, autosmeltIcon);
		inv.setItem(8, silktouchIcon);
		
		inv.setItem(6, GUI.BLANK_PANE); // knockback currently not implemented
		inv.setItem(1, GUI.BLANK_PANE);
		
		player.openInventory(inv);
	}
	
	public static void openScrapInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, 27, ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair");
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		
		// Repair button at bottom right of menu set-up
		Icon repairIcon = new Icon(Material.INK_SACK, (byte) 10);
		repairIcon.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Consume Items and Repair ");
		repairIcon.addLore(ChatColor.GRAY + "To repair your pickaxe, you need");
		repairIcon.addLore(ChatColor.GRAY + "to place items that are made of");
		repairIcon.addLore(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + 
				Scrap.matToString(Scrap.toolMatToScrapMat(pickaxeData.getTool().getType())) + ChatColor.GRAY + " in this inventory and left");
		repairIcon.addLore(ChatColor.GRAY + "click this button." + ChatColor.RED +  " Must be near an anvil.");
		
		// Set all the inventory slots to their respective icons
		inv.setItem(18, pickaxeData.getTool());
		inv.setItem(19, GUI.BLANK_PANE);
		inv.setItem(20, GUI.BLANK_PANE);
		inv.setItem(21, GUI.BLANK_PANE);
		inv.setItem(22, GUI.BLANK_PANE);
		inv.setItem(23, GUI.BLANK_PANE);
		inv.setItem(24, GUI.BLANK_PANE);
		inv.setItem(25, GUI.BLANK_PANE);
		inv.setItem(26, repairIcon);
		
		player.openInventory(inv);
	}

}
