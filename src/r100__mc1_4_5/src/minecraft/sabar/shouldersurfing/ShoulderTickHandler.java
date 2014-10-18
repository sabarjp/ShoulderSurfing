package sabar.shouldersurfing;

import java.util.EnumSet;

import net.minecraft.util.Vec3;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ShoulderTickHandler implements ITickHandler{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.RENDER))){
			//attempt to ray trace
			RayTraceHelper.traceFromEyes(1.0F);
			
			if(ShoulderHelper.rayTraceHit != null){
				if(ShoulderSurfing.mc.thePlayer != null){
					ShoulderHelper.rayTraceHit.xCoord -= ShoulderSurfing.mc.thePlayer.posX;
					ShoulderHelper.rayTraceHit.yCoord -= ShoulderSurfing.mc.thePlayer.posY;
					ShoulderHelper.rayTraceHit.zCoord -= ShoulderSurfing.mc.thePlayer.posZ;
				}
			}
			
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "Shoulder Surfing";
	}

}
