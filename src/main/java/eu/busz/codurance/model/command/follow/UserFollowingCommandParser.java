package eu.busz.codurance.model.command.follow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class UserFollowingCommandParser {

    private static final String FOLLOWING_PATTERN = "(\\w+) follows (\\w+)";
    private final Pattern followingPattern;

    public UserFollowingCommandParser() {
        followingPattern = Pattern.compile(FOLLOWING_PATTERN);
    }

    public boolean isMatching(String command) {
        return followingPattern.matcher(command).matches();
    }

    public Following extractFollowing(String command) {
        Matcher followingMatcher = followingPattern.matcher(command);
        checkArgument(followingMatcher.matches());

        return Following.builder()
                .sourceUserName(followingMatcher.group(1))
                .targetUserNamed(followingMatcher.group(2))
                .build();
    }
}
