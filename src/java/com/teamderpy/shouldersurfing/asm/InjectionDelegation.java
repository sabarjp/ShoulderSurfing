package com.teamderpy.shouldersurfing.asm;

import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.teamderpy.shouldersurfing.ShoulderLoader;
import com.teamderpy.shouldersurfing.ShoulderSettings;
import com.teamderpy.shouldersurfing.ShoulderCamera;
import com.teamderpy.shouldersurfing.ShoulderSurfing;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2013-01-14
 * 
 * Injected code is delegated here
 */
public final class InjectionDelegation {
	private InjectionDelegation(){}
	
	/**
	 * Called by injected code to modify the camera rotation
	 */
	public static float getShoulderRotation(){
		if(ShoulderLoader.mc.gameSettings.thirdPersonView == 1){
			return ShoulderCamera.SHOULDER_ROTATION;
		}
		
		return 0F;
	}
	
	/**
	 * Called by injected code to modify the camera zoom
	 */
	public static float getShoulderZoomMod(){
		if(ShoulderLoader.mc.gameSettings.thirdPersonView == 1){
			return ShoulderCamera.SHOULDER_ZOOM_MOD;
		}
		
		return 1.0F;
	}
	
	/**
	 * Called by injected code to project a raytrace hit to the screen
	 */
	public static void calculateRayTraceProjection(){
        if(ShoulderRenderBin.rayTraceHit != null){
        	ShoulderRenderBin.projectedVector = com.teamderpy.shouldersurfing.math.VectorConverter.project2D(
        			(float)(ShoulderRenderBin.rayTraceHit.xCoord), 
        			(float)(ShoulderRenderBin.rayTraceHit.yCoord), 
        			(float)(ShoulderRenderBin.rayTraceHit.zCoord));
        	
        	ShoulderRenderBin.rayTraceHit = null;
        }
	}
	
    /**
     * Holds the last coordinate drawing position
     */
	@Deprecated
    private static float lastX = 0.0F;
	@Deprecated
    private static float lastY = 0.0F;
	
	/**
	 * Called by injected code to draw the crosshairs on the screen
	 */
	@Deprecated
	public static void drawCrosshairs(net.minecraft.client.gui.Gui g, float tick){
		ScaledResolution sr = new ScaledResolution(ShoulderLoader.mc.gameSettings, ShoulderLoader.mc.displayWidth, ShoulderLoader.mc.displayHeight);
		
		if(ShoulderLoader.mc.gameSettings.thirdPersonView == 0){
			lastX = sr.getScaledWidth()*sr.getScaleFactor()/2;;
			lastY = sr.getScaledHeight()*sr.getScaleFactor()/2;
			
			g.drawTexturedModalRect(sr.getScaledWidth()/2-7, 
								    sr.getScaledHeight()/2-7, 
								    0, 0, 16, 16);
		}
		else if(ShoulderLoader.mc.gameSettings.thirdPersonView == 1){
			if(ShoulderRenderBin.projectedVector != null){
				GL11.glEnable(GL11.GL_BLEND);
				
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
	}
	
	/**
	 * Called by injected code to determine whether the camera is too close to the player
	 */
	public static void verifyReverseBlockDist(double distance){
		if(distance < 0.80 && ShoulderSettings.HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA){
			ShoulderRenderBin.skipPlayerRender = true;
		}
	}
}
