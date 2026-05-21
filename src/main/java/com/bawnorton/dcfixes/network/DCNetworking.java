package com.bawnorton.dcfixes.network;

import com.bawnorton.dcfixes.DeceasedCraftFixes;
import com.bawnorton.dcfixes.network.clientbound.ClientboundRebuildModelPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class DCNetworking {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            DeceasedCraftFixes.rl("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        CHANNEL.registerMessage(0, ClientboundRebuildModelPacket.class, ClientboundRebuildModelPacket::encode, ClientboundRebuildModelPacket::decode, (packet, contextSupplier) -> {
            contextSupplier.get().enqueueWork(() -> ClientboundRebuildModelPacket.handle(packet));
            contextSupplier.get().setPacketHandled(true);
        });
    }
}
