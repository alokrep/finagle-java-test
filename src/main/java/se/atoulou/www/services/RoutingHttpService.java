package se.atoulou.www.services;

import java.util.List;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.beust.jcommander.internal.Lists;
import com.twitter.finagle.Service;
import com.twitter.util.Future;

public abstract class RoutingHttpService extends Service<HttpRequest, HttpResponse> {
    @Override
    public final Future<HttpResponse> apply(HttpRequest request) {
        for (Route route : routes) {
            if (route.matches(request)) {

                return route.service.apply(request);
            }
        }

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        return Future.value(response);
    }

    private final List<Route> routes;

    public RoutingHttpService() {
        routes = Lists.newArrayList();
    }

    protected RouteBuilder from(String route) {
        return new RouteBuilder(route);
    }

    public final class RouteBuilder {
        final String route;

        private RouteBuilder(String route) {
            this.route = route;
        }

        public void to(Service<HttpRequest, HttpResponse> service) {
            routes.add(new Route(route, service));
        }
    }

    private static class Route {
        private final String route;
        private final Service<HttpRequest, HttpResponse> service;

        private Route(String route, Service<HttpRequest, HttpResponse> service) {
            this.route = route;
            this.service = service;
        }

        boolean matches(HttpRequest request) {
            if (route.equals(request.getUri())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
