package com.teamderpy.shouldersurfing.math;

import java.util.List;

import com.teamderpy.shouldersurfing.ShoulderSettings;
import com.teamderpy.shouldersurfing.ShoulderSurfing;
import com.teamderpy.shouldersurfing.renderer.ShoulderRenderBin;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.1
 * @since       2013-01-12
 */
public final class RayTracer {
	private RayTracer(){}
	
    public static void traceFromEyes(final float tick)
    {
    	ShoulderRenderBin.projectedVector = null;
    	
        if (ShoulderSurfing.mc.renderViewEntity != null)
        {
            if (ShoulderSurfing.mc.theWorld != null)
            {
            	if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 1){
            		double playerReach = 1D;

            		if(ShoulderSettings.USE_CUSTOM_RAYTRACE_DISTANCE){
            			playerReach = com.teamderpy.shouldersurfing.ShoulderSettings.RAYTRACE_DISTANCE;
            		}else{
            			playerReach = (double)ShoulderSurfing.mc.playerController.getBlockReachDistance();
            		}
            		
            		//block collision
	                MovingObjectPosition omo = ShoulderSurfing.mc.renderViewEntity.rayTrace(playerReach, tick);
	                double blockDist = 0;

	                if(omo != null){
	                	ShoulderRenderBin.rayTraceHit = omo.hitVec;
	                	blockDist = omo.hitVec.distanceTo(ShoulderSurfing.mc.theWorld.getWorldVec3Pool().getVecFromPool(ShoulderSurfing.mc.renderViewEntity.posX, ShoulderSurfing.mc.renderViewEntity.posY, ShoulderSurfing.mc.renderViewEntity.posZ));
	                	//System.out.println("block dist: " + blockDist);
	                	if(blockDist <= (double)ShoulderSurfing.mc.playerController.getBlockReachDistance()){
	                		ShoulderRenderBin.rayTraceInReach = true;
	                	} else {
	                		ShoulderRenderBin.rayTraceInReach = false;
	                	}
	                }else{
	                	ShoulderRenderBin.rayTraceHit = null;
	                }
	                
	                
	                //entity collision
	                Vec3 renderViewPos = ShoulderSurfing.mc.renderViewEntity.getPosition(tick);
	
	                Vec3 sightVector = ShoulderSurfing.mc.renderViewEntity.getLook(tick);
	                Vec3 sightRay = renderViewPos.addVector(sightVector.xCoord * playerReach, sightVector.yCoord * playerReach, sightVector.zCoord * playerReach);
	
	                List entityList = ShoulderSurfing.mc.theWorld.getEntitiesWithinAABBExcludingEntity(ShoulderSurfing.mc.renderViewEntity, ShoulderSurfing.mc.renderViewEntity.boundingBox.addCoord(sightVector.xCoord * playerReach, sightVector.yCoord * playerReach, sightVector.zCoord * playerReach).expand(1.0D, 1.0D, 1.0D));
	
	                for (int i = 0; i < entityList.size(); ++i)
	                {
	                    Entity ent = (Entity)entityList.get(i);
	
	                    if (ent.canBeCollidedWith())
	                    {
	                        float collisionSize = ent.getCollisionBorderSize();
	                        AxisAlignedBB aabb = ent.boundingBox.expand((double)collisionSize, (double)collisionSize, (double)collisionSize);
	                        MovingObjectPosition potentialIntercept = aabb.calculateIntercept(renderViewPos, sightRay);
	                        
	                        if (potentialIntercept != null){
	                        	double entityDist = potentialIntercept.hitVec.distanceTo(ShoulderSurfing.mc.theWorld.getWorldVec3Pool().getVecFromPool(ShoulderSurfing.mc.renderViewEntity.posX, ShoulderSurfing.mc.renderViewEntity.posY, ShoulderSurfing.mc.renderViewEntity.posZ));
	                        	
	                        	if(entityDist < blockDist){
	                        		ShoulderRenderBin.rayTraceHit = potentialIntercept.hitVec;
		                        	
		                        	
		    	                	//System.out.println("entity dist: " + entityDist);
		                        	if(entityDist <= (double)ShoulderSurfing.mc.playerController.getBlockReachDistance()){
		                        		ShoulderRenderBin.rayTraceInReach = true;
		    	                	} else {
		    	                		ShoulderRenderBin.rayTraceInReach = false;
		    	                	}
	                        	}
	                        }
	                    }
	                }
            	}
            }
        }
    }
}
