package com.libapiexample.socketiomanagerdemo;


import com.lib.socketiomanager.SocketIOManager;

public class SocketManager extends SocketIOManager {
    private static SocketManager sInstance = new SocketManager();

    private SocketManager() {
        if (sInstance != null) {
            throw new IllegalStateException("Object is already created");
        }
    }

    synchronized static SocketManager getInstance() {
        return sInstance;
    }

    @Override
    public void listenEvent() {
        mSocket.on("live-tracking", args -> sendUpdate("live-tracking", args[0].toString()));
    }

    @Override
    public void listenApiError() {
        mSocket.on("test", args -> sendUpdate("ApiError", args[0].toString()));
    }
}
