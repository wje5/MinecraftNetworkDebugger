package com.pinball3d.networkdebugger;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class PackageRummager {
	private static PacketBuffer buffer = new PacketBuffer(Unpooled.directBuffer());
	private static Logger logger = LogManager.getLogger("network_debugger");
	private static Map<String, Integer> total = new HashMap<String, Integer>();
	private static long time;

	public static void rummage(Packet<?> packet, boolean isUpload) {
		if (System.currentTimeMillis() > time + 10000) {
			printTotal();
			time = System.currentTimeMillis();
		}
		try {
			packet.writePacketData(buffer);
			int size = buffer.readableBytes();
			buffer.clear();
			String name = packet.getClass().getName();
//			logger.log(Level.WARN, (isUpload ? "[SEND]" : "[RECEIVE]") + name + "|Size:" + size);
			total.put(name, total.getOrDefault(name, 0) + size);
		} catch (Throwable e) {
			logger.catching(e);
		}
	}

	public static void printTotal() {
		logger.log(Level.WARN, total);
	}
}
