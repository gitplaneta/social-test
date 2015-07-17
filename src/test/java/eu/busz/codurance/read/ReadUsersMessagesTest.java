package eu.busz.codurance.read;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.model.command.read.ReadUserMessageCommand;
import eu.busz.codurance.model.command.read.ReadUserMessageCommandParser;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.persistence.memory.InMemoryMessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadUsersMessagesTest {

    private static final String CORRECT_PUBLISH_MESSAGE = "Alice";
    private static final String INCORRECT_PUBLISH_MESSAGE = "Bob follow Charles";
    private static final LocalDateTime ANY_DATE_TIME = LocalDateTime.of(1970, 12, 12, 0, 0);
    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GONNA_GO_SHOPPING = "Gonna go shopping";

    @Mock
    private ConsolePrinter console;
    @Mock
    private InMemoryMessageRepository repository;
    @Mock
    private Clock clock;
    private MessagePrinter messagePrinter;
    private ReadUserMessageCommand readUserMessageCommand;

    @Before
    public void setUp() {
        messagePrinter = new MessagePrinter(console, clock);
        readUserMessageCommand = new ReadUserMessageCommand(new ReadUserMessageCommandParser(), repository, messagePrinter);
    }

    @Test
    public void readsSingleMessageFromRepositoryThenPrintsIt() {
        when(repository.getMessagesByUserName(eq("Alice"))).thenReturn(
                asList(Message.builder()
                        .userName("Alice")
                        .text(I_LOVE_THE_WEATHER_TODAY)
                        .date(ANY_DATE_TIME).build())
        );
        when(clock.wordedTimeDurationSince(any())).thenReturn("5 minutes");

        readUserMessageCommand.executeCommand("Alice");

        verify(console).printLine(eq(I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)"));
    }

    @Test
    public void readsMultipleMessagesFromRepositoryThenPrintsThem() {
        List<Message> messages = asList(Message.builder()
                        .userName("Alice")
                        .text(I_LOVE_THE_WEATHER_TODAY)
                        .date(ANY_DATE_TIME).build(),
                Message.builder()
                        .userName("Alice")
                        .text(GONNA_GO_SHOPPING)
                        .date(ANY_DATE_TIME)
                        .build());
        when(repository.getMessagesByUserName(eq("Alice"))).thenReturn(messages);
        when(clock.wordedTimeDurationSince(any())).thenReturn("5 minutes", "6 minutes");

        readUserMessageCommand.executeCommand("Alice");

        verify(console).printLine(I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
        verify(console).printLine(GONNA_GO_SHOPPING + " (6 minutes ago)");
    }

    @Test
    public void checksIfCommandMatchingCorrectlyMatchesMessageReadCommand() {
        assertThat("Post command should match message",
                readUserMessageCommand.isMatchingCommand(CORRECT_PUBLISH_MESSAGE), is(true));
        assertThat("Post command should not match message",
                readUserMessageCommand.isMatchingCommand(INCORRECT_PUBLISH_MESSAGE), is(false));
    }

    @Test
    public void executesReadingOfUserMessageThenChecksIfCorrectUserFetchedFromRepository() {
        readUserMessageCommand.executeCommand("Alice");
        verify(repository).getMessagesByUserName("Alice");
    }


}
