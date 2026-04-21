package com.bawnorton.dcfixes.client.mixin.accessor.lrtactical;

import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.manager.CommonDataManager;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.xjqsh.lrtactical.resource.CommonAssetsManager;
import me.xjqsh.lrtactical.resource.manager.MeleeIndexManager;
import me.xjqsh.lrtactical.resource.manager.ThrowableIndexManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(value = CommonAssetsManager.class, remap = false)
public interface CommonAssetsManagerAccessor {
    @Accessor("throwableIndexManager")
    ThrowableIndexManager dcfixes$throwableIndexManager();

    @Accessor("meleeIndexManager")
    MeleeIndexManager dcfixes$meleeIndexManager();
}
