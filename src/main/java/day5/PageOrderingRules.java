package day5;

import day5.input.PrintJobData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PageOrderingRules implements Predicate<Update> {

    private final Map<Integer, List<Integer>> orderRuleByForerunner;

    PageOrderingRules(Stream<PrintJobData.PageOrderRule> input) {
        orderRuleByForerunner = input.collect(Collectors.groupingBy(
                PrintJobData.PageOrderRule::forerunner,
                Collectors.mapping(PrintJobData.PageOrderRule::successor, Collectors.toList()))
        );
    }

    @Override
    public boolean test(Update update) {
        return orderRuleByForerunner.keySet().stream()
                .allMatch(forerunner -> testOrderRuleBy(forerunner, update));
    }

    public Update reorder(Update update) {

        // TRADEOFF Due to possible memory or runtime penalties because of unknown size of updates a more
        // elegant recursive solution is not implemented here.

        Optional<Integer> currentPage = update.firstPage();
        Update currentUpdate = update;

        while (currentPage.isPresent()) {
            var page = currentPage.get();
            var successorsButPrintedBefore = successorsButPrintedBefore(page, currentUpdate);

            currentUpdate = currentUpdate.createUpdateWithPageMovedBeforeSuccessors(page, successorsButPrintedBefore);

            currentPage = currentUpdate.nextPage(page);
        }

        return currentUpdate;
    }

    boolean testOrderRuleBy(int forerunner, Update update) {
        var successorsButPrintedBefore = successorsButPrintedBefore(forerunner, update);

        return successorsButPrintedBefore.isEmpty();
    }

    private List<Integer> successorsButPrintedBefore(int forerunner, Update update) {
        var pageNumbersBefore = update.pageNumbersBefore(forerunner);

        List<Integer> successorsButPrintedBefore = successorsOf(forerunner);
        successorsButPrintedBefore.retainAll(pageNumbersBefore);

        return successorsButPrintedBefore;
    }

    List<Integer> successorsOf(Integer pageNumber) {
        if (orderRuleByForerunner.containsKey(pageNumber)) {
            return new ArrayList<>(orderRuleByForerunner.get(pageNumber));
        }
        else {
            return new ArrayList<>();
        }
    }

    boolean hasOrderingRule(Integer pageNumber) {
        return orderRuleByForerunner.containsKey(pageNumber);
    }

}
