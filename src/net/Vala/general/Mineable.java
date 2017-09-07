package net.Vala.general;

import java.util.Random;

import net.Vala.config.PlayerData;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public abstract class Mineable {

	private static final Random RANDOM = new Random();

	protected Material material;
	private int minLevel;
	private double toughness;
	private int minExp;
	private int maxExp;
	private ItemStack drop;
	private int minVanillaExp;
	private int maxVanillaExp;

	@SuppressWarnings("deprecation")
	protected Mineable(Material material, ItemStack drop, int dropData, int minLevel, double toughness, int minExp, int maxExp, int minVanillaExp, int maxVanillaExp) {
		this.material = material;
		this.drop = drop;
		this.minLevel = minLevel;
		this.toughness = toughness;
		this.minExp = minExp;
		this.maxExp = maxExp;
		this.minVanillaExp = minVanillaExp;
		this.maxVanillaExp = maxVanillaExp;
		MaterialData mData = drop.getData();
		mData.setData((byte) dropData);
		drop.setData(mData);
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
	
	public int getRandomVanillaExp() {
		if (minVanillaExp < 1) {
			return 0;
		} else {
			return (int) ((RANDOM.nextInt((maxVanillaExp - minVanillaExp) + 1) + minVanillaExp));
		}
	}

	public abstract boolean isMineable(PlayerData playerData);

}
