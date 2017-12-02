package tools;

import java.util.ArrayList;
import java.util.List;

import net.Vala.config.ToolData;
import net.Vala.config.YAMLFile;
import net.Vala.pickaxe.PickaxeUtil;
import net.Vala.util.GeneralUtil;
import net.Vala.config.PickaxeData;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RPGPickaxe extends RPGTool {
	
	public static final Material[] PICKAXE_MATERIAL = new Material[] { Material.WOOD_PICKAXE, Material.STONE_PICKAXE,
		Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE };
	
	public RPGPickaxe(PickaxeData data) {
		super(data, YAMLFile.PICKAXECONFIG);
		this.data = (PickaxeData) data;
	}

	@Override
	protected void initializeMaterials() {
		super.MATERIAL = PICKAXE_MATERIAL;
	}

	@Override
	protected short getMaxDurabilityOfMaterial(Material material) {
		switch (material) {
			case WOOD_PICKAXE:
				return 59;
			case STONE_PICKAXE:
				return 131;
			case IRON_PICKAXE:
				return 250;
			case GOLD_PICKAXE:
				return 32;
			case DIAMOND_PICKAXE:
				return 1561;
			default:
				return 1;
		}
	}
	@Override
	protected String getDisplayName() {
		return ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + data.getPlayer().getName() + "'s Pickaxe";
	}

	@Override
	protected List<String> getLore() {
		List<String> pickaxeLore = new ArrayList<>();
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "LVL: " + ChatColor.WHITE + data.getLevel() + ChatColor.AQUA + " [" + data.getSP() + " SP available]");
		pickaxeLore.add("");
		pickaxeLore.add(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "STATS");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Speed: " + ChatColor.WHITE + data.getSpeed());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Fortune: " + ChatColor.WHITE + data.getFortune());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Regen: " + ChatColor.WHITE + data.getAutoregen());
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Reinforce: " + ChatColor.WHITE + data.getReinforced());
//		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "KNOCKBACK: " + ChatColor.WHITE + data.getKnockback());
		if (((PickaxeData) data).getAutosmelt()) {
			pickaxeLore.add(" ");
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Autosmelt Active" + ChatColor.BOLD + " ]");
		}
		if (((PickaxeData) data).getSilktouch()) {
			if (!((PickaxeData) data).getAutosmelt()) {
				pickaxeLore.add(" ");
			}
			pickaxeLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "[ " + ChatColor.BLUE + "Silktouch Active" + ChatColor.BOLD + " ]");
		}
		pickaxeLore.add(" ");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "CURRENT DURA.:" + ChatColor.WHITE + " " + data.getCurrentDurability() + " uses " + ChatColor.BLUE + "("
				+ String.format("%.0f",
						100D * ((double) data.getCurrentDurability() / (double) data.getMaxDurability()))
				+ "%)");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "EXP: " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "["
				+ GeneralUtil.formatPercentageBar("||||||||||||||||||||||||||||||||||||||||||||||||||", ChatColor.DARK_PURPLE,
						ChatColor.GRAY, (double) data.getExp() / (double) data.getExpToNextLevel())
				+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "]");
		pickaxeLore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "        " + ChatColor.UNDERLINE + "(" + data.getExp() + " XP / " + data.getExpToNextLevel() + " XP)");
		return pickaxeLore;
	}

	@Override
	protected boolean shouldGlow() {
		if(!((PickaxeData) data).getAutosmelt() && !((PickaxeData) data).getSilktouch()) {
			return false;
		}
		return true;
	}

	@Override
	public void updateInInventory(Player player) {
		ItemStack pickaxe = PickaxeUtil.getPickaxeInInventory(player);
		if (pickaxe == null) {
			return;
		}
		System.out.println("Updating in inventory");
		refreshItem();
		pickaxe = this;
	}

}
