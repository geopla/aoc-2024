package dayzero;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SetupCheckTest {

    @Test
    @DisplayName("Should confirm thet basic setup works")
    void shouldConfirmBasicSetup() {
        assertThat(true).isTrue();
    }
}