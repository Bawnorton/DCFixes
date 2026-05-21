package com.bawnorton.dcfixes.network.clientbound;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;

public record ClientboundRebuildModelPacket(List<BlockPos> positions) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(positions.size());
        for (BlockPos pos : positions) {
            buf.writeBlockPos(pos);
        }
    }

    public static ClientboundRebuildModelPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<BlockPos> positions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            positions.add(buf.readBlockPos());
        }
        return new ClientboundRebuildModelPacket(positions);
    }

    public static void handle(ClientboundRebuildModelPacket packet) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DeceasedCraftFixesClient.getCompat().getLostCitiesCompat().orElseThrow().handlePacket(packet));
    }
}
