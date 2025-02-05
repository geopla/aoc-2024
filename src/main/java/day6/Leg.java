package day6;

import java.util.stream.Stream;

record Leg<State extends Lifecycle>(
        Room.Position start,
        CardinalDirection direction,
        int steps,
        State state) {
}

