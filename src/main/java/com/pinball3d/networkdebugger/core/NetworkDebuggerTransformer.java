package com.pinball3d.networkdebugger.core;

import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class NetworkDebuggerTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		Logger logger = LogManager.getLogger("network_debugger");
		if (transformedName.equals("net.minecraft.network.NetworkManager")) {
			ClassReader reader = new ClassReader(basicClass);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			Iterator<MethodNode> it = node.methods.iterator();
			boolean flag = false;
			while (it.hasNext()) {
				MethodNode method = it.next();
				if (method.name.equals("dispatchPacket")) {
					logger.log(Level.WARN, method.name + "#" + method.parameters.toString() + "#");
//					flag = true;
				}
			}
			if (!flag) {
				throw new Error();
			}
		}
		return basicClass;
	}
}
