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

    private static final Set<Character> firstLetterOfOperators = Set.of('m');
    private static final Set<Character> allowedLettersOfOperators = Set.of('l', 'm', 'u');
    private static final Set<Character> terminatingLettersOfOperatorName = Set.of('(');
    private static final Set<Character> allowedLettersOfArgument = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Set<Character> terminatingLettersOfArgument = Set.of(',');
    private static final Set<Character> terminatingLettersOfOperation = Set.of(')');
    private static final Set<String> allowedOperationNames = Set.of("mul");

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
            case OP_NAME -> switchFromOpName();
            case OP_NAME_TERMINATOR -> switchFromOpNameTerminator();
            case ARGUMENT -> switchFromArgument();
            case ARGUMENT_TERMINATOR -> switchFromArgumentTerminator();
            case OP_TERMINATOR -> switchFromOpTerminator();
        }
    }

    Multiplier currentOperation() {
        validateCurrentTokens();

        return new Multiplier(currentTokens.get(1), currentTokens.get(2));
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
                && hasExactlyTwoArguments()
                && hasAllowedArgumentSize(currentTokens.get(1))
                && hasAllowedArgumentSize(currentTokens.get(2));
    }

    private void switchFromStart() {
        if (isFirstLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
        } else {
            currentState = START;
        }
    }

    private void switchFromOpName() {
        if (isLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
        } else if (isOperationNameTerminator(currentInput.getLast())) {
            currentState = OP_NAME_TERMINATOR;
            storeOperatorToken();
            clearCurrentInput();
        } else {
            currentState = START;
            clearCurrentInput();
        }
    }

    private void switchFromOpNameTerminator() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
            resetCurrentInputTo(currentInput.getLast());
            clearArgumentTokens();
        } else {
            currentState = START;
            clearCurrentInput();
            clearArgumentTokens();
        }
    }

    private void switchFromArgument() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        } else if (isArgumentTerminator(currentInput.getLast())) {
            currentState = ARGUMENT_TERMINATOR;
            storeArgumentToken();
            clearCurrentInput();
        } else if (isOperationTerminator(currentInput.getLast())) {
            currentState = OP_TERMINATOR;
            storeArgumentToken();
            clearCurrentInput();
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
            resetCurrentInputTo(currentInput.getLast());
            clearArgumentTokens();
        } else {
            currentState = START;
            clearCurrentInput();
            clearArgumentTokens();
        }
    }

    private void switchFromArgumentTerminator() {
        if (isLetterOfArgument(currentInput.getLast())) {
            currentState = ARGUMENT;
        } else if (isFirstLetterOfOperator(currentInput.getLast())) {
            currentState = OP_NAME;
            resetCurrentInputTo(currentInput.getLast());
            clearArgumentTokens();
        } else {
            currentState = START;
            clearCurrentInput();
            clearArgumentTokens();
        }
    }

    private void switchFromOpTerminator() {
        System.out.println("End state ignores %c".formatted(currentInput.getLast()));
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
        if (!hasExactlyTwoArguments()) {
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
        return allowedOperationNames.contains(currentTokens.getFirst());
    }

    private boolean hasExactlyTwoArguments() {
        return currentTokens.size() == 3;
    }

    private boolean hasAllowedArgumentSize(String argument) {
        return !argument.isEmpty() && argument.length() <= 3;
    }

}
