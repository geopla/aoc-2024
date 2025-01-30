package day6;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.PrimitiveIterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

class Map {

    static IntStream codePoints(Reader reader) {
        return StreamSupport.intStream(
                Spliterators.spliteratorUnknownSize(new ReaderIterator(reader), 0),
                false);
    }

    private static class ReaderIterator implements PrimitiveIterator.OfInt {
        private final Reader reader;
        private int nextChar;

        public ReaderIterator(Reader reader) {
            this.reader = reader;
            this.nextChar = readNextChar();
        }

        private int readNextChar() {
            try {
                return reader.read();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public boolean hasNext() {
            return nextChar != -1;
        }

        @Override
        public int nextInt() {
            int currentChar = nextChar;
            nextChar = readNextChar();
            return currentChar;
        }
    }
}
