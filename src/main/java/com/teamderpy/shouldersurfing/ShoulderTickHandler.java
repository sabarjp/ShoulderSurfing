package com.teamderpy.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.teamderpy.shouldersurfing.asm.InjectionDelegation;
import com.teamderpy.shouldersurfing.math.RayTracer;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;


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
					double x = ShoulderLoader.mc.thePlayer.posX;
					double y = ShoulderLoader.mc.thePlayer.posY;
					double z = ShoulderLoader.mc.thePlayer.posZ;
					ShoulderRenderBin.rayTraceHit = ShoulderRenderBin.rayTraceHit.addVector(-x,-y,-z);
				}
			}
			
		}
	}
}
