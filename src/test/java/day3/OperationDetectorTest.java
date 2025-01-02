package day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Spliterator;

import static day3.OperationDetector.State.START;
import static org.assertj.core.api.Assertions.assertThat;

class OperationDetectorTest {

    OperationDetector operationDetector;

    @BeforeEach
    void setUp() {
        operationDetector = new OperationDetector();
    }

    @Test
    @DisplayName("Should be in start state when created")
    void shouldBeInStartStateWhenCreated() {
        assertThat(operationDetector.currentState()).isEqualTo(START);
    }

    @Test
    @DisplayName("Should detect a single valid multiplier operation (no input noise)")
    void shouldDetectSingleValidMultiplier() {
        Spliterator<Character> inputSpliterator = from("mul(2,4)");

        while (inputSpliterator.tryAdvance(operationDetector)) { }

        assertThat(operationDetector.current()).isEqualTo(new Multiplier("2", "4"));
        assertThat(operationDetector.currentState()).isEqualTo(START);
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }
}