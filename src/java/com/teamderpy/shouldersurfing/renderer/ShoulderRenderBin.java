package com.teamderpy.shouldersurfing.renderer;

import net.minecraft.util.Vec3;

import org.lwjgl.util.vector.Vector2f;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2013-01-14
 * 
 * Storage of various variables the injected code will use
 */
public final class ShoulderRenderBin {
	private ShoulderRenderBin(){}
	
	/**
	 * The last thing our line-of-sight collided with
	 */
    public static Vec3 rayTraceHit = null;
    public static boolean rayTraceInReach = false;
    
	/**
	 * Holds the last projected coordinates
	 */
    public static Vector2f projectedVector = null;
    
	/**
	 * Whether or not to render the player on the next pass, used for when
	 * the camera is inside the player.
	 */
	public static boolean skipPlayerRender = false;
}
