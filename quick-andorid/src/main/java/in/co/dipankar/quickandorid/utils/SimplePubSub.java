package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.support.annotation.Nullable;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public final class SimplePubSub {

    private WebSocketClient mWebSocket;

    private String url = "ws://simplestore.dipankar.co.in:8081/";
    private List<Callback> mCallbackList;
    private Set<String> topics;

    public interface Callback{
        void onConnect();
        void onDisconnect();
        void onError(String err);
        void onMessage(String topic, String data);
        void onSignal(String topic, String data);
    }

    public interface Config{
        String getURL();
    }

    public void addCallback(Callback callback){
        mCallbackList.add(callback);
    }
    public void removeCallback(Callback callback){
        mCallbackList.remove(callback);
    }

    public SimplePubSub(){
        mCallbackList = new ArrayList<>();
    }
    public SimplePubSub(@Nullable  Config config) {
        if(config != null && config.getURL() != null){
            url = config.getURL();
        }
        mCallbackList = new ArrayList<>();
        topics = new HashSet<>();
    }

    public void subscribe(String topic){
        ensureInit();
        mWebSocket.send(buildPayload("subscribe", topic, null));
        topics.add(topic);
    }

    public void unsubscribe(String topic){
        ensureInit();
        mWebSocket.send(buildPayload("unsubscribe", topic, null));
        topics.remove(topic);
    }

    public void publish(String topic, String data){
        ensureInit();
        mWebSocket.send(buildPayload("message", topic, data));
    }

    private String buildPayload(String type, String topic, String data) {
        try {
            return new JSONObject()
                    .put("type", type)
                    .put("topic", topic)
                    .put("data", data).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            DLog.e("=========== ERROR ===========");
            return "ERROR";
        }
    }

    public void connect(){
        ensureInit();

        for (String s: topics){
            subscribe(s);
        }
    }

    public void disconnect(){
        if(mWebSocket != null) {
            mWebSocket.close();
            mWebSocket = null;
        }
    }

    private synchronized void ensureInit() {
        if(mWebSocket == null) {
            URI uri;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
            mWebSocket = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    for(Callback c: mCallbackList){
                        c.onConnect();
                    }
                }

                @Override
                public void onMessage(String rawData) {
                    try {
                        JSONObject obj = new JSONObject(rawData);
                        String type = obj.getString("type");
                        String topic = obj.getString("topic");
                        String data = null;
                        if(obj.has("data")){
                            data = obj.getString("data");
                        }
                        switch (type){
                            case "message":
                                for(Callback c: mCallbackList){
                                    c.onMessage(topic, data);
                                }
                                break;
                            case "signal":
                                for(Callback c: mCallbackList){
                                    c.onSignal(topic, data);
                                }
                                break;
                            default:
                                DLog.e("SimplePubSub: onMessage with wrong data"+rawData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        DLog.e("=========== ERROR ===========");
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    for(Callback c: mCallbackList){
                        c.onDisconnect();
                    }
                }

                @Override
                public void onError(Exception e) {
                    for(Callback c: mCallbackList){
                        c.onError(e.getMessage());
                    }
                }
            };
            try {
                mWebSocket.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
