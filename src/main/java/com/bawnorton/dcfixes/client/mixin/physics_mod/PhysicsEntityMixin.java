package com.bawnorton.dcfixes.client.mixin.physics_mod;

import com.bawnorton.dcfixes.client.extend.PhysicsEntityExtender;
import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.diebuddies.physics.PhysicsEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@IfModLoaded({"physicsmod", "geckolib"})
@MixinEnvironment("client")
@Mixin(PhysicsEntity.class)
abstract class PhysicsEntityMixin implements PhysicsEntityExtender {
    @Unique
    private String dcfixes$boneId;

    @Unique
    private int dcfixes$ragdollIndex = -1;

    @Override
    public String dcfixes$getBoneId() {
        return dcfixes$boneId;
    }

    @Override
    public void dcfixes$setBoneId(String boneId) {
        this.dcfixes$boneId = boneId;
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
