package day5.input;


import day5.input.PrintJobData.PageOrderRule;
import day5.input.PrintJobData.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

        var printJobDatas = SafetyManualUpdateInputParser.read(inputLines);

        assertThat(printJobDatas).containsExactlyInAnyOrder(
                new PageOrderRule(35, 97),
                new PageOrderRule(96, 93),
                new Update(List.of(64,93,35,38,97,16,79,36,81,28,58,77,31,11,51,73,86,32,48,47,61)),
                new Update(List.of(16,79,81,28,77,31,73,61,94,21,37,18,39,45,66))
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
    @DisplayName("Should recognize a page order rule line")
    void shouldRecognizePageOrderRule(String input, boolean expectedPageOrderRule) {
        assertThat(SafetyManualUpdateInputParser.isPageOrderRule(input)).isEqualTo(expectedPageOrderRule);
    }

    @Test
    @DisplayName("Should create a page order rule")
    void shouldCreatePageOrderRule() {
        var input = "13|65";

        PrintJobData printJobData = SafetyManualUpdateInputParser.printJobData(input);

        assertThat(printJobData).isEqualTo(new PageOrderRule(13, 65));
    }

    @ParameterizedTest
    @CsvSource({
            "'48,47,21,78,12,26,71,24,13', true",
            "'48', true",
            "'', false",
            "'   ', false",
            "'13|65', false",
    })
    @DisplayName("Should recognize an update input line")
    void shouldRecognizeUpdate(String input, boolean expectedUpdate) {
        assertThat(SafetyManualUpdateInputParser.isUpdate(input)).isEqualTo(expectedUpdate);
    }

    @Test
    @DisplayName("Should create an update")
    void shouldCreatePageUpdate() {
        var input = "61,13,29";

        PrintJobData printJobData = SafetyManualUpdateInputParser.printJobData(input);

        assertThat(printJobData).isEqualTo(new Update(List.of(61, 13, 29)));
    }

    @Test
    @DisplayName("Should read input from resource file")
    void shouldReadInputFromResourceFile() {
        var name = "day-5-sample-input.txt";
        Stream<String> lines = SafetyManualUpdateInputParser.fromResource(name);

        assertThat(lines).contains(
                "35|97",
                "96|93",
                "64,93,35,38,97,16,79,36,81,28,58,77,31,11,51,73,86,32,48,47,61",
                "16,79,81,28,77,31,73,61,94,21,37,18,39,45,66"
        );
    }
}