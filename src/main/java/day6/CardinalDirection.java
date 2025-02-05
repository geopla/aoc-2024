package day6;

enum CardinalDirection {
    NORTH {
        @Override
        CardinalDirection turnLeft() {
            return WEST;
        }

        @Override
        CardinalDirection turnRight() {
            return EAST;
        }
    },
    EAST {
        @Override
        CardinalDirection turnLeft() {
            return NORTH;
        }

        @Override
        CardinalDirection turnRight() {
            return SOUTH;
        }
    },
    SOUTH {
        @Override
        CardinalDirection turnLeft() {
            return EAST;
        }

        @Override
        CardinalDirection turnRight() {
            return WEST;
        }
    },
    WEST {
        @Override
        CardinalDirection turnLeft() {
            return SOUTH;
        }

        @Override
        CardinalDirection turnRight() {
            return NORTH;
        }
    };

    abstract CardinalDirection turnLeft();
    abstract CardinalDirection turnRight();
}
