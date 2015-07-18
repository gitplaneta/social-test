package eu.busz.codurance.guice;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import eu.busz.codurance.model.CommandExecutor;
import eu.busz.codurance.model.command.follow.UserFollowingCommand;
import eu.busz.codurance.model.command.publish.PublishCommand;
import eu.busz.codurance.model.command.read.ReadUserMessageCommand;
import eu.busz.codurance.model.command.wall.UserWallCommand;
import eu.busz.codurance.persistence.InMemoryMessageRepository;
import eu.busz.codurance.persistence.MessageRepository;

import java.time.Clock;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MessageRepository.class).to(InMemoryMessageRepository.class);
        bind(Clock.class).toInstance(Clock.systemUTC());
    }

    @Provides
    public CommandExecutor providesCommandExecutor(UserFollowingCommand followCommand, PublishCommand publishCommand,
                                                   ReadUserMessageCommand readCommand, UserWallCommand wallCommand) {
        return new CommandExecutor(ImmutableList.of(followCommand, publishCommand, readCommand, wallCommand));
    }
}
