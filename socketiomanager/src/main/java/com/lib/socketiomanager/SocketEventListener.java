package com.lib.socketiomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public final class SocketEventListener extends BroadcastReceiver {

    private final ISocketEventListener mISocketEventListener;

    public SocketEventListener(@NonNull ISocketEventListener iSocketEventListener) {
        mISocketEventListener = iSocketEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra(Constants.MESSAGE);
        String key = intent.getStringExtra(Constants.KEY);
        mISocketEventListener.onEventAvailable(key, data);
    }
}
