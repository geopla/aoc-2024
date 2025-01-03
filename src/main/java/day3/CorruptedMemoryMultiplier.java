package day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import java.util.Spliterator;
import java.util.function.Consumer;

public class CorruptedMemoryMultiplier {

    static int multipliersSumFrom(String resourceNamme) {

        try (InputStream resourceAsStream = CorruptedMemoryMultiplier.class.getResourceAsStream(resourceNamme)) {
            return multipliersSumFrom(toCharacterStream(resourceAsStream));
        } catch (IOException e) {
            throw new RuntimeException("failed to read resource %s".formatted(resourceNamme), e);
        }
    }

    static int multipliersSumFrom(Stream<Character> memory) {

        return multipliersFrom(memory)
                .map(Multiplier::apply)
                .mapToInt(Integer::intValue)
                .sum();
    }

    static Stream<Multiplier> multipliersFrom(Stream<Character> memory) {
        var operationDetector = new OperationDetector();
        var operationSpliterator = new OperationSpliterator(memory.spliterator(), operationDetector);

        return StreamSupport.stream(operationSpliterator, false);
    }

    static Stream<Character> toCharacterStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return StreamSupport.stream(new Spliterator<Character>() {

            @Override
            public boolean tryAdvance(Consumer<? super Character> action) {
                try {
                    int c = reader.read();

                    if (c != -1) {
                        action.accept((char) c);
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException("failed to read from input stream", e);
                }
            }

            @Override
            public Spliterator<Character> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }, false);
    }

}
