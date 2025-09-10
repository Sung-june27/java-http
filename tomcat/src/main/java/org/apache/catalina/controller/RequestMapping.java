package org.apache.catalina.controller;

import java.util.List;
import org.apache.coyote.http11.request.HttpRequest;

public class RequestMapping {

    private static class RequestMappingHolder {
        private static final RequestMapping INSTANCE = new RequestMapping();
    }

    private final List<Controller> controllers = List.of(
            new StaticResourceController(),
            new HelloWorldController(),
            new LoginController(),
            new RegisterController()
    );

    public static RequestMapping getInstance() {
        return RequestMappingHolder.INSTANCE;
    }

    private RequestMapping() {
    }

    public Controller getController(final HttpRequest request) {
        for (Controller controller : controllers) {
            if (controller.canHandle(request.getPath())) {
                return controller;
            }
        }

        return null;
    }
}
