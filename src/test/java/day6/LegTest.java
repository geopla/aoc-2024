package day6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LegTest {

    @ParameterizedTest
    @MethodSource("computeEndPosition")
    @DisplayName("Should compute end position")
    void shouldComputeEndPosition(CardinalDirection direction, Room.Position expectedEndPosition) {
        var startPosition = new Room.Position(4, 7);
        var steps = 3;
        var leg = new Leg<>(startPosition, direction, steps, new Lifecycle.Planned());

        assertThat(leg.end()).isEqualTo(expectedEndPosition);
    }

    static Stream<Arguments> computeEndPosition() {
        return Stream.of(
            Arguments.arguments(CardinalDirection.NORTH, new Room.Position(4, 4)),
            Arguments.arguments(CardinalDirection.EAST,  new Room.Position(7, 7)),
            Arguments.arguments(CardinalDirection.SOUTH, new Room.Position(4, 10)),
            Arguments.arguments(CardinalDirection.WEST,  new Room.Position(1, 7))
        );
    }
}