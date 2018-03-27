package in.co.dipankar.quickandorid.utils;

import java.util.Map;

// Contract to be a Network as it needs to support send and recv
public interface INetwork {
    void retrive(
            final String url,
            Network.CacheControl cacheControl,
            final Network.Callback networkCallback);

    void send(
            final String url,
            final Map<String, String> data,
            final Network.Callback networkCallback);
}