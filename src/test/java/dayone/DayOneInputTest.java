package dayone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DayOneInputTest {

    @Test
    @DisplayName("Should have some input")
    void shouldHaveInput() {
        final DayOneInput dayOneInput = new DayOneInput();
        assertThat(dayOneInput.data()).isNotNull();
    }

    @Test
    @DisplayName("Should read first location for each group")
    void shouldReadFirstLocationForEachGroup() {
        final DayOneInput.LocationIdGroups locationIdGroups = new DayOneInput().locationIdGroups();

        assertThat(locationIdGroups.one().getFirst()).isEqualTo(77221);
        assertThat(locationIdGroups.two().getFirst()).isEqualTo(93653);
    }

    @Test
    @DisplayName("Should read last location for each group")
    void shouldReadLastLocationForEachGroup() {
        final DayOneInput.LocationIdGroups locationIdGroups = new DayOneInput().locationIdGroups();

        assertThat(locationIdGroups.one().getLast()).isEqualTo(19976);
        assertThat(locationIdGroups.two().getLast()).isEqualTo(46609);
    }

    @Test
    @DisplayName("Should read whole input")
    void shouldReadWholeInput() {
        final DayOneInput.LocationIdGroups locationIdGroups = new DayOneInput().locationIdGroups();

        assertThat(locationIdGroups.one()).hasSize(1000);
    }
}