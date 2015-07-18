package eu.busz.codurance.follow;

import eu.busz.codurance.model.command.follow.Following;
import eu.busz.codurance.model.command.follow.UserFollowingCommand;
import eu.busz.codurance.model.command.follow.UserFollowingCommandParser;
import eu.busz.codurance.persistence.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserFollowingTest {

    private static final String CORRECT_FOLLOW_SYNTAX = "Charles follows May";
    private static final String INCORRECT_FOLLOW_SYNTAX = "Charles";
    private UserFollowingCommand followingCommand;

    @Mock
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
        followingCommand = new UserFollowingCommand(new UserFollowingCommandParser(), messageRepository);
    }

    @Test
    public void followingCommandMatchesFollowingCommandSyntax() {
        assertThat("Command recognizes correct command syntax",
                followingCommand.isMatchingCommand(CORRECT_FOLLOW_SYNTAX), is(true));
        assertThat("Command does not recognize unknown command syntax",
                followingCommand.isMatchingCommand(INCORRECT_FOLLOW_SYNTAX), is(false));
    }

    @Test
    public void executeFollowingCommandThenCheckIfFollowingEventSavedInRepository() {
        Following following = Following.builder()
                .sourceUserName("Alice")
                .targetUserNamed("Charles")
                .build();

        followingCommand.executeCommand("Alice follows Charles");

        verify(messageRepository).saveFollowing(eq(following));
    }
}
