package net.Vala.GUI;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.PickaxeFactory;
import net.Vala.traits.AutoRegen;
import net.Vala.traits.DropChances;
import net.Vala.traits.GeneralTraitUtil;
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
		
		ItemStack pickIcon = PickaxeFactory.getNewPickaxe(player);
		
		ItemStack blankPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		blankPane.setItemMeta(GUI.getBlankStainedBlackGlassPaneMeta());
		
		ItemStack speedIcon = new ItemStack(Material.NETHER_STAR);
		ItemMeta speedIconMeta = speedIcon.getItemMeta();
		List<String> speedIconLore = new ArrayList<String>(2);
		speedIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Mining Speed" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickSpeedSPReq(pickaxeData.getPickaxeSpeed()) + " SP to level]");
		speedIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeSpeed());
		speedIconLore.add("");
		speedIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(pickaxeData.getPickaxeSpeed()) + ChatColor.BOLD + " -> " 
						+ ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(pickaxeData.getPickaxeSpeed() + 1));
		speedIconLore.add("");
		speedIconLore.add(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
		speedIconLore.add(ChatColor.GRAY + "your max pick speed to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeSpeed() + 1) + ".");
		speedIconLore.add("");
		speedIconLore.add(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Shift-click" + ChatColor.GRAY + " here to manually set your");
		speedIconLore.add(ChatColor.GRAY + "pick speed to anything up to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeSpeed()) + ".");
		speedIconLore.add("");
		speedIconLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Manual setting doesn't use SP, it is for");
		speedIconLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "when you want your pick to go slower!");
		speedIconLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "(this option will prompt you in chat)");
		speedIconMeta.setLore(speedIconLore);
		speedIcon.setItemMeta(speedIconMeta);
		
		DropChances currentLevel = new DropChances(pickaxeData.getPickaxeLevel(), YAMLFile.PICKAXECONFIG);
		DropChances nextLevel = new DropChances(pickaxeData.getPickaxeLevel() + 1, YAMLFile.PICKAXECONFIG);
		ItemStack fortuneIcon = new ItemStack(Material.EMERALD);
		ItemMeta fortuneIconMeta = fortuneIcon.getItemMeta();
		List<String> fortuneIconLore = new ArrayList<String>(2);
		fortuneIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Fortune" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickFortuneSPReq(pickaxeData.getPickaxeFortune()) + " SP to level]");
		fortuneIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeFortune());
		fortuneIconLore.add("");
		fortuneIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Double Drop: " + ChatColor.BLUE + currentLevel.getDoubleDropChance() + "%" + ChatColor.BOLD + " -> " + ChatColor.BLUE + nextLevel.getDoubleDropChance() + "%");
		fortuneIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Triple Drop: " + ChatColor.BLUE + currentLevel.getTripleDropChance() + "%" + ChatColor.BOLD + " -> " + ChatColor.BLUE + nextLevel.getTripleDropChance() + "%");
		fortuneIconLore.add("");
		fortuneIconLore.add(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
		fortuneIconLore.add(ChatColor.GRAY + "your pick fortune to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeFortune() + 1) + ".");
		fortuneIconMeta.setLore(fortuneIconLore);
		fortuneIcon.setItemMeta(fortuneIconMeta);
		
		ItemStack regenIcon = new ItemStack(Material.VINE);
		ItemMeta regenIconMeta = regenIcon.getItemMeta();
		List<String> regenIconLore = new ArrayList<String>(2);
		regenIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Auto-regen" + ChatColor.AQUA + " [" + GeneralTraitUtil.getPickAutoRegenSPReq(pickaxeData.getPickaxeAutoregen()) + " SP to level]");
		regenIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Current Level: " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + pickaxeData.getPickaxeAutoregen());
		regenIconLore.add("");
		regenIconLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen rate: " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass().convertLevelToRandomTick(pickaxeData.getPickaxeLevel()) + " ticks");
		regenIconLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "               -> " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass().convertLevelToRandomTick(pickaxeData.getPickaxeLevel() + 1) + " ticks");
		regenIconLore.add("");
		regenIconLore.add(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Left-click" + ChatColor.GRAY + " here to level up");
		regenIconLore.add(ChatColor.GRAY + "your pick auto-regen to level " + ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + (pickaxeData.getPickaxeAutoregen() + 1) + ".");
		regenIconMeta.setLore(regenIconLore);
		regenIcon.setItemMeta(regenIconMeta);
		
		ItemStack reinforcedIcon = new ItemStack(Material.OBSIDIAN);
		ItemMeta reinforcedIconMeta = reinforcedIcon.getItemMeta();
		reinforcedIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Reinforcement");
		reinforcedIcon.setItemMeta(reinforcedIconMeta);
		
		ItemStack knockbackIcon = new ItemStack(Material.ARROW);
		ItemMeta knockbackIconMeta = knockbackIcon.getItemMeta();
		knockbackIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Knockback");
		knockbackIcon.setItemMeta(knockbackIconMeta);
		
		ItemStack autosmeltIcon = new ItemStack(Material.IRON_INGOT);
		ItemMeta autosmeltIconMeta = autosmeltIcon.getItemMeta();
		autosmeltIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.GRAY + " (TOGGLE)");
		autosmeltIcon.setItemMeta(autosmeltIconMeta);
		
		ItemStack silktouchIcon = new ItemStack(Material.GRASS);
		ItemMeta silktouchIconMeta = silktouchIcon.getItemMeta();
		silktouchIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Silktouch" + ChatColor.GRAY + " (TOGGLE)");
		silktouchIcon.setItemMeta(silktouchIconMeta);
		
		inv.setItem(0, pickIcon);
		inv.setItem(2, speedIcon);
		inv.setItem(3, fortuneIcon);
		inv.setItem(4, regenIcon);
		inv.setItem(5, reinforcedIcon);
		inv.setItem(6, knockbackIcon);
		inv.setItem(7, autosmeltIcon);
		inv.setItem(8, silktouchIcon);
		
		inv.setItem(1, blankPane);
		player.openInventory(inv);
	}
	
	public static void openScrapInventory(Player player) {
		Inventory inv = Bukkit.createInventory(player, 27, ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair");
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		
		ItemStack blankPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		blankPane.setItemMeta(GUI.getBlankStainedBlackGlassPaneMeta());
		
		ItemStack repairIcon = new ItemStack(Material.INK_SACK, 1, (byte) 10);
		ItemMeta repairIconMeta = repairIcon.getItemMeta();
		List<String> repairIconLore = new ArrayList<String>(2);
		repairIconMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Consume Items and Repair Pickaxe");
		repairIconLore.add(ChatColor.GRAY + "To repair your pickaxe, you need");
		repairIconLore.add(ChatColor.GRAY + "to place items that are made of");
		repairIconLore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + 
				ScrapUtil.matToString(ScrapUtil.toolMatToScrapMat(PickaxeFactory.getPickaxeTypeForLevel(pickaxeData.getPickaxeLevel()))) + ChatColor.GRAY + " in this inventory and left");
		repairIconLore.add(ChatColor.GRAY + "click this button." + ChatColor.RED +  " Must be near an anvil.");
		repairIconMeta.setLore(repairIconLore);
		repairIcon.setItemMeta(repairIconMeta);
		
		inv.setItem(18, PickaxeFactory.getNewPickaxe(player));
		inv.setItem(19, blankPane);
		inv.setItem(20, blankPane);
		inv.setItem(21, blankPane);
		inv.setItem(22, blankPane);
		inv.setItem(23, blankPane);
		inv.setItem(24, blankPane);
		inv.setItem(25, blankPane);
		inv.setItem(26, repairIcon);
		
		player.openInventory(inv);
	}

}
