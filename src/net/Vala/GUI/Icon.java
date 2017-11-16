package net.Vala.GUI;

import java.util.ArrayList;
import java.util.List;

import net.Vala.util.EnchantGlow;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Icon extends ItemStack {
	
	private List<String> lore = new ArrayList<String>();
	private ItemMeta itemMeta;
	
	public Icon(Material material) {
		super(material, 1);
		this.itemMeta = this.getItemMeta();
	}
	
	public Icon(Material material, short damage) {
		super(material, 1, damage);
		this.itemMeta = this.getItemMeta();
	}
	
	public Icon(Material material, byte data) {
		super(material, 1, data);
		this.itemMeta = this.getItemMeta();
	}

	public void setDisplayName(String displayName) {
		itemMeta.setDisplayName(displayName);
		super.setItemMeta(itemMeta);
	}
	
	public void addLore(String line) {
		lore.add(line);
		itemMeta.setLore(lore);
		super.setItemMeta(itemMeta);
	}
	
	public void setLore(List<String> newLore) {
		lore = newLore;
		itemMeta.setLore(lore);
		super.setItemMeta(itemMeta);
	}
	
	
	/**
	 * Add's the enchantment effect to the icon. Make sure to use this method only after all 
	 * other meddling is done! (after adding all lore, setting all names).
	 * @param
	 * @return
	 */
	public void addGlow() {
		EnchantGlow.addGlow(this);
	}
	
	public void hideItemFlags() {
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		super.setItemMeta(itemMeta);
	}
	
}
