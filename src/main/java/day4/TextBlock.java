package day4;

import day4.Star.Ray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class TextBlock {

    private final List<String> lines;

    private TextBlock(Stream<String> lines) {
        this.lines = lines.toList();
    }

    public static TextBlock from(InputStream input) {
        return new TextBlock(new BufferedReader(new InputStreamReader(input)).lines());
    }

    public static TextBlock from(String input) {
        return new TextBlock(input.lines());
    }

    public Stream<Ray> rays() {
        return stars()
                .flatMap(Star::rays);
    }

    public Stream<Ray> rays(int maxLength) {
        return stars()
                .flatMap(star -> star.rays(maxLength));
    }

    public Stream<Star> stars() {
        return StreamSupport.stream(new Spliterator<>() {

            int x = 0;
            int y = 0;

            @Override
            public boolean tryAdvance(Consumer<? super Star> action) {
                action.accept(star(x, y));

                if (hasCharAt(x + 1, y)) {
                    ++x;
                    return true;
                }
                else if (hasCharAt(0, y + 1)) {
                    x = 0;
                    ++y;
                    return true;
                }

                return false;
            }

            @Override
            public Spliterator<Star> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return String.join("", lines).length();
            }

            @Override
            public int characteristics() {
                // TODO
                return 0;
            }
        }, false);
    }

    Star star() {
        return star(0, 0);
    }

    public Star star(int x, int y) {
        if (hasCharAt(x, y)) {
            return new Star(this, new Star.Position(x, y));
        }
        throw new IndexOutOfBoundsException("no star at position x=%d, y=%d".formatted(x, y));
    }

    boolean hasCharAt(int x, int y) {
        if (y < 0 || y > lines.size() - 1) {
            return false;
        }
        return x >= 0 && x <= lines.get(y).length() - 1;
    }

    char charAt(int x, int y) {
        String line = lines.get(y);
        return line.charAt(x);
    }

    // Stuff intended for debugging and testing

    Stream<String> lines() {
        return lines.stream();
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }
}
