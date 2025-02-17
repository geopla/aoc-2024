package day6;

import day6.Lifecycle.Computed;
import day6.Lifecycle.Planned;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static day6.CardinalDirection.*;
import static day6.Terminator.BORDER;
import static day6.Terminator.OBSTRUCTION;
import static java.util.Collections.unmodifiableList;

class Room {

    record Size(int width, int depth) {
    }

    record Position(int x, int y) {
    }

    record Obstruction(char type, Position position) {
    }

    enum Tile {
        EMPTY {
            @Override
            char symbol() {
                return '.';
            }
        },
        OBSTRUCTION {
            @Override
            char symbol() {
                return '#';
            }
        },
        GUARD_FACING_NORTH {
            @Override
            char symbol() {
                return '^';
            }
        },
        GUARD_FACING_EAST {
            @Override
            char symbol() {
                return '>';
            }
        },
        GUARD_FACING_SOUTH {
            @Override
            char symbol() {
                return 'v';
            }
        },
        GUARD_FACING_WEST {
            @Override
            char symbol() {
                return '<';
            }
        }
        ;
        abstract char symbol();
    }

    private int width = 0;
    private int depth = 0;
    private int currentX = 0;

    private List<Obstruction> obstructions = new ArrayList<>();
    private List<Guard> guards = new ArrayList<>();

    public static Room from(Reader input) {
        return new Room(input);
    }

    public static Room from(String input) {
        return new Room(new StringReader(input));
    }

    private Room(Reader input) {
        initializeFrom(input);
    }

    Room( Size size, List<Obstruction> obstructions, List<Guard> guards) {
        this.width = size.width();
        this.depth = size.depth();
        this.obstructions = obstructions;
        this.guards = guards.stream()
                .map(guard -> new Guard(this, guard.startPosition(), guard.startFacing().symbol()))
                .toList();
    }

    Room withAdditional(List<Obstruction> obstructions) {
        List<Position> obstructionPositions = obstructions.stream()
                .map(Obstruction::position)
                .toList();

        var hitsAGuardPosition = guards.stream()
                .map(Guard::startPosition)
                .anyMatch(obstructionPositions::contains);

        if (hitsAGuardPosition) {
            throw new IllegalArgumentException("can't place obstruction on guards position");
        }

        var allObstructions = new ArrayList<>(this.obstructions);
        allObstructions.addAll(obstructions);

        return new Room(new Size(width, depth), allObstructions, this.guards);
    }

    Leg<Computed> realize(Leg<Planned> legPlanned) {
        return switch (legPlanned.direction()) {
            case NORTH -> realizeToNorth(legPlanned.start(), legPlanned.steps());
            case EAST -> realizeToEast(legPlanned.start(), legPlanned.steps());
            case SOUTH -> realizeToSouth(legPlanned.start(), legPlanned.steps());
            case WEST -> realizeToWest(legPlanned.start(), legPlanned.steps());
        };
    }

    private Leg<Computed> realizeToNorth(Position start, int steps) {
        var stepsToBorder = start.y;

        //  using max() because of system screen coordinate system, y-coordinate advances top down (north to south)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheNorthFrom(start))
                .max(Comparator.comparingInt(o -> o.position.y))
                .map(stepsToNorthernObstruction(start))
                .orElse(stepsToBorder);

        return new Leg<>(
                start,
                NORTH,
                availableSteps,
                new Computed(terminator(start, NORTH, availableSteps)));
    }

    private static Predicate<Obstruction> obstructionsToTheNorthFrom(Position start) {
        return o -> o.position.x == start.x && o.position.y < start.y;
    }

    private static Function<Obstruction, Integer> stepsToNorthernObstruction(Position start) {
        return obstruction -> start.y - obstruction.position.y - 1;
    }

    private Leg<Computed> realizeToEast(Position start, int steps) {
        var stepsToBorder = width - start.x - 1;

        // using min() because of screen coordinate system, x-coordinate advances left to right (west to east)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheEastFromStart(start))
                .min(Comparator.comparingInt(o -> o.position.x))
                .map(stepsToEasternObstruction(start))
                .orElse(stepsToBorder);

        return new Leg<>(
                start,
                CardinalDirection.EAST,
                availableSteps,
                new Computed(terminator(start, EAST, availableSteps)));
    }

    private static Predicate<Obstruction> obstructionsToTheEastFromStart(Position start) {
        return o -> o.position.y == start.y && o.position.x > start.x;
    }

    private static Function<Obstruction, Integer> stepsToEasternObstruction(Position start) {
        return obstruction -> obstruction.position.x - start.x - 1;
    }

    private Leg<Computed> realizeToSouth(Position start, int steps) {
        var stepsToBorder = depth - start.y - 1;

        // using min() because of screen coordinate system, y-coordinate advances top down (north to south)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheSouthFrom(start))
                .min(Comparator.comparingInt(o -> o.position.y))
                .map(stepsToSouthernObstruction(start))
                .orElse(stepsToBorder);
        ;

        return new Leg<>(
                start,
                CardinalDirection.SOUTH,
                availableSteps,
                new Computed(terminator(start, SOUTH, availableSteps)));
    }

    private static Predicate<Obstruction> obstructionsToTheSouthFrom(Position start) {
        return o -> o.position.x == start.x && o.position.y > start.y;
    }

    private static Function<Obstruction, Integer> stepsToSouthernObstruction(Position start) {
        return obstruction -> obstruction.position.y - start.y - 1;
    }

    private Leg<Computed> realizeToWest(Position start, int steps) {
        var stepsToBorder = start.x;

        // using max() because of screen coordinate system, x-coordinate advances left to right (west to east)
        int availableSteps = obstructions.stream()
                .filter(obstructionsToTheWestFrom(start))
                .max(Comparator.comparingInt(o1 -> o1.position.x))
                .map(stepsToWesternObstruction(start))
                .orElse(stepsToBorder);

        return new Leg<>(
                start,
                CardinalDirection.WEST,
                availableSteps,
                new Computed(terminator(start, WEST, availableSteps)));
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
            case SOUTH -> start.y + availableSteps + 1 == depth ? BORDER : OBSTRUCTION;
            case WEST -> start.x == availableSteps ? BORDER : OBSTRUCTION;
        };
    }

    Size size() {
        return new Size(width, depth);
    }

    List<Obstruction> obstructions() {
        return unmodifiableList(obstructions);
    }

    List<Guard> guards() {
        return guards;
    }

    Guard firstGuard() {
        return guards.getFirst();
    }

    private void initializeFrom(Reader input) {
        // dirty trick to make it accessible to the consumer
        final int[] lastCodePoint = new int[1];

        Map.codePoints(input).forEach(codePoint -> {
            lastCodePoint[0] = codePoint;

            Optional<Tile> tile = tileFrom(codePoint);

            if (tile.isPresent()) {
                evaluate(tile.get());
            }
            else {
                evaluateNonTile(codePoint);
            }
        });

        // handle missing new line (EOF when reading from file or closing """ on same linen when reading from text block)
        if (!isEndOfLine(lastCodePoint[0])) {
            increaseDepth();
        }
    }

    void evaluate(Tile tile) {
        switch (tile) {
            case EMPTY -> {
                ++currentX;
            }
            case OBSTRUCTION -> {
                obstructions.add(new Obstruction(Tile.OBSTRUCTION.symbol(), new Position(currentX, depth)));
                ++currentX;
            }
            case GUARD_FACING_NORTH -> {
                guards.add(new Guard(this, new Position(currentX, depth), Tile.GUARD_FACING_NORTH.symbol()));
                ++currentX;
            }
            case GUARD_FACING_EAST -> {
                guards.add(new Guard(this, new Position(currentX, depth), Tile.GUARD_FACING_EAST.symbol()));
                ++currentX;
            }
            case GUARD_FACING_SOUTH -> {
                guards.add(new Guard(this, new Position(currentX, depth), Tile.GUARD_FACING_SOUTH.symbol()));
                ++currentX;
            }
            case GUARD_FACING_WEST -> {
                guards.add(new Guard(this, new Position(currentX, depth), Tile.GUARD_FACING_WEST.symbol()));
                ++currentX;
            }
        }
    }

    static Optional<Tile> tileFrom(int codePoint) {
        return switch ((char) codePoint) {
            case '.', 'O', '|', '+', '-' -> Optional.of(Tile.EMPTY);
            case '#' -> Optional.of(Tile.OBSTRUCTION);
            case '^' -> Optional.of(Tile.GUARD_FACING_NORTH);
            case '>' -> Optional.of(Tile.GUARD_FACING_EAST);
            case 'v' -> Optional.of(Tile.GUARD_FACING_SOUTH);
            case '<' -> Optional.of(Tile.GUARD_FACING_WEST);

            default -> Optional.empty();
        };
    }

    void evaluateNonTile(int codePoint) {
        if (isEndOfLine(codePoint)) {
            increaseWidthToMax();
            increaseDepth();
        }
    }

    private static boolean isEndOfLine(int codePoint) {
        // ignoring '\r' on WinDOS systems works just fine
        return codePoint == '\n';
    }

    private void increaseDepth() {
        ++depth;
    }

    private void increaseWidthToMax() {
        width = Math.max(width, currentX);
        currentX = 0;
    }
}
