package day6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    @DisplayName("Should have obstacles read from map input NOT including the guard)")
    void shouldHaveObstacles() {
        var roomMap = """
                #....
                .#..#
                ....#
                """;

        var room = new Room(from(roomMap));

        assertThat(room.obstructions()).containsExactlyInAnyOrder(
                new Room.Obstruction('#', new Room.Position(0, 0)),
                new Room.Obstruction('#', new Room.Position(1, 1)),
                new Room.Obstruction('#', new Room.Position(4, 1)),
                new Room.Obstruction('#', new Room.Position(4, 2))
        );
    }

    @Test
    @DisplayName("Should have guard(s) - only one for now")
    void shouldHaveGuards() {
        var room = new Room(from("""
                #....
                .#..#
                ^...#
                """));

        var guard = room.guards().getFirst();

        assertAll("guard",
                () -> assertThat(guard.room()).isEqualTo(room),
                () -> assertThat(guard.startPosition()).isEqualTo(new Room.Position(0, 2)),
                () -> assertThat(guard.startFacing()).isEqualTo(CardinalDirection.NORTH)
        );
    }

    Reader from(String input) {
        return new StringReader(input);
    }
}