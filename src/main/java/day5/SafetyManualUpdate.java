package day5;

import java.util.List;

public class SafetyManualUpdate {

    private final List<PageOrderRule>  pageOrderRules;
    private final List<Update> updates;

    public SafetyManualUpdate(List<PageOrderRule> pageOrderRules, List<Update> updates) {
        this.pageOrderRules = pageOrderRules;
        this.updates = updates;
    }
}
