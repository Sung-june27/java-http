package org.apache.catalina.handler.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StaticResourceLoader {

    private StaticResourceLoader() {
    }

    public static String load(final String uri) throws IOException {
        final String resourcePath = "static" + uri;
        try (final InputStream resource = StaticResourceLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
//            if (resource == null) {
//                throw new IllegalStateException("존재하지 않는 리소스입니다.");
//            }

            return new String(resource.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
