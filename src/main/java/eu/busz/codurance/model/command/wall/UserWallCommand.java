package eu.busz.codurance.model.command.wall;

import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.persistence.MessageRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserWallCommand implements Command {

    private final UserWallCommandParser userWallCommandParser;
    private final MessageRepository messageRepository;
    private final MessagePrinter messagePrinter;

    @Override
    public boolean isMatchingCommand(String command) {
        return userWallCommandParser.isMatching(command);
    }

    @Override
    public void executeCommand(String command) {
        String userName = userWallCommandParser.extractUserName(command);
        List<Message> wallMessages = messageRepository.getUserWallMessages(userName);
        messagePrinter.printWallMessages(wallMessages);

    }
}
