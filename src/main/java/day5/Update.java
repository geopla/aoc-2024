package day5;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

record Update(List<Integer> pageNumbers) {

    Update(Integer ... pageNumbers) {
        this(List.of(pageNumbers));
    }

    Optional<Integer> firstPage() {
        return pageNumbers.isEmpty() ? Optional.empty() : Optional.of(pageNumbers.getFirst());
    }

    Optional<Integer> nextPage(Integer page) {
        int indexOfPage = IntStream.range(0, pageNumbers.size())
                .filter(index -> pageNumbers.get(index).equals(page))
                .findFirst()
                .orElse(-1);

        if (0 <= indexOfPage && indexOfPage < pageNumbers.size() - 1) {
            return Optional.of(pageNumbers.get(indexOfPage + 1));
        }

        return Optional.empty();
    }

    Update createUpdateWithPageMovedBeforeSuccessors(Integer page, List<Integer> successors) {

        if (! successors.isEmpty()) {
            int indexOfFirstOccurrenceOutOfSuccessors = indexOfFirstOccurrenceOutOf(successors);
            ArrayList<Integer> workingCopy = new ArrayList<>(pageNumbers);

            if (workingCopy.remove(page)) {
                workingCopy.add(indexOfFirstOccurrenceOutOfSuccessors, page);

                return new Update(workingCopy);
            }
        }

        return this;
    }

    int indexOfFirstOccurrenceOutOf(List<Integer> pageNumberSubset) {
        return IntStream.range(0, pageNumbers.size())
                .filter(index -> pageNumberSubset.contains(pageNumbers.get(index)))
                .findFirst()
                .orElse(-1);
    }

    List<Integer> pageNumbersBefore(int page) {
        if (pageNumbers.contains(page)) {
            return pageNumbers.stream()
                    .takeWhile(i -> !i.equals(page))
                    .toList();
        }
        else {
            // this is essential for validating an update
            return List.of();
        }
    }

    int middlePageNumber() {
        if (isUneven(pageNumbers.size())) {
            int middlePagePosition = (pageNumbers.size() - 1) / 2;

            return pageNumbers.get(middlePagePosition);
        }

        throw new IllegalArgumentException("can't compute middle page number of even page numbers");
    }

    private boolean isUneven(int length) {
        return length % 2 == 1;
    }
}
