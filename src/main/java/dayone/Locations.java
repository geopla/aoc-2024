package dayone;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;


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

        return Stream.generate(new Zipper(groupOneLocations, groupTwoLocations))
                .limit(toShorterListLength)
                .map(pair -> Math.abs(pair.x - pair.y))
                .mapToInt(Integer::intValue)
                .sum();
    }
}
