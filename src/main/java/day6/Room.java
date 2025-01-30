package day6;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import static java.util.Collections.unmodifiableList;

class Room {

    private int length = 0;
    private int width = 0;

    private int currentX = 0;

    record Size(int width, int length) { }

    record Position(int x, int y) { }

    record Obstruction(char type, Position position) { }

    record Guard(char facing, Position position) { }

    private Size size;

    private List<Obstruction> obstructions = new ArrayList<>();

    private List<Guard> guards = new ArrayList<>();
    public Room(Reader input) {
        initializeFrom(input);
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
                    guards.add(new Guard((char) codePoint, new Position(currentX, length)));
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
