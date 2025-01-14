package day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class OperationActivatorSpliteratorTest {

    OperationDetector operationDetector;

    @BeforeEach
    void setUp() {
        operationDetector = new OperationDetector();
    }

    @Test
    @DisplayName("Should create stream when DO is NOT present at start")
    void shouldCreateStreamWhenDoIsNotPresent() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)mul(3,5)don't()");

        var operationSpliterator = new OperationActivatorSpliterator(inputSpliterator, operationDetector);
        Stream<Operation> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Operation.Multiplier("2", "4"),
                new Operation.Multiplier("3", "5")
        );
    }

    @Test
    @DisplayName("Should create stream when DO is present at start")
    void shouldCreateStreamWhenDoIsPresent() {
        Spliterator<Character> inputSpliterator = from("do()mul(2,4)mul(3,5)don't()");

        var operationSpliterator = new OperationActivatorSpliterator(inputSpliterator, operationDetector);
        Stream<Operation> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Operation.Multiplier("2", "4"),
                new Operation.Multiplier("3", "5")
        );
    }

    @Test
    @DisplayName("Should create stream ignoring operations after DONT")
    void shouldCreateStreamWhenActivatorIsPresent() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)don't()mul(3,5)");

        var operationSpliterator = new OperationActivatorSpliterator(inputSpliterator, operationDetector);
        Stream<Operation> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Operation.Multiplier("2", "4")
        );
    }


    @Test
    @DisplayName("Should resume after detecting DO again")
    void shouldResumeAfterDetectingDoAgain() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)don't()mul(3,5)do()mul(4,6)");

        var operationSpliterator = new OperationActivatorSpliterator(inputSpliterator, operationDetector);
        Stream<Operation> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Operation.Multiplier("2", "4"),
                new Operation.Multiplier("4", "6")
        );
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }
}