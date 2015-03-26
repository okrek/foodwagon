package com.rostrade.foodwagon.foodwagon.utility;

public interface SyncListener {
    void onTaskStarted(int length);

    void onTaskUpdated();

    void onError();
}
