package day6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {

    @Test
    @DisplayName("Should provide IntStream from InputStream")
    void shouldProvideIntStream() {
        var input = "A_9";
        var expectedCodePoints = input.codePoints().boxed().toList();

        IntStream intStream = Map.codePoints(from(input));

        assertThat(intStream).containsExactlyElementsOf(expectedCodePoints);
    }

    Reader from(String input) {
        return new StringReader(input);
    }
}