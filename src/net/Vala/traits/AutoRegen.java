package net.Vala.traits;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import net.Vala.config.ToolData;
import net.Vala.config.YAMLFile;
import net.Vala.general.RPGTools;
import net.Vala.pickaxe.PickaxeUtil;

public class AutoRegen {
	
	private BukkitScheduler scheduler;
	private ToolData toolData;
	private YAMLFile YML;
	private int id;
	private boolean firstRepair = true;
	
	public AutoRegen(ToolData toolData, YAMLFile yamlEnum) {
		this.toolData = toolData;
		scheduler = RPGTools.getPlugin().getServer().getScheduler();
		YML = yamlEnum;
		
		if (yamlEnum == YAMLFile.PICKAXECONFIG) {
			System.out.println("Initializing new autoregen!");
			initiatePickaxeScheduler(convertLevelToRandomTick(toolData.getAutoregen()));
		} else if (yamlEnum == YAMLFile.SHOVELCONFIG) {
//			initiateShovelScheduler(convertLevelToRandomTick(toolData.getShovelAutoregen()));
		} else if (yamlEnum == YAMLFile.AXECONFIG) {
//			initiateAxeScheduler(convertLevelToRandomTick(toolData.getAxeAutoregen()));
		}
	}
	
	public int convertLevelToRandomTick(int level) {
		int tickRate = (int) ((YML.getConfig().getInt("Autoregen.BaseTickRate")) - (level * YML.getConfig().getDouble("Autoregen.DecayRate")));
		if (tickRate <= 0) {
			return YML.getConfig().getInt("Autoregen.BaseTickRate");
		}
		return tickRate;
	}
	
	public void initiatePickaxeScheduler(int tickRate) {
		Integer taskId = scheduler.scheduleSyncRepeatingTask(RPGTools.getPlugin(), new Runnable() {
			@Override
            public void run() {
				if (!firstRepair) { // So it doesn't auto-repair when initialized
	            	if (toolData.getCurrentDurability() < toolData.getMaxDurability()) {
	            		toolData.modifyCurrentDurability(1);
	            		System.out.println("ticked!");
	            		System.out.println(toolData.getCurrentDurability());
	            		toolData.updateInInventory();
	            		ItemStack itemInHand = toolData.getPlayer().getInventory().getItemInMainHand();
	            		if (itemInHand != null && PickaxeUtil.isProfessionPickaxe(itemInHand)) {
	            			toolData.getPlayer().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, toolData.getPlayer().getLocation(), 25, 0.5F, 1.05F, 0.5F, 0.05);
	            		}
	            	}
				}
				firstRepair = false;
            }
        }, 0L, tickRate);
		this.id = taskId;
	}
	
	public int getTaskId() {
		return this.id;
	}
	
	public void cancelTask() {
		Bukkit.getServer().getScheduler().cancelTask(id);
		System.out.println("Cancelling autoregen!");
	}
	
}
