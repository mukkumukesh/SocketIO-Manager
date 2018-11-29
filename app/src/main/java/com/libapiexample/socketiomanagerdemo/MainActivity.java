package com.libapiexample.socketiomanagerdemo;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lib.socketiomanager.Constants;
import com.lib.socketiomanager.ISocketEventListener;
import com.lib.socketiomanager.Option;
import com.lib.socketiomanager.SocketEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ISocketEventListener {

    private TextView txvSocketUpdateLbl;
    private final String URL = "http://172.18.7.106:9100"; // https://beta.wapanda.net/api/socket/map";
    private SocketEventListener socketEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvSocketUpdateLbl = findViewById(R.id.txv_socket_update);
    }

    @Override
    protected void onStart() {
        super.onStart();
        socketEventListener = new SocketEventListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(socketEventListener, new IntentFilter(Constants.KEY_INTENT_FILTER_SOCKET));
    }

    @Override
    protected void onStop() {
        if (socketEventListener != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(socketEventListener);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().disconnect();
    }

    @Override
    public void onEventAvailable(String key, Object obj) {
        txvSocketUpdateLbl.setText(String.format("key : %s \n Value: %s", key, obj));
    }

    public void connectSocket(View view) {
        Option op = new Option.Builder().withTimeOut(10000).withIsReconnect(true).withTransportType(new String[]{Constants.TYPE_WEBSOCKET}).build();
        SocketManager.getInstance().initialize(URL, op, this);
    }

    public void disconnectSocket(View view) {
        SocketManager.getInstance().disconnect();
    }

    public void emitID(View view) {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("id", 116);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketManager.getInstance().emit("connect-kairos", object);
    }

    public void emitEvent(View view) {
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            object.putOpt("patientId", 466);
            object.putOpt("deviceId", "AF802151-9B80-4EAB-A6D2-5C62CF1A1F76");
            data.putOpt("distance", 10000);
            data.putOpt("etaTime", 1000);
            data.putOpt("lat", 28.4626375);
            data.putOpt("lng", 77.0565042);
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketManager.getInstance().emit("live-tracking", object);
    }
}

