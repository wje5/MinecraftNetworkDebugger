package com.pinball3d.networkdebugger.core;

import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class NetworkDebuggerTransformer implements IClassTransformer {
	private static Logger logger = LogManager.getLogger("network_debugger");

	@SuppressWarnings("deprecation")
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		try {
			if (transformedName.equals("net.minecraft.network.NetworkManager")) {
				ClassReader reader = new ClassReader(basicClass);
				ClassNode node = new ClassNode();
				reader.accept(node, 0);
				Iterator<MethodNode> it = node.methods.iterator();
				while (it.hasNext()) {
					MethodNode method = it.next();
					if (method.name.equals("dispatchPacket")
							|| (method.name.equals("a") && method.signature != null && method.signature.equals(
									"(Lht<*>;[Lio/netty/util/concurrent/GenericFutureListener<+Lio/netty/util/concurrent/Future<-Ljava/lang/Void;>;>;)V"))) {
						logger.log(Level.WARN, method.name + "#" + method.signature);
						InsnList list = new InsnList();
						list.add(new InsnNode(Opcodes.DUP));
						list.add(new InsnNode(Opcodes.ICONST_1));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								"com/pinball3d/networkdebugger/PackageRummager", "rummage",
								NetworkDebuggerLoadingPlugin.runtimeDeobf ? "(Lht;Z)V"
										: "(Lnet/minecraft/network/Packet;Z)V"));
						method.instructions.insert(method.instructions.get(2), list);
					}
					if (method.name.equals("channelRead0")
							&& (method.access & Opcodes.ACC_SYNTHETIC) != Opcodes.ACC_SYNTHETIC) {
						logger.log(Level.WARN, method.name + "#" + method.signature);
						printInsn(method.instructions);
						InsnList list = new InsnList();
						list.add(new VarInsnNode(Opcodes.ALOAD, 2));
						list.add(new InsnNode(Opcodes.ICONST_0));
						list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								"com/pinball3d/networkdebugger/PackageRummager", "rummage",
								NetworkDebuggerLoadingPlugin.runtimeDeobf ? "(Lht;Z)V"
										: "(Lnet/minecraft/network/Packet;Z)V"));
						method.instructions.insert(method.instructions.get(1), list);
					}
				}
				ClassWriter writer = new ClassWriter(1);
				node.accept(writer);
				return writer.toByteArray();
			}
		} catch (

		Throwable e) {
			return basicClass;
		}
		return basicClass;
	}

	private void printInsn(InsnList list) {
		String s = "";
		for (AbstractInsnNode insn : list.toArray()) {
			s += insn.getOpcode();
			s += "|";
		}
		logger.log(Level.WARN, s);
	}
}
