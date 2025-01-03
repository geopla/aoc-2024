package day3;

import java.util.Collections;
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
        ARGUMENT_TERMINATOR,
        OP_TERMINATOR

    }

    private static final Set<String> ALLOWED_OPERATION_NAMES = Set.of("mul");
    private static final int MAX_NUMBER_OF_ARGUMENT_DIGITS = 3;

    private static final Set<Character> FIRST_LETTER_OF_OPERATORS = Set.of('m');
    private static final Set<Character> ALLOWED_LETTERS_OF_OPERATORS = Set.of('l', 'm', 'u');
    private static final Set<Character> TERMINATING_LETTERS_OF_OPERATION_NAME = Set.of('(');
    private static final Set<Character> ALLOWED_LETTERS_OF_ARGUMENT = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Set<Character> TERMINATING_LETTERS_OF_ARGUMENT = Set.of(',');
    private static final Set<Character> TERMINATING_LETTERS_OF_OPERATION = Set.of(')');


    private final LinkedList<Character> currentInput;
    private final List<String> currentTokens;
    private State currentState;

    OperationDetector() {
        currentState = State.START;
        currentInput = new LinkedList<>();
        currentTokens = new LinkedList<>();
    }

    @Override
    public void accept(Character character) {
        currentInput.add(character);

        switch (currentState) {
            case START -> switchFromStart();
            case OP_NAME -> switchFromOperationName();
            case OP_NAME_TERMINATOR -> switchFromOperationNameTerminator();
            case ARGUMENT -> switchFromArgument();
            case ARGUMENT_TERMINATOR -> switchFromArgumentTerminator();
            case OP_TERMINATOR -> switchFromOperationTerminator();
        }
    }

    Multiplier currentOperation() {
        validateCurrentTokens();
        return new Multiplier(currentTokens.get(1), currentTokens.get(2));
    }

    void reset() {
        switchBackToStart();
    }

    List<String> currentTokens() {
        // method not required but implemented 'cause n-ary operators of any name will be detected
        return Collections.unmodifiableList(currentTokens);
    }

    boolean foundOperation() {
        // detector has actually no restriction on operation name and argument size so we need to apply
        // some constraints here

        return hasOperationName()
                && isAllowedOperation()
                && hasArgumentCount(2)
                && hasAllowedArgumentSize(currentTokens.get(1))
                && hasAllowedArgumentSize(currentTokens.get(2));
    }

    private void switchFromStart() {
        if (isFirstLetterOfOperator(currentInput.getLast())) {
            switchToOpName();
        } else {
            switchBackToStart();
        }
    }

    private void switchFromOperationName() {
        if (isLetterOfOperator(currentInput.getLast())) {
            switchToOpName();
        } else if (isOperationNameTerminator(currentInput.getLast())) {
            switchToOpNameTerminator();
        } else {
            switchBackToStart();
        }
    }

    private void switchFromOperationNameTerminator() {
        if (isLetterOfArgument(currentInput.getLast())) {
            switchToArgument();
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            switchBackToOpName();
        } else {
            switchBackToStart();
        }
    }

    private void switchFromArgument() {
        if (isLetterOfArgument(currentInput.getLast())) {
            switchToArgument();
        } else if (isArgumentTerminator(currentInput.getLast())) {
            switchToArgumentTerminator();
        } else if (isOperationTerminator(currentInput.getLast())) {
            switchToOperationTerminator();
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            switchBackToOpName();
        } else {
            switchBackToStart();
        }
    }

    private void switchFromArgumentTerminator() {
        if (isLetterOfArgument(currentInput.getLast())) {
            switchToArgument();
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            switchBackToOpName();
        } else {
            switchBackToStart();
        }
    }

    private void switchFromOperationTerminator() {
        // System.out.println("End state ignores %c".formatted(currentInput.getLast()));
    }

    private void switchToOperationTerminator() {
        currentState = OP_TERMINATOR;
        storeArgumentToken();
        clearCurrentInput();
    }

    private void switchToArgumentTerminator() {
        currentState = ARGUMENT_TERMINATOR;
        storeArgumentToken();
        clearCurrentInput();
    }

    private void switchToOpName() {
        currentState = OP_NAME;
    }

    private void switchToOpNameTerminator() {
        currentState = OP_NAME_TERMINATOR;
        storeOperatorToken();
        clearCurrentInput();
    }

    private void switchToArgument() {
        currentState = ARGUMENT;
    }

    private void switchBackToStart() {
        currentState = START;
        clearCurrentInput();
        clearArgumentTokens();
    }

    private void switchBackToOpName() {
        currentState = OP_NAME;
        resetCurrentInputTo(currentInput.getLast());
        clearArgumentTokens();
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

    private void resetCurrentInputTo(Character last) {
        currentInput.clear();
        currentInput.add(last);
    }

    private void clearArgumentTokens() {
        currentTokens.clear();
    }

    private static boolean isFirstLetterOfOperator(Character character) {
        return FIRST_LETTER_OF_OPERATORS.contains(character);
    }

    private static boolean isLetterOfOperator(Character character) {
        return ALLOWED_LETTERS_OF_OPERATORS.contains(character);
    }

    private static boolean isOperationNameTerminator(Character character) {
        return TERMINATING_LETTERS_OF_OPERATION_NAME.contains(character);
    }

    private static boolean isLetterOfArgument(Character character) {
        return ALLOWED_LETTERS_OF_ARGUMENT.contains(character);
    }

    private static boolean isArgumentTerminator(Character character) {
        return TERMINATING_LETTERS_OF_ARGUMENT.contains(character);
    }

    private static boolean isOperationTerminator(Character character) {
        return TERMINATING_LETTERS_OF_OPERATION.contains(character);
    }

    private boolean hasOperationName() {
        return !currentTokens.isEmpty();
    }

    private void validateCurrentTokens() {
        if (!hasOperationName()) {
            throw new IllegalStateException("missing operator name (token)");
        }
        if (!isAllowedOperation()) {
            throw new IllegalStateException("operator name not supported");
        }

        // actually only binary operations are allowed; the following code
        // needs refactoring when that constraint is lifted
        final var numberOfArgumentsAllowed = 2;

        if (!hasArgumentCount(numberOfArgumentsAllowed)) {
            throw new IllegalStateException("only binary operators are supported");
        }
        if (!hasAllowedArgumentSize(currentTokens.get(1))) {
            throw new IllegalStateException("only 1 to 3 digits are allowed for first argument");
        }
        if (!hasAllowedArgumentSize(currentTokens.get(2))) {
            throw new IllegalStateException("only 1 to 3 digits are allowed for second argument");
        }
    }

    private boolean isAllowedOperation() {
        return ALLOWED_OPERATION_NAMES.contains(currentTokens.getFirst());
    }

    private boolean hasArgumentCount(int numberOfArguments) {
        final var operationName = 1;

        return currentTokens.size() == operationName + numberOfArguments;
    }

    private boolean hasAllowedArgumentSize(String argument) {
        return !argument.isEmpty() && argument.length() <= MAX_NUMBER_OF_ARGUMENT_DIGITS;
    }
}
