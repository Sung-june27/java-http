package org.apache;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {

    private final Map<String, String> cookies = new HashMap<>();

    public void setCookies(String value) {
        if (value == null) {
            return;
        }

        final String[] cookies = value.split("; ");
        for (String cookie : cookies) {
            final String[] pair = cookie.split("=");
            this.cookies.put(pair[0], pair[1]);
        }
    }

    public void setCookie(String name, String value) {
        cookies.put(name, value);
    }
}
