package eu.busz.codurance.model.command.read;

import eu.busz.codurance.model.Message;
import eu.busz.codurance.model.command.Command;
import eu.busz.codurance.model.command.MessagePrinter;
import eu.busz.codurance.persistence.memory.MessageRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ReadUserMessageCommand implements Command {

    private final ReadUserMessageCommandParser readUserMessageCommandParser;
    private final MessageRepository repository;
    private final MessagePrinter messagePrinter;

    @Override
    public boolean isMatchingCommand(String command) {
        return readUserMessageCommandParser.isMatching(command);
    }

    @Override
    public void executeCommand(String command) {
        if (!isMatchingCommand(command)) {
            throw new IllegalArgumentException("String command does not match the UserMessageReadCommand input");
        }

        String userName = readUserMessageCommandParser.extractUserName(command);
        List<Message> messagesToPrint = repository.getMessagesByUserName(userName);
        messagePrinter.printUserMessages(messagesToPrint);
    }
}
