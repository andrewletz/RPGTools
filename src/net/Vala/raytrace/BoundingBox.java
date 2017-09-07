package net.Vala.raytrace;

import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.WorldServer;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.util.Vector;

public class BoundingBox {
	
	/*
	 * Credit to https://www.spigotmc.org/members/cjp10.257255/
	 * */

    //min and max points of hit box
    public Vector max;
    public Vector min;

    BoundingBox(Vector min, Vector max) {
        this.max = max;
        this.min = min;
    }
    
    public BoundingBox(Block block) {
    	BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
        WorldServer world = ((CraftWorld) block.getWorld()).getHandle();
        AxisAlignedBB box = world.getType(pos).e(world, pos);
        min = new Vector(pos.getX() + box.a, pos.getY() + box.b, pos.getZ() + box.c);
        max = new Vector(pos.getX() + box.d, pos.getY() + box.e, pos.getZ() + box.f);
    }

    public Vector midPoint(){
        return max.clone().add(min).multiply(0.5);
    }

}
