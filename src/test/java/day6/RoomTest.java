package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;
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
    void shouldRealizePlannedLegInEmptyRoom(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
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
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(start, NORTH, steps, planned), new Leg<>(start, NORTH, 1, new Computed(BORDER))),
                arguments(new Leg<>(start, EAST,  steps, planned), new Leg<>(start, EAST,  3, new Computed(BORDER))),
                arguments(new Leg<>(start, SOUTH, steps, planned), new Leg<>(start, SOUTH, 3, new Computed(BORDER))),
                arguments(new Leg<>(start, WEST,  steps, planned), new Leg<>(start, WEST,  2, new Computed(BORDER)))
        );
    }

    @ParameterizedTest
    @MethodSource("realizePlannedLegInObstructedRoom")
    @DisplayName("Should realize guards planned leg in room with obstructions")
    void shouldRealizePlannedLegInObstructedRoom(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
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
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(start, NORTH, steps, planned), new Leg<>(start, NORTH, 0, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(start, EAST,  steps, planned), new Leg<>(start, EAST,  2, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(start, SOUTH, steps, planned), new Leg<>(start, SOUTH, 1, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(start, WEST,  steps, planned), new Leg<>(start, WEST,  1, new Computed(OBSTRUCTION)))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheNorth")
    @DisplayName("Should compute available steps to the north")
    void shouldComputeAvailableStepsToTheNorth(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
        var room = Room.from("""
                .###
                ..##
                ...#
                ^^^^""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheNorth() {
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(new Position(0, 3), NORTH, steps, planned), new Leg<>(new Position(0, 3), NORTH, 3, new Computed(BORDER))),
                arguments(new Leg<>(new Position(1, 3), NORTH, steps, planned), new Leg<>(new Position(1, 3), NORTH, 2, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(2, 3), NORTH, steps, planned), new Leg<>(new Position(2, 3), NORTH, 1, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(3, 3), NORTH, steps, planned), new Leg<>(new Position(3, 3), NORTH, 0, new Computed(OBSTRUCTION)))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheEast")
    @DisplayName("Should compute available steps to the east")
    void shouldComputeAvailableStepsToTheEast(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
        var room = Room.from("""
                >...
                >..#
                >.##
                >###""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheEast() {
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(new Position(0, 0), EAST, steps, planned), new Leg<>(new Position(0, 0), EAST,3, new Computed(BORDER))),
                arguments(new Leg<>(new Position(0, 1), EAST, steps, planned), new Leg<>(new Position(0, 1), EAST,2, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(0, 2), EAST, steps, planned), new Leg<>(new Position(0, 2), EAST,1, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(0, 3), EAST, steps, planned), new Leg<>(new Position(0, 3), EAST,0, new Computed(OBSTRUCTION)))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheSouth")
    @DisplayName("Should compute available steps to the south")
    void shouldComputeAvailableStepsToTheSouth(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
        var room = Room.from("""
                vvvv
                ...#
                ..##
                .###""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheSouth() {
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(new Position(0, 0), SOUTH, steps, planned), new Leg<>(new Position(0, 0), SOUTH,3, new Computed(BORDER))),
                arguments(new Leg<>(new Position(1, 0), SOUTH, steps, planned), new Leg<>(new Position(1, 0), SOUTH,2, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(2, 0), SOUTH, steps, planned), new Leg<>(new Position(2, 0), SOUTH,1, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(3, 0), SOUTH, steps, planned), new Leg<>(new Position(3, 0), SOUTH,0, new Computed(OBSTRUCTION)))
        );
    }

    @ParameterizedTest
    @MethodSource("computeAvailableStepsToTheWest")
    @DisplayName("Should compute available steps to the west")
    void shouldComputeAvailableStepsToTheWest(Leg<Planned> legPlanned, Leg<Computed> expectedLeg) {
        var room = Room.from("""
                ...<
                #..<
                ##.<
                ###<""");

        assertThat(room.realize(legPlanned)).isEqualTo(expectedLeg);
    }

    static Stream<Arguments> computeAvailableStepsToTheWest() {
        var steps = Integer.MAX_VALUE;
        var planned = new Planned();

        return Stream.of(
                arguments(new Leg<>(new Position(3, 0), WEST, steps, planned), new Leg<>(new Position(3, 0), WEST,3, new Computed(BORDER))),
                arguments(new Leg<>(new Position(3, 1), WEST, steps, planned), new Leg<>(new Position(3, 1), WEST,2, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(3, 2), WEST, steps, planned), new Leg<>(new Position(3, 2), WEST,1, new Computed(OBSTRUCTION))),
                arguments(new Leg<>(new Position(3, 3), WEST, steps, planned), new Leg<>(new Position(3, 3), WEST,0, new Computed(OBSTRUCTION)))
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