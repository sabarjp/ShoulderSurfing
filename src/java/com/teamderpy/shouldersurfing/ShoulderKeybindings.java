package com.teamderpy.shouldersurfing;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import cpw.mods.fml.client.registry.ClientRegistry;
//import cpw.mods.fml.client.registry.KeyBindingRegistry;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-27
 */
public class ShoulderKeybindings {
	/**
	 * Minecraft keybindings for camera functions
	 */
	public static final KeyBinding KEYBIND_ROTATE_CAMERA_LEFT  = new KeyBinding("ShoulderSurfing.key.cameraMoveLeft"   , Keyboard.KEY_J, "ShoulderSurfing.keyCategory");
	public static final KeyBinding KEYBIND_ROTATE_CAMERA_RIGHT = new KeyBinding("ShoulderSurfing.key.cameraMoveRight"  , Keyboard.KEY_L, "ShoulderSurfing.keyCategory"); 
	public static final KeyBinding KEYBIND_ZOOM_CAMERA_OUT     = new KeyBinding("ShoulderSurfing.key.cameraMoveCloser" , Keyboard.KEY_I, "ShoulderSurfing.keyCategory"); 
	public static final KeyBinding KEYBIND_ZOOM_CAMERA_IN      = new KeyBinding("ShoulderSurfing.key.cameraMoveFarther", Keyboard.KEY_K, "ShoulderSurfing.keyCategory"); 
	
	private static KeyBinding[] keyBindings = {
			KEYBIND_ROTATE_CAMERA_LEFT, 
			KEYBIND_ROTATE_CAMERA_RIGHT,
			KEYBIND_ZOOM_CAMERA_OUT,
			KEYBIND_ZOOM_CAMERA_IN};
	
	/**
	 * Registers the ShoulderSurfing keybindings with minecraft.
	 * Should only be called once!
	 * 
	 * @return Returns a {@link ShoulderKeyHandler} 
	 */
	public static ShoulderKeyHandler registerKeybindings(){
		ShoulderKeyHandler skh = new ShoulderKeyHandler();
		for(KeyBinding keybinding:keyBindings){
		ClientRegistry.registerKeyBinding(keybinding);
		}
		return skh;
	}
}
