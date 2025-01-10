package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationFactoryTest {

    @Test
    @DisplayName("Should create multipliers")
    void shouldCreateMultiplier() {
        Operation multiplier = OperationFactory.create(of("mul", "4", "2"));
        assertThat(multiplier).isEqualTo(new Operation.Multiplier(List.of("4", "2")));
    }

    @ParameterizedTest
    @MethodSource("relevantOperation")
    @DisplayName("Should verify relevant operations")
    void shouldVerifyRelevantOperation(List<String> tokens, boolean expectedRelevant) {
        assertThat(OperationFactory.isRelevantOperation(tokens)).isEqualTo(expectedRelevant);
    }

    static Stream<Arguments> relevantOperation() {
        return Stream.of(
                arguments(List.of("mul", "1"), false),
                arguments(List.of("mul", "2", "1"), true),
                arguments(List.of("mul", "3", "4", "5"), false),
                arguments(List.of("xxx", "2", "3"), false)
        );
    }

    @Test
    @DisplayName("Should throw exception when operation is unknown")
    void shouldThrowExceptionWhenOperationIsUnknown() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                OperationFactory.create(List.of("xxx", "2", "6")));
    }

    @Test
    @DisplayName("Should verify that empty tokens is NOT a valid operation")
    void shouldVerifyThatEmptyTokensIsNotAValidOperation() {
        List<String> tokens = List.of();
        assertThat(OperationFactory.isOperation(tokens)).isFalse();
    }

    @Test
    @DisplayName("Should verify that NOT empty tokens represent an operation")
    void shouldVerifyThatNotEmptyTokensRepresentAnOperation() {
        var tokens = List.of("sin", "90");
        assertThat(OperationFactory.isOperation(tokens)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "'sin', false",
            "'mulmul', false",
            "'mul', true"
    })
    @DisplayName("Should verify that operation is known")
    void shouldVerifyThatOperationIsAllowed(String operationName, boolean expectedResult) {
        assertThat(OperationFactory.isKnown(operationName)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("atLeastOneOperandIsPresent")
    @DisplayName("Should verify that at least one operand is present")
    void shouldVerifyThatAtLeastOneOperandIsPresent(List<String> tokens, boolean expectedResult) {
        assertThat(OperationFactory.hasAtLeastOneOperand(tokens)).isEqualTo(expectedResult);
    }

    static Stream<Arguments> atLeastOneOperandIsPresent() {
        return Stream.of(
                arguments(List.of("mul", "1"), true),
                arguments(List.of("mul", "2", "3"), true),
                arguments(List.of("mul", "3", "4", "5"), true),
                arguments(List.of("nop"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("verifyOperationArity")
    @DisplayName("Should verify operation arity")
    void shouldVerifyOperationArity(List<String> tokens, int expectedArity) {
        assertThat(OperationFactory.operationArity(tokens)).isEqualTo(expectedArity);
    }

    static Stream<Arguments> verifyOperationArity() {
        return Stream.of(
                arguments(List.of("mul", "1"), 1),
                arguments(List.of("mul", "2", "3"), 2),
                arguments(List.of("mul", "3", "4", "5"), 3)
        );
    }

    @ParameterizedTest
    @MethodSource("verifyOperationBinarity")
    @DisplayName("Should verify binarity")
    void shouldVerifyBinarity(List<String> tokens, boolean expectedBinarity) {
        assertThat(OperationFactory.isBinaryOperation(tokens)).isEqualTo(expectedBinarity);
    }

    static Stream<Arguments> verifyOperationBinarity() {
        return Stream.of(
                arguments(List.of("mul", "1"), false),
                arguments(List.of("mul", "2", "3"), true),
                arguments(List.of("mul", "3", "4", "5"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("numberOfArgumentDigitsConfirmsToConstraint")
    @DisplayName("Should verify that number of argument digits confirm constraint")
    void shouldVerifyThatNumberOfArgumentDigitsConfirmsToConstraint(List<String> tokens, boolean expectedResult) {
        assertThat(OperationFactory.confirmsToArgumentDigitsConstraint(tokens)).isEqualTo(expectedResult);
    }

    static Stream<Arguments> numberOfArgumentDigitsConfirmsToConstraint() {
        return Stream.of(
                arguments(List.of("mul", "0", "42", "999"), true),
                arguments(List.of("mul", "2001"), false),
                arguments(List.of("mul", "3", "2001", "2"), false)
        );
    }
}