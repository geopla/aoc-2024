package day5;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SafetyManualUpdateInputParserTest {

    @ParameterizedTest
    @CsvSource({
            "'13|65', true",
            "'13|', false",
            "'13', false",
            "'', false",
            "'   ', false",
            "'48,47,21,78,12,26,71,24,13', false"
    })
    @DisplayName("Should recognize page order rule input (single line only)")
    void shouldRecognizePageOrderRule(String input, boolean expectedPageOrderRule) {
        assertThat(SafetyManualUpdateInputParser.isPageOrderRule(input)).isEqualTo(expectedPageOrderRule);
    }

    @ParameterizedTest
    @CsvSource({
            "'48,47,21,78,12,26,71,24,13', true",
            "'48', true",
            "'', false",
            "'   ', false",
            "'13|65', false",
    })
    @DisplayName("Should recognize update input (single line only)")
    void shouldRecognizeUpdate(String input, boolean expectedUpdate) {
        assertThat(SafetyManualUpdateInputParser.isUpdate(input)).isEqualTo(expectedUpdate);
    }
}