package sabar.shouldersurfing;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.Configuration;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(modid="ShoulderSurfing", name="ShoulderSurfing", version="1.0.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-24
 */
public class ShoulderSurfing {
        @Instance("ShoulderSurfing")
        public static ShoulderSurfing instance;
        
    	/**
    	 * Our keyboard input handler for camera rotations
    	 */
        private ShoulderKeyHandler kbh;
        
    	/**
    	 * Our keyboard input handler for camera rotations
    	 */
        private ShoulderTickHandler st;
        
    	/**
    	 * Pointer to Minecraft instance to get some player properties, etc
    	 */
        public static Minecraft mc;
        
    	/**
    	 * Our configuration file
    	 */
        public Configuration config;

        @PreInit
        public void preInit(FMLPreInitializationEvent event) {
        	//load the last good configuration
        	config = new Configuration(event.getSuggestedConfigurationFile());
    		config.load();
        	readConfig();
        }
       
        @Init
        public void load(FMLInitializationEvent event) {
        	//load the keyboard bindings
        	kbh = ShoulderKeybindings.registerKeybindings();
        	
        	//create mc pointer
        	mc = ModLoader.getMinecraftInstance();
        	
        	//create tick handler
        	st = new ShoulderTickHandler();
        	TickRegistry.registerTickHandler(st, Side.CLIENT);
        }
       
        @PostInit
        public void postInit(FMLPostInitializationEvent event) {
        	//do nothing
        }
        
    	/**
    	 * Reads the config file.  If it does not exist it is created.  If properties
    	 * do not exist, they are created.
    	 */
        public void readConfig(){
    		ShoulderHelper.SHOULDER_ROTATION     = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationOffset", ShoulderHelper.SHOULDER_ROTATION, "Third person camera rotation").getDouble((double)ShoulderHelper.SHOULDER_ROTATION);
    		ShoulderHelper.SHOULDER_ZOOM_MOD     = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomOffset"    , ShoulderHelper.SHOULDER_ZOOM_MOD, "Third person camera zoom").getDouble((double)ShoulderHelper.SHOULDER_ZOOM_MOD);
    		ShoulderHelper.IS_ROTATION_UNLIMITED = config.get(Configuration.CATEGORY_GENERAL, "isRotationUnlimited"    , ShoulderHelper.IS_ROTATION_UNLIMITED, "Whether or not rotation adjustment has limits").getBoolean(ShoulderHelper.IS_ROTATION_UNLIMITED);
    		ShoulderHelper.ROTATION_MAXIMUM      = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationMaximum"    , ShoulderHelper.ROTATION_MAXIMUM, "If rotation is limited this is the maximum amount").getDouble((double)ShoulderHelper.ROTATION_MAXIMUM);
    		ShoulderHelper.ROTATION_MINIMUM      = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationMinimum"    , ShoulderHelper.ROTATION_MINIMUM, "If rotation is limited this is the minimum amount").getDouble((double)ShoulderHelper.ROTATION_MINIMUM);
    		ShoulderHelper.IS_ZOOM_UNLIMITED     = config.get(Configuration.CATEGORY_GENERAL, "isZoomUnlimited"    , ShoulderHelper.IS_ZOOM_UNLIMITED, "Whether or not zoom adjustment has limits").getBoolean(ShoulderHelper.IS_ZOOM_UNLIMITED);
    		ShoulderHelper.ZOOM_MAXIMUM          = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomMaximum"    , ShoulderHelper.ZOOM_MAXIMUM, "If zoom is limited this is the maximum amount").getDouble((double)ShoulderHelper.ZOOM_MAXIMUM);
    		ShoulderHelper.ZOOM_MINIMUM          = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomMinimum"    , ShoulderHelper.ZOOM_MINIMUM, "If zoom is limited this is the minimum amount").getDouble((double)ShoulderHelper.ZOOM_MINIMUM);
    		ShoulderHelper.TRACE_TO_HORIZON_LAST_RESORT  = config.get(Configuration.CATEGORY_GENERAL, "alwaysHaveCrosshair"    , ShoulderHelper.TRACE_TO_HORIZON_LAST_RESORT, "Whether or not to show a crosshair in the center of the screen if nothing is in range of you").getBoolean(ShoulderHelper.TRACE_TO_HORIZON_LAST_RESORT);
    		ShoulderHelper.USE_CUSTOM_RAYTRACE_DISTANCE  = config.get(Configuration.CATEGORY_GENERAL, "showCrosshairFarther"    , ShoulderHelper.USE_CUSTOM_RAYTRACE_DISTANCE, "Whether or not to show the crosshairs farther than normal").getBoolean(ShoulderHelper.USE_CUSTOM_RAYTRACE_DISTANCE);
        	
        	config.save();
        }
        
    	/**
    	 * Saves the config with our current camera values
    	 */
        public void writeConfig(){
        	config.get(Configuration.CATEGORY_GENERAL, "rotationOffset", ShoulderHelper.SHOULDER_ROTATION, "Third person camera rotation").value = Float.toString(ShoulderHelper.SHOULDER_ROTATION);
        	config.get(Configuration.CATEGORY_GENERAL, "zoomOffset"    , ShoulderHelper.SHOULDER_ZOOM_MOD, "Third person camera zoom").value = Float.toString(ShoulderHelper.SHOULDER_ZOOM_MOD);
        	
        	config.save();
        }
}