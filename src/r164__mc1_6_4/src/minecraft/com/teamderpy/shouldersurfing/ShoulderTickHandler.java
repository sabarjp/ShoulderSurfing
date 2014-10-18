package com.teamderpy.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.teamderpy.shouldersurfing.asm.InjectionDelegation;
import com.teamderpy.shouldersurfing.math.RayTracer;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

import net.minecraft.util.Vec3;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ShoulderTickHandler implements ITickHandler{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.RENDER))){
			ShoulderRenderBin.skipPlayerRender = false;
			
			//attempt to ray trace
			RayTracer.traceFromEyes(1.0F);
			
			if(ShoulderRenderBin.rayTraceHit != null){
				if(ShoulderLoader.mc.thePlayer != null){
					//calculate the difference
					ShoulderRenderBin.rayTraceHit.xCoord -= ShoulderLoader.mc.thePlayer.posX;
					ShoulderRenderBin.rayTraceHit.yCoord -= ShoulderLoader.mc.thePlayer.posY;
					ShoulderRenderBin.rayTraceHit.zCoord -= ShoulderLoader.mc.thePlayer.posZ;
				}
			}
			
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Shoulder Surfing";
	}

}
