package com.bawnorton.dcfixes.mixin.apocalypsenow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.mcreator.apocalypsenow.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({
        AcaciacrateBlock.class,
        AcaciaPlanksStashBlock.class,
        BirchcrateBlock.class,
        BirchPlanksStashBlock.class,
        CarboardboxBlock.class,
        CherrycrateBlock.class,
        CherryPlanksStashBlock.class,
        CokevendingmachineBlock.class,
        CorpseclothBlock.class,
        CorpsesackBlock.class,
        DarkcrateBlock.class,
        DarkPlanksStashBlock.class,
        DesertmilitarystorageBlock.class,
        EmptymarketshelvesBlock.class,
        IceBlock.class,
        IroncrateBlock.class,
        IronlockerBlock.class,
        IronsafeBlock.class,
        JunglecrateBlock.class,
        JunglePlanksStashBlock.class,
        LargeshelvesBlock.class,
        LargeshelvesfillBlock.class,
        MagazineBoxesBlock.class,
        MangrovecrateBlock.class,
        MangrovePlanksStashBlock.class,
        MarketfridgeBlock.class,
        MarketshelvesBlock.class,
        MechanicsToolChestBlock.class,
        MedicalBoxBlock.class,
        MedicalStorageBlock.class,
        MedicineboxesBlock.class,
        MetalShelvesBlock.class,
        MetalshelvesfiveBlock.class,
        MetalshelvesoneBlock.class,
        MetalshelvesthreeBlock.class,
        MetalShevelstwoBlock.class,
        MetelshelvesfourBlock.class,
        MilitaryboxBlock.class,
        MilitarystorageBlock.class,
        OakPlanksStashBlock.class,
        PalletStorageBlock.class,
        PorpsivendingmachineBlock.class,
        SedexboxBlock.class,
        SprucecrateBlock.class,
        SprucePlanksStashBlock.class,
        Trashcan1Block.class,
        TrashcanBlock.class,
        TrashcannBlock.class,
        WashingmachineBlock.class,
        WoodencrateBlock.class
})
abstract class TheOneMixinToRuleThemAll {
    @WrapMethod(
            method = "use"
    )
    private InteractionResult whyArentYouUsingYourBlockEntitysMenuProvider(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit, Operation<InteractionResult> original) {
        if(world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof MenuProvider menuProvider) {
                entity.openMenu(menuProvider);
            }
            return InteractionResult.CONSUME;
        }
    }
}
