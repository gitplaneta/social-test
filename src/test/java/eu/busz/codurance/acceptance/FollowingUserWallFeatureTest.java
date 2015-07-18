package eu.busz.codurance.acceptance;

import com.google.inject.Injector;
import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.console.ConsolePrinter;
import eu.busz.codurance.model.console.ConsoleReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static eu.busz.codurance.guice.ApplicationTestModule.getInjector;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FollowingUserWallFeatureTest {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final String I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE =
            "I'm in New York today! Anyone want to have a coffee?";
    private static final java.time.Clock ANY_CLOCK = null;
    private static final LocalDateTime ANY_DATE_TIME = null;

    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private Clock clock;
    private ConsoleReader consoleReader;
    private static final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.of(2015, 7, 16, 22, 0);

    @Before
    public void setUp() {
        clock = spy(new Clock(ANY_CLOCK));
        Injector injector = getInjector(clock, consolePrinter);
        consoleReader = injector.getInstance(ConsoleReader.class);
    }

    @Test
    public void publishMessagesThenFollowOneUser() {
        doReturn(ANY_DATE_TIME).when(clock).getCurrentDateTime();
        given(clock.getCurrentDateTime()).willReturn(
                CURRENT_DATE_TIME.minusMinutes(5),
                CURRENT_DATE_TIME.minusSeconds(2),
                CURRENT_DATE_TIME,
                CURRENT_DATE_TIME
        );

        consoleReader.readLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        consoleReader.readLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        consoleReader.readLine("Charlie follows Alice");
        consoleReader.readLine("Charlie wall");

        InOrder inOrder = inOrder(consolePrinter);
        inOrder.verify(consolePrinter).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                " (2 seconds ago)");
        inOrder.verify(consolePrinter).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
    }

    @Test
    public void publishMessagesThenFollowMultipleUsers() {
        doReturn(ANY_DATE_TIME).when(clock).getCurrentDateTime();
        given(clock.getCurrentDateTime()).willReturn(
                CURRENT_DATE_TIME.minusMinutes(5),
                CURRENT_DATE_TIME.minusMinutes(2),
                CURRENT_DATE_TIME.minusMinutes(1),
                CURRENT_DATE_TIME.minusSeconds(15),
                CURRENT_DATE_TIME
        );

        consoleReader.readLine("Alice -> " + I_LOVE_THE_WEATHER_TODAY);
        consoleReader.readLine("Bob -> " + DAMN_WE_LOST);
        consoleReader.readLine("Bob -> " + GOOD_GAME_THOUGH);
        consoleReader.readLine("Charlie -> " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE);

        consoleReader.readLine("Charlie follows Alice");
        consoleReader.readLine("Charlie follows Bob");
        consoleReader.readLine("Charlie wall");

        verify(consolePrinter).printLine("Charlie - " + I_M_IN_NEW_YORK_TODAY_ANYONE_WANT_TO_HAVE_A_COFFEE +
                " (15 seconds ago)");
        verify(consolePrinter).printLine("Bob - " + GOOD_GAME_THOUGH + " (1 minute ago)");
        verify(consolePrinter).printLine("Bob - " + DAMN_WE_LOST + " (2 minutes ago)");
        verify(consolePrinter).printLine("Alice - " + I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
    }
}