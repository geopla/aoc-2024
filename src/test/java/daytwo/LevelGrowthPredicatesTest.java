package daytwo;

import daytwo.LevelGrowthPredicates.GraduallyIncreasing;
import daytwo.Report.LevelPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static daytwo.LevelGrowthPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LevelGrowthPredicatesTest {

    LevelPair increasingLevelPair = new LevelPair(7, 42);
    LevelPair decreasingLevelPair = new LevelPair(451, 3);
    LevelPair flatLevelPair = new LevelPair(6, 6);

    @Test
    @DisplayName("Should handle gradually increasing level pair")
    void shouldHandleGraduallyIncreasingLevelPair() {
        var predicate = new GraduallyIncreasing();

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isTrue(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isFalse(),
                () -> assertThat(predicate.test(flatLevelPair)).isFalse()
        );
    }

    @Test
    @DisplayName("Should handle gradually decreasing level pair")
    void shouldHandleGraduallyDecreasingLevelPair() {
        var predicate = new GraduallyDecreasing();

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isFalse(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isTrue(),
                () -> assertThat(predicate.test(flatLevelPair)).isFalse()
        );
    }

    @Test
    @DisplayName("Should return gradually increasing predicate for increasing levels")
    void shouldReturnGraduallyIncreasingPredicate() {
        Stream<LevelPair> increasingLevels = Stream.of(
                new LevelPair(7, 7),
                new LevelPair(7, 42),
                new LevelPair(42, 451)
        );

        Predicate<LevelPair> predicate = createFrom(increasingLevels);

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isTrue(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isFalse()
        );
    }

    @Test
    @DisplayName("Should return gradually decreasing predicate for decreasing")
    void shouldReturnGraduallyDecreasingPredicate() {
        Stream<LevelPair> increasingLevels = Stream.of(
                new LevelPair(900, 900),
                new LevelPair(900, 77),
                new LevelPair(77, 12)
        );

        Predicate<LevelPair> predicate = createFrom(increasingLevels);

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isFalse(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isTrue()
        );
    }

    @Test
    @DisplayName("Should return always false predicate on empty levels")
    void shouldReturnAlwaysFalsePredicateOnEmptyLevels() {
        Stream<LevelPair> emptyLevels = Stream.of();

        Predicate<LevelPair> predicate = createFrom(emptyLevels);

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isFalse(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isFalse()
        );
    }

    @Test
    @DisplayName("Should return always false predicate on all flat levels")
    void shouldReturnAlwaysFalsePredicateOnFlatLevels() {
        Stream<LevelPair> flatLevels = Stream.of(
          new LevelPair(42, 42),
          new LevelPair(42, 42)
        );

        Predicate<LevelPair> predicate = createFrom(flatLevels);

        assertAll(
                () -> assertThat(predicate.test(increasingLevelPair)).isFalse(),
                () -> assertThat(predicate.test(decreasingLevelPair)).isFalse()
        );
    }
}