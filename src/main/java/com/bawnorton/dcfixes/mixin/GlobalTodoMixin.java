package com.bawnorton.dcfixes.mixin;

import com.bawnorton.dcfixes.FasterLostCities;
import javax.annotation.Nonnull;
import mcjty.lostcities.worldgen.GlobalTodo;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GlobalTodo.class)
public abstract class GlobalTodoMixin {

    /**
     * @author bawnorton
     * @reason See {@link FasterLostCities}
     */
    @Nonnull
    @Overwrite
    public static GlobalTodo getData(World world) {
        if(world instanceof ServerWorld serverWorld) {
            return FasterLostCities.getData(serverWorld);
        }
        throw new IllegalStateException("Called on wrong thread");
    }
}
