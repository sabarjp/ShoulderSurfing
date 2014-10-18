package com.teamderpy.shouldersurfing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.teamderpy.shouldersurfing.asm.ShoulderTransformations;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.6
 * @since       2013-05-05
 */
public class ShoulderSurfing extends DummyModContainer{	
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
    public static Configuration config;
    
	/**
	 * Whether or not this mod is enabled
	 */
    private static boolean enabled = true;
    
	/**
	 * The forge logger
	 */
    public static Logger logger;
	
	public ShoulderSurfing(){
        super(new ModMetadata());
        /* ModMetadata is the same as mcmod.info */
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList(new String[] { "Joshua Powers" });
        myMeta.description = "Changes the vanilla third person camera to an over-the-shoulder camera, as made popular by Capcom's \"Resident Evil 4\". " +
        					 "Includes keybindings for adjusting the camera, along with various settings in the config file.  Must be installed as a Forge " +
        					 "coremod.  Code injection may fail if using mods that mess with the base files.";
        myMeta.modId = "ShoulderSurfing";
        myMeta.version = "1.4.0";
        myMeta.name = "ShoulderSurfing";
        myMeta.url = "teamderpy.com";
        
        if(logger == null){
			logger = Logger.getLogger("ShoulderSurfing");
			logger.setParent(FMLLog.getLogger());
        }
	}
	
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
	    bus.register(this);
	    return enabled;
    }
    
    @Subscribe
    public void postInit(FMLPostInitializationEvent event){
    	int expMods = ShoulderTransformations.CODE_MODIFICATIONS;
    	
    	if(ShoulderTransformations.modifications != expMods){
    		logger.severe("Only found " + ShoulderTransformations.modifications + " code injections, but expected " + expMods);
    		logger.severe("ShoulderSurfing should be disabled!");
    	} else {
    		logger.info("Loaded " + ShoulderTransformations.modifications + " code injections, ShoulderSurfing good to go!");
    	}
    	
    	//create mc pointer
    	ShoulderLoader sl = new ShoulderLoader();
    	sl.load();
    	
    	//create tick handler
    	st = new ShoulderTickHandler();
    	TickRegistry.registerTickHandler(st, Side.CLIENT);
    	
    	//register other events
    	MinecraftForge.EVENT_BUS.register(new ShoulderEvents());
    }	
    
	@Subscribe
    public void init(FMLInitializationEvent event){		
    	//load the keyboard bindings
    	kbh = ShoulderKeybindings.registerKeybindings();
    }

	@Subscribe
    public void preInit(FMLPreInitializationEvent event) {
    	//load the last good configuration
    	config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
    	readConfig();
    }
    
	/**
	 * Reads the config file.  If it does not exist it is created.  If properties
	 * do not exist, they are created.
	 */
    public static void readConfig(){
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