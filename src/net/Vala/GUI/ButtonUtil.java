package net.Vala.GUI;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.ToolData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.repair.Repair;
import net.Vala.traits.DropChances;
import net.Vala.traits.TraitUtil;
import net.Vala.traits.Reinforced;
import net.Vala.util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ButtonUtil {
	
	ToolData data;
	TraitUtil util;
	
	public ButtonUtil(ToolData data) {
		this.data = data;
		if (data.getClass().equals(PickaxeData.class)) {
			util = new TraitUtil(YAMLFile.PICKAXESPCOSTS);
		}
	}

	public void levelSpeed(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getSpeedSPReq(pickaxeData.getSpeed()) + 1;
		
		if(pickaxeData.getSpeed() < pickaxeData.getMaxSpeed()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "speed", pickaxeData.getSpeed(), pickaxeData.getSpeed() + 1);
				pickaxeData.modifySpeed(1);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your pick speed is already at the maximum level!");
		}
	}
	
	public void levelFortune(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getFortuneSPReq(pickaxeData.getFortune()) + 1;
		
		if(pickaxeData.getFortune() < pickaxeData.getMaxFortune()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "fortune", pickaxeData.getFortune(), pickaxeData.getFortune() + 1);
				pickaxeData.modifyFortune(1);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your pick's fortune is already at the maximum level!");
		}
	}
	
	public void levelAutoregen(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getAutoRegenSPReq(pickaxeData.getAutoregen()) + 1;
		
		if(pickaxeData.getAutoregen() < pickaxeData.getMaxAutoregen()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "autoregen", pickaxeData.getAutoregen(), pickaxeData.getAutoregen() + 1);
				pickaxeData.modifyAutoregen(1);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your pick's auto-regeneration is already at the maximum level!");
		}
	}
	
	public void levelReinforced(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getReinforcedSPReq(pickaxeData.getReinforced()) + 1;
		
		if(pickaxeData.getReinforced() < pickaxeData.getMaxReinforced()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "reinforced", pickaxeData.getReinforced(), pickaxeData.getReinforced() + 1);
				pickaxeData.modifyReinforced(1);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your pick's reinforcement is already at the maximum level!");
		}
	}
	
	public void unlockAutosmelt(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getAutosmeltSPReq();
		
		if(!pickaxeData.getAutosmeltUnlocked()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "autosmelt", 0, 1);
				pickaxeData.setAutosmeltUnlocked(true);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your already have auto-smelt unlocked for your pick!");
		}
	}
	
	public void unlockSilktouch(Player player) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		int spReq = util.getSilktouchSPReq();
		
		if(!pickaxeData.getSilktouchUnlocked()) {
			if(pickaxeData.getSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "silktouch", 0, 1);
				pickaxeData.setSilktouchUnlocked(true);
				pickaxeData.modifySP(-spReq);
				PickaxeGUI.openManagementInventory(player);
			} else {
				GeneralUtil.playErrorSound(player);
				player.sendMessage(ChatColor.RED + "Insufficient SP! (" + spReq + " needed)");
			}
		} else {
			GeneralUtil.playErrorSound(player);
			player.sendMessage(ChatColor.RED + "Your already have silktouch unlocked for your pick!");
		}
	}
	
	public static void sendLevelUpMessage(Player player, String stat, int before, int after) {
		PickaxeData pickaxeData = PlayerData.getData(player).getPickaxeData();
		player.sendMessage("");
		switch(stat) {
		case "speed":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Speed Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + PickaxeUtil.convertSpeedToReadable(PickaxeUtil.convertPickSpeedToDamagePerTick(before)) + ChatColor.BOLD + " -> " 
					+ ChatColor.BLUE + PickaxeUtil.convertSpeedToReadable(PickaxeUtil.convertPickSpeedToDamagePerTick(after)));
			break;
			
		case "fortune":
			DropChances currentLevel = new DropChances(pickaxeData.getFortune(), YAMLFile.PICKAXECONFIG);
			double currDoubleDrop = (double)Math.round(currentLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
			double currTripleDrop = (double)Math.round(currentLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
			DropChances nextLevel = new DropChances(pickaxeData.getFortune() + 1, YAMLFile.PICKAXECONFIG);
			double nextDoubleDrop = (double)Math.round(nextLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
			double nextTripleDrop = (double)Math.round(nextLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Fortune Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Double Drop: " + ChatColor.BLUE + currDoubleDrop + "%" + 
					(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextDoubleDrop + "%" : ""));
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Triple Drop: " + ChatColor.BLUE + currTripleDrop + "%" + 
					(!pickaxeData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextTripleDrop + "%" : ""));
			break;
			
		case "autoregen":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Autoregen Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen rate: " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass(false).convertLevelToRandomTick(before) + " ticks");
			player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "               -> " + ChatColor.BLUE + "per " + pickaxeData.getAutoRegenClass(false).convertLevelToRandomTick(after) + " ticks");
			break;
			
		case "reinforced":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Reinforced Level " + after);
			Reinforced reinforced = new Reinforced(YAMLFile.PICKAXECONFIG);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Protection chance: " + ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getReinforced()) + "%" + ChatColor.BOLD + " -> " 
						+ ChatColor.BLUE + "" + reinforced.getTotalPercent(pickaxeData.getReinforced() + 1) + "%");
			break;
			
		case "autosmelt":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Autosmelt Unlocked");
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Autosmelt" + ChatColor.BLUE + " is now available to toggle in menu");
			break;
			
		case "silktouch":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Silktouch Unlocked");
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Silktouch" + ChatColor.BLUE + " is now available to toggle in menu");
			break;
		}
		player.sendMessage("");
	}
	
	public void repair(Player player, Inventory inv, boolean shouldNeedAnvil) {
		Repair pickaxeRepair = new Repair(data);
		pickaxeRepair.repair(player, inv, shouldNeedAnvil);
	}
	
}
