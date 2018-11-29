package com.lib.socketiomanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

@SuppressWarnings("unused")
class MessageHandler extends Handler {

    private final Context mContext;

    MessageHandler(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        Intent intent = new Intent(Constants.KEY_INTENT_FILTER_SOCKET);
        intent.putExtras(bundle);
//        intent.putExtras(Constants.KEY, msg.KEY)
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
