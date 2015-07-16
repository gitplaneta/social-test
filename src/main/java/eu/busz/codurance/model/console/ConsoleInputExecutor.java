package eu.busz.codurance.model.console;

import eu.busz.codurance.model.command.Command;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ConsoleInputExecutor {
    private final List<Command> commands;

    public void readLine(String line) {
        commands.stream()
                .filter(cmd -> cmd.isMatchingCommand(line))
                .findFirst()
                .ifPresent(cmd -> cmd.executeCommand(line));
    }
}