package se.atoulou.www;

import java.net.InetSocketAddress;

import javax.inject.Inject;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.http.Http;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        new JCommander(arguments, args);

        Injector injector = Guice.createInjector(new MyModule(arguments));
        injector.getInstance(Main.class).run();

    }

    private final Arguments arguments;
    private final MyHttpServer httpServer;
    private final ExceptionFilter exceptionFilter;

    @Inject
    public Main(Arguments arguments, MyHttpServer httpServer, ExceptionFilter exceptionFilter) {
        this.arguments = arguments;
        this.httpServer = httpServer;
        this.exceptionFilter = exceptionFilter;

    }

    public void run() {
        Service<HttpRequest, HttpResponse> service = exceptionFilter.andThen(httpServer);

        ServerBuilder.safeBuild(
                service,
                ServerBuilder.get().codec(Http.get()).name("MyHttpServer")
                        .bindTo(new InetSocketAddress("localhost", arguments.getPort())));
    }

}
