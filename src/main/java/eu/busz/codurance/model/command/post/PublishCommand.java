package eu.busz.codurance.model.command.post;

import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.memory.InMemoryMessageRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PublishCommand implements Command {

    private final PublishCommandParser parser;
    private final InMemoryMessageRepository postRepository;

    public boolean isMatchingCommand(String command) {
        return parser.isMatching(command);
    }

    public Optional<String> executeCommand(String command) {
        String userName = parser.extractName(command);
        String message = parser.extractMessage(command);

        postRepository.saveMessage(userName, message);

        return Optional.empty();
    }
}
