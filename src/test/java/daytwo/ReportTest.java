package daytwo;

import daytwo.Report.LevelPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReportTest {

    @ParameterizedTest(name = "levels: {0} safe: {1}")
    @CsvSource({
            "'7 6 4 2 1', true",
            "'1 2 7 8 9', false",
            "'9 7 6 2 1', false",
            "'1 3 2 4 5', true",
            "'8 6 4 4 1', true",
            "'1 3 6 7 9', true"
    })
    @DisplayName("Should verify examples from puzzle with problem dampener")
    void shouldVerifyExamplesFromPuzzleWithProblemDampener(String levels, boolean safe) {
        var report = new Report(levels);

        assertThat(report.isSafeWithProblemDampener()).isEqualTo(safe);
    }

    @ParameterizedTest(name = "levels: {0} safe: {1}")
    @CsvSource({
            "'7 6 4 2 1', true",
            "'1 2 7 8 9', false",
            "'9 7 6 2 1', false",
            "'1 3 2 4 5', false",
            "'8 6 4 4 1', false",
            "'1 3 6 7 9', true"
    })
    @DisplayName("Should verify examples from puzzle")
    void shouldVerifyExamplesFromPuzzle(String levels, boolean safe) {
        var report = new Report(levels);

        assertThat(report.isSafe()).isEqualTo(safe);
    }

    @Test
    @DisplayName("Should be safe on all increasing")
    void shouldBeSafeOnAllIncreasing() {
        var report = new Report("1 2 3 4 5");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isTrue();
    }

    @Test
    @DisplayName("Should be safe on all decreasing")
    void shouldBeSafeOnAllDecreasing() {
        var report = new Report("5 4 3 2 1");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isTrue();
    }

    @Test
    @DisplayName("Should be unsafe on increasing violation")
    void shouldBeUnsafeOnIncreasingViolation() {
        var report = new Report("2 3 4 5 1");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isFalse();
    }

    @Test
    @DisplayName("Should be unsafe on decreasing violation")
    void shouldBeUnsafeOnDecreasingViolation() {
        var report = new Report("4 3 2 1 5");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isFalse();
    }

    @Test
    @DisplayName("Should be safe when keeping level distance min 1, max 3")
    void shouldBeSafeWhenKeepingLevelDistance() {
        var report = new Report("1 2 4 7 8");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isTrue();
    }

    @Test
    @DisplayName("Should be unsafe when violating level distance min 1, max 3")
    void shouldBeSafeWhenViolatingLevelDistance() {
        var report = new Report("1 2 4 8 9");

        boolean isSafe = report.isSafe();

        assertThat(isSafe).isFalse();
    }

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
                        .forEach(level -> {
                        })
        );
    }

    // --- Spliterator stuff

    @Test
    @DisplayName("Should not advance on empty input stream")
    void shouldNotAdvanceOnEmptyInputStream() {
        IntStream levelStream = IntStream.of();
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean advance = spliterator.tryAdvance(n -> {
        });

        assertThat(advance).isFalse();
    }

    @Test
    @DisplayName("Should not advance on single element input stream")
    void shouldNotAdvanceOnSingleElementInputStream() {
        IntStream levelStream = IntStream.of(42);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean advance = spliterator.tryAdvance(n -> {
        });

        assertThat(advance).isFalse();
    }

    @Test
    @DisplayName("Should advance once on two element input stream")
    void shouldAdvanceOnceOnTwoElementInputStream() {
        IntStream levelStream = IntStream.of(42, 7);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean firstAdvance = spliterator.tryAdvance(n -> {
        });
        assertThat(firstAdvance).isTrue();

        boolean secondAdvance = spliterator.tryAdvance(n -> {
        });
        assertThat(secondAdvance).isFalse();
    }

    @Test
    @DisplayName("Should advance twice on three element input stream")
    void shouldAdvanceTwiceOnThreeElementInputStream() {
        IntStream levelStream = IntStream.of(42, 7, 5);
        Spliterator<LevelPair> spliterator = new Report.LevelPairSpliterator(levelStream.spliterator());

        boolean firstAdvance = spliterator.tryAdvance(n -> {
        });
        assertThat(firstAdvance).isTrue();

        boolean secondAdvance = spliterator.tryAdvance(n -> {
        });
        assertThat(secondAdvance).isTrue();

        boolean thirdAdvance = spliterator.tryAdvance(n -> {
        });
        assertThat(thirdAdvance).isFalse();
    }

    // --- skipping n-th element stuff

    @Test
    @DisplayName("Should return stream of levels with skipped n-th element")
    void shouldReturnStreamOfLevelsWhithSkippedNthElement() {
        Stream<IntStream> dampenerLevels = new Report("1 2 3").dampenerLevels();

        var actualSkippedNthLevels = dampenerLevels
                .map(intStream -> intStream.boxed().toList())
                .collect(Collectors.toSet());

        var expectedSkippedNthLevels = Set.of(
                List.of(2, 3),
                List.of(1, 3),
                List.of(1, 2)
        );
        assertThat(actualSkippedNthLevels).isEqualTo(expectedSkippedNthLevels);
    }

    // --- bug stuff

    @Test
    @DisplayName("Should handle dampener case where first pair indicates decrease but is safe in growing")
    void shouldHandleDampenerCaseWhenFirstPairIndicatesDecrease() {
        var allLevels = "57 56 57 59 60 63 64 65";
        boolean allLevelsSafe = new Report(allLevels).isSafe();
        boolean allLevelsSafeWithProblemDampener = new Report(allLevels).isSafeWithProblemDampener();

        var dampenedLevels = "56 57 59 60 63 64 65";
        boolean dampenedLevelsSafe = new Report(dampenedLevels).isSafe();

        assertThat(allLevelsSafe).isFalse();
        assertThat(allLevelsSafeWithProblemDampener).isTrue();

        assertThat(dampenedLevelsSafe).isTrue();
    }
}