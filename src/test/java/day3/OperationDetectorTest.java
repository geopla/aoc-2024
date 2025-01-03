package day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationDetectorTest {

    OperationDetector operationDetector;

    @BeforeEach
    void setUp() {
        operationDetector = new OperationDetector();
    }

    @Test
    @DisplayName("Should be in starting state when created")
    void shouldBeInStartStatingStateWhenCreated() {
        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isFalse();
    }

    @Test
    @DisplayName("Should detect a valid multiplier operation (no input noise)")
    void shouldDetectSingleValidMultiplier() {
        parseWhole("mul(2,4)");

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isTrue();
        assertThat(operationDetector.currentOperation()).isEqualTo(new Multiplier("2", "4"));
    }

    @ParameterizedTest
    @CsvSource({
            "'mul(mul(42,100)'",
            "'mul(2mul(42,100)'",
            "'mul(2,mul(42,100)'"
    })
    @DisplayName("Should restart properly after detecting an operation name start again")
    void shouldRestartProperlyAfterDetectingAnOperationNameStartAgain(String input) {
        parseWhole(input);

        assertThat(operationDetector.currentOperation()).isEqualTo(new Multiplier("42", "100"));
    }

    @Test
    @DisplayName("Should detect first valid multiplier only")
    void shouldDetectFirstValidMultiplierOnly() {
        parseWhole("xmul(mul(2,4)mul(17,42)");

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isTrue();
        assertThat(operationDetector.currentOperation()).isEqualTo(new Multiplier("2", "4"));
    }

    @ParameterizedTest
    @CsvSource({
            "mul(4*",
            "mul(6,9!",
            "?(12,34)",
            "mul ( 2 , 4 )"
    })
    @DisplayName("Should do nothing on corrupted input")
    void shouldDoNothingOnCorruptedInput(String input) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) {
        }

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("syntacticallyCorrenctNAryOperations")
    @DisplayName("Should detect syntactical correct n-ary operation (though not allowed)")
    void shouldDetectSyntacticalCorrectNArayOperation(String input, List<String> expectedTokens) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) {
        }

        assertThat(operationDetector.currentTokens()).containsExactlyElementsOf(expectedTokens);
    }

    static Stream<Arguments> syntacticallyCorrenctNAryOperations() {
        return Stream.of(
                arguments("mul(7)", List.of("mul", "7")),
                arguments("mul(2003,5)", List.of("mul", "2003", "5")),
                arguments("mul(2,50,420)", List.of("mul", "2", "50", "420")),
                arguments("mulmul(2,4)", List.of("mulmul", "2", "4"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "'?(2,4)', 'missing operator name (token)'",
            "'mulmul(2,4)', 'operator name not supported'",
            "'mul(2,50,420)', 'only binary operators are supported'",
            "'mul(2003,5)', 'only 1 to 3 digits are allowed for first argument'",
            "'mul(5,2003)', 'only 1 to 3 digits are allowed for second argument'"
    })
    @DisplayName("Should throw illegal state exception when current syntactically legal tokens violate allowed operations")
    void shouldThrowExceptionOnIllegalState(String input, String expectedMessage) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) { }

        assertThatIllegalStateException()
                .isThrownBy(() -> operationDetector.currentOperation())
                .withMessage(expectedMessage);
    }

    @ParameterizedTest
    @CsvSource({
            "'mulmul(2,4)'",
            "'mul(2,50,420)'",
            "'mul(2003,5)'",
            "'mul(5,2003)'",
            "'mul(7)'"
    })
    @DisplayName("Should reject syntactically correct but not allowed operations")
    void shouldRejectSyntacticallyCorrectButNotAllowedOperations(String input) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) { }

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isFalse();
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }

    private void parseWhole(String input) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) {
            // just eat up the whole input - won't happen in real life but makes the test easy
            // otherwise just use consecutive invocations of the consumer method accept()
        }
    }
}