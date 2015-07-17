package eu.busz.codurance;

import com.google.inject.Guice;
import com.google.inject.Injector;
import eu.busz.codurance.guice.ApplicationModule;
import eu.busz.codurance.model.console.ConsoleReader;

import java.util.Scanner;

public class Application {

    private static ConsoleReader consoleReader;

    public static void main(String[] args) {
        injectMembers();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            consoleReader.readLine(scanner.nextLine());
        }
    }

    private static void injectMembers() {
        Injector injector = Guice.createInjector(new ApplicationModule());
        consoleReader = injector.getInstance(ConsoleReader.class);
    }
}
