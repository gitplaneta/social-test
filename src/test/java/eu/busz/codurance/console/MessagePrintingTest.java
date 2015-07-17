package eu.busz.codurance.console;


import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.model.console.ConsolePrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagePrintingTest {

    private static final LocalDateTime ANY_DATE = null;
    private static final String GOOD_GAME_THOUGH = "Good game though.";
    private static final String I_LOVE_THE_WEATHER_TODAY = "I love the weather today";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2000, 12, 12, 0, 0);

    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private Clock clockMock;
    @Mock
    private java.time.Clock javaClockMock;

    @Test
    public void printUserMessageTextAndMockedTimeElapsedSincePosted() {
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clockMock);
        given(clockMock.wordedTimeDurationSince(any())).willReturn("5 minutes");

        messagePrinter.printUserMessages(asList(Message.builder()
                        .userName("Alice")
                        .text(I_LOVE_THE_WEATHER_TODAY)
                        .date(ANY_DATE)
                        .build())
        );

        verify(consolePrinter).printLine(I_LOVE_THE_WEATHER_TODAY + " (5 minutes ago)");
    }

    @Test
    public void clockCalculatesAndReturnsWordingDurationOfTime() {
        LocalDateTime postingTime = DATE_TIME;
        java.time.Clock clockAfterFiveMinutes = getJavaClockWithAddedTime(postingTime, 0, 5);
        Clock clock = new Clock(clockAfterFiveMinutes);

        assertThat("Clock returns correct duration of 5 minutes", clock.wordedTimeDurationSince(postingTime),
                equalTo("5 minutes"));
    }

    @Test
    public void printUserMessageTextAndTimeElapsedSincePosted() {
        LocalDateTime postingTime = DATE_TIME;
        java.time.Clock clockAfterTenHoursFiveMinutes = getJavaClockWithAddedTime(postingTime, 10, 5);
        Clock clock = new Clock(clockAfterTenHoursFiveMinutes);
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clock);

        messagePrinter.printUserMessages(asList(Message.builder()
                        .userName("Bob")
                        .text(GOOD_GAME_THOUGH)
                        .date(postingTime)
                        .build())
        );

        verify(consolePrinter).printLine(GOOD_GAME_THOUGH + " (10 hours 5 minutes ago)");
    }

    @Test
    public void printUserWallMessagesWithTimeElapsedSincePosted() {
        LocalDateTime postingTime = DATE_TIME;
        java.time.Clock clockAfterTenHoursFiveMinutes = getJavaClockWithAddedTime(postingTime, 10, 5);
        Clock clock = new Clock(clockAfterTenHoursFiveMinutes);
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clock);

        messagePrinter.printWallMessages(asList(Message.builder()
                .userName("Bob")
                .text(GOOD_GAME_THOUGH)
                .date(postingTime)
                .build())
        );

        verify(consolePrinter).printLine("Bob - " + GOOD_GAME_THOUGH + " (10 hours 5 minutes ago)");
    }

    private java.time.Clock getJavaClockWithAddedTime(LocalDateTime currentTime, int hoursToAdd, int minutesToAdd) {
        return java.time.Clock.fixed(currentTime
                .plusHours(hoursToAdd)
                .plusMinutes(minutesToAdd)
                .toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
