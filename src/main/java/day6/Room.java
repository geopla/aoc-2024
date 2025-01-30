package day6;

import java.io.Reader;
import java.util.function.IntConsumer;

class Room {

    private int length = 0;
    private int width = 0;
    int currentLineWidth = 0;

    record Size(int width, int length) { }

    private Size size;

    public Room(Reader input) {
        initializeFrom(input);
    }

    private void initializeFrom(Reader input) {
        Map.codePoints(input).forEach(new IntConsumer() {
            @Override
            public void accept(int codePoint) {
                switch ((char) codePoint) {
                    case '\n' -> {
                        increaseLength();
                        increaseWidthOnDemand();
                    }
                    default -> ++currentLineWidth;
                }
            }
        });
    }

    private void increaseLength() {
        ++length;
    }

    private void increaseWidthOnDemand() {
        width = Math.max(width, currentLineWidth);
        currentLineWidth = 0;
    }

    public Size size() {
        return new Size(width, length);
    }


}
