package day4;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.NamedExecutable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static day4.Star.CardinalDirection.EAST;
import static day4.Star.CardinalDirection.NORTH;
import static day4.Star.CardinalDirection.NORTH_EAST;
import static day4.Star.CardinalDirection.NORTH_WEST;
import static day4.Star.CardinalDirection.SOUTH;
import static day4.Star.CardinalDirection.SOUTH_EAST;
import static day4.Star.CardinalDirection.SOUTH_WEST;
import static day4.Star.CardinalDirection.WEST;
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
    @MethodSource("rayInGivenDirection")
    @DisplayName("Should have ray in given direction")
    void shouldHaveRayInGivenDirection(Star.CardinalDirection cardinalDirection, String expectedRayValue) {
        var star = textBlock.star(2, 1);

        Star.Ray ray = star.ray(cardinalDirection);

        assertThat(ray.value()).isEqualTo(expectedRayValue);
    }

    static Stream<Arguments> rayInGivenDirection() {
        return Stream.of(
                arguments(NORTH, "AM"),
                arguments(NORTH_EAST, "AS"),
                arguments(EAST, "AMXMSMSA"),
                arguments(SOUTH_EAST, "ASAMXXAM"),
                arguments(SOUTH, "AXAAASXMM"),
                arguments(SOUTH_WEST, "AMM"),
                arguments(WEST, "ASM"),
                arguments(NORTH_WEST, "AM")
        );
    }

    @ParameterizedTest
    @MethodSource("provideItsNeighbours")
    @DisplayName("Should provide its neighbours")
    void shouldProvideItsNeighbours(Star.CardinalDirection cardinalDirection, char expectedStar) {
        var star = TextBlock.from("""
                ABCDE
                FGHIJ
                KLMNO""").star(1, 1);

        Star neighbour = star.neighbourTo(cardinalDirection);

        assertThat(neighbour.centerValue()).isEqualTo(expectedStar);
    }

    static Stream<Arguments> provideItsNeighbours() {
        return Stream.of(
                arguments(NORTH, 'B'),
                arguments(NORTH_EAST, 'C'),
                arguments(EAST, 'H'),
                arguments(SOUTH_EAST, 'M'),
                arguments(SOUTH, 'L'),
                arguments(SOUTH_WEST, 'K'),
                arguments(WEST, 'F'),
                arguments(NORTH_WEST, 'A')
        );
    }

    @ParameterizedTest
    @MethodSource("topLeftNeighbours")
    @DisplayName("Should verify neighbours of top left star")
    void shouldVerifyNeighboursOfTopLeft(
            Star.CardinalDirection cardinalDirection,
            boolean expectedNeighbour) {

        var star = textBlock.star();

        assertThat(star.hasNeighbourTo(cardinalDirection)).isEqualTo(expectedNeighbour);
    }

    static Stream<Arguments> topLeftNeighbours() {
        return Stream.of(
                arguments(NORTH, false),
                arguments(NORTH_EAST, false),
                arguments(EAST, true),
                arguments(SOUTH_EAST, true),
                arguments(SOUTH, true),
                arguments(SOUTH_WEST, false),
                arguments(WEST, false),
                arguments(NORTH_WEST, false)
        );
    }

    @ParameterizedTest
    @MethodSource("topRightNeighbours")
    @DisplayName("Should verify neighbours of top right star")
    void shouldVerifyNeighboursOfTopRight(
            Star.CardinalDirection cardinalDirection,
            boolean expectedNeighbour) {

        var star = textBlock.star(9, 0);

        assertThat(star.hasNeighbourTo(cardinalDirection)).isEqualTo(expectedNeighbour);
    }

    static Stream<Arguments> topRightNeighbours() {
        return Stream.of(
                arguments(NORTH, false),
                arguments(NORTH_EAST, false),
                arguments(EAST, false),
                arguments(SOUTH_EAST, false),
                arguments(SOUTH, true),
                arguments(SOUTH_WEST, true),
                arguments(WEST, true),
                arguments(NORTH_WEST, false)
        );
    }

    @ParameterizedTest
    @MethodSource("bottomRightNeighbours")
    @DisplayName("Should verify neighbours of top right star")
    void shouldVerifyNeighboursOfBottomRight(
            Star.CardinalDirection cardinalDirection,
            boolean expectedNeighbour) {

        var star = textBlock.star(9, 9);

        assertThat(star.hasNeighbourTo(cardinalDirection)).isEqualTo(expectedNeighbour);
    }

    static Stream<Arguments> bottomRightNeighbours() {
        return Stream.of(
                arguments(NORTH, true),
                arguments(NORTH_EAST, false),
                arguments(EAST, false),
                arguments(SOUTH_EAST, false),
                arguments(SOUTH, false),
                arguments(SOUTH_WEST, false),
                arguments(WEST, true),
                arguments(NORTH_WEST, true)
        );
    }

    @ParameterizedTest
    @MethodSource("bottomLeftNeighbours")
    @DisplayName("Should verify neighbours of top right star")
    void shouldVerifyNeighboursOfBottomLeft(
            Star.CardinalDirection cardinalDirection,
            boolean expectedNeighbour) {

        var star = textBlock.star(0, 9);

        assertThat(star.hasNeighbourTo(cardinalDirection)).isEqualTo(expectedNeighbour);
    }

    static Stream<Arguments> bottomLeftNeighbours() {
        return Stream.of(
                arguments(NORTH, true),
                arguments(NORTH_EAST, true),
                arguments(EAST, true),
                arguments(SOUTH_EAST, false),
                arguments(SOUTH, false),
                arguments(SOUTH_WEST, false),
                arguments(WEST, false),
                arguments(NORTH_WEST, false)
        );
    }

    @ParameterizedTest
    @EnumSource
    @DisplayName("Should verify neighbours of inner star")
    void shouldVerifyNeighboursOfInnerStar(Star.CardinalDirection cardinalDirection) {
        var star = textBlock.star(1, 1);

        assertThat(star.hasNeighbourTo(cardinalDirection)).isTrue();
    }
}