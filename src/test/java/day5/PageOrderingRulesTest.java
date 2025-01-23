package day5;

import day5.input.PrintJobData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PageOrderingRulesTest {

    @Test
    @DisplayName("Should provide mandatory successors of page numer")
    void shouldProvideSuccessorsOfPage() {
        var pageOrderingRules = new PageOrderingRules(Stream.of(
                new PrintJobData.PageOrderRule(35, 97),
                new PrintJobData.PageOrderRule(35, 72),
                new PrintJobData.PageOrderRule(96, 93)
        ));

        assertThat(pageOrderingRules.successorsOf(35)).containsExactlyInAnyOrder(97, 72);
        assertThat(pageOrderingRules.successorsOf(96)).containsExactly(93);
    }

    @ParameterizedTest
    @CsvSource({
            "35, true",
            "96, true",
            "24, false"
    })
    @DisplayName("Should verify existence of mandatory page number successors")
    void shouldVerifyExistenceOfPageNumberSuccessors(int pageNumber, boolean expectedPageNumberSuccessors) {
        var pageOrderingRules = new PageOrderingRules(Stream.of(
                new PrintJobData.PageOrderRule(35, 97),
                new PrintJobData.PageOrderRule(35, 72),
                new PrintJobData.PageOrderRule(96, 93)
        ));

        assertThat(pageOrderingRules.hasOrderingRule(pageNumber)).isEqualTo(expectedPageNumberSuccessors);
    }
}