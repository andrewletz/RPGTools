package net.Vala.pickaxe;

import java.util.Random;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.Ore.Ores;
import net.Vala.traits.DropChances;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tools.RPGPickaxe;

public class PickaxeUtil {
	
	private static final Random RANDOM = new Random();
	private final static YAMLFile YML = YAMLFile.PICKAXECONFIG;
	
	/**
	 * Checks if the player has a Pickaxe profession pickaxe in their inventory
	 * @param player
	 * @return whether or not the pickaxe was found
	 */
	public static boolean hasPickaxeInInventory(Player player) {
		return getPickaxeInInventory(player) != null;
	}

	/**
	 * Gets the profession pickaxe in the player's inventory
	 * @param player
	 * @return the found pickaxe or null if not found
	 */
	public static ItemStack getPickaxeInInventory(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (isProfessionPickaxe(item)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Checks if item is a profession pickaxe
	 * @param item
	 * @return whether or not the item is a profession pickaxe
	 */
	public static boolean isProfessionPickaxe(ItemStack item) {
		return item != null && isPickaxeMaterial(item.getType()) && item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().contains("'s Pickaxe");
	}

	/**
	 * Checks if the material is a pickaxe material
	 * @param material
	 * @return whether or not the material is pickaxe material
	 */
	public static boolean isPickaxeMaterial(Material material) {
		for (Material pickMaterial : RPGPickaxe.PICKAXE_MATERIAL) {
			if (material == pickMaterial) {
				return true;
			}
		}
		return false;
	}
	
	public static double getFortuneExpMultiplier(int dropAmount) {
		if (dropAmount == 2) {
			return YML.getConfig().getDouble("Fortune.DoubleExpMultiplier");
		} else {
			return YML.getConfig().getDouble("Fortune.TripleExpMultiplier");
		}
	}
	
	/**
	 * Converts the Pickaxe speed level to the speed multiplier for the level
	 * @param level
	 * @return the converted speed
	 */
	
	public static double convertPickSpeedToDamagePerTick(int level) {
		double newDouble =YML.getConfig().getDouble("Speed.Base") 
				+ (level *YML.getConfig().getDouble("Speed.Multiplier"));
		return (double)Math.round(newDouble * 100000d) / 100000d;
	}
	
    public static String convertSpeedToReadable(double damage) {
    	Ore stone = Ores.getOreFromMaterial(Material.STONE, (byte) 0);
    	double toughness = stone.getToughness();
    	double ratio = (toughness / damage) / 20; // time in seconds to break 1 stone
    	double perSecond = (double)Math.round((1 / ratio) * 100000d) / 100000d;
    	return (perSecond + " stone/sec");
    }
    
	/**
	 * Converts the Pickaxe luck level to the luck for the level
	 * @param level
	 * @return the drop chances
	 */
	public static DropChances convertPickaxeLuckLevelToPickaxeDropChances(int level) {
		return new DropChances(level, YML);
	}

	/**
	 * Rolls a random drop amount
	 * @param playerData
	 * @return Either 1, 2, or 3 randomly weighted based on the player's drop rates
	 */
	public static int rollDropAmount(PlayerData playerData) {
		DropChances dropChances = playerData.getPickaxeData().getFortuneDrop();
		float doubleDrop = dropChances.getDoubleDropChance();
		float tripleDrop = doubleDrop + dropChances.getTripleDropChance();
		float roll = RANDOM.nextFloat();
		if (roll < doubleDrop) {
			return 2;
		} else if (roll < tripleDrop) {
			return 3;
		} else {
			return 1;
		}
	}
	
	public static int getMaxSpeed() {
		return YML.getConfig().getInt("Speed.MaxLevel");
	}
	
	public static int getMaxFortune() {
		return YML.getConfig().getInt("Fortune.MaxLevel");
	}
	
	public static int getMaxAutoregen() {
		return YML.getConfig().getInt("Autoregen.MaxLevel");
	}
	
	public static int getMaxReinforced() {
		return YML.getConfig().getInt("Reinforcement.MaxLevel");
	}

}
