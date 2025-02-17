package day7;

import java.util.function.BinaryOperator;

public record Operator(char symbol, BinaryOperator<Integer> operator) {

    static final Operator ADDITION = new Operator('+', Integer::sum);
    static final Operator MULTIPLICATION = new Operator('*', (a, b) -> a * b);
}
