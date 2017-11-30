package tools;

import java.util.List;

import net.Vala.config.ToolData;
import net.Vala.config.YAMLFile;
import net.Vala.general.Logger;
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
		initializeMaterialTiers();
		hideItemFlags();
		refreshItem();
	}
	
	protected abstract void initializeMaterialTiers();
	
	protected abstract short getMaxDurabilityOfMaterial(Material material);
	
	protected abstract List<String> getLore();
	
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
	
	public void refreshItem() {
		if (YML == null) {
			return;
		}
		refreshType();
		refreshLore();
		refreshDurability();
		refreshGlow();
	}
	
	public void refreshType() {
		super.setType(getTypeForLevel(data.getLevel()));
	}
	
	public void refreshLore() {
		itemMeta.setLore(getLore());
		super.setItemMeta(itemMeta);
	}
	
	public void refreshDurability() {
		float trueDuraPercent = (float) data.getCurrentDurability() / (float) data.getMaxDurability();
		short maxDura = getMaxDurabilityOfMaterial(super.getType());
		super.setDurability((short) (maxDura - (trueDuraPercent * (float) maxDura)));
	}
	
	public void refreshGlow() {
		if (shouldGlow()) {
			EnchantGlow.addGlow(this);
			return;
		}
		EnchantGlow.removeGlow(this);
	}
	
	public void hideItemFlags() {
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		super.setItemMeta(itemMeta);
	}

}
