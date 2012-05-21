package se.atoulou.www;

import com.google.inject.AbstractModule;

public class MyModule extends AbstractModule {

    private final Arguments arguments;

    public MyModule(Arguments arguments) {
        this.arguments = arguments;
    }

    @Override
    protected void configure() {

        bind(Arguments.class).toInstance(arguments);
    }
}
