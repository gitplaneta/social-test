package eu.busz.codurance.publish;

import eu.busz.codurance.model.Console;
import eu.busz.codurance.model.ConsoleInputHandler;
import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.command.post.PublishCommand;
import eu.busz.codurance.model.command.post.PublishCommandParser;
import eu.busz.codurance.model.memory.InMemoryMessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagePublishingTest {

    private static final String CORRECT_PUBLISH_MESSAGE = "Alice -> I love the weather";
    private static final String INCORRECT_PUBLISH_MESSAGE = "Bob follow Charles";
    @Mock
    private InMemoryMessageRepository postRepository;
    private Command publishCommand;

    @Before
    public void setUp() {
        publishCommand = new PublishCommand(new PublishCommandParser(), postRepository);
    }

    @Test
    public void passCommandAsInputThenCheckIfPostCommandMatches() {
        assertThat("Post command should match message",
                publishCommand.isMatchingCommand(CORRECT_PUBLISH_MESSAGE), is(true));
        assertThat("Post command should not match message",
                publishCommand.isMatchingCommand(INCORRECT_PUBLISH_MESSAGE), is(false));
    }

    @Test
    public void passCommandAsInputThenCheckIfMessageIsSaved() {
        publishCommand.executeCommand(CORRECT_PUBLISH_MESSAGE);

        verify(postRepository).saveMessage(eq("Alice"), eq("I love the weather"));
    }

    @Test
    public void publishMessageViaConsoleThenCheckIfMessageWasSaved() {
        List<Command> commands = asList(new PublishCommand(new PublishCommandParser(), postRepository));
        ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(commands);
        Console console = new Console(consoleInputHandler);

        console.writeLine(CORRECT_PUBLISH_MESSAGE);

        verify(postRepository).saveMessage(eq("Alice"), eq("I love the weather"));
    }
}
