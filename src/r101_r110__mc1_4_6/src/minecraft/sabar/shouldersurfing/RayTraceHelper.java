package sabar.shouldersurfing;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-27
 */
public class RayTraceHelper {
    public static void traceFromEyes(float size)
    {
        ShoulderHelper.PROJECTED_COORDS = null;
    	
        if (ShoulderSurfing.mc.renderViewEntity != null)
        {
            if (ShoulderSurfing.mc.theWorld != null)
            {
            	if(ShoulderSurfing.mc.gameSettings.thirdPersonView == 1){
            		double playerReach = 1D;

            		if(ShoulderHelper.USE_CUSTOM_RAYTRACE_DISTANCE){
            			playerReach = sabar.shouldersurfing.ShoulderHelper.RAYTRACE_DISTANCE;
            		}else{
            			playerReach = (double)ShoulderSurfing.mc.playerController.getBlockReachDistance();
            		}
            		
            		//block collision
	                MovingObjectPosition omo = ShoulderSurfing.mc.renderViewEntity.rayTrace(playerReach, size);

	                if(omo != null){
	                	sabar.shouldersurfing.ShoulderHelper.rayTraceHit = omo.hitVec;
	                }else{
	                	sabar.shouldersurfing.ShoulderHelper.rayTraceHit = null;
	                }
	                
	                
	                //entity collision
	                Vec3 renderViewPos = ShoulderSurfing.mc.renderViewEntity.getPosition(size);
	
	                Vec3 sightVector = ShoulderSurfing.mc.renderViewEntity.getLook(size);
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
	                        	ShoulderHelper.rayTraceHit = potentialIntercept.hitVec;
	                        }
	                    }
	                }
            	}
            }
        }
    }
}
