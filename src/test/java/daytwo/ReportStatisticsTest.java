package daytwo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReportStatisticsTest {

    @Test
    @DisplayName("Should count safe reports from puzzle file")
    void shouldCountSafeReportsFromPuzzleFile() {
        long safeReportCount = ReportStatistics.safeReportCountFrom("../day-two-input.txt");

        assertThat(safeReportCount).isEqualTo(242);
    }

    @Test
    @DisplayName("Should count safe reports with problem dampener from puzzle file")
    void shouldCountSafeReportsWithProblemDampenerFromPuzzleFile() {
        long safeReportCount = ReportStatistics.safeReportCountWithProblemDampenerFrom("../day-two-input.txt");

        assertThat(safeReportCount).isEqualTo(311);
    }

    @Test
    @DisplayName("Should count safe reports from puzzle example file")
    void shouldCountSafeReportsFromPuzzleExampleFile() {
        long safeReportCount = ReportStatistics.safeReportCountFrom("../day-two-input-sample.txt");

        assertThat(safeReportCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Should count safe reports with problem dampener from puzzle example file")
    void shouldCountSafeReportsWithProblemDampenerFromPuzzleExampleFile() {
        long safeReportCount = ReportStatistics.safeReportCountWithProblemDampenerFrom("../day-two-input-sample.txt");

        assertThat(safeReportCount).isEqualTo(4);
    }

    @Test
    @DisplayName("Should count safe reports from puzzle example")
    void shouldCountSafeReportsFromPuzzleExample() {
        final List<String> unusualData = List.of(
                "7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9"
        );

        long safeReportCount = ReportStatistics.safeReportCountFrom(unusualData);

        assertThat(safeReportCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Should count safe reports with problem dampener from puzzle example")
    void shouldCountSafeReportsWithProblemDampenerFromPuzzleExample() {
        final List<String> unusualData = List.of(
                "7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9"
        );

        long safeReportCount = ReportStatistics.safeReportCountWithProblemDampenerFrom(unusualData);

        assertThat(safeReportCount).isEqualTo(4);
    }
}