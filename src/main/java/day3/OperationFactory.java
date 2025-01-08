package day3;

import java.util.List;
import java.util.Set;

class OperationFactory {

    private static final Set<String> ALLOWED_OPERATION_NAMES = Set.of("mul");
    private static final int MAX_NUMBER_OF_ARGUMENT_DIGITS = 3;


    static Operation create(List<String> tokens) {
        return null;
    }

    static boolean isOperation(List<String> tokens) {
        return !tokens.isEmpty();
    }

    static boolean isKnown(String operationName) {
        return ALLOWED_OPERATION_NAMES.contains(operationName);
    }

    static boolean hasAtLeastOneOperand(List<String> tokens) {
        final var tokenSizeForUnaryOperation = 2;
        return tokens.size() >= tokenSizeForUnaryOperation;
    }

    public static boolean confirmsToArgumentDigitsConstraint(List<String> tokens) {
        return tokens.stream()
                .noneMatch(token -> token.length() > MAX_NUMBER_OF_ARGUMENT_DIGITS);
    }
}
