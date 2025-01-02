package day3;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static day3.OperationDetector.State.*;

class OperationDetector implements Consumer<Character> {

    enum State {
        START,
        OP_NAME,
        OP_NAME_TERMINATOR,
        ARGUMENT,
        ARGUMENT_TERMINATOR
    }

    private static final Set<Character> firstLetterOfOperators = Set.of('m');
    private static final Set<Character> allowedLettersOfOperators = Set.of('l', 'm', 'u');
    private static final Set<Character> terminatingLettersOfOperatorName = Set.of('(');
    private static final Set<Character> allowedLettersOfArgument = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Set<Character> terminatingLettersOfArgument = Set.of(',');
    private static final Set<Character> terminatingLettersOfOperation = Set.of(')');

    private final LinkedList<Character> currentInput;
    private final List<String> currentTokens;
    private State currentState;

    OperationDetector() {
        currentState = State.START;
        currentInput = new LinkedList<>();
        currentTokens = new LinkedList<>();
    }

    Multiplier current() {
        if (currentTokens.isEmpty() || ! "mul".equals(currentTokens.getFirst())) {
            throw new IllegalStateException("missing or wrong operator token");
        }
        if (currentTokens.size() != 3) {
            throw new IllegalStateException("failed to detect exactly two operation arguments");
        }

        return new Multiplier(currentTokens.get(1), currentTokens.get(2));
    }

    boolean foundOperation() {
        return currentTokens.size() == 3;
    }

    @Override
    public void accept(Character character) {
        currentInput.add(character);

        switch (currentState) {
            case START -> switchFromStartForInput();
            case OP_NAME -> switchFromOpNameForInput();
            case OP_NAME_TERMINATOR -> switchFromOpNameTerminatorForInput();
            case ARGUMENT -> switchFromArgumentForInput();
            case ARGUMENT_TERMINATOR -> switchFromArgumentTerminatorForInput();
        }
    }

    private void switchFromStartForInput() {
        // delete the tokens to the latest possible time to ease debugging
        clearArgumentTokens();

        if (isFirstLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
        }
        else {
            currentState = START;
        }
    }

    private void switchFromOpNameForInput() {
        if (isLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
        }
        else if (isOperationNameTerminator(currentInput.getLast())) {
            currentState = OP_NAME_TERMINATOR;
            storeOperatorToken();
            clearCurrentInput();
        }
        else {
            currentState = START;
            clearCurrentInput();
        }
    }

    private void switchFromOpNameTerminatorForInput() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        }
        else {
            currentState = START;
            clearCurrentInput();
        }
    }

    private void switchFromArgumentForInput() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        }
        else if (isArgumentTerminator(currentInput.getLast())) {
            currentState = ARGUMENT_TERMINATOR;
            storeArgumentToken();
            clearCurrentInput();
        }
        else if (isOperationTerminator(currentInput.getLast())) {
            currentState = START;
            storeArgumentToken();
            clearCurrentInput();
        }
        else {
            currentState = START;
            clearCurrentInput();
        }
    }

    private void switchFromArgumentTerminatorForInput() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        }
        else {
            currentState = START;
            clearCurrentInput();
        }
    }

    private void storeOperatorToken() {
        // get rid of the operator terminating symbol '('
        currentInput.removeLast();

        String operatorToken = currentInput.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        currentTokens.add(operatorToken);
    }

    private void storeArgumentToken() {
        // get rid of the argument terminating symbol ',' or the whole operation termination symbol ')'
        currentInput.removeLast();

        String operatorToken = currentInput.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        currentTokens.add(operatorToken);
    }

    private void clearCurrentInput() {
        currentInput.clear();
    }

    private void clearArgumentTokens() {
        currentTokens.clear();
    }

    private static boolean isFirstLetterOfOperator(Character character) {
        return firstLetterOfOperators.contains(character);
    }

    private static boolean isLetterOfOperator(Character character) {
        return allowedLettersOfOperators.contains(character);
    }

    private static boolean isOperationNameTerminator(Character character) {
        return terminatingLettersOfOperatorName.contains(character);
    }

    private static boolean isLetterOfArgument(Character character) {
        return allowedLettersOfArgument.contains(character);
    }

    private static boolean isArgumentTerminator(Character character) {
        return terminatingLettersOfArgument.contains(character);
    }

    private static boolean isOperationTerminator(Character character) {
        return terminatingLettersOfOperation.contains(character);
    }

    State currentState() {
        return currentState;
    }
}
