package day7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class CalibrationEquation {

    private final int result;
    private final List<Integer> operands;

    CalibrationEquation(int result, List<Integer> operands) {
        this.result = result;
        this.operands = new ArrayList<>(operands);
    }

    int operatorsSize() {
        return operands.size() < 2 ? 0 : operands.size() - 1;
    }

    boolean evaluate(Stream<Operator> operators) {
        Iterator<Integer> operandIterator = operands.iterator();
        int initialValue = operandIterator.next();

        int finalResult = operators.reduce(
                initialValue,
                (acc, operator) -> operator.operator().apply(acc, operandIterator.next()),
                (a, b) -> a  // unused, but three args reduce method is required to get the right acc type
        );

        return finalResult == result;
    }
}
