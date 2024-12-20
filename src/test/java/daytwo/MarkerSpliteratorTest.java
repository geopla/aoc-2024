package daytwo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MarkerSpliteratorTest {

    @Test
    @DisplayName("Should return marker when is present in source collection")
    void shouldReturnMarker() {
        var thingies = List.of(7, 42, 2001);
        var markerSpliterator = new MarkerSpliterator<>(thingies.spliterator(), n -> n == 42);

        Optional<Integer> marker = markerSpliterator.marker();

        assertThat(marker).as("marker element").contains(42);
    }

    @Test
    @DisplayName("Should return empty marker optional when marker is NOT present in source collection")
    void shouldReturnEmptyOptionalWhenMarkerIsNotPresentInSourceCollection() {
        var thingies = List.of(7, 42, 2001);
        var markerSpliterator = new MarkerSpliterator<>(thingies.spliterator(), n -> n == 90562);

        Optional<Integer> marker = markerSpliterator.marker();

        assertThat(marker).as("marker element").isEmpty();
    }

    @Test
    @DisplayName("Should return empty marker optional when source collection is empty")
    void shouldReturnEmptyOptionalWhenSourceCollectionIsEmpty() {
        var thingies = List.<Integer>of();
        var markerSpliterator = new MarkerSpliterator<>(thingies.spliterator(), n -> n == 90562);

        Optional<Integer> marker = markerSpliterator.marker();

        assertThat(marker).as("marker element").isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "7",
            "42",
            "2001",
            "90562"
    })
    @DisplayName("Should always provide the whole source collection")
    void shouldProvideSourceCollection(int markerValue) {
        var thingies = List.of(7, 42, 2001);
        var markerSpliterator = new MarkerSpliterator<>(thingies.spliterator(), n -> n == markerValue);
        var processedThingies = new LinkedList<Integer>();

        markerSpliterator.forEachRemaining(processedThingies::add);

        assertThat(processedThingies).containsExactly(7, 42, 2001);
    }
}