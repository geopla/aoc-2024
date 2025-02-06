package day6;

import java.util.Objects;
import java.util.Optional;

class TurnRight implements TurnStrategy {

    @Override
    public Optional<CardinalDirection> changeDirectionOn(Leg<Lifecycle.Computed> leg) {
        Objects.requireNonNull(leg.state().terminator(), "terminator is required not to be null");

        return leg.state().terminator() == Terminator.OBSTRUCTION ? Optional.of(leg.direction().turnRight()) : Optional.empty();
    }
}
