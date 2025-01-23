package day5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SafetyManualUpdateInputParser {

    static Pattern pageOrderRulePattern = Pattern.compile("^(?<forerunner>\\d+)\\|(?<successor>\\d+)$");
    static Pattern updatePattern = Pattern.compile("^\\d+(,\\d+)*$");

    public static Stream<PrintJobData> read(Stream<String> lines) {
        return lines
                .map(SafetyManualUpdateInputParser::printJobData)
                .filter(Objects::nonNull);
    }

    @Deprecated
    static Map<String, List<PrintJobData>> createPrintJob(Stream<String> lines) {
        return lines
                .map(SafetyManualUpdateInputParser::printJobData)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(PrintJobData::key));
    }

    static PrintJobData printJobData(String line) {
        if (isPageOrderRule(line)) {
            return createPageOrderRule(line);
        } else if (isUpdate(line)) {
            return createUpdate(line);
        } else {
            return null;
        }
    }

    static PrintJobData.Update createUpdate(String line) {
        var pageNumbers = new Scanner(line).useDelimiter(",").tokens()
                .map(Integer::parseInt)
                .toList();

        return new PrintJobData.Update(pageNumbers);
    }

    static PrintJobData.PageOrderRule createPageOrderRule(String line) {
        Matcher matcher = pageOrderRulePattern.matcher(line);
        matcher.find();

        var forerunner = Integer.parseInt(matcher.group("forerunner"));
        var successor = Integer.parseInt(matcher.group("successor"));

        return new PrintJobData.PageOrderRule(forerunner, successor);
    }

    static boolean isPageOrderRule(String input) {
        Matcher matcher = pageOrderRulePattern.matcher(input.trim());
        return matcher.matches();
    }

    static boolean isUpdate(String input) {
        Matcher matcher = updatePattern.matcher(input.trim());
        return matcher.matches();
    }

    static Stream<String> fromResource(String name) {
        InputStream sampleInput = SafetyManualUpdateInputParser.class.getResourceAsStream(name);
        var reader = new BufferedReader(new InputStreamReader(sampleInput));

        return reader.lines().onClose(() -> {
            try {
                reader.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
