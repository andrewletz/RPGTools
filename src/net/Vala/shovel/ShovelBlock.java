package net.Vala.shovel;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Mineable;

public class ShovelBlock extends Mineable {
	
	private ShovelBlock(Material material, ItemStack drop, int dropData, int minLevel, double toughness, int minExp, int maxExp, int minVanillaExp, int maxVanillaExp) {
		super(material, drop, dropData, minLevel, toughness, minExp, maxExp, minVanillaExp, maxVanillaExp);
	}

	@Override
	public boolean isMineable(PlayerData playerData) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getDrop() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class ShovelBlocks {
		static List<ShovelBlock> blockList = new ArrayList<ShovelBlock>();
		
		public static void initializeBlocks() {
			clearBlockList();
			for (String key : YAMLFile.SHOVELBLOCKS.getConfig().getKeys(false)) {
				if(Material.getMaterial(key) != null) {
					String material = YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".drop").split(":")[0];
					int data = Integer.parseInt(YAMLFile.SHOVELBLOCKS.getConfig().getString(key + ".drop").split(":")[1]);
					ShovelBlock newBlock = new ShovelBlock(Material.getMaterial(key),
							new ItemStack(Material.getMaterial(material)),
							data,
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minLevel"),
							YAMLFile.SHOVELBLOCKS.getConfig().getDouble(key + ".toughness"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".maxExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".minVanillaExp"),
							YAMLFile.SHOVELBLOCKS.getConfig().getInt(key + ".maxVanillaExp"));
					getBlockList().add(newBlock);
				}
			}
		}
		
		public static List<ShovelBlock> getBlockList() {
			return blockList;
		}
		
		public static void clearBlockList() {
			blockList.clear();
		}
		
		public static ShovelBlock getShovelBlockFromMaterial(Material material) {
			for(ShovelBlock block : blockList) {
				if(block.getMaterial() == material) {
					return block;
				}
			}
			return null;
		}
		
		public static boolean isShovelBlock(ItemStack item) {
			if (item == null) {
				return false;
			}
			for (ShovelBlock block : blockList) {
				if (block.getMaterial() != item.getType()) {
					continue;
				}
				return true;
			}
			return false;
		}
	}

}
