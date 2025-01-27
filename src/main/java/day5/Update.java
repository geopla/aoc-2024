package day5;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

record Update(List<Integer> pageNumbers) {

    Update(Integer ... pageNumbers) {
        this(List.of(pageNumbers));
    }

    Update createUpdateWithMovedPage(int page, int index) {
        ArrayList<Integer> workingCopy = new ArrayList<>(pageNumbers);

        if (workingCopy.remove(Integer.valueOf(page))) {
            workingCopy.add(index, page);

            return new Update(workingCopy);
        }
        else {
            return this;
        }
    }

    int indexOfFirstOccurrenceOutOf(Set<Integer> pageNumberSubset) {
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
