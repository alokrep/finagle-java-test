package se.atoulou.www;

import java.net.InetSocketAddress;

import javax.inject.Inject;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import se.atoulou.www.filters.ExceptionFilter;
import se.atoulou.www.filters.RequestLoggingFilter;
import se.atoulou.www.services.MyHttpService;
import se.atoulou.www.services.RoutingHttpService;

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
    private final Service<HttpRequest, HttpResponse> rootService;
    private final ExceptionFilter exceptionFilter;
    private final RequestLoggingFilter logFilter;

    @Inject
    public Main(Arguments arguments) {
        this.arguments = arguments;
        this.rootService = new RoutingHttpService() {
            {
                from("/").to(new MyHttpService());
                from("/blah").to(new MyHttpService());
            }
        };
        this.exceptionFilter = new ExceptionFilter();
        this.logFilter = new RequestLoggingFilter();

    }

    public void run() {
        Service<HttpRequest, HttpResponse> service = logFilter.andThen(exceptionFilter).andThen(rootService);

        ServerBuilder.safeBuild(
                service,
                ServerBuilder.get().codec(Http.get()).name("atoulou.se")
                        .bindTo(new InetSocketAddress("localhost", arguments.getPort())));
    }

}
