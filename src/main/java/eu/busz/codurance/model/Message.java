package eu.busz.codurance.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Message {
    private final String text;
    private final String userName;
    private final LocalDateTime date;
}
