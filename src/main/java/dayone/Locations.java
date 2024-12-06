package dayone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.sort;


public class Locations {

    record Pair(int x, int y) { }

    static class Zipper implements Supplier<Pair> {

        final List<Integer> x;

        final List<Integer> y;
        Zipper(List<Integer> x, List<Integer> y) {
            this.x = new ArrayList<>(x);
            this.y = new ArrayList<>(y);
        }

        @Override
        public Pair get() {
            return new Pair(x.removeFirst(), y.removeFirst());
        }

    }
    public static int totalDistance(List<Integer> groupOneLocations, List<Integer> groupTwoLocations) {
        final int toShorterListLength = Math.min(groupTwoLocations.size(), groupOneLocations.size());

        // make the n-th smallest numbers accessible by simply sorting the locations - we are good with duplicates
        ArrayList<Integer> groupOne = new ArrayList<>(groupOneLocations);
        sort(groupOne);
        ArrayList<Integer> groupTwo = new ArrayList<>(groupTwoLocations);
        sort(groupTwo);

        return Stream.generate(new Zipper(groupOne, groupTwo))
                .limit(toShorterListLength)
                .map(pair -> Math.abs(pair.x - pair.y))
                .mapToInt(Integer::intValue)
                .sum();
    }

    public static int similarityScore(List<Integer> left, List<Integer> right) {

        return 0;
    }

    public static Map<Integer, Integer> cardinalityMultiplierMap(List<Integer> cardinalityList) {

        return cardinalityList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(entry -> 1)));
    }
}
