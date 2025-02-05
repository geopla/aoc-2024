package day6;

sealed interface Lifecycle {
    record Planned() implements Lifecycle { }
    record Computed(Terminator terminator) implements Lifecycle { }
}

