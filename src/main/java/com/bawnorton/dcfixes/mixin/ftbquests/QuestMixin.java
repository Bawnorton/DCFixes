package com.bawnorton.dcfixes.mixin.ftbquests;

import com.bawnorton.dcfixes.extend.QuestExtender;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.util.TextUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Debug(export = true)
@Mixin(value = Quest.class, remap = false)
abstract class QuestMixin implements QuestExtender {
    @Unique
    private String dcfixes$rawTaskTitle = "";

    @Unique
    private String dcfixes$rawRewardsTitle = "";

    @Unique
    private Component dcfixes$cachedTaskTitle;

    @Unique
    private Component dcfixes$cachedRewardsTitle;

    @Override
    public void dcfixes$setTaskTitle(String taskTitle) {
        dcfixes$rawTaskTitle = taskTitle;
    }

    @Override
    public void dcfixes$setRewardsTitle(String rewardsTitle) {
        dcfixes$rawRewardsTitle = rewardsTitle;
    }

    @Override
    public String dcfixes$getRawTaskTitle() {
        return dcfixes$rawTaskTitle;
    }

    @Override
    public String dcfixes$getRawRewardsTitle() {
        return dcfixes$rawRewardsTitle;
    }

    @Inject(
            method = "writeData",
            at = @At("TAIL")
    )
    private void writeAddedFields(CompoundTag nbt, CallbackInfo ci) {
        if(!this.dcfixes$rawTaskTitle.isEmpty()) {
            nbt.putString("dcfixes$task_title", dcfixes$rawTaskTitle);
        }
        if(!this.dcfixes$rawRewardsTitle.isEmpty()) {
            nbt.putString("dcfixes$rewards_title", dcfixes$rawRewardsTitle);
        }
    }

    @Inject(
            method = "readData",
            at = @At("TAIL")
    )
    private void readAddedFields(CompoundTag nbt, CallbackInfo ci) {
        dcfixes$rawTaskTitle = nbt.getString("dcfixes$task_title");
        dcfixes$rawRewardsTitle = nbt.getString("dcfixes$rewards_title");
    }

    @Inject(
            method = "writeNetData",
            at = @At("HEAD")
    )
    private void writeNewFields(FriendlyByteBuf buffer, CallbackInfo ci) {
        buffer.writeUtf(dcfixes$rawTaskTitle, 32767);
        buffer.writeUtf(dcfixes$rawRewardsTitle, 32767);
    }

    @Inject(
            method = "readNetData",
            at = @At("HEAD")
    )
    private void readNewFields(FriendlyByteBuf buffer, CallbackInfo ci) {
        dcfixes$rawTaskTitle = buffer.readUtf(32767);
        dcfixes$rawRewardsTitle = buffer.readUtf(32767);
    }

    @Definition(id = "getOrCreateSubgroup", method = "Ldev/ftb/mods/ftblibrary/config/ConfigGroup;getOrCreateSubgroup(Ljava/lang/String;)Ldev/ftb/mods/ftblibrary/config/ConfigGroup;")
    @Expression("?.getOrCreateSubgroup('appearance')")
    @Inject(
            method = "fillConfigGroup",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private void addNewFields(ConfigGroup config, CallbackInfo ci) {
        config.addString("dcfixes$task_title", dcfixes$rawTaskTitle, v -> dcfixes$rawTaskTitle = v, "");
        config.addString("dcfixes$rewards_title", dcfixes$rawRewardsTitle, v -> dcfixes$rawRewardsTitle = v, "");
    }

    @Override
    public Component dcfixes$getTaskTitle() {
        if (dcfixes$cachedTaskTitle == null) {
            dcfixes$cachedTaskTitle = TextUtils.parseRawText(dcfixes$rawTaskTitle);
        }
        return dcfixes$cachedTaskTitle;
    }
    @Override
    public Component dcfixes$getRewardsTitle() {
        if (dcfixes$cachedRewardsTitle == null) {
            dcfixes$cachedRewardsTitle = TextUtils.parseRawText(dcfixes$rawRewardsTitle);
        }
        return dcfixes$cachedRewardsTitle;
    }

    @Inject(
            method = "clearCachedData",
            at = @At("TAIL")
    )
    private void clearAddedFields(CallbackInfo ci) {
        dcfixes$cachedTaskTitle = null;
        dcfixes$cachedRewardsTitle = null;
    }
}
