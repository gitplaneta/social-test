package eu.busz.codurance.model.console;

import eu.busz.codurance.model.CommandExecutor;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConsoleReader {

    private final CommandExecutor commandExecutor;

    public void readLine(String line) {
        commandExecutor.executeCommand(line);
    }
}
