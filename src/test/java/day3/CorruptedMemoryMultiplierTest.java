package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static day3.CorruptedMemoryMultiplier.multipliersFrom;
import static day3.CorruptedMemoryMultiplier.multipliersSumFrom;
import static org.assertj.core.api.Assertions.assertThat;

class CorruptedMemoryMultiplierTest {


    @Test
    @DisplayName("shouldAddMultipliersFromChallenge")
    void shouldAddMultipliersFromChallenge() {
        var multipliersSum = multipliersSumFrom("../day-3-input.txt");

        assertThat(multipliersSum).isEqualTo(23552108);
    }

    @Test
    @DisplayName("shouldAddMultipliersFromSampleFile")
    void shouldAddMultipliersFromSampleFile() {
        var multipliersSum = multipliersSumFrom("../day3-input-sample.txt");

        assertThat(multipliersSum).isEqualTo(161);
    }

    @Test
    @DisplayName("Should add valid multipliers from corrupted memory example")
    void shouldAddValidMultipliersFromCorruptedMemory() {
        final Stream<Character> corruptedMemory =
                from("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))");

        var multipliersSum = multipliersSumFrom(corruptedMemory);

        assertThat(multipliersSum).isEqualTo(161);
    }

    @Test
    @DisplayName("Should read valid multipliers from corrupted memory example")
    void shouldReadValidMultipliersFromCorruptedMemoryExample() {
        final Stream<Character> corruptedMemory =
                from("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))");

        Stream<Multiplier> multipliers = multipliersFrom(corruptedMemory);

        assertThat(multipliers).containsExactly(
                new Multiplier("2", "4"),
                new Multiplier("5", "5"),
                new Multiplier("11", "8"),
                new Multiplier("8", "5")
        );
    }

    static Stream<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c);
    }
}