package net.Vala.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.Vala.config.PickaxeData;
import net.Vala.config.PlayerData;
import net.Vala.general.RPGTools;
import net.Vala.mineable.Ore;
import net.Vala.mineable.Ore.Ores;
import net.Vala.raytrace.BoundingBox;
import net.Vala.raytrace.RayTrace;
import net.Vala.tools.RPGPickaxe;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender.Spigot;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BreakingListener implements Listener {

	private static final PotionEffect MINING_POTION_EFFECT = new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 2, true, false);
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent event) throws InstantiationException, IllegalAccessException {
		if (event.isCancelled()) {
			return;
		}
		if (RPGPickaxe.isProfessionPickaxe(event.getPlayer().getItemInHand())) {
			pickaxeMine(event.getPlayer());;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void pickaxeMine(Player player) {
		PlayerData playerData = PlayerData.getData(player);
		PickaxeData pickaxeData = playerData.getPickaxeData();
		
		if (pickaxeData.isBroken()) {
			player.addPotionEffect(MINING_POTION_EFFECT, true); // Stops the cracking
			return;
		}
		
		// Raytrace to find the correct block in their vision
		Block targetBlock = null;
        RayTrace rayTrace = new RayTrace(player.getEyeLocation().toVector(),player.getEyeLocation().getDirection());
        ArrayList<Vector> positions = rayTrace.traverse(4.582,0.01); // 4.582 was the best value I could get for mining range by just testing
        int posSize = positions.size();
        for(int i = 0; i < posSize; i++) {

            Location position = positions.get(i).toLocation(player.getWorld());
            Block block = player.getWorld().getBlockAt(position);
            
            if(block != null && !block.getType().isTransparent() && block.getType().isSolid() && rayTrace.intersects(new BoundingBox(block),10,0.01)){
                targetBlock = block;
                break;
            }

        }
		
        // There is nothing in their vision path
        if (targetBlock == null) {
        	return;
        }
        
        playerData.setDeduct(false); // So we don't use an extra durability when breaking blocks during the test event
		BlockBreakEvent testEvent = new BlockBreakEvent(targetBlock, player);
		Bukkit.getServer().getPluginManager().callEvent(testEvent);
		if (testEvent.isCancelled()) {
			return;
		}
		testEvent.setCancelled(true);
		playerData.setDeduct(true);
        
        net.minecraft.server.v1_12_R1.Block b = CraftMagicNumbers.getBlock(targetBlock);
        IBlockData null1 = null; // Have to initialize these as the nms method is ambiguous
        World null2 = null;
        BlockPosition null3 = null;
        float strength = b.a(null1, null2, null3);
        System.out.println(strength);
        
        // Make sure this block wasn't placed by a player
        boolean placedByPlayer = false;
        List<MetadataValue> metas = targetBlock.getMetadata("placedByPlayer");
        for (MetadataValue m : metas) {
        	if (m.getOwningPlugin() == RPGTools.getPlugin()) {
        		if (m.value() != null) {
        			if ((boolean) m.value()) {
        				placedByPlayer = true;
        			}
        		}
        	}
        }
        
        // If the block isn't an RPGTools specified ore
		Ore ore = Ores.getOreFromMaterial(targetBlock.getType(), targetBlock.getData());
		if (ore == null) {
			return;
		}
		
		// We are now safe to stop vanilla mining and take over with our custom system
		player.addPotionEffect(MINING_POTION_EFFECT, true); 
		
		if (pickaxeData.getLevel() < ore.getMinLevel()) {
			return;
		}
		if (playerData.getTargetBlock() == null || !playerData.getTargetBlock().equals(targetBlock)) {
			// New block, reset block damage
			pickaxeData.setTargetBlock(targetBlock, ore);
			pickaxeData.resetBlockDamage();
		}
		
		// Underwater checks to slow mining speed. Aqua affinity allows mining at normal speed underwater.
		boolean isUnderwater = false;
		boolean hasAquaAffinity = false;
		if (player.getRemainingAir() < player.getMaximumAir()) {
			isUnderwater = true;
		}
		if (player.getInventory().getHelmet() != null) {
			if (player.getInventory().getHelmet().containsEnchantment(Enchantment.WATER_WORKER)) {
				hasAquaAffinity = true;
			}
		}
		pickaxeData.damageBlock(isUnderwater, hasAquaAffinity, placedByPlayer);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (!RPGPickaxe.isProfessionPickaxe(event.getPlayer().getItemInHand())) {
			return;
		}
		
		PlayerData playerData = PlayerData.getData(event.getPlayer());
		if (playerData.getPickaxeData().getCurrentDurability() <= 0) {
			event.getPlayer().sendMessage(ChatColor.RED + "Your pickaxe has reached its breaking point.");
			event.setCancelled(true);
			return;
		}
		
		if (playerData.getDeduct()) {
			playerData.getPickaxeData().modifyCurrentDurability(-1);
		}
		
		playerData.getPickaxeData().updateInInventory();

	}

}
