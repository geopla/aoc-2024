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

        @Override
        char symbol() {
            return '^';
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

        @Override
        char symbol() {
            return '>';
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

        @Override
        char symbol() {
            return 'v';
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

        @Override
        char symbol() {
            return '<';
        }
    };

    abstract CardinalDirection turnLeft();
    abstract CardinalDirection turnRight();
    abstract char symbol();

    static CardinalDirection from(char symbol) {
        return switch (symbol) {
            case '^' -> NORTH;
            case '>' -> EAST;
            case 'v' -> SOUTH;
            case '<' -> WEST;
            default -> throw new IllegalArgumentException("unknown direction symbol '%c'".formatted(symbol));
        };
    }
}
