package in.co.dipankar.quickandorid.utils;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPUtils {

    public interface  Callback{
        public void onBeforeSend();
        public void onSuccess(JSONObject obj);
        public void onError(String msg);
    }
    private  OkHttpClient mClient;
    public HTTPUtils(){
        mClient = new OkHttpClient();
    }

    public boolean get(String url, final Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        callback.onBeforeSend();
        mClient.newCall(request).enqueue( new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(callback != null) {
                    callback.onError("Internal error:" + e.getMessage());
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject Jobject = new JSONObject(jsonData);
                        callback.onSuccess(Jobject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError(
                                "Internal error happened while parsing the json object");
                    }
                }
            }
        });
        return true;
    }

    public boolean post(String url, Map<String, String> data, final  Callback callback){
        JSONObject json = new JSONObject();
        if(data!= null) {
            try {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    json.put(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json.toString());
        callback.onBeforeSend();
        Request request = new Request.Builder().url(url).post(body).build();
        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError(e.toString());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject Jobject = new JSONObject(jsonData);
                        callback.onSuccess(Jobject);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Internal error happened while parsing the json object");
                    }
                }
            }
        });
        return true;
    }
}
