package net.Vala.traits;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitScheduler;

import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.general.RPGTools;
import net.Vala.pickaxe.PickaxeFactory;

public class AutoRegen {
	
	private BukkitScheduler scheduler;
	private PlayerData playerData;
	private YAMLFile YML;
	private int id;
	
	public AutoRegen(PlayerData playerData, YAMLFile yamlEnum) {
		this.playerData = playerData;
		scheduler = RPGTools.getPlugin().getServer().getScheduler();
		YML = yamlEnum;
		
		if (yamlEnum == YAMLFile.PICKAXECONFIG) {
			initiateScheduler(convertLevelToRandomTick(playerData.getPickaxeData().getPickaxeAutoregen()));
		} else if (yamlEnum == YAMLFile.SHOVELCONFIG) {
			initiateScheduler(convertLevelToRandomTick(playerData.getShovelData().getShovelAutoregen()));
		} // do this for axes
	}
	
	public int convertLevelToRandomTick(int level) {
		int tickRate = (int) ((YML.getConfig().getInt("Autoregen.BaseTickRate")) - (level * YML.getConfig().getDouble("Autoregen.DecayRate")));
		if (tickRate <= 0) {
			return YML.getConfig().getInt("Autoregen.BaseTickRate");
		}
		return tickRate;
	}

	// Make multiple versions of this for each tool
	public void initiateScheduler(int tickRate) {
		Integer taskId = scheduler.scheduleSyncRepeatingTask(RPGTools.getPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
            	if (playerData.getPickaxeData().getPickaxeCurrentDurability() < playerData.getPickaxeData().getPickaxeMaxDurability()) {
            		playerData.getPickaxeData().modifyPickaxeCurrentDurability(1);
            		if (PickaxeFactory.isProfessionPickaxe(playerData.getPlayer().getItemInHand())) {
            			playerData.getPlayer().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, playerData.getPlayer().getLocation(), 25, 0.5F, 1.05F, 0.5F, 0.05);
            		}
            	}
            }
        }, 0L, tickRate);
		this.id = taskId;
	}
	
	public int getTaskId() {
		return this.id;
	}
	
	public void cancelTask() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
