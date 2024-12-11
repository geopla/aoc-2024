package daytwo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReportTest {

    @ParameterizedTest(name = "input: \"{0}\" parsed: {1}")
    @FieldSource("levelsFromStringInput")
    @DisplayName("Should parse levels")
    void shouldParseLevels(String input, List<Integer> levels) {
        var report = new Report(input);

        assertThat(report.levels()).containsAnyElementsOf(levels);
    }

    static List<Arguments> levelsFromStringInput = Arrays.asList(
            arguments("", List.of()),
            arguments("42", List.of(42)),
            arguments("7 12", List.of(7, 12))
    );

    @Test
    @DisplayName("Should throw an exception on bad input")
    void shouldThrowExceptionOnInvalidInput() {
        assertThatRuntimeException().isThrownBy(() ->
                new Report("4 e")
                        .levels()
                        .forEach(level -> { })
        );
    }
}