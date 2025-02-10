package sidetrack.decisions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherMakerTest {

    @ParameterizedTest
    @MethodSource("applyDecisionMaking")
    @DisplayName("Should apply decision making")
    void shouldApplyDecisionMaking(Decision decision, String expectedMood) {
        String mood = WeatherMaker.execute(decision);

        assertThat(mood).isEqualTo(expectedMood);
    }

    static Stream<Arguments> applyDecisionMaking() {
        return Stream.of(
                Arguments.arguments(new Decision.LetTheSunShine("be happy"), "be happy"),
                Arguments.arguments(new Decision.MakeItRain("ugly weather"), "ugly weather"),
                Arguments.arguments(new Decision.BeingJustMurky("holy shite"), "holy shite")
        );
    }
}