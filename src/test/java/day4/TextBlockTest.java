package day4;

import daytwo.Report;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource(textBlock = """
              0,  0, true
             -1,  0, false
              0, -1, false
              
              9,  0, true
              9, -1, false
             10,  0, false
             
              9,  2, true
             10,  2, false
              9,  3, false
              
              0,  2, true
              0,  3, false
             -1,  2, false""")
    @DisplayName("Should verify charAt()")
    void shouldVerifyCharAt(int x, int y, boolean expectedCharAt) {
        TextBlock textBlock = TextBlock.from("""
                XMMSXXMASM
                MSAMXMSMSA
                SMXSXMAAMA""");

        assertThat(textBlock.hasCharAt(x, y)).isEqualTo(expectedCharAt);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            0,  0, 'X'
            9,  0, 'M'
            9,  2, 'A'
            0,  2, 'S'
            """)
    @DisplayName("Should return char at given postion")
    void shouldReturnCharAt(int x, int y, char expectedChar) {
        TextBlock textBlock = TextBlock.from("""
                XMMSXXMASM
                MSAMXMSMSA
                SMXSXMAAMA""");

        assertThat(textBlock.charAt(x, y)).isEqualTo(expectedChar);
    }

    @Test
    @DisplayName("Should provide Star on position top left")
    void shouldProvideTopLeftStar() {
        TextBlock textBlock = TextBlock.from(inputSample);

        Star star = textBlock.star();

        assertThat(star.position()).isEqualTo(new Star.Position(0, 0));
        assertThat(star.centerValue()).isEqualTo('M');
    }

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