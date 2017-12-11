package net.Vala.mineable;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ShovelBlock extends Mineable{

	protected ShovelBlock(Material material, byte blockData, Material drop, int minDropAmount, int maxDropAmount, byte dropData, Material silkDrop, byte silkDropData, int minLevel, double toughness, 
			int minExp, int maxExp, int minVanillaExp, int maxVanillaExp, boolean fortuneDisabled) {
		super(material, blockData, drop, minDropAmount, maxDropAmount, dropData, silkDrop, silkDropData, minLevel, toughness, minExp, maxExp, minVanillaExp, maxVanillaExp, fortuneDisabled);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isMineable(PlayerData playerData) {
//		return playerData.getShovelData().getShovelLevel() >= getMinLevel()
//				&& ShovelFactory.isShovelMaterial(playerData.getPlayer().getItemInHand().getType());
		return true;
	}
	
	public static class ShovelBlocks {
		static List<ShovelBlock> shovelBlockList = new ArrayList<ShovelBlock>();
		
		public static void initializeShovelBlocks() {
			clearShovelBlockList();
			for (String key : YAMLFile.SHOVELBLOCKS.getConfig().getKeys(false)) {
				if(Material.getMaterial(key) != null) {
					
					// Get drop and drop data
					String[] dropSplit = YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".drop").split(":");
					String drop = dropSplit[0];
					int dropData = Integer.parseInt(dropSplit[1]);
					
					// Get silk drop and silk drop data
					String[] silkDropSplit = YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".silkDrop").split(":");
					String silkDrop = silkDropSplit[0];
					int silkDropData = Integer.parseInt(silkDropSplit[1]);
					
					// Get drop amount
					int minDrop;
					int maxDrop;
					try {
						String[] dropAmnt = YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".dropAmount").split("-");
						minDrop = Integer.parseInt(dropAmnt[0]);
						maxDrop = Integer.parseInt(dropAmnt[1]);
					} catch (NullPointerException e) {
						minDrop = 1;
						maxDrop = 1;
					}	
					
					boolean fortuneDisabled;
					try {
						fortuneDisabled = YAMLFile.SHOVELBLOCKS.getConfig().getBoolean(key + ".fortuneDisabled");
					} catch (NullPointerException e) {
						fortuneDisabled = false;
					}
					
					ShovelBlock newShovelBlock = new ShovelBlock(Material.getMaterial(YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".bukkitMaterial")),
							(byte) YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".blockData"),
							Material.getMaterial(drop),
							minDrop,
							maxDrop,
							(byte) dropData,
							Material.getMaterial(silkDrop),
							(byte) silkDropData,
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minLevel"),
							YAMLFile.SHOVELBLOCKS.getConfig().getDouble(key + ".toughness"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".maxExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minVanillaExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".maxVanillaExp"),
							fortuneDisabled);
					getShovelBlockList().add(newShovelBlock);
				}
			}
		}
		
		public static List<ShovelBlock> getShovelBlockList() {
			return shovelBlockList;
		}
		
		public static void clearShovelBlockList() {
			shovelBlockList.clear();
		}
		
		public static ShovelBlock getShovelBlockFromMaterial(Material material, byte data) {
			if (material == Material.GLOWING_REDSTONE_ORE) {
				material = Material.REDSTONE_ORE;
			}
			for(ShovelBlock shovelBlock : shovelBlockList) {
				if(shovelBlock.getMaterial() == material && shovelBlock.getBlockData() == data) {
					return shovelBlock;
				}
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		public static boolean isShovelBlock(ItemStack item) {
			if (item == null) {
				return false;
			}
			for (ShovelBlock shovelBlock : shovelBlockList) {
				if (shovelBlock.getMaterial() != item.getType() && shovelBlock.getBlockData() != item.getData().getData()) {
					continue;
				}
				return true;
			}
			return false;
		}
	}
	
}