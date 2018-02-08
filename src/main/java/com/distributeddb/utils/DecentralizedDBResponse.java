package com.distributeddb.utils;

import static com.distributeddb.utils.Constants.*;

import java.util.List;

import org.json.JSONObject;

public class DecentralizedDBResponse {
    
    private String method;
    private String data;
    private List<String> list;

    public void setList(final List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public JSONObject buildJSON() {
        final JSONObject ret = new JSONObject();
        ret.put(METHOD, method);
        
        if (list != null) {
            ret.put(DATA, list);
        } else if (data != null) {
            ret.put(DATA, data);
        }

        return ret;
    }
}
