package net.Vala.mineable;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.tools.RPGPickaxe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Ore extends Mineable{
	
	ItemStack autosmeltDrop;

	protected Ore(Material material, byte blockData, Material drop, int minDropAmount, int maxDropAmount, byte dropData, Material silkDrop, byte silkDropData, Material autosmeltDrop, byte autosmeltDropData, int minLevel, double toughness, 
			int minExp, int maxExp, int minVanillaExp, int maxVanillaExp, boolean fortuneDisabled) {
		super(material, blockData, drop, minDropAmount, maxDropAmount, dropData, silkDrop, silkDropData, minLevel, toughness, minExp, maxExp, minVanillaExp, maxVanillaExp, fortuneDisabled);
		this.autosmeltDrop = new ItemStack(autosmeltDrop, 1, autosmeltDropData);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isMineable(PlayerData playerData) {
		return playerData.getPickaxeData().getLevel() >= getMinLevel()
				&& RPGPickaxe.isPickaxeMaterial(playerData.getPlayer().getItemInHand().getType());
	}
	
	public ItemStack getAutosmeltDrop() {
		autosmeltDrop.setAmount((RANDOM.nextInt((maxDropAmount - minDropAmount) + 1) + minDropAmount));
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
					
					// Get drop amount
					int minDrop;
					int maxDrop;
					try {
						String[] dropAmnt = YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".dropAmount").split("-");
						minDrop = Integer.parseInt(dropAmnt[0]);
						maxDrop = Integer.parseInt(dropAmnt[1]);
					} catch (NullPointerException e) {
						minDrop = 1;
						maxDrop = 1;
					}	
					
					boolean fortuneDisabled;
					try {
						fortuneDisabled = YAMLFile.PICKAXEBLOCKS.getConfig().getBoolean(key + ".fortuneDisabled");
					} catch (NullPointerException e) {
						fortuneDisabled = false;
					}	
					
					Ore newOre = new Ore(Material.getMaterial(YAMLFile.PICKAXEBLOCKS.getConfig().getString(key + ".bukkitMaterial")),
							(byte) YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".blockData"),
							Material.getMaterial(drop),
							minDrop,
							maxDrop,
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
							YAMLFile.PICKAXEBLOCKS.getConfig().getInt(key + ".maxVanillaExp"),
							fortuneDisabled);
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
