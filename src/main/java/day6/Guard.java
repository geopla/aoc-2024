package day6;

import static day6.CardinalDirection.NORTH;

class Guard {

    private final Room room;
    private final Room.Position startPosition;
    private final CardinalDirection startFacing;

    private TurnStrategy turnStrategy;

    Guard(Room room, Room.Position position, char facing) {
        this.room = room;
        if (room == null) {
            throw new IllegalArgumentException("guard must be assigned to a room");
        }

        startPosition = position;
        if (startPosition == null) {
            throw new IllegalArgumentException("guard must be placed on a start position");
        }

        startFacing = CardinalDirection.from(facing);
        turnStrategy = new TurnRight();
    }

    public Room room() {
        return room;
    }

    Room.Position startPosition() {
        return startPosition;
    }

    CardinalDirection startFacing() {
        return startFacing;
    }

    TurnStrategy turnStrategy() {
        return turnStrategy;
    }
}
