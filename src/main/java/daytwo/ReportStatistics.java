package daytwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class ReportStatistics {

    static long safeReportCountFrom(List<String> unusualData) {
        return safeReportCount(unusualData.stream());
    }

    public static long safeReportCountFrom(String resourceName) {

        try (InputStream unusualDataStream = ReportStatistics.class.getResourceAsStream(resourceName)) {
            var bufferedReader = new BufferedReader(new InputStreamReader(unusualDataStream, StandardCharsets.UTF_8));
            return safeReportCount(bufferedReader.lines());
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
}
