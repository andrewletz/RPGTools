package net.Vala.config;
//package net.Vala.config;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
//import net.Vala.general.RPGTools;
//import net.Vala.pickaxe.Ore;
//import net.Vala.pickaxe.Pickaxe;
//import net.Vala.pickaxe.PickaxeUtil;
//import net.Vala.traits.AutoRegen;
//import net.Vala.traits.DropChances;
//import net.Vala.traits.Reinforced;
//import net.Vala.util.GeneralUtil;
//
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.Particle;
//import org.bukkit.Sound;
//import org.bukkit.block.Block;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.ExperienceOrb;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.material.MaterialData;
//import org.bukkit.scheduler.BukkitRunnable;
//
//public class PickaxeData {
//	
//	private PlayerData playerData;
//	private FileConfiguration config = null;
//	private AutoRegen autoRegenTimer;
//	private Ore targetOre;
//	private Reinforced reinforced;
//	private boolean canMine = true;
//	
//	public PickaxeData(PlayerData playerData) {
//		this.playerData = playerData;
//		this.config = playerData.getConfig();
//		this.reinforced = new Reinforced(YAMLFile.PICKAXECONFIG);
//	}
//	
//	public void refreshConfig() {
//		this.modifyPickaxeLevel(0);
//		this.modifyPickaxeSP(0);
//		this.modifyPickaxeSpeed(0);
//		this.modifyPickaxeFortune(0);
//		this.modifyPickaxeAutoregen(0);
//		this.modifyPickaxeReinforced(0);
//		this.modifyPickaxeKnockback(0);
//		this.modifyPickaxeCurrentDurability(0);
//		this.refreshPickaxeAutosmelt();
//		this.refreshPickaxeSilktouch();
//		this.refreshPickaxeAutosmeltUnlocked();
//		this.refreshPickaxeSilktouchUnlocked();
//		this.refreshAutoRegenTimer();
//	}
//	
//	/*
//	 * Target block
//	 */
//	
//	public Ore getTargetPickaxeBlock() {
//		return targetOre;
//	}
//
//	public void setTargetBlock(Block targetBlock, Ore targetOre) {
//		playerData.setTargetBlock(targetBlock);
//		this.targetOre = targetOre;
//	}
//
//	public void damageBlock(boolean isUnderwater, boolean hasAquaAffinity, boolean placedByPlayer) {
//		if (canMine) {
//			canMine = false;
//			if (playerData.getTargetBlock() == null || this.targetOre == null) {
//				resetBlockDamage();
//				return;
//			}
//			if (!isUnderwater || hasAquaAffinity) {
//				playerData.modifyBlockDamage(getPickaxeTotalSpeed());
//			} else {
//				playerData.modifyBlockDamage(getPickaxeTotalSpeed() / 5);
//			}
//			GeneralUtil.sendBreakPacket(playerData.getPlayer(), playerData.getTargetBlock(), (int) (9 * (playerData.getBlockDamage() / (float) this.targetOre.getToughness())));
//			if (playerData.getBlockDamage() >= this.targetOre.getToughness()) {
//				// Break the block
//				GeneralUtil.sendBreakPacket(playerData.getPlayer(), playerData.getTargetBlock(), -1);
//				breakBlock(playerData.getPlayer(), playerData.getTargetBlock(), this.targetOre, placedByPlayer);
//				playerData.setTargetBlock(null);
//				this.targetOre = null;
//				resetBlockDamage();
//				
//			}
//			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RPGTools.getPlugin(), new Runnable() {
//			     public void run() {
//			          canMine = true;
//			     }
//			}, 1);
//			
//		}
//	}
//	
//	/**
//	 * Breaks the block as though the player mined it
//	 * @param player the player who is breaking
//	 * @param block the block to be broken
//	 * @param ore the corresponding ore for the block
//	 */
//	private void breakBlock(Player player, Block block, Ore ore, boolean placedByPlayer) {
//		if (ore == null) {
//			return;
//		}
//		if (!ore.isMineable(playerData)) {
//			return;
//		}
//		// All checks complete, Pickaxe is safe.
//		
//		// Drops
//		ItemStack drop;
//		if(getPickaxeAutosmelt()) {
//			drop = ore.getAutosmeltDrop();
//		} else if(getPickaxeSilktouch()) {
//			drop = ore.getSilkDrop();
//		} else {
//			drop = ore.getDrop();
//		}
//		int dropAmount = 1;
//		if (!placedByPlayer) {
//			dropAmount = PickaxeUtil.rollDropAmount(playerData);
//			drop.setAmount(dropAmount);
//		}
//		
//		if (!placedByPlayer) {
//			int expAmount = (int) PickaxeUtil.getFortuneExpMultiplier(dropAmount) * ore.getRandomExp();
//			modifyPickaxeExp(expAmount, dropAmount);
//		}
//		if (!getPickaxeShouldProtect()) {
//			modifyPickaxeCurrentDurability(-1);
//		} else {
//			player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.06F, 1.7F);
//		}
//		
//		// This lib is broken rn
////		ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0.1F, 0.1F, 0.1F, 0.1F, 20);
//		
//		double x, y, z;
//		x = block.getLocation().getX();
//		y = block.getLocation().getY();
//		z = block.getLocation().getZ();
//		
//		if (dropAmount == 3) {
//			// Triple effect
//			player.getWorld().spawnParticle(Particle.DRAGON_BREATH,  x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
//		} else if (dropAmount == 2) {
//			// Double effect
//			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x + 0.5, y + 0.5, z + 0.5, 8, 0F, 0F, 0F, 0.01);
//		}
//		
//		if (getPickaxeAutosmelt()) {
//			player.getWorld().spawnParticle(Particle.FLAME,  x + 0.5, y + 0.5, z + 0.5, 4, 0F, 0F, 0F, 0.009);
//		}
//		
//		player.getWorld().spawnParticle(Particle.BLOCK_CRACK, x + 0.5, y + 0.5, z + 0.5, 35, 0F, 0F, 0F, 1, new MaterialData(block.getType()));
//		player.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1F, 0.8F);
//		block.setType(Material.AIR);
//		block.getLocation().getWorld().dropItemNaturally(block.getLocation(), drop);
//		if (!placedByPlayer) {
//			int randExp = ore.getRandomVanillaExp();
//			if(!getPickaxeSilktouch() && randExp != 0) {
//				((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(randExp);
//			}
//		}
//		
//		Pickaxe.updatePickaxeInInventory(player);
//	}
//
//	public void resetBlockDamage() {
//		playerData.setBlockDamage(0);
//	}
//	
//	/**
//	 * General Pickaxe Information (Durability, Exp, Level, SP) test
//	 */
//	
//	public int getPickaxeMaxDurability() {
//		return Pickaxe.convertPickLevelToDurability(getPickaxeLevel());
//	}
//	
//	public void setPickaxeCurrentDurability(int value) {
//		if (value < 0) {
//			value = 0;
//		}
//		config.set("Pickaxe.CurrentDurability", value);
//		playerData.saveConfig();
//		Pickaxe.updatePickaxeInInventory(playerData.getPlayer());
//	}
//	
//	public void modifyPickaxeCurrentDurability(int value) {
//		if (!config.contains("Pickaxe.CurrentDurability")) {
//			setPickaxeCurrentDurability(getPickaxeMaxDurability());
//		}
//		setPickaxeCurrentDurability(getPickaxeCurrentDurability() + value);
//	}
//	
//	public int getPickaxeCurrentDurability() {
//		if (!config.contains("Pickaxe.CurrentDurability")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.CurrentDurability");
//	}
//	
//	public void setPickaxeLevel(int value) {
//		config.set("Pickaxe.Level", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeLevel(int value) {
//		if (!config.contains("Pickaxe.Level")) {
//			setPickaxeLevel(1);
//		}
//		setPickaxeLevel(getPickaxeLevel() + value);
//	}
//	
//	public int getPickaxeLevel() {
//		if (!config.contains("Pickaxe.Level")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Level");
//	}
//	
//	public void setPickaxeSP(int value) {
//		config.set("Pickaxe.SP", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeSP(int value) {
//		if (!config.contains("Pickaxe.SP")) {
//			setPickaxeSP(1);
//		}
//		setPickaxeSP(getPickaxeSP() + value);
//	}
//	
//	public int getPickaxeSP() {
//		if (!config.contains("Pickaxe.SP")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.SP");
//	}
//	
//	/*
//	 * Pickaxe experience
//	 */
//	
//	public void setPickaxeExp(int value) {
//		if (getPickaxeLevel() >= Pickaxe.MAX_LEVEL) {
//			return;
//		}
//		config.set("Pickaxe.Exp", value);
//		if (value >= getPickaxeExpToNextLevel()) { // Level Up
//			levelUpPickaxe();
//		}
//		playerData.saveConfig();
//	}
//
//	public int getPickaxeExp() {
//		if (!config.contains("Pickaxe.Exp")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Exp");
//	}
//
//	public void modifyPickaxeExp(int value, int dropAmount) {
//		if (getPickaxeSilktouch() && value > YAMLFile.PICKAXECONFIG.getConfig().getInt("Silktouch.MaxExpGain")) {
//			return;
//		}
//		if (!config.contains("Pickaxe.Exp")) {
//			setPickaxeExp(1);
//		}
//		setPickaxeExp(getPickaxeExp() + value);
//		
//		
//		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
//        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
//        
//        /*
//         * Credit to https://github.com/connorlinfoot/ActionBarAPI by ConnorLinfoot
//         */
//        
//        try {
//         String messageToSend = ChatColor.GRAY + "" + ChatColor.BOLD + "+" + ChatColor.GRAY + value + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + " Pickaxe EXP";
//         if (dropAmount == 2) {
//        	 messageToSend = ChatColor.BOLD + "" + ChatColor.WHITE + "★ " + messageToSend + ChatColor.BOLD + "" + ChatColor.WHITE + " ★";
//         } else if (dropAmount == 3) {
//        	 messageToSend = ChatColor.BOLD + "" + ChatColor.AQUA + "★ " + messageToSend + ChatColor.BOLD + "" + ChatColor.AQUA + " ★";
//         }
//		 Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
//         Object craftPlayer = craftPlayerClass.cast(playerData.getPlayer());
//         Object ppoc;
//         Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
//         Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
//         Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
//         Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
//         Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
//         Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
//         Object chatMessageType = null;
//         for (Object obj : chatMessageTypes) {
//             if (obj.toString().equals("GAME_INFO")) {
//                 chatMessageType = obj;
//             }
//         }
//         Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(messageToSend);
//         ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
//         Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
//         Object h = m1.invoke(craftPlayer);
//         Field f1 = h.getClass().getDeclaredField("playerConnection");
//         Object pc = f1.get(h);
//         Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
//         m5.invoke(pc, ppoc);
//        } catch (Exception ex) {
//        	ex.printStackTrace();
//        }
//	}
//	
//	public int getPickaxeExpToNextLevel() {
//		int level = getPickaxeLevel();
//		return (int) (Pickaxe.DIFFICULTY_MULTIPLIER * Math.pow(level, 1.8) + (4 * level) + 40);
//	}
//
//	public void levelUpPickaxe() {
//		if (getPickaxeLevel() >= Pickaxe.MAX_LEVEL) {
//			return;
//		}
//
//		int newExp = getPickaxeExp() - getPickaxeExpToNextLevel();
//		modifyPickaxeLevel(1);
//		modifyPickaxeSP(YAMLFile.PICKAXECONFIG.getConfig().getInt("SPPerLevel"));
//		GeneralUtil.playLevelSound(playerData.getPlayer());
//		playerData.getPlayer().sendMessage(" ");
//		playerData.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Pickaxe LEVEL " + getPickaxeLevel());
//		playerData.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Pickaxe Stat Points" + ChatColor.AQUA + " + " + YAMLFile.PICKAXECONFIG.getConfig().getInt("SPPerLevel"));
//		playerData.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + getPickaxeExpToNextLevel() + " xp to lvl " + (getPickaxeLevel() + 1));
//		playerData.getPlayer().sendMessage(" ");
//		playerData.saveConfig();
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				setPickaxeExp(newExp);
//				Pickaxe.updatePickaxeInInventory(playerData.getPlayer());
//			}
//		}.runTaskLater(RPGTools.getPlugin(), 1L);
//	}
//
//	/*
//	 * Pickaxe stats and special abilities
//	 */
//	
//	public void setPickaxeSpeed(int value) {
//		config.set( "Pickaxe.Speed", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeSpeed(int value) {
//		if (!config.contains("Pickaxe.Speed")) {
//			setPickaxeSpeed(1);
//		}
//		setPickaxeSpeed(getPickaxeSpeed() + value);
//	}
//	
//	public int getPickaxeSpeed() {
//		if (!config.contains("Pickaxe.Speed")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Speed");
//	}
//	
//	public double getPickaxeTotalSpeed() {
//		return PickaxeUtil.convertPickSpeedToDamagePerTick(getPickaxeSpeed());
//	}
//	
//	public void setPickaxeFortune(int value) {
//		config.set( "Pickaxe.Fortune", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeFortune(int value) {
//		if (!config.contains("Pickaxe.Fortune")) {
//			setPickaxeFortune(1);
//		}
//		setPickaxeFortune(getPickaxeFortune() + value);
//	}
//	
//	public int getPickaxeFortune() {
//		if (!config.contains("Pickaxe.Fortune")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Fortune");
//	}
//	
//	public DropChances getPickaxeFortuneDrop() {
//		return PickaxeUtil.convertPickaxeLuckLevelToPickaxeDropChances(getPickaxeFortune());
//	}
//	
//	public void setPickaxeAutoregen(int value) {
//		config.set( "Pickaxe.Autoregen", value);
//		playerData.saveConfig();
//		refreshAutoRegenTimer();
//	}
//	
//	public void modifyPickaxeAutoregen(int value) {
//		if (!config.contains("Pickaxe.Autoregen")) {
//			setPickaxeAutoregen(1);
//		}
//		setPickaxeAutoregen(getPickaxeAutoregen() + value);
//		refreshAutoRegenTimer();
//	}
//	
//	public int getPickaxeAutoregen() {
//		if (!config.contains("Pickaxe.Autoregen")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Autoregen");
//	}
//	
//	public void refreshAutoRegenTimer() {
//		if (autoRegenTimer != null) {
//			autoRegenTimer.cancelTask();
//		}
//		autoRegenTimer = new AutoRegen(playerData, YAMLFile.PICKAXECONFIG);
//	}
//	
//	public AutoRegen getAutoRegenClass(boolean refresh) {
//		if (refresh) {
//			refreshAutoRegenTimer();
//		}
//		return autoRegenTimer;
//	}
//	
//	public void setPickaxeReinforced(int value) {
//		config.set( "Pickaxe.Reinforced", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeReinforced(int value) {
//		if (!config.contains("Pickaxe.Reinforced")) {
//			setPickaxeReinforced(1);
//		}
//		setPickaxeReinforced(getPickaxeReinforced() + value);
//	}
//	
//	public int getPickaxeReinforced() {
//		if (!config.contains("Pickaxe.Reinforced")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Reinforced");
//	}
//	
//	public boolean getPickaxeShouldProtect() {
//		return reinforced.shouldProtectTool(getPickaxeReinforced());
//	}
//	
//	public void setPickaxeKnockback(int value) {
//		config.set( "Pickaxe.Knockback", value);
//		playerData.saveConfig();
//	}
//	
//	public void modifyPickaxeKnockback(int value) {
//		if (!config.contains("Pickaxe.Knockback")) {
//			setPickaxeKnockback(1);
//		}
//		setPickaxeKnockback(getPickaxeKnockback() + value);
//	}
//	
//	public int getPickaxeKnockback() {
//		if (!config.contains("Pickaxe.Knockback")) {
//			return 1;
//		}
//		return config.getInt("Pickaxe.Knockback");
//	}
//	
//	public void setPickaxeAutosmelt(boolean value) {
//		config.set( "Pickaxe.Special.Autosmelt.Toggle", value);
//		playerData.saveConfig();
//	}
//	
//	public void refreshPickaxeAutosmelt() {
//		if (!config.contains("Pickaxe.Special.Autosmelt.Toggle")) {
//			setPickaxeAutosmelt(false);
//		}
//	}
//	
//	public boolean getPickaxeAutosmelt() {
//		if (!config.contains("Pickaxe.Special.Autosmelt.Toggle")) {
//			return false;
//		}
//		return config.getBoolean("Pickaxe.Special.Autosmelt.Toggle");
//	}
//	
//	public boolean getPickaxeAutosmeltUnlocked() {
//		if (!config.contains("Pickaxe.Special.Autosmelt.Unlocked")) {
//			return false;
//		}
//		return config.getBoolean("Pickaxe.Special.Autosmelt.Unlocked");
//	}
//	
//	public void setPickaxeAutosmeltUnlocked(boolean value) {
//		config.set("Pickaxe.Special.Autosmelt.Unlocked", value);
//		playerData.saveConfig();
//	}
//	
//	public void refreshPickaxeAutosmeltUnlocked() {
//		if (!config.contains("Pickaxe.Special.Autosmelt.Unlocked")) {
//			setPickaxeAutosmeltUnlocked(false);
//		}
//	}
//	
//	public void setPickaxeSilktouch(boolean value) {
//		config.set( "Pickaxe.Special.Silktouch.Toggle", value);
//		playerData.saveConfig();
//	}
//	
//	public void refreshPickaxeSilktouch() {
//		if (!config.contains("Pickaxe.Special.Silktouch.Toggle")) {
//			setPickaxeSilktouch(false);
//		}
//	}
//	
//	public boolean getPickaxeSilktouch() {
//		if (!config.contains("Pickaxe.Special.Silktouch.Toggle")) {
//			return false;
//		}
//		return config.getBoolean("Pickaxe.Special.Silktouch.Toggle");
//	}
//	
//	public boolean getPickaxeSilktouchUnlocked() {
//		if (!config.contains("Pickaxe.Special.Silktouch.Unlocked")) {
//			return false;
//		}
//		return config.getBoolean("Pickaxe.Special.Silktouch.Unlocked");
//	}
//	
//	public void setPickaxeSilktouchUnlocked(boolean value) {
//		config.set("Pickaxe.Special.Silktouch.Unlocked", value);
//		playerData.saveConfig();
//	}
//	
//	public void refreshPickaxeSilktouchUnlocked() {
//		if (!config.contains("Pickaxe.Special.Silktouch.Unlocked")) {
//			setPickaxeSilktouchUnlocked(false);
//		}
//	}
//	
//	public static int getMaxSpeed() {
//		return YAMLFile.PICKAXECONFIG.getConfig().getInt("Speed.MaxLevel");
//	}
//	
//	public boolean isMaxSpeed() {
//		return getPickaxeSpeed() == getMaxSpeed();
//	}
//	
//	public static int getMaxFortune() {
//		return YAMLFile.PICKAXECONFIG.getConfig().getInt("Fortune.MaxLevel");
//	}
//	
//	public boolean isMaxFortune() {
//		return getPickaxeFortune() == getMaxFortune();
//	}
//	
//	public static int getMaxAutoregen() {
//		return YAMLFile.PICKAXECONFIG.getConfig().getInt("Autoregen.MaxLevel");
//	}
//	
//	public boolean isMaxAutoregen() {
//		return getPickaxeAutoregen() == getMaxAutoregen();
//	}
//	
//	public static int getMaxReinforced() {
//		return YAMLFile.PICKAXECONFIG.getConfig().getInt("Reinforcement.MaxLevel");
//	}
//	
//	public boolean isMaxReinforced() {
//		return getPickaxeReinforced() == getMaxReinforced();
//	}
//	
//	public boolean isBroken() {
//		return getPickaxeCurrentDurability() <= 0;
//	}
//
//}