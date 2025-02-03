package day6;

import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static day6.Terminator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RoomTest {

    @ParameterizedTest
    @MethodSource("realizePlannedLegInEmptyRoom")
    @DisplayName("Should realize guards planned leg in room without any obstructions")
    void shouldRealizePlannedLegInEmptyRoom(CardinalDirection direction, Leg expectedLeg) {
        var room = Room.from("""
                ......
                ......
                ......
                ......
                ......""");

        Position start = new Position(2, 1);
        LegPlanned legPlanned = new LegPlanned(start, direction);

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> realizePlannedLegInEmptyRoom() {
        return Stream.of(
                arguments(NORTH, new Leg(new Position(2,1), NORTH, 1, BORDER)),
                arguments(EAST,  new Leg(new Position(2,1), EAST,  4, BORDER)),
                arguments(SOUTH, new Leg(new Position(2,1), SOUTH, 3, BORDER)),
                arguments(WEST,  new Leg(new Position(2,1), WEST,  2, BORDER))
        );
    }

    @Test
    @DisplayName("Should have a dimension computed from a map input")
    void shouldHaveDimension() {
        var roomMap = """
                ...
                ...
                """;

        var roomSize = Room.from(roomMap).size();

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

        var room = Room.from(roomMap);

        assertThat(room.obstructions()).containsExactlyInAnyOrder(
                new Room.Obstruction('#', new Position(0, 0)),
                new Room.Obstruction('#', new Position(1, 1)),
                new Room.Obstruction('#', new Position(4, 1)),
                new Room.Obstruction('#', new Position(4, 2))
        );
    }

    @Test
    @DisplayName("Should have guard(s) - only one for now")
    void shouldHaveGuards() {
        var room = Room.from("""
                #....
                .#..#
                ^...#
                """);

        var guard = room.guards().getFirst();

        assertAll("guard",
                () -> assertThat(guard.room()).isEqualTo(room),
                () -> assertThat(guard.startPosition()).isEqualTo(new Position(0, 2)),
                () -> assertThat(guard.startFacing()).isEqualTo(NORTH)
        );
    }

}