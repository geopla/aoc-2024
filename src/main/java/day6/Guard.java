package day6;

import static day6.CardinalDirection.NORTH;

class Guard {

    private final Room room;
    private final Room.Position startPosition;
    private final CardinalDirection startFacing;

    private TurnStrategy turnStrategy;

    Guard(Room room, Room.Position position, char facing) {
        this.room = room;
        startPosition = position;

        // hard coded due to current requirement but may change in future
        startFacing = NORTH;

        if (facing != '^') {
            throw new IllegalArgumentException("guard's start facing needs to be north");
        }

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
