package net.Vala.traits;

import java.util.Random;

import net.Vala.config.YAMLFile;

public class Reinforced {
	
	Random rand = new Random();
	
	public boolean shouldProtectTool(int level) {
		double newDouble = rand.nextDouble();
		if (newDouble <= getReductionPerLevel() * level) {
			return true;
		}
		return false;
		
	}
	
	public double getReductionPerLevel() {
		return YAMLFile.CONFIG.getConfig().getDouble("Pickaxe.Reinforcement.ReductionPerLevel");
	}
	
	public int getMaxLevel() {
		return YAMLFile.CONFIG.getConfig().getInt("Pickaxe.Reinforcement.MaxLevel");
	}

}
