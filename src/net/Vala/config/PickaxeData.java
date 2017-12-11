package net.Vala.config;

import net.Vala.mineable.Mineable;
import net.Vala.mineable.Ore;
import net.Vala.tools.RPGPickaxe;
import net.Vala.traits.DropChances;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class PickaxeData extends ToolData {

	public PickaxeData(PlayerData playerData) {
		super(playerData, YAMLFile.PICKAXECONFIG);
		super.tool = new RPGPickaxe(this);
	}

	@Override
	protected void refreshToolSpecific() {
		this.refreshAutosmelt();
		this.refreshSilktouch();
		this.refreshAutosmeltUnlocked();
		this.refreshSilktouchUnlocked();
	}

	@Override
	public double getTotalSpeed() {
		return RPGPickaxe.convertPickSpeedToDamagePerTick(getSpeed());
	}

	@Override
	public DropChances getFortuneDrop() {
		return RPGPickaxe.convertPickaxeLuckLevelToPickaxeDropChances(getFortune());
	}
	
	@Override
	protected void breakBlock(Player player, Block block, Mineable mineable, boolean placedByPlayer) {
		if (mineable == null) {
			return;
		}
		if (!mineable.isMineable(playerData)) {
			return;
		}
		// All checks complete, Pickaxe is safe.
		
		// Drops
		ItemStack drop;
		if(getAutosmelt()) {
			drop = ((Ore) mineable).getAutosmeltDrop();
		} else if(getSilktouch()) {
			drop = mineable.getSilkDrop();
		} else {
			drop = mineable.getDrop();
		}
		int dropMultiplier = 1;
		if (!placedByPlayer) {
			if (!mineable.isFortuneDisabled()) {
				System.out.println(mineable.getMaterial() + "is fortune " + mineable.isFortuneDisabled());
				dropMultiplier = RPGPickaxe.rollDropMultiplier(playerData);
			}
			int amount = drop.getAmount();
			drop.setAmount(amount * dropMultiplier);
			
			int expAmount = (int) RPGPickaxe.getFortuneExpMultiplier(dropMultiplier) * mineable.getRandomExp();
			modifyExp(expAmount, dropMultiplier);
		}
		if (!getShouldProtect()) {
			modifyCurrentDurability(-1);
		} else {
			player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.06F, 1.7F);
		}
		
		double x, y, z;
		x = block.getLocation().getX();
		y = block.getLocation().getY();
		z = block.getLocation().getZ();
		
		if (dropMultiplier == 3) {
			// Triple effect
			player.getWorld().spawnParticle(Particle.DRAGON_BREATH,  x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
		} else if (dropMultiplier == 2) {
			// Double effect
			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
		}
		
		if (getAutosmelt()) {
			player.getWorld().spawnParticle(Particle.FLAME,  x + 0.5, y + 0.5, z + 0.5, 4, 0F, 0F, 0F, 0.009);
		}
		
		player.getWorld().spawnParticle(Particle.BLOCK_CRACK, x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 1, new MaterialData(block.getType()));
		player.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1F, 0.8F);
		block.setType(Material.AIR);
		block.getLocation().getWorld().dropItemNaturally(block.getLocation(), drop);
		if (!placedByPlayer) {
			int randExp = mineable.getRandomVanillaExp();
			if(!getSilktouch() && randExp != 0) {
				((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(randExp);
			}
		}
		
		tool.updateInInventory(player);
		
	}
	
	public void setAutosmelt(boolean value) {
		config.set("Pickaxe.Special.Autosmelt.Toggle", value);
		playerData.saveConfig();
	}
	
	public void refreshAutosmelt() {
		if (!config.contains("Pickaxe.Special.Autosmelt.Toggle")) {
			setAutosmelt(false);
		}
	}
	
	public boolean getAutosmelt() {
		if (!config.contains("Pickaxe.Special.Autosmelt.Toggle")) {
			return false;
		}
		return config.getBoolean("Pickaxe.Special.Autosmelt.Toggle");
	}
	
	public boolean getAutosmeltUnlocked() {
		if (!config.contains("Pickaxe.Special.Autosmelt.Unlocked")) {
			return false;
		}
		return config.getBoolean("Pickaxe.Special.Autosmelt.Unlocked");
	}
	
	public void setAutosmeltUnlocked(boolean value) {
		config.set("Pickaxe.Special.Autosmelt.Unlocked", value);
		playerData.saveConfig();
	}
	
	public void refreshAutosmeltUnlocked() {
		if (!config.contains("Pickaxe.Special.Autosmelt.Unlocked")) {
			setAutosmeltUnlocked(false);
		}
	}
	
	public void setSilktouch(boolean value) {
		config.set("Pickaxe.Special.Silktouch.Toggle", value);
		playerData.saveConfig();
	}
	
	public void refreshSilktouch() {
		if (!config.contains("Pickaxe.Special.Silktouch.Toggle")) {
			setSilktouch(false);
		}
	}
	
	public boolean getSilktouch() {
		if (!config.contains("Pickaxe.Special.Silktouch.Toggle")) {
			return false;
		}
		return config.getBoolean("Pickaxe.Special.Silktouch.Toggle");
	}
	
	public boolean getSilktouchUnlocked() {
		if (!config.contains("Pickaxe.Special.Silktouch.Unlocked")) {
			return false;
		}
		return config.getBoolean("Pickaxe.Special.Silktouch.Unlocked");
	}
	
	public void setSilktouchUnlocked(boolean value) {
		config.set("Pickaxe.Special.Silktouch.Unlocked", value);
		playerData.saveConfig();
	}
	
	public void refreshSilktouchUnlocked() {
		if (!config.contains("Pickaxe.Special.Silktouch.Unlocked")) {
			setSilktouchUnlocked(false);
		}
	}

}
