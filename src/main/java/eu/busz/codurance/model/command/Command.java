package eu.busz.codurance.model.command;

import java.util.Optional;

public interface Command {
    boolean isMatchingCommand(String command);
    Optional<String> executeCommand(String command);
}
