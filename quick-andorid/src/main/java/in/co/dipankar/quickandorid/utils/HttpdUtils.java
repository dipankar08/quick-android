package in.co.dipankar.quickandorid.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class HttpdUtils extends NanoHTTPD {

    public interface Callback{
        public String Handle(String method, String key, Map<String, String> param);
        public void onSuccess(String msg);
        public void onError(String msg);
    }

    private Callback mCallback;

    public void init(int port, Callback callback) {
        mCallback = callback;
        this.start();
    }

    public HttpdUtils( int port, Callback callback){
        super(port);
        init(port, callback);
    }
    public HttpdUtils(Callback callback) {
        super(8080);
        init(8080, callback);
    }

    @Override public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        String result = mCallback.Handle(method.name(), uri,session.getParms());
        return NanoHTTPD.newFixedLengthResponse(result+"\n");
    }

    @Override
    public void start(){
        try {
            super.start();
            mCallback.onSuccess("started");
        } catch (IOException e) {
            e.printStackTrace();
            mCallback.onError("Not able start");
        }
    }

    @Override
    public void stop(){
        super.stop();
        mCallback.onSuccess("stoped");
    }
}
