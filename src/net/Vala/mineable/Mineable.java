package net.Vala.mineable;

import java.util.Random;

import net.Vala.config.PlayerData;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Mineable {

	protected static final Random RANDOM = new Random();

	protected Material material;
	private byte blockData;
	private int minLevel;
	private double toughness;
	private int minExp;
	private int maxExp;
	private ItemStack drop;
	protected int minDropAmount;
	protected int maxDropAmount;
	private ItemStack silkDrop;
	private int minVanillaExp;
	private int maxVanillaExp;
	protected boolean fortuneDisabled;

	protected Mineable(Material material, byte blockData, Material drop,  int minDropAmount, int maxDropAmount, byte dropData, Material silkDrop, byte silkDropData, int minLevel, double toughness, 
			int minExp, int maxExp, int minVanillaExp, int maxVanillaExp, boolean fortuneDisabled) {
		this.material = material;
		this.blockData = blockData;
		this.drop = new ItemStack(drop, 1, dropData);
		this.minDropAmount = minDropAmount;
		this.maxDropAmount = maxDropAmount;
		this.silkDrop = new ItemStack(silkDrop, 1, silkDropData);
		this.minLevel = minLevel;
		this.toughness = toughness;
		this.minExp = minExp;
		this.maxExp = maxExp;
		this.minVanillaExp = minVanillaExp;
		this.maxVanillaExp = maxVanillaExp;
		this.fortuneDisabled = fortuneDisabled;
	}

	public Material getMaterial() {
		return material;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public double getToughness() {
		return toughness;
	}

	public int getRandomExp() {
		return (int) ((RANDOM.nextInt((maxExp - minExp) + 1) + minExp));
	}
	
	public ItemStack getDrop() {
		drop.setAmount((RANDOM.nextInt((maxDropAmount - minDropAmount) + 1) + minDropAmount));
		return drop;
	}
	
	public ItemStack getSilkDrop() {
		silkDrop.setAmount(1);
		return silkDrop;
	}
	
	public int getRandomVanillaExp() {
		if (minVanillaExp < 1) {
			return 0;
		} else {
			return (int) ((RANDOM.nextInt((maxVanillaExp - minVanillaExp) + 1) + minVanillaExp));
		}
	}
	
	public byte getBlockData() {
		return blockData;
	}

	public boolean isFortuneDisabled() {
		return fortuneDisabled;
	}
	public abstract boolean isMineable(PlayerData playerData);

}
