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

    Stream<String> lines() {
        return lines.stream();
    }

    @Override
    public String toString() {
        return lines.stream().collect(Collectors.joining("\n"));
    }
}
