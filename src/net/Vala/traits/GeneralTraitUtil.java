package net.Vala.traits;

import net.Vala.config.YAMLFile;

public class GeneralTraitUtil {
	
	public static int getPickSpeedSPReq(int level) {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Speed." + level);
	}
	
	public static int getPickFortuneSPReq(int level) {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Fortune." + level);
	}
	
	public static int getPickAutoRegenSPReq(int level) {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Autoregen." + level);
	}
	
	public static int getPickReinforcedSPReq(int level) {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Reinforced." + level);
	}

	public static int getPickKnockbackSPReq(int level) {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Knockback." + level);
	}
	
	public static int getPickSilktouchSPReq() {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Silktouch");
	}
	
	public static int getPickAutosmeltSPReq() {
		return YAMLFile.PICKAXESPCOSTS.getConfig().getInt("Autosmelt");
	}
	
	public static int getShovelSpeedSPReq(int level) {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Speed." + level);
	}
	
	public static int getShovelFortuneSPReq(int level) {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Fortune." + level);
	}
	
	public static int getShovelAutoRegenSPReq(int level) {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Autoregen." + level);
	}
	
	public static int getShovelReinforcedSPReq(int level) {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Reinforced." + level);
	}

	public static int getShovelKnockbackSPReq(int level) {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Knockback." + level);
	}
	
	public static int getShovelSilktouchSPReq() {
		return YAMLFile.SHOVELSPCOSTS.getConfig().getInt("Silktouch");
	}
	
	public static int getAxeSpeedSPReq(int level) {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Speed." + level);
	}
	
	public static int getAxeFortuneSPReq(int level) {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Fortune." + level);
	}
	
	public static int getAxeAutoRegenSPReq(int level) {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Autoregen." + level);
	}
	
	public static int getAxeReinforcedSPReq(int level) {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Reinforced." + level);
	}

	public static int getAxeKnockbackSPReq(int level) {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Knockback." + level);
	}
	
	public static int getAxeSilktouchSPReq() {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Silktouch");
	}
	
	public static int getAxeAutosmeltSPReq() {
		return YAMLFile.AXESPCOSTS.getConfig().getInt("Autosmelt");
	}
	
}
