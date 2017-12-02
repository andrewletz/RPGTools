package net.Vala.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.Vala.general.Mineable;
import net.Vala.general.RPGTools;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.traits.AutoRegen;
import net.Vala.traits.DropChances;
import net.Vala.traits.Reinforced;
import net.Vala.util.GeneralUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import tools.RPGTool;

public abstract class ToolData {
	
	protected PlayerData playerData;
	protected FileConfiguration config = null;
	
	protected AutoRegen autoRegenTimer;
	protected Reinforced reinforced;
	
	protected Mineable targetMineable;
	protected boolean canMine = true;
	
	protected YAMLFile YML;
	protected String TOOL_STRING;
	
	protected RPGTool tool;
	
	public ToolData(PlayerData playerData, YAMLFile yamlFile) {
		this.playerData = playerData;
		this.config = playerData.getConfig();
		this.YML = yamlFile;
		this.TOOL_STRING = getToolStringFromYML();
		this.reinforced = new Reinforced(yamlFile);
	}
	
	public void refreshConfig() {
		this.modifyLevel(0);
		this.modifySP(0);
		this.modifySpeed(0);
		this.modifyFortune(0);
		this.modifyAutoregen(0);
		this.modifyReinforced(0);
		this.modifyKnockback(0);
		this.modifyCurrentDurability(0);
		this.refreshAutoRegenTimer();
		this.refreshToolSpecific();
	}
	
	protected abstract void refreshToolSpecific();
	
	public abstract double getTotalSpeed();
	
	public abstract DropChances getFortuneDrop();
	
	private String getToolStringFromYML() {
		switch (YML) {
		case PICKAXECONFIG:
			return "Pickaxe";
		case SHOVELCONFIG:
			return "Shovel";
		case AXECONFIG:
			return "Axe";
		default:
			break;
		}
		return "Pickaxe";
	}
	
	public boolean giveNew(Player player) {
		return tool.giveNew(player);
	}
	
	public Player getPlayer() {
		return playerData.getPlayer();
	}
	
	/*
	 * Target block
	 */
	
	public Mineable getTargetBlock() {
		return targetMineable;
	}

	public void setTargetBlock(Block targetBlock, Mineable targetMineable) {
		playerData.setTargetBlock(targetBlock);
		this.targetMineable = targetMineable;
	}

	public void damageBlock(boolean isUnderwater, boolean hasAquaAffinity, boolean placedByPlayer) {
		if (canMine) {
			canMine = false;
			if (playerData.getTargetBlock() == null || this.targetMineable == null) {
				resetBlockDamage();
				return;
			}
			if (!isUnderwater || hasAquaAffinity) {
				playerData.modifyBlockDamage(getTotalSpeed());
			} else {
				playerData.modifyBlockDamage(getTotalSpeed() / 5);
			}
			GeneralUtil.sendBreakPacket(playerData.getPlayer(), playerData.getTargetBlock(), (int) (9 * (playerData.getBlockDamage() / (float) this.targetMineable.getToughness())));
			if (playerData.getBlockDamage() >= this.targetMineable.getToughness()) {
				// Break the block
				GeneralUtil.sendBreakPacket(playerData.getPlayer(), playerData.getTargetBlock(), -1);
				breakBlock(playerData.getPlayer(), playerData.getTargetBlock(), this.targetMineable, placedByPlayer);
				playerData.setTargetBlock(null);
				this.targetMineable = null;
				resetBlockDamage();
				
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RPGTools.getPlugin(), new Runnable() {
			     public void run() {
			          canMine = true;
			     }
			}, 1);
			
		}
	}
	
	/**
	 * Breaks the block as though the player mined it
	 * @param player the player who is breaking
	 * @param block the block to be broken
	 * @param mineable the corresponding mineable for the block
	 */
	protected abstract void breakBlock(Player player, Block block, Mineable mineable, boolean placedByPlayer);

	public void resetBlockDamage() {
		playerData.setBlockDamage(0);
	}
	
	/**
	 * General  Information (Durability, Exp, Level, SP) test
	 */
	
	public int getMaxDurability() {
//		System.out.println("\n\n\n\n\n"+getLevel()+"\n\n\n\n\n");
		return convertLevelToDurability(getLevel());
	}
	
	public int convertLevelToDurability(int level) {
		if (level >= YML.getConfig().getInt("UnlockLevels.Diamond")) {
			return YML.getConfig().getInt("DiamondDurability");
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Gold")) {
			return YML.getConfig().getInt("GoldDurability");
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Iron")) {
			return YML.getConfig().getInt("IronDurability");
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Stone")) {
			return YML.getConfig().getInt("StoneDurability");
		}
		
		return YML.getConfig().getInt("WoodDurability");
	}
	
	public int getMaxLevel() {
		return YML.getConfig().getInt("MaxLevel");
	}
	
	public int getDifficultyMultiplier() {
		return YML.getConfig().getInt("DifficultyMultiplier");
	}
	
	public void setCurrentDurability(int value) {
		if (value < 0) {
			value = 0;
		}
		config.set(TOOL_STRING + ".CurrentDurability", value);
		playerData.saveConfig();
		tool.updateInInventory(playerData.getPlayer());
	}
	
	public void modifyCurrentDurability(int value) {
		if (!config.contains(TOOL_STRING + ".CurrentDurability")) {
			setCurrentDurability(getMaxDurability());
		}
		setCurrentDurability(getCurrentDurability() + value);
	}
	
	public int getCurrentDurability() {
		if (!config.contains(TOOL_STRING + ".CurrentDurability")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".CurrentDurability");
	}
	
	public void setLevel(int value) {
		config.set(TOOL_STRING + ".Level", value);
		playerData.saveConfig();
	}
	
	public void modifyLevel(int value) {
		if (!config.contains(TOOL_STRING + ".Level")) {
			setLevel(1);
		}
		setLevel(getLevel() + value);
	}
	
	public int getLevel() {
		if (!config.contains(TOOL_STRING + ".Level")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Level");
	}
	
	public void setSP(int value) {
		config.set(TOOL_STRING + ".SP", value);
		playerData.saveConfig();
	}
	
	public void modifySP(int value) {
		if (!config.contains(TOOL_STRING + ".SP")) {
			setSP(1);
		}
		setSP(getSP() + value);
	}
	
	public int getSP() {
		if (!config.contains(TOOL_STRING + ".SP")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".SP");
	}
	
	/*
	 *  experience
	 */
	
	public void setExp(int value) {
		if (getLevel() >= getMaxLevel()) {
			return;
		}
		config.set(TOOL_STRING + ".Exp", value);
		if (value >= getExpToNextLevel()) { // Level Up
			levelUp();
		}
		playerData.saveConfig();
	}

	public int getExp() {
		if (!config.contains(TOOL_STRING + ".Exp")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Exp");
	}

	public void modifyExp(int value, int dropAmount) {
		if (!config.contains(TOOL_STRING + ".Exp")) {
			setExp(1);
		}
		setExp(getExp() + value);
		
		
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        
        /*
         * Credit to https://github.com/connorlinfoot/ActionBarAPI by ConnorLinfoot
         */
        
        try {
         String messageToSend = ChatColor.GRAY + "" + ChatColor.BOLD + "+" + ChatColor.GRAY + value + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "  EXP";
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
	
	public int getExpToNextLevel() {
		int level = getLevel();
		return (int) (getDifficultyMultiplier() * Math.pow(level, 1.8) + (4 * level) + 40);
	}

	public void levelUp() {
		if (getLevel() >= getMaxLevel()) {
			return;
		}

		int newExp = getExp() - getExpToNextLevel();
		modifyLevel(1);
		modifySP(YML.getConfig().getInt("SPPerLevel"));
		GeneralUtil.playLevelSound(playerData.getPlayer());
		playerData.getPlayer().sendMessage(" ");
		playerData.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " LEVEL " + getLevel());
		playerData.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + " Stat Points" + ChatColor.AQUA + " + " + YML.getConfig().getInt("SPPerLevel"));
		playerData.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + getExpToNextLevel() + " xp to lvl " + (getLevel() + 1));
		playerData.getPlayer().sendMessage(" ");
		playerData.saveConfig();
		new BukkitRunnable() {
			@Override
			public void run() {
				setExp(newExp);
				tool.updateInInventory(playerData.getPlayer());
			}
		}.runTaskLater(RPGTools.getPlugin(), 1L);
	}

	/*
	 *  stats and special abilities
	 */
	
	public void setSpeed(int value) {
		config.set(TOOL_STRING + ".Speed", value);
		playerData.saveConfig();
	}
	
	public void modifySpeed(int value) {
		if (!config.contains(TOOL_STRING + ".Speed")) {
			setSpeed(1);
		}
		setSpeed(getSpeed() + value);
	}
	
	public int getSpeed() {
		if (!config.contains(TOOL_STRING + ".Speed")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Speed");
	}
	
	public void setFortune(int value) {
		config.set(TOOL_STRING + ".Fortune", value);
		playerData.saveConfig();
	}
	
	public void modifyFortune(int value) {
		if (!config.contains(TOOL_STRING + ".Fortune")) {
			setFortune(1);
		}
		setFortune(getFortune() + value);
	}
	
	public int getFortune() {
		if (!config.contains(TOOL_STRING + ".Fortune")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Fortune");
	}

	
	public void setAutoregen(int value) {
		config.set(TOOL_STRING + ".Autoregen", value);
		playerData.saveConfig();
		refreshAutoRegenTimer();
	}
	
	public void modifyAutoregen(int value) {
		if (!config.contains(TOOL_STRING + ".Autoregen")) {
			setAutoregen(1);
		}
		setAutoregen(getAutoregen() + value);
		refreshAutoRegenTimer();
	}
	
	public int getAutoregen() {
		if (!config.contains(TOOL_STRING + ".Autoregen")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Autoregen");
	}
	
	public void refreshAutoRegenTimer() {
		if (autoRegenTimer != null) {
			autoRegenTimer.cancelTask();
		}
		autoRegenTimer = new AutoRegen(playerData, YML);
	}
	
	public AutoRegen getAutoRegenClass(boolean refresh) {
		if (refresh) {
			refreshAutoRegenTimer();
		}
		return autoRegenTimer;
	}
	
	public void setReinforced(int value) {
		config.set(TOOL_STRING + ".Reinforced", value);
		playerData.saveConfig();
	}
	
	public void modifyReinforced(int value) {
		if (!config.contains(TOOL_STRING + ".Reinforced")) {
			setReinforced(1);
		}
		setReinforced(getReinforced() + value);
	}
	
	public int getReinforced() {
		if (!config.contains(TOOL_STRING + ".Reinforced")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Reinforced");
	}
	
	public boolean getShouldProtect() {
		return reinforced.shouldProtectTool(getReinforced());
	}
	
	public void setKnockback(int value) {
		config.set(TOOL_STRING + ".Knockback", value);
		playerData.saveConfig();
	}
	
	public void modifyKnockback(int value) {
		if (!config.contains(TOOL_STRING + ".Knockback")) {
			setKnockback(1);
		}
		setKnockback(getKnockback() + value);
	}
	
	public int getKnockback() {
		if (!config.contains(TOOL_STRING + ".Knockback")) {
			return 1;
		}
		return config.getInt(TOOL_STRING + ".Knockback");
	}
	
	public boolean isMaxSpeed() {
		return getSpeed() == PickaxeUtil.getMaxSpeed();
	}
	
	public boolean isMaxFortune() {
		return getFortune() == PickaxeUtil.getMaxFortune();
	}
	
	public boolean isMaxAutoregen() {
		return getAutoregen() == PickaxeUtil.getMaxAutoregen();
	}
	
	public boolean isMaxReinforced() {
		return getReinforced() == PickaxeUtil.getMaxReinforced();
	}
	
	public boolean isBroken() {
		return getCurrentDurability() <= 0;
	}

}
