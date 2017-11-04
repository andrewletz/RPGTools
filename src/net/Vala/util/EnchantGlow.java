package net.Vala.util;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class EnchantGlow extends EnchantmentWrapper
{
	
	private static Enchantment glow;
	
	public EnchantGlow( int id )
	{
		super(id);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item)
	{
		return true;
	}
	
	@Override
	public boolean conflictsWith(Enchantment other)
	{
		return false;
	}
	
	@Override
	public EnchantmentTarget getItemTarget()
	{
		return null;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 10;
	}
	
	@Override
	public String getName()
	{
		return "Glow";
	}
	
	@Override
	public int getStartLevel()
	{
		return 1;
	}
	
	public static Enchantment getGlow()
	{
		if ( glow != null )
			return glow;
		
		try
		{
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null , true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		glow = new EnchantGlow(255);
		Enchantment.registerEnchantment(glow);
		return glow;
	}
	
	public static void addGlow(ItemStack item)
	{
		Enchantment glow = getGlow();
		
		item.addEnchantment(glow , 1);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isGlowing(ItemStack item) {
		if (item.getEnchantments().containsKey(Enchantment.getById(255))) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void removeGlow(ItemStack item) {
		item.removeEnchantment(Enchantment.getById(255));
	}
	
}
