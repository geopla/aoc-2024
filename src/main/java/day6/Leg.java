package day6;

import java.util.stream.IntStream;
import java.util.stream.Stream;

record Leg<State extends Lifecycle>(
        Room.Position start,
        CardinalDirection direction,
        int steps,
        State state) {

    static int stepsUnlimited() {
        return Integer.MAX_VALUE;
    }

    public boolean isZeroStepToBorder() {
        return switch (state) {
            case Lifecycle.Planned planned -> false;
            case Lifecycle.Computed computed -> steps == 0 && computed.terminator() == Terminator.BORDER;
        };
    }

    Room.Position end() {
        return switch (direction) {
            case NORTH -> new Room.Position(start.x(), start.y() - steps);
            case EAST -> new Room.Position(start.x() + steps, start.y());
            case SOUTH -> new Room.Position(start.x(), start.y() + steps);
            case WEST -> new Room.Position(start.x() - steps, start.y());
        };
    }

    Stream<Room.Position> positions() {
        return switch (direction) {
            case NORTH -> positionsToNorth();
            case EAST -> positionsToEast();
            case SOUTH -> positionsToSouth();
            case WEST -> positionsToWest();
        };
    }

    private Stream<Room.Position> positionsToNorth() {
        return IntStream.rangeClosed(0, steps)
                .boxed()
                .map(i -> new Room.Position(start.x(), start.y() - i));
    }

    private Stream<Room.Position> positionsToEast() {
        return IntStream.rangeClosed(0, steps)
                .boxed()
                .map(i -> new Room.Position(start.x() + i, start.y()));
    }

    private Stream<Room.Position> positionsToSouth() {
        return IntStream.rangeClosed(0, steps)
                .boxed()
                .map(i -> new Room.Position(start.x(), start.y() + i));
    }

    private Stream<Room.Position> positionsToWest() {
        return IntStream.rangeClosed(0, steps)
                .boxed()
                .map(i -> new Room.Position(start.x() - i, start.y()));
    }
}
