package eu.busz.codurance.model.command.publish;

import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.persistence.memory.MessageRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PublishCommand implements Command {

    private final PublishCommandParser parser;
    private final MessageRepository postRepository;

    public boolean isMatchingCommand(String command) {
        return parser.isMatching(command);
    }

    public void executeCommand(String command) {
        String userName = parser.extractName(command);
        String message = parser.extractMessage(command);

        postRepository.saveMessage(userName, message);
    }
}
