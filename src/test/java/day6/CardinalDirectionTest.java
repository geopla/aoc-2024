package day6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day6.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CardinalDirectionTest {

    @ParameterizedTest
    @MethodSource("turnLeft")
    @DisplayName("Should turn left")
    void shouldTurnLeft(CardinalDirection actualDirection, CardinalDirection expectedDirection) {
        assertThat(actualDirection.turnLeft()).isEqualTo(expectedDirection);
    }

    static Stream<Arguments> turnLeft() {
        return Stream.of(
                arguments(NORTH, WEST),
                arguments(WEST, SOUTH),
                arguments(SOUTH, EAST),
                arguments(EAST, NORTH)
        );
    }

    @ParameterizedTest
    @MethodSource("turnRight")
    @DisplayName("Should turn right")
    void shouldTurnRight(CardinalDirection actualDirection, CardinalDirection expectedDirection) {
        assertThat(actualDirection.turnRight()).isEqualTo(expectedDirection);
    }

    static Stream<Arguments> turnRight() {
        return Stream.of(
                arguments(NORTH, EAST),
                arguments(EAST, SOUTH),
                arguments(SOUTH, WEST),
                arguments(WEST, NORTH)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "^, NORTH",
            ">, EAST",
            "v, SOUTH",
            "<, WEST"
    })
    @DisplayName("Should create direction from symbol")
    void shouldCreateFromSymbol(char symbol, CardinalDirection expectedDirection) {
        assertThat(CardinalDirection.from(symbol)).isEqualTo(expectedDirection);
    }

    @Test
    @DisplayName("Shold reject unknown direction symbol")
    void shouldRejectUnknownDirectionSymbol() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                        CardinalDirection.from('X'))
                .withMessage("unknown direction symbol 'X'");
    }
}