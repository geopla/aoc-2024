package daytwo;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A wrapper to extract a marker element from a source collection.
 * <p>
 * Finds a marker element in a source collection without consuming the source collection. This class
 * is meant to applied with streams, in scenarios where a marker element existing in the stream
 * is used for processing of the whole stream.
 * </p><p>
 * Instead of duplicating the stream for searching a marker element first and then processing the
 * duplicated stream, this class stores the elements read in the search step. When processing the
 * stream with this <code>MarkerSpliterator</code> the whole source stream is provided. The marker element,
 * if present in the source stream, can be queried from this instance.
 * </p>
 */
public class MarkerSpliterator<T> implements Spliterator<T> {

    private final Spliterator<T> sourceSpliterator;
    private final LinkedList<T> beforeMarkerElements;
    private final Predicate<T> markerPredicate;
    private T marker;

    public MarkerSpliterator(Spliterator<T> sourceSpliterator, Predicate<T> markerPredicate) {
        this.sourceSpliterator = sourceSpliterator;
        beforeMarkerElements = new LinkedList<>();
        this.markerPredicate = markerPredicate;
    }

    public Optional<T> marker() {
        if (marker == null) {
            while (marker == null && sourceSpliterator.tryAdvance(beforeMarkerElements::add)) {
                if (markerPredicate.test(beforeMarkerElements.getLast())) {
                    marker = beforeMarkerElements.getLast();
                }
            }
        }
        return Optional.ofNullable(marker);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        marker();
        T beforeMarker = beforeMarkerElements.pollFirst();

        if (beforeMarker != null) {
            action.accept(beforeMarker);
            return true;
        }
        return sourceSpliterator.tryAdvance(action);
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return sourceSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        // TODO not sure if all characteristics from the source spliterator are still applicable
        return sourceSpliterator.characteristics();
    }
}
