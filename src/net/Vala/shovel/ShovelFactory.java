package net.Vala.shovel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.Vala.config.PlayerData;
import net.Vala.config.ShovelData;
import net.Vala.config.YAMLFile;
import net.Vala.general.RPGTools;
import net.Vala.traits.DropChances;
import net.Vala.util.GeneralUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class ShovelFactory {
	
	public static final int MAX_LEVEL = YAMLFile.SHOVELCONFIG.getConfig().getInt("MaxLevel");
	public static final int DIFFICULTY_MULTIPLIER = YAMLFile.SHOVELCONFIG.getConfig().getInt("DifficultyMultiplier");
	private static final Material[] Shovel_MATERIAL = new Material[] { Material.WOOD_SPADE, Material.STONE_SPADE,
		Material.IRON_SPADE, Material.GOLD_SPADE, Material.DIAMOND_SPADE };

	private static final Random RANDOM = new Random();

	private ShovelFactory() {}

	/**
	 * Gets the corresponding Shovel material for the level
	 * @param level
	 * @return the corresponding Shovel material
	 */
	public static Material getShovelTypeForLevel(int level) {
		if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Diamond")) {
			return Material.DIAMOND_SPADE;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Gold")) {
			return Material.GOLD_SPADE;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Iron")) {
			return Material.IRON_SPADE;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Stone")) {
			return Material.STONE_SPADE;
		}
		
		return Material.WOOD_SPADE;
	}

	/**
	 * Generates a new profession Shovel for the player
	 * @param player
	 * @return the generated Shovel
	 */
	public static ItemStack getNewShovel(Player player) {
		
		PlayerData playerData = PlayerData.getData(player);
		int ShovelLevel = playerData.getShovelData().getShovelLevel();
		ItemStack shovel = new ItemStack(getShovelTypeForLevel(ShovelLevel), 1, (short) 0);
		ItemMeta shovelMeta = shovel.getItemMeta();
		shovelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		shovelMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getName() + "'s Shovel");
		shovelMeta.setLore(getShovelLore(playerData));
		shovel.setItemMeta(shovelMeta);
		updateShovelDurability(playerData, shovel);
		return shovel;
	}

	/**
	 * Generates the lore of the profession Shovel for the player
	 * @param playerData
	 * @return the generated lore
	 */
	private static List<String> getShovelLore(PlayerData playerData) {
		ShovelData shovelData = playerData.getShovelData();
		List<String> ShovelLore = new ArrayList<>();
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "LEVEL: " + ChatColor.WHITE + shovelData.getShovelLevel() + ChatColor.AQUA + " [" + shovelData
				.getShovelSP() + " SP available]");
		ShovelLore.add("");
		ShovelLore.add(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "STATS");
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "SPEED: " + ChatColor.WHITE + shovelData.getShovelSpeed());
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "FORTUNE: " + ChatColor.WHITE + shovelData.getShovelFortune());
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "REGEN: " + ChatColor.WHITE + shovelData.getShovelAutoregen());
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "REINFORCE: " + ChatColor.WHITE + shovelData.getShovelReinforced());
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "KNOCKBACK: " + ChatColor.WHITE + shovelData.getShovelKnockback());
		ShovelLore.add(" ");
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "CURRENT DURA.:" + ChatColor.WHITE + " " + shovelData.getShovelCurrentDurability() + " uses " + ChatColor.BLUE + "("
				+ String.format("%.0f",
						100D * ((double) shovelData.getShovelCurrentDurability() / (double) shovelData.getShovelMaxDurability()))
				+ "%)");
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "EXP: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "["
				+ GeneralUtil.formatPercentageBar("||||||||||||||||||||||||||||||||||||||||||||||||||", ChatColor.DARK_PURPLE,
						ChatColor.GRAY, (double) shovelData.getShovelExp() / (double) shovelData.getShovelExpToNextLevel())
				+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "]");
		ShovelLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "        " + ChatColor.UNDERLINE + "(" + shovelData.getShovelExp() + " XP / " + shovelData.getShovelExpToNextLevel() + " XP)");
		return ShovelLore;
	}

	/**
	 * Checks if the player has a Shovel profession Shovel in their inventory
	 * @param player
	 * @return whether or not the Shovel was found
	 */
	public static boolean hasShovelInInventory(Player player) {
		return getShovelInInventory(player) != null;
	}

	/**
	 * Gets the profession Shovel in the player's inventory
	 * @param player
	 * @return the found Shovel or null if not found
	 */
	public static ItemStack getShovelInInventory(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (isProfessionShovel(item)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Updates the Shovel in the player's inventory
	 * @param player
	 */
	public static void updateShovelInInventory(Player player) {
		new BukkitRunnable() {

			@Override
			public void run() {
				ItemStack shovel = getShovelInInventory(player);
				if (shovel == null) {
					return;
				}
				PlayerData playerData = PlayerData.getData(player);
				ItemMeta shovelMeta = shovel.getItemMeta();
				shovelMeta.setLore(getShovelLore(playerData));
				shovel.setItemMeta(shovelMeta);
				shovel.setType(getShovelTypeForLevel(playerData.getShovelData().getShovelLevel()));
				updateShovelDurability(playerData, shovel);
			}
		}.runTaskLater(RPGTools.getPlugin(), 0L);
	}

	/**
	 * Updates the durability of the player's  Assumes Shovel is valid
	 * @param playerData
	 * @param Shovel the valid Shovel
	 */
	private static void updateShovelDurability(PlayerData playerData, ItemStack shovel) {
		float trueDuraPercent = (float) playerData.getShovelData().getShovelCurrentDurability() / (float) playerData.getShovelData().getShovelMaxDurability();
		short maxDura = getMaxDurabilityOfShovelMaterial(shovel.getType());
		shovel.setDurability((short) (maxDura - (trueDuraPercent * (float) maxDura)));
	}

	/**
	 * Gets the corresponding durability of the Shovel material
	 * @param material the Shovel material - assumed to be valid on passing
	 * @return the maximum durability for the corresponding material
	 */
	private static short getMaxDurabilityOfShovelMaterial(Material material) {
		switch (material) {
		case WOOD_SPADE:
			return 59;
		case STONE_SPADE:
			return 131;
		case IRON_SPADE:
			return 250;
		case GOLD_SPADE:
			return 32;
		case DIAMOND_SPADE:
			return 1561;
		default:
			return 1;
		}
	}

	/**
	 * Checks if item is a profession Shovel
	 * @param item
	 * @return whether or not the item is a profession Shovel
	 */
	public static boolean isProfessionShovel(ItemStack item) {
		return item != null && isShovelMaterial(item.getType()) && item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().contains("'s Shovel");
	}

	/**
	 * Checks if the material is a Shovel material
	 * @param material
	 * @return whether or not the material is Shovel material
	 */
	public static boolean isShovelMaterial(Material material) {
		for (Material ShovelMaterial : Shovel_MATERIAL) {
			if (material == ShovelMaterial) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gives a new profession Shovel to the player if there is room in their inventory
	 * @param player
	 * @return whether or not there was room for the Shovel
	 */
	public static boolean giveNewShovel(Player player) {
		return player.getInventory().addItem(getNewShovel(player)).isEmpty();
	}
	
	public static double getFortuneExpMultiplier(int dropAmount) {
		if (dropAmount == 2) {
			return YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.DoubleExpMultiplier");
		} else {
			return YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.TripleExpMultiplier");
		}
	}
	
	/**
	 * Breaks the block as though the player mined it
	 * @param player the player who is breaking
	 * @param block the block to be broken
	 * @param ore the corresponding ore for the block
	 */
	public static void breakBlock(Player player, Block block, ShovelBlock ore) {
		if (ore == null) {
			return;
		}
		PlayerData playerData = PlayerData.getData(player);
		ShovelData shovelData = playerData.getShovelData();
		if (!ore.isMineable(playerData)) {
			return;
		}
		if (shovelData.getShovelCurrentDurability() <= 0) {
			player.sendMessage(ChatColor.RED + "Your Shovel is too worn out to break anything.");
			return;
		}
		// All checks complete, Shovel is safe.
		
		// Drops
		ItemStack drop = ore.getDrop();
		int dropAmount = rollDropAmount(playerData);
		drop.setAmount(dropAmount);
		
		int expAmount = (int) getFortuneExpMultiplier(dropAmount) * ore.getRandomExp();
		shovelData.modifyShovelExp(expAmount, dropAmount);
		shovelData.modifyShovelCurrentDurability(-1);
		
		// This lib is broken rn
//		ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0.1F, 0.1F, 0.1F, 0.1F, 20);
		
		double x, y, z;
		x = block.getLocation().getX();
		y = block.getLocation().getY();
		z = block.getLocation().getZ();
		
		if (dropAmount == 3) {
			// Triple effect
			player.getWorld().spawnParticle(Particle.DRAGON_BREATH,  x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 0.025);
		} else if (dropAmount == 2) {
			// Double effect
			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 0.05);
		}
		
		player.getWorld().spawnParticle(Particle.BLOCK_CRACK, x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 1, new MaterialData(block.getType()));
		player.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1F, 0.8F);
		block.setType(Material.AIR);
		block.getLocation().getWorld().dropItemNaturally(block.getLocation(), drop);
		((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(ore.getRandomVanillaExp());
		
//		playerData.resetBlockDamage();
//		playerData.setTargetBlock(null, null);
		updateShovelInInventory(player);
	}
	
	/**
	 * Converts the Shovel speed level to the speed multiplier for the level
	 * @param level
	 * @return the converted speed
	 */
	
	public static double convertShovelSpeedToDamagePerTick(int level) {
		return YAMLFile.SHOVELCONFIG.getConfig().getDouble("Speed.Base") 
				+ (level * YAMLFile.SHOVELCONFIG.getConfig().getDouble("Speed.Multiplier"));
	}

	/**
	 * Converts the Shovel max durability level to the durability for the level
	 * @param level
	 * @return the converted durability
	 */
	public static int convertShovelLevelToDurability(int level) {
		if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Diamond")) {
			return YAMLFile.SHOVELCONFIG.getConfig().getInt("DiamondDurability");
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Gold")) {
			return YAMLFile.SHOVELCONFIG.getConfig().getInt("GoldDurability");
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Iron")) {
			return YAMLFile.SHOVELCONFIG.getConfig().getInt("IronDurability");
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Stone")) {
			return YAMLFile.SHOVELCONFIG.getConfig().getInt("StoneDurability");
		}
		
		return YAMLFile.SHOVELCONFIG.getConfig().getInt("WoodDurability");
	}

	/**
	 * Converts the Shovel luck level to the luck for the level
	 * @param level
	 * @return the drop chances
	 */
	public static DropChances convertShovelLuckLevelToShovelDropChances(int level) {
		return new DropChances(level, YAMLFile.SHOVELCONFIG);
	}

	/**
	 * Rolls a random drop amount
	 * @param playerData
	 * @return Either 1, 2, or 3 randomly weighted based on the player's drop rates
	 */
	public static int rollDropAmount(PlayerData playerData) {
		DropChances dropChances = playerData.getShovelData().getShovelFortuneDrop();
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
	
	public static int getMaxLevel() {
		return MAX_LEVEL;
	}
	
	public static double getShovelDifficultyMultiplier(int level) {
		if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Diamond")) {
			return 385 * DIFFICULTY_MULTIPLIER;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Gold")) {
			return 213 * DIFFICULTY_MULTIPLIER;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Iron")) {
			return 99 * DIFFICULTY_MULTIPLIER;
		} else if (level >= YAMLFile.SHOVELCONFIG.getConfig().getInt("UnlockLevels.Stone")) {
			return 30 * DIFFICULTY_MULTIPLIER;
		}
		
		return 6.5 * DIFFICULTY_MULTIPLIER;
	}


}
