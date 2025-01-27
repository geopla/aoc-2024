package day5;

import day5.input.PrintJobData;
import day5.input.SafetyManualUpdateInputParser;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SafetyManualUpdate {

    private PrintJob printJob;


    public void printJobFromInput(String name) {
        List<PrintJobData> printJobData = SafetyManualUpdateInputParser.readFromResource(name).toList();
        printJob = new PrintJob(printJobData);
    }

    public Stream<Update> validUpdates() {
        return printJob.updates()
                .filter(update -> printJob.pageOrderingRules().test(update));
    }

    public int middlePageNumberSumOfValidUpdates() {
        return printJob.updates()
                .filter(update -> printJob.pageOrderingRules().test(update))
                .map(update -> update.middlePageNumber())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public PrintJob printJob() {
        return printJob;
    }
}
