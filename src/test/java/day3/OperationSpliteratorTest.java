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

    @Test
    @DisplayName("Should detect operations from corrupted memory example")
    void shouldDetectOperationFromCorruptedMemoryExample() {
        Spliterator<Character> inputSpliterator = from("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))");

        var operationSpliterator = new OperationSpliterator(inputSpliterator, operationDetector);
        Stream<Multiplier> operationStream = StreamSupport.stream(operationSpliterator, false);

        assertThat(operationStream).containsExactly(
                new Multiplier("2", "4"),
                new Multiplier("5", "5"),
                new Multiplier("11", "8"),
                new Multiplier("8", "5")
        );
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }
}