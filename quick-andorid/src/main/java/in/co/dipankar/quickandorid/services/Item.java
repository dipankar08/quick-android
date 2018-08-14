package in.co.dipankar.quickandorid.services;

import java.io.Serializable;

public final class Item implements Serializable {
    private String id;
    private String name;
    private String url;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Item(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
