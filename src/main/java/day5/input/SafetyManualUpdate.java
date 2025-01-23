package day5.input;

import java.util.List;

public class SafetyManualUpdate {

    private final List<PrintJobData.PageOrderRule>  pageOrderRules;
    private final List<PrintJobData.Update> updates;

    public SafetyManualUpdate(List<PrintJobData.PageOrderRule> pageOrderRules, List<PrintJobData.Update> updates) {
        this.pageOrderRules = pageOrderRules;
        this.updates = updates;
    }
}
