package se.atoulou.www.filters;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.google.common.base.Charsets;
import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.util.Future;
import com.twitter.util.FutureTransformer;

public class ExceptionFilter extends SimpleFilter<HttpRequest, HttpResponse> {

    @Override
    public Future<HttpResponse> apply(HttpRequest request, Service<HttpRequest, HttpResponse> service) {
        Future<HttpResponse> response;
        try {
            Future<HttpResponse> initialResponse = service.apply(request);
            response = initialResponse;
        } catch (Exception e) {
            response = Future.exception(e);
        }

        return response.transformedBy(new FutureTransformer<HttpResponse, HttpResponse>() {
            @Override
            public HttpResponse map(HttpResponse value) {
                return value;
            }

            @Override
            public HttpResponse handle(Throwable throwable) {
                HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.INTERNAL_SERVER_ERROR);
                httpResponse.setContent(ChannelBuffers.copiedBuffer("errorrrrrrr", Charsets.UTF_8));
                return httpResponse;
            }
        });
    }
}
