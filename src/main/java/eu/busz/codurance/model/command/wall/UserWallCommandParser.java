package eu.busz.codurance.model.command.wall;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class UserWallCommandParser {

    private static final String WALL_COMMAND_PATTERN = "(\\w+) wall";
    private final Pattern wallCommandPattern = Pattern.compile(WALL_COMMAND_PATTERN);

    public boolean isMatching(String command) {
        return wallCommandPattern.matcher(command).matches();
    }

    public String extractUserName(String command) {
        Matcher matcher = wallCommandPattern.matcher(command);
        checkArgument(matcher.matches());

        return matcher.group(1);
    }
}
