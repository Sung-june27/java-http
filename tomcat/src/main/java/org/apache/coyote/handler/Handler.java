package org.apache.coyote.handler;

import java.io.IOException;

public interface Handler {

    boolean canHandle(final String requestUri);

    String handle(final String requestTarget) throws IOException;
}
