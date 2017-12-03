package net.Vala.pickaxe;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.RPGTools;
import net.Vala.util.EnchantGlow;
import net.Vala.util.GeneralUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PickaxeOld {

	public static final int MAX_LEVEL = YAMLFile.PICKAXECONFIG.getConfig().getInt("MaxLevel");
	public static final int DIFFICULTY_MULTIPLIER = YAMLFile.PICKAXECONFIG.getConfig().getInt("DifficultyMultiplier");
	public static final Material[] PICKAXE_MATERIAL = new Material[] { Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
		Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE };

	private PickaxeOld() {}

	/**
	 * Gets the corresponding pickaxe material for the level
	 * @param level
	 * @return the corresponding pickaxe material
	 */
	public static Material getTypeForLevel(int level) {
		if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Diamond")) {
			return Material.DIAMOND_PICKAXE;
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Gold")) {
			return Material.GOLD_PICKAXE;
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Iron")) {
			return Material.IRON_PICKAXE;
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Stone")) {
			return Material.STONE_PICKAXE;
		}
		
		return Material.WOOD_PICKAXE;
	}

	/**
	 * Generates a new profession pickaxe for the player
	 * @param player
	 * @return the generated pickaxe
	 */
	public static ItemStack getNewPickaxe(Player player) {
		PlayerData playerData = PlayerData.getData(player);
		int pickaxeLevel = playerData.getPickaxeData().getLevel();
		ItemStack pickaxe = new ItemStack(getTypeForLevel(pickaxeLevel), 1, (short) 0);
		ItemMeta pickaxeMeta = pickaxe.getItemMeta();
		pickaxeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pickaxeMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getName() + "'s Pickaxe");
		pickaxeMeta.setLore(getLore(playerData));
		pickaxe.setItemMeta(pickaxeMeta);
		updatePickaxeDurability(playerData, pickaxe);
		if (playerData.getPickaxeData().getAutosmelt() || playerData.getPickaxeData().getSilktouch()) {
			EnchantGlow.addGlow(pickaxe);
		}
		return pickaxe;
	}

	/**
	 * Generates the lore of the profession pickaxe for the player
	 * @param playerData
	 * @return the generated lore
	 */
	private static List<String> getLore(PlayerData playerData) {
		List<String> pickaxeLore = new ArrayList<>();
		PickaxeData pickaxeData = playerData.getPickaxeData();
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "LVL: " + ChatColor.WHITE + pickaxeData.getLevel() + ChatColor.AQUA + " [" + pickaxeData
				.getSP() + " SP available]");
		pickaxeLore.add("");
		pickaxeLore.add(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "STATS");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.WHITE + pickaxeData.getSpeed());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Fortune: " + ChatColor.WHITE + pickaxeData.getFortune());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen: " + ChatColor.WHITE + pickaxeData.getAutoregen());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Reinforce: " + ChatColor.WHITE + pickaxeData.getReinforced());
//		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "KNOCKBACK: " + ChatColor.WHITE + pickaxeData.getKnockback());
		if (pickaxeData.getAutosmelt()) {
			pickaxeLore.add(" ");
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Autosmelt Active" + ChatColor.BOLD + " ]");
		}
		if (pickaxeData.getSilktouch()) {
			if (!pickaxeData.getAutosmelt()) {
				pickaxeLore.add(" ");
			}
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Silktouch Active" + ChatColor.BOLD + " ]");
		}
		pickaxeLore.add(" ");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "CURRENT DURA.:" + ChatColor.WHITE + " " + pickaxeData.getCurrentDurability() + " uses " + ChatColor.BLUE + "("
				+ String.format("%.0f",
						100D * ((double) pickaxeData.getCurrentDurability() / (double) pickaxeData.getMaxDurability()))
				+ "%)");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "EXP: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "["
				+ GeneralUtil.formatPercentageBar("||||||||||||||||||||||||||||||||||||||||||||||||||", ChatColor.DARK_PURPLE,
						ChatColor.GRAY, (double) pickaxeData.getExp() / (double) pickaxeData.getExpToNextLevel())
				+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "]");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "        " + ChatColor.UNDERLINE + "(" + pickaxeData.getExp() + " XP / " + pickaxeData.getExpToNextLevel() + " XP)");
		return pickaxeLore;
	}

	/**
	 * Updates the pickaxe in the player's inventory
	 * @param player
	 */
	public static void updatePickaxeInInventory(Player player) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ItemStack pickaxe = PickaxeUtil.getPickaxeInInventory(player);
				if (pickaxe == null) {
					return;
				}
				PlayerData playerData = PlayerData.getData(player);
				ItemMeta pickaxeMeta = pickaxe.getItemMeta();
				pickaxeMeta.setLore(getLore(playerData));
				pickaxe.setItemMeta(pickaxeMeta);
				pickaxe.setType(getTypeForLevel(playerData.getPickaxeData().getLevel()));
				if(EnchantGlow.isGlowing(pickaxe)) {
					if(!playerData.getPickaxeData().getAutosmelt() && !playerData.getPickaxeData().getSilktouch()) {
						EnchantGlow.removeGlow(pickaxe);
					}
				}
				if(playerData.getPickaxeData().getAutosmelt() || playerData.getPickaxeData().getSilktouch()
						&& !EnchantGlow.isGlowing(pickaxe)) {
					EnchantGlow.addGlow(pickaxe);
				}
				updatePickaxeDurability(playerData, pickaxe);
			}
		}.runTaskLater(RPGTools.getPlugin(), 0L);
	}

	/**
	 * Updates the durability of the player's pickaxe. Assumes pickaxe is valid
	 * @param playerData
	 * @param pickaxe the valid pickaxe
	 */
	private static void updatePickaxeDurability(PlayerData playerData, ItemStack pickaxe) {
		float trueDuraPercent = (float) playerData.getPickaxeData().getCurrentDurability() / (float) playerData.getPickaxeData().getMaxDurability();
		short maxDura = getMaxDurabilityOfPickaxeMaterial(pickaxe.getType());
		pickaxe.setDurability((short) (maxDura - (trueDuraPercent * (float) maxDura)));
	}

	/**
	 * Gets the corresponding durability of the pickaxe material
	 * @param material the pickaxe material - assumed to be valid on passing
	 * @return the maximum durability for the corresponding material
	 */
	private static short getMaxDurabilityOfPickaxeMaterial(Material material) {
		switch (material) {
		case WOOD_PICKAXE:
			return 59;
		case STONE_PICKAXE:
			return 131;
		case IRON_PICKAXE:
			return 250;
		case GOLD_PICKAXE:
			return 32;
		case DIAMOND_PICKAXE:
			return 1561;
		default:
			return 1;
		}
	}

	/**
	 * Gives a new profession pickaxe to the player if there is room in their inventory
	 * @param player
	 * @return whether or not there was room for the pickaxe
	 */
	public static boolean giveNewPickaxe(Player player) {
		return player.getInventory().addItem(getNewPickaxe(player)).isEmpty();
	}

	/**
	 * Converts the Pickaxe max durability level to the durability for the level
	 * @param level
	 * @return the converted durability
	 */
	public static int convertPickLevelToDurability(int level) {
		if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Diamond")) {
			return YAMLFile.PICKAXECONFIG.getConfig().getInt("DiamondDurability");
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Gold")) {
			return YAMLFile.PICKAXECONFIG.getConfig().getInt("GoldDurability");
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Iron")) {
			return YAMLFile.PICKAXECONFIG.getConfig().getInt("IronDurability");
		} else if (level >= YAMLFile.PICKAXECONFIG.getConfig().getInt("UnlockLevels.Stone")) {
			return YAMLFile.PICKAXECONFIG.getConfig().getInt("StoneDurability");
		}
		
		return YAMLFile.PICKAXECONFIG.getConfig().getInt("WoodDurability");
	}

}

