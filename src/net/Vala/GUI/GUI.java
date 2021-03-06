package net.Vala.GUI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.general.Logger;
import net.Vala.repair.Repair;
import net.Vala.repair.Scrap;
import net.Vala.util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;

public class GUI implements Listener {
	
	public static final Icon BLANK_PANE = getBlankPane();
	
	public static Icon getBlankPane() {
		Icon i = new Icon(Material.STAINED_GLASS_PANE, (byte) 15);
		i.setDisplayName(ChatColor.GRAY + "");
		return i;
	}
	
	public static void openMain(Player player) {
		PlayerData playerData = PlayerData.getData(player);
		Inventory inv = Bukkit.createInventory(player, 18, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu");
		
		Icon pickIcon = new Icon(playerData.getPickaxeData().getTool().getType());
		pickIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Options");
		pickIcon.addLore(ChatColor.GRAY + "" + "Click here to access");
		pickIcon.addLore(ChatColor.GRAY + "" + "your pickaxe options.");
		pickIcon.hideItemFlags();
		
		Icon pickRepairIcon = new Icon(Material.ANVIL);
		pickRepairIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair");
		pickRepairIcon.addLore(ChatColor.GRAY + "" + "Click here to repair your pickaxe");
		pickRepairIcon.addLore(ChatColor.GRAY + "" + "with its respective material.");
		pickRepairIcon.addLore("");
		pickRepairIcon.addLore(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		pickRepairIcon.hideItemFlags();
		
		Icon shovelIcon = new Icon(Material.WOOD_SPADE);
		shovelIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Shovel Options");
		shovelIcon.addLore(ChatColor.GRAY + "" + "Click here to access");
		shovelIcon.addLore(ChatColor.GRAY + "" + "your shovel options.");
		shovelIcon.hideItemFlags();
		
		Icon shovelRepairIcon = new Icon(Material.ANVIL);
		shovelRepairIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Shovel Repair");
		shovelRepairIcon.addLore(ChatColor.GRAY + "" + "Click here to repair your shovel");
		shovelRepairIcon.addLore(ChatColor.GRAY + "" + "with its respective material.");
		shovelRepairIcon.addLore("");
		shovelRepairIcon.addLore(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		shovelRepairIcon.hideItemFlags();
		
		Icon axeIcon = new Icon(Material.WOOD_AXE);
		axeIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Axe Options");
		axeIcon.addLore(ChatColor.GRAY + "" + "Click here to access");
		axeIcon.addLore(ChatColor.GRAY + "" + "your axe options.");
		axeIcon.hideItemFlags();
		
		Icon axeRepairIcon = new Icon(Material.ANVIL);
		axeRepairIcon.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Axe Repair");
		axeRepairIcon.addLore(ChatColor.GRAY + "" + "Click here to repair your axe");
		axeRepairIcon.addLore(ChatColor.GRAY + "" + "with its respective material.");
		axeRepairIcon.addLore("");
		axeRepairIcon.addLore(ChatColor.RED + "" + ChatColor.ITALIC + "(must be near an anvil to repair!)");
		axeRepairIcon.hideItemFlags();
		
		inv.setItem(3, pickIcon);
		inv.setItem(4, shovelIcon);
		inv.setItem(5, axeIcon);
		inv.setItem(12, pickRepairIcon);
		inv.setItem(13, shovelRepairIcon);
		inv.setItem(14, axeRepairIcon);
		
		player.openInventory(inv);
	}
	
	private static final Set<String> VALIDTITLES = new HashSet<String>(Arrays.asList(new String[] {
			ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu",
			ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Management",
			ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Shovel Management",
			ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Axe Management",
			ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair",
			ChatColor.BLUE + "" + ChatColor.BOLD + "Shovel Repair",
			ChatColor.BLUE + "" + ChatColor.BOLD + "Axe Repair",
			}));
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) {
			return;
		}
		
		// No hotbar key swapping of RPG picks
		if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
			if (GeneralUtil.isProfessionItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()))) {
				event.setCancelled(true);
			}
		}
		if ((GeneralUtil.isProfessionItem(event.getCurrentItem()) || (GeneralUtil.isProfessionItem(event.getCursor())))) {
			if (!(event.getInventory() instanceof CraftingInventory)) {
				event.setCancelled(true);
			}
			
			if (event.getRawSlot() <= 4) {
				event.setCancelled(true);
			}
		}
		
		String title = event.getInventory().getTitle();
		if (VALIDTITLES.contains(title) && (event.getWhoClicked() instanceof Player)) {
			// Cover general repair menu case where we need to cancel
			if (event.getInventory().getTitle().contains("Repair") && (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) || event.getCurrentItem().getType().equals(Material.INK_SACK))) {
				event.setCancelled(true);
			}
			
			Player player = (Player) event.getWhoClicked();
			
			// In hub menu
			if (title.equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "RPG Tools Management Menu")) {
				event.setCancelled(true);
				if (event.getRawSlot() == 3) {
					PickaxeGUI.openManagementInventory(player);
				}
				if (event.getRawSlot() == 12) {
					PickaxeGUI.openScrapInventory(player);
				}
			}
			
			/*
			 * In pickaxe related menus
			 */
			if (title.contains("Pickaxe")) {
				// We don't need player data until we get to more specific menus
				PlayerData playerData = PlayerData.getData(player);
				PickaxeData pickaxeData = playerData.getPickaxeData();
				ButtonUtil pickaxeButtons = new ButtonUtil(pickaxeData);
				
				// In pickaxe management
				if (title.equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Management")) {
					
					event.setCancelled(true);
					switch (event.getRawSlot()) {
						case 2:
							pickaxeButtons.levelSpeed(player);
							break;
						case 3:
							pickaxeButtons.levelFortune(player);
							break;
						case 4:
							pickaxeButtons.levelAutoregen(player);
							break;
						case 5:
							pickaxeButtons.levelReinforced(player);
							break;
						case 7:
							if (!pickaxeData.getAutosmeltUnlocked()) {
								pickaxeButtons.unlockAutosmelt(player);
							} else {
								if (pickaxeData.getSilktouch()) { break; }
								boolean toggleState = pickaxeData.getAutosmelt();
								pickaxeData.setAutosmelt(!toggleState);
								pickaxeData.updateInInventory();
								PickaxeGUI.openManagementInventory(player);
							}
							break;
						case 8:
							if (!pickaxeData.getSilktouchUnlocked()) {
								pickaxeButtons.unlockSilktouch(player);
							} else {
								if (pickaxeData.getAutosmelt()) { break; }
								boolean toggleState = pickaxeData.getSilktouch();
								pickaxeData.setSilktouch(!toggleState);
								pickaxeData.updateInInventory();
								PickaxeGUI.openManagementInventory(player);
							}
							break;
					
					}
				}
				
				// In pickaxe repair menu
				if(title.equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair")) {
					if (event.getRawSlot() == 26) {
						boolean shouldNeedAnvil = true;
						if (pickaxeData.getTool().getType() == Material.WOOD_PICKAXE || pickaxeData.getTool().getType() == Material.STONE_PICKAXE) {
							shouldNeedAnvil = false;
						}
						pickaxeButtons.repair(player, event.getInventory(), shouldNeedAnvil);
						event.getInventory().setItem(18, pickaxeData.getTool());
						pickaxeData.updateInInventory();
					}
					
				}
				
			}

		
		}
		
	}

}
