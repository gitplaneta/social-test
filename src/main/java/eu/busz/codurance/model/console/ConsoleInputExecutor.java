package eu.busz.codurance.model.console;

import eu.busz.codurance.model.command.Command;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConsoleInputExecutor {
    private final List<Command> commands;

    public void readLine(String line) {
        commands.stream()
                .filter(cmd -> cmd.isMatchingCommand(line))
                .findFirst()
                .ifPresent(cmd -> cmd.executeCommand(line));
    }
}