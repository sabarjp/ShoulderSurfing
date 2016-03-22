package com.teamderpy.shouldersurfing;

import net.minecraft.client.Minecraft;

public class ShoulderLoader {
	/**
	 * Pointer to Minecraft instance to get some player properties, etc
	 */
    public static Minecraft mc;
	
	public void load(){
		ShoulderLoader.mc = Minecraft.getMinecraft();
	}
}
