package day5;

import day5.input.PrintJobData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PrintJobTest {

    @Test
    @DisplayName("Should have page ordering rules")
    void shouldHavePageOrderingRules() {
        List<PrintJobData> printJobData = List.of(
                new PrintJobData.PageOrderRule(35, 97),
                new PrintJobData.PageOrderRule(35, 72),
                new PrintJobData.PageOrderRule(96, 93),
                new PrintJobData.Update(List.of(61, 13, 29))
        );

        PrintJob printJob = new PrintJob(printJobData);

        assertThat(printJob.pageOrderingRules().hasOrderingRule(35)).isTrue();
        assertThat(printJob.pageOrderingRules().hasOrderingRule(96)).isTrue();
    }

    @Test
    @DisplayName("Should have updates")
    void shouldHaveUpdates() {
        List<PrintJobData> printJobData = List.of(
                new PrintJobData.PageOrderRule(35, 97),
                new PrintJobData.Update(List.of(61, 13, 29)),
                new PrintJobData.Update(List.of(75, 47, 61, 53, 29))
        );

        PrintJob printJob = new PrintJob(printJobData);

        assertThat(printJob.updates()).containsExactlyInAnyOrder(
                new Update(61, 13, 29),
                new Update(75, 47, 61, 53, 29)
        );
    }
}