package day4;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day4.Star.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StarTest {

    static TextBlock textBlock;

    @BeforeAll
    static void beforeAll() {
        textBlock = TextBlock.from("""
                MMMSXXMASM
                MSAMXMSMSA
                AMXSXMAAMM
                MSAMASMSMX
                XMASAMXAMM
                XXAMMXXAMA
                SMSMSASXSS
                SAXAMASAAA
                MAMMMXMMMM
                MXMXAXMASX
                """
        );
    }

    @ParameterizedTest
    @MethodSource("neighboursInQuadrantSE")
    @DisplayName("Should have only neighbours in SE quadrant when being on top left")
    void shouldHaveDirectNeighboursOnlyInSouthEastQuadrandtWhenBeingOnTopLeft(
            Star.CardinalDirection cardinalDirection,
            boolean expectedNeighbour) {

        var star = textBlock.star();

        assertThat(star.hasNeighbour(cardinalDirection)).isEqualTo(expectedNeighbour);
    }

    static Stream<Arguments> neighboursInQuadrantSE() {
        return Stream.of(
                arguments(NORTH, false),
                arguments(NORTH_EAST, false),
                arguments(EAST, true)
        );
    }
}