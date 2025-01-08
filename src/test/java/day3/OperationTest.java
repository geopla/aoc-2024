package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationTest {

    @Test
    @DisplayName("Should return zero when multiply with zero operands")
    void shouldReturnZeroWhenMultiplyWithZeroOperands() {
        var multiplier = new Operation.Multiplier(Collections.emptyList());

        assertThat(multiplier.apply()).isZero();
    }

    @ParameterizedTest
    @MethodSource("productsOfNonEmptyOperands")
    @DisplayName("Should compute product from non empty operands list")
    void shouldReturnOperandWhenMultiplyWithSingleArgument(List<String> operands, int expectedResult) {
        var multiplier = new Operation.Multiplier(operands);

        assertThat(multiplier.apply()).isEqualTo(expectedResult);
    }

    static Stream<Arguments> productsOfNonEmptyOperands() {
        return Stream.of(
                arguments(List.of("1"), 1),
                arguments(List.of("2", "5"), 10),
                arguments(List.of("3", "7"), 21)
        );
    }
}