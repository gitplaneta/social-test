package eu.busz.codurance.model.command.post;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublishCommandParser {

    private final String POST_MESSAGE_PATTERN = "(\\w+) -> (.+)";
    private final Pattern pattern;

    public PublishCommandParser() {
        pattern = Pattern.compile(POST_MESSAGE_PATTERN);
    }

    public boolean isMatching(String command) {
        Matcher matcher = pattern.matcher(command);
        return matcher.matches();
    }

    public String extractName(String command) {
        return getPatternGroup(command, 1);
    }

    private String getPatternGroup(String string, int group) {
        Matcher matcher = pattern.matcher(string);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        return matcher.group(group);
    }

    public String extractMessage(String command) {
        return getPatternGroup(command, 2);
    }
}
