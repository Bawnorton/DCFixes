package com.bawnorton.dcfixes.extend;

import java.util.function.BooleanSupplier;

public interface MinecraftServerExtender {
    void dcfixes$runChunkSystemHousekeeping(BooleanSupplier haveTime);

    void dcfixes$markChunkSystemHousekeeping();
}
