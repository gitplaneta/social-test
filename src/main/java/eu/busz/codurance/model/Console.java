package eu.busz.codurance.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Console {

    private final ConsoleInputHandler inputHandler;

    public void printLine(String line) {
        throw new UnsupportedOperationException();
    }

    public void writeLine(String line) {
        inputHandler.executeCommand(line)
                .ifPresent(str -> System.out.println(line));
    }
}
