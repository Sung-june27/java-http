package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpCookie {

    private final Map<String, String> cookies = new HashMap<>();

    public void setCookie(
            String name,
            String value
    ) {
        cookies.put(name, value);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return cookies.keySet()
                .stream()
                .map(key -> key + "=" + cookies.get(key))
                .collect(Collectors.joining("; "));
    }
}
