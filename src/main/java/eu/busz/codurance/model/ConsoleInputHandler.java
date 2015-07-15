package eu.busz.codurance.model;

import eu.busz.codurance.model.command.Command;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@RequiredArgsConstructor
public class ConsoleInputHandler {

    private final List<Command> commands;

    public Optional<String> executeCommand(String line) {
        return commands.stream()
                .filter(cmd -> cmd.isMatchingCommand(line))
                .findFirst()
                .map(cmd -> cmd.executeCommand(line))
                .orElse(empty());
    }
}
