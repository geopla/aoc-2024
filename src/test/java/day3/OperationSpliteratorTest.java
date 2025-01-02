package day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class OperationSpliteratorTest {

    OperationDetector operationDetector;

    @BeforeEach
    void setUp() {
        operationDetector = new OperationDetector();
    }

    @Test
    @DisplayName("Should create a single element stream")
    void shouldCreateSingleElementStream() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)");
        var operationSpliterator = new OperationSpliterator(inputSpliterator, operationDetector);
        Stream<Multiplier> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(new Multiplier("2", "4"));
    }

    @Test
    @DisplayName("Should create two element stream")
    void shouldCreateTwoElementStream() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)mul(3,5)");

        var operationSpliterator = new OperationSpliterator(inputSpliterator, operationDetector);
        Stream<Multiplier> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Multiplier("2", "4"),
                new Multiplier("3", "5")
        );
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }
}