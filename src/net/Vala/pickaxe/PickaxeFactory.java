package net.Vala.pickaxe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.RPGTools;
import net.Vala.traits.DropChances;
import net.Vala.util.EnchantGlow;
import net.Vala.util.GeneralUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class PickaxeFactory {

	public static final int MAX_LEVEL = YAMLFile.PICKAXECONFIG.getConfig().getInt("MaxLevel");
	public static final int DIFFICULTY_MULTIPLIER = YAMLFile.PICKAXECONFIG.getConfig().getInt("DifficultyMultiplier");
	private static final Material[] PICKAXE_MATERIAL = new Material[] { Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
		Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE };

	private static final Random RANDOM = new Random();

	private PickaxeFactory() {}

	/**
	 * Gets the corresponding pickaxe material for the level
	 * @param level
	 * @return the corresponding pickaxe material
	 */
	public static Material getPickaxeTypeForLevel(int level) {
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
		int pickaxeLevel = playerData.getPickaxeData().getPickaxeLevel();
		ItemStack pickaxe = new ItemStack(getPickaxeTypeForLevel(pickaxeLevel), 1, (short) 0);
		ItemMeta pickaxeMeta = pickaxe.getItemMeta();
		pickaxeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pickaxeMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getName() + "'s Pickaxe");
		pickaxeMeta.setLore(getPickaxeLore(playerData));
		pickaxe.setItemMeta(pickaxeMeta);
		updatePickaxeDurability(playerData, pickaxe);
		if (playerData.getPickaxeData().getPickaxeAutosmelt() || playerData.getPickaxeData().getPickaxeSilktouch()) {
			EnchantGlow.addGlow(pickaxe);
		}
		return pickaxe;
	}

	/**
	 * Generates the lore of the profession pickaxe for the player
	 * @param playerData
	 * @return the generated lore
	 */
	private static List<String> getPickaxeLore(PlayerData playerData) {
		List<String> pickaxeLore = new ArrayList<>();
		PickaxeData pickaxeData = playerData.getPickaxeData();
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "LVL: " + ChatColor.WHITE + pickaxeData.getPickaxeLevel() + ChatColor.AQUA + " [" + pickaxeData
				.getPickaxeSP() + " SP available]");
		pickaxeLore.add("");
		pickaxeLore.add(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "STATS");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.WHITE + pickaxeData.getPickaxeSpeed());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Fortune: " + ChatColor.WHITE + pickaxeData.getPickaxeFortune());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen: " + ChatColor.WHITE + pickaxeData.getPickaxeAutoregen());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Reinforce: " + ChatColor.WHITE + pickaxeData.getPickaxeReinforced());
//		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "KNOCKBACK: " + ChatColor.WHITE + pickaxeData.getPickaxeKnockback());
		if (pickaxeData.getPickaxeAutosmelt()) {
			pickaxeLore.add(" ");
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Autosmelt Active" + ChatColor.BOLD + " ]");
		}
		if (pickaxeData.getPickaxeSilktouch()) {
			if (!pickaxeData.getPickaxeAutosmelt()) {
				pickaxeLore.add(" ");
			}
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Silktouch Active" + ChatColor.BOLD + " ]");
		}
		pickaxeLore.add(" ");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "CURRENT DURA.:" + ChatColor.WHITE + " " + pickaxeData.getPickaxeCurrentDurability() + " uses " + ChatColor.BLUE + "("
				+ String.format("%.0f",
						100D * ((double) pickaxeData.getPickaxeCurrentDurability() / (double) pickaxeData.getPickaxeMaxDurability()))
				+ "%)");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "EXP: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "["
				+ GeneralUtil.formatPercentageBar("||||||||||||||||||||||||||||||||||||||||||||||||||", ChatColor.DARK_PURPLE,
						ChatColor.GRAY, (double) pickaxeData.getPickaxeExp() / (double) pickaxeData.getPickaxeExpToNextLevel())
				+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "]");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "        " + ChatColor.UNDERLINE + "(" + pickaxeData.getPickaxeExp() + " XP / " + pickaxeData.getPickaxeExpToNextLevel() + " XP)");
		return pickaxeLore;
	}

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
	 * Updates the pickaxe in the player's inventory
	 * @param player
	 */
	public static void updatePickaxeInInventory(Player player) {
		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				ItemStack pickaxe = getPickaxeInInventory(player);
				if (pickaxe == null) {
					return;
				}
				PlayerData playerData = PlayerData.getData(player);
				ItemMeta pickaxeMeta = pickaxe.getItemMeta();
				pickaxeMeta.setLore(getPickaxeLore(playerData));
				pickaxe.setItemMeta(pickaxeMeta);
				pickaxe.setType(getPickaxeTypeForLevel(playerData.getPickaxeData().getPickaxeLevel()));
				if(EnchantGlow.isGlowing(pickaxe)) {
					if(!playerData.getPickaxeData().getPickaxeAutosmelt() && !playerData.getPickaxeData().getPickaxeSilktouch()) {
						EnchantGlow.removeGlow(pickaxe);
					}
				}
				if(playerData.getPickaxeData().getPickaxeAutosmelt() || playerData.getPickaxeData().getPickaxeSilktouch()
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
		float trueDuraPercent = (float) playerData.getPickaxeData().getPickaxeCurrentDurability() / (float) playerData.getPickaxeData().getPickaxeMaxDurability();
		short maxDura = getMaxDurabilityOfPickMaterial(pickaxe.getType());
		pickaxe.setDurability((short) (maxDura - (trueDuraPercent * (float) maxDura)));
	}

	/**
	 * Gets the corresponding durability of the pickaxe material
	 * @param material the pickaxe material - assumed to be valid on passing
	 * @return the maximum durability for the corresponding material
	 */
	private static short getMaxDurabilityOfPickMaterial(Material material) {
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
		for (Material pickMaterial : PICKAXE_MATERIAL) {
			if (material == pickMaterial) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gives a new profession pickaxe to the player if there is room in their inventory
	 * @param player
	 * @return whether or not there was room for the pickaxe
	 */
	public static boolean giveNewPickaxe(Player player) {
		return player.getInventory().addItem(getNewPickaxe(player)).isEmpty();
	}
	
	public static double getFortuneExpMultiplier(int dropAmount) {
		if (dropAmount == 2) {
			return YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.DoubleExpMultiplier");
		} else {
			return YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.TripleExpMultiplier");
		}
	}
	
	/**
	 * Breaks the block as though the player mined it
	 * @param player the player who is breaking
	 * @param block the block to be broken
	 * @param ore the corresponding ore for the block
	 */
	public static void breakBlock(Player player, Block block, Ore ore) {
		if (ore == null) {
			return;
		}
		PlayerData playerData = PlayerData.getData(player);
		PickaxeData pickaxeData = playerData.getPickaxeData();
		if (!ore.isMineable(playerData)) {
			return;
		}
		// All checks complete, Pickaxe is safe.
		
		// Drops
		ItemStack drop;
		if(pickaxeData.getPickaxeAutosmelt()) {
			drop = ore.getAutosmeltDrop();
		} else if(pickaxeData.getPickaxeSilktouch()) {
			drop = ore.getSilkDrop();
		} else {
			drop = ore.getDrop();
		}
		int dropAmount = rollDropAmount(playerData);
		drop.setAmount(dropAmount);
		
		int expAmount = (int) getFortuneExpMultiplier(dropAmount) * ore.getRandomExp();
		pickaxeData.modifyPickaxeExp(expAmount, dropAmount);
		if (!pickaxeData.getPickaxeShouldProtect()) {
			pickaxeData.modifyPickaxeCurrentDurability(-1);
		} else {
			player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.06F, 1.7F);
		}
		
		// This lib is broken rn
//		ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0.1F, 0.1F, 0.1F, 0.1F, 20);
		
		double x, y, z;
		x = block.getLocation().getX();
		y = block.getLocation().getY();
		z = block.getLocation().getZ();
		
		if (dropAmount == 3) {
			// Triple effect
			player.getWorld().spawnParticle(Particle.DRAGON_BREATH,  x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
		} else if (dropAmount == 2) {
			// Double effect
			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
		}
		
		if (pickaxeData.getPickaxeAutosmelt()) {
			player.getWorld().spawnParticle(Particle.FLAME,  x + 0.5, y + 0.5, z + 0.5, 4, 0F, 0F, 0F, 0.009);
		}
		
		player.getWorld().spawnParticle(Particle.BLOCK_CRACK, x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 1, new MaterialData(block.getType()));
		player.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1F, 0.8F);
		block.setType(Material.AIR);
		block.getLocation().getWorld().dropItemNaturally(block.getLocation(), drop);
		if(!pickaxeData.getPickaxeSilktouch()) {
			((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(ore.getRandomVanillaExp());
		}
		
//		playerData.resetBlockDamage();
//		playerData.setTargetBlock(null, null);
		updatePickaxeInInventory(player);
	}
	
	/**
	 * Converts the Pickaxe speed level to the speed multiplier for the level
	 * @param level
	 * @return the converted speed
	 */
	
	public static double convertPickSpeedToDamagePerTick(int level) {
		double newDouble = YAMLFile.PICKAXECONFIG.getConfig().getDouble("Speed.Base") 
				+ (level * YAMLFile.PICKAXECONFIG.getConfig().getDouble("Speed.Multiplier"));
		return (double)Math.round(newDouble * 100000d) / 100000d;
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

	/**
	 * Converts the Pickaxe luck level to the luck for the level
	 * @param level
	 * @return the drop chances
	 */
	public static DropChances convertPickaxeLuckLevelToPickaxeDropChances(int level) {
		return new DropChances(level, YAMLFile.PICKAXECONFIG);
	}

	/**
	 * Rolls a random drop amount
	 * @param playerData
	 * @return Either 1, 2, or 3 randomly weighted based on the player's drop rates
	 */
	public static int rollDropAmount(PlayerData playerData) {
		DropChances dropChances = playerData.getPickaxeData().getPickaxeFortuneDrop();
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

}

