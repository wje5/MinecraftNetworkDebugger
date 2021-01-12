package com.pinball3d.networkdebugger;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
@Mod(modid = NetworkDebugger.MODID, name = NetworkDebugger.NAME, version = NetworkDebugger.VERSION)
public class NetworkDebugger {
	public static final String MODID = "network_debugger";
	public static final String NAME = "Network Debugger";
	public static final String VERSION = "1.0.0";

	@Mod.Instance(NetworkDebugger.MODID)
	public static NetworkDebugger instance;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			// FML
			NetworkRegistry registry = NetworkRegistry.INSTANCE;
			Map<String, FMLEmbeddedChannel> channels = new HashMap<String, FMLEmbeddedChannel>();
			registry.channelNamesFor(Side.CLIENT).forEach(e -> channels.put(e, registry.getChannel(e, Side.CLIENT)));
		}
	}
}
