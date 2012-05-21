package se.atoulou.www.services;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.twitter.finagle.Service;
import com.twitter.util.Future;

public class MyHttpService extends Service<HttpRequest, HttpResponse> {

    @Override
    public Future<HttpResponse> apply(HttpRequest req) {
        Future<HttpResponse> resp = Future.value((HttpResponse) new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK));

        return resp;

    }

}
