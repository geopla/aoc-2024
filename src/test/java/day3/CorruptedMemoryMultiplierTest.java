package day3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static day3.CorruptedMemoryMultiplier.*;
import static org.assertj.core.api.Assertions.assertThat;

class CorruptedMemoryMultiplierTest {

    @Test
    @DisplayName("shouldAddMultipliersFromChallenge")
    void shouldAddMultipliersFromChallenge() {
        var multipliersSum = multipliersSumFrom("../day-3-input.txt");

        assertThat(multipliersSum).isEqualTo(169021493);
    }

    @Test
    @DisplayName("shouldAddMultipliersWithConditionalsFromChallenge")
    void shouldAddMultipliersWithConditionalsFromChallenge() {
        var multipliersSum = multipliersWithConditionalsSumFrom("../day-3-input.txt");

        assertThat(multipliersSum).isEqualTo(111762583);
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

        Stream<Operation> multipliers = multipliersFrom(corruptedMemory);

        assertThat(multipliers).containsExactly(
                new Operation.Multiplier(List.of("2", "4")),
                new Operation.Multiplier(List.of("5", "5")),
                new Operation.Multiplier(List.of("11", "8")),
                new Operation.Multiplier(List.of("8", "5"))
        );
    }

    @Test
    @DisplayName("Should read valid multipliers with conditionals from corrupted memory example")
    void shouldReadValidMultipliersWithConditionalsFromCorruptedMemoryExample() {
        final Stream<Character> corruptedMemory =
                from("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))");

        Stream<Operation> multipliers = multipliersWithConditionalsFrom(corruptedMemory);

        assertThat(multipliers).containsExactly(
                new Operation.Multiplier(List.of("2", "4")),
                new Operation.Multiplier(List.of("8", "5"))
        );
    }

    static Stream<Character> from(String input) {
        return input.codePoints()
                .mapToObj(c -> (char) c);
    }
}