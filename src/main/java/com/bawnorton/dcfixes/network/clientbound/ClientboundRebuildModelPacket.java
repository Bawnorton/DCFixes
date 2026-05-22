package com.bawnorton.dcfixes.network.clientbound;

import com.bawnorton.dcfixes.client.DeceasedCraftFixesClient;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.checkerframework.checker.units.qual.C;
import org.luaj.vm2.ast.Chunk;

import java.util.ArrayList;
import java.util.List;

public record ClientboundRebuildModelPacket(ChunkPos chunkPos, List<BlockPos> positions) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(chunkPos.toLong());
        buf.writeVarInt(positions.size());
        for (BlockPos pos : positions) {
            buf.writeBlockPos(pos);
        }
    }

    public static ClientboundRebuildModelPacket decode(FriendlyByteBuf buf) {
        ChunkPos pos = new ChunkPos(buf.readLong());
        int size = buf.readVarInt();
        List<BlockPos> positions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            positions.add(buf.readBlockPos());
        }
        return new ClientboundRebuildModelPacket(pos, positions);
    }

    public static void handle(ClientboundRebuildModelPacket packet) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DeceasedCraftFixesClient.getCompat().getLostCitiesCompat().orElseThrow().handlePacket(packet));
    }
}
