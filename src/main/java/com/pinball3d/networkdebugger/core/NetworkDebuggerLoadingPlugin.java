package com.pinball3d.networkdebugger.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(100)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "com.pinball3d.networkdebugger.core" })
public class NetworkDebuggerLoadingPlugin implements IFMLLoadingPlugin {
	public static final String MODID = "network_debugger_core";
	public static final String NAME = "Network Debugger Core";
	public static final String VERSION = "1.0.0";
	public static boolean runtimeDeobf;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "com.pinball3d.networkdebugger.core.NetworkDebuggerTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		runtimeDeobf = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
