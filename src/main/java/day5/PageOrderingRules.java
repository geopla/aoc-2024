package day5;

import day5.input.PrintJobData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PageOrderingRules {

    Map<Integer, List<Integer>> orderRuleByForerunner;

    PageOrderingRules(Stream<PrintJobData.PageOrderRule> input) {
        orderRuleByForerunner = input.collect(Collectors.groupingBy(
                PrintJobData.PageOrderRule::forerunner,
                Collectors.mapping(PrintJobData.PageOrderRule::successor, Collectors.toList()))
        );
    }

    List<Integer> successorsOf(Integer pageNumber) {
        return orderRuleByForerunner.get(pageNumber);
    }

    boolean hasOrderingRule(Integer pageNumber) {
        return orderRuleByForerunner.containsKey(pageNumber);
    }
}
