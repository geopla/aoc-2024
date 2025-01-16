package day4;

public class Star {

    record Position(int x, int y) {
    }

    enum CardinalDirection {
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST;
    }

    private final TextBlock textBlock;

    private final Position position;

    Star(TextBlock textBlock, Position position) {
        this.textBlock = textBlock;
        this.position = position;
    }

    Position position() {
        return this.position;
    }

    char centerValue() {
        return textBlock.charAt(position.x, position.y);
    }

    boolean hasNeighbourTo(CardinalDirection cardinalDirection) {
        return switch (cardinalDirection) {
            case NORTH -> hasNeighbourToTheNorth();
            case NORTH_EAST -> hasNeighbourToTheNorthEast();
            case EAST -> hasNeighbourToTheEast();
            case SOUTH_EAST -> hasNeighbourToTheSouthEast();
            case SOUTH -> hasNeighbourToTheSouth();
            case SOUTH_WEST -> hasNeighbourToTheSouthWest();
            case WEST -> hasNeighbourToTheWest();
            case NORTH_WEST -> hasNeighbourToTheNorthWest();
        };
    }

    private boolean hasNeighbourToTheNorth() {
        return textBlock.hasCharAt(position.x, position.y -1);
    }

    private boolean hasNeighbourToTheNorthEast() {
        return textBlock.hasCharAt(position.x + 1, position.y -1);
    }

    private boolean hasNeighbourToTheEast() {
        return textBlock.hasCharAt(position.x + 1, position.y);
    }

    private boolean hasNeighbourToTheSouthEast() {
        return textBlock.hasCharAt(position.x + 1, position.y + 1);
    }

    private boolean hasNeighbourToTheSouth() {
        return textBlock.hasCharAt(position.x, position.y + 1);
    }

    private boolean hasNeighbourToTheSouthWest() {
        return textBlock.hasCharAt(position.x - 1, position.y + 1);
    }

    private boolean hasNeighbourToTheWest() {
        return textBlock.hasCharAt(position.x - 1, position.y);
    }

    private boolean hasNeighbourToTheNorthWest() {
        return textBlock.hasCharAt(position.x - 1, position.y - 1);
    }
}
