package daytwo;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static daytwo.Report.LevelPair;

class LevelGrowthPredicates {

    static class GraduallyIncreasing implements Predicate<LevelPair> {

        @Override
        public boolean test(LevelPair levelPair) {
            return levelPair.first() < levelPair.second();
        }
    }

    static class GraduallyDecreasing implements Predicate<LevelPair> {

        @Override
        public boolean test(LevelPair levelPair) {
            return levelPair.first() > levelPair.second();
        }
    }

    public static Predicate<LevelPair> createFrom(Stream<LevelPair> levelPairStream) {
        return levelPairStream
                .dropWhile(LevelPair::hasEqualValues)
                .findFirst()
                .map(LevelGrowthPredicates::createFrom)
                .orElse(empty -> false);

    }

    private static Predicate<LevelPair> createFrom(LevelPair levelPair) {
        if (levelPair.first() < levelPair.second()) {
            return new GraduallyIncreasing();
        }
        else if (levelPair.first() > levelPair.second())  {
            return new GraduallyDecreasing();
        }

        throw new IllegalStateException("tried to create predicate from flat level pair");
    }

}
