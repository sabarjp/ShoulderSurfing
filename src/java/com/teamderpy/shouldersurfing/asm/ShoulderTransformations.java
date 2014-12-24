package com.teamderpy.shouldersurfing.asm;

import static org.objectweb.asm.Opcodes.*;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.teamderpy.shouldersurfing.ShoulderSurfing;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.5
 * @since       2013-11-17
 */
public class ShoulderTransformations implements IClassTransformer
{
    private final HashMap obfStrings;
    private final HashMap mcpStrings;
    public static final int CODE_MODIFICATIONS = 3;
    
    public static int modifications = 0;
    
    public ShoulderTransformations()
    {
        if(ShoulderSurfing.logger == null){
        	ShoulderSurfing.logger = LogManager.getLogger("ShoulderSurfing");
        	//ShoulderSurfing.logger.setParent(FMLLog.getLogger());
        }
    	
        obfStrings = new HashMap();
        mcpStrings = new HashMap();

        /* net.minecraft.client.renderer.EntityRenderer   orientCamera
         * MD: ban/g (F)V net/minecraft/src/EntityRenderer/func_78467_g (F)V
         */

        registerMapping("EntityRendererClass",     "net.minecraft.client.renderer.EntityRenderer", "bll");
        registerMapping("EntityRendererJavaClass", "net/minecraft/client/renderer/EntityRenderer", "bll");
        registerMapping("EntityLivingJavaClass",   "net/minecraft/entity/EntityLivingBase"       , "rh");
        registerMapping("EntityJavaClass",         "net/minecraft/entity/Entity"                 , "qn");
        registerMapping("orientCameraMethod",      "orientCamera"                                , "g");
        registerMapping("rotationYawField",        "rotationYaw"                                 , "z");
        registerMapping("rotationPitchField",      "rotationPitch"                               , "A");
        
        registerMapping("SHOULDER_ROTATIONField",  "SHOULDER_ROTATION"                           , "SHOULDER_ROTATION");
        registerMapping("SHOULDER_ZOOM_MODField",  "SHOULDER_ZOOM_MOD"                           , "SHOULDER_ZOOM_MOD");
        registerMapping("InjectionDelegationJavaClass", "com/teamderpy/shouldersurfing/asm/InjectionDelegation" , "com/teamderpy/shouldersurfing/asm/InjectionDelegation");
        registerMapping("ShoulderRenderBinJavaClass",   "com/teamderpy/shouldersurfing/renderer/ShoulderRenderBin" , "com/teamderpy/shouldersurfing/renderer/ShoulderRenderBin");
        
        /* net.minecraft.client.renderer.EntityRenderer   renderWorld
         * MD: ban/a (FJ)V net/minecraft/src/EntityRenderer/func_78471_a (FJ)V
         */
        
        registerMapping("renderWorldMethod",               "renderWorld"                                             , "a");
        registerMapping("clippingHelperImplJavaClass",     "net/minecraft/client/renderer/culling/ClippingHelperImpl", "bmo");
        registerMapping("clippingHelperJavaClass",         "net/minecraft/client/renderer/culling/ClippingHelper"    , "bmq");
        registerMapping("clippingHelperGetInstanceMethod", "getInstance"                                             , "a");
        
        /* net.minecraft.client.gui.GuiIngame             renderGameOverlay
         * M  D: atr/a (FZII)V net/minecraft/src/GuiIngame/func_73830_a (FZII)V
         */
        //registerMapping("GuiIngameClass",              "net.minecraft.client.gui.GuiIngame", "avj");
        //registerMapping("GuiIngameJavaClass",          "net/minecraft/client/gui/GuiIngame", "avj");
        //registerMapping("GuiJavaClass",                "net/minecraft/client/gui/Gui"      , "avk");
        //registerMapping("renderGameOverlayMethod",     "renderGameOverlay"                 , "a");
        //registerMapping("drawTexturedModalRectMethod", "drawTexturedModalRect"             , "b");
        
        /* net.minecraft.client.renderer.entity.RenderPlayer   renderPlayer
         */
        //registerMapping("RenderPlayerClass",           "net.minecraft.client.renderer.entity.RenderPlayer", "bhj");
        //registerMapping("RenderPlayerJavaClass",       "net/minecraft/client/renderer/entity/RenderPlayer", "bhj");
        //registerMapping("renderPlayerMethod",          "renderPlayer"                                     , "a");
        //registerMapping("EntityPlayerJavaClass",       "net/minecraft/entity/player/EntityPlayer"         , "uf");
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
    	//This lets us transform code whether or not it is obfuscated yet
        if (name.equals(obfStrings.get("EntityRendererClass")))
        {
        	ShoulderSurfing.logger.info("Injecting into obfuscated code - EntityRendererClass");
            return transformEntityRenderClass(bytes, obfStrings);
        }
        else if (name.equals(mcpStrings.get("EntityRendererClass")))
        {
        	ShoulderSurfing.logger.info("Injecting into non-obfuscated code - EntityRendererClass");
            return transformEntityRenderClass(bytes, mcpStrings);
        }
        
        
        return bytes;
    }
    
        
    private byte[] transformEntityRenderClass(byte[] bytes, HashMap hm)
    {
        ShoulderSurfing.logger.info("Attempting class transformation against EntityRender");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        //Find method
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(hm.get("orientCameraMethod")) && m.desc.equals("(F)V"))
            {
            	ShoulderSurfing.logger.info("Located method " + m.name + m.desc + ", locating signature");
                
                //Locate injection point, after the yaw and pitch fields in the camera function
				InsnList searchList = new InsnList();
				searchList.add(new VarInsnNode(ALOAD, 2));
				searchList.add(new FieldInsnNode(GETFIELD, (String) hm.get("EntityLivingJavaClass"), (String) hm.get("rotationYawField"), "F"));
				searchList.add(new VarInsnNode(FSTORE, 13));
				searchList.add(new VarInsnNode(ALOAD, 2));
				searchList.add(new FieldInsnNode(GETFIELD, (String) hm.get("EntityLivingJavaClass"), (String) hm.get("rotationPitchField"), "F"));
				searchList.add(new VarInsnNode(FSTORE, 12));
				
				
				int offset = ShoulderASMHelper.locateOffset(m.instructions, searchList);
				if(offset == -1){
					ShoulderSurfing.logger.fatal("Failed to locate first of two offsets in " + m.name + m.desc + "!  Is base file changed?");
					return bytes;
				} else {
					ShoulderSurfing.logger.info("Located offset @ " + offset);
					InsnList hackCode = new InsnList();
					hackCode.add(new VarInsnNode(FLOAD, 13));
					hackCode.add(new MethodInsnNode(INVOKESTATIC, (String) hm.get("InjectionDelegationJavaClass"), "getShoulderRotation", "()F"));
					hackCode.add(new InsnNode(FADD));
					hackCode.add(new VarInsnNode(FSTORE, 13));
					hackCode.add(new VarInsnNode(DLOAD, 10));
					hackCode.add(new MethodInsnNode(INVOKESTATIC, (String) hm.get("InjectionDelegationJavaClass"), "getShoulderZoomMod", "()F"));
					hackCode.add(new InsnNode(F2D));
					hackCode.add(new InsnNode(DMUL));
					hackCode.add(new VarInsnNode(DSTORE, 10));
					hackCode.add(new LabelNode(new Label()));
					m.instructions.insertBefore(m.instructions.get(offset+1), hackCode);
					ShoulderSurfing.logger.info("Injected code for camera orientation!");
					modifications++;
				}
				
				//Locate second injection point, after the reverse raytrace is performed
				searchList = new InsnList();
				searchList.add(new VarInsnNode(DSTORE, 25));
				searchList.add(new VarInsnNode(DLOAD, 25));
				
				offset = ShoulderASMHelper.locateOffset(m.instructions, searchList);
				if(offset == -1){
					ShoulderSurfing.logger.fatal("Failed to locate second of two offsets in " + m.name + m.desc + "!  Is base file changed?");
					return bytes;
				} else {
					ShoulderSurfing.logger.info("Located offset @ " + offset);
					InsnList hackCode = new InsnList();
					hackCode.add(new VarInsnNode(DLOAD, 25));
					hackCode.add(new MethodInsnNode(INVOKESTATIC, (String) hm.get("InjectionDelegationJavaClass"), "verifyReverseBlockDist", "(D)V"));
					hackCode.add(new LabelNode(new Label()));
					m.instructions.insertBefore(m.instructions.get(offset), hackCode);
					ShoulderSurfing.logger.info("Injected code for camera distance check!");
					modifications++;
				}
            } else if (m.name.equals(hm.get("renderWorldMethod")) && m.desc.equals("(FJ)V")){
            	ShoulderSurfing.logger.info("Located method " + m.name + m.desc + ", locating signature");
            	
                //Locate injection point, after the clipping helper returns an instance
				InsnList searchList = new InsnList();
				searchList.add(new MethodInsnNode(INVOKESTATIC , (String) hm.get("clippingHelperImplJavaClass"), (String) hm.get("clippingHelperGetInstanceMethod"), "()L" + (String) hm.get("clippingHelperJavaClass") + ";"));
				searchList.add(new InsnNode(POP));
				
				int offset = ShoulderASMHelper.locateOffset(m.instructions, searchList);
				if(offset == -1){
					ShoulderSurfing.logger.fatal("Failed to locate offset in " + m.name + m.desc + "!  Is base file changed?");
					return bytes;
				} else {
					ShoulderSurfing.logger.info("Located offset @ " + offset);
					InsnList hackCode = new InsnList();
					hackCode.add(new MethodInsnNode(INVOKESTATIC, (String) hm.get("InjectionDelegationJavaClass"), "calculateRayTraceProjection", "()V"));
					hackCode.add(new LabelNode(new Label()));
					m.instructions.insertBefore(m.instructions.get(offset+1), hackCode);
					ShoulderSurfing.logger.info("Injected code for ray trace projection!");
					modifications++;
				}
            }
        }
     
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private void registerMapping(String key, String normalValue, String obfuscatedValue){
    	mcpStrings.put(key, normalValue);
        obfStrings.put(key, obfuscatedValue);
    }
}
