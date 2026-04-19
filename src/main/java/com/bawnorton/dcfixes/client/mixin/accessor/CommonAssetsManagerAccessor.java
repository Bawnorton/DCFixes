package com.bawnorton.dcfixes.client.mixin.accessor;

import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.manager.CommonDataManager;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@MixinEnvironment("client")
@Mixin(CommonAssetsManager.class)
public interface CommonAssetsManagerAccessor {
    @Accessor("ammoIndex")
    CommonDataManager<CommonAmmoIndex> dcfixes$ammoIndex();
}
