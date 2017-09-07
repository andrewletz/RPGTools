package net.Vala.pickaxe;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Mineable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Ore extends Mineable{

	protected Ore(Material material, ItemStack drop, int dropData, int minLevel, double toughness, int minExp, int maxExp, int minVanillaExp, int maxVanillaExp) {
		super(material, drop, dropData, minLevel, toughness, minExp, maxExp, minVanillaExp, maxVanillaExp);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isMineable(PlayerData playerData) {
		return playerData.getPickaxeData().getPickaxeLevel() >= getMinLevel()
				&& PickaxeFactory.isPickaxeMaterial(playerData.getPlayer().getItemInHand().getType());
	}
	
	public static class Ores {
		static List<Ore> oreList = new ArrayList<Ore>();
		
		public static void initializeOres() {
			clearOreList();
			for (String key : YAMLFile.PICKAXEBLOCKS.getConfig().getKeys(false)) {
				if(Material.getMaterial(key) != null) {
					String[] split = YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".drop").split(":");
					String material = split[0];
					int data = Integer.parseInt(split[1]);
					Ore newOre = new Ore(Material.getMaterial(key),
							new ItemStack(Material.getMaterial(material)),
							data,
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".minLevel"),
							YAMLFile.PICKAXEBLOCKS.getConfig().getDouble(key + ".toughness"),
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".minExp"),
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".maxExp"),
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".minVanillaExp"),
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".maxVanillaExp"));
					getOreList().add(newOre);
				}
			}
		}
		
		public static List<Ore> getOreList() {
			return oreList;
		}
		
		public static void clearOreList() {
			oreList.clear();
		}
		
		public static Ore getOreFromMaterial(Material material) {
			if (material == Material.GLOWING_REDSTONE_ORE) {
				material = Material.REDSTONE_ORE;
			}
			for(Ore ore : oreList) {
				if(ore.getMaterial() == material) {
					return ore;
				}
			}
			return null;
		}
		
		public static boolean isOre(ItemStack item) {
			if (item == null) {
				return false;
			}
			for (Ore ore : oreList) {
				if (ore.getMaterial() != item.getType()) {
					continue;
				}
				return true;
			}
			return false;
		}
	}
	
}
