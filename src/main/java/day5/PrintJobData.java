package day5;

import java.util.List;

sealed interface PrintJobData {

    String key();

    record PageOrderRule( int forerunner,
                          int successor) implements PrintJobData {
        @Override
        public String key() {
            return "page order rule";
        }
    };

    record Update(List<Integer> pageNumbers) implements PrintJobData {
        @Override
        public String key() {
            return "update";
        }
    }
}
