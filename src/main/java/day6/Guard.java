package day6;

import static day6.CardinalDirection.NORTH;

class Guard {

    private final Room room;
    private final Room.Position startPosition;
    private final CardinalDirection startFacing;

    Guard(Room room, Room.Position position, char facing) {
        this.room = room;
        startPosition = position;
        startFacing = NORTH;

        if (facing != '^') {
            throw new IllegalArgumentException("guard's start facing needs to be north");
        }
    }

    public Room room() {
        return room;
    }

    public Room.Position startPosition() {
        return startPosition;
    }

    public CardinalDirection startFacing() {
        return startFacing;
    }
}
