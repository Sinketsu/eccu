package com.voidsong.eccu.support_classes;

import java.util.Map;
import java.util.TreeMap;

public class RequestBodyBuilder {
    private TreeMap<String, String> map = new TreeMap<>();

    public RequestBodyBuilder add(String key, String value) {
        map.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> item : map.entrySet())
            result.append(item.getKey())
                .append("=")
                .append(item.getValue())
                .append("&");
        return result.toString().substring(0, result.length() - 1);
    }
}
