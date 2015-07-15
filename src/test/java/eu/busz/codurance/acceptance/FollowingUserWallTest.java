package eu.busz.codurance.acceptance;

import eu.busz.codurance.model.Console;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FollowingUserWallTest {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final String I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE =
            "I'm in New York today! Anyone want to have a coffee?";

    private Console spyConsole;

    @Before
    public void setUp() {
        Console console = new Console(null);
        spyConsole = Mockito.spy(console);
    }

    @Test
    public void publish_messages_then_follow_one_user() {
        spyConsole.writeLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        spyConsole.writeLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        spyConsole.writeLine("Charlie follows Alice");
        spyConsole.writeLine("Charlie wall");

        Mockito.verify(spyConsole).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                "(2 seconds ago)");
        Mockito.verify(spyConsole).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + "(5 minutes ago)");
    }

    @Test
    public void publish_messages_then_follow_multiple_users() {
        spyConsole.writeLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        spyConsole.writeLine("Bob -> " + DAMN_WE_LOST);
        spyConsole.writeLine("Bob -> " + GOOD_GAME_THOUGH);
        spyConsole.writeLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        spyConsole.writeLine("Charlie follows Alice");
        spyConsole.writeLine("Charlie follows Bob");
        spyConsole.writeLine("Charlie wall");

        Mockito.verify(spyConsole).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                "(15 seconds ago)");
        Mockito.verify(spyConsole).printLine("Bob - " + DAMN_WE_LOST + "(1 minute ago)");
        Mockito.verify(spyConsole).printLine("Bob - " + GOOD_GAME_THOUGH + "(2 minutes ago)");
        Mockito.verify(spyConsole).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + "(5 minutes ago)");
    }
}