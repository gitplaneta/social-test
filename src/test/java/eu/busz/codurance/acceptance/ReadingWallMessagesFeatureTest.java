package eu.busz.codurance.acceptance;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.CommandExecutor;
import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.model.command.publish.PublishCommand;
import eu.busz.codurance.model.command.publish.PublishCommandParser;
import eu.busz.codurance.model.command.read.ReadUserMessageCommand;
import eu.busz.codurance.model.command.read.ReadUserMessageCommandParser;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.model.console.ConsoleReader;
import eu.busz.codurance.persistence.memory.InMemoryMessageRepository;
import eu.busz.codurance.persistence.memory.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReadingWallMessagesFeatureTest {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final Object ANY_DATE_TIME = null;

    private ConsoleReader consoleReader;
    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private java.time.Clock mockedJavaClock;
    private Clock clock;

    @Before
    public void setUp() {
        clock = spy(new Clock(mockedJavaClock));
        MessageRepository messageRepository = new InMemoryMessageRepository(clock);
        PublishCommand publishCommand = new PublishCommand(new PublishCommandParser(), messageRepository);
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clock);
        ReadUserMessageCommand readUserMessageCommand = new ReadUserMessageCommand(new ReadUserMessageCommandParser(),
                messageRepository, messagePrinter);
        List<Command> commandList = asList(publishCommand, readUserMessageCommand);
        CommandExecutor commandExecutor = new CommandExecutor(commandList);
        consoleReader = new ConsoleReader(commandExecutor);
    }

    @Test
    public void publishMessagesForTwoUsersThenReadMessagesViaWall() {
        LocalDateTime testEndDateTime = getTestEndDateTime();
        doReturn(ANY_DATE_TIME).when(clock).getCurrentDateTime();
        given(clock.getCurrentDateTime()).willReturn(
                testEndDateTime.minusMinutes(5),
                testEndDateTime.minusMinutes(2),
                testEndDateTime.minusMinutes(1),
                testEndDateTime,
                testEndDateTime,
                testEndDateTime
        );

        consoleReader.readLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        consoleReader.readLine("Bob -> " + DAMN_WE_LOST);
        consoleReader.readLine("Bob -> " + GOOD_GAME_THOUGH);

        consoleReader.readLine("Alice");
        consoleReader.readLine("Bob");

        InOrder inOrder = inOrder(consolePrinter);
        inOrder.verify(consolePrinter).printLine(I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
        inOrder.verify(consolePrinter).printLine(GOOD_GAME_THOUGH + " (1 minute ago)");
        inOrder.verify(consolePrinter).printLine(DAMN_WE_LOST + " (2 minutes ago)");
    }

    private LocalDateTime getTestEndDateTime() {
        return LocalDateTime.of(2015, 7, 16, 22, 0);
    }


}
