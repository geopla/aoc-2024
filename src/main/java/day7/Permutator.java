package day7;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class Permutator {

    static Stream<Operator> operatorsFrom(IntStream symbols) {
        return symbols
                .mapToObj(symbol -> operatorFrom((char) symbol));
    }

    static Operator operatorFrom(char symbol) {
        return switch (symbol) {
            case '+' -> new Operator(symbol, (a, b) -> a + b);
            case '*' -> new Operator(symbol, (a, b) -> a * b);
            default -> throw new IllegalArgumentException("unrecognized operator symbol: '%c'".formatted(symbol));
        };
    }
}
