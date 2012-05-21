package se.atoulou.www.filters;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.util.Future;
import com.twitter.util.FutureTransformer;

public class RequestLoggingFilter extends SimpleFilter<HttpRequest, HttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class.getCanonicalName());

    @Override
    public Future<HttpResponse> apply(HttpRequest request, Service<HttpRequest, HttpResponse> service) {
        logger.debug("Request: {}", request);

        Future<HttpResponse> response;
        try {
            Future<HttpResponse> initialResponse = service.apply(request);
            response = initialResponse;
        } catch (Exception e) {
            response = Future.exception(e);
        }

        return response.transformedBy(new FutureTransformer<HttpResponse, HttpResponse>() {

            @Override
            public HttpResponse map(HttpResponse response) {
                logger.debug("Response: {}", response);
                return response;
            }

            @Override
            public HttpResponse handle(Throwable throwable) {
                logger.debug("Service Exception: {}", throwable);
                return super.handle(throwable);
            }
        });
    }

}
