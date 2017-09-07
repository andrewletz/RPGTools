package net.Vala.traits;

import net.Vala.config.YAMLFile;

public class DropChances {

	private float doubleDropChance;
	private float tripleDropChance;
	private YAMLFile YML;

	/**
	 * @param level the level to calculate
	 */
	public DropChances(int level, YAMLFile yamlEnum) {
		this.doubleDropChance = (float) (YAMLFile.CONFIG.getConfig().getDouble("Pickaxe.Fortune.DoubleDropBase") 
				+ (level * YAMLFile.CONFIG.getConfig().getDouble("Pickaxe.Fortune.DoubleDropMultiplier")));
		this.tripleDropChance = (float) (YAMLFile.CONFIG.getConfig().getDouble("Pickaxe.Fortune.TripleDropBase") 
				+ (level * YAMLFile.CONFIG.getConfig().getDouble("Pickaxe.Fortune.TripleDropMultiplier")));
	}

	public float getDoubleDropChance() {0
		return doubleDropChance;
	}

	public float getTripleDropChance() {
		return tripleDropChance;
	}

}
