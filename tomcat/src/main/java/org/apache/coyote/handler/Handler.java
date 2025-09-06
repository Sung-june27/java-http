package org.apache.coyote.handler;

import java.io.IOException;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.HttpResponse;

public interface Handler {

    boolean canHandle(final String requestUri);

    HttpResponse handle(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException;
}
