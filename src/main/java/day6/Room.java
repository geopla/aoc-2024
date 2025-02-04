package day6;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

import static day6.CardinalDirection.*;
import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;
import static java.util.Collections.unmodifiableList;

class Room {

    private int length = 0;
    private int width = 0;
    private int currentX = 0;

    record Size(int width, int length) { }

    record Position(int x, int y) { }
    record Obstruction(char type, Position position) { }
    private final List<Obstruction> obstructions = new ArrayList<>();

    private final List<Guard> guards = new ArrayList<>();
    public static Room from(Reader input) {
        return new Room(input);
    }

    public static Room from(String input) {
        return new Room(new StringReader(input));
    }

    private Room(Reader input) {
        initializeFrom(input);
    }

    Leg realize(LegPlanned legPlanned) {
        return switch (legPlanned.direction()) {
            case NORTH -> realizeToNorth(legPlanned.start(), legPlanned.steps());
            case EAST -> realizeToEast(legPlanned.start(), legPlanned.steps());
            case SOUTH -> realizeToSouth(legPlanned.start(), legPlanned.steps());
            case WEST -> realizeToWest(legPlanned.start(), legPlanned.steps());
        };
    }

    private Leg realizeToNorth(Position start, int steps) {
        var distanceToBorder = start.y;

        int availableSteps = obstructions.stream()
                .filter(o -> o.position.x == start.x && o.position.y < start.y)
                .max(Comparator.comparingInt(o -> o.position.y))
                .map(obstruction -> start.y - obstruction.position.y - 1)
                .orElse(distanceToBorder);

        return new Leg(
                start,
                NORTH,
                availableSteps,
                terminator(start, NORTH, availableSteps));
    }

    private Leg realizeToEast(Position start, int steps) {
        var distanceToBorder = width - start.x - 1;

        int availableSteps = obstructions.stream()
                .filter(o -> o.position.y == start.y && o.position.x > start.x)
                .min(Comparator.comparingInt(o -> o.position.x))
                .map(obstruction -> obstruction.position.x - start.x - 1)
                .orElse(distanceToBorder);

        return new Leg(
                start,
                CardinalDirection.EAST,
                availableSteps,
                terminator(start, EAST, availableSteps));
    }

    private Leg realizeToSouth(Position start, int steps) {
        var distanceToBorder = length - start.y;

        int availableSteps = obstructions.stream()
                .filter(o -> o.position.x == start.x && o.position.y > start.y)
                .min(Comparator.comparingInt(o -> o.position.y))
                .map(obstruction -> obstruction.position.y - start.y - 1)
                .orElse(distanceToBorder);;

        return new Leg(
                start,
                CardinalDirection.SOUTH,
                availableSteps,
                terminator(start, SOUTH, availableSteps));
    }

    private Leg realizeToWest(Position start, int steps) {
        var distanceToBorder = start.x;

        int availableSteps = obstructions.stream()
                .filter(o -> o.position.y == start.y && o.position.x < start.x)
                .max(Comparator.comparingInt(o -> o.position.x))
                .map(obstruction -> start.x - obstruction.position.x - 1)
                .orElse(distanceToBorder);

        return new Leg(
                start,
                CardinalDirection.WEST,
                availableSteps,
                terminator(start, WEST, availableSteps));
    }

    Terminator terminator(Position start, CardinalDirection direction, int availableSteps) {
        // very first implementation, not interested in obstruction type yet
        return switch (direction) {
            case NORTH -> start.y == availableSteps ? BORDER : OBSTRUCTION;
            case EAST -> start.x + availableSteps + 1 == width ? BORDER : OBSTRUCTION;
            case SOUTH -> start.y + availableSteps == length ? BORDER : OBSTRUCTION;
            case WEST -> start.x == availableSteps ? BORDER : OBSTRUCTION;
        };
    }

    Size size() {
        return new Size(width, length);
    }

    List<Obstruction> obstructions() {
        return unmodifiableList(obstructions);
    }

    List<Guard> guards() {
        return guards;
    }

    private void initializeFrom(Reader input) {
        Room thisRoom = this;

        Map.codePoints(input).forEach(new IntConsumer() {
            @Override
            public void accept(int codePoint) {
                if (isBorder(codePoint)) {
                    increaseLength();
                    increaseWidthOnDemand();
                }
                else if (isObstruction(codePoint)) {
                    obstructions.add(new Obstruction((char) codePoint, new Position(currentX, length)));
                    ++currentX;
                }
                else if (isGuard(codePoint)) {
                    guards.add(new Guard(thisRoom, new Position(currentX, length), (char) codePoint));
                    ++currentX;
                }
                else {
                    ++currentX;
                }
            }
        });
    }

    private void increaseLength() {
        ++length;
    }

    private void increaseWidthOnDemand() {
        width = Math.max(width, currentX);
        currentX = 0;
    }

    boolean isObstruction(int type) {
        return type == '#';
    }

    boolean isGuard(int type) {
        return type == '^';
    }

    boolean isBorder(int type) {
        return type == '\n';
    }
}
