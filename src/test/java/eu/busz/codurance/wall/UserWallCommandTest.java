package eu.busz.codurance.wall;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.model.command.wall.UserWallCommand;
import eu.busz.codurance.model.command.wall.UserWallCommandParser;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.persistence.memory.MessageRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserWallCommandTest {

    private static final String CORRECT_WALL_SYNTAX = "Bob wall";
    private static final String IN_CORRECT_WALL_SYNTAX = "Bob";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final LocalDateTime ANY_DATE = LocalDateTime.of(2000, 12, 12, 0, 0);

    private UserWallCommand wallCommand;
    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private Clock clock;
    private MessagePrinter messagePrinter;

    @Before
    public void setUp() {
        messagePrinter = new MessagePrinter(consolePrinter, clock);
        wallCommand = new UserWallCommand(new UserWallCommandParser(), messageRepository, messagePrinter);
    }

    @Test
    public void wallCommandMatchesWallCommandSyntax() {
        assertThat("Wall command matches correct wall command syntax",
                wallCommand.isMatchingCommand(CORRECT_WALL_SYNTAX), is(true));
        assertThat("Wall command does not match incorrect wall command syntax",
                wallCommand.isMatchingCommand(IN_CORRECT_WALL_SYNTAX), is(false));
    }

    @Test
    public void wallCommandPrintsPostsFetchedFromRepository() {
        LocalDateTime laterDate = ANY_DATE.plusNanos(1);
        List<Message> messageList = asList(
                Message.builder()
                        .userName("Bob")
                        .text(DAMN_WE_LOST)
                        .date(ANY_DATE)
                        .build(),
                Message.builder()
                        .userName("Bob")
                        .text(GOOD_GAME_THOUGH)
                        .date(laterDate)
                        .build()
        );
        given(messageRepository.getWallMessagesByUserName("Bob")).willReturn(messageList);
        given(clock.wordedTimeDurationSince(any())).willReturn("1 minute", "2 minutes");

        wallCommand.executeCommand("Bob wall");

        verify(consolePrinter).printLine("Bob - " + GOOD_GAME_THOUGH + " (1 minute ago)");
        verify(consolePrinter).printLine("Bob - " + DAMN_WE_LOST + " (2 minutes ago)");
    }
}
