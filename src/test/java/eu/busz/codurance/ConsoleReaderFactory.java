package eu.busz.codurance;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.CommandExecutor;
import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.model.command.follow.UserFollowingCommand;
import eu.busz.codurance.model.command.follow.UserFollowingCommandParser;
import eu.busz.codurance.model.command.publish.PublishCommand;
import eu.busz.codurance.model.command.publish.PublishCommandParser;
import eu.busz.codurance.model.command.read.ReadUserMessageCommand;
import eu.busz.codurance.model.command.read.ReadUserMessageCommandParser;
import eu.busz.codurance.model.command.wall.UserWallCommand;
import eu.busz.codurance.model.command.wall.UserWallCommandParser;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.model.console.ConsoleReader;
import eu.busz.codurance.persistence.memory.InMemoryMessageRepository;
import eu.busz.codurance.persistence.memory.MessageRepository;

import java.util.List;

import static java.util.Arrays.asList;

public class ConsoleReaderFactory {

    public static ConsoleReader create(Clock clock, ConsolePrinter consolePrinter) {
        MessageRepository messageRepository = new InMemoryMessageRepository(clock);
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clock);

        Command publishCommand = new PublishCommand(new PublishCommandParser(), messageRepository);

        Command readUserMessageCommand = new ReadUserMessageCommand(new ReadUserMessageCommandParser(),
                messageRepository, messagePrinter);

        UserFollowingCommandParser followingCommandParser = new UserFollowingCommandParser();
        Command followingCommand = new UserFollowingCommand(followingCommandParser, messageRepository);

        UserWallCommandParser wallCommandParser = new UserWallCommandParser();
        Command wallCommand = new UserWallCommand(wallCommandParser, messageRepository, messagePrinter);

        List<Command> commandList = asList(publishCommand, readUserMessageCommand, followingCommand, wallCommand);
        CommandExecutor commandExecutor = new CommandExecutor(commandList);

        return new ConsoleReader(commandExecutor);
    }
}
