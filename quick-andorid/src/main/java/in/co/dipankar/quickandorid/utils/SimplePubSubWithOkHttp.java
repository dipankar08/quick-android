package in.co.dipankar.quickandorid.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class SimplePubSub {

    private OkHttpClient mOkHttpClient;
    private EchoWebSocketListener mEchoWebSocketListener;
    private WebSocket mWebSocket;

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
            mWebSocket.close(1000, "Disconnecting");
            mWebSocket = null;
        }
    }

    private synchronized void ensureInit() {
        if(mWebSocket == null) {
            mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            mEchoWebSocketListener = new EchoWebSocketListener();
            mWebSocket = mOkHttpClient.newWebSocket(request, mEchoWebSocketListener);
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            for(Callback c: mCallbackList){
                c.onConnect();
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, String rawData) {
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
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            // this is not used.
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            for(Callback c: mCallbackList){
                c.onDisconnect();
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            for(Callback c: mCallbackList){
                c.onError(t.getMessage());
            }
        }
    }
}
