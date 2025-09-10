package org.apache.catalina.handler;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public interface Controller {

    boolean canHandle(String path);

    void service(HttpRequest request, HttpResponse response) throws Exception;
}
