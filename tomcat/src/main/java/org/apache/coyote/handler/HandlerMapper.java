package org.apache.coyote.handler;

import java.util.ArrayList;
import java.util.List;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.ResponseBuilder;

public class HandlerMapper {

    private static class HandlerMapperHolder {
        private static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private final List<Handler> handlers = new ArrayList<>();

    public static HandlerMapper getInstance() {
        return HandlerMapperHolder.INSTANCE;
    }

    private HandlerMapper() {
        final ResponseBuilder responseBuilder = new ResponseBuilder();
        // static resource 우선 매핑
        handlers.add(new StaticResourceHandler(responseBuilder));

        // 이외 endpoint 매핑
        handlers.add(new HelloWorldHandler());
        handlers.add(new LoginHandler(responseBuilder));
        handlers.add(new RegisterHandler(responseBuilder));
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
