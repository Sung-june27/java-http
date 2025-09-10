package org.apache.catalina.controller;

import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public abstract class AbstractController implements Controller {

    @Override
    public void service(
            HttpRequest request,
            HttpResponse response
    ) throws Exception {
        if (HttpMethod.GET == request.getMethod()) {
            doGet(request, response);
        }
        if (HttpMethod.POST == request.getMethod()) {
            doPost(request, response);
        }
    }

    protected void doPost(
            HttpRequest request,
            HttpResponse response
    ) throws Exception {
        response.redirect("/405.html");
    }

    protected void doGet(
            HttpRequest request,
            HttpResponse response
    ) throws Exception {
        response.redirect("/405.html");
    }
}
