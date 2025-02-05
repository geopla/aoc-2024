package day6;

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
    void shouldTurnRightOnObstructionOnly(CardinalDirection currentFacing, CardinalDirection expectedFacing) {
        assertThat(turnRightStrategy.changeDirectionOn(OBSTRUCTION, currentFacing)).contains(expectedFacing);
    }

    static Stream<Arguments> turnRightOnObstructionOnly() {
        return Stream.of(
            Arguments.arguments(NORTH, EAST),
            Arguments.arguments(EAST,  SOUTH),
            Arguments.arguments(SOUTH, WEST),
            Arguments.arguments(WEST,  NORTH)
        );
    }

    @ParameterizedTest
    @EnumSource(CardinalDirection.class)
    @DisplayName("Should keep facing in border when using default turn strategy")
    void shouldKeepFacingOnBorder(CardinalDirection comingFrom) {
        assertThat(turnRightStrategy.changeDirectionOn(BORDER, comingFrom)).isEmpty();
    }
}