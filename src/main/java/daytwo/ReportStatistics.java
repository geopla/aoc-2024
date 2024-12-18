package daytwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReportStatistics {

    public static long safeReportCountFrom(String resourceName) {
        return countReports(resourceName, ReportStatistics::safeReportCount);
    }

    public static long safeReportCountWithProblemDampenerFrom(String resourceName) {
        return countReports(resourceName, ReportStatistics::safeReportCountWithProblemDampener);
    }

    static long safeReportCountFrom(List<String> unusualData) {
        return safeReportCount(unusualData.stream());
    }

    static long safeReportCountWithProblemDampenerFrom(List<String> unusualData) {
        return safeReportCountWithProblemDampener(unusualData.stream());
    }

    private static long countReports(String resourceName, Function<Stream<String>, Long> countFunction) {
        try (InputStream unusualDataStream = ReportStatistics.class.getResourceAsStream(resourceName)) {
            var bufferedReader = new BufferedReader(new InputStreamReader(unusualDataStream, StandardCharsets.UTF_8));
            return countFunction.apply(bufferedReader.lines());
        } catch (IOException e) {
            throw new RuntimeException("failed to read unusual data from file: %s".formatted(resourceName), e);
        }
    }

    private static long safeReportCount(Stream<String> levels) {
        return levels
                .map(Report::new)
                .filter(Report::isSafe)
                .count();
    }

    private static long safeReportCountWithProblemDampener(Stream<String> levels) {
        return levels
                .map(Report::new)
                .filter(Report::isSafeWithProblemDampener)
                .count();
    }
}
