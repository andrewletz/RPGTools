package net.Vala.traits;

import net.Vala.config.YAMLFile;

public class DropChances {

	private float doubleDropChance;
	private float tripleDropChance;

	/**
	 * @param level the level to calculate
	 */
	public DropChances(int level, YAMLFile yamlEnum) {
		switch (yamlEnum) {
		case PICKAXECONFIG:
			this.doubleDropChance = (float) (YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.DoubleDropBase") 
					+ (level * YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.DoubleDropMultiplier")));
			this.tripleDropChance = (float) (YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.TripleDropBase") 
					+ (level * YAMLFile.PICKAXECONFIG.getConfig().getDouble("Fortune.TripleDropMultiplier")));
		case SHOVELCONFIG:
			this.doubleDropChance = (float) (YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.DoubleDropBase") 
					+ (level * YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.DoubleDropMultiplier")));
			this.tripleDropChance = (float) (YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.TripleDropBase") 
					+ (level * YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.TripleDropMultiplier")));
		case AXECONFIG:
			this.doubleDropChance = (float) (YAMLFile.AXECONFIG.getConfig().getDouble("Fortune.DoubleDropBase") 
					+ (level * YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.DoubleDropMultiplier")));
			this.tripleDropChance = (float) (YAMLFile.AXECONFIG.getConfig().getDouble("Fortune.TripleDropBase") 
					+ (level * YAMLFile.SHOVELCONFIG.getConfig().getDouble("Fortune.TripleDropMultiplier")));
		default:
			break;
			
		}
		
	}

	public float getDoubleDropChance() {
		return doubleDropChance;
	}

	public float getTripleDropChance() {
		return tripleDropChance;
	}

}
