package util;

import java.util.HashSet;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
		if (data < 1) {
			data = 1;
		} else if (data > 9) {
			data = 9;
		}
		BlockPosition bp = new BlockPosition(block.getX(), block.getY(), block.getZ());
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0, bp, data);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static String[] getHelpMessage() {
		return new String[] { 
				"",
				ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "RPGTools Commands",
				ChatColor.DARK_PURPLE  + "/rpgtools menu | gui or /rt menu | gui - " + ChatColor.GRAY + "Opens the RPG tools management menu.",
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

}
