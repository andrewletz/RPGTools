package net.Vala.general;

import net.Vala.GUI.GUI;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.listeners.GeneralListener;
import net.Vala.listeners.BreakingListener;
import net.Vala.mineable.Ore.Ores;
import net.Vala.mineable.ShovelBlock.ShovelBlocks;
import net.Vala.tools.ToolCommands;

import org.bukkit.plugin.java.JavaPlugin;

public class RPGTools extends JavaPlugin {
	
	private static RPGTools plugin;
	
    @Override
    public void onEnable() {
    	// Enable listeners and commands
    	this.enableListeners();
    	this.enableCommands();
    	
    	// Everything else
    	RPGTools.plugin = this;
    	
		YAMLFile.prepareFiles();
		Logger.log("YAML file preparation complete");
		
    	PlayerData.prepareFiles();
    	Logger.log("Player data preparation complete");
		
		Ores.initializeOres();
		ShovelBlocks.initializeShovelBlocks();
		Logger.log("Mineables initialization complete");
		
		Logger.log("&7RPG Tools has completed initialization");
		if (!YAMLFile.CONFIG.getConfig().getBoolean("DisableConsoleColors")) {
			Logger.log("Console colors are currently enabled, option in config.yml.");
		}
		
    }
    
    @Override
    public void onDisable() {
    }

	public static RPGTools getPlugin() {
		return RPGTools.plugin;
	}
	
	private void enableListeners() {
		this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
		this.getServer().getPluginManager().registerEvents(new BreakingListener(), this);
		this.getServer().getPluginManager().registerEvents(new GUI(), this);
	}
	
	private void enableCommands() {
		getCommand("pickaxe").setExecutor(new ToolCommands());
		getCommand("pick").setExecutor(new ToolCommands());
		getCommand("rpgtools").setExecutor(new RPGCommands());
		getCommand("rt").setExecutor(new RPGCommands());
	}
}
