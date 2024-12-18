package daytwo;

import java.util.Spliterator;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Report {

    private final String levels;
    private final Predicate<LevelPair> safetyPredicate;

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

    public Report(String levels) {
        this.levels = levels;

        Predicate<LevelPair> growthPredicate = LevelGrowthPredicates.createFrom(levelPairsFrom(levels()));

        final var minDistance = 1;
        final var maxDistance = 3;
        final Predicate<LevelPair> distancePredicate = new LevelDistancePredicate(minDistance, maxDistance);

        safetyPredicate = growthPredicate.and(distancePredicate);
    }

    public boolean isSafeWithProblemDampener() {
        final int violationPosition = firstFirstViolationPosition();

        if (violationPosition == -1) {
            return true;
        }
        else {
            var levelsHavingFirstBadLevelSkipped = IntStream.concat(
                    levels().limit(violationPosition),
                    levels().skip(violationPosition + 1)
            );
            return levelPairsFrom(levelsHavingFirstBadLevelSkipped).allMatch(safetyPredicate);
        }
    }

    int firstFirstViolationPosition() {
        var violationPosition = new AtomicInteger(0);

        return levelPairsFrom(levels())
                .peek(levelPair -> violationPosition.incrementAndGet())
                .filter(levelPair -> !safetyPredicate.test(levelPair))
                .findFirst()
                .map(unused -> violationPosition.get() - 1)
                .orElse(-1);
    }

    public boolean isSafe() {
        return levelPairsFrom(levels()).allMatch(safetyPredicate);
    }

    Stream<LevelPair> levelPairsFrom(IntStream levels) {
        Spliterator<LevelPair> spliterator = new LevelPairSpliterator(levels.spliterator());
        return StreamSupport.stream(spliterator, false);
    }

    IntStream levels() {
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
