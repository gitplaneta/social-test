package eu.busz.codurance.acceptance;

import eu.busz.codurance.model.Console;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReadingWallMessagesFeature {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";

    private Console spyConsole;

    @Before
    public void setUp() {
        Console console = new Console(null);
        spyConsole = Mockito.spy(console);
    }

    @Test
    public void publish_messages_then_read_them_via_wall() {
        spyConsole.writeLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        spyConsole.writeLine("Bob -> " + DAMN_WE_LOST);
        spyConsole.writeLine("Bob -> " + GOOD_GAME_THOUGH);

        spyConsole.writeLine("Alice");
        spyConsole.writeLine("Bob");

        verify(spyConsole).printLine(I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
        verify(spyConsole).printLine(GOOD_GAME_THOUGH + " (1 minute ago)");
        verify(spyConsole).printLine(DAMN_WE_LOST + " (2 minutes ago)");
    }


}
