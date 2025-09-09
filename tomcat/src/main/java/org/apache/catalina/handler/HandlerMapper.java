package org.apache.catalina.handler;

import java.util.List;
import org.apache.coyote.http11.request.HttpRequest;

public class HandlerMapper {

    private static class HandlerMapperHolder {
        private static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private final List<Handler> handlers = List.of(
            new StaticResourceHandler(),
            new HelloWorldHandler(),
            new LoginHandler(),
            new RegisterHandler()
    );

    public static HandlerMapper getInstance() {
        return HandlerMapperHolder.INSTANCE;
    }

    private HandlerMapper() {
    }

    public Handler getHandler(final HttpRequest request) {
        for (Handler handler : handlers) {
            if (handler.canHandle(request.getPath())) {
                return handler;
            }
        }

        return null;
    }
}
