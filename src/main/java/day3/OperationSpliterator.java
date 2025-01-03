package day3;

import java.util.Spliterator;
import java.util.function.Consumer;

public class OperationSpliterator implements Spliterator<Multiplier> {

    private final Spliterator<Character> sourceSpliterator;
    private final OperationDetector operationDetector;

    public OperationSpliterator(Spliterator<Character> sourceSpliterator, OperationDetector operationDetector) {
        this.sourceSpliterator = sourceSpliterator;
        this.operationDetector = operationDetector;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Multiplier> action) {

        while (sourceSpliterator.tryAdvance(operationDetector)) {
            if (operationDetector.foundOperation()) {
                action.accept(operationDetector.currentOperation());
                operationDetector.reset();

                return true;
            }
        }
        return false;
    }

    @Override
    public Spliterator<Multiplier> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        // TODO
        return 0;
    }

    @Override
    public int characteristics() {
        // TODO
        return 0;
    }
}
