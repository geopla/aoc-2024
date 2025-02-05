package day6;

import java.util.Objects;
import java.util.Optional;

class TurnRight implements TurnStrategy {

    @Override
    public Optional<CardinalDirection> changeDirectionOn(Terminator terminator, CardinalDirection comingFrom) {
        Objects.requireNonNull(terminator, "terminator is required not to be null");

        return terminator == Terminator.OBSTRUCTION ? Optional.of(comingFrom.turnRight()) : Optional.empty();
    }
}
