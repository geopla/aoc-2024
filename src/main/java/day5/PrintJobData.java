package day5;

import java.util.List;

sealed interface PrintJobData {

    record PageOrderRule( int forerunner,
                          int successor) implements PrintJobData { };

    record Update(List<Integer> pageNumbers) implements PrintJobData { }
}
