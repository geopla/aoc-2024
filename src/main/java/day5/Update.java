package day5;

import java.util.List;

record Update(List<Integer> pageNumbers) {

    Update(Integer ... pageNumbers) {
        this(List.of(pageNumbers));
    }

    List<Integer> pageNumbersBefore(int page) {
        return pageNumbers.stream()
                .takeWhile(i -> !i.equals(page))
                .toList();
    }
}
