package day6;

import java.util.Optional;

interface TurnStrategy {

    // Optional<CardinalDirection> changeDirectionOn(Terminator terminator, CardinalDirection comingFrom);

    Optional<CardinalDirection> changeDirectionOn(Leg<Lifecycle.Computed> leg);


}
