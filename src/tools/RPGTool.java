package tools;

import java.util.List;

import net.Vala.config.ToolData;
import net.Vala.config.YAMLFile;
import net.Vala.util.EnchantGlow;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class RPGTool extends ItemStack {
	
	protected YAMLFile YML;
	protected Material[] MATERIAL = new Material[5];
	
	protected ToolData data;
	protected ItemMeta itemMeta;
	
	public RPGTool(ToolData data, YAMLFile yamlEnum) {
		super(Material.WOOD_SWORD, 1, (short) 0);
		this.YML = yamlEnum;
		this.itemMeta = this.getItemMeta();
		this.data = data;
		this.initializeMaterials();
		hideItemFlags();
		refreshItem(this);
	}
	
	protected abstract void initializeMaterials();
	
	protected abstract short getMaxDurabilityOfMaterial(Material material);
	
	protected abstract List<String> getLore();
	
	protected abstract String getDisplayName();
	
	protected abstract boolean shouldGlow();
	
	public abstract void updateInInventory(Player player);
	
	public Material getTypeForLevel(int level) {
		if (level >= YML.getConfig().getInt("UnlockLevels.Diamond")) {
			return MATERIAL[4];
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Gold")) {
			return MATERIAL[3];
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Iron")) {
			return MATERIAL[2];
		} else if (level >= YML.getConfig().getInt("UnlockLevels.Stone")) {
			
			return MATERIAL[1];
		}
		return MATERIAL[0];
	}
	
	public void refreshItem(ItemStack item) {
		if (YML == null) {
			return;
		}
		refreshType(item);
		refreshLore(item);
		refreshDisplayName(item);
		refreshDurability(item);
		refreshGlow(item);
	}
	
	public void refreshType(ItemStack item) {
		item.setType(getTypeForLevel(data.getLevel()));
	}
	
	public void refreshDisplayName(ItemStack item) {
		itemMeta.setDisplayName(getDisplayName());
		item.setItemMeta(itemMeta);
	}
	
	public void refreshLore(ItemStack item) {
		itemMeta.setLore(getLore());
		item.setItemMeta(itemMeta);
	}
	
	public void refreshDurability(ItemStack item) {
		float trueDuraPercent = (float) data.getCurrentDurability() / (float) data.getMaxDurability();
		short maxDura = getMaxDurabilityOfMaterial(super.getType());
		item.setDurability((short) (maxDura - (trueDuraPercent * (float) maxDura)));
	}
	
	public void refreshGlow(ItemStack item) {
		if (shouldGlow()) {
			EnchantGlow.addGlow(item);
			return;
		}
		EnchantGlow.removeGlow(item);
	}
	
	public void hideItemFlags() {
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		super.setItemMeta(itemMeta);
	}
	
	public boolean giveNew(Player player) {
		refreshItem(this);
		return player.getInventory().addItem(this).isEmpty();
	}

}
