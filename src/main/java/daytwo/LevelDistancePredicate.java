package daytwo;

import java.util.function.Predicate;

class LevelDistancePredicate implements Predicate<Report.LevelPair> {

    private final int min;
    private final int max;

    LevelDistancePredicate(int min, int max) {
        this.min = min;
        this.max = max;
    }

    LevelDistancePredicate(int max) {
        this(1, max);
    }

    @Override
    public boolean test(Report.LevelPair levelPair) {
        var distance = Math.abs(levelPair.first() - levelPair.second());

        return min <= distance && distance <= max;
    }
}
