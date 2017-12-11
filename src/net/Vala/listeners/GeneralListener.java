package net.Vala.listeners;

import java.util.UUID;

import net.Vala.config.PlayerData;
import net.Vala.general.RPGTools;
import net.Vala.tools.RPGPickaxe;
import net.Vala.util.GeneralUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class GeneralListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		try {
			PlayerData playerData = PlayerData.getData(player);
			playerData.refreshConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		new BukkitRunnable() {
			@Override
			public void run() {
				PlayerData.CONNECTED_PLAYERS.remove(playerUUID);
			}
		}.runTaskLater(RPGTools.getPlugin(), 1L);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// Track metadata so they can't place and destroy blocks over and over
		// Not a fullproof solution, they can still place blocks and on server restart they
		// will get full exp and drops again. Look at mcMMO's solution for more elegance.
		Block block = event.getBlock();
		block.setMetadata("placedByPlayer", new FixedMetadataValue(RPGTools.getPlugin(), true));
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (event.getItemDrop().getItemStack().hasItemMeta()) {
			ItemStack item = event.getItemDrop().getItemStack();
			if (RPGPickaxe.isProfessionPickaxe(item)) {
					event.setCancelled(true);
					item.setAmount(0);
					player.getWorld().spawnParticle(Particle.FALLING_DUST, player.getLocation(), 25, 0.5F, 1F, 0.5F, 0.05);
					player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.35F, 1F);
					player.sendMessage(ChatColor.RED + "Your pickaxe vanished into the void." + ChatColor.ITALIC + " (/pick to respawn)");
					return;
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getName().equals(ChatColor.BLUE + "" + ChatColor.BOLD + "Pickaxe Repair")) {
			for (int i = 0; i <= 17; i++) {
				ItemStack item = event.getInventory().getItem(i);
				if (item != null) {
					event.getPlayer().getLocation().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		try {
			for (ItemStack i : event.getDrops()) {
				if (RPGPickaxe.isProfessionPickaxe(i)) {
					event.getDrops().remove(i);
				}
			}
		} catch (Exception e) {}
	}
	
	// Don't allow players to repair default pickaxes with RPG pickaxes
	@EventHandler
	public void onPlayerCraft(PrepareItemCraftEvent event) {
        if (event.isRepair()) {
            for (ItemStack item : event.getInventory()) {
            	if (item != null && item.getType() != Material.AIR) {
            		if (GeneralUtil.isProfessionItem(item)) {
            			event.getInventory().setResult(null);
            		}
            	}
            }
        }
	}
}
