package eu.busz.codurance.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Message {
    private final String text;
    private final String userName;
    private final String date;
}
