package day6;

public record Leg(
        Room.Position start,
        CardinalDirection direction,
        int steps,
        Terminator terminator) {
}
