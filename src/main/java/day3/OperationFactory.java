package day3;

import java.util.List;
import java.util.Set;

class OperationFactory {

    private static final Set<String> COMPUTING_OPERATION_NAMES = Set.of("mul");
    private static final int MAX_NUMBER_OF_ARGUMENT_DIGITS = 3;
    private static final Set<String> CONDITIONAL_OPERATION_NAMES = Set.of("do", "dont");

    static Operation create(List<String> tokens) {
        return switch (operationName(tokens)) {
            case "mul" -> new Operation.Multiplier(operands(tokens));
            case "do" -> new Operation.Do();
            case "don't" -> new Operation.Dont();
            default -> new Operation.Unknown(tokens);
        };
    }

    public static boolean isComputing(Operation operation) {
        return operation instanceof Operation.Multiplier;
    }

    static boolean isConditional(Operation operation) {
        return operation instanceof Operation.Do || operation instanceof Operation.Dont;
    }

    public static boolean isBinary(Operation operation) {
        if (operation instanceof Operation.Multiplier multiplier) {
            return multiplier.factors().size() == 2;
        }
        return false;
    }

    private static String operationName(List<String> tokens) {
        return tokens.getFirst();
    }

    private static List<String> operands(List<String> tokens) {
        return List.copyOf(tokens.subList(1, tokens.size()));
    }
}
