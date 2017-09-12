package net.Vala.traits;

import java.util.Random;

import net.Vala.config.YAMLFile;

public class Reinforced {
	
	Random rand = new Random();
	YAMLFile YML;
	
	public Reinforced(YAMLFile yamlEnum) {
		this.YML = yamlEnum;
	}
	
	public boolean shouldProtectTool(int level) {
		double newDouble = rand.nextDouble();
		if (newDouble <= (getReductionPerLevel() * level) / 100) {
			return true;
		}
		return false;
		
	}
	
	public double getTotalPercent(int level) {
		return getReductionPerLevel() * level;
	}
	
	public double getReductionPerLevel() {
		return YML.getConfig().getDouble("Reinforcement.ReductionPerLevel");
	}
	
	public int getMaxLevel() {
		return YML.getConfig().getInt("Reinforcement.MaxLevel");
	}

}
