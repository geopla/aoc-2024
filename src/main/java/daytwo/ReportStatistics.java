package daytwo;

import java.util.List;

public class ReportStatistics {

    public static long safeReportCountFrom(List<String> unusualData) {

        return unusualData.stream()
                .map(Report::new)
                .filter(Report::isSafe)
                .count();
    }
}
