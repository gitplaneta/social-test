package eu.busz.codurance.acceptance;

import eu.busz.codurance.ConsoleReaderFactory;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReadingWallMessagesFeatureTest {

    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String DAMN_WE_LOST = "Damn! We lost!";
    private static final LocalDateTime ANY_DATE_TIME = null;
    private static final java.time.Clock ANY_CLOCK = null;

    private ConsoleReader consoleReader;
    @Mock
    private ConsolePrinter consolePrinter;
    private Clock clock;

    @Before
    public void setUp() {
        clock = spy(new Clock(ANY_CLOCK));
        consoleReader = ConsoleReaderFactory.create(clock, consolePrinter);
    }

    @Test
    public void publishMessagesForTwoUsersThenReadMessagesViaWall() {
        LocalDateTime currentTime = LocalDateTime.of(2015, 7, 16, 22, 0);
        doReturn(ANY_DATE_TIME).when(clock).getCurrentDateTime();
        given(clock.getCurrentDateTime()).willReturn(
                currentTime.minusMinutes(5),
                currentTime.minusMinutes(2),
                currentTime.minusMinutes(1),
                currentTime,
                currentTime,
                currentTime
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


}
