package com.teamderpy.shouldersurfing.asm;

import java.util.Map;

import com.teamderpy.shouldersurfing.ShoulderSurfing;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

/**
 * @author      Joshua Powers <jsh.powers@yahoo.com>
 * @version     1.0
 * @since       2012-12-30
 */
public class ShoulderPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "com.teamderpy.shouldersurfing.asm.ShoulderTransformations" };
	}

	@Override
	public String getModContainerClass() {
		return ShoulderSurfing.class.getName();
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}


}
