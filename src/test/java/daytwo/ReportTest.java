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

import static daytwo.Report.GrowOrShrinkPredicate.GrowOrShrink.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReportTest {


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

    // -- will become obsolete after redesign
    //
    @Test
    @DisplayName("Should determine increasing definition on very first level pair only")
    void shouldSetDefinitionOnTestingFirstIncreasingLevelPair() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(UNDECIDED_YET);

        var increasingLevelPair = new LevelPair(1, 42);
        growOrShrinkPredicate.test(increasingLevelPair);
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(INCREASING);

        var decreasingLevelPair = new LevelPair(42, 3);
        growOrShrinkPredicate.test(decreasingLevelPair);
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(INCREASING);
    }

    @Test
    @DisplayName("Should determine decreasing definition on very first level pair only")
    void shouldSetDefinitionOnTestingFirstDecreasingLevelPair() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(UNDECIDED_YET);

        var decreasingLevelPair = new LevelPair(7, 3);
        growOrShrinkPredicate.test(decreasingLevelPair);
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(DECREASING);

        var increasingLevelPair = new LevelPair(3, 42);
        growOrShrinkPredicate.test(increasingLevelPair);
        assertThat(growOrShrinkPredicate.definition()).isEqualTo(DECREASING);
    }

    @Test
    @DisplayName("Should accept another increasing level pair")
    void shouldAcceptAnotherIncreasingLevelPair() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();

        var increasingLevelPair = new LevelPair(7, 42);
        var increasingLevelPairTest = growOrShrinkPredicate.test(increasingLevelPair);
        assertThat(increasingLevelPairTest).isTrue();  // first one is always true

        var anotherIncreasingLevelPair = new LevelPair(42, 100);
        var anotherIncreasingLevelPairTest = growOrShrinkPredicate.test(anotherIncreasingLevelPair);
        assertThat(anotherIncreasingLevelPairTest).isTrue();
    }

    @Test
    @DisplayName("Should accept another decreasing level pair")
    void shouldAcceptAnotherDecreasingLevelPair() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();

        var decreasingLevelPair = new LevelPair(42, 7);
        var decreasingLevelPairTest = growOrShrinkPredicate.test(decreasingLevelPair);
        assertThat(decreasingLevelPairTest).isTrue(); // first one is always true

        var anotherDecreasingLevelPair = new LevelPair(7, 3);
        var anotherDecreasingLevelPairTest = growOrShrinkPredicate.test(anotherDecreasingLevelPair);
        assertThat(anotherDecreasingLevelPairTest).isTrue();
    }

    @Test
    @DisplayName("Should fail on growth violation")
    void shouldFailOnGrowthViolation() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();

        var increasingLevelPair = new LevelPair(7, 42);
        growOrShrinkPredicate.test(increasingLevelPair);

        var decreasingLevelPair = new LevelPair(42, 9);
        var decreasingLevelPairTest = growOrShrinkPredicate.test(decreasingLevelPair);
        assertThat(decreasingLevelPairTest).isFalse();
    }

    @Test
    @DisplayName("Should fail on shrink violation")
    void shouldFailOnShrinkViolation() {
        var growOrShrinkPredicate = new Report.GrowOrShrinkPredicate();

        var decreasingLevelPair = new LevelPair(42, 7);
        growOrShrinkPredicate.test(decreasingLevelPair);

        var increasingLevelPair = new LevelPair(7, 9);
        var increasingLevelPairTest = growOrShrinkPredicate.test(increasingLevelPair);
        assertThat(increasingLevelPairTest).isFalse();
    }
    //
    // -- obsolete after redesign

    // TODO gap gate


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
}