package com.teamderpy.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

import com.teamderpy.shouldersurfing.ShoulderKeybindings;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.1
 * @since       2013-01-14
 */
public class ShoulderKeyHandler {	
	
		ShoulderKeyHandler(){}
		
		@SubscribeEvent
		public void tickStart(ClientTickEvent event) {
		/* minecraft ought to be running and with no gui up */
		if((event.side==Side.CLIENT)&&(event.phase==Phase.START)){
			if(ShoulderLoader.mc != null && ShoulderLoader.mc.currentScreen == null){
				if (ShoulderLoader.mc.gameSettings.thirdPersonView == 1){
					if(ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_LEFT.getIsKeyPressed()){
						ShoulderCamera.adjustCameraLeft();
					}
					else if(ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_RIGHT.getIsKeyPressed()){
						ShoulderCamera.adjustCameraRight();
					}
					else if(ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_IN.getIsKeyPressed()){
						ShoulderCamera.adjustCameraIn();
					}
					else if(ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_OUT.getIsKeyPressed()){
						ShoulderCamera.adjustCameraOut();
					}
				
					ShoulderSurfing.writeConfig();
				}
			}
		}
	}

}
