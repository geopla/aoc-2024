package day3;

import java.util.List;

sealed interface Operation {

    Integer apply();

    record Multiplier(List<String> factors) implements Operation {

        public Multiplier(String x, String y) {
            this(List.of(x, y));
        }

        @Override
        public Integer apply() {
            return factors.stream()
                    .map(Integer::parseInt)
                    .reduce((x, y) -> x * y)
                    .orElse(0);
        }
    }
}
