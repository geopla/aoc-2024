package daytwo;

import daytwo.Report.LevelPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReportTest {

    @Test
    @DisplayName("Should deliver pairs")
    void shouldDeliverPairs() {
        var report = new Report("1 2 3 4 5");
        Stream<LevelPair> levelPairs = report.levelPairs();

        assertThat(levelPairs).containsExactly(
           new LevelPair(1, 2),
           new LevelPair(2, 3),
           new LevelPair(3, 4),
           new LevelPair(4, 5)
        );
    }

    @ParameterizedTest(name = "input: \"{0}\" parsed: {1}")
    @FieldSource("levelsFromStringInput")
    @DisplayName("Should parse levels")
    void shouldParseLevels(String input, List<Integer> levels) {
        var report = new Report(input);

        assertThat(report.levels()).containsAnyElementsOf(levels);
    }

    static List<Arguments> levelsFromStringInput = Arrays.asList(
            arguments("", List.of()),
            arguments("42", List.of(42)),
            arguments("7 12", List.of(7, 12))
    );

    @Test
    @DisplayName("Should throw an exception on bad input")
    void shouldThrowExceptionOnInvalidInput() {
        assertThatRuntimeException().isThrownBy(() ->
                new Report("4 e")
                        .levels()
                        .forEach(level -> { })
        );
    }

    // --- Spliterator stuff

    @Test
    @DisplayName("Should not advance on empty input stream")
    void shouldNotAdvanceOnEmptyInputStream() {
        IntStream levelStream = IntStream.of();
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean advance = spliterator.tryAdvance(n -> { });

        assertThat(advance).isFalse();
    }

    @Test
    @DisplayName("Should not advance on single element input stream")
    void shouldNotAdvanceOnSingleElementInputStream() {
        IntStream levelStream = IntStream.of(42);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean advance = spliterator.tryAdvance(n -> { });

        assertThat(advance).isFalse();
    }

    @Test
    @DisplayName("Should advance once on two element input stream")
    void shouldAdvanceOnceOnTwoElementInputStream() {
        IntStream levelStream = IntStream.of(42, 7);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean firstAdvance = spliterator.tryAdvance(n -> { });
        assertThat(firstAdvance).isTrue();

        boolean secondAdvance = spliterator.tryAdvance(n -> { });
        assertThat(secondAdvance).isFalse();
    }

    @Test
    @DisplayName("Should advance twice on three element input stream")
    void shouldAdvanceTwiceOnThreeElementInputStream() {
        IntStream levelStream = IntStream.of(42, 7, 5);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean firstAdvance = spliterator.tryAdvance(n -> { });
        assertThat(firstAdvance).isTrue();

        boolean secondAdvance = spliterator.tryAdvance(n -> { });
        assertThat(secondAdvance).isTrue();

        boolean thirdAdvance = spliterator.tryAdvance(n -> { });
        assertThat(thirdAdvance).isFalse();
    }
}