package day4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TextBlock {

    private List<String> lines;

    private TextBlock(Stream<String> lines) {
        this.lines = lines.collect(Collectors.toUnmodifiableList());
    }

    static TextBlock from(InputStream input) {
        return new TextBlock(new BufferedReader(new InputStreamReader(input)).lines());
    }

    static TextBlock from(String input) {
        return new TextBlock(input.lines());
    }

    Star star() {
        return new Star(this, new Star.Position(0, 0));
    }

    boolean hasCharAt(int x, int y) {

        if (y < 0 || y > lines.size() - 1) {
            return false;
        }
        if (x < 0 || x > lines.get(y).length() - 1) {
            return false;
        }
        return true;
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
        return lines.stream().collect(Collectors.joining("\n"));
    }
}
