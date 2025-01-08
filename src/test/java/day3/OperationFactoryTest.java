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
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationFactoryTest {

    @Test
    @DisplayName("Should create multipliers")
    void shouldCreateMultiplier() {
        Operation multiplier = OperationFactory.create(of("mul", "4", "2"));

        assertThat(multiplier).isInstanceOf(Operation.Multiplier.class);
    }

    @Test
    @DisplayName("Should verify that empty tokens is NOT a valid operation")
    void shouldVerifyThatEmptyTokensIsNotAValidOperation() {
        List<String> tokens = List.of();
        assertThat(OperationFactory.isOperation(tokens)).isFalse();
    }

    @Test
    @DisplayName("Should verify that NOT empty tokens represent an operation")
    void shouldVerifyThatNotEmpptyTokensRepresentAnOperation() {
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
                arguments(List.of("mul", "2", "-1"), true),
                arguments(List.of("mul", "3", "-1", "-2"), true),
                arguments(List.of("nop"), false)
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