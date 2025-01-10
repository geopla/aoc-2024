package day3;

import java.util.List;
import java.util.Set;

class OperationFactory {

    private static final Set<String> ALLOWED_OPERATION_NAMES = Set.of("mul");
    private static final int MAX_NUMBER_OF_ARGUMENT_DIGITS = 3;

    static Operation create(List<String> tokens) {
        return switch (operationName(tokens)) {
            case "mul" -> new Operation.Multiplier(operands(tokens));
            default -> {
                throw new IllegalArgumentException("operation %s is unknown".formatted(operationName(tokens)));
            }
        };
    }

    static boolean isRelevantOperation(List<String> tokens) {
        return isOperation(tokens)
                && isKnown(operationName(tokens))
                && isBinaryOperation(tokens);
    }

    static int operationArity(List<String> tokens) {
        var operationNameSize = 1;
        return tokens.size() - operationNameSize;
    }

    static boolean isBinaryOperation(List<String> tokens) {
        return operationArity(tokens) == 2;
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

    static boolean confirmsToArgumentDigitsConstraint(List<String> tokens) {
        return tokens.stream()
                .noneMatch(token -> token.length() > MAX_NUMBER_OF_ARGUMENT_DIGITS);
    }

    private static String operationName(List<String> tokens) {
        return tokens.getFirst();
    }

    private static List<String> operands(List<String> tokens) {
        return List.copyOf(tokens.subList(1, tokens.size()));
    }
}
