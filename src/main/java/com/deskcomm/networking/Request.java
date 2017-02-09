package com.deskcomm.networking;

import com.deskcomm.support.L;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jay Rathod on 16-01-2017.
 */
public class Request {
    private String path;
    private int methodType;
    private Map<String, String> params;
    private Client client;
    private WebTarget target;
    private String[] mediaTypes;

    public Request(String path, int methodType, String... mediaTypes) {
        this.path = path;
        this.methodType = methodType;
        this.mediaTypes = mediaTypes;
    }

    public Request(String path, int methodType) {
        this.path = path;
        this.methodType = methodType;
        StringArray array = new StringArray();
        array.add(MediaType.TEXT_HTML);
        array.add(MediaType.TEXT_PLAIN);
        array.add(MediaType.TEXT_XML);
        array.add(MediaType.APPLICATION_JSON);
        mediaTypes = array._array;
    }

    public Request(String path) {
        this.path = path;
        this.methodType = MethodType.GET;
        StringArray array = new StringArray();
        array.add(MediaType.TEXT_HTML);
        array.add(MediaType.TEXT_PLAIN);
        array.add(MediaType.TEXT_XML);
        array.add(MediaType.APPLICATION_JSON);
        mediaTypes = array._array;
    }

    public Request(String path, int methodType, Map<String, String> params) {
        this.path = path;
        this.methodType = methodType;
        this.params = params;
        StringArray array = new StringArray();
        array.add(MediaType.TEXT_HTML);
        array.add(MediaType.TEXT_PLAIN);
        array.add(MediaType.TEXT_XML);
        array.add(MediaType.APPLICATION_JSON);
        mediaTypes = array._array;
    }


    public Response perform() {
        client = ClientBuilder.newClient();
        target = client.target(URLManager.TARGET_URL).path(path);
        params = getParams();
        switch (methodType) {
            case MethodType.GET:
                return performGet();
            case MethodType.POST:
                return performPost();
            case MethodType.PUT:
                return performPut();
            case MethodType.DELETE:
                return performDelete();
            default:
                return performGet();
        }
    }

    private Response performGet() {
        for (int i = 0; i < params.size(); i++) {
            Set set = params.keySet();
            ArrayList<String> params = new ArrayList<>(set);
            target.queryParam(params.get(i), params.get(i));
        }
        L.println(target.getUri().toString());
        return target
                .request(mediaTypes)
                .accept(mediaTypes)
                .get();

    }

    private Response performPost() {
        Form form = new Form();
        Set<String> set = params.keySet();
        ArrayList keys = new ArrayList(set);
        System.out.println(keys.toString());
        // String[] keys = (String[]) set.toArray();
        for (int i = 0; i < params.size(); i++) {
            form.param((String) keys.get(i), params.get(keys.get(i)));
        }
        return target.request(mediaTypes).post(Entity.form(form));
    }

    private Response performPut() {
        return null;
    }

    private Response performDelete() {
        return null;
    }


    public Map<String, String> getParams() {
        if (params == null) params = new HashMap<>();
        return this.params;
    }

}
