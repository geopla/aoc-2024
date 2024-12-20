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
    private Predicate<LevelPair> safetyPredicate;

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
    }

    private void initializeSafetyPredicate(LevelPair levelGrowthMarker) {
        var growthPredicate = LevelGrowthPredicates.createFrom(levelGrowthMarker);
        var distancePredicate = createLevelDistancePredicate();

        safetyPredicate =  growthPredicate.and(distancePredicate);
    }


    private static Predicate<LevelPair> createLevelDistancePredicate() {
        final var minDistance = 1;
        final var maxDistance = 3;
        return new LevelDistancePredicate(minDistance, maxDistance);
    }
    
    public boolean isSafeWithProblemDampener() {
        if (isSafe()) {
            return true;
        } else {
            return isSafeWhenRemovingOneElement();
        }
    }

    private boolean isSafeWhenRemovingOneElement() {
        return dampenerLevels(levels())
                .map(this::levelPairs)
                .map(this::isSafe)
                .filter(safe -> safe)
                .findFirst()
                .orElse(false);
    }

    public boolean isSafe() {
        return isSafe(levelPairs());
    }

    boolean isSafe(Stream<LevelPair> levelPairs) {
        var levelPairMarkerSpliterator = new MarkerSpliterator<>(
                levelPairs.spliterator(),
                levelPair -> ! levelPair.hasEqualValues()
        );

        levelPairMarkerSpliterator.marker().ifPresent(this::initializeSafetyPredicate);

        return StreamSupport.stream(levelPairMarkerSpliterator, false).allMatch(safetyPredicate);
    }

    IntStream levels() {
        var levelsIterator = new StringTokenizer(levels).asIterator();

        return Stream.generate(() -> null)
                .takeWhile(thinghy -> levelsIterator.hasNext())
                .map(obj -> levelsIterator.next())
                .mapToInt(Report::tryParseInteger);
    }

    Stream<LevelPair> levelPairs() {
        return levelPairs(levels());
    }

    Stream<LevelPair> levelPairs(IntStream levels) {
        Spliterator<LevelPair> spliterator = new LevelPairSpliterator(levels.spliterator());
        return StreamSupport.stream(spliterator, false);
    }

    Stream<IntStream> dampenerLevels() {
        return dampenerLevels(levels());
    }

    Stream<IntStream> dampenerLevels(IntStream levels) {
        int[] allLevels = levels.toArray();

        return IntStream.range(0, allLevels.length)
                .mapToObj(n -> IntStream.concat(
                        IntStream.of(allLevels).limit(n),
                        IntStream.of(allLevels).skip(n + 1)
                ));
    }

    private static int tryParseInteger(Object value) {
        try {
            return Integer.parseInt((String) value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
