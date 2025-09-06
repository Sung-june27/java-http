package org.apache.coyote.handler;

import java.io.IOException;
import org.apache.coyote.HttpRequest;

public interface Handler {

    boolean canHandle(final String requestUri);

    String handle(final HttpRequest request) throws IOException;
}
