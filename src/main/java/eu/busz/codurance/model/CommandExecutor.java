package eu.busz.codurance.model;

import eu.busz.codurance.model.command.Command;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CommandExecutor {

    private final List<Command> commands;

    public void executeCommand(String line) {
        commands.stream()
                .filter(cmd -> cmd.isMatchingCommand(line))
                .findFirst()
                .ifPresent(cmd -> cmd.executeCommand(line));
    }
}
