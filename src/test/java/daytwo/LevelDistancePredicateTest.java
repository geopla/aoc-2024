package daytwo;

import daytwo.Report.LevelPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class LevelDistancePredicateTest {

    @ParameterizedTest
    @CsvSource({
            "1, 2, false",
            "1, 3, false",
            "1, 4, true"
    })
    @DisplayName("Should verify level pair distance")
    void shouldVerifyLevelPairDistance(int min, int max, boolean matchesDistance) {
        var levelPair = new LevelPair(2, 6);

        var predicate = new LevelDistancePredicate(min, max);

        assertThat(predicate.test(levelPair)).isEqualTo(matchesDistance);
    }
}