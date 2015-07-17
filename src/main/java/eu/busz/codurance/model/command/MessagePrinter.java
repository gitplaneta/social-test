package eu.busz.codurance.model.command;

import eu.busz.codurance.model.Clock;
import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.console.ConsolePrinter;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MessagePrinter {

    private static final String MESSAGE_FORMAT = "%s (%s ago)";
    private static final String WALL_MESSAGE_FORMAT = "%s - %s (%s ago)";
    private static final Comparator<Message> BY_DESC_DATE_COMPARATOR = (msg1, msg2) ->
            msg1.getDate().compareTo(msg2.getDate()) * -1;

    private final ConsolePrinter console;
    private final Clock clock;

    public void printUserMessages(List<Message> messagesToPrint) {
        printMessage(messagesToPrint, this::readingMessageFormatter);
    }

    public void printWallMessages(List<Message> wallMessages) {
        printMessage(wallMessages, this::wallMessageFormat);

    }

    private void printMessage(List<Message> messages, BiFunction<Message, String, String> formatter) {
        messages.stream()
                .sorted(BY_DESC_DATE_COMPARATOR)
                .forEach(message -> {
                    String duration = clock.wordedTimeDurationSince(message.getDate());
                    console.printLine(formatter.apply(message, duration));
                });
    }

    private String readingMessageFormatter(Message message, String duration) {
        return String.format(MESSAGE_FORMAT, message.getText(), duration);
    }

    private String wallMessageFormat(Message message, String duration) {
        return String.format(WALL_MESSAGE_FORMAT, message.getUserName(), message.getText(),
                duration);
    }
}
