package com.teamderpy.shouldersurfing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
//import java.util.logging.Logger;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
//import net.minecraft.src.ModLoader;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.teamderpy.shouldersurfing.asm.ShoulderTransformations;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.ModContainer;




//import net.minecraftforge.fml.client.registry.KeyBindingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import net.minecraftforge.fml.common.event.FMLLoadEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//import net.minecraftforge.fml.common.registry.TickRegistry;

import net.minecraftforge.fml.relauncher.Side;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.9
 * @since       2013-11-18
 */
@Mod(modid="shouldersurfing", name="ShoulderSurfing", version="1.0")
public class ShoulderSurfing  {	
	@Mod.Instance("shouldersurfing")
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
	 * Our configuration file
	 */
    public static Configuration config;
    
	/**
	 * Whether or not this mod is enabled
	 */
    private static boolean enabled = true;
    
	/**
	 * The forge logger
	 */
    public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//load the last good configuration
    	config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
    	readConfig();
    }
    
    
    @EventHandler
    public void load(FMLInitializationEvent event){		
    	//load the keyboard bindings
    	kbh = ShoulderKeybindings.registerKeybindings();
    	FMLCommonHandler.instance().bus().register(kbh);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	if(logger == null)logger = LogManager.getLogger("ShoulderSurfing");
    	int expMods = ShoulderTransformations.CODE_MODIFICATIONS;
    	
    	if(ShoulderTransformations.modifications != expMods){
    		logger.fatal("Only found " + ShoulderTransformations.modifications + " code injections, but expected " + expMods);
    		logger.fatal("ShoulderSurfing should be disabled!");
    	} else {
    		logger.info("Loaded " + ShoulderTransformations.modifications + " code injections, ShoulderSurfing good to go!");
    	}
    	
    	//create mc pointer
    	ShoulderLoader sl = new ShoulderLoader();
    	sl.load();
    	
    	//create tick handler
    	st = new ShoulderTickHandler();
    	FMLCommonHandler.instance().bus().register(st);
    	//TickRegistry.registerTickHandler(st, Side.CLIENT);
    	
    	//register other events
    	MinecraftForge.EVENT_BUS.register(new ShoulderEvents());
    }

 

   
    
	/**
	 * Reads the config file.  If it does not exist it is created.  If properties
	 * do not exist, they are created.
	 */
    public static void readConfig(){
    	ShoulderSettings.IS_DYNAMIC_CROSSHAIR_ENABLED = config.get(Configuration.CATEGORY_GENERAL, "isCrosshairDynamic"    , ShoulderSettings.IS_DYNAMIC_CROSSHAIR_ENABLED, "If enabled, then the crosshair moves around to line up with the block you are facing.").getBoolean(ShoulderSettings.IS_DYNAMIC_CROSSHAIR_ENABLED);
		ShoulderCamera.SHOULDER_ROTATION     = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationOffset", ShoulderCamera.SHOULDER_ROTATION, "Third person camera rotation").getDouble((double)ShoulderCamera.SHOULDER_ROTATION);
		ShoulderCamera.SHOULDER_ZOOM_MOD     = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomOffset"    , ShoulderCamera.SHOULDER_ZOOM_MOD, "Third person camera zoom").getDouble((double)ShoulderCamera.SHOULDER_ZOOM_MOD);
		ShoulderSettings.IS_ROTATION_UNLIMITED = config.get(Configuration.CATEGORY_GENERAL, "isRotationUnlimited"    , ShoulderSettings.IS_ROTATION_UNLIMITED, "Whether or not rotation adjustment has limits").getBoolean(ShoulderSettings.IS_ROTATION_UNLIMITED);
		ShoulderSettings.ROTATION_MAXIMUM      = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationMaximum"    , ShoulderSettings.ROTATION_MAXIMUM, "If rotation is limited this is the maximum amount").getDouble((double)ShoulderSettings.ROTATION_MAXIMUM);
		ShoulderSettings.ROTATION_MINIMUM      = (float) config.get(Configuration.CATEGORY_GENERAL, "rotationMinimum"    , ShoulderSettings.ROTATION_MINIMUM, "If rotation is limited this is the minimum amount").getDouble((double)ShoulderSettings.ROTATION_MINIMUM);
		ShoulderSettings.IS_ZOOM_UNLIMITED     = config.get(Configuration.CATEGORY_GENERAL, "isZoomUnlimited"    , ShoulderSettings.IS_ZOOM_UNLIMITED, "Whether or not zoom adjustment has limits").getBoolean(ShoulderSettings.IS_ZOOM_UNLIMITED);
		ShoulderSettings.ZOOM_MAXIMUM          = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomMaximum"    , ShoulderSettings.ZOOM_MAXIMUM, "If zoom is limited this is the maximum amount").getDouble((double)ShoulderSettings.ZOOM_MAXIMUM);
		ShoulderSettings.ZOOM_MINIMUM          = (float) config.get(Configuration.CATEGORY_GENERAL, "zoomMinimum"    , ShoulderSettings.ZOOM_MINIMUM, "If zoom is limited this is the minimum amount").getDouble((double)ShoulderSettings.ZOOM_MINIMUM);
		ShoulderSettings.TRACE_TO_HORIZON_LAST_RESORT  = config.get(Configuration.CATEGORY_GENERAL, "alwaysHaveCrosshair"    , ShoulderSettings.TRACE_TO_HORIZON_LAST_RESORT, "Whether or not to show a crosshair in the center of the screen if nothing is in range of you").getBoolean(ShoulderSettings.TRACE_TO_HORIZON_LAST_RESORT);
		ShoulderSettings.USE_CUSTOM_RAYTRACE_DISTANCE  = config.get(Configuration.CATEGORY_GENERAL, "showCrosshairFarther"    , ShoulderSettings.USE_CUSTOM_RAYTRACE_DISTANCE, "Whether or not to show the crosshairs farther than normal").getBoolean(ShoulderSettings.USE_CUSTOM_RAYTRACE_DISTANCE);
		ShoulderSettings.HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA  = config.get(Configuration.CATEGORY_GENERAL, "keepCameraOutOfHead"    , ShoulderSettings.HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA, "Whether or not to hide the player model if the camera gets too close to it").getBoolean(ShoulderSettings.HIDE_PLAYER_IF_TOO_CLOSE_TO_CAMERA);
		ShoulderSettings.CROSSHAIR_COLOR_BOX_3RD  = config.get(Configuration.CATEGORY_GENERAL, "ThirdPersonCrosshairColor"    , ShoulderSettings.CROSSHAIR_COLOR_BOX_3RD, "Third person box color").getBoolean(ShoulderSettings.CROSSHAIR_COLOR_BOX_3RD);

		
    	
    	config.save();
    }
    
	/**
	 * Saves the config with our current camera values
	 */
    public static void writeConfig(){
    	config.get(Configuration.CATEGORY_GENERAL, "rotationOffset", ShoulderCamera.SHOULDER_ROTATION, "Third person camera rotation").set(Float.toString(ShoulderCamera.SHOULDER_ROTATION));
    	config.get(Configuration.CATEGORY_GENERAL, "zoomOffset"    , ShoulderCamera.SHOULDER_ZOOM_MOD, "Third person camera zoom").set(Float.toString(ShoulderCamera.SHOULDER_ZOOM_MOD));
    	
    	config.save();
    }
}