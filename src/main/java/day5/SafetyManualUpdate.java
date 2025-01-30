package day5;

import day5.input.PrintJobData;
import day5.input.SafetyManualUpdateInputParser;

import java.util.List;
import java.util.stream.Stream;

public class SafetyManualUpdate {

    private PrintJob printJob;

    // TODO not a smart way to get the construction done, but it does the job for now

    public void printJobFromInput(String name) {
        List<PrintJobData> printJobData = SafetyManualUpdateInputParser.readFromResource(name).toList();
        printJob = new PrintJob(printJobData);
    }

    public int middlePageNumberSumOfValidUpdates() {
        return printJob.updates()
                .filter(update -> printJob.pageOrderingRules().test(update))
                .map(Update::middlePageNumber)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int middlePageNumberSumOfReorderedUpdates() {
        return printJob.updates()
                .filter(update -> ! printJob.pageOrderingRules().test(update))
                .map(update -> printJob.pageOrderingRules().reorder(update))
                .map(Update::middlePageNumber)
                .mapToInt(Integer::intValue)
                .sum();
    }

     Stream<Update> validUpdates() {
        return printJob.updates()
                .filter(update -> printJob.pageOrderingRules().test(update));
    }

    public Stream<Update> invalidUpdates() {
        return printJob.updates()
                .filter(update -> ! printJob.pageOrderingRules().test(update));
    }

    PrintJob printJob() {
        return printJob;
    }
}
