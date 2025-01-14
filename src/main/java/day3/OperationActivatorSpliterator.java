package day3;

import java.util.Spliterator;
import java.util.function.Consumer;

public class OperationActivatorSpliterator implements Spliterator<Operation> {

    private final Spliterator<Character> sourceSpliterator;
    private final OperationDetector operationDetector;

    private Operation conditional;

    public OperationActivatorSpliterator(Spliterator<Character> sourceSpliterator, OperationDetector operationDetector) {
        this.sourceSpliterator = sourceSpliterator;
        this.operationDetector = operationDetector;

        conditional = new Operation.Do();
    }

    @Override
    public boolean tryAdvance(Consumer<? super Operation> action) {

        while (sourceSpliterator.tryAdvance(operationDetector)) {

            if (operationDetector.foundOperationTokens()) {
                var operation = OperationFactory.create(operationDetector.currentTokens());

                if (operation.isConditional()) {
                    conditional = operation;
                }

                if (conditional instanceof Operation.Do
                        && OperationFactory.isComputing(operation)
                        && OperationFactory.isBinary(operation)) {

                    action.accept(operation);
                }
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
