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
        final DayOneInput dayOneInput = new DayOneInput();

        assertThat(dayOneInput.groupOneLocations().getFirst()).isEqualTo(77221);
        assertThat(dayOneInput.groupTwoLocations().getFirst()).isEqualTo(93653);
    }

    @Test
    @DisplayName("Should read last location for each group")
    void shouldReadLastLocationForEachGroup() {
        final DayOneInput dayOneInput = new DayOneInput();

        assertThat(dayOneInput.groupOneLocations().getLast()).isEqualTo(19976);
        assertThat(dayOneInput.groupTwoLocations().getLast()).isEqualTo(46609);
    }

    @Test
    @DisplayName("Should read whole input")
    void shouldReadWholeInput() {
        final DayOneInput dayOneInput = new DayOneInput();

        assertThat(dayOneInput.groupOneLocations()).hasSize(1000);
    }
}