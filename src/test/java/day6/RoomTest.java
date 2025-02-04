package day6;

import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RoomTest {

    @ParameterizedTest
    @MethodSource("realizePlannedLegInEmptyRoom")
    @DisplayName("Should realize guards planned leg in room without any obstructions")
    void shouldRealizePlannedLegInEmptyRoom(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                ......
                ..^...
                ......
                ......
                ......""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> realizePlannedLegInEmptyRoom() {
        var start = new Position(2,1);

        return Stream.of(
                arguments(new LegPlanned(start, NORTH), new Leg(start, NORTH, 1, BORDER)),
                arguments(new LegPlanned(start, EAST),  new Leg(start, EAST,  3, BORDER)),
                arguments(new LegPlanned(start, SOUTH), new Leg(start, SOUTH, 3, BORDER)),
                arguments(new LegPlanned(start, WEST),  new Leg(start, WEST,  2, BORDER))
        );
    }

    @ParameterizedTest
    @MethodSource("realizePlannedLegInObstructedRoom")
    @DisplayName("Should realize guards planned leg in room with obstructions")
    void shouldRealizePlannedLegInObstructedRoom(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                ..#...
                #.^..#
                ......
                ..#...
                ..#...""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> realizePlannedLegInObstructedRoom() {
        var start = new Position(2,1);

        return Stream.of(
                arguments(new LegPlanned(start, NORTH), new Leg(start, NORTH, 0, OBSTRUCTION)),
                arguments(new LegPlanned(start, EAST),  new Leg(start, EAST,  2, OBSTRUCTION)),
                arguments(new LegPlanned(start, SOUTH), new Leg(start, SOUTH, 1, OBSTRUCTION)),
                arguments(new LegPlanned(start, WEST),  new Leg(start, WEST,  1, OBSTRUCTION))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheNorth")
    @DisplayName("Should compute available steps to the north")
    void shouldComputeAvailableStepsToTheNorth(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                .###
                ..##
                ...#
                ^^^^""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheNorth() {
        return Stream.of(
                arguments(new LegPlanned(new Position(0, 3), NORTH), new Leg(new Position(0, 3), NORTH, 3, BORDER)),
                arguments(new LegPlanned(new Position(1, 3), NORTH), new Leg(new Position(1, 3), NORTH, 2, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(2, 3), NORTH), new Leg(new Position(2, 3), NORTH, 1, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(3, 3), NORTH), new Leg(new Position(3, 3), NORTH, 0, OBSTRUCTION))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheEast")
    @DisplayName("Should compute available steps to the east")
    void shouldComputeAvailableStepsToTheEast(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                >...
                >..#
                >.##
                >###""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheEast() {
        return Stream.of(
                arguments(new LegPlanned(new Position(0, 0), EAST), new Leg(new Position(0, 0), EAST, 3, BORDER)),
                arguments(new LegPlanned(new Position(0, 1), EAST), new Leg(new Position(0, 1), EAST, 2, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(0, 2), EAST), new Leg(new Position(0, 2), EAST, 1, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(0, 3), EAST), new Leg(new Position(0, 3), EAST, 0, OBSTRUCTION))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheSouth")
    @DisplayName("Should compute available steps to the south")
    void shouldComputeAvailableStepsToTheSouth(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                vvvv
                ...#
                ..##
                .###""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheSouth() {
        return Stream.of(
                arguments(new LegPlanned(new Position(0, 0), SOUTH), new Leg(new Position(0, 0), SOUTH, 3, BORDER)),
                arguments(new LegPlanned(new Position(1, 0), SOUTH), new Leg(new Position(1, 0), SOUTH, 2, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(2, 0), SOUTH), new Leg(new Position(2, 0), SOUTH, 1, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(3, 0), SOUTH), new Leg(new Position(3, 0), SOUTH, 0, OBSTRUCTION))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheWest")
    @DisplayName("Should compute available steps to the west")
    void shouldComputeAvailableStepsToTheWest(LegPlanned legPlanned, Leg expectedLeg) {
        var room = Room.from("""
                ...<
                #..<
                ##.<
                ###<""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheWest() {
        return Stream.of(
                arguments(new LegPlanned(new Position(3, 0), WEST), new Leg(new Position(3, 0), WEST, 3, BORDER)),
                arguments(new LegPlanned(new Position(3, 1), WEST), new Leg(new Position(3, 1), WEST, 2, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(3, 2), WEST), new Leg(new Position(3, 2), WEST, 1, OBSTRUCTION)),
                arguments(new LegPlanned(new Position(3, 3), WEST), new Leg(new Position(3, 3), WEST, 0, OBSTRUCTION))
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