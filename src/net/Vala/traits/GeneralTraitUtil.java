package net.Vala.traits;

import net.Vala.config.YAMLFile;

public class GeneralTraitUtil {
	
	public static int getPickSpeedSPReq(int level) {
		if(level <= YAMLFile.ADVANCED.getConfig().getInt("Pickaxe.Speed.CeilCutoff")) {
			return (int) Math.ceil(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Speed.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Speed.SPCorrector"));
		}
		return (int) Math.round(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Speed.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Speed.SPCorrector"));
	}
	
	public static int getPickFortuneSPReq(int level) {
		if(level <= YAMLFile.ADVANCED.getConfig().getInt("Pickaxe.Fortune.CeilCutoff")) {
			return (int) Math.ceil(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Fortune.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Fortune.SPCorrector"));
		}
		return (int) Math.round(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Fortune.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Fortune.SPCorrector"));
	}
	
	public static int getPickAutoRegenSPReq(int level) {
		if(level <= YAMLFile.ADVANCED.getConfig().getInt("Pickaxe.Autoregen.CeilCutoff")) {
			return (int) Math.ceil(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Autoregen.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Autoregen.SPCorrector"));
		}
		return (int) Math.round(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Autoregen.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Autoregen.SPCorrector"));
	}
	
	public static int getPickReinforcedSPReq(int level) {
		if(level <= YAMLFile.ADVANCED.getConfig().getInt("Pickaxe.Reinforced.CeilCutoff")) {
			return (int) Math.ceil(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Reinforced.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Reinforced.SPCorrector"));
		}
		return (int) Math.round(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Reinforced.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Reinforced.SPCorrector"));
	}

	public static int getPickKnockbackSPReq(int level) {
		if(level <= YAMLFile.ADVANCED.getConfig().getInt("Pickaxe.Knockback.CeilCutoff")) {
			return (int) Math.ceil(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Knockback.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Knockback.SPCorrector"));
		}
		return (int) Math.round(Math.pow(level, YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Knockback.SPExponent")) - YAMLFile.ADVANCED.getConfig().getDouble("Pickaxe.Knockback.SPCorrector"));
	}
	
}
