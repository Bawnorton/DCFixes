package com.bawnorton.dcfixes.extend;

public interface FoodInstanceExtender {
    long dcfixes$getTimestamp();

    void dcfixes$setTimestamp(long time);

    boolean dcfixes$isForgotten();

    void dcfixes$setForgotten(boolean forgotten);
}
