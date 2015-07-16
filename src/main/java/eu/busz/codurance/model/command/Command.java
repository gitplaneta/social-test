package eu.busz.codurance.model.command;

public interface Command {
    boolean isMatchingCommand(String command);
    void executeCommand(String command);
}
