package day5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UpdateTest {


    @ParameterizedTest
    @MethodSource("providePageNumbersBeforePage")
    @DisplayName("Should provide page numbers before actual page")
    void shouldProvidePageNumbersBeforePage(int actualPage, List<Integer> expectedPageNumbersBefore) {
        var update = new Update(75, 47, 61, 53, 29);

        var pageNumbersBefore = update.pageNumbersBefore(actualPage);

        assertThat(pageNumbersBefore).containsAnyElementsOf(expectedPageNumbersBefore);
    }

    static Stream<Arguments> providePageNumbersBeforePage() {
        return Stream.of(
                arguments(75, List.of()),
                arguments(47, List.of(75)),
                arguments(61, List.of(75, 47)),
                arguments(53, List.of(75, 47, 61)),
                arguments(29, List.of(75, 47, 61, 53))
        );
    }
}