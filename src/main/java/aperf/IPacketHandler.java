package aperf;

import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;

public interface IPacketHandler {
	boolean onOutgoingPacket(NetHandler network, int packetID, Packet packet);
}
