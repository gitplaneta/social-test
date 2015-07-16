package eu.busz.codurance.model.command;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.console.ConsolePrinter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MessagePrinter {

    private static final String MESSAGE_FORMAT = "%s (%s ago)";

    private final ConsolePrinter console;
    private final Clock clock;

    public void print(List<Message> messagesToPrint) {
        messagesToPrint.forEach(message -> {
            String duration = clock.wordedTimeDurationSince(message.getDate());
            console.printLine(formatMessage(message.getText(), duration));
        });
    }

    private String formatMessage(String text, String duration) {
        return String.format(MESSAGE_FORMAT, text, duration);
    }
}
