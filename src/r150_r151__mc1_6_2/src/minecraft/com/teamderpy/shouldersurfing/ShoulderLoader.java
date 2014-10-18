package com.teamderpy.shouldersurfing;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public class ShoulderLoader {
	/**
	 * Pointer to Minecraft instance to get some player properties, etc
	 */
    public static Minecraft mc;
	
	public void load(){
		ShoulderLoader.mc = ModLoader.getMinecraftInstance();
	}
}
