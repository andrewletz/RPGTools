package net.Vala.GUI;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Logger;
import net.Vala.pickaxe.PickaxeFactory;
import net.Vala.traits.AutoRegen;
import net.Vala.traits.DropChances;
import net.Vala.traits.GeneralTraitUtil;
import net.Vala.traits.Reinforced;
import net.Vala.util.EnchantGlow;
import net.Vala.util.ScrapUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PickaxeGUI {
	
	public static void openManagementInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, 9, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Management");
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		
		// Get our pick icon for the first slot
		ItemStack pickIcon = PickaxeFactory.getNewPickaxe(player);
		
		// Set up speed icon/button
		Icon speedIcon = new Icon(Material.NETHER_STAR);
		int currentSpeed = pickaxeData.getPickaxeSpeed();
		if (pickaxeData.isMaxSpeed()) {
			speedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Mining Speed" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			speedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Mining Speed" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickSpeedSPReq(currentSpeed + 1) + " SP to level]");
		}
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + currentSpeed);
		speedIcon.addLore("");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + PickaxeFactory.convertSpeedToReadable(PickaxeFactory.convertPickSpeedToDamagePerTick(currentSpeed))
						+ (!pickaxeData.isMaxSpeed() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + PickaxeFactory.convertSpeedToReadable(PickaxeFactory.convertPickSpeedToDamagePerTick(currentSpeed + 1)) : ""));
		speedIcon.addLore("");
		if (!pickaxeData.isMaxSpeed()) {
			speedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			speedIcon.addLore(ChatColor.GRAY + "your max pick speed to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (currentSpeed + 1) + ".");
			speedIcon.addLore("");
		}
		speedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Shift-click" + ChatColor.GRAY + " here to manually set your");
		speedIcon.addLore(ChatColor.GRAY + "pick speed to anything up to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (currentSpeed) + ".");
		speedIcon.addLore("");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Manual setting doesn't use SP, it is for");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "when you want your pick to go slower!");
		speedIcon.addLore(ChatColor.GRAY + "" + ChatColor.ITALIC + "(this option will prompt you in chat)");
		
		// Set up fortune icon/button
		DropChances currentLevel = new DropChances(pickaxeData.getPickaxeFortune(), YAMLFile.PICKAXECONFIG);
		double currDoubleDrop = (double)Math.round(currentLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
		double currTripleDrop = (double)Math.round(currentLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
		
		DropChances nextLevel = new DropChances(pickaxeData.getPickaxeFortune() + 1, YAMLFile.PICKAXECONFIG);
		double nextDoubleDrop = (double)Math.round(nextLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
		double nextTripleDrop = (double)Math.round(nextLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
		
		Icon fortuneIcon = new Icon(Material.EMERALD);
		if (pickaxeData.isMaxFortune()) {
			fortuneIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Fortune" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			fortuneIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Fortune" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickFortuneSPReq(pickaxeData.getPickaxeFortune() + 1) + " SP to level]");
		}
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeFortune());
		fortuneIcon.addLore("");
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Double Drop: " + ChatColor.BLUE + currDoubleDrop + "%" + 
				(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextDoubleDrop + "%" : ""));
		fortuneIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Triple Drop: " + ChatColor.BLUE + currTripleDrop + "%" + 
				(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextTripleDrop + "%" : ""));
		if (!pickaxeData.isMaxFortune()) {
			fortuneIcon.addLore("");
			fortuneIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			fortuneIcon.addLore(ChatColor.GRAY + "your pick fortune to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeFortune() + 1) + ".");
		}
		
		// Set up auto-regeneration icon/button
		Icon regenIcon = new Icon(Material.VINE);
		if (pickaxeData.isMaxAutoregen()) {
			regenIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Auto-regen" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			regenIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Auto-regen" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickAutoRegenSPReq(pickaxeData.getPickaxeAutoregen() + 1) + " SP to level]");
		}
		regenIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeAutoregen());
		regenIcon.addLore("");
		regenIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen rate: " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass().convertLevelToRandomTick(pickaxeData.getPickaxeAutoregen()) + " ticks");
		if (!pickaxeData.isMaxAutoregen()) {
			regenIcon.addLore(ChatColor.BLUE + "" + ChatColor.BOLD + "               -> " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass().convertLevelToRandomTick(pickaxeData.getPickaxeAutoregen() + 1) + " ticks");
			regenIcon.addLore("");
			regenIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			regenIcon.addLore(ChatColor.GRAY + "your pick auto-regen to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeAutoregen() + 1) + ".");
		}
		
		// Set up reinforcement icon/button
		Reinforced reinforced = new Reinforced(YAMLFile.PICKAXECONFIG);
		Icon reinforcedIcon = new Icon(Material.ANVIL);
		if (pickaxeData.isMaxReinforced()) {
			reinforcedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Reinforced" + ChatColor.AQUA + " [MAX LEVEL]");
		} else {
			reinforcedIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Reinforced" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickReinforcedSPReq(pickaxeData.getPickaxeReinforced() + 1) + " SP to level]");
		}
		reinforcedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeReinforced());
		reinforcedIcon.addLore("");
		reinforcedIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Protection chance: " + (pickaxeData.isMaxReinforced() ? ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getPickaxeReinforced()) + "%" : ""));
		if(!pickaxeData.isMaxReinforced()) {
			reinforcedIcon.addLore(ChatColor.BLUE + "         " + reinforced.getTotalPercent(pickaxeData.getPickaxeReinforced()) + "%" + ChatColor.BOLD + " -> " 
					+ ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getPickaxeReinforced() + 1) + "%");
			reinforcedIcon.addLore("");
			reinforcedIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
			reinforcedIcon.addLore(ChatColor.GRAY + "your reinforced to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeReinforced() + 1) + ".");
		}
		
		// Set up fortune icon/button
//		Icon knockbackIcon = new Icon(Material.ARROW);
//		ItemMeta knockbackIconMeta = knockbackIcon.getItemMeta();
//		knockbackIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Knockback");
//		knockbackIcon.setItemMeta(knockbackIconMeta);
		
		// Set up autosmelt icon/button
		Icon autosmeltIcon = new Icon(Material.BLAZE_POWDER);
		if (pickaxeData.getPickaxeAutosmeltUnlocked()) {
			autosmeltIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.AQUA + " [UNLOCKED]");
		} else {
			autosmeltIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickAutosmeltSPReq() + " SP TO UNLOCK]");
		}
		autosmeltIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Toggled: " + ChatColor.WHITE + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + String.valueOf(pickaxeData.getPickaxeAutosmelt()).toUpperCase());
		autosmeltIcon.addLore("");
		if (!pickaxeData.getPickaxeAutosmeltUnlocked()) {
			autosmeltIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to unlock");
			autosmeltIcon.addLore(ChatColor.GRAY + "autosmelt permanently.");
		} else {
			autosmeltIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to toggle");
			autosmeltIcon.addLore(ChatColor.GRAY + "autosmelt on your pickaxe.");
		}
		if (pickaxeData.getPickaxeAutosmelt()) {
			autosmeltIcon.addGlow();
		}
		
		// Set up silktouch icon/button
		Icon silktouchIcon = new Icon(Material.GRASS);
		if (pickaxeData.getPickaxeSilktouchUnlocked()) {
			silktouchIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Silktouch" + ChatColor.AQUA + " [UNLOCKED]");
		} else {
			silktouchIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Silktouch" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickSilktouchSPReq() + " SP TO UNLOCK]");
		}
		silktouchIcon.addLore(ChatColor.GRAY + "" + ChatColor.BOLD + "Toggled: " + ChatColor.WHITE + "" + ChatColor.UNDERLINE + "" + ChatColor.BOLD + String.valueOf(pickaxeData.getPickaxeSilktouch()).toUpperCase());
		silktouchIcon.addLore("");
		if (!pickaxeData.getPickaxeSilktouchUnlocked()) {
			silktouchIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to unlock");
			silktouchIcon.addLore(ChatColor.GRAY + "silktouch permanently.");
		} else {
			silktouchIcon.addLore(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to toggle");
			silktouchIcon.addLore(ChatColor.GRAY + "silktouch on your pickaxe.");
		}
		if (pickaxeData.getPickaxeSilktouch()) {
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
		repairIcon.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Consume Items and Repair Pickaxe");
		repairIcon.addLore(ChatColor.GRAY + "To repair your pickaxe, you need");
		repairIcon.addLore(ChatColor.GRAY + "to place items that are made of");
		repairIcon.addLore(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + 
				ScrapUtil.matToString(ScrapUtil.toolMatToScrapMat(PickaxeFactory.getPickaxeTypeForLevel(pickaxeData.getPickaxeLevel()))) + ChatColor.GRAY + " in this inventory and left");
		repairIcon.addLore(ChatColor.GRAY + "click this button." + ChatColor.RED +  " Must be near an anvil.");
		
		// Set all the inventory slots to their respective icons
		inv.setItem(18, PickaxeFactory.getNewPickaxe(player));
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
