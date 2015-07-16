package eu.busz.codurance.acceptance;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.CommandExecutor;
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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FollowingUserWallFeatureTest {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final String I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE =
            "I'm in New York today! Anyone want to have a coffee?";

    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private Clock clock;
    private ConsoleReader consoleReader;

    @Before
    public void setUp() {
        MessageRepository messageRepository = new InMemoryMessageRepository(clock);
        PublishCommand publishCommand = new PublishCommand(new PublishCommandParser(), messageRepository);
        ReadUserMessageCommand readCommand = new ReadUserMessageCommand(new ReadUserMessageCommandParser(), messageRepository,
                new MessagePrinter(consolePrinter, clock));

        consoleReader = new ConsoleReader(new CommandExecutor(asList(publishCommand, readCommand)));
    }

    @Test
    public void publish_messages_then_follow_one_user() {
        consoleReader.readLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        consoleReader.readLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        consoleReader.readLine("Charlie follows Alice");
        consoleReader.readLine("Charlie wall");

        verify(consolePrinter).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                "(2 seconds ago)");
        verify(consolePrinter).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + "(5 minutes ago)");
    }

    @Test
    public void publish_messages_then_follow_multiple_users() {
        consoleReader.readLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        consoleReader.readLine("Bob -> " + DAMN_WE_LOST);
        consoleReader.readLine("Bob -> " + GOOD_GAME_THOUGH);
        consoleReader.readLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        consoleReader.readLine("Charlie follows Alice");
        consoleReader.readLine("Charlie follows Bob");
        consoleReader.readLine("Charlie wall");

        verify(consolePrinter).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                "(15 seconds ago)");
        verify(consolePrinter).printLine("Bob - " + DAMN_WE_LOST + "(1 minute ago)");
        verify(consolePrinter).printLine("Bob - " + GOOD_GAME_THOUGH + "(2 minutes ago)");
        verify(consolePrinter).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + "(5 minutes ago)");
    }
}