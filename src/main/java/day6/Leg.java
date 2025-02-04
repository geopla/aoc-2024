package day6;

public record Leg<Computed extends Lifecycle>(
        Room.Position start,
        CardinalDirection direction,
        Computed computed) {
}
