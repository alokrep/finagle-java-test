package se.atoulou.www;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.twitter.finagle.Service;
import com.twitter.util.Future;

public class MyHttpServer extends Service<HttpRequest, HttpResponse> {

    @Override
    public Future<HttpResponse> apply(HttpRequest req) {
        // Forget about this for now
        @SuppressWarnings("unused")
        HttpResponse resp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // HELP: Which of these are preferable?
        return Future.exception(new IllegalStateException("boo"));
        // throw new IllegalStateException("boo");
    }

}
