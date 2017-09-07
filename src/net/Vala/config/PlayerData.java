package net.Vala.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import net.Vala.general.RPGTools;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
	
	public static final Map<UUID, PlayerData> CONNECTED_PLAYERS = new HashMap<>();
	private static File playersFolder = null;
	
	/*
	 * Per instance player data variables test
	 */
	
	private FileConfiguration config = null;
	private File configFile = null;
	
	private Player player;
	private Block targetBlock;
	private float blockDamage;
	
	private ShovelData shovelData;
	private PickaxeData pickaxeData;

	private PlayerData(Player player) {
		this.player = player;
		this.reloadConfig();
		this.pickaxeData = new PickaxeData(this);
		this.shovelData = new ShovelData(this);
	}
	
	/*
	 * Static (per server) methods
	 */
	
	public static PlayerData getData(Player player) {
		if (player == null) {
			return null;
		}
		PlayerData playerData = CONNECTED_PLAYERS.get(player.getUniqueId());
		if (playerData == null) {
			playerData = new PlayerData(player);
			CONNECTED_PLAYERS.put(player.getUniqueId(), playerData);
		}
		return playerData;
	}
	
	public static void prepareFiles() {
		makeFolders();
	}

	public static void makeFolders() {
		playersFolder = new File(RPGTools.getPlugin().getDataFolder(), "Players");
		playersFolder.mkdirs();
	}
	
	public static File getPlayersFolder() {
		return playersFolder;
	}
	
	/*
	 * Per config file methods
	 */
	
	public void reloadConfig() {
		if (configFile == null) {
			configFile = new File(playersFolder, "(" + player.getDisplayName() +  ") " + player.getUniqueId() + ".yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		return config;
	}
	
	public void saveConfig() {
		if (config == null || configFile == null) {
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			RPGTools.getPlugin().getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
		}
	}
	
	/*
	 * Per player methods
	 */
	
	public ShovelData getShovelData() {
		return shovelData;
	}
	
	public PickaxeData getPickaxeData() {
		return pickaxeData;
	}
	
	public void refreshConfig() {
		pickaxeData.refreshConfig();
		shovelData.refreshConfig();
	}
	
	public void refreshAutoRegenTimers() {
		pickaxeData.refreshAutoRegenTimer();
		shovelData.refreshAutoRegenTimer();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Block getTargetBlock() {
		return targetBlock;
	}
	
	public void setBlockDamage(float val) {
		this.blockDamage = val;
	}
	
	public void modifyBlockDamage(double val) {
		this.blockDamage += val;
	}
	
	public float getBlockDamage() {
		return blockDamage;
	}
	
	public void setTargetBlock(Block block) {
		this.targetBlock = block;
	}

}
