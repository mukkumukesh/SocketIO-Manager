package com.lib.socketiomanager;

public interface ISocketEventListener {
    void onEventAvailable(String key, Object val);
}
