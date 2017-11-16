package net.Vala.GUI;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Logger;
import net.Vala.pickaxe.PickaxeFactory;
import net.Vala.traits.DropChances;
import net.Vala.traits.GeneralTraitUtil;
import net.Vala.traits.Reinforced;
import net.Vala.util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

public class PickaxeLevelUtil {

	public static void levelSpeed(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickSpeedSPReq(pickData.getPickaxeSpeed()) + 1;
		
		if(pickData.getPickaxeSpeed() < PickaxeData.getMaxSpeed()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "speed", pickData.getPickaxeSpeed(), pickData.getPickaxeSpeed() + 1);
				pickData.modifyPickaxeSpeed(1);
				pickData.modifyPickaxeSP(-spReq);
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
	
	public static void levelFortune(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickFortuneSPReq(pickData.getPickaxeFortune()) + 1;
		
		if(pickData.getPickaxeFortune() < PickaxeData.getMaxFortune()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "fortune", pickData.getPickaxeFortune(), pickData.getPickaxeFortune() + 1);
				pickData.modifyPickaxeFortune(1);
				pickData.modifyPickaxeSP(-spReq);
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
	
	public static void levelAutoregen(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickAutoRegenSPReq(pickData.getPickaxeAutoregen()) + 1;
		
		if(pickData.getPickaxeAutoregen() < PickaxeData.getMaxAutoregen()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "autoregen", pickData.getPickaxeAutoregen(), pickData.getPickaxeAutoregen() + 1);
				pickData.modifyPickaxeAutoregen(1);
				pickData.modifyPickaxeSP(-spReq);
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
	
	public static void levelReinforced(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickReinforcedSPReq(pickData.getPickaxeReinforced()) + 1;
		
		if(pickData.getPickaxeReinforced() < PickaxeData.getMaxReinforced()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "reinforced", pickData.getPickaxeReinforced(), pickData.getPickaxeReinforced() + 1);
				pickData.modifyPickaxeReinforced(1);
				pickData.modifyPickaxeSP(-spReq);
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
	
	public static void unlockAutosmelt(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickAutosmeltSPReq();
		
		if(!pickData.getPickaxeAutosmeltUnlocked()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "autosmelt", 0, 1);
				pickData.setPickaxeAutosmeltUnlocked(true);
				pickData.modifyPickaxeSP(-spReq);
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
	
	public static void unlockSilktouch(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickSilktouchSPReq();
		
		if(!pickData.getPickaxeSilktouchUnlocked()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendLevelUpMessage(player, "silktouch", 0, 1);
				pickData.setPickaxeSilktouchUnlocked(true);
				pickData.modifyPickaxeSP(-spReq);
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
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		player.sendMessage("");
		switch(stat) {
		case "speed":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Speed Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(before) + ChatColor.BOLD + " -> " 
					+ ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(after));
			break;
			
		case "fortune":
			DropChances currentLevel = new DropChances(pickData.getPickaxeFortune(), YAMLFile.PICKAXECONFIG);
			double currDoubleDrop = (double)Math.round(currentLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
			double currTripleDrop = (double)Math.round(currentLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
			DropChances nextLevel = new DropChances(pickData.getPickaxeFortune() + 1, YAMLFile.PICKAXECONFIG);
			double nextDoubleDrop = (double)Math.round(nextLevel.getDoubleDropChance() * 100 * 1000d) / 1000d;
			double nextTripleDrop = (double)Math.round(nextLevel.getTripleDropChance() * 100 * 1000d) / 1000d;
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Fortune Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Double Drop: " + ChatColor.BLUE + currDoubleDrop + "%" + 
					(!pickData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextDoubleDrop + "%" : ""));
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Triple Drop: " + ChatColor.BLUE + currTripleDrop + "%" + 
					(!pickData.isMaxFortune() ? ChatColor.BOLD + " -> " + ChatColor.BLUE + nextTripleDrop + "%" : ""));
			break;
			
		case "autoregen":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Autoregen Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen rate: " + ChatColor.BLUE + "per " + pickData.getAutoRegenClass().convertLevelToRandomTick(before) + " ticks");
			player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "               -> " + ChatColor.BLUE + "per " + pickData.getAutoRegenClass().convertLevelToRandomTick(after) + " ticks");
			break;
			
		case "reinforced":
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Reinforced Level " + after);
			Reinforced reinforced = new Reinforced(YAMLFile.PICKAXECONFIG);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Protection chance: " + ChatColor.BLUE + "" + reinforced.getTotalPercent(pickData.getPickaxeReinforced()) + "%" + ChatColor.BOLD + " -> " 
						+ ChatColor.BLUE + "" + reinforced.getTotalPercent(pickData.getPickaxeReinforced() + 1) + "%");
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
	
}