package day6;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

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
        var stepsToBorder = start.y;

        //  using max() because of system screen coordinate system, y-coordinate advances top down (north to south)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheNorthFrom(start))
                .max(Comparator.comparingInt(o -> o.position.y))
                .map(stepsToNorthernObstruction(start))
                .orElse(stepsToBorder);

        return new Leg(
                start,
                NORTH,
                availableSteps,
                terminator(start, NORTH, availableSteps));
    }

    private static Predicate<Obstruction> obstructionsToTheNorthFrom(Position start) {
        return o -> o.position.x == start.x && o.position.y < start.y;
    }

    private static Function<Obstruction, Integer> stepsToNorthernObstruction(Position start) {
        return obstruction -> start.y - obstruction.position.y - 1;
    }

    private Leg realizeToEast(Position start, int steps) {
        var stepsToBorder = width - start.x - 1;

        // using min() because of screen coordinate system, x-coordinate advances left to right (west to east)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheEastFromStart(start))
                .min(Comparator.comparingInt(o -> o.position.x))
                .map(stepsToEasternObstruction(start))
                .orElse(stepsToBorder);

        return new Leg(
                start,
                CardinalDirection.EAST,
                availableSteps,
                terminator(start, EAST, availableSteps));
    }

    private static Predicate<Obstruction> obstructionsToTheEastFromStart(Position start) {
        return o -> o.position.y == start.y && o.position.x > start.x;
    }

    private static Function<Obstruction, Integer> stepsToEasternObstruction(Position start) {
        return obstruction -> obstruction.position.x - start.x - 1;
    }

    private Leg realizeToSouth(Position start, int steps) {
        var stepsToBorder = length - start.y;

        // using min() because of screen coordinate system, y-coordinate advances top down (north to south)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheSouthFrom(start))
                .min(Comparator.comparingInt(o -> o.position.y))
                .map(stepsToSouthernObstruction(start))
                .orElse(stepsToBorder);;

        return new Leg(
                start,
                CardinalDirection.SOUTH,
                availableSteps,
                terminator(start, SOUTH, availableSteps));
    }

    private static Predicate<Obstruction> obstructionsToTheSouthFrom(Position start) {
        return o -> o.position.x == start.x && o.position.y > start.y;
    }

    private static Function<Obstruction, Integer> stepsToSouthernObstruction(Position start) {
        return obstruction -> obstruction.position.y - start.y - 1;
    }

    private Leg realizeToWest(Position start, int steps) {
        var stepsToBorder = start.x;

        // using max() because of screen coordinate system, x-coordinate advances left to right (west to east)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheWestFrom(start))
                .max(Comparator.comparingInt(o1 -> o1.position.x))
                .map(stepsToWesternObstruction(start))
                .orElse(stepsToBorder);

        return new Leg(
                start,
                CardinalDirection.WEST,
                availableSteps,
                terminator(start, WEST, availableSteps));
    }

    private static Predicate<Obstruction> obstructionsToTheWestFrom(Position start) {
        return o -> o.position.y == start.y && o.position.x < start.x;
    }

    private static Function<Obstruction, Integer> stepsToWesternObstruction(Position start) {
        return obstruction -> start.x - obstruction.position.x - 1;
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
