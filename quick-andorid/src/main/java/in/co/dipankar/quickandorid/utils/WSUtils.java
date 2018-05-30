package in.co.dipankar.quickandorid.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WSUtils {

    private String mUrl;
    private OkHttpClient mClient;
    public interface Callback{
        public void onConnected();
        public void onDisconnected();
        public void onMessage(String message);
        public void onError();
    }
    private  WebSocket mWebSocket;
    private Callback mCallback;

    public  WSUtils(String url, Callback callback){
        mUrl = url;
        mCallback = callback;
        init();
    }
    private void init(){
        mClient = new OkHttpClient();
        Request request = new Request.Builder().url(mUrl).build();
        WebSocket ws = mClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mCallback.onConnected();
                mWebSocket = webSocket;
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                mCallback.onMessage(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                mCallback.onMessage(bytes.toString());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                mWebSocket.close(1, null);
                mWebSocket = null;
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                mCallback.onDisconnected();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                mCallback.onError();
            }
        });

    }

    public boolean sendMessage(String message){
        if(mWebSocket != null){
            mWebSocket.send(message);
            return true;
        } else {
            mCallback.onError();
            return false;
        }
    }

    public boolean connect(){
        return true;
    }
    public boolean disconnect(){
        if(mWebSocket != null){
            mClient.dispatcher().executorService().shutdown();
            return true;
        } else{
            return false;
        }
    }
}
