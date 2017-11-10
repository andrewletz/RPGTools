package net.Vala.general;

import net.Vala.config.YAMLFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
	
    public static void log(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&7[&5" + RPGTools.getPlugin(RPGTools.class).getName() + "&7]&r " + msg);
        if (YAMLFile.CONFIG.getConfig().getBoolean("DisableConsoleColors")) {
        	msg = ChatColor.stripColor(msg);
        }
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    public static void debug(String msg) {
        log("&4[&cDEBUG&4]&r " + msg);
    }
    
}
