package sabar.shouldersurfing;

import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-27
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
	
	//documented changes in the base files follow
	
    /************************************************************
	 *   class:  net.minecraft.client.renderer.EntityRenderer
	 *  method:  renderWorld()
	 *   after:  ActiveRenderInfo.updateRenderInfo
	 *   notes:  The code needs to be applied when the transformation matrixes
	 *           are correct, which is after the camera is set and before the
	 *           openGL context switches to 2D projections.
	 * 

    if(sabar.shouldersurfing.ShoulderHelper.rayTraceHit != null){
    	sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS = sabar.shouldersurfing.VectorConverter.get2DFrom3D(
    			(float)(sabar.shouldersurfing.ShoulderHelper.rayTraceHit.xCoord), 
    			(float)(sabar.shouldersurfing.ShoulderHelper.rayTraceHit.yCoord), 
    			(float)(sabar.shouldersurfing.ShoulderHelper.rayTraceHit.zCoord));
    	
    	sabar.shouldersurfing.ShoulderHelper.rayTraceHit = null;
    }
    
    */
	
	
	/************************************************************
	 *   class:  net.minecraft.client.renderer.EntityRenderer
	 *  method:  orientCamera()
	 *   after:  var13 = var2.rotationPitch;
	 *   notes:  This code should run when the third person view has been confirmed.
	 *           The variables may not stay the same!
	 * 

    if (this.mc.gameSettings.thirdPersonView == 2)
    {
        var13 += 180.0F;
    } else {
        //rotate the camera
        var28 += sabar.shouldersurfing.ShoulderHelper.SHOULDER_ROTATION;
        
        //change the camera distance
        var27 *= sabar.shouldersurfing.ShoulderHelper.SHOULDER_ZOOM_MOD;	
    }
    
    */
	
	
	/************************************************************
	 *   class:  net.minecraft.client.gui.GuiIngame
	 *  method:  renderGameOverlay()
	 *   after:  The first time the /gui/icons.png texture is bound for the cross-hairs
	 * 

    if (this.mc.gameSettings.thirdPersonView > 0){
    	if (this.mc.gameSettings.thirdPersonView == 2){
    		//don't bother drawing the cross-hairs from behind
    	}else{
    		if(sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS != null){
        		this.drawTexturedModalRect((int)(sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS.x)/var5.getScaleFactor()-7, (int)(sabar.shouldersurfing.ShoulderHelper.PROJECTED_COORDS.y)/var5.getScaleFactor()-7, 0, 0, 16, 16);
    		} else if(ShoulderHelper.TRACE_TO_HORIZON_LAST_RESORT){
    			this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
            }
    	}
    }else{
    	this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
    }
    
    */
}
