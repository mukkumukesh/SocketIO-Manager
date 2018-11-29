package com.lib.socketiomanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Extend this abstract class to your own manager class and provide implementation of abstract method
 * It provide the basic implementation and initialisation of socket
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class SocketIOManager {

    private static final String TAG = SocketIOManager.class.getSimpleName();
    private Handler mMessageHandler;
    protected Socket mSocket;
    private Option mSocketOption;
    private MessageHandler handler;

    /**
     * SetUp initial value for socket
     * Using handler they also provide your event success value as well
     * @param option  Option provide builder object. They have timeOut, isConnected and transportType value
     * @param context Provide context to initialize handler [Handler provide a communication link to send message to you UI]
     */
    public void initialize(@NonNull String url, @Nullable Option option, @NonNull Context context) {
        if (option == null) {
            option = new Option.Builder().build();
        }
        mSocketOption = option;
        mMessageHandler = new MessageHandler(context);
        connect(url);
    }

    /**
     * This method provide the functionality of socket connection
     * This method is called when you initialize the socket
     * When socket is connected then the also add event listener
     * They provide to method
     * 1. listenEvent : You have to register all success event in this method
     * 2. listenApiError : You have to register all error event in this method
     * Both method is abstract so you have to provide your own implementation
     */
    private void connect(final String url) {
        IO.Options opts = new IO.Options();
        opts.timeout = mSocketOption.getTimeOut();
        opts.reconnection = mSocketOption.isReconnect();
        opts.transports = mSocketOption.getTransportType();
        try {
            Uri.Builder uri = Uri.parse(url).buildUpon();
            mSocket = IO.socket(uri.build().toString(), opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (mSocket != null) {
            listenApiError();
            listenSocketErrorEvent();
            mSocket.connect();
        }
    }

    public final Socket getSocket() {
        return mSocket;
    }

    /**
     * Emit an event
     * @param key Event key
     * @param obj Event value
     */
    public final void emit(final String key, final Object... obj) {
        mSocket.emit(key, obj);
    }

    /**
     * Emit an event with an acknowledgement
     * @param key  Event key
     * @param args Event value
     * @param ack  Callback for acknowledgement
     */
    public final void emit(final String key, final Object[] args, final Ack ack) {
        mSocket.emit(key, args, ack);
    }

    // On socket connection event error
    private void listenSocketErrorEvent() {
        mSocket.on(Socket.EVENT_CONNECT, args -> {
            listenEvent();
            emit("client-connection", mSocket.id());
            Log.e(TAG, "Socket is connected");
        }).on(Socket.EVENT_CONNECT_ERROR, args -> Log.e(TAG, Socket.EVENT_CONNECT_ERROR + " : " + (args.length != 0 ? args[0].toString() : "")))
                .on(Socket.EVENT_CONNECT_TIMEOUT, args -> Log.e(TAG, Socket.EVENT_CONNECT_TIMEOUT + " : " + (args.length != 0 ? args[0].toString() : "")))
                .on(Socket.EVENT_CONNECTING, args -> Log.e(TAG, Socket.EVENT_CONNECTING + " : " + (args.length != 0 ? args[0].toString() : "")))
                .on(Socket.EVENT_ERROR, args -> Log.e(TAG, Socket.EVENT_ERROR + " : " + (args.length != 0 ? args[0].toString() : "")))
                .on(Socket.EVENT_DISCONNECT, args -> Log.e(TAG, Socket.EVENT_DISCONNECT + " : " + (args.length != 0 ? args[0].toString() : "")));
    }

    //To check is socket is connected or not
    public final boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    // Close connection if socket is available
    public final void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }
    }

    /**
     * Provide all expected event name here
     */
    public abstract void listenEvent();

    /**
     * Provide all expected error event
     * Like Socket.on(eventName, callback)
     */
    public abstract void listenApiError();

    /**
     * Send message to UI
     * @param key Update key [You have to provide you own key so when handler send message to your local broadcast
     *            then using this key you separate the type of event value available]
     * @param msg Value
     */
    protected void sendUpdate(String key, String msg) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY, key);
        bundle.putString(Constants.MESSAGE, msg);
        message.setData(bundle);
        mMessageHandler.sendMessage(message);
    }
}
