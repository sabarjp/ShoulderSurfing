package com.teamderpy.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

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
					if(ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_LEFT.isKeyDown()){
						ShoulderCamera.adjustCameraLeft();
					}
					else if(ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_RIGHT.isKeyDown()){
						ShoulderCamera.adjustCameraRight();
					}
					else if(ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_IN.isKeyDown()){
						ShoulderCamera.adjustCameraIn();
					}
					else if(ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_OUT.isKeyDown()){
						ShoulderCamera.adjustCameraOut();
					}
				
					ShoulderSurfing.writeConfig();
				}
			}
		}
	}

}
