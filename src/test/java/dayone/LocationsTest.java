package dayone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class LocationsTest {

    @Test
    @DisplayName("Should calculate similarity for cardinality greater than onn in right list")
    void shouldCalculateSingleSimilarityScoreForCardinalityGreaterThanOneInRightList() {
        final List<Integer> leftLocations = List.of(3, 42);
        final List<Integer> rightLocations = List.of(3, 3);

        int similarityScore = Locations.similarityScore(leftLocations, rightLocations);

        assertThat(similarityScore).isEqualTo(6);
    }

    @Test
    @DisplayName("Should calculate similarity score for cardinality one in right list")
    void shouldCalculateSingleSimilarityScoreForCardinalityOneInRightList() {
        final List<Integer> leftLocations = List.of(3);
        final List<Integer> rightLocations = List.of(3);

        int similarityScore = Locations.similarityScore(leftLocations, rightLocations);

        assertThat(similarityScore).isEqualTo(3);
    }

    @Test
    @DisplayName("Should calculate similarity score for cardinality zero in right list")
    void shouldCalculateSingleSimilarityScoreForCardinalityZeroInRightList() {
        final List<Integer> leftLocations = List.of(3);
        final List<Integer> rightLocations = List.of(7);

        int similarityScore = Locations.similarityScore(leftLocations, rightLocations);

        assertThat(similarityScore).isEqualTo(0);
    }


    @Test
    @DisplayName("Should create a cardinality multiplier map")
    void shouldCreateMapForCardinalityZero() {
        final List<Integer> cardinalityMultiplierList = List.of(4, 3, 5, 3, 9, 3);

        Map<Integer, Integer> cardinalMultiplierMap = Locations.cardinalityMultiplierMap(cardinalityMultiplierList);

        assertThat(cardinalMultiplierMap).isNotNull();
    }

    @Test
    @DisplayName("Should create empty cardinal multiplier map on empty input")
    void shouldCreateEmptyMapForEmptyLocationsLists() {
        Map<Integer, Integer> cardinalMultiplierMap = Locations.cardinalityMultiplierMap(emptyList());

        assertThat(cardinalMultiplierMap).isEmpty();
    }

    @Test
    @DisplayName("Should calculate the similarity score of an empty list")
    void shouldCalculateSimilarityScoreOfEmptyLocationLists() {
        final List<Integer> emptyLocations = emptyList();

        int similarityScore = Locations.similarityScore(emptyLocations, emptyLocations);

        assertThat(similarityScore).isEqualTo(0);
    }

    @Test
    @DisplayName("Should calculate the puzzle input")
    void shouldCalculateThePuzzleInput() {
        final DayOneInput dayOneInput = new DayOneInput();
        final List<Integer> groupOneLocations = dayOneInput.groupOneLocations();
        final List<Integer> groupTwoLocations = dayOneInput.groupTwoLocations();

        int totalDistance = Locations.totalDistance(groupOneLocations, groupTwoLocations);

        assertThat(totalDistance).isEqualTo(1580061);
    }

    @Test
    @DisplayName("Should calculate the total distance of example from puzzle description")
    void shouldCalculateTotalDistanceFromExample() {
        final List<Integer> groupOneLocations = List.of(3, 4, 2, 1, 3, 3);
        final List<Integer> groupTwoLocations = List.of(4, 3, 5, 3, 9, 3);

        int totalDistance = Locations.totalDistance(groupOneLocations, groupTwoLocations);

        assertThat(totalDistance).isEqualTo(11);
    }

    @ParameterizedTest(name = "left: {0}, right: {1} distance: {2}")
    @CsvSource({
            "3, 7, 4",
            "9, 3, 6",
            "3, 3, 0"
    })
    @DisplayName("Should calculate the total distance of single elements lists of locations")
    void shouldCalculateTotalDistanceForSingleElementLists(int left, int right, int expectedDistance) {
        final List<Integer> groupOneList = List.of(left);
        final List<Integer> groupTwoList = List.of(right);

        int totalDistance = Locations.totalDistance(groupOneList, groupTwoList);

        assertThat(totalDistance).isEqualTo(expectedDistance);
    }

    @Test
    @DisplayName("Should calculate the total distance of empty lists of locations")
    void shouldCalculateTotalDistanceOfEmptyListsIfLocations() {
        final List<Integer> emptyLocations = emptyList();

        int totalDistance = Locations.totalDistance(emptyLocations, emptyLocations);

        assertThat(totalDistance).isEqualTo(0);
    }

    @Test
    @DisplayName("Should zip single element lists of same size")
    void shouldZipSingleElementLists() {
        final Locations.Zipper zipper = new Locations.Zipper(List.of(7), List.of(42));

        Stream<Locations.Pair> locationTuples = Stream.generate(zipper);

        assertThat(locationTuples.limit(1)).containsExactly(new Locations.Pair(7, 42));
    }

    @Test
    @DisplayName("Should zip multiple element lists of same size")
    void shouldZipMultipleElementLists() {
        final Locations.Zipper zipper = new Locations.Zipper(List.of(0, 2, 4), List.of(1, 3, 5));

        Stream<Locations.Pair> locationTuples = Stream.generate(zipper);

        assertThat(locationTuples.limit(3)).containsExactly(
                new Locations.Pair(0, 1),
                new Locations.Pair(2, 3),
                new Locations.Pair(4, 5)
        );
    }
}