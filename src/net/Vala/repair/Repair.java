package net.Vala.repair;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.Vala.config.ToolData;
import net.Vala.util.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

public class Repair {
	
	ToolData data;
	
	public Repair(ToolData data) {
		this.data = data;
	}
	
	private boolean playerIsInAnvilRadius() {
		Player player = data.getPlayer();
		Location loc = player.getLocation();
		int radius = 5;
        for(int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for(int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                for(int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                   if(loc.getWorld().getBlockAt(x, y, z).getType() == Material.ANVIL) {
                	   return true;
                   }
                }
            }
        }
        return false;
	}
	
	private void clearItems(Inventory inv) {
		for (int i = 0; i <= 17; i++) {
			inv.setItem(i, new ItemStack(Material.AIR));
		}
	}
	
	private int convertItemsToRepairValue(Inventory inv, int neededValue) {
		int repairValue = 0;
		ItemStack currentItem;
		for (int i = 0; i <= 17; i++) {
			currentItem = inv.getItem(i);
			if (currentItem != null && currentItem.getType() != Material.AIR) {
				int itemAmount = currentItem.getAmount();
				if (itemAmount > 0) {
					for (int j = 0; j < itemAmount; j++) {
						int newValue = (int) (Scrap.getMaterialScrapValue(currentItem.getType(), Scrap.matToString(Scrap.toolMatToScrapMat(data.getTool().getType()))));
						if (newValue > 0) {
							repairValue += newValue;
							currentItem.setAmount(currentItem.getAmount() - 1);
							if (repairValue >= neededValue) {
								return repairValue - (repairValue - neededValue);
							}
						}
					}
				}	
			}
		}
		return repairValue;
	}
	
	public void repair(Player player, Inventory inv) {
		if (data.getCurrentDurability() >= data.getMaxDurability()) {
			player.sendMessage(ChatColor.RED + "Tool is already max durability!");
			GeneralUtil.playErrorSound(player);
			return;
		}
		if (!playerIsInAnvilRadius()) {
			player.sendMessage(ChatColor.RED + "Not in radius of anvil!");
			GeneralUtil.playErrorSound(player);
			return;
		}
		int neededValue = data.getMaxDurability() - data.getCurrentDurability();
		int repairValue = convertItemsToRepairValue(inv, neededValue);
		if (repairValue != 0) {
			data.modifyCurrentDurability(repairValue);
			player.sendMessage(ChatColor.GREEN + "Repaired tool for " + repairValue + " durability.");
			GeneralUtil.playLevelSound(player);
			return;
		} else {
			player.sendMessage(ChatColor.RED + "You can't repair your tool with those items.");
			GeneralUtil.playErrorSound(player);
			return;
		}
	}
		
}
