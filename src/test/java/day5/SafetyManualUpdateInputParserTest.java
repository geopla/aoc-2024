package day5;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SafetyManualUpdateInputParserTest {


    @Test
    @DisplayName("Should parse print job data")
    void shouldParsePrintJobData() {
        var inputLines = Stream.of(
                "35|97",
                "96|93",

                "64,93,35,38,97,16,79,36,81,28,58,77,31,11,51,73,86,32,48,47,61",
                "16,79,81,28,77,31,73,61,94,21,37,18,39,45,66"
        );

        var printJob = SafetyManualUpdateInputParser.createPrintJob(inputLines);

        assertThat(printJob).isNotNull();
    }

    @Test
    @DisplayName("Should parse page order rule (single line)")
    void shouldParsePageOrderRule() {
        PrintJobData printJobData = SafetyManualUpdateInputParser.printJobData("35|97");

        assertThat(printJobData).isInstanceOf(PrintJobData.PageOrderRule.class);
        assertThat(printJobData).isEqualTo(new PrintJobData.PageOrderRule(35, 97));
    }

    @ParameterizedTest
    @MethodSource("parseUpdate")
    @DisplayName("Should parse update (single line)")
    void shouldParseUpdate(String input, List<Integer> expectedParseResult) {
        PrintJobData printJobData = SafetyManualUpdateInputParser.printJobData(input);

        assertThat(printJobData).isInstanceOf(PrintJobData.Update.class);
        assertThat(printJobData).isEqualTo(new PrintJobData.Update(expectedParseResult));
    }

    static Stream<Arguments> parseUpdate() {
        return Stream.of(
          Arguments.arguments("42,200,100", List.of(42, 200, 100)),
          Arguments.arguments("42,100", List.of(42, 100)),
          Arguments.arguments("42", List.of(42))
        );
    }

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

    @Test
    @DisplayName("Should read input from resource file")
    void shouldReadInputFromResourceFile() {
        var name = "../day-5-sample-input.txt";
        Stream<String> lines = SafetyManualUpdateInputParser.fromResource(name);

        assertThat(lines).contains(
                "35|97",
                "96|93",
                "64,93,35,38,97,16,79,36,81,28,58,77,31,11,51,73,86,32,48,47,61",
                "16,79,81,28,77,31,73,61,94,21,37,18,39,45,66"
        );
    }
}