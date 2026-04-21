package com.bawnorton.dcfixes.client.mixin;

import com.bawnorton.dcfixes.extend.QuestExtender;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BlankPanel;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.ModalPanel;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftbquests.client.gui.quests.ViewQuestPanel;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@MixinEnvironment("client")
@Mixin(value = ViewQuestPanel.class, remap = false)
abstract class ViewQuestPanelMixin extends ModalPanel {
    protected ViewQuestPanelMixin(Panel panel) {
        super(panel);
    }

    @Shadow
    private static Component hotkey(String key) {
        throw new AssertionError();
    }

    @Shadow
    private Quest quest;

    @Shadow
    private BlankPanel panelText;

    @Definition(id = "setText", method = "Ldev/ftb/mods/ftblibrary/ui/TextField;setText(Lnet/minecraft/network/chat/Component;)Ldev/ftb/mods/ftblibrary/ui/TextField;", remap = true)
    @Expression("?.setText(?('ftbquests.tasks'))")
    @ModifyArg(
            method = "addWidgets",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private Component replaceWithSpecifiedTask(Component original) {
        QuestExtender extender = (QuestExtender) (Object) quest;
        String rawTaskTitle = extender.dcfixes$getRawTaskTitle();
        if(rawTaskTitle.isEmpty()) {
            return original;
        } else {
            return extender.dcfixes$getTaskTitle();
        }
    }

    @Definition(id = "setText", method = "Ldev/ftb/mods/ftblibrary/ui/TextField;setText(Lnet/minecraft/network/chat/Component;)Ldev/ftb/mods/ftblibrary/ui/TextField;", remap = true)
    @Expression("?.setText(?('ftbquests.rewards'))")
    @ModifyArg(
            method = "addWidgets",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private Component replaceWithSpecifiedReward(Component original) {
        QuestExtender extender = (QuestExtender) (Object) quest;
        String rawTaskTitle = extender.dcfixes$getRawRewardsTitle();
        if(rawTaskTitle.isEmpty()) {
            return original;
        } else {
            return extender.dcfixes$getRewardsTitle();
        }
    }


    @Inject(
            method = "keyReleased",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/ftb/mods/ftblibrary/ui/input/Key;is(I)Z",
                    ordinal = 0
            )
    )
    private void addNewFieldHotkeys(Key key, CallbackInfo ci) {
        if(key.is(InputConstants.KEY_J)) {
            dcfixes$editTasks();
        } else if(key.is(InputConstants.KEY_K)) {
            dcfixes$editRewards();
        }
    }

    @Unique
    private void dcfixes$editTasks() {
        StringConfig c = new StringConfig(null);
        QuestExtender extender = (QuestExtender) (Object) quest;
        c.setValue(extender.dcfixes$getRawTaskTitle());
        EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(getGui(), c, accepted -> {
            if (accepted) {
                extender.dcfixes$setTaskTitle(c.getValue());
                (new EditObjectMessage(this.quest)).sendToServer();
            }

        }, Component.translatable("dcfixes.quest.tasks"));
        overlay.setWidth(Mth.clamp(overlay.getWidth(), 150, this.getScreen().getGuiScaledWidth() - 20));
        overlay.setPos(panelText.getX() + (this.panelText.width - overlay.width) / 2, this.panelText.getY() - 14);
        this.getGui().pushModalPanel(overlay);
    }

    @Unique
    private void dcfixes$editRewards() {
        StringConfig c = new StringConfig(null);
        QuestExtender extender = (QuestExtender) (Object) quest;
        c.setValue(extender.dcfixes$getRawRewardsTitle());
        EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(getGui(), c, accepted -> {
            if (accepted) {
                extender.dcfixes$setRewardsTitle(c.getValue());
                (new EditObjectMessage(this.quest)).sendToServer();
            }

        }, Component.translatable("dcfixes.quest.rewards"));
        overlay.setWidth(Mth.clamp(overlay.getWidth(), 150, this.getScreen().getGuiScaledWidth() - 20));
        overlay.setPos(panelText.getX() + (this.panelText.width - overlay.width) / 2, this.panelText.getY() - 14);
        this.getGui().pushModalPanel(overlay);
    }

    @Definition(id = "add", method = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    @Definition(id = "SEPARATOR", field = "Ldev/ftb/mods/ftblibrary/ui/ContextMenuItem;SEPARATOR:Ldev/ftb/mods/ftblibrary/ui/ContextMenuItem;")
    @Expression("?.add(SEPARATOR)")
    @WrapOperation(
            method = "openEditButtonContextMenu",
            at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0)
    )
    private <E> boolean addNewFields(List<ContextMenuItem> instance, E e, Operation<Boolean> original) {
        instance.add(ContextMenuItem.SEPARATOR);
        instance.add(new ContextMenuItem(Component.translatable("dcfixes.quest.tasks").append(hotkey("J")), Icons.NOTES, b -> dcfixes$editTasks()));
        instance.add(new ContextMenuItem(Component.translatable("dcfixes.quest.rewards").append(hotkey("K")), Icons.NOTES, b -> dcfixes$editRewards()));
        return original.call(instance, e);
    }
}