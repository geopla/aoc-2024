package day6;

import java.util.Optional;

interface TurnStrategy {

    Optional<CardinalDirection> changeDirectionOn(Terminator terminator, CardinalDirection comingFrom);
}
