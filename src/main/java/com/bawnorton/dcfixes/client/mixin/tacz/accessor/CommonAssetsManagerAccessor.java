package com.bawnorton.dcfixes.client.mixin.tacz.accessor;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.index.CommonAmmoIndex;
import com.tacz.guns.resource.manager.CommonDataManager;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfModLoaded("tacz")
@MixinEnvironment("client")
@Mixin(value = CommonAssetsManager.class, remap = false)
public interface CommonAssetsManagerAccessor {
    @Accessor("ammoIndex")
    CommonDataManager<CommonAmmoIndex> dcfixes$ammoIndex();
}
