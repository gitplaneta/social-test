package eu.busz.codurance.model.command.read;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadUserMessageCommandParser {

    private static final String MESSAGE_READ_PATTERN = "^\\w+$";
    private final Pattern pattern;

    public ReadUserMessageCommandParser() {
        pattern = Pattern.compile(MESSAGE_READ_PATTERN);
    }

    public boolean isMatching(String command) {
        Matcher matcher = pattern.matcher(command);
        return matcher.matches();
    }

    public String extractUserName(String command) {
        return command;
    }
}
