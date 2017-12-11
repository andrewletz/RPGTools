package net.Vala.mineable;

import java.util.Random;

import net.Vala.config.PlayerData;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Mineable {

	private static final Random RANDOM = new Random();

	protected Material material;
	private byte blockData;
	private int minLevel;
	private double toughness;
	private int minExp;
	private int maxExp;
	private ItemStack drop;
	private ItemStack silkDrop;
	private int minVanillaExp;
	private int maxVanillaExp;

	protected Mineable(Material material, byte blockData, Material drop, byte dropData, Material silkDrop, byte silkDropData, int minLevel, double toughness, 
			int minExp, int maxExp, int minVanillaExp, int maxVanillaExp) {
		this.material = material;
		this.blockData = blockData;
		this.drop = new ItemStack(drop, 1, dropData);
		this.silkDrop = new ItemStack(silkDrop, 1, silkDropData);
		this.minLevel = minLevel;
		this.toughness = toughness;
		this.minExp = minExp;
		this.maxExp = maxExp;
		this.minVanillaExp = minVanillaExp;
		this.maxVanillaExp = maxVanillaExp;
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
		return drop;
	}
	
	public ItemStack getSilkDrop() {
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

	public abstract boolean isMineable(PlayerData playerData);

}
