package com.bawnorton.dcfixes.extend;

import net.minecraft.network.chat.Component;

public interface QuestExtender {
    void dcfixes$setTaskTitle(String taskTitle);

    String dcfixes$getRawTaskTitle();

    Component dcfixes$getTaskTitle();

    void dcfixes$setRewardsTitle(String rewardsTitle);

    String dcfixes$getRawRewardsTitle();

    Component dcfixes$getRewardsTitle();
}
