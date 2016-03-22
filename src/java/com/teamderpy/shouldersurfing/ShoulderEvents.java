package com.teamderpy.shouldersurfing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import com.google.common.eventbus.Subscribe;
import com.teamderpy.shouldersurfing.asm.InjectionDelegation;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.2
 * @since       2013-11-18
 */
public class ShoulderEvents {
    /**
     * Holds the last coordinate drawing position
     */
    private static float lastX = 0.0F;
    private static float lastY = 0.0F;
	
    @SubscribeEvent
    public void postRenderCrosshairs(RenderGameOverlayEvent.Post event){		
		if(event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS){

		}
    }
	
    @SubscribeEvent
	public void preRenderPlayer(RenderPlayerEvent.Pre event){
		if(ShoulderRenderBin.skipPlayerRender){
			if(event.isCancelable()){
				event.setCanceled(true);
			}
		}
	}
	
    @SubscribeEvent
    public void preRenderCrosshairs(RenderGameOverlayEvent.Pre event){		
		if(event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS){
			if(!ShoulderSettings.IS_DYNAMIC_CROSSHAIR_ENABLED){
				//note that the regular method will not be short circuited
				return;
			}
			
			float tick = event.partialTicks;
			GuiIngame g  = ShoulderLoader.mc.ingameGUI;
			
			ScaledResolution sr = new ScaledResolution(ShoulderLoader.mc, ShoulderLoader.mc.displayWidth, ShoulderLoader.mc.displayHeight);
			
			if(ShoulderLoader.mc.gameSettings.thirdPersonView == 0){
				lastX = sr.getScaledWidth()*sr.getScaleFactor()/2;;
				lastY = sr.getScaledHeight()*sr.getScaleFactor()/2;
				
				bind(Gui.icons);
				g.drawTexturedModalRect(sr.getScaledWidth()/2-7, 
									    sr.getScaledHeight()/2-7, 
									    0, 0, 16, 16);
			}
			else if(ShoulderLoader.mc.gameSettings.thirdPersonView == 1){
				if(ShoulderRenderBin.projectedVector != null){
					GL11.glEnable(GL11.GL_BLEND);
					bind(Gui.icons);
					
					if(ShoulderRenderBin.rayTraceInReach){
						//GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
						GL14.glBlendColor(0.2f, 0.2f, 1.0f, 1.0f);
						GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_CONSTANT_COLOR);
					} else {
						GL14.glBlendColor(1.0f, 0.2f, 0.2f, 1.0f);
						GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_CONSTANT_COLOR);
					}
					
					float diffX = (ShoulderRenderBin.projectedVector.x - lastX) * tick;
					float diffY = (ShoulderRenderBin.projectedVector.y - lastY) * tick;
					
					g.drawTexturedModalRect((int)((lastX + diffX)/sr.getScaleFactor()-7), (int)((lastY + diffY)/sr.getScaleFactor()-7), 0, 0, 16, 16);
			
					lastX = lastX + diffX;
					lastY = lastY + diffY;
					
					GL11.glDisable(GL11.GL_BLEND);
					

				} else if(ShoulderSettings.TRACE_TO_HORIZON_LAST_RESORT){
					bind(Gui.icons);
					GL11.glEnable(GL11.GL_BLEND);
					GL14.glBlendColor(1.0f, 0.2f, 0.2f, 1.0f);
					GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_CONSTANT_COLOR);
					
					float diffX = (sr.getScaledWidth()*sr.getScaleFactor()/2 - lastX) * tick;
					float diffY = (sr.getScaledHeight()*sr.getScaleFactor()/2 - lastY) * tick;
					
					g.drawTexturedModalRect((int)((lastX + diffX)/sr.getScaleFactor()-7), (int)((lastY + diffY)/sr.getScaleFactor()-7), 0, 0, 16, 16);
			
					lastX = lastX + diffX;
					lastY = lastY + diffY;

					GL11.glDisable(GL11.GL_BLEND);
				}
			}
			
			/** SHORT-CIRCUIT THE RENDER */
			if (event.isCancelable())
			{
				event.setCanceled(true);
			}

		}
    }
	
	/**
	 * Binds a texture
	 * 
	 * @param res the resource to bind
	 */
	private void bind(ResourceLocation res)
    {
		ShoulderLoader.mc.getTextureManager().bindTexture(res);
		//ShoulderLoader.mc.func_110434_K().func_110577_a(res);
    }
}
