package day5.input;

import java.util.List;

public sealed interface PrintJobData {

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
