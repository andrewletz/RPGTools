package net.Vala.GUI;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.pickaxe.PickaxeFactory;
import net.Vala.traits.GeneralTraitUtil;
import net.Vala.util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

public class LevelUtil {

	public static void levelPickSpeed(Player player) {
		PickaxeData pickData = PlayerData.getData(player).getPickaxeData();
		int spReq = GeneralTraitUtil.getPickSpeedSPReq(pickData.getPickaxeSpeed());
		
		if(pickData.getPickaxeSpeed() <= PickaxeData.getMaxSpeed()) {
			if(pickData.getPickaxeSP() >= spReq) {
				GeneralUtil.playLevelSound(player);
				sendPickaxeLevelUpMessage(player, "speed", pickData.getPickaxeSpeed(), pickData.getPickaxeSpeed() + 1);
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
	
	public static void sendPickaxeLevelUpMessage(Player player, String stat, int before, int after) {
		switch(stat) {
		case "speed":
			player.sendMessage("");
			player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe Speed Level " + after);
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(before) + ChatColor.BOLD + " -> " 
					+ ChatColor.BLUE + PickaxeFactory.convertPickSpeedToDamagePerTick(after));
			player.sendMessage("");
		}
	}
	
}
