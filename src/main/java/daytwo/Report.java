package daytwo;

import java.util.Spliterator;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Report {
    private final String levels;

    record LevelPair(Integer first, Integer second) {

        boolean hasEqualValues() {
            return first.equals(second);
        }
    }

    static class LevelPairSpliterator implements Spliterator<LevelPair> {
        private final Spliterator.OfInt sourceElementsSpliterator;
        private int current;
        private boolean hasNext;

        LevelPairSpliterator(Spliterator.OfInt sourceElementsSpliterator) {
            this.sourceElementsSpliterator = sourceElementsSpliterator;
            this.hasNext = sourceElementsSpliterator.tryAdvance((int value) -> current = value);
        }

        @Override
        public boolean tryAdvance(Consumer<? super LevelPair> action) {
            if (!hasNext) {
                return false;
            }
            int previous = current;
            hasNext = sourceElementsSpliterator.tryAdvance((int value) -> current = value);

            if (hasNext) {
                action.accept(new LevelPair(previous, current));
            }
            return hasNext;
        }

        @Override
        public Spliterator<LevelPair> trySplit() {
            return null; // do not allow partitioning
        }

        @Override
        public long estimateSize() {
            return sourceElementsSpliterator.estimateSize() - 1;
        }

        @Override
        public int characteristics() {
            return sourceElementsSpliterator.characteristics() & ~Spliterator.SIZED;
        }
    }


    public boolean isSafe() {
        final Predicate<LevelPair> growthPredicate = LevelGrowthPredicates.createFrom(levelPairs());

        final var minDistance = 1;
        final var maxDistance = 3;
        final Predicate<LevelPair> distancePredicate = new LevelDistancePredicate(minDistance, maxDistance);

        return levelPairs().allMatch(growthPredicate.and(distancePredicate));
    }

    public Report(String levels) {
        this.levels = levels;
    }

    Stream<LevelPair> levelPairs() {
        IntStream levelStream = levels();
        Spliterator<LevelPair> spliterator = new LevelPairSpliterator(levelStream.spliterator());
        return StreamSupport.stream(spliterator, false);
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
