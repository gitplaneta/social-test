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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagePrintingTest {

    private static final LocalDateTime ANY_DATE = null;

    @Mock
    private ConsolePrinter consolePrinter;
    @Mock
    private Clock clockMock;
    @Mock
    private java.time.Clock javaClockMock;

    @Test
    public void printTextAndMockedTimeElapsedSincePosted() {
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clockMock);
        given(clockMock.wordedTimeDurationSince(any())).willReturn("5 minutes");

        messagePrinter.print(asList(Message.builder()
                        .userName("Alice")
                        .text("I love the weather today")
                        .date(ANY_DATE)
                        .build())
        );

        verify(consolePrinter).printLine(eq("I love the weather today (5 minutes ago)"));
    }

    @Test
    public void clockCalculatesAndReturnsWordingDurationOfTime() {
        LocalDateTime postingTime = LocalDateTime.now();
        java.time.Clock clockAfterFiveMinutes = getJavaClockWithAddedTime(postingTime, 0, 5);
        Clock clock = new Clock(clockAfterFiveMinutes);

        assertThat("Clock returns correct duration of 5 minutes", clock.wordedTimeDurationSince(postingTime),
                equalTo("5 minutes"));
    }

    @Test
    public void printTextAndTimeElapsedSincePosted() {
        LocalDateTime postingTime = LocalDateTime.now();
        java.time.Clock clockAfterTenHoursFiveMinutes = getJavaClockWithAddedTime(postingTime, 10, 5);
        Clock clock = new Clock(clockAfterTenHoursFiveMinutes);
        MessagePrinter messagePrinter = new MessagePrinter(consolePrinter, clock);

        messagePrinter.print(asList(Message.builder()
                        .userName("Bob")
                        .text("Good game though.")
                        .date(postingTime)
                        .build())
        );

        verify(consolePrinter).printLine(eq("Good game though. (10 hours 5 minutes ago)"));
    }

    private java.time.Clock getJavaClockWithAddedTime(LocalDateTime currentTime, int hoursToAdd, int minutesToAdd) {
        return java.time.Clock.fixed(currentTime
                .plusHours(hoursToAdd)
                .plusMinutes(minutesToAdd)
                .toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    }
}
