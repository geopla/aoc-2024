package day6;

record LegPlanned(
        Room.Position start,
        CardinalDirection direction,
        int steps) {

    public LegPlanned(Room.Position start, CardinalDirection direction) {
        this(start, direction, Integer.MAX_VALUE);
    }
}
