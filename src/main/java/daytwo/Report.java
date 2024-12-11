package daytwo;

import java.util.StringTokenizer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Report {

    private final String levels;

    public Report(String levels) {
        this.levels = levels;
    }

    IntStream levels() {
        return parseLevels();
    }

    private IntStream parseLevels() {
        var levelsIterator = new StringTokenizer(levels).asIterator();

        return  Stream.generate(() -> null)
                .takeWhile(thinghy -> levelsIterator.hasNext())
                .map(obj -> levelsIterator.next())
                .mapToInt(Report::tryParseInteger);
    }

    private static int tryParseInteger(Object value) {
        try {
            return Integer.parseInt((String) value);
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
