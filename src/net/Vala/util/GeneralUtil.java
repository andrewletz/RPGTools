package net.Vala.util;

import java.util.HashSet;

import net.Vala.pickaxe.Pickaxe;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.shovel.ShovelFactory;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeneralUtil {
	
	/**
	 * Formats the bar to the given specifications
	 * @param cleanBar The bar to be formatted e.g. "|||||||||||||||||||||||||||||||"
	 * @param filledFormat the format for the filled section
	 * @param unfilledFormat the format for the unfilled section
	 * @param percentage the percentage to be filled
	 * @return the formatted bar: <span style="color:#9A41A0">||||||||||||</span><span style="color:#000000">|||||||||||||||||||</span>
	 */
	public static String formatPercentageBar(String cleanBar, String filledFormat, String unfilledFormat, double percentage) {
		StringBuilder barBuilder = new StringBuilder(cleanBar);
		int index = (int) (cleanBar.length() * percentage);
		if (index > barBuilder.length()) {
			index = barBuilder.length();
		}
		barBuilder.insert(index, unfilledFormat);
		return filledFormat + barBuilder.toString();
	}

	/**
	 * Formats the bar to the given specifications
	 * @param cleanBar The bar to be formatted e.g. "|||||||||||||||||||||||||||||||"
	 * @param filledFormat the format for the filled section
	 * @param unfilledFormat the format for the unfilled section
	 * @param percentage the percentage to be filled
	 * @return the formatted bar: <span style="color:#9A41A0">||||||||||||</span><span style="color:#000000">|||||||||||||||||||</span>
	 */
	public static String formatPercentageBar(String cleanBar, ChatColor filledFormat, ChatColor unfilledFormat, double percentage) {
		return formatPercentageBar(cleanBar, filledFormat.toString(), unfilledFormat.toString(), percentage);
	}
	
	
	/**
	 * Sends the block cracking effect to a player
	 * @param block
	 * @param data an int from 1 to 9 that increases the cracking size
	 */
	public static void sendBreakPacket(Player player, Block block, int data) {
		if (data > 9) {
			data = 9;
		}
		BlockPosition bp = new BlockPosition(block.getX(), block.getY(), block.getZ());
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0, bp, data);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static boolean needsAdminMessage(CommandSender sender) {
		if (sender.hasPermission("rpgtools.*") || sender.hasPermission("rpgtools.modifypick") || sender.hasPermission("rpgtools.reload")) {
			return true;
		}
		return false;
	}
	
	public static String[] getAdminHelpMessage() {
		return new String[] { 
				"",
				ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "RPGTools Commands",
				ChatColor.DARK_PURPLE  + "/rpgtools menu | gui or /rt menu | gui - " + ChatColor.GRAY + "Opens the RPG tools management menu.",
				ChatColor.DARK_PURPLE  + "/(pick | shovel | axe) (menu | gui) - " + ChatColor.GRAY + "Easier way to get to specific menus.",
				ChatColor.DARK_PURPLE  + "/pickaxe or /pick - " + ChatColor.GRAY + "Spawns your personal pickaxe.",
				ChatColor.DARK_PURPLE  + "/shovel - " + ChatColor.GRAY + "Spawns your personal shovel.",
				ChatColor.DARK_PURPLE  + "/axe - " + ChatColor.GRAY + "Spawns your personal axe.",
				ChatColor.DARK_PURPLE  + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Admin commands:",
				ChatColor.DARK_PURPLE  + "/rt modifypick [stat] [player] [value] - " + ChatColor.GRAY + "Modify a player's pick manually.",
				ChatColor.DARK_PURPLE  + "/rt reload - " + ChatColor.GRAY + "Reload all RPGtools related files.",
				"",
				};
	}
	
	public static String[] getHelpMessage() {
		return new String[] { 
				"",
				ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "RPGTools Commands",
				ChatColor.DARK_PURPLE  + "/rpgtools menu | gui or /rt menu | gui - " + ChatColor.GRAY + "Opens the RPG tools management menu.",
				ChatColor.DARK_PURPLE  + "/(pick | shovel | axe) (menu | gui) - " + ChatColor.GRAY + "Easier way to get to specific menus.",
				ChatColor.DARK_PURPLE  + "/pickaxe or /pick - " + ChatColor.GRAY + "Spawns your personal pickaxe.",
				ChatColor.DARK_PURPLE  + "/shovel - " + ChatColor.GRAY + "Spawns your personal shovel.",
				ChatColor.DARK_PURPLE  + "/axe - " + ChatColor.GRAY + "Spawns your personal axe.",
				"",
				};
	}
	
    public static HashSet<Material> getTransparentBlocks() { 
        HashSet<Material> trans = new HashSet<Material>();
        for (Material mat : Material.values()) {
        	if (mat.isTransparent()) {
        		trans.add(mat);
        	}
        }
        return trans; 
    } 
    
    public static boolean isProfessionItem(ItemStack item) {
    	return PickaxeUtil.isProfessionPickaxe(item) || ShovelFactory.isProfessionShovel(item);
    }
    
	public static void playErrorSound(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1F, 1F);
	}
	
	public static void playLevelSound(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5F, 10F);
	}

}
