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

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
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
        assertThat(operationDetector.operation()).isEqualTo(new Operation.Multiplier(of("2", "4")));
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

        assertThat(operationDetector.operation()).isEqualTo(new Operation.Multiplier(of("42", "100")));
    }

    @Test
    @DisplayName("Should detect first valid multiplier only")
    void shouldDetectFirstValidMultiplierOnly() {
        parseWhole("xmul(mul(2,4)mul(17,42)");

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isTrue();
        assertThat(operationDetector.operation()).isEqualTo(new Operation.Multiplier(of("2", "4")));
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
        parseWhole(input);

        assertThat(operationDetector.foundOperation())
                .as("found operation")
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("syntacticallyCorrenctNAryOperations")
    @DisplayName("Should detect syntactical correct n-ary operation (though not allowed)")
    void shouldDetectSyntacticalCorrectNArayOperation(String input, List<String> expectedTokens) {
        parseWhole(input);

        assertThat(operationDetector.currentTokens()).containsExactlyElementsOf(expectedTokens);
    }

    static Stream<Arguments> syntacticallyCorrenctNAryOperations() {
        return Stream.of(
                arguments("mul(7)", of("mul", "7")),
                arguments("mul(2003,5)", of("mul", "2003", "5")),
                arguments("mul(2,50,420)", of("mul", "2", "50", "420")),
                arguments("mulmul(2,4)", of("mulmul", "2", "4"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "'?(2,4)', 'missing operator name (token)'",
            "'mulmul(2,4)', 'operator name not supported'",
            "'mul(5,2003)', 'only 1 to 3 digits are allowed for any argument'"
    })
    @DisplayName("Should throw illegal state exception when current syntactically legal tokens violate allowed operations")
    void shouldThrowExceptionOnIllegalState(String input, String expectedMessage) {
        parseWhole(input);

        assertThat(operationDetector.foundOperation()).isFalse();

        assertThatIllegalStateException()
                .isThrownBy(() -> operationDetector.operation())
                .withMessage(expectedMessage);
    }



    @Test
    @DisplayName("Should be resettable to read another operation")
    void shouldBeResettable() {
        // mul(2,4)mul(3,5)

        operationDetector.accept('m');
        operationDetector.accept('u');
        operationDetector.accept('l');
        operationDetector.accept('(');
        operationDetector.accept('2');
        operationDetector.accept(',');
        operationDetector.accept('4');
        operationDetector.accept(')');

        assertThat(operationDetector.operation()).isEqualTo(new Multiplier("2", "4"));

        operationDetector.reset();
        assertThat(operationDetector.foundOperation()).isFalse();

        operationDetector.accept('m');
        operationDetector.accept('u');
        operationDetector.accept('l');
        operationDetector.accept('(');
        operationDetector.accept('3');
        operationDetector.accept(',');
        operationDetector.accept('5');
        operationDetector.accept(')');

        assertThat(operationDetector.operation()).isEqualTo(new Multiplier("3", "5"));
    }

    static Spliterator<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c)
                .spliterator();
    }

    private void parseWhole(String input) {
        Spliterator<Character> inputSpliterator = from(input);

        while (inputSpliterator.tryAdvance(operationDetector)) {
            // just eat up the whole input - won't happen in real life but makes the test easier to implement
            // otherwise you need to use consecutive invocations of the consumer method accept()
        }
    }
}