package eu.busz.codurance.model;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationWords;

@RequiredArgsConstructor
public class Clock {

    private final java.time.Clock javaClock;
    private LocalDateTime currentDateTime;

    public String wordedTimeDurationSince(LocalDateTime dateTime) {
        Duration duration = Duration.between(dateTime, getCurrentDateTime());
        return formatDurationWords(duration.toMillis(), true, true);
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(javaClock);
    }
}
