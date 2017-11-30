package net.Vala.config;

import net.Vala.general.Mineable;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.traits.DropChances;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tools.RPGPickaxe;

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
		return PickaxeUtil.convertPickSpeedToDamagePerTick(getSpeed());
	}

	@Override
	public DropChances getFortuneDrop() {
		return PickaxeUtil.convertPickaxeLuckLevelToPickaxeDropChances(getFortune());
	}

	@Override
	protected void breakBlock(Player player, Block block, Mineable mineable,
			boolean placedByPlayer) {
		// TODO Auto-generated method stub
		
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
