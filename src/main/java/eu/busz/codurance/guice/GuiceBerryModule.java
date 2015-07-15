package eu.busz.codurance.guice;

import com.google.inject.AbstractModule;

public class GuiceBerryModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new GuiceBerryModule());
    }
}
