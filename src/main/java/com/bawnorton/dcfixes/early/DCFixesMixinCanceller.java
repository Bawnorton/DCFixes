package com.bawnorton.dcfixes.early;

import com.bawnorton.mixinsquared.api.MixinCanceller;

import java.util.List;

public final class DCFixesMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if(mixinClassName.equals("ca.fxco.moreculling.mixin.models.WeightedBakedModel_cacheMixin")
                || mixinClassName.equals("ca.fxco.moreculling.mixin.models.MultipartBakedModel_cacheMixin")) {
            return true;
        }
        return false;
    }
}
