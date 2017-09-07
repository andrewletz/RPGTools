package net.Vala.general;

import java.util.logging.Logger;

import net.Vala.GUI.GUI;
import net.Vala.config.PlayerData;
import net.Vala.config.YAMLFile;
import net.Vala.listeners.GeneralListener;
import net.Vala.listeners.MiningListener;
import net.Vala.pickaxe.PickaxeCommands;
import net.Vala.pickaxe.Ore.Ores;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGTools extends JavaPlugin {
	
	private static RPGTools plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
    @Override
    public void onEnable() {
    	// Logging
    	PluginDescriptionFile pdfFile = this.getDescription();
    	this.logger.info(pdfFile.getName() + " \n\n\nRPGTools Has Been Enabled!\n\n\n");
    	
    	// Enable listeners and commands
    	this.enableListeners();
    	this.enableCommands();
    	
    	// Everything else
    	RPGTools.plugin = this;
    	PlayerData.prepareFiles();
		YAMLFile.prepareFiles();
		
		Ores.initializeOres();
    }
    
    @Override
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
    	this.logger.info(pdfFile.getName() + " Has Been Disabled!");
    }

	public static RPGTools getPlugin() {
		return RPGTools.plugin;
	}
	
	private void enableListeners() {
		this.getServer().getPluginManager().registerEvents(new GeneralListener(), this);
		this.getServer().getPluginManager().registerEvents(new MiningListener(), this);
		this.getServer().getPluginManager().registerEvents(new GUI(), this);
	}
	
	private void enableCommands() {
		getCommand("pickaxe").setExecutor(new PickaxeCommands());
		getCommand("pick").setExecutor(new PickaxeCommands());
		getCommand("rpgtools").setExecutor(new RPGCommands());
		getCommand("rt").setExecutor(new RPGCommands());
	}
}
