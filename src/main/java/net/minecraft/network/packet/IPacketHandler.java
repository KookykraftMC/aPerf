package net.minecraft.network.packet;

import net.minecraft.network.Packet;

public interface IPacketHandler {
	boolean onOutgoingPacket(NetHandler network, int packetID, Packet packet);
}