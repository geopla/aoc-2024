package day5;

import day5.input.PrintJobData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PageOrderingRulesTest {

    static PageOrderingRules examplePageOrderingRules;

    @BeforeAll
    static void beforeAll() {
        examplePageOrderingRules = new PageOrderingRules(Stream.of(
                new PrintJobData.PageOrderRule(47, 53),
                new PrintJobData.PageOrderRule(97, 13),
                new PrintJobData.PageOrderRule(97, 61),
                new PrintJobData.PageOrderRule(97, 47),
                new PrintJobData.PageOrderRule(75, 29),
                new PrintJobData.PageOrderRule(61, 13),
                new PrintJobData.PageOrderRule(75, 53),
                new PrintJobData.PageOrderRule(29, 13),
                new PrintJobData.PageOrderRule(97, 29),
                new PrintJobData.PageOrderRule(53, 29),
                new PrintJobData.PageOrderRule(61, 53),
                new PrintJobData.PageOrderRule(61, 29),
                new PrintJobData.PageOrderRule(47, 13),
                new PrintJobData.PageOrderRule(75, 47),
                new PrintJobData.PageOrderRule(97, 75),
                new PrintJobData.PageOrderRule(47, 61),
                new PrintJobData.PageOrderRule(75, 61),
                new PrintJobData.PageOrderRule(47, 29),
                new PrintJobData.PageOrderRule(75, 13),
                new PrintJobData.PageOrderRule(53, 13)
        ));
    }

    @ParameterizedTest
    @MethodSource("validateUpdate")
    @DisplayName("Should validateUpdate")
    void shouldValidateUpdate(Update update, boolean expectedRightOrder) {
        assertThat(examplePageOrderingRules.test(update)).isEqualTo(expectedRightOrder);
    }

    static Stream<Arguments> validateUpdate() {
        return Stream.of(
                arguments(new Update(75, 47, 61, 53, 29), true),
                arguments(new Update(97, 61, 53, 29, 13), true),
                arguments(new Update(75, 29, 13), true),

                arguments(new Update(75, 97, 47, 61, 53), false),
                arguments(new Update(61, 13, 29), false),
                arguments(new Update(97, 13, 75, 29, 47), false)
        );
    }

    @Test
    @DisplayName("Should validate an update to be in right order when no ordering rules exists")
    void shouldBeInRightOrderWhenNoOrderingRuleExists() {
        var pageOrderingRules = new PageOrderingRules(Stream.of());
        var update = new Update(61, 13, 29);

        boolean isInRightOrder = pageOrderingRules.test(update);

        assertThat(isInRightOrder).isTrue();
    }

    @Test
    @DisplayName("Should validate an update to be in right order when no ordering rule is applicable")
    void shouldBeInRightOrderWhenNoOrderingRuleIsApplicable() {
        var pageOrderingRules = new PageOrderingRules(Stream.of(
                new PrintJobData.PageOrderRule(75, 61),
                new PrintJobData.PageOrderRule(47, 61)
        ));
        var update = new Update(61, 13, 29);

        boolean isInRightOrder = pageOrderingRules.test(update);

        assertThat(isInRightOrder).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "75",
            "47",
            "61",
            "53",
            "29"
    })
    @DisplayName("Should be in right order by forerunner")
    void shouldValidateByForerunner(int forerunner) {
        var update = new Update(75, 47, 61, 53, 29);

        // applicable ordering rules
        // 75 -> 53, 47, 61, 13
        // 47 -> 61, 29
        // 61 -> 13, 53, 29
        // 53 -> 29, 13
        // 29 -> 13

        assertThat(examplePageOrderingRules.testOrderRuleBy(forerunner, update)).isTrue();
    }

    @Test
    @DisplayName("Should detect order violations by forerunner")
    void shouldDetectOrderViolationsByForerunner() {
        var update = new Update(75, 97, 47, 61, 53);

        // applicable ordering rules
        // 75 -> 53, 47, 61, 13
        // 97 -> 13, 61, 47, 29, 75
        // 47 -> 61, 29
        // 61 -> 13, 53, 29
        // 53 -> 29, 13

        assertThat(examplePageOrderingRules.testOrderRuleBy(97, update)).isFalse();

        assertThat(examplePageOrderingRules.testOrderRuleBy(75, update)).isTrue();
        assertThat(examplePageOrderingRules.testOrderRuleBy(47, update)).isTrue();
        assertThat(examplePageOrderingRules.testOrderRuleBy(61, update)).isTrue();
        assertThat(examplePageOrderingRules.testOrderRuleBy(53, update)).isTrue();
    }

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