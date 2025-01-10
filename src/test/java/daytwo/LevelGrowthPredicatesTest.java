package daytwo;

import daytwo.LevelGrowthPredicates.GraduallyIncreasing;
import daytwo.Report.LevelPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static daytwo.LevelGrowthPredicates.GraduallyDecreasing;
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
}