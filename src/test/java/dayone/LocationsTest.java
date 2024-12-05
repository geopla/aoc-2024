package dayone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LocationsTest {

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
        final List<Integer> emptyLocations = List.of();

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