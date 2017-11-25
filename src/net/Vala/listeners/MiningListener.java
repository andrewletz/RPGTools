package net.Vala.listeners;

import java.util.ArrayList;

import net.Vala.config.PlayerData;
import net.Vala.pickaxe.Ore;
import net.Vala.pickaxe.Ore.Ores;
import net.Vala.pickaxe.Pickaxe;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.raytrace.BoundingBox;
import net.Vala.raytrace.RayTrace;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class MiningListener implements Listener {

	private static final PotionEffect MINING_POTION_EFFECT = new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 2, true, false);
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent event) throws InstantiationException, IllegalAccessException {
		if (!PickaxeUtil.isProfessionPickaxe(event.getPlayer().getItemInHand())) {
			return;
		}
		Player player = event.getPlayer();
		PlayerData playerData = PlayerData.getData(player);
		
		if (playerData.getPickaxeData().isBroken()) {
			return;
		}
		
		Block targetBlock = null;
        RayTrace rayTrace = new RayTrace(player.getEyeLocation().toVector(),player.getEyeLocation().getDirection());
        ArrayList<Vector> positions = rayTrace.traverse(4.582,0.01);
        for(int i = 0; i < positions.size();i++) {

            Location position = positions.get(i).toLocation(player.getWorld());
            Block block = player.getWorld().getBlockAt(position);
            
            if(block != null && !block.getType().isTransparent() && block.getType().isSolid() && rayTrace.intersects(new BoundingBox(block),10,0.01)){
                targetBlock = block;
                break;
            }

        }
		
        if (targetBlock == null) {
        	return;
        }
		Ore ore = Ores.getOreFromMaterial(targetBlock.getType(), targetBlock.getData());
		if (ore == null) {
			return;
		}
		
		player.addPotionEffect(MINING_POTION_EFFECT, true); // Stops the cracking
		
		if (playerData.getPickaxeData().getPickaxeLevel() < ore.getMinLevel()) {
			return;
		}
		if (playerData.getTargetBlock() == null || !playerData.getTargetBlock().equals(targetBlock)) {
			// New block, reset block damage
			playerData.getPickaxeData().setTargetBlock(targetBlock, ore);
			playerData.getPickaxeData().resetBlockDamage();
		}
		playerData.getPickaxeData().damageBlock();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!PickaxeUtil.isProfessionPickaxe(event.getPlayer().getItemInHand())) {
			return;
		}
		
		PlayerData playerData = PlayerData.getData(event.getPlayer());
		if (playerData.getPickaxeData().getPickaxeCurrentDurability() <= 0) {
			event.getPlayer().sendMessage(ChatColor.RED + "Your pickaxe has reached its breaking point.");
			event.setCancelled(true);
			return;
		}
		playerData.getPickaxeData().modifyPickaxeCurrentDurability(-1);
		
		Pickaxe.updatePickaxeInInventory(event.getPlayer());

	}

}
