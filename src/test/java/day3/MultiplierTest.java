package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MultiplierTest {

    @Test
    @DisplayName("Should multiply at all")
    void shouldMultiply() {
        var multiplier = new Multiplier("2", "4");

        assertThat(multiplier.apply()).isEqualTo(8);
    }
}