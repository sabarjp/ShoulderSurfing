package sabar.shouldersurfing;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.client.settings.KeyBinding;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-27
 */
public class ShoulderKeyHandler extends KeyHandler{	
	public ShoulderKeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		return "Shoulder surfing";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (ShoulderSurfing.mc.gameSettings.thirdPersonView == 1 ){
			if(kb == ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_LEFT){
				ShoulderHelper.adjustCameraLeft();
			}
			else if(kb == ShoulderKeybindings.KEYBIND_ROTATE_CAMERA_RIGHT){
				ShoulderHelper.adjustCameraRight();
			}
			else if(kb == ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_IN){
				ShoulderHelper.adjustCameraIn();
			}
			else if(kb == ShoulderKeybindings.KEYBIND_ZOOM_CAMERA_OUT){
				ShoulderHelper.adjustCameraOut();
			}
			
			ShoulderSurfing.writeConfig();
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		//do nothing
	}

	@Override
	public EnumSet<TickType> ticks() {
		/**
		 * Allows keybind events to be listened by the client
		 */
		return EnumSet.of(TickType.CLIENT);
	}
}
