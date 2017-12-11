package net.Vala.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.mineable.Ore;
import net.Vala.mineable.Ore.Ores;
import net.Vala.traits.DropChances;
import net.Vala.util.GeneralUtil;
import net.Vala.config.PickaxeData;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RPGPickaxe extends RPGTool {
	
	public static final Material[] PICKAXE_MATERIAL = new Material[] { Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
		Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE };
	
	static YAMLFile PickaxeYML = YAMLFile.PICKAXECONFIG;
	static Random RANDOM = new Random();
	
	public RPGPickaxe(PickaxeData data) {
		super(data, PickaxeYML);
		this.data = (PickaxeData) data;
	}

	@Override
	protected void initializeMaterials() {
		super.MATERIAL = PICKAXE_MATERIAL;
	}

	@Override
	protected short getMaxDurabilityOfMaterial(Material material) {
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
	@Override
	protected String getDisplayName() {
		return ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + data.getPlayer().getName() + "'s Pickaxe";
	}

	@Override
	protected List<String> getLore() {
		List<String> pickaxeLore = new ArrayList<>();
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "LVL: " + ChatColor.WHITE + data.getLevel() + ChatColor.AQUA + " [" + data.getSP() + " SP available]");
		pickaxeLore.add("");
		pickaxeLore.add(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "STATS");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.WHITE + data.getSpeed());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Fortune: " + ChatColor.WHITE + data.getFortune());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen: " + ChatColor.WHITE + data.getAutoregen());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Reinforce: " + ChatColor.WHITE + data.getReinforced());
//		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "KNOCKBACK: " + ChatColor.WHITE + data.getKnockback());
		if (((PickaxeData) data).getAutosmelt()) {
			pickaxeLore.add(" ");
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Autosmelt Active" + ChatColor.BOLD + " ]");
		}
		if (((PickaxeData) data).getSilktouch()) {
			if (!((PickaxeData) data).getAutosmelt()) {
				pickaxeLore.add(" ");
			}
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Silktouch Active" + ChatColor.BOLD + " ]");
		}
		pickaxeLore.add(" ");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "CURRENT DURA.:" + ChatColor.WHITE + " " + data.getCurrentDurability() + " uses " + ChatColor.BLUE + "("
				+ String.format("%.0f",
						100D * ((double) data.getCurrentDurability() / (double) data.getMaxDurability()))
				+ "%)");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "EXP: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "["
				+ GeneralUtil.formatPercentageBar("||||||||||||||||||||||||||||||||||||||||||||||||||", ChatColor.DARK_PURPLE,
						ChatColor.GRAY, (double) data.getExp() / (double) data.getExpToNextLevel())
				+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "]");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "        " + ChatColor.UNDERLINE + "(" + data.getExp() + " XP / " + data.getExpToNextLevel() + " XP)");
		return pickaxeLore;
	}

	@Override
	protected boolean shouldGlow() {
		if(!((PickaxeData) data).getAutosmelt() && !((PickaxeData) data).getSilktouch()) {
			return false;
		}
		return true;
	}
	
	/*
	 * Static methods for all RPG picks
	 */
	
	public static boolean isPickaxeMaterial(Material material) {
		for (Material pickMaterial : PICKAXE_MATERIAL) {
			if (material == pickMaterial) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isProfessionPickaxe(ItemStack item) {
		return item != null && isPickaxeMaterial(item.getType()) && item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().contains("'s Pickaxe");
	}
	

	public static double getFortuneExpMultiplier(int dropAmount) {
		if (dropAmount == 2) {
			return PickaxeYML.getConfig().getDouble("Fortune.DoubleExpMultiplier");
		} else {
			return PickaxeYML.getConfig().getDouble("Fortune.TripleExpMultiplier");
		}
	}

	public static double convertPickSpeedToDamagePerTick(int level) {
		double newDouble = PickaxeYML.getConfig().getDouble("Speed.Base") 
				+ (level * PickaxeYML.getConfig().getDouble("Speed.Multiplier"));
		return (double)Math.round(newDouble * 100000d) / 100000d;
	}
	
    public static String convertSpeedToReadable(double damage) {
    	Ore stone = Ores.getOreFromMaterial(Material.STONE, (byte) 0);
    	double toughness = stone.getToughness();
    	double ratio = (toughness / damage) / 20; // time in seconds to break 1 stone
    	double perSecond = (double)Math.round((1 / ratio) * 100000d) / 100000d;
    	return (perSecond + " stone/sec");
    }
    
	public static DropChances convertPickaxeLuckLevelToPickaxeDropChances(int level) {
		return new DropChances(level, PickaxeYML);
	}
	
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
	
}
