package eu.busz.codurance.model.command.follow;

import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.persistence.MessageRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserFollowingCommand implements Command {

    private final UserFollowingCommandParser followingCommandParser;
    private final MessageRepository messageRepository;

    @Override
    public boolean isMatchingCommand(String command) {
        return followingCommandParser.isMatching(command);
    }

    @Override
    public void executeCommand(String command) {
        Following following = followingCommandParser.extractFollowing(command);
        messageRepository.saveFollowing(following);
    }
}
