package day5;

import java.util.List;

record Update(List<Integer> pageNumbers) {

    Update(Integer ... pageNumbers) {
        this(List.of(pageNumbers));
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
