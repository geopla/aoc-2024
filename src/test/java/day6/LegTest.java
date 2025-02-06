package day6;

import day6.Lifecycle.Computed;
import day6.Room.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static day6.Terminator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LegTest {

    @ParameterizedTest
    @MethodSource("computeEndPosition")
    @DisplayName("Should compute end position")
    void shouldComputeEndPosition(CardinalDirection direction, Position expectedEndPosition) {
        var startPosition = new Position(4, 7);
        var steps = 3;
        var leg = new Leg<>(startPosition, direction, steps, new Lifecycle.Planned());

        assertThat(leg.end()).isEqualTo(expectedEndPosition);
    }

    static Stream<Arguments> computeEndPosition() {
        return Stream.of(
                arguments(NORTH, new Position(4, 4)),
                arguments(CardinalDirection.EAST, new Position(7, 7)),
                arguments(CardinalDirection.SOUTH, new Position(4, 10)),
                arguments(CardinalDirection.WEST, new Position(1, 7))
        );
    }

    @ParameterizedTest
    @MethodSource("providePositions")
    @DisplayName("Should provide steps to the end with start position included")
    void shouldProvidePositions(Leg<Computed> leg, List<Position> expectedPositions) {
        assertThat(leg.positions()).containsExactlyElementsOf(expectedPositions);
    }

    static Stream<Arguments> providePositions() {
        var startPosition = new Position(4, 7);

        return Stream.of(
                arguments(
                        new Leg<>(startPosition, NORTH, 2, new Computed(OBSTRUCTION)),
                        List.of(
                                new Position(4, 7),
                                new Position(4, 6),
                                new Position(4, 5))),
                arguments(
                        new Leg<>(startPosition, EAST, 2, new Computed(OBSTRUCTION)),
                        List.of(
                                new Position(4, 7),
                                new Position(5, 7),
                                new Position(6, 7))),
                arguments(
                        new Leg<>(startPosition, SOUTH, 2, new Computed(OBSTRUCTION)),
                        List.of(
                                new Position(4, 7),
                                new Position(4, 8),
                                new Position(4, 9))),
                arguments(
                        new Leg<>(startPosition, WEST, 2, new Computed(OBSTRUCTION)),
                        List.of(
                                new Position(4, 7),
                                new Position(3, 7),
                                new Position(2, 7))
                )
        );
    }

    @ParameterizedTest
    @EnumSource(CardinalDirection.class)
    @DisplayName("Should provide start position only when leg has zero steps")
    void shouldProvideStartPositionOnlyWhenLegHasZeroSteps(CardinalDirection direction) {
        var startPosition = new Position(4, 7);
        var zeroSteps = 0;
        var leg = new Leg<>(startPosition, direction, zeroSteps, new Computed(OBSTRUCTION));

        assertThat(leg.positions()).containsExactly(startPosition);
    }
}