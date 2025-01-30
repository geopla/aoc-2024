package day6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    @Test
    @DisplayName("Should have a dimension computed from a map input")
    void shouldHaveDimension() {
        var roomMap = """
                ...
                ...
                """;

        var roomSize = new Room(from(roomMap)).size();

        assertThat(roomSize).isEqualTo(new Room.Size(3, 2));
    }

    Reader from(String input) {
        return new StringReader(input);
    }
}