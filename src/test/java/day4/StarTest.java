package day4;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static day4.Star.CardinalDirection.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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

    @Test
    @DisplayName("Should find an X-match")
    void shouldFindXMatch() {
        var textBlock = TextBlock.from("""
                M_S_
                _A__
                M_S_
                ____""");

        var star = textBlock.star(1, 1);
        boolean xmatch = star.xmatch("MAS");

        assertThat(xmatch).isTrue();
    }

    @ParameterizedTest
    @MethodSource("verifyDiameterMatch")
    @DisplayName("Should verify diameter match")
    void shouldVerifyDiameterMatch(Star.CardinalDirection tailTextDirection, boolean expectedMatch) {
        var textBlock = TextBlock.from("""
                M_S_
                _A__
                M_S_
                ____""");

        var star = textBlock.star(1, 1);
        boolean diameterMatch = star.diameterMatch("MAS", tailTextDirection);

        assertThat(diameterMatch).isEqualTo(expectedMatch);
    }

    static Stream<Arguments> verifyDiameterMatch() {
        return Stream.of(
                arguments(NORTH_EAST, true),
                arguments(SOUTH_EAST, true),

                arguments(NORTH, false),
                arguments(EAST, false),
                arguments(SOUTH, false),
                arguments(SOUTH_WEST, false),
                arguments(WEST, false),
                arguments(NORTH_WEST, false)
        );
    }

    @Test
    @DisplayName("Should have single center crossing match")
    void shouldHaveSingleCenterCrossingMatch() {
        var textBlock = TextBlock.from("""
                M__
                _A_
                __S""");

        var star = textBlock.star(1, 1);
        boolean diameterMatch = star.diameterMatch("MAS", SOUTH_EAST);

        assertThat(diameterMatch).isTrue();
    }

    @ParameterizedTest
    @MethodSource("provideDiameterStrings")
    @DisplayName("Should provide strings through star center")
    void shouldProvideDiameterString(Star.CardinalDirection tailTextDirection, String expectedDiameterText) {
        var textBlock = TextBlock.from("""
                ABC
                DEF
                GHI""");

        var star = textBlock.star(1, 1);
        var diameter = 3;

        String diameterText = star.diameterText(tailTextDirection, diameter);

        assertThat(diameterText).isEqualTo(expectedDiameterText);
    }

    static Stream<Arguments> provideDiameterStrings() {
        return Stream.of(
                arguments(NORTH, "HEB"),
                arguments(NORTH_EAST, "GEC"),
                arguments(EAST, "DEF"),
                arguments(SOUTH_EAST, "AEI"),
                arguments(SOUTH, "BEH"),
                arguments(SOUTH_WEST, "CEG"),
                arguments(WEST, "FED"),
                arguments(NORTH_WEST, "IEA")
        );
    }

    @Test
    @DisplayName("Should throw exception when text diameter is given as even number")
    void shouldThrowExceptionWhenTextDiameterIsEven() {
        var textBlock = TextBlock.from("""
                ABC
                DEF
                GHI""");

        var star = textBlock.star(1, 1);

        assertThatIllegalArgumentException().isThrownBy(() ->
                        star.diameterText(SOUTH_EAST, 4))
                .withMessage("Sorry mate, diameter needs to be uneven");
    }

    @Test
    @DisplayName("Should provide its rays")
    void shouldProvideItsRays() {
        var star = textBlock.star(2, 1);

        assertThat(star.rays()).containsExactly(
                new Star.Ray(NORTH, "AM"),
                new Star.Ray(NORTH_EAST, "AS"),
                new Star.Ray(EAST, "AMXMSMSA"),
                new Star.Ray(SOUTH_EAST, "ASAMXXAM"),
                new Star.Ray(SOUTH, "AXAAASXMM"),
                new Star.Ray(SOUTH_WEST, "AMM"),
                new Star.Ray(WEST, "ASM"),
                new Star.Ray(NORTH_WEST, "AM")
        );
    }

    @Test
    @DisplayName("Should provide its length limited rays")
    void shouldProvideItsLengthLimitedRays() {
        var star = textBlock.star(2, 1);
        var maxRayLength = 3;

        assertThat(star.rays(maxRayLength)).containsExactly(
                new Star.Ray(NORTH, "AM"),
                new Star.Ray(NORTH_EAST, "AS"),
                new Star.Ray(EAST, "AMX"),
                new Star.Ray(SOUTH_EAST, "ASA"),
                new Star.Ray(SOUTH, "AXA"),
                new Star.Ray(SOUTH_WEST, "AMM"),
                new Star.Ray(WEST, "ASM"),
                new Star.Ray(NORTH_WEST, "AM")
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
    @MethodSource("limitedRayInGivenDirection")
    @DisplayName("Should have ray limited by length in given direction")
    void shouldHaveLimitedRayInGivenDirection(Star.CardinalDirection cardinalDirection, String expectedRayValue) {
        var star = textBlock.star(2, 1);
        var maxRayLength = 3;
        Star.Ray ray = star.ray(cardinalDirection, maxRayLength);

        assertThat(ray.value()).isEqualTo(expectedRayValue);
    }

    static Stream<Arguments> limitedRayInGivenDirection() {
        return Stream.of(
                arguments(NORTH, "AM"),
                arguments(NORTH_EAST, "AS"),
                arguments(EAST, "AMX"),
                arguments(SOUTH_EAST, "ASA"),
                arguments(SOUTH, "AXA"),
                arguments(SOUTH_WEST, "AMM"),
                arguments(WEST, "ASM"),
                arguments(NORTH_WEST, "AM")
        );
    }

    @ParameterizedTest
    @MethodSource("haveOnlyRaysWithAtLeastTwoElements")
    @DisplayName("Should have only rays with at least two elements")
    void shouldHaveOnlyRaysWithAtLeastTwoElements(char starCenterValue, Set<String> expectedRays) {
        var textBlock = TextBlock.from("""
                ABC
                DEF
                GHI""");

        Star starToEvaluate = textBlock.stars()
                .filter(star -> star.centerValue() == starCenterValue)
                .findFirst()
                .get();

        assertThat(starToEvaluate.rays().map(Star.Ray::value)).containsExactlyInAnyOrderElementsOf(expectedRays);
    }

    static Stream<Arguments> haveOnlyRaysWithAtLeastTwoElements() {
        return Stream.of(
                arguments('A', Set.of("ABC", "AEI", "ADG")),
                arguments('B', Set.of("BC", "BF", "BEH", "BD", "BA")),
                arguments('C', Set.of("CFI", "CEG", "CBA")),
                arguments('D', Set.of("DA", "DB", "DEF", "DH", "DG")),
                arguments('E', Set.of("EB", "EC", "EF", "EI", "EH", "EG", "ED", "EA")),
                arguments('F', Set.of("FC", "FI", "FH", "FED", "FB")),
                arguments('G', Set.of("GDA", "GEC", "GHI")),
                arguments('H', Set.of("HEB", "HF", "HI", "HG", "HD"))
        );
    }

    @ParameterizedTest
    @MethodSource("haveOnlyLengthLimitedRaysWithAtLeastTwoElements")
    @DisplayName("Should have only length limited rays with at least two elements")
    void shouldHaveOnlyLengthLimitedRaysWithAtLeastTwoElements(char starCenterValue, Set<String> expectedRays) {
        var textBlock = TextBlock.from("""
                ABC
                DEF
                GHI""");

        Star starToEvaluate = textBlock.stars()
                .filter(star -> star.centerValue() == starCenterValue)
                .findFirst()
                .get();

        var lengthLimit = 2;
        assertThat(starToEvaluate.rays(lengthLimit).map(Star.Ray::value)).containsExactlyInAnyOrderElementsOf(expectedRays);
    }

    static Stream<Arguments> haveOnlyLengthLimitedRaysWithAtLeastTwoElements() {
        return Stream.of(
                arguments('A', Set.of("AB", "AE", "AD")),
                arguments('B', Set.of("BC", "BF", "BE", "BD", "BA")),
                arguments('C', Set.of("CF", "CE", "CB")),
                arguments('D', Set.of("DA", "DB", "DE", "DH", "DG")),
                arguments('E', Set.of("EB", "EC", "EF", "EI", "EH", "EG", "ED", "EA")),
                arguments('F', Set.of("FC", "FI", "FH", "FE", "FB")),
                arguments('G', Set.of("GD", "GE", "GH")),
                arguments('H', Set.of("HE", "HF", "HI", "HG", "HD"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1", "0", "-1"
    })
    @DisplayName("Should reject request for rays limited to less than two elements")
    void shouldRejectRequestForRaysLimitedToLessThanTwoElements(int maxRayLength) {
        assertThatIllegalArgumentException().isThrownBy(() ->
                        textBlock.star().rays(maxRayLength))
                .withMessage("a ray can't be limited to less than two elements");
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