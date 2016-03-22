package com.teamderpy.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.teamderpy.shouldersurfing.asm.InjectionDelegation;
import com.teamderpy.shouldersurfing.math.RayTracer;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

import net.minecraft.util.Vec3;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;


public class ShoulderTickHandler {
		
	@SubscribeEvent
	public void renderTickStart(RenderTickEvent event) {
		if((event.side==Side.CLIENT)&&(event.phase==Phase.START)&&(event.type==Type.RENDER)){
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
}
