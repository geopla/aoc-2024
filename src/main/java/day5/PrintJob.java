package day5;

import day5.input.PrintJobData;

import java.util.List;
import java.util.stream.Stream;

class PrintJob {

    private final PageOrderingRules pageOrderingRules;
    private final List<Update> updates;

    PrintJob(List<PrintJobData> printJobData) {
        // TODO eliminate the intermediate list input

        pageOrderingRules = new PageOrderingRules(printJobData.stream()
                .filter(thingy -> thingy instanceof PrintJobData.PageOrderRule)
                .map(PrintJobData.PageOrderRule.class::cast));

        updates = printJobData.stream()
                .filter(thingy -> thingy instanceof PrintJobData.Update)
                .map(PrintJobData.Update.class::cast)
                .map(printJobDataUpdate -> new Update(printJobDataUpdate.pageNumbers()))
                .toList();
    }

    public PageOrderingRules pageOrderingRules() {
        return pageOrderingRules;
    }

    public Stream<Update> updates() {
        return updates.stream();
    }
}
