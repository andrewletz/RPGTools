package net.Vala.pickaxe;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Mineable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Ore extends Mineable{
	
	ItemStack autosmeltDrop;

	protected Ore(Material material, byte blockData, Material drop, byte dropData, Material silkDrop, byte silkDropData, Material autosmeltDrop, byte autosmeltDropData, int minLevel, double toughness, 
			int minExp, int maxExp, int minVanillaExp, int maxVanillaExp) {
		super(material, blockData, drop, dropData, silkDrop, silkDropData, minLevel, toughness, minExp, maxExp, minVanillaExp, maxVanillaExp);
		this.autosmeltDrop = new ItemStack(autosmeltDrop, 1, autosmeltDropData);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isMineable(PlayerData playerData) {
		return playerData.getPickaxeData().getLevel() >= getMinLevel()
				&& PickaxeUtil.isPickaxeMaterial(playerData.getPlayer().getItemInHand().getType());
	}
	
	public ItemStack getAutosmeltDrop() {
		return autosmeltDrop;
	}
	
	public static class Ores {
		static List<Ore> oreList = new ArrayList<Ore>();
		
		public static void initializeOres() {
			clearOreList();
			for (String key : YAMLFile.PICKAXEBLOCKS.getConfig().getKeys(false)) {
				if(Material.getMaterial(YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".bukkitMaterial")) != null) {
					
					// Get drop and drop data
					String[] dropSplit = YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".drop").split(":");
					String drop = dropSplit[0];
					int dropData = Integer.parseInt(dropSplit[1]);
					
					// Get silk drop and silk drop data
					String[] silkDropSplit = YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".silkDrop").split(":");
					String silkDrop = silkDropSplit[0];
					int silkDropData = Integer.parseInt(silkDropSplit[1]);
					
					// Get auto-smelt drop and auto-smelt drop data
					String[] autosmeltDropSplit = YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".autosmeltDrop").split(":");
					String autosmeltDrop = autosmeltDropSplit[0];
					int autosmeltDropData = Integer.parseInt(autosmeltDropSplit[1]);
					
					Ore newOre = new Ore(Material.getMaterial(YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".bukkitMaterial")),
							(byte) YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".blockData"),
							Material.getMaterial(drop),
							(byte) dropData,
							Material.getMaterial(silkDrop),
							(byte) silkDropData,
							Material.getMaterial(autosmeltDrop),
							(byte) autosmeltDropData,
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
		
		public static Ore getOreFromMaterial(Material material, byte data) {
			if (material == Material.GLOWING_REDSTONE_ORE) {
				material = Material.REDSTONE_ORE;
			}
			for(Ore ore : oreList) {
				if(ore.getMaterial() == material && ore.getBlockData() == data) {
					return ore;
				}
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		public static boolean isOre(ItemStack item) {
			if (item == null) {
				return false;
			}
			for (Ore ore : oreList) {
				if (ore.getMaterial() != item.getType() && ore.getBlockData() != item.getData().getData()) {
					continue;
				}
				return true;
			}
			return false;
		}
	}
	
}
