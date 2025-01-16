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

    private TextBlock textBlock;

    private Position position;

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

    boolean hasNeighbour(CardinalDirection cardinalDirection) {
        return switch (cardinalDirection) {
            case NORTH -> hasNeighbourToTheNorth();
            case NORTH_EAST -> hasNeighbourToTheNorthEast();
            case EAST -> hasNeighbourToTheNorthEast();
            case SOUTH_EAST -> hasNeighbourToTheEast();
            case SOUTH -> false;
            case SOUTH_WEST -> false;
            case WEST -> false;
            case NORTH_WEST -> false;
        };
    }

    private boolean hasNeighbourToTheNorth() {
        return false;
    }

    private boolean hasNeighbourToTheNorthEast() {
        return false;
    }

    private boolean hasNeighbourToTheEast() {
        return false;
    }
}
