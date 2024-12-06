package dayone;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class DayOneInput {

    private LinkedList<Integer> groupOneLocations = new LinkedList<>();
    private LinkedList<Integer> groupTwoLocations = new LinkedList<>();
    private InputStream data;


    DayOneInput() {
        data = getClass().getResourceAsStream("../day-one-input.txt");
        parse();
    }

    List<Integer> groupOneLocations() {
        return groupOneLocations;
    }

    List<Integer> groupTwoLocations() {
        return groupTwoLocations;
    }

    InputStream data() {
        return data;
    }

    private void parse() {
        try (Scanner scanner = new Scanner(data, StandardCharsets.UTF_8)) {
            var pattern = Pattern.compile("^(?<groupOne>\\d+)\\s+(?<groupTwo>\\d+)\\s*$");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher locations = pattern.matcher(line);

                if (locations.matches()) {
                    groupOneLocations.add(Integer.parseInt(locations.group("groupOne")));
                    groupTwoLocations.add(Integer.parseInt(locations.group("groupTwo")));
                }
                else {
                    throw new IllegalArgumentException("malformed input");
                }
            }
        }
    }
}
