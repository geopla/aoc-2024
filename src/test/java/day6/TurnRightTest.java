package day6;

import day6.Lifecycle.Computed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;
import static org.assertj.core.api.Assertions.assertThat;

class TurnRightTest {

    TurnStrategy turnRightStrategy = new TurnRight();

    @ParameterizedTest
    @MethodSource("turnRightOnObstructionOnly")
    @DisplayName("Should turn right by on obstruction when using default turn strategy")
    void shouldTurnRightOnObstructionOnly(Leg<Computed> leg, CardinalDirection expectedFacing) {
        assertThat(turnRightStrategy.changeDirectionOn(leg)).contains(expectedFacing);
    }

    static Stream<Arguments> turnRightOnObstructionOnly() {
        var start = new Room.Position(3, 3);

        return Stream.of(
            Arguments.arguments(new Leg<>(start, NORTH, 2, new Computed(OBSTRUCTION)), EAST),
            Arguments.arguments(new Leg<>(start, EAST, 2, new Computed(OBSTRUCTION)),  SOUTH),
            Arguments.arguments(new Leg<>(start, SOUTH, 2, new Computed(OBSTRUCTION)), WEST),
            Arguments.arguments(new Leg<>(start, WEST, 2, new Computed(OBSTRUCTION)),  NORTH)
        );
    }

    @ParameterizedTest
    @EnumSource(CardinalDirection.class)
    @DisplayName("Should keep facing in border when using default turn strategy")
    void shouldKeepFacingOnBorder(CardinalDirection comingFrom) {
        var legToBorder = new Leg<>(new Room.Position(3, 3), NORTH, 2, new Computed(BORDER));
        assertThat(turnRightStrategy.changeDirectionOn(legToBorder)).isEmpty();
    }
}