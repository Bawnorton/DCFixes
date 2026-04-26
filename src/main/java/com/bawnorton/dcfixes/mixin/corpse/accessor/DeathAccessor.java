package com.bawnorton.dcfixes.mixin.corpse.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import de.maxhenkel.corpse.corelib.death.Death;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("coprse")
@Mixin(value = Death.class, remap = false)
public interface DeathAccessor {
    @Accessor("posX")
    void dcfixes$posX(double posX);

    @Accessor("posY")
    void dcfixes$posY(double posY);

    @Accessor("posZ")
    void dcfixes$posZ(double posZ);
}
