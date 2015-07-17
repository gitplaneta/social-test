package eu.busz.codurance.model.command.publish;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class PublishCommandParser {

    private final static String POST_MESSAGE_PATTERN = "(\\w+) -> (.+)";
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

    private String getPatternGroup(String command, int group) {
        Matcher matcher = pattern.matcher(command);
        checkArgument(matcher.matches());

        return matcher.group(group);
    }

    public String extractMessage(String command) {
        return getPatternGroup(command, 2);
    }
}
