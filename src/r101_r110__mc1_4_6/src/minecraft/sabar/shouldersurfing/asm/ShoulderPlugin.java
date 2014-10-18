package sabar.shouldersurfing.asm;

import java.util.Map;

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
		return new String[] { "sabar.shouldersurfing.asm.ShoulderTransformations" };
	}

	@Override
	public String getModContainerClass() {
		return "sabar.shouldersurfing.ShoulderSurfing";
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
