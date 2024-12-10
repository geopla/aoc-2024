package dayone;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DayOneInput {

    private final List<Integer> groupOneLocations = new LinkedList<>();
    private final List<Integer> groupTwoLocations = new LinkedList<>();
    private final InputStream data;

    record LocationIdGroups(
            List<Integer> one,
            List<Integer> two
    ) { }

    DayOneInput() {
        data = getClass().getResourceAsStream("../day-one-input.txt");
        parse();
    }

    LocationIdGroups locationIdGroups() {
        return new LocationIdGroups(
                Collections.unmodifiableList(groupOneLocations),
                Collections.unmodifiableList(groupTwoLocations));
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
                } else {
                    throw new IllegalArgumentException("malformed input");
                }
            }
        }
    }
}
