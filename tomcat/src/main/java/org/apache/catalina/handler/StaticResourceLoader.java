package org.apache.catalina.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StaticResourceLoader {

    private StaticResourceLoader() {
    }

    public static String load(final String uri) throws IOException {
        final String resourcePath = "static" + uri;
        try (final InputStream resource = StaticResourceLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resource == null) {
                return load("/404.html");
            }
            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
