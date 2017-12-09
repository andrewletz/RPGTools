package net.Vala.traits;

import net.Vala.config.YAMLFile;

public class TraitUtil {
	
	private YAMLFile YML;
	
	public TraitUtil(YAMLFile yamlEnum) {
		this.YML = yamlEnum;
	}
	
	public int getSpeedSPReq(int level) {
		return YML.getConfig().getInt("Speed." + level);
	}
	
	public int getFortuneSPReq(int level) {
		return YML.getConfig().getInt("Fortune." + level);
	}
	
	public int getAutoRegenSPReq(int level) {
		return YML.getConfig().getInt("Autoregen." + level);
	}
	
	public int getReinforcedSPReq(int level) {
		return YML.getConfig().getInt("Reinforced." + level);
	}

	public int getKnockbackSPReq(int level) {
		return YML.getConfig().getInt("Knockback." + level);
	}
	
	public int getSilktouchSPReq() {
		return YML.getConfig().getInt("Silktouch");
	}
	
	public int getAutosmeltSPReq() {
		return YML.getConfig().getInt("Autosmelt");
	}
	
}
