package day7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CalibrationEquationTest {

    @ParameterizedTest
    @CsvSource({
            "'[]', 0",
            "'[42]', 0",
            "'[42, 100]', 1",
            "'[42, 100, 77]', 2",
            "'[42, 100, 77, 32]', 3"
    })
    @DisplayName("Should provide number of required operators")
    void shouldProvideNumberOfRequiredOperators(
            @ConvertWith(ToListOfIntegerConverter.class) List<Integer> operands,
            int expectedOperatorsSize
    ) {
        var calibrationEquation = new CalibrationEquation(0, operands);

        var operatorsSize = calibrationEquation.operatorsSize();

        assertThat(operatorsSize).isEqualTo(expectedOperatorsSize);
    }

    @Test
    @DisplayName("Should add two values")
    void shouldAddTwoValues() {
        var calibrationEquation =  new CalibrationEquation(10, List.of(4, 6));

        var matchesExpectedResult = calibrationEquation.evaluate(Stream.of(
                Operator.ADDITION
        ));

        assertThat(matchesExpectedResult)
                .withFailMessage("addition delivers unexpected result")
                .isTrue();
    }

    @Test
    @DisplayName("Should multiply two values")
    void shouldMultiplyTwoValues() {
        var calibrationEquation =  new CalibrationEquation(21, List.of(3, 7));

        var matchesExpectedResult = calibrationEquation.evaluate(Stream.of(
                Operator.MULTIPLICATION
        ));

        assertThat(matchesExpectedResult)
                .withFailMessage("multiplication delivers unexpected result")
                .isTrue();
    }

    @Test
    @DisplayName("Should add more than two values")
    void shouldAddMoreThanTwoValues() {
        var calibrationEquation =  new CalibrationEquation(22, List.of(4, 6, 12));

        var matchesExpectedResult = calibrationEquation.evaluate(Stream.of(
                Operator.ADDITION,
                Operator.ADDITION
        ));

        assertThat(matchesExpectedResult)
                .withFailMessage("addition of more than two values delivers unexpected result")
                .isTrue();
    }

    @Test
    @DisplayName("Should apply different operators")
    void shouldApplyDifferentOperators() {
        var calibrationEquation =  new CalibrationEquation(42, List.of(4, 6, 4, 2));

        var matchesExpectedResult = calibrationEquation.evaluate(Stream.of(
                Operator.ADDITION,
                Operator.MULTIPLICATION,
                Operator.ADDITION
        ));

        assertThat(matchesExpectedResult)
                .withFailMessage("application of different operators delivers unexpected result")
                .isTrue();
    }
}