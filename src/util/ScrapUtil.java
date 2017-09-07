package util;

import org.bukkit.Material;

public class ScrapUtil {

    public static Material toolMatToScrapMat(Material mat) {
    	switch(mat) {
    		case WOOD_PICKAXE:
    		case WOOD_SPADE:
    		case WOOD_AXE:
    			return Material.WOOD;
    		case STONE_PICKAXE:
    		case STONE_SPADE:
    		case STONE_AXE:
    			return Material.COBBLESTONE;
    		case IRON_PICKAXE:
    		case IRON_SPADE:
    		case IRON_AXE:
    			return Material.IRON_INGOT;
    		case GOLD_PICKAXE:
    		case GOLD_SPADE:
    		case GOLD_AXE:
    			return Material.GOLD_INGOT;
    		case DIAMOND_PICKAXE:
    		case DIAMOND_SPADE:
    		case DIAMOND_AXE:
    			return Material.DIAMOND;
    		default:
    			return null;
    	}
    }
    
    public static String matToString(Material mat) {
    	switch(mat) {
    	case WOOD:
    		return "Wood";
    	case COBBLESTONE:
    		return "Cobble";
    	case IRON_INGOT:
    		return "Iron";
    	case GOLD_INGOT:
    		return "Gold";
    	case DIAMOND:
    		return "Diamond";
    	default:
    		return null;
    	}
    }
    
    public int diamondScrapMatToValue(Material mat) {
    	return 1;
    }
	
}
