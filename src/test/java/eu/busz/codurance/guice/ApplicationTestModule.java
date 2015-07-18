package eu.busz.codurance.guice;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.CommandExecutor;
import eu.busz.codurance.model.command.follow.UserFollowingCommand;
import eu.busz.codurance.model.command.publish.PublishCommand;
import eu.busz.codurance.model.command.read.ReadUserMessageCommand;
import eu.busz.codurance.model.command.wall.UserWallCommand;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.persistence.InMemoryMessageRepository;
import eu.busz.codurance.persistence.MessageRepository;
import lombok.RequiredArgsConstructor;

import static com.google.inject.Guice.createInjector;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class ApplicationTestModule extends AbstractModule {

    private final Clock clock;
    private final ConsolePrinter consolePrinter;

    public static Injector getInjector(Clock clock, ConsolePrinter consolePrinter) {
        ApplicationTestModule guiceModule = new ApplicationTestModule(clock, consolePrinter);
        return createInjector(guiceModule);
    }

    @Override
    protected void configure() {
        bind(MessageRepository.class).to(InMemoryMessageRepository.class);
        bind(Clock.class).toInstance(clock);
        bind(ConsolePrinter.class).toInstance(consolePrinter);
    }

    @Provides
    public CommandExecutor providesCommandExecutor(UserFollowingCommand followCommand, PublishCommand publishCommand,
                                                   ReadUserMessageCommand readCommand, UserWallCommand wallCommand) {
        return new CommandExecutor(ImmutableList.of(followCommand, publishCommand, readCommand, wallCommand));
    }
}
