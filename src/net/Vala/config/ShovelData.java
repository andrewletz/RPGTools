package net.Vala.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.Vala.general.RPGTools;
import net.Vala.shovel.ShovelBlock;
import net.Vala.shovel.ShovelFactory;
import net.Vala.traits.AutoRegen;
import net.Vala.traits.DropChances;
import net.Vala.traits.Reinforced;
import net.Vala.util.GeneralUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class ShovelData {
	
	private PlayerData playerData;
	private FileConfiguration config = null;
	private AutoRegen autoRegenTimer;
	private ShovelBlock targetShovelBlock;
	private Reinforced reinforced;
	
	public ShovelData(PlayerData playerData) {
		this.playerData = playerData;
		this.config = playerData.getConfig();
		this.reinforced = new Reinforced(YAMLFile.SHOVELCONFIG);
	}
	
	public void refreshConfig() {
		this.modifyShovelLevel(0);
		this.modifyShovelSP(0);
		this.modifyShovelSpeed(0);
		this.modifyShovelFortune(0);
		this.modifyShovelAutoregen(0);
		this.modifyShovelReinforced(0);
		this.modifyShovelKnockback(0);
		this.modifyShovelCurrentDurability(0);
		this.refreshShovelSilktouch();
		this.refreshAutoRegenTimer();
	}
	
	/**
	 * General Shovel Information (Durability, Exp, Level, SP)
	 */
	
	public int getShovelMaxDurability() {
		return ShovelFactory.convertShovelLevelToDurability(getShovelLevel());
	}
	
	public void setShovelCurrentDurability(int value) {
		if (value < 0) {
			value = 0;
		}
		config.set("Shovel.CurrentDurability", value);
		playerData.saveConfig();
		ShovelFactory.updateShovelInInventory(playerData.getPlayer());
	}
	
	public void modifyShovelCurrentDurability(int value) {
		if (!config.contains("Shovel.CurrentDurability")) {
			setShovelCurrentDurability(getShovelMaxDurability());
		}
		setShovelCurrentDurability(getShovelCurrentDurability() + value);
	}
	
	public int getShovelCurrentDurability() {
		if (!config.contains("Shovel.CurrentDurability")) {
			return 1;
		}
		return config.getInt("Shovel.CurrentDurability");
	}
	
	public void setShovelLevel(int value) {
		config.set("Shovel.Level", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelLevel(int value) {
		if (!config.contains("Shovel.Level")) {
			setShovelLevel(1);
		}
		setShovelLevel(getShovelLevel() + value);
	}
	
	public int getShovelLevel() {
		if (!config.contains("Shovel.Level")) {
			return 1;
		}
		return config.getInt("Shovel.Level");
	}
	
	public void setShovelSP(int value) {
		config.set("Shovel.SP", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelSP(int value) {
		if (!config.contains("Shovel.SP")) {
			setShovelSP(1);
		}
		setShovelSP(getShovelSP() + value);
	}
	
	public int getShovelSP() {
		if (!config.contains("Shovel.SP")) {
			return 1;
		}
		return config.getInt("Shovel.SP");
	}
	
	/*
	 * Shovel experience
	 */
	
	public void setShovelExp(int value) {
		if (getShovelLevel() >= ShovelFactory.getMaxLevel()) {
			return;
		}
		config.set("Shovel.Exp", value);
		if (value >= getShovelExpToNextLevel()) { // Level Up
			levelUpShovel();
		}
		playerData.saveConfig();
	}

	public int getShovelExp() {
		if (!config.contains("Shovel.Exp")) {
			return 1;
		}
		return config.getInt("Shovel.Exp");
	}

	public void modifyShovelExp(int value, int dropAmount) {
		if (getShovelSilktouch() && value > YAMLFile.SHOVELCONFIG.getConfig().getInt("Silktouch.MaxExpGain")) {
			return;
		}
		if (!config.contains("Shovel.Exp")) {
			setShovelExp(1);
		}
		setShovelExp(getShovelExp() + value);
		
		
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        
        /*
         * Credit to https://github.com/connorlinfoot/ActionBarAPI by ConnorLinfoot
         */
        
        try {
         String messageToSend = ChatColor.GRAY + "" + ChatColor.BOLD + "+" + ChatColor.GRAY + value + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " Shovel EXP";
         if (dropAmount == 2) {
        	 messageToSend = ChatColor.BOLD + "" + ChatColor.WHITE + "★ " + messageToSend + ChatColor.BOLD + "" + ChatColor.WHITE + " ★";
         } else if (dropAmount == 3) {
        	 messageToSend = ChatColor.BOLD + "" + ChatColor.AQUA + "★ " + messageToSend + ChatColor.BOLD + "" + ChatColor.AQUA + " ★";
         }
		 Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
         Object craftPlayer = craftPlayerClass.cast(playerData.getPlayer());
         Object ppoc;
         Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
         Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
         Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
         Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
         Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
         Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
         Object chatMessageType = null;
         for (Object obj : chatMessageTypes) {
             if (obj.toString().equals("GAME_INFO")) {
                 chatMessageType = obj;
             }
         }
         Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(messageToSend);
         ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
         Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
         Object h = m1.invoke(craftPlayer);
         Field f1 = h.getClass().getDeclaredField("playerConnection");
         Object pc = f1.get(h);
         Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
         m5.invoke(pc, ppoc);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	public int getShovelExpToNextLevel() {
		int level = getShovelLevel();
		return (int) (ShovelFactory.getShovelDifficultyMultiplier(level) * Math.pow(level, 1.4) + 2 * level + 50);
	}

	public void levelUpShovel() {
		if (getShovelLevel() >= ShovelFactory.getMaxLevel()) {
			return;
		}

		int newExp = getShovelExp() - getShovelExpToNextLevel();
		modifyShovelLevel(1);
		modifyShovelSP(YAMLFile.SHOVELCONFIG.getConfig().getInt("SPPerLevel"));
		GeneralUtil.playLevelSound(playerData.getPlayer());
		playerData.getPlayer().sendMessage(" ");
		playerData.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Shovel LEVEL " + getShovelLevel());
		playerData.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Shovel Stat Points" + ChatColor.AQUA + " + 1");
		playerData.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + getShovelExpToNextLevel() + " xp to lvl " + (getShovelLevel() + 1));
		playerData.getPlayer().sendMessage(" ");
		playerData.saveConfig();
		new BukkitRunnable() {
			@Override
			public void run() {
				setShovelExp(newExp);
				ShovelFactory.updateShovelInInventory(playerData.getPlayer());
			}
		}.runTaskLater(RPGTools.getPlugin(), 1L);
	}
	
	/*
	 * Target block
	 */
	
	public ShovelBlock getTargetShovelBlock() {
		return targetShovelBlock;
	}

	public void setTargetBlock(Block targetBlock, ShovelBlock targetShovelBlock) {
		playerData.setTargetBlock(targetBlock);
		this.targetShovelBlock = targetShovelBlock;
	}

	public void damageBlock() {
		if (playerData.getTargetBlock() == null || this.targetShovelBlock == null) {
			resetBlockDamage();
			return;
		}
		playerData.modifyBlockDamage(getShovelTotalSpeed());
		GeneralUtil.sendBreakPacket(playerData.getPlayer(), playerData.getTargetBlock(), (int) (9 * (playerData.getBlockDamage() / (float) this.targetShovelBlock.getToughness())));
		if (playerData.getBlockDamage() >= this.targetShovelBlock.getToughness()) {
			// Break the block
			ShovelFactory.breakBlock(playerData.getPlayer(), playerData.getTargetBlock(), this.targetShovelBlock);
			playerData.setTargetBlock(null);
			this.targetShovelBlock = null;
			resetBlockDamage();
		}
	}

	public void resetBlockDamage() {
		playerData.setBlockDamage(0);
	}

	/*
	 * Shovel stats and special abilities
	 */
	
	public void setShovelSpeed(int value) {
		config.set( "Shovel.Speed", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelSpeed(int value) {
		if (!config.contains("Shovel.Speed")) {
			setShovelSpeed(1);
		}
		setShovelSpeed(getShovelSpeed() + value);
	}
	
	public int getShovelSpeed() {
		if (!config.contains("Shovel.Speed")) {
			return 1;
		}
		return config.getInt("Shovel.Speed");
	}
	
	public double getShovelTotalSpeed() {
		return ShovelFactory.convertShovelSpeedToDamagePerTick(getShovelSpeed());
	}
	
	public void setShovelFortune(int value) {
		config.set( "Shovel.Fortune", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelFortune(int value) {
		if (!config.contains("Shovel.Fortune")) {
			setShovelFortune(1);
		}
		setShovelFortune(getShovelFortune() + value);
	}
	
	public int getShovelFortune() {
		if (!config.contains("Shovel.Fortune")) {
			return 1;
		}
		return config.getInt("Shovel.Fortune");
	}
	
	public DropChances getShovelFortuneDrop() {
		return ShovelFactory.convertShovelLuckLevelToShovelDropChances(getShovelFortune());
	}
	
	public void setShovelAutoregen(int value) {
		config.set( "Shovel.Autoregen", value);
		playerData.saveConfig();
		refreshAutoRegenTimer();
	}
	
	public void modifyShovelAutoregen(int value) {
		if (!config.contains("Shovel.Autoregen")) {
			setShovelAutoregen(1);
		}
		setShovelAutoregen(getShovelAutoregen() + value);
		refreshAutoRegenTimer();
	}
	
	public int getShovelAutoregen() {
		if (!config.contains("Shovel.Autoregen")) {
			return 1;
		}
		return config.getInt("Shovel.Autoregen");
	}
	
	public void refreshAutoRegenTimer() {
		if (autoRegenTimer != null) {
			autoRegenTimer.cancelTask();
		}
		autoRegenTimer = new AutoRegen(playerData, YAMLFile.SHOVELCONFIG);
	}
	
	public AutoRegen getAutoRegenClass() {
		return autoRegenTimer;
	}
	
	public void setShovelReinforced(int value) {
		config.set( "Shovel.Reinforced", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelReinforced(int value) {
		if (!config.contains("Shovel.Reinforced")) {
			setShovelReinforced(1);
		}
		setShovelReinforced(getShovelReinforced() + value);
	}
	
	public int getShovelReinforced() {
		if (!config.contains("Shovel.Reinforced")) {
			return 1;
		}
		return config.getInt("Shovel.Reinforced");
	}
	
	public boolean getShovelShouldProtect() {
		return reinforced.shouldProtectTool(getShovelReinforced());
	}
	
	public void setShovelKnockback(int value) {
		config.set( "Shovel.Knockback", value);
		playerData.saveConfig();
	}
	
	public void modifyShovelKnockback(int value) {
		if (!config.contains("Shovel.Knockback")) {
			setShovelKnockback(1);
		}
		setShovelKnockback(getShovelKnockback() + value);
	}
	
	public int getShovelKnockback() {
		if (!config.contains("Shovel.Knockback")) {
			return 1;
		}
		return config.getInt("Shovel.Knockback");
	}
	
	public void setShovelSilktouch(boolean value) {
		config.set( "Shovel.Special.Silktouch", value);
		playerData.saveConfig();
	}
	
	public void refreshShovelSilktouch() {
		if (!config.contains("Shovel.Special.Silktouch")) {
			setShovelSilktouch(false);
		}
	}
	
	public boolean getShovelSilktouch() {
		if (!config.contains("Shovel.Special.Silktouch")) {
			return false;
		}
		return config.getBoolean("Shovel.Special.Silktouch");
	}

}
