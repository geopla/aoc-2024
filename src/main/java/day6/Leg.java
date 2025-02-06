package day6;

record Leg<State extends Lifecycle>(
        Room.Position start,
        CardinalDirection direction,
        int steps,
        State state) {

    static int stepsUnlimited() {
        return Integer.MAX_VALUE;
    }

    Room.Position end() {
        return switch (direction) {
            case NORTH -> new Room.Position(start.x(), start.y() - steps);
            case EAST -> new Room.Position(start.x() + steps, start.y());
            case SOUTH -> new Room.Position(start.x(), start.y() + steps);
            case WEST -> new Room.Position(start.x() - steps, start.y());
        };
    }
}
