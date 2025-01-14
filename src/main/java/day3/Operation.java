package day3;

import java.util.List;

sealed interface Operation {

    default boolean isConditional() { return false; }

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
    };

    record Do() implements Operation {

        public Integer apply() {
            return 0;
        }

        @Override
        public boolean isConditional() {
            return true;
        }
    }

    record Dont() implements Operation {

        public Integer apply() {
            return 0;
        }

        @Override
        public boolean isConditional() {
            return true;
        }
    }

    record Unknown(List<String> operands) implements Operation {

        @Override
        public Integer apply() {
            throw new UnsupportedOperationException("unknown operation can't compute anything");
        }
    }
}
