package net.Vala.GUI;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PlayerData;
import net.Vala.pickaxe.PickaxeFactory;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI implements Listener {
			
	public static ItemMeta getBlankStainedBlackGlassPaneMeta() {
		ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(ChatColor.GRAY + "");
		return iMeta;
	}
	
	public static void openMain(Player player) {
		PlayerData playerData = PlayerData.getData(player);
		Inventory inv = Bukkit.createInventory(player, 18, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu");
		
		ItemStack pickIcon = new ItemStack(PickaxeFactory.getPickaxeTypeForLevel(playerData.getPickaxeData().getPickaxeLevel()));
		ItemMeta pickIconMeta = pickIcon.getItemMeta();
		pickIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Options");
		List<String> pickIconLore = new ArrayList<String>(2);
		pickIconLore.add(ChatColor.GRAY + "" + "Click here to access");
		pickIconLore.add(ChatColor.GRAY + "" + "your pickaxe options.");
		pickIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pickIconMeta.setLore(pickIconLore);
		pickIcon.setItemMeta(pickIconMeta);
		
		ItemStack pickRepairIcon = new ItemStack(Material.ANVIL);
		ItemMeta pickRepairIconMeta = pickRepairIcon.getItemMeta();
		pickRepairIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair");
		List<String> pickRepairIconLore = new ArrayList<String>(2);
		pickRepairIconLore.add(ChatColor.GRAY + "" + "Click here to repair your pickaxe");
		pickRepairIconLore.add(ChatColor.GRAY + "" + "with its respective material.");
		pickRepairIconLore.add("");
		pickRepairIconLore.add(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		pickRepairIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pickRepairIconMeta.setLore(pickRepairIconLore);
		pickRepairIcon.setItemMeta(pickRepairIconMeta);
		
		ItemStack shovelIcon = new ItemStack(Material.WOOD_SPADE);
		ItemMeta shovelIconMeta = shovelIcon.getItemMeta();
		shovelIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Shovel Options");
		List<String> shovelIconLore = new ArrayList<String>(2);
		shovelIconLore.add(ChatColor.GRAY + "" + "Click here to access");
		shovelIconLore.add(ChatColor.GRAY + "" + "your shovel options.");
		shovelIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		shovelIconMeta.setLore(shovelIconLore);
		shovelIcon.setItemMeta(shovelIconMeta);
		
		ItemStack shovelRepairIcon = new ItemStack(Material.ANVIL);
		ItemMeta shovelRepairIconMeta = shovelRepairIcon.getItemMeta();
		shovelRepairIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Shovel Repair");
		List<String> shovelRepairIconLore = new ArrayList<String>(2);
		shovelRepairIconLore.add(ChatColor.GRAY + "" + "Click here to repair your shovel");
		shovelRepairIconLore.add(ChatColor.GRAY + "" + "with its respective material.");
		shovelRepairIconLore.add("");
		shovelRepairIconLore.add(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		shovelRepairIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		shovelRepairIconMeta.setLore(shovelRepairIconLore);
		shovelRepairIcon.setItemMeta(shovelRepairIconMeta);
		
		ItemStack axeIcon = new ItemStack(Material.WOOD_AXE);
		ItemMeta axeIconMeta = axeIcon.getItemMeta();
		axeIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Axe Options");
		List<String> axeIconLore = new ArrayList<String>(2);
		axeIconLore.add(ChatColor.GRAY + "" + "Click here to access");
		axeIconLore.add(ChatColor.GRAY + "" + "your axe options.");
		axeIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		axeIconMeta.setLore(axeIconLore);
		axeIcon.setItemMeta(axeIconMeta);
		
		ItemStack axeRepairIcon = new ItemStack(Material.ANVIL);
		ItemMeta axeRepairIconMeta = axeRepairIcon.getItemMeta();
		axeRepairIconMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Axe Repair");
		List<String> axeRepairIconLore = new ArrayList<String>(2);
		axeRepairIconLore.add(ChatColor.GRAY + "" + "Click here to repair your axe");
		axeRepairIconLore.add(ChatColor.GRAY + "" + "with its respective material.");
		axeRepairIconLore.add("");
		axeRepairIconLore.add(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		axeRepairIconMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		axeRepairIconMeta.setLore(axeRepairIconLore);
		axeRepairIcon.setItemMeta(axeRepairIconMeta);
		
		inv.setItem(3, pickIcon);
		inv.setItem(4, shovelIcon);
		inv.setItem(5, axeIcon);
		inv.setItem(12, pickRepairIcon);
		inv.setItem(13, shovelRepairIcon);
		inv.setItem(14, axeRepairIcon);
		
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) {
			return;
		}
		if (PickaxeFactory.isProfessionPickaxe(event.getCurrentItem()) && !(event.getInventory() instanceof CraftingInventory)) {
//			event.getWhoClicked().sendMessage(event.getInventory().getType().toString());
			event.setCancelled(true);
		}
		if (event.getInventory().getTitle().contains("Repair") && (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) || event.getCurrentItem().getType().equals(Material.INK_SACK))) {
			event.setCancelled(true);
		}
		if ((event.getInventory().getTitle().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu") || 
				event.getInventory().getTitle().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Management")) && 
				(event.getWhoClicked() instanceof Player)) {
			
			event.setCancelled(true);
			
			Player player = (Player) event.getWhoClicked();
			
			// In main menu
			if (event.getInventory().getTitle().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu")) {
				if (event.getRawSlot() == 3) {
					PickaxeGUI.openManagementInventory(player);
				}
				if (event.getRawSlot() == 12) {
					PickaxeGUI.openScrapInventory(player);
				}
			}
			
			PlayerData playerData = PlayerData.getData(player);
		
		}
		
	}

}
