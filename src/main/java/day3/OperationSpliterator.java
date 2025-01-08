package day3;

import java.util.Spliterator;
import java.util.function.Consumer;

public class OperationSpliterator implements Spliterator<Operation> {

    private final Spliterator<Character> sourceSpliterator;
    private final OperationDetector operationDetector;

    public OperationSpliterator(Spliterator<Character> sourceSpliterator, OperationDetector operationDetector) {
        this.sourceSpliterator = sourceSpliterator;
        this.operationDetector = operationDetector;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Operation> action) {

        while (sourceSpliterator.tryAdvance(operationDetector)) {


            // TODO foundOperation() will terminate the stream because it returns false on n-ary operations

            if (operationDetector.foundOperation()) {
                action.accept(operationDetector.operation());
                operationDetector.reset();

                return true;
            }
        }
        return false;
    }

    @Override
    public Spliterator<Operation> trySplit() {
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
