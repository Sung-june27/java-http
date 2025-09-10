package org.apache.catalina.controller.util;

import java.util.Map;

public class ContentTypeExtractor {

    private static final Map<String, String> MIME_MAP = Map.ofEntries(
            Map.entry("html", "text/html"),
            Map.entry("css", "text/css"),
            Map.entry("js", "text/javascript"),
            Map.entry("svg", "image/svg+xml")
    );

    public static String extract(final String filePath) {
        final String finalExtension = parseFileExtension(filePath);
        final String mime = MIME_MAP.get(finalExtension);
        if (isTextual(mime)) {
            return mime + ";charset=utf-8";
        }

        return mime;
    }

    private static String parseFileExtension(final String filePath) {
        final int index = filePath.lastIndexOf('.');

        return filePath.substring(index + 1);
    }

    private static boolean isTextual(String mime) {
        return mime.startsWith("text/");
    }
}
