package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.client.extend.PhysicsEntityExtender;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.cache.object.GeoBone;

@MixinEnvironment("client")
@Mixin(PhysicsEntity.class)
abstract class PhysicsEntityMixin implements PhysicsEntityExtender {
    @Unique
    private GeoBone dcfixes$geoBone;

    @Unique
    private int dcfixes$ragdollIndex = -1;

    @Override
    public GeoBone dcfixes$getGeoBone() {
        return dcfixes$geoBone;
    }

    @Override
    public void dcfixes$setGeoBone(GeoBone geoBone) {
        this.dcfixes$geoBone = geoBone;
    }

    @Override
    public int dcfixes$ragdollIndex() {
        return dcfixes$ragdollIndex;
    }

    @Override
    public void dcfixes$setRagdollIndex(int index) {
        this.dcfixes$ragdollIndex = index;
    }
}
