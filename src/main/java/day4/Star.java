package day4;

import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Star {

    record Position(int x, int y) { }

    public record Ray(CardinalDirection cardinalDirection, String value) { }

    public enum CardinalDirection {
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST
    }

    private final TextBlock textBlock;
    private final Position position;

    Star(TextBlock textBlock, Position position) {
        this.textBlock = textBlock;
        this.position = position;
    }

    Stream<Ray> rays() {
        return EnumSet.allOf(CardinalDirection.class).stream()
                .map(this::ray);
    }

    Stream<Ray> rays(int maxRayLength) {
        return EnumSet.allOf(CardinalDirection.class).stream()
                .map(cardinalDirection -> ray(cardinalDirection, maxRayLength));
    }

    Ray ray(CardinalDirection cardinalDirection) {
        String value = Stream.iterate(
                        this,
                        Objects::nonNull,
                        star -> star.hasNeighbourTo(cardinalDirection) ? star.neighbourTo(cardinalDirection) : null
                )
                .map(Star::centerValue)
                .collect(Collector.of(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append,
                        StringBuilder::toString));

        return new Ray(cardinalDirection, value);
    }

    Ray ray(CardinalDirection cardinalDirection, int maxRayLength) {
        String value = Stream.iterate(
                        this,
                        Objects::nonNull,
                        star -> star.hasNeighbourTo(cardinalDirection) ? star.neighbourTo(cardinalDirection) : null
                )
                .limit(maxRayLength)
                .map(Star::centerValue)
                .collect(Collector.of(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append,
                        StringBuilder::toString));

        return new Ray(cardinalDirection, value);
    }

    char centerValue() {
        return textBlock.charAt(position.x, position.y);
    }

    Position position() {
        return this.position;
    }

    public Star neighbourTo(CardinalDirection cardinalDirection) {
        return switch (cardinalDirection) {
            case NORTH -> new Star(textBlock, new Position(position.x, position.y - 1));
            case NORTH_EAST -> new Star(textBlock, new Position(position.x + 1, position.y - 1));
            case EAST -> new Star(textBlock, new Position(position.x + 1, position.y));
            case SOUTH_EAST -> new Star(textBlock, new Position(position.x + 1, position.y + 1));
            case SOUTH -> new Star(textBlock, new Position(position.x, position.y + 1));
            case SOUTH_WEST -> new Star(textBlock, new Position(position.x - 1, position.y + 1));
            case WEST -> new Star(textBlock, new Position(position.x - 1, position.y));
            case NORTH_WEST -> new Star(textBlock, new Position(position.x - 1, position.y - 1));
        };
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
        return textBlock.hasCharAt(position.x, position.y - 1);
    }

    private boolean hasNeighbourToTheNorthEast() {
        return textBlock.hasCharAt(position.x + 1, position.y - 1);
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
