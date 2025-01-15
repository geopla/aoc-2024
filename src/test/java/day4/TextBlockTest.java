package day4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextBlockTest {

    String inputSample = """
                MMMSXXMASM
                MSAMXMSMSA
                AMXSXMAAMM
                MSAMASMSMX
                XMASAMXAMM
                XXAMMXXAMA
                SMSMSASXSS
                SAXAMASAAA
                MAMMMXMMMM
                MXMXAXMASX""";

    @Test
    @DisplayName("Should read from String")
    void shouldReadFromString() {
        TextBlock textBlock = TextBlock.from(inputSample);

        assertThat(textBlock.lines()).containsExactly(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        );
    }

    @Test
    @DisplayName("Should read from resource")
    void shouldReadFromResource() {
        var resourceName = "../day-4-input-sample.txt";
        var input = TextBlock.class.getResourceAsStream(resourceName);

        TextBlock textBlock = TextBlock.from(input);

        assertThat(textBlock.lines()).containsExactly(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        );
    }

    @Test
    @DisplayName("Should have user friendly toString() method, hiding the internal structure")
    void shouldHaveUserFriendlyToString() {
        var toString = TextBlock.from(inputSample).toString();

        assertThat(toString).isEqualTo(inputSample);
    }
}