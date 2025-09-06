package org.apache.coyote.handler;

import java.util.ArrayList;
import java.util.List;
import org.apache.coyote.HttpRequest;

public class HandlerMapper {

    private static class HandlerMapperHolder {
        private static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private final List<Handler> handlers = new ArrayList<>();

    public static HandlerMapper getInstance() {
        return HandlerMapperHolder.INSTANCE;
    }

    private HandlerMapper() {
        // static resource 우선 매핑
        handlers.add(new StaticResourceHandler());

        // 이외 endpoint 매핑
        handlers.add(new HelloWorldHandler());
        handlers.add(new LoginHandler());
        handlers.add(new RegisterHandler());
    }

    public Handler getHandler(final HttpRequest request) {
        for (Handler handler : handlers) {
            if (handler.canHandle(request.getUri())) {
                return handler;
            }
        }

        return null;
    }
}
