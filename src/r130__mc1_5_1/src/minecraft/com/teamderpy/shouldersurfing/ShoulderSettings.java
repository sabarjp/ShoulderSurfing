package com.teamderpy.shouldersurfing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.3
 * @since       2013-01-14
 */
public class ShoulderSettings {	
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
	

	public static boolean HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA  = true;
	
	/**
	 * Distance to raytrace to find the player's line of eye sight
	 * and whether or not we use this custom distance.  If we are not
	 * using the distance here, then the player's block break length
	 * is used.  
	 */
	public static boolean USE_CUSTOM_RAYTRACE_DISTANCE = true;
	public static float RAYTRACE_DISTANCE = 400.0F;
	
	/**
	 * If the ray trace hits nothing, assume it hit the horizon
	 */
	public static boolean TRACE_TO_HORIZON_LAST_RESORT  = true;
}
