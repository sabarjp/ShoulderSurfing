package com.teamderpy.shouldersurfing;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import cpw.mods.fml.client.registry.KeyBindingRegistry;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-27
 */
public class ShoulderKeybindings {
	/**
	 * Minecraft keybindings for camera functions
	 */
	public static final KeyBinding KEYBIND_ROTATE_CAMERA_LEFT  = new KeyBinding("Camera adj left"    , Keyboard.KEY_J);
	public static final KeyBinding KEYBIND_ROTATE_CAMERA_RIGHT = new KeyBinding("Camera adj right"   , Keyboard.KEY_L); 
	public static final KeyBinding KEYBIND_ZOOM_CAMERA_OUT     = new KeyBinding("Camera adj closer"  , Keyboard.KEY_I); 
	public static final KeyBinding KEYBIND_ZOOM_CAMERA_IN      = new KeyBinding("Camera adj farther" , Keyboard.KEY_K); 
	
	private static KeyBinding[] keyBindings = {
			KEYBIND_ROTATE_CAMERA_LEFT, 
			KEYBIND_ROTATE_CAMERA_RIGHT,
			KEYBIND_ZOOM_CAMERA_OUT,
			KEYBIND_ZOOM_CAMERA_IN};
	
	/**
	 * Whether or not each keybinding repeats
	 */
	private static boolean[] isKeyBindingRepeat = {
			true, 
			true, 
			true, 
			true};
	
	/**
	 * Registers the ShoulderSurfing keybindings with minecraft.
	 * Should only be called once!
	 * 
	 * @return Returns a {@link ShoulderKeyHandler} 
	 */
	public static ShoulderKeyHandler registerKeybindings(){
		ShoulderKeyHandler skh = new ShoulderKeyHandler(keyBindings, isKeyBindingRepeat);
		KeyBindingRegistry.registerKeyBinding(skh);
		return skh;
	}
}
