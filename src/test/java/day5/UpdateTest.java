package day5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class UpdateTest {


    @ParameterizedTest
    @MethodSource("createUpdateWithMovedPage")
    @DisplayName("Should create new update with moved element")
    void shouldCreateUpdateWithMovedElement(int page, int index, Update expectedUpdate) {
        var update = new Update(97, 13, 75, 29, 47);

        var actualUpdate = update.createUpdateWithMovedPage(page, index);

        assertThat(actualUpdate).isEqualTo(expectedUpdate);
    }

    static Stream<Arguments> createUpdateWithMovedPage() {
        return Stream.of(
                arguments(75, 1, new Update(97, 75, 13, 29, 47)),
                arguments(97, 2, new Update(13, 75, 97, 29, 47)),
                arguments(29, 0, new Update(29, 97, 13, 75, 47)),
                arguments(97, 0, new Update(97, 13, 75, 29, 47)),
                arguments(47, 4, new Update(97, 13, 75, 29, 47)),
                // Element not present should not cause any havoc
                arguments(2007, 1, new Update(97, 13, 75, 29, 47))
        );
    }

    @ParameterizedTest
    @MethodSource("findIndexOfFirstOccurrenceOutOfPageNumbers")
    @DisplayName("Should find index of first occurrence out of given page numbers")
    void shouldFindIndexOfFirstOccurrenceOutOfPageNumbers(Set<Integer> pageNumberSubset, int expectedIndex) {
        var update = new Update(97, 13, 75, 29, 47);

        int firstOccurrence = update.indexOfFirstOccurrenceOutOf(pageNumberSubset);

        assertThat(firstOccurrence).isEqualTo(expectedIndex);
    }

    static Stream<Arguments> findIndexOfFirstOccurrenceOutOfPageNumbers() {
        return Stream.of(
                // TODO add some more cases
                arguments(Set.of(75, 47), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("providePageNumbersBeforePage")
    @DisplayName("Should provide page numbers before actual page")
    void shouldProvidePageNumbersBeforePage(int actualPage, List<Integer> expectedPageNumbersBefore) {
        var update = new Update(75, 47, 61, 53, 29);

        var pageNumbersBefore = update.pageNumbersBefore(actualPage);

        assertThat(pageNumbersBefore).containsAnyElementsOf(expectedPageNumbersBefore);
    }

    @Test
    @DisplayName("Should provide empty stream when actual page is not in page numbers")
    void shouldProvideEmptyStreamWhenActualPageIsNotInPageNumbers() {
        var update = new Update(75, 47, 61, 53, 29);

        var pageNumbersBefore = update.pageNumbersBefore(300);

        assertThat(pageNumbersBefore).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideMiddlePageNumber")
    @DisplayName("Should provide middle page number")
    void shouldProvideMiddlePageNumber(List<Integer> pageNumbers, int expectedMiddlePageNumber) {
        int middlePageNumber = new Update(pageNumbers).middlePageNumber();

        assertThat(middlePageNumber).isEqualTo(expectedMiddlePageNumber);
    }

    static Stream<Arguments> provideMiddlePageNumber() {
        return Stream.of(
                arguments(List.of(75, 47, 61, 53, 29), 61),
                arguments(List.of(75, 29, 13), 29),
                arguments(List.of(75), 75)
        );
    }

    @Test
    @DisplayName("Should throw exception when there is no middle page number due to even number of pages")
    void shouldThrowExceptionWhenThereIsNoMiddlePageNumber() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                        new Update(75, 47).middlePageNumber())
                .withMessage("can't compute middle page number of even page numbers");
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