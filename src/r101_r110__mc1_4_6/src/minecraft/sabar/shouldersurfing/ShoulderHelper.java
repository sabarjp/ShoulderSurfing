package sabar.shouldersurfing;

import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.1
 * @since       2012-12-30
 */
public class ShoulderHelper {
	/**
	 * The number of degrees to rotate the camera
	 */
	public static float SHOULDER_ROTATION = -22.0F;
	
	/**
	 * How much the camera view distance should change
	 */
	public static float SHOULDER_ZOOM_MOD = 0.70F;
	
	/**
	 * Whether or not zooming is unlimited
	 */
	public static boolean IS_ZOOM_UNLIMITED     = false;
	public static float   ZOOM_MINIMUM          = 0.3F;
	public static float   ZOOM_MAXIMUM          = 2.0F;
	
	/**
	 * Whether or not rotation is unlimited
	 */
	public static boolean IS_ROTATION_UNLIMITED = false;
	public static float   ROTATION_MINIMUM      = -60.0F;
	public static float   ROTATION_MAXIMUM      =  60.0F;
	
	/**
	 * Whether or not to render the player on the next pass, used for when
	 * the camera is inside the player.
	 */
	public static boolean SKIP_PLAYER_RENDER                  = false;
	public static boolean HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA  = true;
	
	/**
	 * Distance to raytrace to find the player's line of eye sight
	 * and whether or not we use this custom distance.  If we are not
	 * using the distance here, then the player's block break length
	 * is used.  
	 */
	public static boolean USE_CUSTOM_RAYTRACE_DISTANCE = false;
	public static float RAYTRACE_DISTANCE = 50.0F;
	
	/**
	 * If the ray trace hits nothing, assume it hit the horizon
	 */
	public static boolean TRACE_TO_HORIZON_LAST_RESORT  = false;

	/**
	 * The last thing our line-of-sight collided with
	 */
    public static Vec3 rayTraceHit = null;
    
	/**
	 * Holds the last projected coordinates
	 */
    public static Vector2f PROJECTED_COORDS = null;
    
    /**
	 * Whether or not to switch to third person on the next render
	 */
    public static boolean SWITCH_TO_THIRD = false;
	
	public static void adjustCameraLeft(){
		if(IS_ROTATION_UNLIMITED || SHOULDER_ZOOM_MOD < ROTATION_MAXIMUM)
			SHOULDER_ROTATION += 0.5F;
	}
	
	public static void adjustCameraRight(){
		if(IS_ROTATION_UNLIMITED || SHOULDER_ZOOM_MOD > ROTATION_MINIMUM)
			SHOULDER_ROTATION -= 0.5F;
	}
	
	public static void adjustCameraIn(){
		if(IS_ZOOM_UNLIMITED || SHOULDER_ZOOM_MOD < ZOOM_MAXIMUM)
			SHOULDER_ZOOM_MOD += 0.01F;
	}

	public static void adjustCameraOut(){
		if(IS_ZOOM_UNLIMITED ||SHOULDER_ZOOM_MOD > ZOOM_MINIMUM)
			SHOULDER_ZOOM_MOD -= 0.01F;
	}
	
	/**
	 * Called by injected code to modify the camera rotation
	 */
	public static float getShoulderRotation(){
		if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 1){
			return SHOULDER_ROTATION;
		}
		
		return 0F;
	}
	
	/**
	 * Called by injected code to modify the camera zoom
	 */
	public static float getShoulderZoomMod(){
		if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 1){
			return SHOULDER_ZOOM_MOD;
		}
		
		return 1.0F;
	}
	
	/**
	 * Called by injected code to project a raytrace hit to the screen
	 */
	public static void calculateRayTraceProjection(){
        if(rayTraceHit != null){
        	PROJECTED_COORDS = sabar.shouldersurfing.VectorConverter.project2D(
        			(float)(rayTraceHit.xCoord), 
        			(float)(rayTraceHit.yCoord), 
        			(float)(rayTraceHit.zCoord));
        	
        	rayTraceHit = null;
        }
	}
	
	/**
	 * Called by injected code to draw the crosshairs on the screen
	 */
	public static void drawCrosshairs(net.minecraft.client.gui.Gui g){
		ScaledResolution sr = new ScaledResolution(ShoulderSurfing.mc.gameSettings, ShoulderSurfing.mc.displayWidth, ShoulderSurfing.mc.displayHeight);
		
		if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 0){
			g.drawTexturedModalRect(sr.getScaledWidth()/2-7, 
								    sr.getScaledHeight()/2-7, 
								    0, 0, 16, 16);
		}
		else if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 1){
			if(PROJECTED_COORDS != null){
				g.drawTexturedModalRect((int)(sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS.x)/sr.getScaleFactor()-7, 
										(int)(sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS.y)/sr.getScaleFactor()-7, 
										0, 0, 16, 16);
			}
		}
	}
	
	public static void verifyReverseBlockDist(double distance){
		if(distance < 0.80 && HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA){
			SKIP_PLAYER_RENDER = true;
		}
	}
}
